package br.com.abimael.cursotestes.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.nio.file.Files.readString;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;

public class SqlScriptMerger {

  public static String mergeAllSqlScripts(String outputDir, String mergedFileName) {

    try {
      File sqlFolder = new File("pgsql-image");
      if (!sqlFolder.exists() || !sqlFolder.isDirectory()) {
        throw new IllegalArgumentException("The SQL folder not exists: " + outputDir);
      }

      List<File> sqlFiles =
              asList(requireNonNull(sqlFolder.listFiles((dir, name) -> name.endsWith(".sql"))));

      sqlFiles.sort(comparing(File::getName));

      String mergedFilePath = outputDir + "/" + mergedFileName;

      try (BufferedWriter writer =
                   new BufferedWriter(
                           new OutputStreamWriter(
                                   new FileOutputStream(mergedFilePath), StandardCharsets.UTF_8))) {
        for (File sqlFile : sqlFiles) {
          writer.write("-- Script: " + sqlFile.getName() + "\n");
          String content = readString(sqlFile.toPath()).replace("\r\n", "\n"); // Garante LF
          writer.write(content);
          writer.write("\n\n");
        }
      }

      return mergedFileName;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
