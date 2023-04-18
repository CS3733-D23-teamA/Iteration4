package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IServiceDAO;
import edu.wpi.teamA.database.ORMclasses.MealEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealDAOImpl implements IServiceDAO<MealEntity> {
  ArrayList<MealEntity> mealArray;
  static DBConnectionProvider mealProvider = new DBConnectionProvider();

  public MealDAOImpl() {
    this.mealArray = new ArrayList<>();
  }

  public MealDAOImpl(ArrayList<MealEntity> mealArray) {
    this.mealArray = mealArray;
  }

  @Override
  public void add(MealEntity meal) {
    /** Insert new node object to the existing node table */
    try {
      String name = meal.getName();
      String room = meal.getRoom();
      Date date = meal.getDate();
      int time = meal.getTime();
      String type = meal.getMealType();
      String comment = meal.getComment();
      String status = meal.getStatus();

      String sqlCreateEdge =
          "Create Table if not exists \"Prototype2_schema\".\"Meal\""
              + "(namee    Varchar(600),"
              + "room    VarChar(600),"
              + "datee    date,"
              + "timee     int,"
              + "mealType     Varchar(600),"
              + "comment     Varchar(600),"
              + "status  Varchar(600))";
      Statement stmtMeal = mealProvider.createConnection().createStatement();
      stmtMeal.execute(sqlCreateEdge);

      PreparedStatement ps =
          mealProvider
              .createConnection()
              .prepareStatement(
                  "INSERT INTO \"Prototype2_schema\".\"Meal\" VALUES (?, ?, ?, ?, ?, ?, ?)");
      ps.setString(1, name);
      ps.setString(2, room);
      ps.setDate(3, date);
      ps.setInt(4, time);
      ps.setString(5, type);
      ps.setString(6, comment);
      ps.setString(7, status);
      ps.executeUpdate();

      mealArray.add(new MealEntity(name, room, date, time, type, comment, status));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(MealEntity meal) {

    try {
      PreparedStatement ps =
          mealProvider
              .createConnection()
              .prepareStatement("DELETE FROM \"Prototype2_schema\".\"Meal\" WHERE namee = ?");
      ps.setString(1, meal.getName());
      ps.executeUpdate();

      mealArray.removeIf(mealEntity -> mealEntity.getName().equals(meal.getName()));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<MealEntity> getAll() {
    ArrayList<MealEntity> tempList = new ArrayList<>();
    try {
      Statement stmt = mealProvider.createConnection().createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Meal\"");

      while (rs.next()) {
        String namee = rs.getString("namee");
        String room = rs.getString("room");
        Date date = rs.getDate("datee");
        int time = rs.getInt("timee");
        String mealType = rs.getString("mealType");
        String comment = rs.getString("comment");
        String status = rs.getString("status");

        MealEntity temp = new MealEntity();
        temp.setName(namee);
        temp.setRoom(room);
        temp.setDate(date);
        temp.setTime(time);
        temp.setMealType(mealType);
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
  public MealEntity get(String name) {
    MealEntity temp = new MealEntity();
    try {
      PreparedStatement ps =
          mealProvider
              .createConnection()
              .prepareStatement("SELECT FROM \"Prototype2_schema\".\"Meal\" WHERE namee = ?");
      ps.setString(1, name);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        String idk = rs.getString("namee");
        String room = rs.getString("room");
        Date date = rs.getDate("datee");
        int time = rs.getInt("timee");
        String mealType = rs.getString("mealType");
        String comment = rs.getString("comment");
        String status = rs.getString("status");

        temp.setName(idk);
        temp.setRoom(room);
        temp.setDate(date);
        temp.setTime(time);
        temp.setMealType(mealType);
        temp.setComment(comment);
        temp.setStatus(status);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return temp;
  }

  @Override
  public void update(MealEntity meal) {
    try {
      String room = meal.getRoom();
      Date date = meal.getDate();
      int time = meal.getTime();
      String type = meal.getMealType();
      String comment = meal.getComment();
      String status = meal.getStatus();

      PreparedStatement ps =
          mealProvider
              .createConnection()
              .prepareStatement(
                  "UPDATE Prototype2_schema.\"Meal\" SET room = ?, datee = ?, timee = ?, mealType = ?, comment = ?, status = ? WHERE namee = ?");
      ps.setString(1, room);
      ps.setDate(2, date);
      ps.setInt(3, time);
      ps.setString(4, type);
      ps.setString(5, comment);
      ps.setString(6, status);
      ps.executeUpdate();

      mealArray.forEach(
          MealEntity -> {
            if (MealEntity.getName().equals(meal.getName())) {
              MealEntity.setRoom(room);
              MealEntity.setDate(date);
              MealEntity.setTime(time);
              MealEntity.setMealType(type);
              MealEntity.setComment(comment);
              MealEntity.setStatus(status);
            }
          });
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
