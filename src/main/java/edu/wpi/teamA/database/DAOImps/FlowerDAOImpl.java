package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IFlowerDAO;
import edu.wpi.teamA.database.ORMclasses.FlowerEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlowerDAOImpl implements IFlowerDAO {
  ArrayList<FlowerEntity> flowerArray;
  static DBConnectionProvider flowerProvider = new DBConnectionProvider();

  public FlowerDAOImpl() {
    this.flowerArray = new ArrayList<>();
  }

  public FlowerDAOImpl(ArrayList<FlowerEntity> flowerArray) {
    this.flowerArray = flowerArray;
  }

  @Override
  public void addFlower(FlowerEntity flower) {
    /** Insert new node object to the existing node table */
    try {
      String name = flower.getName();
      String room = flower.getRoom();
      Date date = flower.getDate();
      int time = flower.getTime();
      String type = flower.getFlowerType();
      String comment = flower.getComment();
      String status = flower.getStatus();

      String sqlCreateEdge =
          "Create Table if not exists \"Prototype2_schema\".\"Flower\""
              + "(namee    Varchar(600),"
              + "room    VarChar(600),"
              + "datee    date,"
              + "timee     int,"
              + "flowerType     Varchar(600),"
              + "comment     Varchar(600),"
              + "status  Varchar(600))";
      Statement stmtFlower = flowerProvider.createConnection().createStatement();
      stmtFlower.execute(sqlCreateEdge);

      PreparedStatement ps =
          flowerProvider.createConnection()
              .prepareStatement(
                  "INSERT INTO \"Prototype2_schema\".\"Flower\" VALUES (?, ?, ?, ?, ?, ?, ?)");
      ps.setString(1, name);
      ps.setString(2, room);
      ps.setDate(3, date);
      ps.setInt(4, time);
      ps.setString(5, type);
      ps.setString(6, comment);
      ps.setString(7, status);
      ps.executeUpdate();

      flowerArray.add(new FlowerEntity(name, room, date, time, type, comment, status));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteFlower(FlowerEntity flower) {

    try {
      PreparedStatement ps =
          flowerProvider.createConnection()
              .prepareStatement("DELETE FROM \"Prototype2_schema\".\"Flower\" WHERE namee = ?");
      ps.setString(1, flower.getName());
      ps.executeUpdate();

      flowerArray.removeIf(flowerEntity -> flowerEntity.getName().equals(flower.getName()));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<FlowerEntity> getAllFlowers() {
    ArrayList<FlowerEntity> tempList = new ArrayList<>();
    try {
      Statement stmt = flowerProvider.createConnection().createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Flower\"");

      while (rs.next()) {
        String namee = rs.getString("namee");
        String room = rs.getString("room");
        Date date = rs.getDate("datee");
        int time = rs.getInt("timee");
        String flowerType = rs.getString("flowertype");
        String comment = rs.getString("comment");
        String status = rs.getString("status");

        FlowerEntity temp = new FlowerEntity();
        temp.setName(namee);
        temp.setRoom(room);
        temp.setDate(date);
        temp.setTime(time);
        temp.setFlowerType(flowerType);
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
  public FlowerEntity getFlower(String name) {
    FlowerEntity temp = new FlowerEntity();
    try {
      PreparedStatement ps =
          flowerProvider
              .createConnection()
              .prepareStatement("SELECT FROM \"Prototype2_schema\".\"Flower\" WHERE namee = ?");
      ps.setString(1, name);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        String idk = rs.getString("namee");
        String room = rs.getString("room");
        Date date = rs.getDate("datee");
        int time = rs.getInt("timee");
        String flowerType = rs.getString("flowertype");
        String comment = rs.getString("comment");
        String status = rs.getString("status");

        temp.setName(idk);
        temp.setRoom(room);
        temp.setDate(date);
        temp.setTime(time);
        temp.setComment(comment);
        temp.setStatus(status);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return temp;
  }

  @Override
  public void updateFlower(FlowerEntity flower) {
    try {
      String room = flower.getRoom();
      Date date = flower.getDate();
      int time = flower.getTime();
      String type = flower.getFlowerType();
      String comment = flower.getComment();
      String status = flower.getStatus();

      PreparedStatement ps =
          flowerProvider.createConnection()
              .prepareStatement(
                  "UPDATE Prototype2_schema.\"Flower\" SET room = ?, datee = ?, timee = ?, flowerType = ?, comment = ?, status = ? WHERE namee = ?");
      ps.setString(1, room);
      ps.setDate(2, date);
      ps.setInt(3, time);
      ps.setString(4, type);
      ps.setString(5, comment);
      ps.setString(5, status);
      ps.executeUpdate();

      flowerArray.forEach(
          FlowerEntity -> {
            if (FlowerEntity.getName().equals(flower.getName())) {
              FlowerEntity.setRoom(room);
              FlowerEntity.setDate(date);
              FlowerEntity.setTime(time);
              FlowerEntity.setComment(comment);
              FlowerEntity.setStatus(status);
            }
          });
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
