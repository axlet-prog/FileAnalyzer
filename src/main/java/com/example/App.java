package com.example;

import picocli.CommandLine;


public class App {

    public static void main(String[] args) {
        AnalyzeOptions options = new AnalyzeOptions();
        CommandLine cmd = new CommandLine(options);

        try {
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

        } catch (Exception e) {
            System.out.println("Exception");
        }

        Analyzer analyzer = new Analyzer(options);
        Result res = analyzer.analyze();
        System.out.println(res);
    }
}
