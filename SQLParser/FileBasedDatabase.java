import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileBasedDatabase {
  private static final String DELIMITER = ",";
  private static final String TABLE_FILE_SUFFIX = ".tbl";

  private static final String SELECT_PATTERN = "^SELECT\\s+(.+?)\\s+FROM\\s+(\\S+)$";
  private static final String INSERT_PATTERN = "^INSERT\\s+INTO\\s+(\\S+)\\s+VALUES\\s+(.+?)$";
  private static final String UPDATE_PATTERN = "^UPDATE\\s+(\\S+)\\s+SET\\s+(.+?)\\s+WHERE\\s+(.+?)$";
  private static final String DELETE_PATTERN = "^DELETE\\s+FROM\\s+(\\S+)\\s+WHERE\\s+(.+?)$";

  private static final Pattern SELECT_REGEX = Pattern.compile(SELECT_PATTERN);
  private static final Pattern INSERT_REGEX = Pattern.compile(INSERT_PATTERN);
  private static final Pattern UPDATE_REGEX = Pattern.compile(UPDATE_PATTERN);
  private static final Pattern DELETE_REGEX = Pattern.compile(DELETE_PATTERN);

  public static void main(String[] args) throws IOException {
    String line;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    while ((line = reader.readLine()) != null) {
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
        executeSelect(tableName, columns);
      } else if (insertMatcher.matches()) {
        String tableName = insertMatcher.group(1);
        String values = insertMatcher.group(2);
        executeInsert(tableName, values);
      } else if (updateMatcher.matches()) {
        String tableName = updateMatcher.group(1);
        String updates = updateMatcher.group(2);
        String condition = updateMatcher.group(3);
        executeUpdate(tableName, updates, condition);
      } else if (deleteMatcher.matches()) {
        String tableName = deleteMatcher.group(1);
        String condition = deleteMatcher.group(2);
        executeDelete(tableName, condition);
      } else {
        System.out.println("Error: Invalid SQL statement");
      }
    }
  }

  private static void executeSelect(String tableName, String columns) throws IOException {
    File tableFile = new File(tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      System.out.println("Error: Table does not exist");
      return;
    }

    String[] columnArray = columns.split(",");
    BufferedReader reader = new BufferedReader(new FileReader(tableFile));
    String header = reader.readLine();
    if (header == null) {
      reader.close();
      return;
    }
    String[] headerArray = header.split(DELIMITER);

    Map<String, Integer> columnIndices = new HashMap<>();
    for (int i = 0; i < headerArray.length; i++) {
      columnIndices.put(headerArray[i], i);
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
      for (String column : columnArray) {
        int index = columnIndices.get(column.trim());
        output.append(values[index]).append(DELIMITER);
      }
      output.setLength(output.length() - 1);
      output.append("\n");
    }

    reader.close();
    System.out.println(output.toString());
  }

  private static void executeInsert(String tableName, String values) throws IOException {
    File tableFile = new File(tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      System.out.println("Error: Table does not exist");
      return;
    }

    String[] valueArray = values.split(",");
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

    List<String> lines = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(tableFile));
    String header = reader.readLine();
    lines.add(header);

    String[] updateArray = updates.split(",");
    Map<String, String> updateMap = new HashMap<>();
    StringBuilder updatedLine = new StringBuilder();
    for (String value : updateArray) {
      updatedLine.append(value).append(DELIMITER);
    }
    updatedLine.setLength(updatedLine.length() - 1);
    lines.add(updatedLine.toString());
    reader.close();

    BufferedWriter writer = new BufferedWriter(new FileWriter(tableFile));
    for (String updatedLine2 : lines) {
      writer.write(updatedLine2);
      writer.newLine();
    }
    writer.close();
  }

  private static void executeDelete(String tableName, String condition) throws IOException {
    File tableFile = new File(tableName + TABLE_FILE_SUFFIX);
    if (!tableFile.exists()) {
      System.out.println("Error: Table does not exist");
      return;
    }

    List<String> lines = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(tableFile));
    String header = reader.readLine();
    lines.add(header);

    String[] headerArray = header.split(DELIMITER);

    String line;
    while ((line = reader.readLine()) != null) {
      String[] values = line.split(DELIMITER);
      boolean matchesCondition = evaluateCondition(condition, headerArray, values);
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
    String column = parts[0];
    int columnIndex = -1;
    for (int i = 0; i < headerArray.length; i++) {
      if (headerArray[i].equals(column)) {
        columnIndex = i;
        break;
      }
    }
    if (columnIndex == -1) {
      // Invalid column name
      return false;
    }

    // Get operator and value
    String operator = parts[1];
    String value = parts[2];

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
