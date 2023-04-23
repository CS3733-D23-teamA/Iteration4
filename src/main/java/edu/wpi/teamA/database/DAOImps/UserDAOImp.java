package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.IncorrectLengthException;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDAOImp {
  private ArrayList<User> UserArray;

  static DBConnectionProvider UserLoginProvider = new DBConnectionProvider();

  public UserDAOImp() {
    this.UserArray = new ArrayList<User>();
  }

  // Create database table for User
  public void createUserTable() {
    try {
      Statement stmtUser = UserLoginProvider.createConnection().createStatement();
      String sqlCreateUser =
          "CREATE TABLE IF NOT EXISTS \"Teama_schema\".\"Users\" ("
              + "adminYes   int,"
              + "userName   VARCHAR(255) PRIMARY KEY,"
              + "password   VARCHAR(255),"
              + "firstName  VARCHAR(255),"
              + "lastName   VARCHAR(255))";
      stmtUser.execute(sqlCreateUser);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  // Check if the user exists. If exists, pull the password from the database and check if it fits
  // If not exist, call addUser
  public User checkUser(String userName, String password) {
    try {
      PreparedStatement ps =
          UserLoginProvider.createConnection()
              .prepareStatement("SELECT * FROM \"Teama_schema\".\"Users\" WHERE userName = ?");
      ps.setString(1, userName);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        String storedPassword = rs.getString("password");
        if (password.equals(storedPassword)) {
          User returnUser =
              new User(
                  rs.getInt("adminYes"),
                  rs.getString("userName"),
                  rs.getString("password"),
                  rs.getString("firstName"),
                  rs.getString("lastName"));
          return returnUser;
        } else {
          User returnNoUser = new User(2, "N", "N", "N", "N");
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

  public void updatePassword(
      String password1, String password2, String newPassword1, String newPassword2) {
    try {
      PreparedStatement ps =
          UserLoginProvider.createConnection()
              .prepareStatement("SELECT * FROM \"Teama_schema\".\"Users\" WHERE userName = ?");
      ps.setString(1, AccountSingleton.INSTANCE1.getValue().getUserName());
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        String storedPassword = rs.getString("password");
        if (password1.equals(storedPassword)
            && password2.equals(storedPassword)
            && newPassword1.equals(newPassword2)) {

          // Update the password in the database
          PreparedStatement updatePs =
              UserLoginProvider.createConnection()
                  .prepareStatement(
                      "UPDATE \"Teama_schema\".\"Users\" SET password = ? WHERE userName = ?");
          updatePs.setString(1, newPassword1);
          updatePs.setString(2, AccountSingleton.INSTANCE1.getValue().getUserName());
          updatePs.executeUpdate();

          // Update the user object in AccountSingleton
          User returnUser =
              new User(
                  rs.getInt("adminYes"),
                  rs.getString("userName"),
                  newPassword1,
                  rs.getString("firstName"),
                  rs.getString("lastName"));
          AccountSingleton.INSTANCE1.setValue(returnUser);

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
      int adminYes, String userName, String password, String firstName, String lastName)
      throws IncorrectLengthException {
    try {

      PreparedStatement ps =
          UserLoginProvider.createConnection()
              .prepareStatement(
                  "INSERT INTO \"Teama_schema\".\"Users\" (adminYes, userName, password, firstName, lastName) VALUES (?, ?, ?, ?, ?)");
      ps.setInt(1, adminYes);
      ps.setString(2, userName);
      ps.setString(3, password);
      ps.setString(4, firstName);
      ps.setString(5, lastName);
      ps.executeUpdate();
      if (userName.length() < 5
          || password.length() < 5
          || firstName.length() < 1
          || lastName.length() < 1) {
        throw new IncorrectLengthException("Your attributes are not of the right length");
      }
      UserArray.add(new User(adminYes, userName, password, firstName, lastName));
      System.out.println("New user added successfully.");

    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (IncorrectLengthException e) {
      throw new RuntimeException(e);
    }
  }
}
