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

        } catch (Exception e) {
            System.out.println("Exception");
        }

        System.out.println("params:");
        System.out.println(options.x);
        System.out.println(options.a);
        System.out.println(options.archive.toPath().toAbsolutePath());
    }


}
