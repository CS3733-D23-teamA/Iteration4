package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.ICRRRDAO;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class CRRRDAOImp implements ICRRRDAO {
  @Getter @Setter private ArrayList<ConferenceRoomResRequest> crrrArray;
  static DBConnectionProvider crrrProvider = new DBConnectionProvider();

  public CRRRDAOImp() {
    this.crrrArray = new ArrayList<>();
  }

  public CRRRDAOImp(ArrayList<ConferenceRoomResRequest> crrrArray) {
    this.crrrArray = crrrArray;
  }

  public void addCRRR(ConferenceRoomResRequest crrr) {
    try {
      String name = crrr.getName();
      String room = crrr.getRoom();
      Date date = crrr.getDate();
      int startTime = crrr.getStartTime();
      int endTime = crrr.getEndTime();
      String comment = crrr.getComment();
      String status = crrr.getStatus();

      String sqlCreateTable =
          "CREATE TABLE IF NOT EXISTS \"Teama_schema\".\"ConferenceRoomRequest\""
              + "(name VARCHAR(600),"
              + "room VARCHAR(600),"
              + "date DATE,"
              + "startTime INT,"
              + "endTime INT,"
              + "comment VARCHAR(600),"
              + "status VARCHAR(600))";
      Statement stmtCRRR = crrrProvider.createConnection().createStatement();
      stmtCRRR.execute(sqlCreateTable);

      PreparedStatement ps =
          crrrProvider
              .createConnection()
              .prepareStatement(
                  "INSERT INTO \"Teama_schema\".\"ConferenceRoomRequest\" VALUES (?, ?, ?, ?, ?, ?, ?)");
      ps.setString(1, name);
      ps.setString(2, room);
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
                  "DELETE FROM \"Teama_schema\".\"ConferenceRoomRequest\" WHERE name = ?");
      ps.setString(1, crrr.getName());
      ps.executeUpdate();

      crrrArray.removeIf(crrrEntity -> crrrEntity.getName().equals(crrr.getName()));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<ConferenceRoomResRequest> getAllCRRR() {
    ArrayList<ConferenceRoomResRequest> tempList = new ArrayList<>();
    try {
      Statement stmt = crrrProvider.createConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT * FROM \"Teama_schema\".\"ConferenceRoomRequest\"");

      while (rs.next()) {
        String namee = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int startTime = rs.getInt("starttime");
        int endTime = rs.getInt("endtime");
        String comment = rs.getString("comment");
        String status = rs.getString("status");

        ConferenceRoomResRequest temp = new ConferenceRoomResRequest();
        temp.setName(namee);
        temp.setRoom(room);
        temp.setDate(date);
        temp.setStartTime(startTime);
        temp.setEndTime(endTime);
        temp.setComment(comment);
        temp.setStatus(status);

        tempList.add(temp);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return tempList;
  }

  @Override
  public ConferenceRoomResRequest getCRRR(String name) {
    return null;
  }

  public void updateCRRR(ConferenceRoomResRequest crrr) {}
}
