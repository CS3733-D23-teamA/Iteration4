package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.ICRRRDAO;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CRRRDAOImp implements ICRRRDAO {
  ArrayList<ConferenceRoomResRequest> crrrArray;
  static DBConnectionProvider crrrProvider = new DBConnectionProvider();

  public CRRRDAOImp() {
    this.crrrArray = new ArrayList<>();
  }

  public CRRRDAOImp(ArrayList<ConferenceRoomResRequest> crrrArray) {
    this.crrrArray = crrrArray;
  }

  public static void createTable() {
    try {
      String sqlCreateTable =
              "CREATE TABLE IF NOT EXISTS \"Prototype2_schema\".\"ConferenceRoomRequest\""
                      + "(name VARCHAR(600),"
                      + "room INT,"
                      + "date DATE,"
                      + "startTime INT,"
                      + "endTime INT,"
                      + "comment VARCHAR(600),"
                      + "status VARCHAR(600))";
      Statement stmtCRRR = crrrProvider.createConnection().createStatement();
      stmtCRRR.execute(sqlCreateTable);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void addCRRR(ConferenceRoomResRequest crrr) {
    try {
      String name = crrr.getName();
      int room = crrr.getRoom();
      Date date = crrr.getDate();
      int startTime = crrr.getStartTime();
      int endTime = crrr.getEndTime();
      String comment = crrr.getComment();
      String status = crrr.getStatus();

      PreparedStatement ps =
          crrrProvider.createConnection()
              .prepareStatement(
                  "INSERT INTO \"Prototype2_schema\".\"ConferenceRoomRequest\" VALUES (?, ?, ?, ?, ?, ?, ?)");
      ps.setString(1, name);
      ps.setInt(2, room);
      ps.setDate(3, date);
      ps.setInt(4, startTime);
      ps.setInt(5, endTime);
      ps.setString(6, comment);
      ps.setString(7, status);
      ps.executeUpdate();

      crrrArray.add(
          new ConferenceRoomResRequest(name, room, date, startTime, endTime, comment, status));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteCRRR(ConferenceRoomResRequest crrr) {
    try {
      PreparedStatement ps =
          crrrProvider
              .createConnection()
              .prepareStatement(
                  "DELETE FROM \"Prototype2_schema\".\"ConferenceRoomRequest\" WHERE name = ?");
      ps.setString(1, crrr.getName());
      ps.executeUpdate();

      crrrArray.removeIf(crrrEntity -> crrrEntity.getName().equals(crrr.getName()));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void updateCRRR(ConferenceRoomResRequest crrr) {}

  public void editCRRR(ConferenceRoomResRequest crrr) {}
}
