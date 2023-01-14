package Queries;

import java.io.*;
import java.util.ArrayList;

import Exceptions.TableNotFoundException;

public class Delete {
  private static final String DELIMITER = ",";
  private static final String TABLE_FILE_SUFFIX = ".tbl";
  private static final String LOCATION = "Database/";

  public static void executeDelete(String tableName, String condition) throws TableNotFoundException, IOException {
    File tableFile = new File(LOCATION + tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      throw new TableNotFoundException("Table " + tableName + " does not exist", tableName);
    }

    ArrayList<String> lines = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(tableFile));
    String header = reader.readLine();
    lines.add(header);

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

    reader.close();

    BufferedWriter writer = new BufferedWriter(new FileWriter(tableFile));
    for (String updatedLine : lines) {
      writer.write(updatedLine);
      writer.newLine();
    }
    writer.close();
  }
}
