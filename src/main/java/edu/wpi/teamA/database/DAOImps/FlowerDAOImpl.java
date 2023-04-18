package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IFlowerDAO;
import edu.wpi.teamA.database.ORMclasses.FlowerEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class FlowerDAOImpl implements IFlowerDAO {
  @Getter @Setter private ArrayList<FlowerEntity> flowerArray;
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

      flowerArray.add(
          new FlowerEntity(id, name, room, date, time, type, comment, employee, status));

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
      ps.setString(1, flower.getName());
      ps.executeUpdate();

      flowerArray.removeIf(flowerEntity -> flowerEntity.getId() == (flower.getId()));

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
        int id = rs.getInt("id");
        String namee = rs.getString("namee");
        String room = rs.getString("room");
        Date date = rs.getDate("datee");
        int time = rs.getInt("timee");
        String flowerType = rs.getString("flowertype");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");

        FlowerEntity temp =
            new FlowerEntity(id, namee, room, date, time, flowerType, comment, employee, status);
        //        temp.setName(namee);
        //        temp.setRoom(room);
        //        temp.setDate(date);
        //        temp.setTime(time);
        //        temp.setFlowerType(flowerType);
        //        temp.setComment(comment);
        //        temp.setStatus(status);

        tempList.add(temp);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return tempList;
  }

  @Override
  public FlowerEntity getFlower(int id) {
    FlowerEntity temp = new FlowerEntity();
    try {
      PreparedStatement ps =
          flowerProvider
              .createConnection()
              .prepareStatement("SELECT FROM \"Prototype2_schema\".\"Flower\" WHERE id = ?");
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        int tempID = rs.getInt("id");
        String namee = rs.getString("namee");
        String room = rs.getString("room");
        Date date = rs.getDate("datee");
        int time = rs.getInt("timee");
        String flowerType = rs.getString("flowertype");
        String comment = rs.getString("comment");
        String status = rs.getString("status");

        temp.setId(tempID);
        temp.setName(namee);
        temp.setRoom(room);
        temp.setDate(date);
        temp.setTime(time);
        temp.setFlowerType(flowerType);
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
                  "UPDATE Prototype2_schema.\"Flower\" SET name = ?, room = ?, datee = ?, timee = ?, flowerType = ?, comment = ?, employee = ?, status = ? WHERE id = ?");
      ps.setString(1, name);
      ps.setString(2, room);
      ps.setDate(3, date);
      ps.setInt(4, time);
      ps.setString(5, type);
      ps.setString(6, comment);
      ps.setString(7, employee);
      ps.setString(8, status);
      ps.executeUpdate();

      flowerArray.forEach(
          flowerEntity -> {
            if (flowerEntity.getId() == (flower.getId())) {
              flowerEntity.setName(name);
              flowerEntity.setRoom(room);
              flowerEntity.setDate(date);
              flowerEntity.setTime(time);
              flowerEntity.setFlowerType(type);
              flowerEntity.setComment(comment);
              flowerEntity.setEmployee(employee);
              flowerEntity.setStatus(status);
            }
          });
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
