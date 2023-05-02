package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.ISignageDAO;
import edu.wpi.teamA.database.ORMclasses.SignageComponent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class SignageDAOImp implements ISignageDAO {

  @Getter @Setter private HashMap<Integer, SignageComponent> signageMap = new HashMap<>();

  public SignageDAOImp() {
    createTable();
    this.signageMap = loadSignagesFromDatabaseInMap();
  }

  public void createTable() {
    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();

      st.execute(
          "CREATE TABLE IF NOT EXISTS \"Teama_schema\".\"SignageComponent\" ("
              + "locationname VARCHAR(255),"
              + "direction VARCHAR(255) NOT NULL,"
              + "date DATE NOT NULL,"
              + "signageid INTEGER PRIMARY KEY,"
              + "screen INTEGER NOT NULL"
              + ")");
    } catch (SQLException e) {

      throw new RuntimeException(e);
    }
  }

  @Override
  public SignageComponent getSignage(int signageID) {
    return signageMap.get(signageID);
  }

  public HashMap<Integer, SignageComponent> loadSignagesFromDatabaseInMap() {
    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"SignageComponent\"");

      while (rs.next()) {
        String locationName = rs.getString("locationName");
        String direction = rs.getString("direction");
        Date date = rs.getDate("date");
        int signageID = rs.getInt("signageid");
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

  public HashMap<Integer, SignageComponent> Import(String filePath) {
    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        String locationName = data[0];
        String direction = data[1];
        Date date = Date.valueOf((data[2]));
        int signageID = Integer.parseInt(data[3]);
        int screen = Integer.parseInt(data[4]);

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.getInstance())
                .prepareStatement(
                    "INSERT INTO \"Teama_schema\".\"SignageComponent\" VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, locationName);
        ps.setString(2, direction);
        ps.setDate(3, date);
        ps.setInt(4, signageID);
        ps.setInt(5, screen);
        ps.executeUpdate();

        SignageComponent signageComponent =
            new SignageComponent(locationName, direction, date, screen, signageID);
        signageMap.put(signageID, signageComponent);
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return signageMap;
  }

  public void Export(String folderExportPath) {
    try {
      String newFile = folderExportPath + "/Signage.csv";
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"SignageComponent\"");

      FileWriter csvWriter = new FileWriter(newFile);

      csvWriter.append("locationname,direction,date,signageid,screen\n");

      while (rs.next()) {
        csvWriter.append(rs.getString("locationname")).append(",");
        csvWriter.append(rs.getString("direction")).append(",");
        csvWriter.append(String.valueOf(rs.getDate("date"))).append(",");
        csvWriter.append(String.valueOf(rs.getInt("signageid"))).append(",");
        csvWriter.append(String.valueOf((rs.getInt("screen")))).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("Signage table exported to Signage.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void modifySignage(SignageComponent signage) {
    try {
      String locationName = signage.getLocationName();
      String direction = signage.getDirection();
      Date date = signage.getDate();
      int screen = signage.getScreen();
      int signageID = signage.getSignageID();

      PreparedStatement ps =
          DBConnectionProvider.getInstance()
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"SignageComponent\" SET direction = ?, date = ?, screen = ?, locationname = ? WHERE signageid = ?");
      ps.setString(1, direction);
      ps.setDate(2, date);
      ps.setInt(3, screen);
      ps.setString(4, locationName);
      ps.setInt(5, signageID);

      ps.executeUpdate();

      signageMap.put(
          signageID, new SignageComponent(locationName, direction, date, screen, signageID));

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
      int signageID = signage.getSignageID();

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.getInstance())
              .prepareStatement(
                  "INSERT INTO \"Teama_schema\".\"SignageComponent\" VALUES (?, ?, ?, ?, ?)");
      ps.setString(1, locationName);
      ps.setString(2, direction);
      ps.setDate(3, date);
      ps.setInt(4, signageID);
      ps.setInt(5, screen);
      ps.executeUpdate();

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
          Objects.requireNonNull(DBConnectionProvider.getInstance())
              .prepareStatement(
                  "DELETE FROM \"Teama_schema\".\"SignageComponent\" WHERE signageid = ?");
      ps.setInt(1, signage.getSignageID());
      ps.executeUpdate();

      // String signageID = signage.getLocationName() + signage.getDate().toString();
      signageMap.remove(signage.getSignageID());

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int getNextID() {
    SignageComponent largestID = null;
    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs =
          st.executeQuery(
              "SELECT * FROM \"Teama_schema\".\"SignageComponent\" ORDER BY signageid DESC LIMIT 1");

      if (rs.next()) {
        String locationName = rs.getString("locationname");
        String name = rs.getString("direction");
        Date date = rs.getDate("date");
        int signageID = rs.getInt("signageid");
        int screen = rs.getInt("screen");

        largestID = new SignageComponent(locationName, name, date, screen, signageID);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    if (largestID == null) {
      return 1;
    } else {
      return largestID.getSignageID() + 1;
    }
  }
}
