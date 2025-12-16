package com.example.analysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class WriterUtil {

    private static final String FILE_INTEGERS_NAME_DEFAULT = "integers.txt";
    private static final String FILE_DOUBLES_NAME_DEFAULT = "floats.txt";
    private static final String FILE_STRINGS_NAME_DEFAULT = "strings.txt";

    public static void writeResults(AnalyzeOptions options, Result result) {

        if (result.integers().isEmpty() && result.doubles().isEmpty() && result.strings().isEmpty()) {
            System.out.println("No results to write");
            return;
        }

        String prefix = options.getTitlePrefix() == null ? "" : options.getTitlePrefix();

        boolean appendFlag = options.isAppendToExistingFiles();
        Path directoryPath = options.getDirectoryPath();

        Path integerFilePath = Paths.get(prefix + FILE_INTEGERS_NAME_DEFAULT);
        Path floatsFilePath = Paths.get(prefix + FILE_DOUBLES_NAME_DEFAULT);
        Path stringFilePath = Paths.get(prefix + FILE_STRINGS_NAME_DEFAULT);

        if (directoryPath != null) {
            if (!Files.exists(directoryPath)) {
                try {
                    Files.createDirectories(directoryPath);
                } catch (IOException e) {
                    System.out.println("Unable to create directory: " + directoryPath.toFile().getAbsolutePath());
                }
            }
            integerFilePath = directoryPath.resolve(integerFilePath);
            floatsFilePath = directoryPath.resolve(floatsFilePath);
            stringFilePath = directoryPath.resolve(stringFilePath);
        }

        if (!result.integers().isEmpty()) {
            writeFile(integerFilePath, result.integers(), appendFlag);
        }
        if (!result.doubles().isEmpty()) {
            writeFile(floatsFilePath, result.doubles(), appendFlag);
        }

        if (!result.strings().isEmpty()) {
            writeFile(stringFilePath, result.strings(), appendFlag);
        }

        if (options.isFullStatistics()) {
            printFullStatistics(result);
        } else if (options.isShortStatistics()) {
            printShortStatistics(result);
        }
    }

    private static <T> void writeFile(Path filePath, List<T> data, boolean appendToExisting) {

        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                if (!Files.exists(filePath)) {
                    System.out.println("File could not be created" + filePath);
                }
            }
            StandardOpenOption[] openOptions = getStandardOpenOptions(appendToExisting);
            try (
                BufferedWriter bw = new BufferedWriter(Files.newBufferedWriter(filePath, openOptions))
            ) {
                for (T element : data) {
                    bw.write(element.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("IO writing results: " + filePath);
        }
    }

    private static StandardOpenOption[] getStandardOpenOptions(boolean appendToExisting) {
        StandardOpenOption[] openOptions;
        if (appendToExisting) {
            openOptions = new StandardOpenOption[]{
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND
            };
        } else {
            openOptions = new StandardOpenOption[]{
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
            };
        }
        return openOptions;
    }

    private static void printFullStatistics(Result result) {
        if (!result.integers().isEmpty()) {
            System.out.printf(
                """
                    [Integers]
                        Count: %d
                        Min:   %d
                        Max:   %d
                        Sum:   %d
                        Avg:   %s
                    """,
                result.integers().size(),
                result.minInt(),
                result.maxInt(),
                result.sumInt(),
                result.avgInt()
            );
        } else {
            System.out.println(
                """
                    [Integers]
                        No data
                    """);
        }

        if (!result.doubles().isEmpty()) {
            System.out.printf(
                """
                    [Doubles]
                        Count: %d
                        Min:   %s
                        Max:   %s
                        Sum:   %s
                        Avg:   %s
                    """,
                result.doubles().size(),
                result.minDouble(),
                result.maxDouble(),
                result.sumDouble(),
                result.avgDouble()
            );
        } else {
            System.out.println(
                """
                    [Doubles]
                        No data
                    """);
        }

        if (!result.strings().isEmpty()) {
            System.out.printf(
                """
                    [Strings]
                        Count: %d
                        Min Length: %d
                        Max Length: %d
                    """,
                result.strings().size(),
                result.minLength(),
                result.maxLength()
            );
        } else {
            System.out.println(
                """
                    [Strings]
                        No data
                    """);
        }
        System.out.println();
    }

    private static void printShortStatistics(Result result) {
        System.out.printf(
            """
                Integers: %d
                Doubles:  %d
                Strings:  %d
                %n""",
            result.integers().size(),
            result.doubles().size(),
            result.strings().size()
        );
    }
}