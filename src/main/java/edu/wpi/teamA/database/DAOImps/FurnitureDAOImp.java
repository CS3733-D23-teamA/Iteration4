package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IServiceDAO;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import edu.wpi.teamA.database.ORMclasses.FurnitureRequest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class FurnitureDAOImp implements IServiceDAO<FurnitureRequest> {
  @Getter @Setter private HashMap<Integer, FurnitureRequest> furnitureMap = new HashMap<>();

  public FurnitureDAOImp() {
    this.furnitureMap = loadDataFromDatabaseInMap();
  }

  public FurnitureDAOImp(HashMap<Integer, FurnitureRequest> furnitureMap) {
    this.furnitureMap = furnitureMap;
  }

  public HashMap<Integer, FurnitureRequest> loadDataFromDatabaseInMap() {
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Furniture\"");

      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int time = rs.getInt("time");
        String items = rs.getString("items");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");
        String creator = rs.getString("creator");

        FurnitureRequest fr =
            new FurnitureRequest(
                id, name, room, date, time, items, comment, employee, status, creator);
        furnitureMap.put(id, fr);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return furnitureMap;
  }

  public HashMap<Integer, FurnitureRequest> Import(String filePath) {
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
        int time = Integer.parseInt(data[4]);
        String items = data[5];
        String comment = data[6];
        String employee = data[7];
        String status = data[8];
        String creator = data[9];

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.createConnection())
                .prepareStatement(
                    "INSERT INTO \"Teama_schema\".\"Furniture\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, room);
        ps.setDate(4, date);
        ps.setInt(5, time);
        ps.setString(6, items);
        ps.setString(7, comment);
        ps.setString(8, employee);
        ps.setString(9, status);
        ps.setString(10, creator);
        ps.executeUpdate();

        FurnitureRequest fr =
            new FurnitureRequest(
                id, name, room, date, time, items, comment, employee, status, creator);
        furnitureMap.put(id, fr);
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return furnitureMap;
  }

  public void Export(String folderExportPath) {
    try {
      String newFile = folderExportPath + "/Furniture.csv";
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Furniture\"");

      FileWriter csvWriter = new FileWriter(newFile);

      csvWriter.append("id,name,room,date,time,items,comment,employee,status,creator\n");

      while (rs.next()) {
        csvWriter.append((rs.getInt("id")) + (","));
        csvWriter.append((rs.getString("name")) + (","));
        csvWriter.append((rs.getString("room")) + (","));
        csvWriter.append(rs.getString("date")).append(",");
        csvWriter.append((rs.getInt("time")) + (","));
        csvWriter.append(rs.getString("items")).append(",");
        csvWriter.append(rs.getString("comment")).append(",");
        csvWriter.append(rs.getString("employee")).append(",");
        csvWriter.append(rs.getString("status")).append(",");
        csvWriter.append(rs.getString("creator")).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("Furniture table exported to Furniture.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void add(FurnitureRequest furniture) {
    /** Insert new node object to the existing node table */
    try {
      int id = furniture.getId();
      String name = furniture.getName();
      String room = furniture.getRoom();
      Date date = furniture.getDate();
      int time = furniture.getTime();
      String items = furniture.getItems();
      String comment = furniture.getComment();
      String employee = furniture.getEmployee();
      String status = furniture.getStatus();
      String creator = furniture.getCreator();

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "INSERT INTO \"Teama_schema\".\"Furniture\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      ps.setInt(1, id);
      ps.setString(2, name);
      ps.setString(3, room);
      ps.setDate(4, date);
      ps.setInt(5, time);
      ps.setString(6, items);
      ps.setString(7, comment);
      ps.setString(8, employee);
      ps.setString(9, status);
      ps.setString(10, creator);
      ps.executeUpdate();

      furnitureMap.put(
          id,
          new FurnitureRequest(
              id, name, room, date, time, items, comment, employee, status, creator));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public FurnitureRequest get(int ID) {
    return furnitureMap.get(ID);
  }

  public void delete(FurnitureRequest furniture) {

    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement("DELETE FROM \"Teama_schema\".\"Furniture\" WHERE id = ?");
      ps.setInt(1, furniture.getId());
      ps.executeUpdate();

      furnitureMap.remove(furniture.getId());

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void update(FurnitureRequest furniture) {
    try {
      int id = furniture.getId();
      String name = furniture.getName();
      String room = furniture.getRoom();
      Date date = furniture.getDate();
      int time = furniture.getTime();
      String items = furniture.getItems();
      String comment = furniture.getComment();
      String employee = furniture.getEmployee();
      String status = furniture.getStatus();
      String creator = furniture.getCreator();

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"Furniture\" SET name = ?, room = ?, date = ?, time = ?, items = ?, comment = ?, employee = ?, status = ?, creator = ? WHERE id = ?");
      ps.setString(1, name);
      ps.setString(2, room);
      ps.setDate(3, date);
      ps.setInt(4, time);
      ps.setString(5, items);
      ps.setString(6, comment);
      ps.setString(7, employee);
      ps.setString(8, status);
      ps.setString(9, creator);
      ps.setInt(10, id);
      ps.executeUpdate();

      furnitureMap.put(
          id,
          new FurnitureRequest(
              id, name, room, date, time, items, comment, employee, status, creator));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void edit(FurnitureRequest o, FurnitureRequest n) {
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
  public ArrayList<FurnitureRequest> getAssigned(String username) {
    ArrayList<FurnitureRequest> furnitureRequests = new ArrayList<>();

    for (Map.Entry<Integer, FurnitureRequest> entry : furnitureMap.entrySet()) {
      if(entry.getValue().getEmployee().equals(username)) {
        furnitureRequests.add(entry.getValue());
      }
    }

    return furnitureRequests;
  }

  @Override
  public ArrayList<FurnitureRequest> getCreated(String username) {
    ArrayList<FurnitureRequest> furnitureRequests = new ArrayList<>();

    for (Map.Entry<Integer, FurnitureRequest> entry : furnitureMap.entrySet()) {
      if(entry.getValue().getCreator().equals(username)) {
        furnitureRequests.add(entry.getValue());
      }
    }

    return furnitureRequests;
  }

  public FurnitureRequest getFurniture(int id) {
    return furnitureMap.get(id);
  }

  public int getNextID() {
    FurnitureRequest largestID = null;
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs =
          st.executeQuery("SELECT * FROM \"Teama_schema\".\"Furniture\" ORDER BY id DESC LIMIT 1");

      if (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int time = rs.getInt("time");
        String items = rs.getString("items");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");
        String creator = rs.getString("creator");

        largestID =
            new FurnitureRequest(
                id, name, room, date, time, items, comment, employee, status, creator);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    assert largestID != null;
    return largestID.getId() + 1;
  }
}
