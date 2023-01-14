package Queries;

import java.io.*;
import java.util.ArrayList;

import Exceptions.TableNotFoundException;

public class Delete {
  private static final String DELIMITER = ",";
  private static final String TABLE_FILE_SUFFIX = ".tbl";
  private static final String LOCATION = "Database/";

  public static void executeDelete(String tableName, String condition) throws TableNotFoundException, IOException {
    File tableFile = assignFile(tableName);

    ArrayList<String> lines = new ArrayList<String>();
    BufferedReader reader = new BufferedReader(new FileReader(tableFile));

    String header = reader.readLine();
    lines.add(header);

    lines = determineLinesToDelete(reader, condition, lines, header);
    writeToFile(tableFile, lines);
    reader.close();
  }

  private static void writeToFile(File tableFile, ArrayList<String> lines) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(tableFile));
      for (String updatedLine : lines) {
        writer.write(updatedLine);
        writer.newLine();
      }
      writer.close();
    } catch (IOException e) {
      e.getMessage();
    }
  }

  private static File assignFile(String tableName) throws TableNotFoundException {
    File tableFile = new File(LOCATION + tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      throw new TableNotFoundException("Table " + tableName + " does not exist", tableName);
    }
    return tableFile;
  }

  private static ArrayList<String> determineLinesToDelete(BufferedReader reader, String condition,
      ArrayList<String> lines, String header) throws IOException {
    String[] headerArray = header.split(DELIMITER);
    String line;
    while ((line = reader.readLine()) != null) {
      boolean matchesCondition = true;
      String[] values = line.split(DELIMITER);
      if (condition != null) {
        matchesCondition = WhereClause.evaluateCondition(condition, headerArray, values);
      }
      if (!matchesCondition) {
        lines.add(line);
      }
    }
    return lines;
  }
}
