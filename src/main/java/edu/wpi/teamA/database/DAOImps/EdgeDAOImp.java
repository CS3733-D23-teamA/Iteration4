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
import lombok.Getter;
import lombok.Setter;

public class EdgeDAOImp implements IDataBase, IEdgeDAO {
  // ArrayList<Edge> EdgeArray;
  @Getter @Setter private HashMap<String, Edge> EdgeMap = new HashMap<String, Edge>();
  private static DBConnectionProvider edgeProvider = new DBConnectionProvider();

  public EdgeDAOImp(HashMap<String, Edge> EdgeMap) {
    this.EdgeMap = EdgeMap;
    // check if the table exist
    // if it exist, populate the array list
    // use select * to get all info from the table
    // create objects based off of the results
  }

  public EdgeDAOImp() {
    this.EdgeMap = loadEdgesFromDatabaseInMap();
  }

  public static void createTable() {
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

      Statement stmtEdge = edgeProvider.createConnection().createStatement();
      stmtEdge.execute(sqlCreateEdge);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static HashMap<String, Edge> loadEdgesFromCSV(String filePath) {
    HashMap<String, Edge> edges = new HashMap<String, Edge>();

    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine(); // Skip the header line
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        Integer startNode = Integer.parseInt(data[0]);
        Integer endNode = Integer.parseInt(data[1]);

        Edge edge = new Edge(startNode, endNode);
        edges.put(startNode.toString() + endNode.toString(), edge);
      }

      csvReader.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return edges;
  }

  public static HashMap<String, Edge> Import(String filePath) {
    HashMap<String, Edge> EdgeMap = loadEdgesFromCSV(filePath);

    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        PreparedStatement ps =
            edgeProvider
                .createConnection()
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

  public static void Export(String folderExportPath) {
    try {
      String newFile = folderExportPath + "/Edge.csv";
      Statement st = edgeProvider.createConnection().createStatement();
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

  // TODO delete
  public ArrayList<Edge> loadEdgesFromDatabase() {
    ArrayList<Edge> edges = new ArrayList<>();

    try {
      Statement st = edgeProvider.createConnection().createStatement();
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

  public HashMap<String, Edge> loadEdgesFromDatabaseInMap() {
    // HashMap<String, Edge> edges = new HashMap<String, Edge>();

    try {
      Statement st = edgeProvider.createConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Edge\"");

      while (rs.next()) {
        Integer startNode = rs.getInt("startNode");
        Integer endNode = rs.getInt("endNode");

        Edge edge = new Edge(startNode, endNode);
        // edges.put(startNode.toString() + endNode.toString(), edge);
        EdgeMap.put(startNode.toString() + endNode.toString(), edge);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return EdgeMap;
  }

  public Edge Add(Integer startNode, Integer endNode) {
    /** Insert new edge object to the existing edge table and the arraylist */
    Edge edge = null;
    try {

      PreparedStatement ps =
          edgeProvider
              .createConnection()
              .prepareStatement("INSERT INTO \"Prototype2_schema\".\"Edge\" VALUES (?, ?)");
      ps.setInt(1, startNode);
      ps.setInt(2, endNode);
      ps.executeUpdate();

      edge = new Edge(startNode, endNode);

      EdgeMap.put(startNode.toString() + endNode.toString(), edge);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return edge;
  }

  public void Delete(Integer startNode, Integer endNode) {
    /**
     * delete the edge when specified with a composite key (startNode+endNode) and in the arrayList
     */
    try {

      PreparedStatement ps =
          edgeProvider
              .createConnection()
              .prepareStatement(
                  "DELETE FROM \"Prototype2_schema\".\"Edge\" WHERE startnode = ? AND endnode = ?");
      ps.setInt(1, startNode);
      ps.setInt(2, endNode);
      ps.executeUpdate();

      EdgeMap.remove(startNode.toString() + endNode.toString());
      // EdgeMap.removeIf(Edge -> Edge.getStartNode() == startNode && Edge.getEndNode() == endNode);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void Update(int oldStartNode, int oldEndNode, Integer newStartNode, Integer newEndNode) {
    /**
     * update the edge startNode and endNode when specified with a composite key (startNode +
     * ednNode) and In the arrayList
     */
    try {

      PreparedStatement ps =
          edgeProvider
              .createConnection()
              .prepareStatement(
                  "UPDATE \"Prototype2_schema\".\"Edge\" SET startnode = ?, endnode = ? WHERE startnode = ? AND endnode = ?");
      ps.setInt(1, newStartNode);
      ps.setInt(2, newEndNode);
      ps.setInt(3, oldStartNode);
      ps.setInt(4, oldEndNode);
      ps.executeUpdate();

      EdgeMap.put(
          newStartNode.toString() + newEndNode.toString(), new Edge(newStartNode, newEndNode));
      //      EdgeArray.forEach(
      //          edge -> {
      //            if (edge.getStartNode() == oldStartNode && edge.getEndNode() == oldEndNode) {
      //              edge.setStartNode(newStartNode);
      //              edge.setEndNode(newEndNode);
      //            }
      //          });

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Edge getEdge(Integer startNode, Integer endNode) {
    Edge edge = null;
    try {
      PreparedStatement ps =
          edgeProvider
              .createConnection()
              .prepareStatement(
                  "SELECT * FROM \"Prototype2_schema\".\"Edge\" WHERE startnode = ? AND endnode = ?");
      ps.setInt(1, startNode);
      ps.setInt(2, endNode);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        edge = new Edge(startNode, endNode);
      }
    } catch (SQLException e) {
      return null;
    }
    return edge;
  }

  public ArrayList<Edge> deleteEdgesWithNode(int nodeID) {
    ArrayList<Edge> edgesToRemove = new ArrayList<Edge>();
    HashMap<String, Edge> copiedEdgeMap = new HashMap<String, Edge>(EdgeMap);
    try {
      PreparedStatement ps =
          edgeProvider
              .createConnection()
              .prepareStatement(
                  "DELETE FROM \"Prototype2_schema\".\"Edge\" WHERE startnode = ? OR endnode = ?");
      ps.setInt(1, nodeID);
      ps.setInt(2, nodeID);
      ps.executeUpdate();

      for (Map.Entry<String, Edge> entry : copiedEdgeMap.entrySet()) {
        Edge edge = entry.getValue();
        if (edge.getStartNode() == nodeID || edge.getEndNode() == nodeID) {
          EdgeMap.remove(edge.getStartNode().toString() + edge.getEndNode().toString());
          edgesToRemove.add(edge);
        }
      }
      //      for (Edge edge : copiedEdgeArray) {
      //        if (edge.getStartNode() == nodeID || edge.getEndNode() == nodeID) {
      //          EdgeArray.remove(edge);
      //          edgesToRemove.add(edge);
      //        }
      //      }
      // EdgeArray.removeIf(edge -> edge.getStartNode() == nodeID || edge.getEndNode() == nodeID);
      return edgesToRemove;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
