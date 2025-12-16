package com.example.analysis;

public class WriterUtil {

    public static void writeResults(AnalyzeOptions options, Result result) {
        if (options.isFullStatistics()) {
            printFullStatistics(result);
        } else if (options.isShortStatistics()) {
            printShortStatistics(result);
        }
    }

    private static void printFullStatistics(Result result) {
        if (result.intsSize() > 0) {
            System.out.printf(
                """
                    [Integers]
                        Count: %d
                        Min:   %d
                        Max:   %d
                        Sum:   %d
                        Avg:   %s
                    """,
                result.intsSize(),
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

        if (result.doublesSize() > 0) {
            System.out.printf(
                """
                    [Doubles]
                        Count: %d
                        Min:   %s
                        Max:   %s
                        Sum:   %s
                        Avg:   %s
                    """,
                result.doublesSize(),
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

        if (result.stringsSize() > 0) {
            System.out.printf(
                """
                    [Strings]
                        Count: %d
                        Min Length: %d
                        Max Length: %d
                    """,
                result.stringsSize(),
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
            result.intsSize(),
            result.doublesSize(),
            result.stringsSize()
        );
    }
}