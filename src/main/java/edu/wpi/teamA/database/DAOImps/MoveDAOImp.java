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
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class MoveDAOImp implements IDatabaseDAO<Move> {

  // Used to store all moves
  @Getter @Setter private HashMap<Integer, LinkedList<Move>> MoveMap = new HashMap<>();
  // Used to store current moves that are being used
  @Getter @Setter private HashMap<Integer, Move> currentMoveMap = new HashMap<>();
  @Getter private final LocalDate currentDate = LocalDate.now();

  public MoveDAOImp(HashMap<Integer, LinkedList<Move>> MoveMap) {
    this.MoveMap = MoveMap;
  }

  public MoveDAOImp() {
    this.MoveMap = loadDataFromDatabaseInMap();
    this.currentMoveMap = loadCurrentMoveMap();
  }

  public void createTable() {
    try {
      String sqlCreateEdge =
          "Create Table if not exists \"Teama_schema\".\"Move\""
              + "(nodeID   int,"
              + "longName  Varchar(600),"
              + "localDate     Varchar(600),"
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

  public HashMap<Integer, Move> loadCurrentMoveMap() {
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Move\"");

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        String longName = rs.getString("longName");
        LocalDate localDate = rs.getDate("localDate").toLocalDate();

        updateCurrentMove(nodeID, longName, localDate);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return currentMoveMap;
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
        ps.setString(3, java.sql.Date.valueOf(localDate).toString());
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
      ps.setString(3, java.sql.Date.valueOf(localDate).toString());
      ps.executeUpdate();
      move = new Move(nodeID, longName, localDate);
      if (!MoveMap.containsKey(localDate.hashCode())) {
        // create new linkedlist
        MoveMap.put(localDate.hashCode(), new LinkedList<>());
      }
      MoveMap.get(localDate.hashCode()).add(move);

      updateCurrentMove(nodeID, longName, localDate);

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
      ps.setString(3, java.sql.Date.valueOf(localDate).toString());
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
      ps.setString(2, java.sql.Date.valueOf(newMove.getDate()).toString());
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

  public Move getMoveForNodeSlow(int nodeID) {
    Move move = null;

    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "SELECT * FROM \"Teama_schema\".\"Move\" WHERE localdate <= ? AND nodeid = ? ORDER BY localdate DESC LIMIT 1");
      ps.setString(1, currentDate.toString());
      ps.setInt(2, nodeID);
      // ps.executeUpdate();
      ps.execute();
      ps.getResultSet().next();

      move =
          new Move(
              ps.getResultSet().getInt("nodeid"),
              ps.getResultSet().getString("longname"),
              LocalDate.parse(
                  ps.getResultSet().getString("localdate"), DateTimeFormatter.ISO_DATE));

    } catch (SQLException e) {
      throw new RuntimeException("SQLException in MoveDAOImp.getMoveForNode()");
    }

    return move;
  }

  public Move getMoveForNode(int nodeID) {
    return currentMoveMap.get(nodeID);
  }

  public Move getMoveForLocName(String longname) {
    for (Map.Entry<Integer, Move> entry : currentMoveMap.entrySet()) {
      if (entry.getValue().getLongName().equals(longname)) {
        return entry.getValue();
      }
    }
    return null;
  }

  private void updateCurrentMove(int nodeID, String longName, LocalDate localDate) {
    if (currentMoveMap.containsKey(nodeID)) {
      // compare the value and the possible new one to see which should be there
      if (currentMoveMap.get(nodeID).getDate().isBefore(localDate)) {
        currentMoveMap.put(nodeID, new Move(nodeID, longName, localDate));
      }
    } else {
      currentMoveMap.put(nodeID, new Move(nodeID, longName, localDate));
    }
  }
}
