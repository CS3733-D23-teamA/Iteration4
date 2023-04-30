package edu.wpi.teamA.navigation;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import java.sql.SQLException;
import java.sql.Statement;

public class Adb {
  static DBConnectionProvider mainProvider = new DBConnectionProvider();

  public static void createSchema() {
    try {
      Statement stmtSchema = mainProvider.createLocalConnection().createStatement();
      String sqlCreateSchema = "CREATE SCHEMA IF NOT EXISTS \"Teama_schema\"";
      stmtSchema.execute(sqlCreateSchema);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
