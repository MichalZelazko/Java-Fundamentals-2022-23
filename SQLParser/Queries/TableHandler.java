package Queries;

import java.io.*;

import Exceptions.TableNotFoundException;
import Exceptions.TableAlreadyExistsException;

public class TableHandler {
  private static final String DELIMITER = ",";
  private static final String TABLE_FILE_SUFFIX = ".tbl";
  private static final String LOCATION = "Database/";

  public static void createTable(String tableName, String columns)
      throws IOException, TableAlreadyExistsException {
    File tableFile = assignFileToCreate(tableName);
    tableFile.createNewFile();
    writeHeadersToFile(tableFile, columns);
  }

  public static void dropTable(String tableName) throws TableNotFoundException {
    File tableFile = assignFileToDrop(tableName);
    tableFile.delete();
  }

  private static File assignFileToCreate(String tableName) throws TableAlreadyExistsException {
    File tableFile = new File(LOCATION + tableName + TABLE_FILE_SUFFIX);
    if (tableFile.exists()) {
      throw new TableAlreadyExistsException("Table " + tableName + " does not exist", tableName);
    }
    return tableFile;
  }

  private static File assignFileToDrop(String tableName) throws TableNotFoundException {
    File tableFile = new File(LOCATION + tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      throw new TableNotFoundException("Table " + tableName + " does not exist", tableName);
    }
    return tableFile;
  }

  private static void writeHeadersToFile(File tableFile, String columns)
      throws IOException {
    String[] columnArray = parseHeadersToWrite(columns);
    BufferedWriter writer = new BufferedWriter(new FileWriter(tableFile));
    for (int i = 0; i < columnArray.length; i++) {
      writer.write(columnArray[i]);
      if (i < columnArray.length - 1) {
        writer.write(DELIMITER);
      }
    }
    writer.write("\n");
    writer.close();
  }

  private static String[] parseHeadersToWrite(String columns) {
    String[] columnArray = columns.split(DELIMITER);
    for (int i = 0; i < columnArray.length; i++) {
      columnArray[i] = columnArray[i].trim();
      if (columnArray[i].startsWith("(")) {
        columnArray[i] = columnArray[i].substring(1);
      }
      if (columnArray[i].endsWith(")")) {
        columnArray[i] = columnArray[i].substring(0, columnArray[i].length() - 1);
      }
    }
    return columnArray;
  }
}
