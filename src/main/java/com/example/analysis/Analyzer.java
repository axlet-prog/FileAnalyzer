package com.example.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Analyzer {

    public static final Pattern INT_PATTERN = Pattern.compile("^-?\\d+$");
    public static final Pattern DOUBLE_PATTERN = Pattern.compile("^-?\\d+\\.?\\d+([eE][+-]?\\d+)?$");
    public static final Pattern DOUBLE_EXP_PATTERN = Pattern.compile("^-?\\d+[eE][+-]?\\d+$");

    private final List<String> strings = new ArrayList<>();
    private final List<String> integers = new ArrayList<>();
    private final List<String> doubles = new ArrayList<>();

    private Long minLong = Long.MAX_VALUE;
    private Long maxLong = Long.MIN_VALUE;
    private BigInteger sumLong = BigInteger.ZERO;

    private Double minDouble = Double.MAX_VALUE;
    private Double maxDouble = -Double.MAX_VALUE;
    private Double sumDouble = 0.0;

    private long maxLength = 0;
    private long minLength = Integer.MAX_VALUE;

    public Result buildResult() {
        Result.ResultBuilder builder = Result.builder();
        builder.integers(integers);
        if (!integers.isEmpty()) {
            BigDecimal sumDecimal = new BigDecimal(this.sumLong);
            BigDecimal countDecimal = BigDecimal.valueOf(integers.size());
            double avg = sumDecimal.divide(countDecimal, MathContext.DECIMAL64).doubleValue();
            builder
                .minInt(minLong)
                .maxInt(maxLong)
                .sumInt(sumLong)
                .avgInt(avg);
        }
        builder.doubles(doubles);
        if (!doubles.isEmpty()) {
            double avgDouble = sumDouble / doubles.size();
            builder.doubles(doubles)
                .minDouble(minDouble)
                .maxDouble(maxDouble)
                .sumDouble(sumDouble)
                .avgDouble(avgDouble);
        }
        builder.strings(strings);
        if (!strings.isEmpty()) {
            builder
                .strings(strings)
                .minLength(this.minLength)
                .maxLength(maxLength);
        }

        return builder.build();
    }

    public Result analyzeFiles(List<File> files) {
        for (File file : files) {
            analyzeFile(file);
        }

        return buildResult();
    }

    private void analyzeFile(File file) {
        try (
            Reader reader = new FileReader(file, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader)
        ) {
            analyzeContent(bufferedReader);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getAbsoluteFile());
        } catch (IOException e) {
            System.err.println("Unable to process file: " + file.getAbsoluteFile());
        }
    }

    public void analyzeContent(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                continue;
            }
            line = line.trim();
            try {
                if (INT_PATTERN.matcher(line).matches()) {
                    processInteger(line);
                } else if (DOUBLE_PATTERN.matcher(line).matches() ||
                           DOUBLE_EXP_PATTERN.matcher(line).matches()
                ) {
                    processDouble(line);
                } else {
                    processString(line);
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Unable to process number: " + line + ". It is considered as string.");
                processString(line);
            }
        }
    }

    private void processInteger(String integerString) {
        long value = Long.parseLong(integerString);
        integers.add(integerString);
        maxLong = Math.max(value, maxLong);
        minLong = Math.min(value, minLong);
        sumLong = sumLong.add(BigInteger.valueOf(value));
    }

    private void processDouble(String doubleString) {
        double value = Double.parseDouble(doubleString);
        doubles.add(doubleString);
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