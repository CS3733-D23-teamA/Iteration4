package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IServiceDAO;
import edu.wpi.teamA.database.ORMclasses.Meal;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class MealDAOImp implements IServiceDAO<Meal> {
  @Getter @Setter private HashMap<Integer, Meal> mealMap = new HashMap<>();

  public MealDAOImp() {
    this.mealMap = loadDataFromDatabaseInMap();
  }

  public MealDAOImp(HashMap<Integer, Meal> mealMap) {
    this.mealMap = mealMap;
  }

  public HashMap<Integer, Meal> loadDataFromDatabaseInMap() {
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Meal\"");

      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int time = rs.getInt("time");
        String items = rs.getString("mealtype");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");
        String creator = rs.getString("creator");

        Meal meal = new Meal(id, name, room, date, time, items, comment, employee, status, creator);
        mealMap.put(id, meal);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return mealMap;
  }

  public HashMap<Integer, Meal> Import(String filePath) {
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
        String mealType = data[5];
        String comment = data[6];
        String employee = data[7];
        String status = data[8];
        String creator = data[9];

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.createConnection())
                .prepareStatement(
                    "INSERT INTO \"Teama_schema\".\"Meal\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, room);
        ps.setDate(4, date);
        ps.setInt(5, time);
        ps.setString(6, mealType);
        ps.setString(7, comment);
        ps.setString(8, employee);
        ps.setString(9, status);
        ps.setString(10, creator);
        ps.executeUpdate();

        Meal meal =
            new Meal(id, name, room, date, time, mealType, comment, employee, status, creator);
        mealMap.put(id, meal);
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return mealMap;
  }

  public void Export(String folderExportPath) {
    try {
      String newFile = folderExportPath + "/Meal.csv";
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Flower\"");

      FileWriter csvWriter = new FileWriter(newFile);

      csvWriter.append("id,name,room,date,time,mealtype,comment,employee,status,creator\n");

      while (rs.next()) {
        csvWriter.append((rs.getInt("id")) + (","));
        csvWriter.append((rs.getString("name")) + (","));
        csvWriter.append((rs.getString("room")) + (","));
        csvWriter.append(rs.getString("date")).append(",");
        csvWriter.append((rs.getInt("time")) + (","));
        csvWriter.append(rs.getString("mealtype")).append(",");
        csvWriter.append(rs.getString("comment")).append(",");
        csvWriter.append(rs.getString("employee")).append(",");
        csvWriter.append(rs.getString("status")).append(",");
        csvWriter.append(rs.getString("creator")).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("Meal table exported to Meal.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void add(Meal meal) {
    /** Insert new node object to the existing node table */
    try {
      int id = meal.getId();
      String name = meal.getName();
      String room = meal.getRoom();
      Date date = meal.getDate();
      int time = meal.getTime();
      String type = meal.getItems();
      String comment = meal.getComment();
      String employee = meal.getEmployee();
      String status = meal.getStatus();
      String creator = meal.getCreator();

      PreparedStatement ps =
          DBConnectionProvider.createConnection()
              .prepareStatement(
                  "INSERT INTO \"Teama_schema\".\"Meal\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      ps.setInt(1, id);
      ps.setString(2, name);
      ps.setString(3, room);
      ps.setDate(4, date);
      ps.setInt(5, time);
      ps.setString(6, type);
      ps.setString(7, comment);
      ps.setString(8, employee);
      ps.setString(9, status);
      ps.setString(10, creator);
      ps.executeUpdate();

      mealMap.put(
          id, new Meal(id, name, room, date, time, type, comment, employee, status, creator));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Meal get(int ID) {
    return null;
  }

  public void delete(Meal meal) {

    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement("DELETE FROM \"Teama_schema\".\"Meal\" WHERE id = ?");
      ps.setInt(1, meal.getId());
      ps.executeUpdate();

      mealMap.remove(meal.getId());

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void update(Meal meal) {
    try {
      int id = meal.getId();
      String name = meal.getName();
      String room = meal.getRoom();
      Date date = meal.getDate();
      int time = meal.getTime();
      String type = meal.getItems();
      String comment = meal.getComment();
      String employee = meal.getEmployee();
      String status = meal.getStatus();
      String creator = meal.getCreator();

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"Meal\" SET name = ?, room = ?, date = ?, time = ?, mealtype = ?, comment = ?, employee = ?, status = ?, creator = ? WHERE id = ?");
      ps.setString(1, name);
      ps.setString(2, room);
      ps.setDate(3, date);
      ps.setInt(4, time);
      ps.setString(5, type);
      ps.setString(6, comment);
      ps.setString(7, employee);
      ps.setString(8, status);
      ps.setString(9, creator);
      ps.setInt(10, id);

      ps.executeUpdate();

      mealMap.put(
          id, new Meal(id, name, room, date, time, type, comment, employee, status, creator));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void edit(Meal o, Meal n) {
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
  public ArrayList<Meal> getAssigned(String username) {
    ArrayList<Meal> meals = new ArrayList<>();
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement("SELECT * FROM \"Prototype2_schema\".\"Meal\" WHERE employee = ?");
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int time = rs.getInt("time");
        String mealType = rs.getString("mealtype");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");
        String creator = rs.getString("creator");

        Meal temp =
            new Meal(id, name, room, date, time, mealType, comment, employee, status, creator);
        meals.add(temp);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return meals;
  }

  @Override
  public ArrayList<Meal> getCreated(String username) {
    ArrayList<Meal> meals = new ArrayList<>();
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement("SELECT * FROM \"Prototype2_schema\".\"Meal\" WHERE creator = ?");
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int time = rs.getInt("time");
        String mealType = rs.getString("mealtype");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");
        String creator = rs.getString("creator");

        Meal temp =
            new Meal(id, name, room, date, time, mealType, comment, employee, status, creator);
        meals.add(temp);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return meals;
  }

  public Meal getMeal(int id) {
    return mealMap.get(id);
  }

  public int getNextID() {
    Meal largestID = null;
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs =
          st.executeQuery("SELECT * FROM \"Teama_schema\".\"Meal\" ORDER BY id DESC LIMIT 1");

      if (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String room = rs.getString("room");
        Date date = rs.getDate("date");
        int time = rs.getInt("time");
        String mealType = rs.getString("mealtype");
        String comment = rs.getString("comment");
        String employee = rs.getString("employee");
        String status = rs.getString("status");
        String creator = rs.getString("creator");

        largestID =
            new Meal(id, name, room, date, time, mealType, comment, employee, status, creator);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    assert largestID != null;
    return largestID.getId() + 1;
  }
}
