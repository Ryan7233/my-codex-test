package com.parkour.parkour_game;

public class CsvCleanerMain {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java CsvCleanerMain <input.csv> <output.csv>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];
        try {
            CsvCleanerService.cleanFile(inputFile, outputFile);
            System.out.println("Cleaning completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}