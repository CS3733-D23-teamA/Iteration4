package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.ORMclasses.Node;
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

public class NodeDAOImp implements IDatabaseDAO<Node> {
  // ArrayList<Node> NodeArray;
  @Getter @Setter private HashMap<Integer, Node> NodeMap = new HashMap<>();

  public NodeDAOImp(HashMap<Integer, Node> NodeMap) {
    this.NodeMap = NodeMap;
  }

  public NodeDAOImp() {
    this.NodeMap = loadDataFromDatabaseInMap();
  }

  public void createTable() {
    try {
      String sqlCreateNode =
          "CREATE TABLE IF NOT EXISTS \"Teama_schema\".\"Node\""
              + "(nodeID   INT PRIMARY KEY,"
              + "xcoord    INT,"
              + "ycoord    INT,"
              + "floor     VARCHAR(600),"
              + "building  VARCHAR(600))";
      Statement stmtNode =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      stmtNode.execute(sqlCreateNode);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  //  private HashMap<Integer, Node> loadDataFromCSV(String filePath) {
  //    HashMap<Integer, Node> nodes = new HashMap<>();
  //
  //    try {
  //      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
  //      csvReader.readLine(); // Skip the header line
  //      String row;
  //
  //      while ((row = csvReader.readLine()) != null) {
  //        String[] data = row.split(",");
  //
  //        int nodeID = Integer.parseInt(data[0]);
  //        int xcoord = Integer.parseInt(data[1]);
  //        int ycoord = Integer.parseInt(data[2]);
  //        String floor = data[3];
  //        String building = data[4];
  //
  //        Node node = new Node(nodeID, xcoord, ycoord, floor, building);
  //        nodes.put(nodeID, node);
  //      }
  //
  //      csvReader.close();
  //    } catch (IOException e) {
  //      throw new RuntimeException(e);
  //    }
  //
  //    return nodes;
  //  }

  public HashMap<Integer, Node> loadDataFromDatabaseInMap() {
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Node\"");

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        int xcoord = rs.getInt("xcoord");
        int ycoord = rs.getInt("ycoord");
        String floor = rs.getString("floor");
        String building = rs.getString("building");

        Node node = new Node(nodeID, xcoord, ycoord, floor, building);
        NodeMap.put(nodeID, node);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return NodeMap;
  }

  // TODO delete
  public ArrayList<Node> loadNodesFromDatabaseInArray() {
    ArrayList<Node> nodes = new ArrayList<>();

    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Node\"");

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        int xcoord = rs.getInt("xcoord");
        int ycoord = rs.getInt("ycoord");
        String floor = rs.getString("floor");
        String building = rs.getString("building");

        Node node = new Node(nodeID, xcoord, ycoord, floor, building);
        nodes.add(node);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return nodes;
  }

  public HashMap<Integer, Node> Import(String filePath) {
    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        int nodeID = Integer.parseInt(data[0]);
        int xcoord = Integer.parseInt(data[1]);
        int ycoord = Integer.parseInt(data[2]);
        String floor = data[3];
        String building = data[4];

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.createConnection())
                .prepareStatement("INSERT INTO \"Teama_schema\".\"Node\" VALUES (?, ?, ?, ?, ?)");
        ps.setInt(1, nodeID);
        ps.setInt(2, xcoord);
        ps.setInt(3, ycoord);
        ps.setString(4, floor);
        ps.setString(5, building);
        ps.executeUpdate();

        Node node = new Node(nodeID, xcoord, ycoord, floor, building);
        NodeMap.put(nodeID, node);
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return NodeMap;
  }

  public void Export(String folderExportPath) {
    try {
      String newFile = folderExportPath + "/Node.csv";
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Node\"");

      FileWriter csvWriter = new FileWriter(newFile);

      csvWriter.append("nodeid,xcoord,ycoord,floor,building\n");

      while (rs.next()) {
        csvWriter.append((rs.getInt("nodeid")) + (","));
        csvWriter.append((rs.getInt("xcoord")) + (","));
        csvWriter.append((rs.getInt("ycoord")) + (","));
        csvWriter.append(rs.getString("floor")).append(",");
        csvWriter.append(rs.getString("building")).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("Node table exported to Node.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Node Add(Node node) {
    /* Insert new node object to the existing node table */
    int nodeID = node.getNodeID();
    int xcoord = node.getXcoord();
    int ycoord = node.getYcoord();
    String floor = node.getFloor();
    String building = node.getBuilding();
    try {

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement("INSERT INTO \"Teama_schema\".\"Node\" VALUES (?, ?, ?, ?, ?)");
      ps.setInt(1, nodeID);
      ps.setInt(2, xcoord);
      ps.setInt(3, ycoord);
      ps.setString(4, floor);
      ps.setString(5, building);
      ps.executeUpdate();

      node = new Node(nodeID, xcoord, ycoord, floor, building);
      NodeMap.put(nodeID, node);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return node;
  }

  public void Delete(Node node) {
    /* delete one of the node according to the nodeID, also delete the node from the arraylist */
    int nodeID = node.getNodeID();
    try {
      EdgeDAOImp edgeDAO = new EdgeDAOImp();
      edgeDAO.deleteEdgesWithNode(node);

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement("DELETE FROM \"Teama_schema\".\"Node\" WHERE nodeid = ?");
      ps.setInt(1, nodeID);
      ps.executeUpdate();

      NodeMap.remove(nodeID);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void Update(Node node) {
    /* update the node fields in the database and arraylist according to the inserts */
    int nodeID = node.getNodeID();
    int xcoord = node.getXcoord();
    int ycoord = node.getYcoord();
    String floor = node.getFloor();
    String building = node.getBuilding();
    try {

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"Node\" SET xcoord = ?, ycoord = ?, floor = ?, building = ? WHERE nodeid = ?");
      ps.setInt(1, xcoord);
      ps.setInt(2, ycoord);
      ps.setString(3, floor);
      ps.setString(4, building);
      ps.setInt(5, nodeID);
      ps.executeUpdate();

      NodeMap.put(nodeID, node);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Node getNode(int nodeID) {
    return NodeMap.get(nodeID);
  }

  public Node getLargestNodeID() {
    Node largestNode = null;
    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs =
          st.executeQuery("SELECT * FROM \"Teama_schema\".\"Node\" ORDER BY nodeid DESC LIMIT 1");

      if (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        int xcoord = rs.getInt("xcoord");
        int ycoord = rs.getInt("ycoord");
        String floor = rs.getString("floor");
        String building = rs.getString("building");

        largestNode = new Node(nodeID, xcoord, ycoord, floor, building);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return largestNode;
  }
}
