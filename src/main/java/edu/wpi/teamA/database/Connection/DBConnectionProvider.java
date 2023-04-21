package edu.wpi.teamA.database.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectionProvider {
  private static DBConnectionProvider instance = null;
  private static Connection connection;

  public static DBConnectionProvider getInstance() {
    if (instance == null) {
      instance = new DBConnectionProvider();
      instance.createConnection();
    }
    return instance;
  }

  public static Connection createConnection() {
    if (connection == null) {

      String url = "jdbc:postgresql://database.cs.wpi.edu:5432/teamadb";
      String user = "teama";
      String password = "teama10";

      try {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(url, user, password);
      } catch (SQLException e) {
        e.printStackTrace();
        return null;
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    return connection;
  }

  public static void createSchema() {
    try {
      Statement stmtSchema = instance.createConnection().createStatement();
      String sqlCreateSchema = "CREATE SCHEMA IF NOT EXISTS \"Teama_schema\"";
      stmtSchema.execute(sqlCreateSchema);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  // close the connection and exit
  public static void closeConnection() {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
