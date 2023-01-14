package Queries;

import java.io.*;
import java.util.*;

import Exceptions.TableNotFoundException;
import Exceptions.EmptyTableException;

public class Update {
  private static final String DELIMITER = ",";
  private static final String TABLE_FILE_SUFFIX = ".tbl";
  private static final String LOCATION = "Database/";

  public static void executeUpdate(String tableName, String update, String condition)
      throws IOException, TableNotFoundException, EmptyTableException, IllegalArgumentException {
    File tableFile = assignFile(tableName);

    String[] updatePair = update.split("=");
    String column = updatePair[0].trim();
    String value = updatePair[1].trim();

    BufferedReader reader = new BufferedReader(new FileReader(tableFile));

    String header = getHeader(tableName, reader);
    String[] headerArray = header.split(DELIMITER);
    Map<String, Integer> columnIndices = getColumnIndices(headerArray);
    checkColumn(column, columnIndices);
    ArrayList<String[]> rows = determineRowsToUpdate(reader, condition, headerArray, columnIndices, column, value);

    reader.close();

    writeToFile(tableFile, header, rows);
  }

  private static File assignFile(String tableName) throws TableNotFoundException {
    File tableFile = new File(LOCATION + tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      throw new TableNotFoundException("Table " + tableName + " does not exist", tableName);
    }
    return tableFile;
  }

  private static void writeToFile(File tableFile, String header, ArrayList<String[]> rows)
      throws IOException {
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

  private static void checkColumn(String column, Map<String, Integer> columnIndices) throws IllegalArgumentException {
    if (!columnIndices.containsKey(column)) {
      throw new IllegalArgumentException("Column " + column + " doesn't exist");
    }
  }

  private static String getHeader(String tableName, BufferedReader reader) throws IOException, EmptyTableException {
    String header = reader.readLine();
    if (header == null) {
      reader.close();
      throw new EmptyTableException("Table " + tableName + " is empty", tableName);
    }
    return header;
  }

  private static ArrayList<String[]> determineRowsToUpdate(BufferedReader reader, String condition,
      String[] headerArray, Map<String, Integer> columnIndices, String column, String value)
      throws IOException, IllegalArgumentException {
    ArrayList<String[]> rows = new ArrayList<>();
    String line;
    while ((line = reader.readLine()) != null) {
      String[] values = line.split(DELIMITER);
      boolean matchesCondition = true;
      if (condition != null) {
        matchesCondition = WhereClause.evaluateCondition(condition, headerArray, values);
      }
      if (matchesCondition) {
        int index = columnIndices.get(column);
        values[index] = value;
      }
      rows.add(values);
    }
    return rows;
  }

  private static HashMap<String, Integer> getColumnIndices(String[] headerArray) {
    HashMap<String, Integer> columnIndices = new HashMap<>();
    for (int i = 0; i < headerArray.length; i++) {
      columnIndices.put(headerArray[i].trim(), i);
    }
    return columnIndices;
  }
}
