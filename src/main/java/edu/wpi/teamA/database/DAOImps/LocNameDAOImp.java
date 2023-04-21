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
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class LocNameDAOImp implements IDatabaseDAO, ILocNameDAO {

  @Getter @Setter private HashMap<String, LocationName> LocNameMap = new HashMap<>();

  public LocNameDAOImp(HashMap<String, LocationName> LocNameMap) {
    this.LocNameMap = LocNameMap;
  }

  public LocNameDAOImp() {
    this.LocNameMap = loadDataFromDatabaseInMap();
  }

  public void createTable() {
    try {
      String sqlCreateEdge =
          "Create Table if not exists \"Teama_schema\".\"LocationName\""
              + "(longName     Varchar(600) PRIMARY KEY,"
              + "shortName     Varchar(600),"
              + "nodeType      Varchar(600))";
      Statement stmtLocName =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      stmtLocName.execute(sqlCreateEdge);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  //  private HashMap<String, LocationName> loadDataFromCSV(String filePath) {
  //    HashMap<String, LocationName> locationNames = new HashMap<>();
  //
  //    try {
  //      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
  //      csvReader.readLine(); // Skip the header line
  //      String row;
  //
  //      while ((row = csvReader.readLine()) != null) {
  //        String[] data = row.split(",");
  //
  //        String longName = data[0];
  //        String shortName = data[1];
  //        String nodetype = data[2];
  //        LocationName locationName = new LocationName(longName, shortName, nodetype);
  //        locationNames.put(longName, locationName);
  //      }
  //
  //      csvReader.close();
  //    } catch (IOException e) {
  //      throw new RuntimeException(e);
  //    }
  //
  //    return locationNames;
  //  }

  public HashMap<String, LocationName> loadDataFromDatabaseInMap() {
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"LocationName\"");

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

  public HashMap<String, LocationName> Import(String filePath) {
    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        String longName = data[0];
        String shortName = data[1];
        String nodeType = data[2];

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.createConnection())
                .prepareStatement("INSERT INTO \"Teama_schema\".\"LocationName\" VALUES (?, ?, ?)");
        ps.setString(1, longName);
        ps.setString(2, shortName);
        ps.setString(3, nodeType);
        ps.executeUpdate();

        LocationName locName = new LocationName(longName, shortName, nodeType);
        LocNameMap.put(longName, locName);
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return LocNameMap;
  }

  public void Export(String filePath) {
    try {
      String newFile = filePath + "/LocationName.csv";
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"LocationName\"");

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

  public LocationName Add(LocationName locName) {
    String longName = locName.getLongName();
    String shortName = locName.getShortName();
    String nodetype = locName.getNodeType();
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement("INSERT INTO \"Teama_schema\".\"LocationName\" VALUES (?, ?, ?)");
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

  public void Delete(LocationName locName) {
    String longName = locName.getLongName();
    try {

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "DELETE FROM \"Teama_schema\".\"LocationName\" WHERE longname = ? ");
      ps.setString(1, longName);
      ps.executeUpdate();

      LocNameMap.remove(longName);

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
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"LocationName\" SET longname = ?, shortname = ?, nodetype = ? WHERE longname = ? AND shortname = ?");
      ps.setString(1, newLongName);
      ps.setString(2, newShortName);
      ps.setString(3, newNodeType);
      ps.setString(4, oldLongName);
      ps.setString(5, oldShortName);
      ps.executeUpdate();

      LocNameMap.put(newLongName, new LocationName(newLongName, newShortName, newNodeType));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public LocationName getLocName(String longName) {
    return LocNameMap.get(longName);
  }

  // TODO refactor
  public ArrayList<String> filterLocType(String type) {
    ArrayList<String> lnList = new ArrayList<>();
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "SELECT * FROM \"Teama_schema\".\"LocationName\" WHERE nodetype = ?");
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
