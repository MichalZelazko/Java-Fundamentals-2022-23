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
      throws IOException, TableNotFoundException, EmptyTableException, IllegalArgumentException {
    File tableFile = assignFile(tableName);
    String[] columnArray = getColumnArray(columns, tableFile);
    BufferedReader reader = new BufferedReader(new FileReader(tableFile));
    String[] headerArray = getHeaderArray(tableName, reader);
    Map<String, Integer> columnIndices = getColumnIndices(headerArray);
    checkColumns(columnArray, columnIndices);
    String output = addHeadersToOutput(columnArray);
    output = addValuesToOutput(output, reader, condition, columnArray, headerArray, columnIndices);
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

  private static void checkColumns(String[] columnArray, Map<String, Integer> columnIndices)
      throws IllegalArgumentException {
    for (String column : columnArray) {
      if (!columnIndices.containsKey(column.trim())) {
        throw new IllegalArgumentException("Column " + column + " does not exist");
      }
    }
  }

  private static String[] getColumnArray(String columns, File tableFile) throws IOException {
    String[] columnArray;
    if (columns.equals("*")) {
      columnArray = getColumnsFromTable(tableFile);
    } else {
      columnArray = columns.split(DELIMITER);
    }
    return columnArray;
  }

  private static String[] getHeaderArray(String tableName, BufferedReader reader)
      throws IOException, EmptyTableException {
    String header = reader.readLine();
    if (header == null) {
      reader.close();
      throw new EmptyTableException("Table " + tableName + " is empty", tableName);
    }
    return header.split(DELIMITER);
  }

  private static Map<String, Integer> getColumnIndices(String[] headerArray) {
    Map<String, Integer> columnIndices = new HashMap<>();
    for (int i = 0; i < headerArray.length; i++) {
      columnIndices.put(headerArray[i].trim(), i);
    }
    return columnIndices;
  }

  private static String[] getColumnsFromTable(File tableFile) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(tableFile));
    String header = reader.readLine();
    String[] columnArray = header.split(DELIMITER);
    reader.close();
    return columnArray;
  }

  private static String addHeadersToOutput(String[] columnArray) {
    String output = new String("");
    for (String column : columnArray) {
      output += column.trim() + DELIMITER;
    }
    output = output.substring(0, output.length() - 1) + "\n";
    return output;
  }

  private static String addValuesToOutput(String output, BufferedReader reader, String condition, String[] columnArray,
      String[] headerArray, Map<String, Integer> columnIndices) throws IOException, IllegalArgumentException {
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
          output += values[index] + DELIMITER;
        }
        output = output.substring(0, output.length() - 1) + "\n";
      }
    }
    return output;
  }
}
