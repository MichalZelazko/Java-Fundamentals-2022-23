package Queries;

public class WhereClause {
  public static boolean evaluateCondition(String condition, String[] headerArray, String[] values)
      throws IllegalArgumentException {
    String[] parts = parseConditionIntoParts(condition);
    String column = parts[0].trim();
    int columnIndex = getColumnIndex(column, headerArray);

    String operator = parts[1].trim();
    String value = parts[2].trim();

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
      throw new IllegalArgumentException("Invalid comparison operator");
    }
  }

  private static int getColumnIndex(String column, String[] headerArray) throws IllegalArgumentException {
    for (int i = 0; i < headerArray.length; i++) {
      if (headerArray[i].trim().equals(column)) {
        return i;
      }
    }
    throw new IllegalArgumentException("Column " + column + " doesn't exist");
  }

  private static String[] parseConditionIntoParts(String condition) throws IllegalArgumentException {
    String[] parts = condition.trim().split("\\s+");
    if (parts.length != 3) {
      throw new IllegalArgumentException("Invalid condition");
    }
    return parts;
  }
}
