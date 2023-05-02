package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.App;
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
import java.util.*;
import lombok.Getter;
import lombok.Setter;

public class MoveDAOImp implements IDatabaseDAO<Move> {

  // Used to store all moves
  @Getter @Setter private HashMap<Integer, LinkedList<Move>> nodeMoveMap = new HashMap<>();
  // Used to store current moves that are being used by location name
  @Getter @Setter private HashMap<String, Move> currentMoveMap = new HashMap<>();

  public MoveDAOImp() {
    loadDataFromDatabaseInMap();
    this.currentMoveMap = loadCurrentMoveMap(App.getCurrentDate());
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
          Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      stmtMove.execute(sqlCreateEdge);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public HashMap<Integer, LinkedList<Move>> loadDataFromDatabaseInMap() {
    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Move\"");

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        String longName = rs.getString("longName");
        LocalDate localDate = rs.getDate("localDate").toLocalDate();

        Move move = new Move(nodeID, longName, localDate);

        if (!nodeMoveMap.containsKey(nodeID)) {
          // create new linkedlist
          nodeMoveMap.put(nodeID, new LinkedList<>());
        }
        nodeMoveMap.get(nodeID).add(move);
      }
      checkDuplicateLocationNames();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return nodeMoveMap;
  }

  /**
   * Loads the currentMoveMap hashmap
   *
   * @param date the current date
   * @return the currentMoveMap hashmap
   */
  public HashMap<String, Move> loadCurrentMoveMap(LocalDate date) {
    HashMap<String, Move> map = new HashMap<>();
    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Move\"");

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        String longName = rs.getString("longName");
        LocalDate localDate = rs.getDate("localDate").toLocalDate();

        updateCurrentMove(map, nodeID, longName, localDate, date);
      }
      checkDuplicateLocationNames();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    // printMap();
    return map;
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
            Objects.requireNonNull(DBConnectionProvider.getInstance())
                .prepareStatement("INSERT INTO \"Teama_schema\".\"Move\" VALUES (?, ?, ?)");
        ps.setInt(1, nodeID);
        ps.setString(2, longName);
        ps.setString(3, java.sql.Date.valueOf(localDate).toString());
        ps.executeUpdate();

        Move move = new Move(nodeID, longName, localDate);

        if (!nodeMoveMap.containsKey(nodeID)) {
          // create new linkedlist
          nodeMoveMap.put(nodeID, new LinkedList<>());
        }
        nodeMoveMap.get(nodeID).add(move);
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return nodeMoveMap;
  }

  public void Export(String filePath) {
    try {
      String newFile = filePath + "/Move.csv";
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
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
          Objects.requireNonNull(DBConnectionProvider.getInstance())
              .prepareStatement("INSERT INTO \"Teama_schema\".\"Move\" VALUES (?, ?, ?)");
      ps.setInt(1, nodeID);
      ps.setString(2, longName);
      ps.setString(3, java.sql.Date.valueOf(localDate).toString());
      ps.executeUpdate();
      move = new Move(nodeID, longName, localDate);

      if (!nodeMoveMap.containsKey(nodeID)) {
        // create new linkedlist
        nodeMoveMap.put(nodeID, new LinkedList<>());
      }
      nodeMoveMap.get(nodeID).add(move);

      updateCurrentMove(currentMoveMap, nodeID, longName, localDate, App.getCurrentDate());
      checkDuplicateLocationNames();
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
          Objects.requireNonNull(DBConnectionProvider.getInstance())
              .prepareStatement(
                  "DELETE FROM \"Teama_schema\".\"Move\" WHERE nodeid = ? AND longname = ? AND localdate = ?");
      ps.setInt(1, nodeID);
      ps.setString(2, longName);
      ps.setString(3, java.sql.Date.valueOf(localDate).toString());
      ps.executeUpdate();

      Move currentMove = currentMoveMap.get(move.getLongName());
      if (currentMove != null) {
        if (Objects.equals(currentMove.getNodeID(), move.getNodeID())
            && currentMove.getDate().equals(move.getDate())) {
          currentMoveMap.remove(move.getLongName());
        }
      }
      nodeMoveMap.get(nodeID).remove(move);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void Update(Move obj) {}

  public void Update(Move oldMove, Move newMove) {
    Delete(oldMove);
    Add(newMove);
  }

  /**
   * Gets the first move associated to the node
   *
   * @param nodeID the node
   * @return the move
   */
  public Move getFirstMoveForNode(int nodeID) {
    LinkedList<Move> movesForNode = nodeMoveMap.get(nodeID);
    for (Move move : movesForNode) {
      if (!move.getDate().isAfter(App.getCurrentDate()) // if move is before current date
          && (Objects.equals(currentMoveMap.get(move.getLongName()).getNodeID(), move.getNodeID()))
          && (currentMoveMap.get(move.getLongName()).getDate().isEqual(move.getDate()))) {
        return move;
      }
    }
    return null;
  }

  /**
   * Gets the second move associated to the node if it exists
   *
   * @param nodeID the node
   * @return the second move or the first move if no second move exists
   */
  public Move getSecondMoveForNode(int nodeID) {
    Move firstMove = getFirstMoveForNode(nodeID);
    LinkedList<Move> movesForNode = nodeMoveMap.get(nodeID);
    for (Move move : movesForNode) {
      if (!move.getDate().isAfter(App.getCurrentDate())
          && (Objects.equals(currentMoveMap.get(move.getLongName()).getNodeID(), move.getNodeID()))
          && (currentMoveMap.get(move.getLongName()).getDate().isEqual(move.getDate()))
          && !move.getLongName().equals(firstMove.getLongName())) {
        return move;
      }
    }
    return firstMove;
  }

  /**
   * Checks to see if the move provided can be added into the currentMoveMap
   *
   * @param map map to check
   * @param nodeID nodeID for move
   * @param longName longName for move
   * @param localDate localDate for move
   * @param currentDate currentDate to check for
   */
  private void updateCurrentMove(
      HashMap<String, Move> map,
      int nodeID,
      String longName,
      LocalDate localDate,
      LocalDate currentDate) {
    // check if in future
    if (!localDate.isAfter(currentDate)) {
      if (map.containsKey(longName)) {
        // compare the value and the possible new one to see which should be there
        if (map.get(longName).getDate().isBefore(localDate)) {
          map.put(longName, new Move(nodeID, longName, localDate));
        }
      } else {
        map.put(longName, new Move(nodeID, longName, localDate));
      }
    }
  }

  /** Checks if a location name shows mutliple times in the current move map */
  private void checkDuplicateLocationNames() {
    HashMap<String, Move> locationNamesMap = new HashMap<>();
    HashMap<String, Move> currentMoveMapCopy = new HashMap<>(currentMoveMap);
    for (Map.Entry<String, Move> entry : currentMoveMapCopy.entrySet()) {
      String longname = entry.getValue().getLongName();
      if (locationNamesMap.containsKey(longname)) {
        // if current entry is a later date and not in the future, replace location name
        if (entry.getValue().getDate().isAfter(locationNamesMap.get(longname).getDate())
            && (entry.getValue().getDate().isBefore(App.getCurrentDate())
                || entry.getValue().getDate().isEqual(App.getCurrentDate()))) {
          currentMoveMap.remove(locationNamesMap.get(longname).getLongName());
          locationNamesMap.put(longname, entry.getValue());
        } else {
          currentMoveMap.remove(entry.getValue().getLongName());
        }
      } else {
        locationNamesMap.put(entry.getValue().getLongName(), entry.getValue());
      }
    }
  }
}
