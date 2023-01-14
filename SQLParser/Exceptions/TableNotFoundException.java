package Exceptions;

public class TableNotFoundException extends Exception {

  private String tableName;

  public TableNotFoundException(String message, String tableName) {
    super(message);
    this.tableName = tableName;
  }

  public String getTableName() {
    return tableName;
  }
}
