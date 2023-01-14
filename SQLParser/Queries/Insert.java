package Queries;

import java.io.*;

import Exceptions.TableNotFoundException;

public class Insert {
  private static final String DELIMITER = ",";
  private static final String TABLE_FILE_SUFFIX = ".tbl";
  private static final String LOCATION = "Database/";

  public static void executeInsert(String tableName, String values) throws IOException, TableNotFoundException {
    File tableFile = new File(LOCATION + tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      throw new TableNotFoundException("Table " + tableName + " does not exist", tableName);
    }

    String[] valueArray = values.split(",");
    for (int i = 0; i < valueArray.length; i++) {
      valueArray[i] = valueArray[i].trim();
      if (valueArray[i].startsWith("(")) {
        valueArray[i] = valueArray[i].substring(1);
      }
      if (valueArray[i].endsWith(")")) {
        valueArray[i] = valueArray[i].substring(0, valueArray[i].length() - 1);
      }
    }

    StringBuilder insert = new StringBuilder();
    for (String value : valueArray) {
      insert.append(value.trim()).append(DELIMITER);
    }
    insert.setLength(insert.length() - 1);
    insert.append("\n");

    BufferedWriter writer = new BufferedWriter(new FileWriter(tableFile, true));
    writer.write(insert.toString());
    writer.close();
  }
}
