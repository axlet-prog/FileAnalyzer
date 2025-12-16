package com.example.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Analyzer {

    public static final Pattern INT_PATTERN = Pattern.compile("^-?\\d+$");
    public static final Pattern DOUBLE_PATTERN = Pattern.compile("^-?\\d+\\.?\\d+([eE][+-]?\\d+)?$");
    public static final Pattern DOUBLE_EXP_PATTERN = Pattern.compile("^-?\\d+[eE][+-]?\\d+$");

    private final Function<AnalyzeOptions, OutputWriter> writerFactory;

    public Analyzer() {
        this(FileOutputWriter::new);
    }

    public Analyzer(Function<AnalyzeOptions, OutputWriter> writerFactory) {
        this.writerFactory = writerFactory;
    }

    public Result analyzeFilesAndWrite(AnalyzeOptions op) {
        ResultCollector resCollector = new ResultCollector();
        try (OutputWriter ow = writerFactory.apply(op)) {
            for (File file : op.getInputFiles()) {
                processFile(file, ow, resCollector);
            }
        } catch (Exception e) {
            System.err.println("Exception during processing files");
        }

        return resCollector.buildResult();
    }

    private void processFile(File file, OutputWriter writer, ResultCollector collector) {
        try (
            Reader reader = new FileReader(file, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader)
        ) {
            analyzeContentAndWrite(bufferedReader, writer, collector);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getAbsoluteFile());
        } catch (IOException e) {
            System.err.println("Unable to process file: " + file.getAbsoluteFile());
        }
    }

    public void analyzeContentAndWrite(
        BufferedReader reader,
        OutputWriter writer,
        ResultCollector resCollector
    ) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                continue;
            }
            line = line.trim();
            try {
                if (INT_PATTERN.matcher(line).matches()) {
                    resCollector.processInteger(line);
                    writer.writeInteger(line);
                } else if (DOUBLE_PATTERN.matcher(line).matches() ||
                           DOUBLE_EXP_PATTERN.matcher(line).matches()
                ) {
                    resCollector.processDouble(line);
                    writer.writeFloat(line);
                } else {
                    resCollector.processString(line);
                    writer.writeString(line);
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Unable to process number: " + line + ". It is considered as string.");
                resCollector.processString(line);
                writer.writeString(line);
            }
        }
    }
}