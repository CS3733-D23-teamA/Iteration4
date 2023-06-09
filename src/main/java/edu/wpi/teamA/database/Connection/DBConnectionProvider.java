package edu.wpi.teamA.database.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.SneakyThrows;

public class DBConnectionProvider {
  private static Connection instance = null;

  public static Connection getInstance() {
    if (instance == null) {
      instance = createLocalConnection();
    }
    return instance;
  }

  @SneakyThrows
  public static Connection createLocalConnection() {
    closeConnection();
    if (instance == null || instance.isClosed()) {

      String url = "jdbc:postgresql://database.cs.wpi.edu:5432/teamadb";
      String user = "teama";
      String password = "teama10";

      try {
        Class.forName("org.postgresql.Driver");
        instance = DriverManager.getConnection(url, user, password);
        System.out.println("Local connection created");
      } catch (SQLException e) {
        e.printStackTrace();
        return null;
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    return instance;
  }

  @SneakyThrows
  public static Connection createAWSConnection() {
    closeConnection();
    if (instance == null || instance.isClosed()) {

      String url =
          "jdbc:postgresql://cs3733-d23-teama.coiigqxuofye.us-east-2.rds.amazonaws.com:5432/postgres";
      String user = "teama";
      String password = "teamaiswongsfavoriteteam";

      try {
        Class.forName("org.postgresql.Driver");
        instance = DriverManager.getConnection(url, user, password);
        System.out.println("AWS connection created");
      } catch (SQLException e) {
        e.printStackTrace();
        return null;
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    return instance;
  }

  public static void createSchema() {
    try {
      Statement stmtSchema = instance.createStatement();
      String sqlCreateSchema = "CREATE SCHEMA IF NOT EXISTS \"Teama_schema\"";
      stmtSchema.execute(sqlCreateSchema);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  // close the connection and exit
  public static void closeConnection() {
    try {
      if (instance != null && !instance.isClosed()) {
        instance.close();
        System.out.println("Connection closed");
      } else {
        System.out.println("Connection already closed");
      }

    } catch (SQLException e) {
      System.out.println("Connection already closed");
      e.printStackTrace();
    }
  }
}
