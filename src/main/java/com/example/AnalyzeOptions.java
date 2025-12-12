package com.example;


import java.io.File;
import picocli.CommandLine;
import picocli.CommandLine.Option;

@CommandLine.Command(name = "file-analyzer",
                     mixinStandardHelpOptions = true,
                     version = "1.0",
                     description = "Analyzes a list of files and generates statistics.")
public class AnalyzeOptions {

    @Option(names = "-a", description = "some argument")
    public String a;

    @Option(names = { "-f", "--file" }, paramLabel = "ARCHIVE", description = "the archive file")
    public File archive;

    @Option(names = "-k")
    public int x;
}
