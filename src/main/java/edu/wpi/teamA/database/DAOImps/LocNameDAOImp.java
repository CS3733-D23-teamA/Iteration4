package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IDatabaseDAO;
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

public class LocNameDAOImp implements IDatabaseDAO<LocationName> {

  @Getter @Setter private HashMap<String, LocationName> LocNameMap = new HashMap<>();

  public LocNameDAOImp() {
    createTable();
    this.LocNameMap = loadDataFromDatabaseInMap();
  }

  public void createTable() {
    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();

      st.execute(
          "Create Table if not exists \"Teama_schema\".\"LocationName\""
              + "(longName     Varchar(600) PRIMARY KEY,"
              + "shortName     Varchar(600) NOT NULL,"
              + "nodeType      Varchar(600) NOT NULL)");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public HashMap<String, LocationName> loadDataFromDatabaseInMap() {
    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
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
            Objects.requireNonNull(DBConnectionProvider.getInstance())
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
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
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
          Objects.requireNonNull(DBConnectionProvider.getInstance())
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
          Objects.requireNonNull(DBConnectionProvider.getInstance())
              .prepareStatement(
                  "DELETE FROM \"Teama_schema\".\"LocationName\" WHERE longname = ? ");
      ps.setString(1, longName);
      ps.executeUpdate();

      LocNameMap.remove(longName);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void Update(LocationName locName) {
    String longName = locName.getLongName();
    String shortName = locName.getShortName();
    String nodeType = locName.getNodeType();
    try {

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.getInstance())
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"LocationName\" SET shortname = ?, nodetype = ? WHERE longname = ?");
      ps.setString(1, shortName);
      ps.setString(2, nodeType);
      ps.setString(3, longName);
      ps.executeUpdate();

      LocNameMap.put(longName, new LocationName(longName, shortName, nodeType));

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
          Objects.requireNonNull(DBConnectionProvider.getInstance())
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
