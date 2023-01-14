package Queries;

import java.io.*;
import java.util.*;

import Exceptions.TableNotFoundException;

public class Update {
  private static final String DELIMITER = ",";
  private static final String TABLE_FILE_SUFFIX = ".tbl";
  private static final String LOCATION = "Database/";

  public static void executeUpdate(String tableName, String update, String condition)
      throws IOException, TableNotFoundException {
    File tableFile = assignFile(tableName);

    String[] updatePair = update.split("=");
    String column = updatePair[0].trim();
    String value = updatePair[1].trim();

    // Read the table file and update the values in memory
    ArrayList<String[]> rows = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(tableFile));
    String header = reader.readLine();
    if (header == null) {
      reader.close();
      return;
    }

    String[] headerArray = header.split(DELIMITER);

    Map<String, Integer> columnIndices = new HashMap<>();
    for (int i = 0; i < headerArray.length; i++) {
      columnIndices.put(headerArray[i].trim(), i);
    }

    String line;
    while ((line = reader.readLine()) != null) {
      String[] values = line.split(DELIMITER);
      boolean matchesCondition = true;
      if (condition != null) {
        // Check if the row should be updated based on the condition
        matchesCondition = WhereClause.evaluateCondition(condition, headerArray, values);
      }

      if (matchesCondition) {
        // Update the values in the row
        int index = columnIndices.get(column);
        values[index] = value;
      }
      rows.add(values);
    }
    reader.close();

    // Write the updated table back to the file
    BufferedWriter writer = new BufferedWriter(new FileWriter(tableFile));
    writer.write(header);
    writer.newLine();
    for (String[] row : rows) {
      for (int i = 0; i < row.length; i++) {
        writer.write(row[i]);
        if (i < row.length - 1) {
          writer.write(DELIMITER);
        }
      }
      writer.newLine();
    }
    writer.close();
  }

  private static File assignFile(String tableName) throws TableNotFoundException {
    File tableFile = new File(LOCATION + tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      throw new TableNotFoundException("Table " + tableName + " does not exist", tableName);
    }
    return tableFile;
  }
}
