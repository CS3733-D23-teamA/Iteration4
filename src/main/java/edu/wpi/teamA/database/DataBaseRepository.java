package edu.wpi.teamA.database;

import edu.wpi.teamA.database.DAOImps.EdgeDAOImp;
import edu.wpi.teamA.database.DAOImps.LocNameDAOImp;
import edu.wpi.teamA.database.DAOImps.MoveDAOImp;
import edu.wpi.teamA.database.DAOImps.NodeDAOImp;
import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Move;
import edu.wpi.teamA.database.ORMclasses.Node;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataBaseRepository {
  private NodeDAOImp nodeDAOImp;
  private EdgeDAOImp edgeDAOImp;
  private LocNameDAOImp locNameDAOImp;
  private MoveDAOImp moveDAOImp;

  public DataBaseRepository() {
    nodeDAOImp = new NodeDAOImp();
    edgeDAOImp = new EdgeDAOImp();
    locNameDAOImp = new LocNameDAOImp();
    moveDAOImp = new MoveDAOImp();
  }

  // Node related methods
  public void addNode(int id, int xcoord, int ycoord, String floor, String building) {
    nodeDAOImp.Add(id, xcoord, ycoord, floor, building);
  }

  public void deleteNode(int id) {
    nodeDAOImp.Delete(id);
    edgeDAOImp.deleteEdgesWithNode(id);
  }

  public void updateNode(int id, int xcoord, int ycoord, String floor, String building) {
    nodeDAOImp.Update(id, xcoord, ycoord, floor, building);
  }

  public Node getNode(int id) {
    return nodeDAOImp.getNode(id);
  }

  // Edge related methods
  public void addEdge(int startNode, int endNode) {
    edgeDAOImp.Add(startNode, endNode);
  }

  public void deleteEdge(int startNode, int endNode) {
    edgeDAOImp.Delete(startNode, endNode);
  }

  public void updateEdge(int oldStartNode, int oldEndNode, int newStartNode, int newEndNode) {
    edgeDAOImp.Update(oldStartNode, oldEndNode, newStartNode, newEndNode);
  }

  public Edge getEdge(int startNode, int endNode) {
    return edgeDAOImp.getEdge(startNode, endNode);
  }

  // LocationName related methods
  public void addLocName(String longName, String shortName, String nodeType) {
    locNameDAOImp.Add(longName, shortName, nodeType);
  }

  public void deleteLocName(String longName) {
    locNameDAOImp.Delete(longName);
  }

  public void updateLocName(
      String oldLongName,
      String oldShortName,
      String newLongName,
      String newShortName,
      String newNodeType) {
    locNameDAOImp.Update(oldLongName, oldShortName, newLongName, newShortName, newNodeType);
  }

  public LocationName getLocName(String longName) {
    return locNameDAOImp.getLocName(longName);
  }

  // Move related methods
  public void addMove(int nodeID, String longName, String dateString) {
    moveDAOImp.Add(nodeID, longName, dateString);
  }

  public void deleteMove(int nodeID) {
    moveDAOImp.Delete(nodeID);
  }

  public void updateMove(int nodeID, String longName, String dateString) {
    moveDAOImp.Update(nodeID, longName, dateString);
  }

  public Move getMove(int nodeID) {
    return moveDAOImp.getMove(nodeID);
  }

  // Import and Export methods
  public void importData(
      String nodeFilePath, String edgeFilePath, String locNameFilePath, String moveFilePath) {

    // Convert file paths to Path objects and make them case-sensitive
    Path nodePath = Paths.get(nodeFilePath).toAbsolutePath().normalize();
    Path edgePath = Paths.get(edgeFilePath).toAbsolutePath().normalize();
    Path locNamePath = Paths.get(locNameFilePath).toAbsolutePath().normalize();
    Path movePath = Paths.get(moveFilePath).toAbsolutePath().normalize();

    // Use the case-sensitive file paths to import data
    ArrayList<Node> importedNodes = NodeDAOImp.Import(nodePath.toString());
    ArrayList<Edge> importedEdges = EdgeDAOImp.Import(edgePath.toString());
    ArrayList<LocationName> importedLocationNames = LocNameDAOImp.Import(locNamePath.toString());
    ArrayList<Move> importedMoves = MoveDAOImp.Import(movePath.toString());

    nodeDAOImp = new NodeDAOImp(importedNodes);
    edgeDAOImp = new EdgeDAOImp(importedEdges);
    locNameDAOImp = new LocNameDAOImp(importedLocationNames);
    moveDAOImp = new MoveDAOImp(importedMoves);
  }

  public void exportData(String folderExportPath) {
    NodeDAOImp.Export(folderExportPath);
    EdgeDAOImp.Export(folderExportPath);
    LocNameDAOImp.Export(folderExportPath);
    MoveDAOImp.Export(folderExportPath);
  }
}
