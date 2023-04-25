package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IDatabaseDAO;
import edu.wpi.teamA.database.ORMclasses.Move;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class MoveDAOImp implements IDatabaseDAO<Move> {

  @Getter @Setter private HashMap<Integer, LinkedList<Move>> MoveMap = new HashMap<>();
  @Getter @Setter private LocalDate currentDate = LocalDate.now();

  public MoveDAOImp(HashMap<Integer, LinkedList<Move>> MoveMap) {
    this.MoveMap = MoveMap;
  }

  public MoveDAOImp() {
    this.MoveMap = loadDataFromDatabaseInMap();
  }

  public void createTable() {
    try {
      String sqlCreateEdge =
          "Create Table if not exists \"Teama_schema\".\"Move\""
              + "(nodeID   int,"
              + "longName  Varchar(600),"
              + "localDate     date,"
              + "CONSTRAINT fk_longname "
              + "FOREIGN KEY(longname) "
              + "REFERENCES \"Teama_schema\".\"LocationName\"(longname)"
              + "ON DELETE CASCADE,"
              + "CONSTRAINT fk_longname "
              + "FOREIGN KEY(longname) "
              + "REFERENCES \"Teama_schema\".\"LocationName\"(longname)"
              + "ON UPDATE CASCADE)";
      Statement stmtMove =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      stmtMove.execute(sqlCreateEdge);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public HashMap<Integer, LinkedList<Move>> loadDataFromDatabaseInMap() {
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Move\"");

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        String longName = rs.getString("longName");
        LocalDate localDate = rs.getDate("localDate").toLocalDate();

        if (!MoveMap.containsKey(localDate.hashCode())) {
          // create new linkedlist
          MoveMap.put(localDate.hashCode(), new LinkedList<>());
        }
        MoveMap.get(localDate.hashCode()).add(new Move(nodeID, longName, localDate));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return MoveMap;
  }

  public HashMap<Integer, LinkedList<Move>> Import(String filePath) {
    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        int nodeID = Integer.parseInt(data[0]);
        String longName = data[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate localDate = LocalDate.parse(data[2], formatter);

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.createConnection())
                .prepareStatement("INSERT INTO \"Teama_schema\".\"Move\" VALUES (?, ?, ?)");
        ps.setInt(1, nodeID);
        ps.setString(2, longName);
        ps.setDate(3, java.sql.Date.valueOf(localDate));
        ps.executeUpdate();

        if (!MoveMap.containsKey(localDate.hashCode())) {
          // create new linkedlist
          MoveMap.put(localDate.hashCode(), new LinkedList<>());
        }
        MoveMap.get(localDate.hashCode()).add(new Move(nodeID, longName, localDate));
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return MoveMap;
  }

  public void Export(String filePath) {
    try {
      String newFile = filePath + "/Move.csv";
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Move\"");

      FileWriter csvWriter = new FileWriter(newFile);
      csvWriter.append("nodeid,longname,localdate\n");

      while (rs.next()) {
        csvWriter.append(rs.getInt("nodeid") + ",");
        csvWriter.append(rs.getString("longname")).append(",");
        csvWriter.append(rs.getString("localdate")).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("Move table exported to Move.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** create a new instance of Move and Insert the new object into database */
  public Move Add(Move move) {
    Integer nodeID = move.getNodeID();
    String longName = move.getLongName();
    LocalDate localDate = move.getDate();
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement("INSERT INTO \"Teama_schema\".\"Move\" VALUES (?, ?, ?)");
      ps.setInt(1, nodeID);
      ps.setString(2, longName);
      ps.setDate(3, java.sql.Date.valueOf(localDate));
      ps.executeUpdate();
      move = new Move(nodeID, longName, localDate);
      if (!MoveMap.containsKey(localDate.hashCode())) {
        // create new linkedlist
        MoveMap.put(localDate.hashCode(), new LinkedList<>());
      }
      MoveMap.get(localDate.hashCode()).add(move);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return move;
  }

  public void Delete(Move move) {
    Integer nodeID = move.getNodeID();
    String longName = move.getLongName();
    LocalDate localDate = move.getDate();
    try {

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "DELETE FROM \"Teama_schema\".\"Move\" WHERE nodeid = ? AND longname = ? AND localdate = ?");
      ps.setInt(1, nodeID);
      ps.setString(2, longName);
      ps.setDate(3, java.sql.Date.valueOf(localDate));
      ps.executeUpdate();

      // MoveMap.remove(nodeID);
      // TODO check to see if works
      MoveMap.get(localDate.hashCode()).remove(move);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void Update(Move obj) {}

  public void Update(Move oldMove, Move newMove) {
    LocalDate localDate = newMove.getDate();
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"Move\" SET longname = ?, localdate = ?, nodeid = ?");
      ps.setString(1, newMove.getLongName());
      ps.setDate(2, java.sql.Date.valueOf(newMove.getDate()));
      ps.setInt(3, newMove.getNodeID());
      ps.executeUpdate();

      // TODO check to see if works
      MoveMap.get(localDate.hashCode()).remove(oldMove);
      // MoveMap.put(move.getNodeID(), move);
      if (!MoveMap.containsKey(localDate.hashCode())) {
        // create new linkedlist
        MoveMap.put(localDate.hashCode(), new LinkedList<>());
      }
      MoveMap.get(localDate.hashCode()).add(newMove);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Move getMoveForNode(int nodeID) {
    Move move = null;

    try {
      Statement st =
              Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs =
              st.executeQuery(
                      "SELECT *\n"
                              + "FROM \"Teama_schema\".\"Move\"\n"
                              + "WHERE\n"
                              + "        localdate < "
                              + currentDate
                              + " AND nodeID = "
                              + nodeID
                              + "\n"
                              + "ORDER BY localdate DESC LIMIT 1");

      move =
              new Move(
                      rs.getInt("nodeID"),
                      rs.getString("longname"),
                      LocalDate.parse(rs.getString("localdate"), DateTimeFormatter.ISO_DATE));

    } catch (SQLException e) {
      throw new RuntimeException("SQLException in MoveDAOImp.getMoveForNode()");
    }

    return move;
  }
}
