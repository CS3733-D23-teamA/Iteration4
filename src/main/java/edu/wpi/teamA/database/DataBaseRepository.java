package edu.wpi.teamA.database;

import edu.wpi.teamA.database.DAOImps.*;
import edu.wpi.teamA.database.ORMclasses.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseRepository {
  private NodeDAOImp nodeDAOImp;
  private EdgeDAOImp edgeDAOImp;
  private LocNameDAOImp locNameDAOImp;
  private MoveDAOImp moveDAOImp;
  private FlowerDAOImpl flowerDAOImpl;
  private CRRRDAOImp crrrDAOImp;
  private FurnitureDAOImp furnitureDAOImp;

  public DataBaseRepository() {
    nodeDAOImp = new NodeDAOImp();
    edgeDAOImp = new EdgeDAOImp();
    locNameDAOImp = new LocNameDAOImp();
    moveDAOImp = new MoveDAOImp();
    flowerDAOImpl = new FlowerDAOImpl();
    crrrDAOImp = new CRRRDAOImp();
    furnitureDAOImp = new FurnitureDAOImp();
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
  //
  // takes in two edge objects, one old version one updated version
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

  // same as edge
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
    ArrayList<Node> importedNodes = NodeDAOImp.Import(nodeFilePath);
    ArrayList<Edge> importedEdges = EdgeDAOImp.Import(edgeFilePath);
    ArrayList<LocationName> importedLocationNames = LocNameDAOImp.Import(locNameFilePath);
    ArrayList<Move> importedMoves = MoveDAOImp.Import(moveFilePath);

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

  // Flower related methods
  public void addFlower(FlowerEntity flower) {
    flowerDAOImpl.addFlower(flower);
  }

  public void deleteFlower(FlowerEntity flower) {
    flowerDAOImpl.deleteFlower(flower);
  }

  public List<FlowerEntity> getAllFlowers() {
    return flowerDAOImpl.getAllFlowers();
  }

  public FlowerEntity getFlower(String name) {
    return flowerDAOImpl.getFlower(name);
  }

  public void updateFlower(FlowerEntity flower) {
    flowerDAOImpl.updateFlower(flower);
  }

  // CRRR related methods
  public void addCRRR(ConferenceRoomResRequest crrr) {
    crrrDAOImp.addCRRR(crrr);
  }

  public void deleteCRRR(ConferenceRoomResRequest crrr) {
    crrrDAOImp.deleteCRRR(crrr);
  }

  public List<ConferenceRoomResRequest> getAllCRRR() {
    return crrrDAOImp.getAllCRRR();
  }

  public ConferenceRoomResRequest getCRRR(String name) {
    return crrrDAOImp.getCRRR(name);
  }

  public void updateCRRR(ConferenceRoomResRequest crrr) {
    crrrDAOImp.updateCRRR(crrr);
  }

  // Furniture related methods
  public void addFurniture(FurnitureRequest furniture) {
    furnitureDAOImp.addFurniture(furniture);
  }

  public void deleteFurniture(FurnitureRequest furniture) {
    furnitureDAOImp.deleteFurniture(furniture);
  }

  public void updateFurniture(FurnitureRequest furniture) {
    furnitureDAOImp.updateFurniture(furniture);
  }

  public void editFurniture(FurnitureRequest furniture) {
    furnitureDAOImp.editFurniture(furniture);
  }
}
