package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Analyzer {

    private static final Pattern INT_PATTERN = Pattern.compile("^-?\\d+$");
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("^-?\\d+\\.?\\d+([eE][+-]?\\d+)?$");

    private final AnalyzeOptions options;
    private final List<String> strings = new ArrayList<>();
    private final List<Long> integers = new ArrayList<>();
    private final List<Double> doubles = new ArrayList<>();

    private Long minLong = Long.MAX_VALUE;
    private Long maxLong = Long.MIN_VALUE;
    private Long sumLong = 0L;

    private Double minDouble = Double.MAX_VALUE;
    private Double maxDouble = -Double.MAX_VALUE;
    private Double sumDouble = 0.0;

    private long maxLength = 0;
    private long minLength = Integer.MAX_VALUE;

    public Analyzer(AnalyzeOptions options) {
        this.options = options;
    }

    public Result analyze() {
        for (File file : options.getInputFiles()) {
            analyzeFile(file);
        }

        Result.ResultBuilder builder = Result.builder();

        if (!integers.isEmpty()) {
            double avgInt = (double) sumLong / integers.size();
            builder.integers(integers)
                .minInt(minLong)
                .maxInt(maxLong)
                .avgInt(avgInt)
            ;
        }

        if (!doubles.isEmpty()) {
            double avgDouble = sumDouble / doubles.size();
            builder.doubles(doubles)
                .minDouble(minDouble)
                .maxDouble(maxDouble)
                .avgDouble(avgDouble);
        }

        if (!strings.isEmpty()) {
            builder
                .strings(strings)
                .minLength(this.minLength)
                .maxLength(maxLength);
        }

        return builder.build();
    }

    private void analyzeFile(File file) {
        try (
            Reader reader = new FileReader(file, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader)
        ) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                if (line.isEmpty()) {
                    continue;
                }

                line = line.trim();
                if (INT_PATTERN.matcher(line).matches()) {
                    processInteger(line);
                } else if (DOUBLE_PATTERN.matcher(line).matches()) {
                    processDouble(line);
                } else {
                    processString(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getAbsoluteFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processInteger(String integerString) {
        long value = Long.parseLong(integerString);
        integers.add(value);
        maxLong = Math.max(value, maxLong);
        minLong = Math.min(value, minLong);
        sumLong += value;
    }

    private void processDouble(String doubleString) {
        double value = Double.parseDouble(doubleString);
        doubles.add(value);
        maxDouble = Math.max(value, maxDouble);
        minDouble = Math.min(value, minDouble);
        sumDouble += value;
    }

    private void processString(String str) {
        strings.add(str);
        int len = str.length();
        maxLength = Math.max(len, maxLength);
        minLength = Math.min(len, minLength);
    }
}