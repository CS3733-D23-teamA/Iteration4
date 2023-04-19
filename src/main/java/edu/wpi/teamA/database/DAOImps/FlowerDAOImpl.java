package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IFlowerDAO;
import edu.wpi.teamA.database.ORMclasses.FlowerEntity;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class FlowerDAOImpl implements IFlowerDAO {
  @Getter @Setter private HashMap<Integer, FlowerEntity> flowerMap = new HashMap<>();
  static DBConnectionProvider flowerProvider = new DBConnectionProvider();

  public FlowerDAOImpl() {
    this.flowerMap = loadFlowersFromDatabaseInMap();
  }

  public FlowerDAOImpl(HashMap<Integer, FlowerEntity> flowerMap) {
    this.flowerMap = flowerMap;
  }

  public HashMap<Integer, FlowerEntity> loadFlowersFromDatabaseInMap() {
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Flower\"");

      while (rs.next()) {
        int id = rs.getInt("id");
        String namee = rs.getString("namee");
        String room = rs.getString("room");
        Date date = rs.getDate("datee");
        int time = rs.getInt("timee");
        String flowerType = rs.getString("flowertype");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");

        FlowerEntity flower =
            new FlowerEntity(id, namee, room, date, time, flowerType, comment, employee, status);
        flowerMap.put(id, flower);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return flowerMap;
  }

  @Override
  public void addFlower(FlowerEntity flower) {
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

      String sqlCreateEdge =
          "Create Table if not exists \"Prototype2_schema\".\"Flower\""
              + "(id     int,"
              + "namee    Varchar(600),"
              + "room    VarChar(600),"
              + "datee    date,"
              + "timee     int,"
              + "flowerType     Varchar(600),"
              + "comment     Varchar(600),"
              + "employee Varchar(600),"
              + "status  Varchar(600))";
      Statement stmtFlower = flowerProvider.createConnection().createStatement();
      stmtFlower.execute(sqlCreateEdge);

      PreparedStatement ps =
          flowerProvider
              .createConnection()
              .prepareStatement(
                  "INSERT INTO \"Prototype2_schema\".\"Flower\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
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

      flowerMap.put(
          id, new FlowerEntity(id, name, room, date, time, type, comment, employee, status));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteFlower(FlowerEntity flower) {

    try {
      PreparedStatement ps =
          flowerProvider
              .createConnection()
              .prepareStatement("DELETE FROM \"Prototype2_schema\".\"Flower\" WHERE id = ?");
      ps.setInt(1, flower.getId());
      ps.executeUpdate();

      flowerMap.remove(flower.getId());
      // flowerArray.removeIf(flowerEntity -> flowerEntity.getId() == (flower.getId()));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public FlowerEntity getFlower(int id) {
    return flowerMap.get(id);
  }

  @Override
  public void updateFlower(FlowerEntity flower) {
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
          flowerProvider
              .createConnection()
              .prepareStatement(
                  "UPDATE \"Prototype2_schema\".\"Flower\" SET namee = ?, room = ?, datee = ?, timee = ?, flowerType = ?, comment = ?, employee = ?, status = ? WHERE id = ?");
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
          new FlowerEntity(
              flower.getId(), name, room, date, time, type, comment, employee, status));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int getNextID() {
    FlowerEntity largestID = null;
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs =
          st.executeQuery(
              "SELECT * FROM \"Prototype2_schema\".\"Flower\" ORDER BY id DESC LIMIT 1");

      if (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("namee");
        String room = rs.getString("room");
        Date date = rs.getDate("datee");
        int time = rs.getInt("timee");
        String flowerType = rs.getString("flowertype");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");

        largestID =
            new FlowerEntity(id, name, room, date, time, flowerType, comment, employee, status);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return largestID.getId() + 1;
  }
}
