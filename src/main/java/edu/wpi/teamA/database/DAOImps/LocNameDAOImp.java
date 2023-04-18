package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.ILocNameDAO;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

public class LocNameDAOImp implements IDataBase, ILocNameDAO {

  // ArrayList<LocationName> LocNameArray = new ArrayList<LocationName>();
  @Getter @Setter
  private HashMap<String, LocationName> LocNameMap = new HashMap<String, LocationName>();

  private static DBConnectionProvider LocNameProvider = new DBConnectionProvider();

  public LocNameDAOImp(HashMap<String, LocationName> LocNameMap) {
    this.LocNameMap = LocNameMap;
  }

  public LocNameDAOImp() {
    this.LocNameMap = loadLocNamefromDatabase();
  }

  public static void createTable() {
    try {
      String sqlCreateEdge =
          "Create Table if not exists \"Prototype2_schema\".\"LocationName\""
              + "(longName     Varchar(600) PRIMARY KEY,"
              + "shortName     Varchar(600),"
              + "nodeType      Varchar(600))";
      Statement stmtLocName = LocNameProvider.createConnection().createStatement();
      stmtLocName.execute(sqlCreateEdge);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static HashMap<String, LocationName> loadLocNamesFromCSV(String filePath) {
    HashMap<String, LocationName> locationNames = new HashMap<String, LocationName>();

    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine(); // Skip the header line
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        String longName = data[0];
        String shortName = data[1];
        String nodetype = data[2];
        LocationName locationName = new LocationName(longName, shortName, nodetype);
        locationNames.put(longName, locationName);
      }

      csvReader.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return locationNames;
  }

  public static HashMap<String, LocationName> Import(String filePath) {
    HashMap<String, LocationName> LocNameMap = loadLocNamesFromCSV(filePath);

    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        PreparedStatement ps =
            LocNameProvider.createConnection()
                .prepareStatement(
                    "INSERT INTO \"Prototype2_schema\".\"LocationName\" VALUES (?, ?, ?)");
        ps.setString(1, data[0]);
        ps.setString(2, data[1]);
        ps.setString(3, data[2]);
        ps.executeUpdate();
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return LocNameMap;
  }

  public static void Export(String filePath) {
    try {
      String newFile = filePath + "/LocationName.csv";
      Statement st = LocNameProvider.createConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"LocationName\"");

      FileWriter csvWriter = new FileWriter(newFile);
      csvWriter.append("longname,shortname,nodetype\n");

      while (rs.next()) {
        csvWriter.append(rs.getString("longname")).append(",");
        csvWriter.append(rs.getString("shortname")).append(",");
        csvWriter.append(rs.getString("nodetype")).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("Location Name table exported to LocationName.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public HashMap<String, LocationName> loadLocNamefromDatabase() {
    // HashMap<String, LocationName> locationNames = new HashMap<String, LocationName>();

    try {
      Statement st = LocNameProvider.createConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"LocationName\"");

      while (rs.next()) {
        String longName = rs.getString("longName");
        String shortName = rs.getString("shortName");
        String nodetype = rs.getString("nodetype");

        LocationName locationName = new LocationName(longName, shortName, nodetype);
        LocNameMap.put(longName, locationName);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return LocNameMap;
  }

  public LocationName Add(String longName, String shortName, String nodetype) {
    LocationName locName = null;
    try {
      PreparedStatement ps =
          LocNameProvider.createConnection()
              .prepareStatement(
                  "INSERT INTO \"Prototype2_schema\".\"LocationName\" VALUES (?, ?, ?)");
      ps.setString(1, longName);
      ps.setString(2, shortName);
      ps.setString(3, nodetype);
      ps.executeUpdate();

      locName = new LocationName(longName, shortName, nodetype);
      LocNameMap.put(longName, locName);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return locName;
  }

  public void Delete(String longName) {
    try {

      PreparedStatement ps =
          LocNameProvider.createConnection()
              .prepareStatement(
                  "DELETE FROM \"Prototype2_schema\".\"LocationName\" WHERE longname = ? ");
      ps.setString(1, longName);
      ps.executeUpdate();

      LocNameMap.remove(longName);
      // LocNameArray.removeIf(locationName -> locationName.getLongName().equals(longName));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void Update(
      String oldLongName,
      String oldShortName,
      String newLongName,
      String newShortName,
      String newNodeType) {
    try {

      PreparedStatement ps =
          LocNameProvider.createConnection()
              .prepareStatement(
                  "UPDATE \"Prototype2_schema\".\"LocationName\" SET longname = ?, shortname = ?, nodetype = ? WHERE longname = ? AND shortname = ?");
      ps.setString(1, newLongName);
      ps.setString(2, newShortName);
      ps.setString(3, newNodeType);
      ps.setString(4, oldLongName);
      ps.setString(5, oldShortName);
      ps.executeUpdate();

      LocNameMap.put(newLongName, new LocationName(newLongName, newShortName, newNodeType));
      //      LocNameArray.forEach(
      //          locationName -> {
      //            if (locationName.getLongName().equals(oldLongName)
      //                && locationName.getShortName().equals(oldShortName)) {
      //              locationName.setLongName(newLongName);
      //              locationName.setShortName(newShortName);
      //              locationName.setNodeType(newNodeType);
      //            }
      //          });

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public LocationName getLocName(String longName) {
    LocationName locationName = null;
    try {
      PreparedStatement ps =
          LocNameProvider.createConnection()
              .prepareStatement(
                  "SELECT * FROM \"Prototype2_schema\".\"LocationName\" WHERE longname = ?");
      ps.setString(1, longName);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        String shortName = rs.getString("shortName");
        String nodeType = rs.getString("nodeType");
        locationName = new LocationName(longName, shortName, nodeType);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return locationName;
  }

  public ArrayList<String> filterLocType(String type) {
    ArrayList<String> lnList = new ArrayList<>();
    try {
      PreparedStatement ps =
          LocNameProvider.createConnection()
              .prepareStatement(
                  "SELECT * FROM \"Prototype2_schema\".\"LocationName\" WHERE nodetype = ?");
      ps.setString(1, type);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        String n = rs.getString("longName");
        lnList.add(n);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return lnList;
  }
}
