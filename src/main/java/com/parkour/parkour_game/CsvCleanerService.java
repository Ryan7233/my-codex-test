package com.parkour.parkour_game;

import org.mapdb.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Service class for cleaning CSV files using off-heap MapDB storage.
 */
public class CsvCleanerService {
    private static final int BUFFER_SIZE = 16 * 1024;

    /**
     * Replace single quotes with escaped quotes so standard CSV parsers can
     * handle embedded quotes.
     */
    public static String cleanLine(String line) {
        if (line == null) {
            return "";
        }
        return line.replaceAll("(?<!\")\"(?!\")", "\"\"");
    }

    /**
     * Clean the input CSV file and write the result to the output file.
     */
    public static void cleanFile(String inputFile, String outputFile) throws IOException {
        DB db = DBMaker.tempFileDB()
                .fileMmapEnableIfSupported()
                .make();

        HTreeMap<Long, String> map = db
                .hashMap("lines", Serializer.LONG, Serializer.STRING)
                .create();

        long index = 0;
        long written = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8),
                BUFFER_SIZE);
             BufferedWriter bw = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8),
                     BUFFER_SIZE)) {
            String raw;
            StringBuilder record = new StringBuilder();
            boolean inQuotes = false;

            while ((raw = br.readLine()) != null) {
                if (record.length() > 0) {
                    record.append('\n');
                }
                record.append(raw);
                int quoteCount = countUnescapedQuotes(raw);
                if (quoteCount % 2 == 1) {
                    inQuotes = !inQuotes;
                }
                if (!inQuotes) {
                    String cleaned = cleanLine(record.toString());
                    map.put(index++, cleaned);
                    record.setLength(0);
                    if (index % 100000 == 0) {
                        flushChunk(map, bw, written, index);
                        written = index;
                        System.out.println("Processed " + written + " lines...");
                    }
                }
            }

            if (record.length() > 0) {
                String cleaned = cleanLine(record.toString());
                map.put(index++, cleaned);
            }

            flushChunk(map, bw, written, index);
        } finally {
            db.close();
        }
    }

    private static int countUnescapedQuotes(String line) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '"') {
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    i++; // skip escaped quote
                } else {
                    count++;
                }
            }
        }
        return count;
    }

    private static void flushChunk(HTreeMap<Long, String> map, BufferedWriter bw,
                                   long start, long end) throws IOException {
        for (long i = start; i < end; i++) {
            String val = map.remove(i);
            if (val != null) {
                bw.write(val);
            }
            bw.newLine();
        }
        bw.flush();
    }
}