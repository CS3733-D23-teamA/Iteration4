package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.ISignageDAO;
import edu.wpi.teamA.database.ORMclasses.SignageComponent;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class SignageDAOImp implements ISignageDAO {

  @Getter @Setter private HashMap<String, SignageComponent> signageMap = new HashMap<>();
  static DBConnectionProvider signageProvider = new DBConnectionProvider();

  public SignageDAOImp() {
    this.signageMap = loadSignagesFromDatabaseInMap();
  }

  public SignageDAOImp(HashMap<String, SignageComponent> signageMap) {
    this.signageMap = signageMap;
  }

  @Override
  public SignageComponent getSignage(String signageID) {
    return signageMap.get(signageID);
  }

  public HashMap<String, SignageComponent> loadSignagesFromDatabaseInMap() {
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"SignageComponent\"");

      while (rs.next()) {
        String locationName = rs.getString("locationName");
        String direction = rs.getString("direction");
        Date date = rs.getDate("date");
        String signageID = locationName + date.toString();
        int screen = rs.getInt("screen");

        SignageComponent signage =
            new SignageComponent(locationName, direction, date, screen, signageID);
        signageMap.put(signageID, signage);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return signageMap;
  }

  @Override
  public void modifySignage(SignageComponent signage) {
    try {
      String locationName = signage.getLocationName();
      String direction = signage.getDirection();
      Date date = signage.getDate();
      int screen = signage.getScreen();
      String signageID = locationName + date.toString();

      PreparedStatement ps =
          signageProvider
              .createConnection()
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"SignageComponent\" SET direction = ?, date = ?, screen = ?, locationname = ? WHERE signageid = ?");
      ps.setString(1, direction);
      ps.setDate(2, date);
      ps.setInt(3, screen);
      ps.setString(4, locationName);
      ps.setString(5, signageID);

      signageMap.put(
          signageID,
          new SignageComponent(locationName, direction, date, screen, signage.getSignageID()));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addSignage(SignageComponent signage) {
    try {
      String locationName = signage.getLocationName();
      String direction = signage.getDirection();
      Date date = signage.getDate();
      int screen = signage.getScreen();

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "INSERT INTO \"Teama_schema\".\"SignageComponent\" VALUES (?, ?, ?, ?)");
      ps.setString(1, locationName);
      ps.setString(2, direction);
      ps.setDate(3, date);
      ps.setInt(4, screen);
      ps.executeUpdate();

      String signageID = locationName + date.toString();

      signageMap.put(
          signageID, new SignageComponent(locationName, direction, date, screen, signageID));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void removeSignage(SignageComponent signage) {
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "DELETE FROM \"Teama_schema\".\"SignageComponent\" WHERE signageid = ?");
      ps.setString(1, signage.getSignageID());
      ps.executeUpdate();

      // String signageID = signage.getLocationName() + signage.getDate().toString();
      signageMap.remove(signage.getSignageID());

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
