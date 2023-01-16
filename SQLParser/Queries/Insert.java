package Queries;

import java.io.*;

import Exceptions.TableNotFoundException;

public class Insert {
  private static final String DELIMITER = ",";
  private static final String TABLE_FILE_SUFFIX = ".tbl";
  private static final String LOCATION = "Database/";

  public static void executeInsert(String tableName, String values)
      throws IOException, TableNotFoundException, IllegalArgumentException {
    File tableFile = assignFile(tableName);
    String[] valueArray = parseValuesToInsert(values, tableFile);
    String insert = prepareStringForInsertion(valueArray);
    writeToFile(tableFile, insert);
  }

  private static File assignFile(String tableName) throws TableNotFoundException {
    File tableFile = new File(LOCATION + tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      throw new TableNotFoundException("Table " + tableName + " does not exist", tableName);
    }
    return tableFile;
  }

  private static String[] parseHeaders(File tableFile) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(tableFile));
    String header = reader.readLine();
    reader.close();
    return header.split(DELIMITER);
  }

  private static String[] parseValuesToInsert(String values, File tableFile)
      throws IOException, IllegalArgumentException {
    String[] headerArray = parseHeaders(tableFile);
    String[] valueArray = values.split(",");
    if (headerArray.length != valueArray.length) {
      throw new IllegalArgumentException("Number of values does not match number of columns");
    }
    for (int i = 0; i < valueArray.length; i++) {
      valueArray[i] = valueArray[i].trim();
      if (valueArray[i].startsWith("(")) {
        valueArray[i] = valueArray[i].substring(1);
      }
      if (valueArray[i].endsWith(")")) {
        valueArray[i] = valueArray[i].substring(0, valueArray[i].length() - 1);
      }
      if (valueArray[i].isEmpty()) {
        throw new IllegalArgumentException("Value cannot be empty");
      }
    }
    return valueArray;
  }

  private static String prepareStringForInsertion(String[] valueArray) {
    String insert = new String("");
    for (String value : valueArray) {
      insert += value.trim() + DELIMITER;
    }
    insert = insert.substring(0, insert.length() - 1) + "\n";
    return insert;
  }

  private static void writeToFile(File tableFile, String insert) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(tableFile, true));
    writer.write(insert.toString());
    writer.close();
  }
}
