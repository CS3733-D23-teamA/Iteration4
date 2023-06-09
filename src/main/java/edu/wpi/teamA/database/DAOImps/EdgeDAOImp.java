package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IDatabaseDAO;
import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.database.ORMclasses.Node;
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

public class EdgeDAOImp implements IDatabaseDAO<Edge> {
  // ArrayList<Edge> EdgeArray;
  @Getter @Setter private HashMap<String, Edge> EdgeMap = new HashMap<>();

  public EdgeDAOImp() {
    createTable();
    this.EdgeMap = loadDataFromDatabaseInMap();
  }

  public void createTable() {
    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();

      st.execute(
          "Create Table if not exists \"Teama_schema\".\"Edge\""
              + "(startNode   int,"
              + "endNode    int,"
              + "CONSTRAINT fk_startnode "
              + "FOREIGN KEY(startNode) "
              + "REFERENCES \"Teama_schema\".\"Node\"(nodeid)"
              + "ON DELETE CASCADE,"
              + "CONSTRAINT fk_endnode "
              + "FOREIGN KEY(endNode)"
              + "REFERENCES \"Teama_schema\".\"Node\"(nodeid)"
              + "ON DELETE CASCADE)");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public HashMap<String, Edge> loadDataFromDatabaseInMap() {
    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Edge\"");

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

  public ArrayList<Edge> loadEdgesFromDatabaseInArray() {
    ArrayList<Edge> edges = new ArrayList<>();

    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Edge\"");

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

  public HashMap<String, Edge> Import(String filePath) {
    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        int startNode = Integer.parseInt(data[0]);
        int endNode = Integer.parseInt(data[1]);

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.getInstance())
                .prepareStatement("INSERT INTO \"Teama_schema\".\"Edge\" VALUES (?, ?)");
        ps.setInt(1, startNode);
        ps.setInt(2, endNode);
        ps.executeUpdate();

        Edge edge = new Edge(startNode, endNode);
        EdgeMap.put(Integer.toString(startNode) + endNode, edge);
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
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Edge\"");

      FileWriter csvWriter = new FileWriter(newFile);
      csvWriter.append("startnode,endode\n");

      while (rs.next()) {
        csvWriter.append(String.valueOf((rs.getInt("startnode")))).append(",");
        csvWriter.append(String.valueOf((rs.getInt("endnode")))).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("Edge table exported to Edge.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Edge Add(Edge edge) {
    /* Insert new edge object to the existing edge table and the arraylist */
    Integer startNode = edge.getStartNode();
    Integer endNode = edge.getEndNode();

    try {

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.getInstance())
              .prepareStatement("INSERT INTO \"Teama_schema\".\"Edge\" VALUES (?, ?)");
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

  public void Delete(Edge edge) {
    /*
     * delete the edge when specified with a composite key (startNode+endNode) and in the arrayList
     */
    Integer startNode = edge.getStartNode();
    Integer endNode = edge.getEndNode();
    try {

      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.getInstance())
              .prepareStatement(
                  "DELETE FROM \"Teama_schema\".\"Edge\" WHERE startnode = ? AND endnode = ?");
      ps.setInt(1, startNode);
      ps.setInt(2, endNode);
      ps.executeUpdate();

      EdgeMap.remove(startNode + endNode.toString());

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void Update(Edge obj) {}

  public Edge getEdge(Integer startNode, Integer endNode) {
    return EdgeMap.get(startNode.toString() + endNode.toString());
  }

  public ArrayList<Edge> deleteEdgesWithNode(Node node) {
    ArrayList<Edge> edgesToRemove = new ArrayList<>();
    HashMap<String, Edge> copiedEdgeMap = new HashMap<>(EdgeMap);
    try {
      PreparedStatement ps =
          Objects.requireNonNull(DBConnectionProvider.getInstance())
              .prepareStatement(
                  "DELETE FROM \"Teama_schema\".\"Edge\" WHERE startnode = ? OR endnode = ?");
      ps.setInt(1, node.getNodeID());
      ps.setInt(2, node.getNodeID());
      ps.executeUpdate();

      for (Map.Entry<String, Edge> entry : copiedEdgeMap.entrySet()) {
        Edge edge = entry.getValue();
        if (Objects.equals(edge.getStartNode(), node.getNodeID())
            || Objects.equals(edge.getEndNode(), node.getNodeID())) {
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
