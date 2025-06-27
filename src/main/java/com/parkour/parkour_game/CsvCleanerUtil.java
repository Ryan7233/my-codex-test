package com.parkour.parkour_game;

import org.mapdb.*;
import java.io.*;

/**
 * Utility methods for cleaning large CSV files using off-heap storage.
 */

public class CsvCleanerUtil {
    /**
     * Clean a single CSV record by escaping embedded quotes. The returned line
     * will contain doubled quotes so standard CSV parsers can handle fields
     * that contain special characters.
     */
    public static String cleanLine(String line) {
        if (line == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                sb.append("\"");
                sb.append("\"");
                inQuotes = !inQuotes;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Clean an input CSV file and write the result to {@code outputFile}. Data
     * is processed in streaming mode with an off-heap MapDB cache to avoid
     * loading everything into memory. Progress is printed every 100000 records.
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
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            String raw;
            StringBuilder record = new StringBuilder();
            boolean inQuotes = false;

            while ((raw = br.readLine()) != null) {
                if (record.length() > 0) {
                    record.append('\n');
                }
                record.append(raw);
                int quoteCount = countQuotes(raw);
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

    private static int countQuotes(String line) {
        int cnt = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '"') {
                cnt++;
            }
        }
        return cnt;
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