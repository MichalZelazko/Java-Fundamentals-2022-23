package Exceptions;

public class TableAlreadyExistsException extends Exception {
  private String tableName;

  public TableAlreadyExistsException(String message, String tableName) {
    super(message);
    this.tableName = tableName;
  }

  public String getTableName() {
    return tableName;
  }
}
