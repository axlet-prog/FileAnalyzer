package com.example;

import com.example.analysis.AnalyzeOptions;
import com.example.analysis.Analyzer;
import com.example.analysis.Result;
import com.example.analysis.WriterUtil;
import picocli.CommandLine;


public class App {

    public static void main(String[] args) {
        AnalyzeOptions options = new AnalyzeOptions();
        CommandLine cmd = new CommandLine(options);

        cmd.parseArgs(args);
        if (cmd.isUsageHelpRequested()) {
            cmd.usage(System.out);
            return;
        }

        if (cmd.isVersionHelpRequested()) {
            cmd.printVersionHelp(System.out);
            return;
        }

        if (options.getInputFiles().isEmpty()) {
            System.out.println("No files to analyze.");
            return;
        }

        Analyzer analyzer = new Analyzer();
        Result res = analyzer.analyzeFiles(options.getInputFiles());

        WriterUtil.writeResults(options, res);
    }
}
