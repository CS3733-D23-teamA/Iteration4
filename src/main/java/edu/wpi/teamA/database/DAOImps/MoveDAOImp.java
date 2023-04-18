package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IMoveDAO;
import edu.wpi.teamA.database.ORMclasses.Move;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

public class MoveDAOImp implements IDataBase, IMoveDAO {

  @Getter @Setter private HashMap<Integer, Move> MoveMap = new HashMap<Integer, Move>();

  private static DBConnectionProvider moveProvider = new DBConnectionProvider();

  public MoveDAOImp(HashMap<Integer, Move> MoveMap) {
    this.MoveMap = MoveMap;
  }

  public MoveDAOImp() {
    this.MoveMap = loadMovesFromDatabaseInMap();
  }

  public static void createSchema() {
    try {
      Statement stmtSchema = moveProvider.createConnection().createStatement();
      String sqlCreateSchema = "CREATE SCHEMA IF NOT EXISTS \"Prototype2_schema\"";
      stmtSchema.execute(sqlCreateSchema);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void createTable() {
    try {
      String sqlCreateEdge =
          "Create Table if not exists \"Prototype2_schema\".\"Move\""
              + "(nodeID   int PRIMARY KEY,"
              + "longName  Varchar(600),"
              + "localDate     date,"
              + "CONSTRAINT fk_longname "
              + "FOREIGN KEY(longname) "
              + "REFERENCES \"Prototype2_schema\".\"LocationName\"(longname)"
              + "ON DELETE CASCADE,"
              + "CONSTRAINT fk_longname "
              + "FOREIGN KEY(longname) "
              + "REFERENCES \"Prototype2_schema\".\"LocationName\"(longname)"
              + "ON UPDATE CASCADE)";
      Statement stmtMove = moveProvider.createConnection().createStatement();
      stmtMove.execute(sqlCreateEdge);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static HashMap<Integer, Move> loadMovesFromCSV(String filePath) {
    HashMap<Integer, Move> moves = new HashMap<Integer, Move>();

    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine(); // Skip the header line
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        int nodeID = Integer.parseInt(data[0]);
        String longName = data[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate localDate = LocalDate.parse(data[2], formatter);

        Move move = new Move(nodeID, longName, localDate);
        moves.put(nodeID, move);
      }

      csvReader.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return moves;
  }

  public static HashMap<Integer, Move> Import(String filePath) {
    MoveDAOImp.createSchema();
    HashMap<Integer, Move> MoveMap = loadMovesFromCSV(filePath);

    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        PreparedStatement ps =
            moveProvider
                .createConnection()
                .prepareStatement("INSERT INTO \"Prototype2_schema\".\"Move\" VALUES (?, ?, ?)");
        ps.setInt(1, Integer.parseInt(data[0]));
        ps.setString(2, data[1]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate localDate = LocalDate.parse(data[2], formatter);
        ps.setDate(3, java.sql.Date.valueOf(localDate));
        ps.executeUpdate();
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return MoveMap;
  }

  public static void Export(String filePath) {
    try {
      String newFile = filePath + "/Move.csv";
      Statement st = moveProvider.createConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Move\"");

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

  public ArrayList<Move> loadMovesFromDatabase() {
    ArrayList<Move> moves = new ArrayList<>();

    try {
      Statement st = moveProvider.createConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Move\"");

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        String longName = rs.getString("longName");
        LocalDate localDate = rs.getDate("localDate").toLocalDate();

        Move move = new Move(nodeID, longName, localDate);
        moves.add(move);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return moves;
  }

  public HashMap<Integer, Move> loadMovesFromDatabaseInMap() {
    // HashMap<Integer, Move> moves = new HashMap<Integer, Move>();

    try {
      Statement st = moveProvider.createConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Move\"");

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        String longName = rs.getString("longName");
        LocalDate localDate = rs.getDate("localDate").toLocalDate();

        Move move = new Move(nodeID, longName, localDate);
        // moves.put(move.getNodeID(), move);
        MoveMap.put(move.getNodeID(), move);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return MoveMap;
  }

  /** create a new instance of Move and Insert the new object into database */
  public Move Add(int nodeID, String longName, String dateString) {
    Move move = null;
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
      LocalDate localDate = LocalDate.parse(dateString, formatter);

      PreparedStatement ps =
          moveProvider
              .createConnection()
              .prepareStatement("INSERT INTO \"Prototype2_schema\".\"Move\" VALUES (?, ?, ?)");
      ps.setInt(1, nodeID);
      ps.setString(2, longName);
      ps.setDate(3, java.sql.Date.valueOf(localDate));
      ps.executeUpdate();
      move = new Move(nodeID, longName, localDate);
      MoveMap.put(nodeID, move);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return move;
  }

  public void Delete(int nodeID) {
    try {

      PreparedStatement ps =
          moveProvider
              .createConnection()
              .prepareStatement("DELETE FROM \"Prototype2_schema\".\"Move\" WHERE nodeid = ?");
      ps.setInt(1, nodeID);
      ps.executeUpdate();

      MoveMap.remove(nodeID);
      // MoveMap.removeIf(move -> move.getNodeID() == nodeID);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void Update(int nodeID, String longName, String dateString) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
      LocalDate localDate = LocalDate.parse(dateString, formatter);

      PreparedStatement ps =
          moveProvider
              .createConnection()
              .prepareStatement(
                  "UPDATE \"Prototype2_schema\".\"Move\" SET longname = ?, localdate = ? WHERE nodeid = ?");
      ps.setString(1, longName);
      ps.setDate(2, java.sql.Date.valueOf(localDate));
      ps.setInt(3, nodeID);
      ps.executeUpdate();

      MoveMap.put(nodeID, new Move(nodeID, longName, localDate));
      //      MoveArray.forEach(
      //          move -> {
      //            if (move.getNodeID() == nodeID) {
      //              move.setLongName(longName);
      //              move.setDate(localDate);
      //            }
      //          });

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Move getMove(int nodeID) {
    Move result = null;

    try {
      PreparedStatement ps =
          moveProvider
              .createConnection()
              .prepareStatement("SELECT * FROM \"Prototype2_schema\".\"Move\" WHERE nodeid = ?");
      ps.setInt(1, nodeID);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        String longName = rs.getString("longName");
        LocalDate localDate = rs.getDate("localDate").toLocalDate();

        result = new Move(nodeID, longName, localDate);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return result;
  }
}
