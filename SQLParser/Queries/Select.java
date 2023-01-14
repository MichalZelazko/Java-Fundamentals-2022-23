package Queries;

import java.io.*;
import java.util.*;

import Exceptions.TableNotFoundException;
import Exceptions.EmptyTableException;

public class Select {
  private static final String DELIMITER = ",";
  private static final String TABLE_FILE_SUFFIX = ".tbl";
  private static final String LOCATION = "Database/";

  public static void executeSelect(String tableName, String columns, String condition)
      throws IOException, TableNotFoundException, EmptyTableException {
    File tableFile = assignFile(tableName);

    String[] columnArray = columns.split(DELIMITER);
    BufferedReader reader = new BufferedReader(new FileReader(tableFile));
    String header = reader.readLine();
    if (header == null) {
      reader.close();
      throw new EmptyTableException("Table " + tableName + " is empty", tableName);
    }

    String[] headerArray = header.split(DELIMITER);

    Map<String, Integer> columnIndices = new HashMap<>();
    for (int i = 0; i < headerArray.length; i++) {
      columnIndices.put(headerArray[i].trim(), i);
    }

    StringBuilder output = new StringBuilder();
    for (String column : columnArray) {
      output.append(column.trim()).append(DELIMITER);
    }
    output.setLength(output.length() - 1);
    output.append("\n");

    String line;
    while ((line = reader.readLine()) != null) {
      String[] values = line.split(DELIMITER);
      boolean matchesCondition = true;
      if (condition != null) {
        matchesCondition = WhereClause.evaluateCondition(condition, headerArray, values);
      }
      if (matchesCondition) {
        for (String column : columnArray) {
          int index = columnIndices.get(column.trim());
          output.append(values[index]).append(DELIMITER);
        }
        output.setLength(output.length() - 1);
        output.append("\n");
      }
    }

    reader.close();
    System.out.println(output.toString());
  }

  private static File assignFile(String tableName) throws TableNotFoundException {
    File tableFile = new File(LOCATION + tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      throw new TableNotFoundException("Table " + tableName + " does not exist", tableName);
    }
    return tableFile;
  }
}
