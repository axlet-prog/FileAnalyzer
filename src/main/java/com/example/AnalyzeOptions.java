package com.example;


import java.io.File;
import java.nio.file.Path;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "file-analyzer",
                     mixinStandardHelpOptions = true,
                     version = "1.0",
                     description = "Analyzes a list of files and generates statistics.")
@Getter
@Setter
public class AnalyzeOptions {

    @Option(names = { "-o" }, description = "directory to save result files")
    private Path directoryPath;

    @Option(names = { "-p", "--prefix" }, description = "prefix of saved files")
    private String titlePrefix;

    @Option(names = "-a", description = "append to existing files")
    private boolean appendToExistingFiles;

    @Option(names = "-s", description = "short statistics")
    private boolean shortStatistics;

    @Option(names = "-f", description = "full statistics")
    private boolean fullStatistics;

    @Parameters(index = "0..*", description = "Input files to analyze")
    private List<File> inputFiles;
}
