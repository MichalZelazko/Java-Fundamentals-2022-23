import java.io.*;
import java.util.*;
import java.util.regex.*;

import Exceptions.EmptyTableException;
import Exceptions.TableNotFoundException;
import Exceptions.TableAlreadyExistsException;
import Queries.Delete;
import Queries.Insert;
import Queries.Select;
import Queries.Update;
import Queries.TableHandler;

public class FileBasedDatabase {
  private static final String SELECT_PATTERN = "^SELECT\\s+(.+?)\\s+FROM\\s+(\\S+)(?:\\s+WHERE\\s+)?(.+?)?$";
  private static final String INSERT_PATTERN = "^INSERT\\s+INTO\\s+(\\S+)\\s+VALUES\\s+(.+?)$";
  private static final String UPDATE_PATTERN = "^UPDATE\\s+(\\S+)\\s+SET\\s+(\\S+)\\s*=\\s*(\\S+)(?:\\s+WHERE\\s+)?(.+?)?$";
  private static final String DELETE_PATTERN = "^DELETE\\s+FROM\\s+(\\S+)(?:\\s+WHERE\\s+)?(.+?)?$";
  private static final String CREATE_PATTERN = "^CREATE\\s+TABLE\\s+(\\S+)\\s+\\((.+?)\\)$";
  private static final String DROP_PATTERN = "^DROP\\s+TABLE\\s+(\\S+)$";

  private static final Pattern SELECT_REGEX = Pattern.compile(SELECT_PATTERN);
  private static final Pattern INSERT_REGEX = Pattern.compile(INSERT_PATTERN);
  private static final Pattern UPDATE_REGEX = Pattern.compile(UPDATE_PATTERN);
  private static final Pattern DELETE_REGEX = Pattern.compile(DELETE_PATTERN);
  private static final Pattern CREATE_REGEX = Pattern.compile(CREATE_PATTERN);
  private static final Pattern DROP_REGEX = Pattern.compile(DROP_PATTERN);

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String line = getQuery(scanner);

    while (line != null && !line.equalsIgnoreCase("x")) {
      Matcher selectMatcher = SELECT_REGEX.matcher(line);
      Matcher insertMatcher = INSERT_REGEX.matcher(line);
      Matcher updateMatcher = UPDATE_REGEX.matcher(line);
      Matcher deleteMatcher = DELETE_REGEX.matcher(line);
      Matcher createMatcher = CREATE_REGEX.matcher(line);
      Matcher dropMatcher = DROP_REGEX.matcher(line);

      if (selectMatcher.matches()) {
        trySelect(selectMatcher);
      } else if (insertMatcher.matches()) {
        tryInsert(insertMatcher);
      } else if (updateMatcher.matches()) {
        tryUpdate(updateMatcher);
      } else if (deleteMatcher.matches()) {
        tryDelete(deleteMatcher);
      } else if (createMatcher.matches()) {
        tryCreate(createMatcher);
      } else if (dropMatcher.matches()) {
        tryDrop(dropMatcher);
      } else {
        System.out.println("Error: Invalid SQL statement. Try again.");
      }
      line = getQuery(scanner);
    }
    scanner.close();
  }

  private static String getQuery(Scanner scanner) {
    System.out.print("Enter a SQL statement (enter x to exit the program): ");
    String line = scanner.nextLine().trim();
    return line;
  }

  private static void trySelect(Matcher selectMatcher) {
    String columns = selectMatcher.group(1);
    String tableName = selectMatcher.group(2);
    String condition = selectMatcher.group(3);
    try {
      Select.executeSelect(tableName, columns, condition);
    } catch (TableNotFoundException e) {
      System.out.println(e.getMessage());
    } catch (EmptyTableException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private static void tryInsert(Matcher insertMatcher) {
    String tableName = insertMatcher.group(1);
    String values = insertMatcher.group(2);
    try {
      Insert.executeInsert(tableName, values);
    } catch (TableNotFoundException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void tryUpdate(Matcher updateMatcher) {
    String tableName = updateMatcher.group(1);
    String update = updateMatcher.group(2) + " = " + updateMatcher.group(3);
    String condition = updateMatcher.group(4);
    try {
      Update.executeUpdate(tableName, update, condition);
    } catch (TableNotFoundException e) {
      System.out.println(e.getMessage());
    } catch (EmptyTableException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private static void tryDelete(Matcher deleteMatcher) {
    String tableName = deleteMatcher.group(1);
    String condition = deleteMatcher.group(2);
    try {
      Delete.executeDelete(tableName, condition);
    } catch (TableNotFoundException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private static void tryCreate(Matcher createMatcher) {
    String tableName = createMatcher.group(1);
    String columns = createMatcher.group(2);
    try {
      TableHandler.createTable(tableName, columns);
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    } catch (TableAlreadyExistsException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void tryDrop(Matcher dropMatcher) {
    String tableName = dropMatcher.group(1);
    try {
      TableHandler.dropTable(tableName);
    } catch (TableNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}
