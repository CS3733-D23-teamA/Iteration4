package edu.wpi.teamA.databaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.DAOImps.CRRRDAOImp;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
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

public class CRRRDAOImpTest {

  private CRRRDAOImp crrrDAOImp;
  private DBConnectionProvider dbConnectionProvider;
  private Connection connection;
  private ConferenceRoomResRequest crrr;

  @BeforeEach
  public void setUp() {
    crrrDAOImp = new CRRRDAOImp();
    crrr =
        new ConferenceRoomResRequest(
            1,
            "testName",
            "testRoom",
            new Date(System.currentTimeMillis()),
            1000,
            1100,
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
        ResultSet resultSet =
            statement.executeQuery("SELECT * FROM \"Teama_schema\".\"ConferenceRoomRequest\"")) {

      resultSet.next();
      resultSet.getInt("id");
      resultSet.getString("name");
      resultSet.getString("room");
      resultSet.getDate("date");
      resultSet.getInt("starttime");
      resultSet.getInt("endtime");
      resultSet.getString("comment");
      resultSet.getString("employee");
      resultSet.getString("status");

      HashMap<Integer, ConferenceRoomResRequest> crrrMap = crrrDAOImp.loadDataFromDatabaseInMap();

      assertEquals(2, crrrMap.size());
    }
  }

  // Add tests for other methods like Import, Export, add, delete, update, getCRRR, and getNextID

  // @Test
  // public void testImport() throws SQLException {
  //  HashMap<Integer, ConferenceRoomResRequest> crrrMap = crrrDAOImp.Import("");

  // assertNotNull(crrrMap);
  // }

  // @Test
  // public void testExport() throws SQLException {
  //  crrrDAOImp.Export("");
  // }

  @Test
  public void testAdd() throws SQLException {
    crrrDAOImp.add(crrr);
  }

  @Test
  public void testDelete() throws SQLException {
    crrrDAOImp.delete(crrr);
  }

  @Test
  public void testUpdate() throws SQLException {
    crrrDAOImp.update(crrr);
  }

  @Test
  public void testGetCRRR() throws SQLException {
    ConferenceRoomResRequest result = crrrDAOImp.get(crrr.getId());
    assertNotNull(result);
    assertEquals(crrr.getId(), result.getId());
    assertEquals(crrr.getName(), result.getName());
    Assertions.assertEquals(crrr.getRoom(), result.getRoom());
    Assertions.assertEquals(crrr.getDate().toString(), result.getDate().toString());
    assertEquals(crrr.getStartTime(), result.getStartTime());
    assertEquals(crrr.getEndTime(), result.getEndTime());
    assertEquals(crrr.getComment(), result.getComment());
    assertEquals(crrr.getEmployee(), result.getEmployee());
    assertEquals(crrr.getStatus(), result.getStatus());
    assertEquals(crrr.getCreator(), result.getCreator());
  }

  @Test
  public void testGetNextID() throws SQLException {
    int nextId = crrrDAOImp.getNextID();
    assertNotEquals(-1, nextId);
  }

  @AfterEach
  public void tearDown() throws SQLException {
    if (connection != null) {
      connection.close();
    }
  }
}
