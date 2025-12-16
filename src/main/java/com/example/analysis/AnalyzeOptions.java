package com.example.analysis;


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
         version = "1.1",
         description = "Анализ списка файлов, фильтрация и вывод статистики.")
@Getter
@Setter
public class AnalyzeOptions {

    @Option(names = { "-o", "--output" },
            description = "Путь к директории для результатов (по умолчанию: текущая папка).")
    private Path directoryPath;

    @Option(names = { "-p", "--prefix" }, description = "Префикс имен выходных файлов (по умолчанию: отсутствует).")
    private String titlePrefix;

    @Option(names = { "-a", "--append" }, description = "Режим добавления в существующие файлы.")
    private boolean appendToExistingFiles;

    @Option(names = { "-s", "--short" }, description = "Вывод краткой статистики.")
    private boolean shortStatistics;

    @Option(names = { "-f", "--full" }, description = "Вывод полной статистики.")
    private boolean fullStatistics;

    @Parameters(index = "0..*", paramLabel = "FILES", description = "Входящие файлы для анализа")
    private List<File> inputFiles;
}
