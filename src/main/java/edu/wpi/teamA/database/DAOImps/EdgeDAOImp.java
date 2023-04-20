package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IEdgeDAO;
import edu.wpi.teamA.database.ORMclasses.Edge;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class EdgeDAOImp implements IDatabaseDAO, IEdgeDAO {
  // ArrayList<Edge> EdgeArray;
  @Getter @Setter private HashMap<String, Edge> EdgeMap = new HashMap<>();

  public EdgeDAOImp(HashMap<String, Edge> EdgeMap) {
    this.EdgeMap = EdgeMap;
    // check if the table exist
    // if it exist, populate the array list
    // use select * to get all info from the table
    // create objects based off of the results
  }

  public EdgeDAOImp() {
    this.EdgeMap = loadDataFromDatabaseInMap();
  }

  public void createTable() {
    try {
      String sqlCreateEdge =
          "Create Table if not exists \"Prototype2_schema\".\"Edge\""
              + "(startNode   int,"
              + "endNode    int,"
              + "CONSTRAINT fk_startnode "
              + "FOREIGN KEY(startNode) "
              + "REFERENCES \"Prototype2_schema\".\"Node\"(nodeid)"
              + "ON DELETE CASCADE,"
              + "CONSTRAINT fk_endnode "
              + "FOREIGN KEY(endNode)"
              + "REFERENCES \"Prototype2_schema\".\"Node\"(nodeid)"
              + "ON DELETE CASCADE)";

      Statement stmtEdge =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      stmtEdge.execute(sqlCreateEdge);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private HashMap<String, Edge> loadDataFromCSV(String filePath) {
    HashMap<String, Edge> edges = new HashMap<>();

    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine(); // Skip the header line
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        Integer startNode = Integer.parseInt(data[0]);
        Integer endNode = Integer.parseInt(data[1]);

        Edge edge = new Edge(startNode, endNode);
        edges.put(startNode + endNode.toString(), edge);
      }

      csvReader.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return edges;
  }

  public HashMap<String, Edge> Import(String filePath) {
    HashMap<String, Edge> EdgeMap = loadDataFromCSV(filePath);

    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.createConnection())
                .prepareStatement("INSERT INTO \"Prototype2_schema\".\"Edge\" VALUES (?, ?)");
        ps.setInt(1, Integer.parseInt(data[0]));
        ps.setInt(2, Integer.parseInt(data[1]));
        ps.executeUpdate();
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }

    return EdgeMap;
  }

  public void Export(String folderExportPath) {
    try {
      String newFile = folderExportPath + "/Edge.csv";
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Edge\"");

      FileWriter csvWriter = new FileWriter(newFile);
      csvWriter.append("startnode,endode\n");

      while (rs.next()) {
        csvWriter.append((rs.getInt("startnode")) + (","));
        csvWriter.append((rs.getInt("endnode")) + ("\n"));
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("Edge table exported to Edge.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Edge> loadEdgesFromDatabaseInArray() {
    ArrayList<Edge> edges = new ArrayList<>();

    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Edge\"");

      while (rs.next()) {
        int startNode = rs.getInt("startNode");
        int endNode = rs.getInt("endNode");

        Edge edge = new Edge(startNode, endNode);
        edges.add(edge);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return edges;
  }

  public HashMap<String, Edge> loadDataFromDatabaseInMap() {
    // HashMap<String, Edge> edges = new HashMap<String, Edge>();

    try {
      Statement st =
          Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Edge\"");

      while (rs.next()) {
        Integer startNode = rs.getInt("startNode");
        Integer endNode = rs.getInt("endNode");

        Edge edge = new Edge(startNode, endNode);
        // edges.put(startNode.toString() + endNode.toString(), edge);
        EdgeMap.put(startNode + endNode.toString(), edge);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return EdgeMap;
  }

  public Edge Add(Integer startNode, Integer endNode) {
    /* Insert new edge object to the existing edge table and the arraylist */
    Edge edge = null;
    try {

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement("INSERT INTO \"Prototype2_schema\".\"Edge\" VALUES (?, ?)");
      ps.setInt(1, startNode);
      ps.setInt(2, endNode);
      ps.executeUpdate();

      edge = new Edge(startNode, endNode);

      EdgeMap.put(startNode + endNode.toString(), edge);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return edge;
  }

  public void Delete(Integer startNode, Integer endNode) {
    /*
     * delete the edge when specified with a composite key (startNode+endNode) and in the arrayList
     */
    try {

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "DELETE FROM \"Prototype2_schema\".\"Edge\" WHERE startnode = ? AND endnode = ?");
      ps.setInt(1, startNode);
      ps.setInt(2, endNode);
      ps.executeUpdate();

      EdgeMap.remove(startNode + endNode.toString());

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void Update(int oldStartNode, int oldEndNode, Integer newStartNode, Integer newEndNode) {
    /*
     * update the edge startNode and endNode when specified with a composite key (startNode +
     * ednNode) and In the arrayList
     */
    try {

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "UPDATE \"Prototype2_schema\".\"Edge\" SET startnode = ?, endnode = ? WHERE startnode = ? AND endnode = ?");
      ps.setInt(1, newStartNode);
      ps.setInt(2, newEndNode);
      ps.setInt(3, oldStartNode);
      ps.setInt(4, oldEndNode);
      ps.executeUpdate();

      EdgeMap.put(newStartNode + newEndNode.toString(), new Edge(newStartNode, newEndNode));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Edge getEdge(Integer startNode, Integer endNode) {
    return EdgeMap.get(startNode.toString() + endNode.toString());
  }

  public ArrayList<Edge> deleteEdgesWithNode(int nodeID) {
    ArrayList<Edge> edgesToRemove = new ArrayList<>();
    HashMap<String, Edge> copiedEdgeMap = new HashMap<>(EdgeMap);
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.createConnection())
              .prepareStatement(
                  "DELETE FROM \"Prototype2_schema\".\"Edge\" WHERE startnode = ? OR endnode = ?");
      ps.setInt(1, nodeID);
      ps.setInt(2, nodeID);
      ps.executeUpdate();

      for (Map.Entry<String, Edge> entry : copiedEdgeMap.entrySet()) {
        Edge edge = entry.getValue();
        if (edge.getStartNode() == nodeID || edge.getEndNode() == nodeID) {
          EdgeMap.remove(edge.getStartNode() + edge.getEndNode().toString());
          edgesToRemove.add(edge);
        }
      }
      return edgesToRemove;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
