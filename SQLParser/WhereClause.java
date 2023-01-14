public class WhereClause {
  public static boolean evaluateCondition(String condition, String[] headerArray, String[] values) {
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
