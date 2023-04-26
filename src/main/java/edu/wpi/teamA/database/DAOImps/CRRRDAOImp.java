package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IServiceDAO;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class CRRRDAOImp implements IServiceDAO<ConferenceRoomResRequest> {
  @Getter @Setter private HashMap<Integer, ConferenceRoomResRequest> crrrMap = new HashMap<>();

  public CRRRDAOImp() {
    this.crrrMap = loadDataFromDatabaseInMap();
  }

  public CRRRDAOImp(HashMap<Integer, ConferenceRoomResRequest> crrrMap) {
    this.crrrMap = crrrMap;
  }

  public HashMap<Integer, ConferenceRoomResRequest> loadDataFromDatabaseInMap() {
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"ConferenceRoomRequest\"");

      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int starttime = rs.getInt("starttime");
        int endtime = rs.getInt("endtime");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");
        String creator = rs.getString("creator");

        ConferenceRoomResRequest crrr =
            new ConferenceRoomResRequest(
                id, name, room, date, starttime, endtime, comment, employee, status, creator);
        crrrMap.put(id, crrr);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return crrrMap;
  }

  public HashMap<Integer, ConferenceRoomResRequest> Import(String filePath) {
    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        int id = Integer.parseInt(data[0]);
        String name = data[1];
        String room = data[2];
        Date date = java.sql.Date.valueOf(data[3]);
        int starttime = Integer.parseInt(data[4]);
        int endtime = Integer.parseInt(data[5]);
        String comment = data[6];
        String employee = data[7];
        String status = data[8];
        String creator = data[9];

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.createConnection())
                .prepareStatement(
                    "INSERT INTO \"Teama_schema\".\"ConferenceRoomRequest\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, room);
        ps.setDate(4, date);
        ps.setInt(5, starttime);
        ps.setInt(6, endtime);
        ps.setString(7, comment);
        ps.setString(8, employee);
        ps.setString(9, status);
        ps.setString(10, creator);
        ps.executeUpdate();

        ConferenceRoomResRequest crrr =
            new ConferenceRoomResRequest(
                id, name, room, date, starttime, endtime, comment, employee, status, creator);
        crrrMap.put(id, crrr);
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return crrrMap;
  }

  public void Export(String folderExportPath) {
    try {
      String newFile = folderExportPath + "/CRRR.csv";
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"ConferenceRoomRequest\"");

      FileWriter csvWriter = new FileWriter(newFile);

      csvWriter.append("id,name,room,date,starttime,endtime,comment,employee,status,creator\n");

      while (rs.next()) {
        csvWriter.append(String.valueOf((rs.getInt("id")))).append(",");
        csvWriter.append(rs.getString("name")).append(",");
        csvWriter.append(rs.getString("room")).append(",");
        csvWriter.append(rs.getString("date")).append(",");
        csvWriter.append(String.valueOf((rs.getInt("starttime")))).append(",");
        csvWriter.append(String.valueOf((rs.getInt("endtime")))).append(",");
        csvWriter.append(rs.getString("comment")).append(",");
        csvWriter.append(rs.getString("employee")).append(",");
        csvWriter.append(rs.getString("status")).append(",");
        csvWriter.append(rs.getString("creator")).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("CRRR table exported to CRRR.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void add(ConferenceRoomResRequest crrr) {
    try {
      int id = crrr.getId();
      String name = crrr.getName();
      String room = crrr.getRoom();
      Date date = crrr.getDate();
      int startTime = crrr.getStartTime();
      int endTime = crrr.getEndTime();
      String comment = crrr.getComment();
      String employee = crrr.getEmployee();
      String status = crrr.getStatus();
      String creator = crrr.getCreator();

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "INSERT INTO \"Teama_schema\".\"ConferenceRoomRequest\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      ps.setInt(1, id);
      ps.setString(2, name);
      ps.setString(3, room);
      ps.setDate(4, date);
      ps.setInt(5, startTime);
      ps.setInt(6, endTime);
      ps.setString(7, comment);
      ps.setString(8, employee);
      ps.setString(9, status);
      ps.setString(10, creator);
      ps.executeUpdate();

      crrrMap.put(
          id,
          new ConferenceRoomResRequest(
              id, name, room, date, startTime, endTime, comment, employee, status, creator));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ConferenceRoomResRequest get(int ID) {
    return crrrMap.get(ID);
  }

  public void delete(ConferenceRoomResRequest crrr) {
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "DELETE FROM \"Teama_schema\".\"ConferenceRoomRequest\" WHERE id = ?");
      ps.setInt(1, crrr.getId());
      ps.executeUpdate();

      crrrMap.remove(crrr.getId());

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void update(ConferenceRoomResRequest crrr) {
    try {
      int id = crrr.getId();
      String name = crrr.getName();
      String room = crrr.getRoom();
      Date date = crrr.getDate();
      int startTime = crrr.getStartTime();
      int endTime = crrr.getEndTime();
      String comment = crrr.getComment();
      String employee = crrr.getEmployee();
      String status = crrr.getStatus();
      String creator = crrr.getCreator();

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"ConferenceRoomRequest\" SET name = ?, room = ?, date = ?, starttime = ?, endtime = ?, comment = ?, employee = ?, status = ?, creator = ? WHERE id = ?");
      ps.setString(1, name);
      ps.setString(2, room);
      ps.setDate(3, date);
      ps.setInt(4, startTime);
      ps.setInt(5, endTime);
      ps.setString(6, comment);
      ps.setString(7, employee);
      ps.setString(8, status);
      ps.setString(9, creator);
      ps.setInt(10, id);
      ps.executeUpdate();

      crrrMap.put(
          id,
          new ConferenceRoomResRequest(
              id, name, room, date, startTime, endTime, comment, employee, status, creator));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void edit(ConferenceRoomResRequest o, ConferenceRoomResRequest n) {
    int id = o.getId();
    String employee = o.getEmployee();
    String status = o.getStatus();
    String creator = o.getCreator();

    delete(o);
    n.setId(id);
    n.setStatus(status);
    n.setEmployee(employee);
    n.setCreator(creator);
    add(n);
  }

  @Override
  public ArrayList<ConferenceRoomResRequest> getAssigned(String username) {
    ArrayList<ConferenceRoomResRequest> rooms = new ArrayList<>();
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "SELECT * FROM \"Teama_schema\".\"ConferenceRoomRequest\" WHERE employee = ?");
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int startTime = rs.getInt("starttime");
        int endTime = rs.getInt("endtime");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");
        String creator = rs.getString("creator");

        ConferenceRoomResRequest temp =
            new ConferenceRoomResRequest(
                id, name, room, date, startTime, endTime, comment, employee, status, creator);
        rooms.add(temp);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return rooms;
  }

  @Override
  public ArrayList<ConferenceRoomResRequest> getCreated(String username) {
    ArrayList<ConferenceRoomResRequest> rooms = new ArrayList<>();
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "SELECT * FROM \"Teama_schema\".\"ConferenceRoomRequest\" WHERE creator = ?");
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int startTime = rs.getInt("starttime");
        int endTime = rs.getInt("endtime");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");
        String creator = rs.getString("creator");

        ConferenceRoomResRequest temp =
            new ConferenceRoomResRequest(
                id, name, room, date, startTime, endTime, comment, employee, status, creator);
        rooms.add(temp);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return rooms;
  }

  public int getNextID() {
    ConferenceRoomResRequest largestID = null;
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs =
          st.executeQuery(
              "SELECT * FROM \"Teama_schema\".\"ConferenceRoomRequest\" ORDER BY id DESC LIMIT 1");

      if (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int startTime = rs.getInt("starttime");
        int endTime = rs.getInt("endtime");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");
        String creator = rs.getString("creator");

        largestID =
            new ConferenceRoomResRequest(
                id, name, room, date, startTime, endTime, comment, employee, status, creator);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    assert largestID != null;
    return largestID.getId() + 1;
  }

  public ArrayList<ConferenceRoomResRequest> filterDate(Date d) {
    ArrayList<ConferenceRoomResRequest> rooms = new ArrayList<>();
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "SELECT * FROM \"Teama_schema\".\"ConferenceRoomRequest\" WHERE date = ?");
      ps.setDate(1, d);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int startTime = rs.getInt("starttime");
        int endTime = rs.getInt("endtime");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");
        String creator = rs.getString("creator");

        ConferenceRoomResRequest temp =
            new ConferenceRoomResRequest(
                id, name, room, date, startTime, endTime, comment, employee, status, creator);
        rooms.add(temp);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return rooms;
  }

  public BufferedReader getBufferedReader(String filePath) throws FileNotFoundException {
    return new BufferedReader(new FileReader(filePath));
  }
}
