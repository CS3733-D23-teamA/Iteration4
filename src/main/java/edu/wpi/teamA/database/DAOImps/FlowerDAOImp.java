package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IServiceDAO;
import edu.wpi.teamA.database.ORMclasses.Flower;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class FlowerDAOImp implements IServiceDAO<Flower> {
  @Getter @Setter private HashMap<Integer, Flower> flowerMap = new HashMap<>();

  public FlowerDAOImp() {
    this.flowerMap = loadDataFromDatabaseInMap();
  }

  private FlowerDAOImp(HashMap<Integer, Flower> flowerMap) {
    this.flowerMap = flowerMap;
  }

  public HashMap<Integer, Flower> loadDataFromDatabaseInMap() {
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Flower\"");

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

        Flower flower =
            new Flower(id, name, room, date, time, items, comment, employee, status, creator);
        flowerMap.put(id, flower);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return flowerMap;
  }

  public HashMap<Integer, Flower> Import(String filePath) {
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
                    "INSERT INTO \"Teama_schema\".\"Flower\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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

        Flower flower =
            new Flower(id, name, room, date, time, items, comment, employee, status, creator);
        flowerMap.put(id, flower);
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return flowerMap;
  }

  public void Export(String folderExportPath) {
    try {
      String newFile = folderExportPath + "/Flower.csv";
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Flower\"");

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

      System.out.println("Flower table exported to Flower.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void add(Flower flower) {
    /** Insert new node object to the existing node table */
    try {
      int id = flower.getId();
      String name = flower.getName();
      String room = flower.getRoom();
      Date date = flower.getDate();
      int time = flower.getTime();
      String items = flower.getItems();
      String comment = flower.getComment();
      String employee = flower.getEmployee();
      String status = flower.getStatus();
      String creator = flower.getCreator();

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "INSERT INTO \"Teama_schema\".\"Flower\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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

      flowerMap.put(
          id, new Flower(id, name, room, date, time, items, comment, employee, status, creator));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void delete(Flower flower) {

    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement("DELETE FROM \"Teama_schema\".\"Flower\" WHERE id = ?");
      ps.setInt(1, flower.getId());
      ps.executeUpdate();

      flowerMap.remove(flower.getId());

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void update(Flower flower) {
    try {
      int id = flower.getId();
      String name = flower.getName();
      String room = flower.getRoom();
      Date date = flower.getDate();
      int time = flower.getTime();
      String items = flower.getItems();
      String comment = flower.getComment();
      String employee = flower.getEmployee();
      String status = flower.getStatus();
      String creator = flower.getCreator();

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"Flower\" SET name = ?, room = ?, date = ?, time = ?, items = ?, comment = ?, employee = ?, status = ?, creator = ? WHERE id = ?");
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

      flowerMap.put(
          id, new Flower(id, name, room, date, time, items, comment, employee, status, creator));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Flower get(int id) {
    return flowerMap.get(id);
  }

  @Override
  public ArrayList<Flower> getAssigned(String username) {
    ArrayList<Flower> flowers = new ArrayList<>();

    for (Map.Entry<Integer, Flower> entry : flowerMap.entrySet()) {
      if (entry.getValue().getEmployee().equals(username)) {
        flowers.add(entry.getValue());
      }
    }

    return flowers;
  }

  @Override
  public ArrayList<Flower> getCreated(String username) {
    ArrayList<Flower> flowers = new ArrayList<>();

    for (Map.Entry<Integer, Flower> entry : flowerMap.entrySet()) {
      if (entry.getValue().getCreator().equals(username)) {
        flowers.add(entry.getValue());
      }
    }

    return flowers;
  }

  @Override
  public int getNextID() {
    Flower largestID = null;
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs =
          st.executeQuery("SELECT * FROM \"Teama_schema\".\"Flower\" ORDER BY id DESC LIMIT 1");

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
            new Flower(id, name, room, date, time, items, comment, employee, status, creator);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    assert largestID != null;
    return largestID.getId() + 1;
  }
}
