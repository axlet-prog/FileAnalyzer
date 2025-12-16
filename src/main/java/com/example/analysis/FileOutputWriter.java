package com.example.analysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileOutputWriter implements AutoCloseable, OutputWriter {

    private static final String FILE_INTEGERS_NAME_DEFAULT = "integers.txt";
    private static final String FILE_DOUBLES_NAME_DEFAULT = "floats.txt";
    private static final String FILE_STRINGS_NAME_DEFAULT = "strings.txt";

    private BufferedWriter intWriter;
    private BufferedWriter floatWriter;
    private BufferedWriter stringWriter;

    private final AnalyzeOptions options;

    public FileOutputWriter(AnalyzeOptions options) {
        this.options = options;
    }

    @Override
    public void writeInteger(String line) {
        try {
            if (intWriter == null) {
                intWriter = createWriter(FILE_INTEGERS_NAME_DEFAULT);
            }
            intWriter.write(line);
            intWriter.newLine();
        } catch (IOException e) {
            System.out.println("IO writing int results");
        }
    }

    @Override
    public void writeFloat(String line) {
        try {
            if (floatWriter == null) {
                floatWriter = createWriter(FILE_DOUBLES_NAME_DEFAULT);
            }
            floatWriter.write(line);
            floatWriter.newLine();
        } catch (IOException e) {
            System.out.println("IO writing float results");
        }
    }

    @Override
    public void writeString(String line) {
        try {
            if (stringWriter == null) {
                stringWriter = createWriter(FILE_STRINGS_NAME_DEFAULT);
            }
            stringWriter.write(line);
            stringWriter.newLine();
        } catch (IOException e) {
            System.out.println("IO writing string results");
        }
    }

    @Override
    public void close() {
        closeWriter(intWriter);
        closeWriter(floatWriter);
        closeWriter(stringWriter);
    }

    private void closeWriter(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println("IO exception during closing writer");
            }
        }
    }

    private BufferedWriter createWriter(String name) throws IOException {
        String prefix = options.getTitlePrefix() == null ? "" : options.getTitlePrefix();

        boolean appendFlag = options.isAppendToExistingFiles();
        Path directoryPath = options.getDirectoryPath();

        Path fileFilePath = Paths.get(prefix + name);

        if (directoryPath != null) {
            if (!Files.exists(directoryPath)) {
                try {
                    Files.createDirectories(directoryPath);
                } catch (IOException e) {
                    System.out.println("Unable to create directory: " + directoryPath.toFile().getAbsolutePath());
                }
            }
            fileFilePath = directoryPath.resolve(fileFilePath);
        }

        return Files.newBufferedWriter(fileFilePath, getStandardOpenOptions(appendFlag));
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
}
