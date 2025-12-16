package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.analysis.AnalyzeOptions;
import com.example.analysis.Result;
import com.example.analysis.WriterUtil;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class WriterUtilTest {

    @TempDir
    Path tempDir;

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
            .integers(List.of("10", "20"))
            .minInt(10L)
            .maxInt(20L)
            .sumInt(BigInteger.valueOf(30))
            .avgInt(15.0)

            .doubles(List.of("1.1", "2.2"))
            .minDouble(1.1)
            .maxDouble(2.2)
            .sumDouble(3.3)
            .avgDouble(1.65)

            .strings(List.of("foo", "bar"))
            .minLength(3L)
            .maxLength(3L)
            .build();
    }

    @Test
    void testWriteFilesToDirectory() throws Exception {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setDirectoryPath(tempDir);

        Result result = createFullResult();

        WriterUtil.writeResults(options, result);

        Path integersFile = tempDir.resolve("integers.txt");
        Path floatsFile = tempDir.resolve("floats.txt");
        Path stringsFile = tempDir.resolve("strings.txt");

        assertTrue(Files.exists(integersFile));
        assertTrue(Files.exists(floatsFile));
        assertTrue(Files.exists(stringsFile));

        List<String> intLines = Files.readAllLines(integersFile);
        assertEquals(List.of("10", "20"), intLines);

        List<String> floatLines = Files.readAllLines(floatsFile);
        assertEquals(List.of("1.1", "2.2"), floatLines);
    }

    @Test
    void testFilePrefix() {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setDirectoryPath(tempDir);
        options.setTitlePrefix("sample_");

        Result result = createFullResult();

        WriterUtil.writeResults(options, result);

        assertTrue(Files.exists(tempDir.resolve("sample_integers.txt")));
        assertTrue(Files.exists(tempDir.resolve("sample_floats.txt")));
        assertTrue(Files.exists(tempDir.resolve("sample_strings.txt")));
    }

    @Test
    void testAppendToFile() throws Exception {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setDirectoryPath(tempDir);
        options.setAppendToExistingFiles(true);

        Result result1 = Result.builder().strings(List.of("Line 1")).integers(List.of()).doubles(List.of()).build();
        Result result2 = Result.builder().strings(List.of("Line 2")).integers(List.of()).doubles(List.of()).build();

        WriterUtil.writeResults(options, result1);
        WriterUtil.writeResults(options, result2);

        Path stringFile = tempDir.resolve("strings.txt");
        List<String> lines = Files.readAllLines(stringFile);

        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    @Test
    void testOverwriteByDefault() throws Exception {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setDirectoryPath(tempDir);
        options.setAppendToExistingFiles(false);

        Result result1 = Result.builder().strings(List.of("Old Data")).integers(List.of()).doubles(List.of()).build();
        Result result2 = Result.builder().strings(List.of("New Data")).integers(List.of()).doubles(List.of()).build();

        WriterUtil.writeResults(options, result1);
        WriterUtil.writeResults(options, result2);

        Path stringFile = tempDir.resolve("strings.txt");
        List<String> lines = Files.readAllLines(stringFile);

        assertEquals(1, lines.size());
        assertEquals("New Data", lines.get(0));
    }

    @Test
    void testShortStatisticsOutput() {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setDirectoryPath(tempDir);
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
        options.setDirectoryPath(tempDir);
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
    void testDoNotWriteEmptyLists() {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setDirectoryPath(tempDir);

        Result result = Result.builder()
            .strings(List.of("test"))
            .integers(List.of())
            .doubles(List.of())
            .build();

        WriterUtil.writeResults(options, result);

        assertTrue(Files.exists(tempDir.resolve("strings.txt")));
        assertFalse(Files.exists(tempDir.resolve("integers.txt")));
        assertFalse(Files.exists(tempDir.resolve("floats.txt")));
    }
}
