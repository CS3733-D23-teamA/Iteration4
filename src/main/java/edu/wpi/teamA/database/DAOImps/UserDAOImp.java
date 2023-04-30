package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class UserDAOImp {
  @Getter @Setter private HashMap<String, User> userMap = new HashMap<>();

  public UserDAOImp() {
    this.userMap = loadUsersFromDatabaseInMap();
  }

  // Create database table for User
  public void createUserTable() {
    try {
      Statement stmtUser = DBConnectionProvider.getInstance().createStatement();
      String sqlCreateUser =
          "CREATE TABLE IF NOT EXISTS \"Teama_schema\".\"Users\" ("
              + "adminYes   int,"
              + "userName   VARCHAR(255) PRIMARY KEY,"
              + "password   VARCHAR(255),"
              + "firstName  VARCHAR(255),"
              + "lastName   VARCHAR(255),"
              + "userID VARCHAR(255) PRIMARY KEY)";
      stmtUser.execute(sqlCreateUser);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public HashMap<String, User> loadUsersFromDatabaseInMap() {
    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Users\"");

      while (rs.next()) {
        int adminYes = rs.getInt("adminyes");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String firstname = rs.getString("firstname");
        String lastname = rs.getString("lastname");
        String userID = rs.getString("userid");

        User user = new User(adminYes, username, password, firstname, lastname, userID);
        userMap.put(username, user);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return userMap;
  }

  public HashMap<String, User> Import(String filePath) {
    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        int accessLevel = Integer.parseInt(data[0]);
        String userName = data[1];
        String password = data[2];
        String firstName = data[3];
        String lastName = data[4];
        String userID = data[5];

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.getInstance())
                .prepareStatement(
                    "INSERT INTO \"Teama_schema\".\"Users\" VALUES (?, ?, ?, ?, ?, ?)");
        ps.setInt(1, accessLevel);
        ps.setString(2, userName);
        ps.setString(3, password);
        ps.setString(4, firstName);
        ps.setString(5, lastName);
        ps.setString(6, userID);
        ps.executeUpdate();

        User user = new User(accessLevel, userName, password, firstName, lastName, userID);

        userMap.put(userName, user);
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return userMap;
  }

  public void Export(String folderExportPath) {
    try {
      String newFile = folderExportPath + "/User.csv";
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Users\"");

      FileWriter csvWriter = new FileWriter(newFile);

      csvWriter.append("adminyes,username,password,firstname,lastname,userid\n");

      while (rs.next()) {
        csvWriter.append((rs.getInt("adminyes")) + (","));
        csvWriter.append((rs.getString("username")) + (","));
        csvWriter.append((rs.getString("password")) + (","));
        csvWriter.append(rs.getString("firstname")).append(",");
        csvWriter.append((rs.getInt("userid")) + (","));
        csvWriter.append(rs.getString("lastname")).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("User table exported to User.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  // Check if the user exists. If exists, pull the password from the database and check if it fits
  // If not exist, call addUser
  public User checkUser(String userName, String password) {
    try {
      PreparedStatement ps =
          DBConnectionProvider.getInstance()
              .prepareStatement("SELECT * FROM \"Teama_schema\".\"Users\" WHERE userName = ?");
      ps.setString(1, userName);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        String storedPassword = rs.getString("password");
        if (password.equals(storedPassword)) {
          User returnUser =
              new User(
                  rs.getInt("adminyes"),
                  rs.getString("username"),
                  rs.getString("password"),
                  rs.getString("firstname"),
                  rs.getString("lastname"),
                  rs.getString("userid"));
          return returnUser;
        } else {
          User returnNoUser = new User(2, "N", "N", "N", "N", "N");
          return returnNoUser;
        }
      } else {
        System.out.println("User not found, add new user.");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  public User checkUserByID(String userID) {
    try {
      PreparedStatement ps =
          DBConnectionProvider.getInstance()
              .prepareStatement("SELECT * FROM \"Teama_schema\".\"Users\" WHERE userid = ?");
      ps.setString(1, userID);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return new User(
            rs.getInt("adminyes"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("firstname"),
            rs.getString("lastname"),
            rs.getString("userid"));
      } else {
        System.out.println("User not found, add new user.");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  public void updatePassword(
      String password1, String password2, String newPassword1, String newPassword2) {
    try {
      PreparedStatement ps =
          DBConnectionProvider.getInstance()
              .prepareStatement("SELECT * FROM \"Teama_schema\".\"Users\" WHERE userName = ?");
      ps.setString(1, AccountSingleton.INSTANCE.getValue().getUserName());
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        String storedPassword = rs.getString("password");
        if (password1.equals(storedPassword)
            && password2.equals(storedPassword)
            && newPassword1.equals(newPassword2)) {

          // Update the password in the database
          PreparedStatement updatePs =
              DBConnectionProvider.getInstance()
                  .prepareStatement(
                      "UPDATE \"Teama_schema\".\"Users\" SET password = ? WHERE userName = ?");
          updatePs.setString(1, newPassword1);
          updatePs.setString(2, AccountSingleton.INSTANCE.getValue().getUserName());
          updatePs.executeUpdate();

          // Update the user object in AccountSingleton
          User returnUser =
              new User(
                  rs.getInt("adminYes"),
                  rs.getString("userName"),
                  newPassword1,
                  rs.getString("firstName"),
                  rs.getString("lastName"),
                  rs.getString("userid"));
          AccountSingleton.INSTANCE.setValue(returnUser);

          // Update password in map
          userMap.put(AccountSingleton.INSTANCE.getValue().getUserName(), returnUser);

        } else {
          System.out.println("Incorrect Password");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void updateName(
      String oldFirstName, String oldLastName, String newFirstName, String newLastName) {
    try {
      PreparedStatement ps =
          DBConnectionProvider.getInstance()
              .prepareStatement("SELECT * FROM \"Teama_schema\".\"Users\" WHERE userName = ?");
      ps.setString(1, AccountSingleton.INSTANCE.getValue().getUserName());
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        String storedFirstName = rs.getString("firstName");
        String storedLastName = rs.getString("lastName");
        if (oldFirstName.equals(storedFirstName) && oldLastName.equals(storedLastName)) {

          // Update the password in the database
          PreparedStatement updatePs =
              DBConnectionProvider.getInstance()
                  .prepareStatement(
                      "UPDATE \"Teama_schema\".\"Users\" SET firstName = ? WHERE userName = ?");
          updatePs.setString(1, newFirstName);
          updatePs.setString(2, AccountSingleton.INSTANCE.getValue().getUserName());
          updatePs.executeUpdate();

          // Update the user object in AccountSingleton
          User returnUser =
              new User(
                  rs.getInt("adminYes"),
                  rs.getString("userName"),
                  rs.getString("password"),
                  rs.getString("firstName"),
                  rs.getString("lastName"),
                  rs.getString("userid"));
          AccountSingleton.INSTANCE.setValue(returnUser);

          // Update the user map
          userMap.put(AccountSingleton.INSTANCE.getValue().getUserName(), returnUser);

        } else {
          System.out.println("Incorrect Password");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  // Add the new user into the database
  // Also store the user into the array
  public void addUser(
      int adminYes,
      String userName,
      String password,
      String firstName,
      String lastName,
      String userID) {
    try {

      PreparedStatement ps =
          DBConnectionProvider.getInstance()
              .prepareStatement(
                  "INSERT INTO \"Teama_schema\".\"Users\" (adminyes, username, password, firstname, lastname, userid) VALUES (?, ?, ?, ?, ?, ?)");
      ps.setInt(1, adminYes);
      ps.setString(2, userName);
      ps.setString(3, password);
      ps.setString(4, firstName);
      ps.setString(5, lastName);
      ps.setString(6, userID);
      ps.executeUpdate();

      userMap.put(userName, new User(adminYes, userName, password, firstName, lastName, userID));
      System.out.println("New user added successfully.");

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
