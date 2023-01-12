import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileBasedDatabase {
  private static final String DELIMITER = ",";
  private static final String TABLE_FILE_SUFFIX = ".tbl";

  private static final String SELECT_PATTERN = "^SELECT\\s+(.+?)\\s+FROM\\s+(\\S+)(?:\\s+WHERE\\s+)?(.+?)$";
  private static final String INSERT_PATTERN = "^INSERT\\s+INTO\\s+(\\S+)\\s+VALUES\\s+(.+?)$";
  // TODO: fix set regex
  private static final String UPDATE_PATTERN = "^UPDATE\\s+(\\S+)\\s+SET\\s+(.+?)(?:\\s+WHERE\\s+)?(.+)$";
  private static final String DELETE_PATTERN = "^DELETE\\s+FROM\\s+(\\S+)(?:\\s+WHERE\\s+)?(.+?)$";

  private static final Pattern SELECT_REGEX = Pattern.compile(SELECT_PATTERN);
  private static final Pattern INSERT_REGEX = Pattern.compile(INSERT_PATTERN);
  private static final Pattern UPDATE_REGEX = Pattern.compile(UPDATE_PATTERN);
  private static final Pattern DELETE_REGEX = Pattern.compile(DELETE_PATTERN);

  public static void main(String[] args) throws IOException {
    Scanner query = new Scanner(System.in);
    System.out.print("Enter a SQL statement (enter x to exit the program): ");
    String line = query.nextLine();

    while (line != null && !line.trim().equalsIgnoreCase("x")) {
      line = line.trim();
      if (line.isEmpty()) {
        continue;
      }

      Matcher selectMatcher = SELECT_REGEX.matcher(line);
      Matcher insertMatcher = INSERT_REGEX.matcher(line);
      Matcher updateMatcher = UPDATE_REGEX.matcher(line);
      Matcher deleteMatcher = DELETE_REGEX.matcher(line);

      if (selectMatcher.matches()) {
        String columns = selectMatcher.group(1);
        String tableName = selectMatcher.group(2);
        String condition = selectMatcher.group(3);
        executeSelect(tableName, columns, condition);
      } else if (insertMatcher.matches()) {
        String tableName = insertMatcher.group(1);
        String values = insertMatcher.group(2);
        executeInsert(tableName, values);
      } else if (updateMatcher.matches()) {
        String tableName = updateMatcher.group(1);
        String updates = updateMatcher.group(2);
        System.out.println(updates);
        String condition = updateMatcher.group(3);
        executeUpdate(tableName, updates, condition);
      } else if (deleteMatcher.matches()) {
        String tableName = deleteMatcher.group(1);
        String condition = deleteMatcher.group(2);
        executeDelete(tableName, condition);
      } else {
        System.out.println("Error: Invalid SQL statement");
        // TODO: invalid statement exception
      }
      System.out.print("Enter a SQL statement (enter x to exit the program): ");
      line = query.nextLine();
    }
    query.close();
  }

  private static void executeSelect(String tableName, String columns, String condition) throws IOException {
    File tableFile = new File(tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      System.out.println("Error: Table does not exist");
      // TODO: no table exception
      return;
    }

    String[] columnArray = columns.split(DELIMITER);
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
        matchesCondition = evaluateCondition(condition, headerArray, values);
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

  private static void executeInsert(String tableName, String values) throws IOException {
    File tableFile = new File(tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      System.out.println("Error: Table does not exist");
      // TODO: no table exception
      return;
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

  private static void executeUpdate(String tableName, String updates, String condition) throws IOException {
    File tableFile = new File(tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      System.out.println("Error: Table does not exist");
      return;
    }

    // Parse the updates string to determine the column names and new values
    Map<String, String> updateMap = new HashMap<>();
    for (String update : updates.split(",")) {
      String[] updatePair = update.split("=");
      String column = updatePair[0].trim();
      String value = updatePair[1].trim();
      updateMap.put(column, value);
    }

    // Read the table file and update the values in memory
    List<String[]> rows = new ArrayList<>();
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
        matchesCondition = evaluateCondition(condition, headerArray, values);
      }

      if (matchesCondition) {
        // Update the values in the row
        for (Map.Entry<String, String> entry : updateMap.entrySet()) {
          int index = columnIndices.get(entry.getKey());
          values[index] = entry.getValue();
        }
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

  private static void executeDelete(String tableName, String condition) throws IOException {
    File tableFile = new File(tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      System.out.println("Error: Table does not exist");
      // TODO: no table exception
      return;
    }

    List<String> lines = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(tableFile));
    String header = reader.readLine();
    lines.add(header);

    String[] headerArray = header.split(DELIMITER);

    String line;
    while ((line = reader.readLine()) != null) {
      boolean matchesCondition = true;
      String[] values = line.split(DELIMITER);
      if (condition != null) {
        matchesCondition = evaluateCondition(condition, headerArray, values);
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

  private static boolean evaluateCondition(String condition, String[] headerArray, String[] values) {
    // Split condition into parts
    String[] parts = condition.trim().split("\\s+");
    if (parts.length != 3) {
      // Invalid condition
      return false;
    }

    // Get column index
    String column = parts[0].trim();
    int columnIndex = -1;
    for (int i = 0; i < headerArray.length; i++) {
      if (headerArray[i].trim().equals(column)) {
        columnIndex = i;
        break;
      }
    }
    if (columnIndex == -1) {
      // Invalid column name
      return false;
    }
    // Get operator and value
    String operator = parts[1].trim();
    String value = parts[2].trim();

    // Evaluate condition
    if ("=".equals(operator)) {
      return values[columnIndex].equals(value);
    } else if ("!=".equals(operator)) {
      return !values[columnIndex].equals(value);
    } else if (">".equals(operator)) {
      try {
        int intValue = Integer.parseInt(values[columnIndex]);
        int intCondition = Integer.parseInt(value);
        return intValue > intCondition;
      } catch (NumberFormatException e) {
        return false;
      }
    } else if (">=".equals(operator)) {
      try {
        int intValue = Integer.parseInt(values[columnIndex]);
        int intCondition = Integer.parseInt(value);
        return intValue >= intCondition;
      } catch (NumberFormatException e) {
        return false;
      }
    } else if ("<".equals(operator)) {
      try {
        int intValue = Integer.parseInt(values[columnIndex]);
        int intCondition = Integer.parseInt(value);
        return intValue < intCondition;
      } catch (NumberFormatException e) {
        return false;
      }
    } else if ("<=".equals(operator)) {
      try {
        int intValue = Integer.parseInt(values[columnIndex]);
        int intCondition = Integer.parseInt(value);
        return intValue <= intCondition;
      } catch (NumberFormatException e) {
        return false;
      }
    } else {
      // Invalid operator
      return false;
    }
  }
}
