import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileBasedDatabase {
  private static final String SELECT_PATTERN = "^SELECT\\s+(.+?)\\s+FROM\\s+(\\S+)(?:\\s+WHERE\\s+)?(.+?)?$";
  private static final String INSERT_PATTERN = "^INSERT\\s+INTO\\s+(\\S+)\\s+VALUES\\s+(.+?)$";
  private static final String UPDATE_PATTERN = "^UPDATE\\s+(\\S+)\\s+SET\\s+(\\S+)\\s*=\\s*(\\S+)(?:\\s+WHERE\\s+)?(.+?)?$";
  private static final String DELETE_PATTERN = "^DELETE\\s+FROM\\s+(\\S+)(?:\\s+WHERE\\s+)?(.+?)?$";

  private static final Pattern SELECT_REGEX = Pattern.compile(SELECT_PATTERN);
  private static final Pattern INSERT_REGEX = Pattern.compile(INSERT_PATTERN);
  private static final Pattern UPDATE_REGEX = Pattern.compile(UPDATE_PATTERN);
  private static final Pattern DELETE_REGEX = Pattern.compile(DELETE_PATTERN);

  public static void main(String[] args) {
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
        try {
          Select.executeSelect(tableName, columns, condition);
        } catch (TableNotFoundException e) {
          System.out.println(e.getMessage());
        } catch (IOException e) {
          System.out.println("Error: " + e.getMessage());
        }
      } else if (insertMatcher.matches()) {
        String tableName = insertMatcher.group(1);
        String values = insertMatcher.group(2);
        try {
          Insert.executeInsert(tableName, values);
        } catch (TableNotFoundException e) {
          System.out.println(e.getMessage());
        } catch (IOException e) {
          System.out.println("Error: " + e.getMessage());
        }
      } else if (updateMatcher.matches()) {
        String tableName = updateMatcher.group(1);
        String update = updateMatcher.group(2) + " = " + updateMatcher.group(3);
        String condition = updateMatcher.group(4);
        try {
          Update.executeUpdate(tableName, update, condition);
        } catch (TableNotFoundException e) {
          System.out.println(e.getMessage());
        } catch (IOException e) {
          System.out.println("Error: " + e.getMessage());
        }
      } else if (deleteMatcher.matches()) {
        String tableName = deleteMatcher.group(1);
        String condition = deleteMatcher.group(2);
        try {
          Delete.executeDelete(tableName, condition);
        } catch (TableNotFoundException e) {
          System.out.println(e.getMessage());
        } catch (IOException e) {
          System.out.println("Error: " + e.getMessage());
        }
      } else {
        System.out.println("Error: Invalid SQL statement");
        // TODO: invalid statement exception
      }
      System.out.print("Enter a SQL statement (enter x to exit the program): ");
      line = query.nextLine();
    }
    query.close();
  }
}
