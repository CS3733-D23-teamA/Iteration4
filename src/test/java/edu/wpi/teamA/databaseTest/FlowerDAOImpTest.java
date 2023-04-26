package edu.wpi.teamA.databaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.DAOImps.FlowerDAOImp;
import edu.wpi.teamA.database.ORMclasses.Flower;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FlowerDAOImpTest {

  private FlowerDAOImp flowerDAOImp;
  private DBConnectionProvider dbConnectionProvider;
  private Connection connection;
  private Flower flower;

  @BeforeEach
  public void setUp() {
    flowerDAOImp = new FlowerDAOImp();
    flower =
        new Flower(
            1,
            "testName",
            "testRoom",
            new Date(System.currentTimeMillis()),
            1000,
            "testItems",
            "testComment",
            "testEmployee",
            "testStatus",
            "testCreator");

    connection = DBConnectionProvider.createConnection();
  }

  @Test
  public void testLoadDataFromDatabaseInMap() throws SQLException {
    try (Connection connection = dbConnectionProvider.createConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM \"Teama_schema\".\"Flower\"")) {

      resultSet.next();
      resultSet.getInt("id");
      resultSet.getString("name");
      resultSet.getString("room");
      resultSet.getDate("date");
      resultSet.getInt("time");
      resultSet.getString("items");
      resultSet.getString("comment");
      resultSet.getString("employee");
      resultSet.getString("status");

      HashMap<Integer, Flower> flowerMap = flowerDAOImp.loadDataFromDatabaseInMap();

      assertEquals(20, flowerMap.size());
    }
  }

  // Add tests for other methods like Import, Export, add, delete, update, get, and getNextID

  // @Test
  // public void testImport() throws SQLException {
  //  HashMap<Integer, Flower> flowerMap = flowerDAOImp.Import("");

  // assertNotNull(flowerMap);
  // }

  // @Test
  // public void testExport() throws SQLException {
  //  flowerDAOImp.Export("");
  // }

  @Test
  public void testAdd() throws SQLException {
    flowerDAOImp.add(flower);
  }

  @Test
  public void testDelete() throws SQLException {
    flowerDAOImp.delete(flower);
  }

  @Test
  public void testUpdate() throws SQLException {
    flowerDAOImp.update(flower);
  }

  @Test
  public void testGet() throws SQLException {
    Flower result = flowerDAOImp.get(flower.getId());
    assertNotNull(result);
    assertEquals(flower.getId(), result.getId());
    assertEquals(flower.getName(), result.getName());
    Assertions.assertEquals(flower.getRoom(), result.getRoom());
    Assertions.assertEquals(flower.getDate().toString(), result.getDate().toString());
    assertEquals(flower.getTime(), result.getTime());
    assertEquals(flower.getItems(), result.getItems());
    assertEquals(flower.getComment(), result.getComment());
    assertEquals(flower.getEmployee(), result.getEmployee());
    assertEquals(flower.getStatus(), result.getStatus());
    assertEquals(flower.getCreator(), result.getCreator());
  }

  @Test
  public void testGetNextID() throws SQLException {
    int nextId = flowerDAOImp.getNextID();
    assertNotEquals(-1, nextId);
  }

  @AfterEach
  public void tearDown() throws SQLException {
    if (connection != null) {
      connection.close();
    }
  }
}
