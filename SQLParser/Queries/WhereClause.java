package Queries;

public class WhereClause {
  public static boolean evaluateCondition(String condition, String[] headerArray, String[] values)
      throws IllegalArgumentException {
    // Split condition into parts
    String[] parts = condition.trim().split("\\s+");
    if (parts.length != 3) {
      // Invalid condition
      return false;
    }

    // Get column index
    String column = parts[0].trim();
    int columnIndex = getColumnIndex(column, headerArray);
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

  private static int getColumnIndex(String column, String[] headerArray) throws IllegalArgumentException {
    for (int i = 0; i < headerArray.length; i++) {
      if (headerArray[i].trim().equals(column)) {
        return i;
      }
    }
    throw new IllegalArgumentException("Column " + column + " doesn't exist");
  }
}
