package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.analysis.AnalyzeOptions;
import com.example.analysis.FileOutputWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class FileOutputWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void testWriteFilesToDirectory() throws Exception {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setDirectoryPath(tempDir);

        try (FileOutputWriter writer = new FileOutputWriter(options)) {
            writer.writeInteger("10");
            writer.writeInteger("20");
            writer.writeFloat("1.1");
            writer.writeString("foo");
        }

        Path integersFile = tempDir.resolve("integers.txt");
        Path floatsFile = tempDir.resolve("floats.txt");
        Path stringsFile = tempDir.resolve("strings.txt");

        assertTrue(Files.exists(integersFile));
        assertTrue(Files.exists(floatsFile));
        assertTrue(Files.exists(stringsFile));

        assertEquals(List.of("10", "20"), Files.readAllLines(integersFile));
        assertEquals(List.of("1.1"), Files.readAllLines(floatsFile));
        assertEquals(List.of("foo"), Files.readAllLines(stringsFile));
    }

    @Test
    void testFilePrefix() {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setDirectoryPath(tempDir);
        options.setTitlePrefix("sample_");

        try (FileOutputWriter writer = new FileOutputWriter(options)) {
            writer.writeString("content");
            writer.writeInteger("1");
            writer.writeFloat("1.1");
        }

        assertTrue(Files.exists(tempDir.resolve("sample_integers.txt")));
        assertTrue(Files.exists(tempDir.resolve("sample_floats.txt")));
        assertTrue(Files.exists(tempDir.resolve("sample_strings.txt")));
    }

    @Test
    void testAppendToFile() throws Exception {
        AnalyzeOptions options = new AnalyzeOptions();
        options.setDirectoryPath(tempDir);
        options.setAppendToExistingFiles(true); // Включаем append

        try (FileOutputWriter writer = new FileOutputWriter(options)) {
            writer.writeString("Line 1");
        }

        try (FileOutputWriter writer = new FileOutputWriter(options)) {
            writer.writeString("Line 2");
        }

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

        try (FileOutputWriter writer = new FileOutputWriter(options)) {
            writer.writeString("Old Data");
        }

        Path stringFile = tempDir.resolve("strings.txt");
        List<String> lines = Files.readAllLines(stringFile);

        assertEquals(1, lines.size());
        assertEquals("Old Data", lines.get(0));

        try (FileOutputWriter writer = new FileOutputWriter(options)) {
            writer.writeString("New Data");
        }

        lines = Files.readAllLines(stringFile);

        assertEquals(1, lines.size());
        assertEquals("New Data", lines.get(0));
    }

    @Test
    void testLazyFileCreation() {

        AnalyzeOptions options = new AnalyzeOptions();
        options.setDirectoryPath(tempDir);

        try (FileOutputWriter writer = new FileOutputWriter(options)) {
            writer.writeString("Only String");
        }

        assertTrue(Files.exists(tempDir.resolve("strings.txt")));
        assertFalse(Files.exists(tempDir.resolve("integers.txt")));
        assertFalse(Files.exists(tempDir.resolve("floats.txt")));
    }
}
