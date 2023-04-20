package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IFlowerDAO;
import edu.wpi.teamA.database.ORMclasses.Flower;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class FlowerDAOImpl implements IFlowerDAO {
  @Getter @Setter private HashMap<Integer, Flower> flowerMap = new HashMap<>();
  public FlowerDAOImpl() {
    this.flowerMap = loadFlowersFromDatabaseInMap();
  }

  private FlowerDAOImpl(HashMap<Integer, Flower> flowerMap) {
    this.flowerMap = flowerMap;
  }

  private HashMap<Integer, Flower> loadDataFromCSV(String filePath) {
    HashMap<Integer, Flower> flowers = new HashMap<>();

    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine(); // Skip the header line
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        int id = Integer.parseInt(data[0]);
        String name = data[1];
        String room = data[2];
        Date date = java.sql.Date.valueOf(data[3]);
        int time = Integer.parseInt(data[4]);
        String flowerType = data[5];
        String comment = data[6];
        String employee = data[7];
        String status = data[8];

        Flower flower =
            new Flower(id, name, room, date, time, flowerType, comment, employee, status);
        flowers.put(id, flower);
      }

      csvReader.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return flowers;
  }

  public HashMap<Integer, Flower> loadFlowersFromDatabaseInMap() {
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
        String flowerType = rs.getString("flowertype");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");

        Flower flower =
            new Flower(id, name, room, date, time, flowerType, comment, employee, status);
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
        String flowerType = data[5];
        String comment = data[6];
        String employee = data[7];
        String status = data[8];

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.createConnection())
                .prepareStatement(
                    "INSERT INTO \"Teama_schema\".\"Flower\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, room);
        ps.setDate(4, date);
        ps.setInt(5, time);
        ps.setString(6, flowerType);
        ps.setString(7, comment);
        ps.setString(8, employee);
        ps.setString(9, status);
        ps.executeUpdate();

        Flower flower =
            new Flower(id, name, room, date, time, flowerType, comment, employee, status);
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

      csvWriter.append("nodeid,xcoord,ycoord,floor,building\n");

      while (rs.next()) {
        csvWriter.append((rs.getInt("id")) + (","));
        csvWriter.append((rs.getString("name")) + (","));
        csvWriter.append((rs.getString("room")) + (","));
        csvWriter.append(rs.getString("date")).append(",");
        csvWriter.append((rs.getInt("time")) + (","));
        csvWriter.append(rs.getString("flowertype")).append(",");
        csvWriter.append(rs.getString("comment")).append(",");
        csvWriter.append(rs.getString("employee")).append(",");
        csvWriter.append(rs.getString("status")).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("Flower table exported to Flower.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addFlower(Flower flower) {
    /** Insert new node object to the existing node table */
    try {
      int id = flower.getId();
      String name = flower.getName();
      String room = flower.getRoom();
      Date date = flower.getDate();
      int time = flower.getTime();
      String type = flower.getFlowerType();
      String comment = flower.getComment();
      String employee = flower.getEmployee();
      String status = flower.getStatus();

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "INSERT INTO \"Teama_schema\".\"Flower\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
      ps.setInt(1, id);
      ps.setString(2, name);
      ps.setString(3, room);
      ps.setDate(4, date);
      ps.setInt(5, time);
      ps.setString(6, type);
      ps.setString(7, comment);
      ps.setString(8, employee);
      ps.setString(9, status);
      ps.executeUpdate();

      flowerMap.put(id, new Flower(id, name, room, date, time, type, comment, employee, status));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteFlower(Flower flower) {

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

  @Override
  public void updateFlower(Flower flower) {
    try {
      int id = flower.getId();
      String name = flower.getName();
      String room = flower.getRoom();
      Date date = flower.getDate();
      int time = flower.getTime();
      String type = flower.getFlowerType();
      String comment = flower.getComment();
      String employee = flower.getEmployee();
      String status = flower.getStatus();

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"Flower\" SET name = ?, room = ?, date = ?, time = ?, flowerType = ?, comment = ?, employee = ?, status = ? WHERE id = ?");
      ps.setString(1, name);
      ps.setString(2, room);
      ps.setDate(3, date);
      ps.setInt(4, time);
      ps.setString(5, type);
      ps.setString(6, comment);
      ps.setString(7, employee);
      ps.setString(8, status);
      ps.setInt(9, id);
      ps.executeUpdate();

      flowerMap.put(
          flower.getId(),
          new Flower(flower.getId(), name, room, date, time, type, comment, employee, status));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Flower getFlower(int id) {
    return flowerMap.get(id);
  }

  public int getNextID() {
    Flower largestID = null;
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs =
          st.executeQuery(
              "SELECT * FROM \"Teama_schema\".\"Flower\" ORDER BY id DESC LIMIT 1");

      if (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int time = rs.getInt("time");
        String flowerType = rs.getString("flowertype");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");

        largestID = new Flower(id, name, room, date, time, flowerType, comment, employee, status);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    assert largestID != null;
    return largestID.getId() + 1;
  }
}
