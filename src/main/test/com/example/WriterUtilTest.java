package com.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.analysis.AnalyzeOptions;
import com.example.analysis.Result;
import com.example.analysis.WriterUtil;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WriterUtilTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    private Result createFullResult() {
        return Result.builder()
            .intsSize(2L)
            .minInt(10L)
            .maxInt(20L)
            .sumInt(BigInteger.valueOf(30))
            .avgInt(15.0)

            .doublesSize(2L)
            .minDouble(1.1)
            .maxDouble(2.2)
            .sumDouble(3.3)
            .avgDouble(1.65)

            .stringsSize(2L)
            .minLength(3L)
            .maxLength(3L)
            .build();
    }

    @Test
    void testShortStatisticsOutput() {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setShortStatistics(true);
        options.setFullStatistics(false);

        Result result = createFullResult();

        WriterUtil.writeResults(options, result);

        String consoleOutput = outContent.toString();

        assertTrue(consoleOutput.contains("Integers: 2"));
        assertTrue(consoleOutput.contains("Doubles:  2"));
        assertTrue(consoleOutput.contains("Strings:  2"));
        assertFalse(consoleOutput.contains("Min:"), "Вывод только краткой статистики");
    }

    @Test
    void testFullStatisticsOutput() {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setShortStatistics(true);
        options.setFullStatistics(true);

        Result result = createFullResult();

        WriterUtil.writeResults(options, result);

        String consoleOutput = outContent.toString();

        assertTrue(consoleOutput.contains("[Integers]"));
        assertTrue(consoleOutput.contains("Min:   10"));
        assertTrue(consoleOutput.contains("Avg:   15.0"));

        assertTrue(consoleOutput.contains("[Doubles]"));
        assertTrue(consoleOutput.contains("Max:   2.2"));

        assertTrue(consoleOutput.contains("[Strings]"));
        assertTrue(consoleOutput.contains("Max Length: 3"));
    }

    @Test
    void testNoDataOutput() {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setFullStatistics(true);

        Result result = Result.builder()
            .intsSize(0L)
            .doublesSize(0L)
            .stringsSize(0L)
            .build();

        WriterUtil.writeResults(options, result);
        String output = outContent.toString();

        assertFalse(output.contains("Min:"));
    }
}
