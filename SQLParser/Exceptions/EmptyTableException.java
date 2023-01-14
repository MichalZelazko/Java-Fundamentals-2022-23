package Exceptions;

public class EmptyTableException extends Exception {
  private String tableName;

  public EmptyTableException(String message, String tableName) {
    super(message);
    this.tableName = tableName;
  }

  public String getTableName() {
    return tableName;
  }
}
