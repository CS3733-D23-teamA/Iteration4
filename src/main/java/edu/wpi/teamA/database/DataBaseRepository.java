package edu.wpi.teamA.database;

import edu.wpi.teamA.database.DAOImps.*;
import edu.wpi.teamA.database.ORMclasses.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBaseRepository {

  private static DataBaseRepository instance = null;
  private NodeDAOImp nodeDAOImp;
  private EdgeDAOImp edgeDAOImp;
  private LocNameDAOImp locNameDAOImp;
  private MoveDAOImp moveDAOImp;
  private FlowerDAOImpl flowerDAOImpl;
  private CRRRDAOImp crrrDAOImp;
  private FurnitureDAOImp furnitureDAOImp;
  private UserDAOImp userDAOImp;

  private EmployeeDAOImp employeeDAOImp;

  public DataBaseRepository() {
    nodeDAOImp = new NodeDAOImp();
    edgeDAOImp = new EdgeDAOImp();
    locNameDAOImp = new LocNameDAOImp();
    moveDAOImp = new MoveDAOImp();
    flowerDAOImpl = new FlowerDAOImpl();
    crrrDAOImp = new CRRRDAOImp();
    furnitureDAOImp = new FurnitureDAOImp();

    userDAOImp = new UserDAOImp();
    employeeDAOImp = new EmployeeDAOImp();
  }

  public static DataBaseRepository getInstance() {
    if (instance == null) {
      instance = new DataBaseRepository();
    }
    return instance;
  }

  // Node related methods

  public HashMap<Integer, Node> getNodeMap() {
    return nodeDAOImp.getNodeMap();
  }

  public ArrayList<Node> loadNodesFromDatabaseInArray() {
    return nodeDAOImp.loadNodesFromDatabaseInArray();
  }

  public HashMap<Integer, Node> loadNodesFromDatabaseInMap() {
    return nodeDAOImp.loadNodesFromDatabaseInMap();
  }

  public Node addNode(int id, int xcoord, int ycoord, String floor, String building) {
    return nodeDAOImp.Add(id, xcoord, ycoord, floor, building);
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

  public Node getLargestNodeID() {
    return nodeDAOImp.getLargestNodeID();
  }

  // Edge related methods

  public HashMap<String, Edge> getEdgeMap() {
    return edgeDAOImp.getEdgeMap();
  }

  public ArrayList<Edge> loadEdgesFromDatabaseInArray() {
    return edgeDAOImp.loadEdgesFromDatabaseInArray();
  }

  public HashMap<String, Edge> loadEdgesFromDatabaseInMap() {
    return edgeDAOImp.loadEdgesFromDatabaseInMap();
  }

  public Edge addEdge(int startNode, int endNode) {
    return edgeDAOImp.Add(startNode, endNode);
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

  public ArrayList<Edge> deleteEdgesWithNode(int nodeID) {
    return edgeDAOImp.deleteEdgesWithNode(nodeID);
  }

  // LocationName related methods
  public HashMap<String, LocationName> getLocNameMap() {
    return locNameDAOImp.getLocNameMap();
  }

  public HashMap<String, LocationName> loadLocNameFromDatabase() {
    return locNameDAOImp.loadLocNamefromDatabase();
  }

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

  public ArrayList<String> filterLocType(String type) {
    return locNameDAOImp.filterLocType(type);
  }

  // Move related methods
  public HashMap<Integer, Move> getMoveMap() {
    return moveDAOImp.getMoveMap();
  }

  public ArrayList<Move> loadMovesFromDatabase() {
    return moveDAOImp.loadMovesFromDatabase();
  }

  public HashMap<Integer, Move> loadMovesFromDatabaseInMap() {
    return moveDAOImp.loadMovesFromDatabaseInMap();
  }

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
  public void importData(String filepath, String type) {
    if (type.equals("Node")) {
      HashMap<Integer, Node> importedNodes = NodeDAOImp.Import(filepath);
      nodeDAOImp = new NodeDAOImp(importedNodes);
    } else if (type.equals("LocName")) {
      HashMap<String, LocationName> importedLocationNames = LocNameDAOImp.Import(filepath);
      locNameDAOImp = new LocNameDAOImp(importedLocationNames);
    } else if (type.equals("Move")) {
      HashMap<Integer, Move> importedMoves = MoveDAOImp.Import(filepath);
      moveDAOImp = new MoveDAOImp(importedMoves);
    } else if (type.equals("Edge")) {
      HashMap<String, Edge> importedEdges = EdgeDAOImp.Import(filepath);
      edgeDAOImp = new EdgeDAOImp(importedEdges);
    }
  }

  public void exportData(String folderExportPath, String type) {
    if (type.equals("Node")) {
      NodeDAOImp.Export(folderExportPath);
    } else if (type.equals("LocName")) {
      LocNameDAOImp.Export(folderExportPath);
    } else if (type.equals("Move")) {
      MoveDAOImp.Export(folderExportPath);
    } else if (type.equals("Edge")) {
      EdgeDAOImp.Export(folderExportPath);
    }
  }

  // Flower related methods
  public HashMap<Integer, FlowerEntity> getFlowerMap() {
    return flowerDAOImpl.getFlowerMap();
  }

  public HashMap<Integer, FlowerEntity> loadFlowersFromDatabaseInMap() {
    return flowerDAOImpl.loadFlowersFromDatabaseInMap();
  }

  public void addFlower(FlowerEntity flower) {
    flowerDAOImpl.addFlower(flower);
  }

  public void deleteFlower(FlowerEntity flower) {
    flowerDAOImpl.deleteFlower(flower);
  }

  public FlowerEntity getFlower(int id) {
    return flowerDAOImpl.getFlower(id);
  }

  public void updateFlower(FlowerEntity flower) {
    flowerDAOImpl.updateFlower(flower);
  }

  public void editFlower(FlowerEntity oldFlower, FlowerEntity newFlower) {
    flowerDAOImpl.editFlower(oldFlower, newFlower);
  }

  public int getNextID() {
    return flowerDAOImpl.getNextID();
  }

  public List<FlowerEntity> filterFlowerEmployee(String username) {
    return flowerDAOImpl.filterFlowerEmployee(username);
  }

  // Conference room service request related methods
  public ArrayList<ConferenceRoomResRequest> getCrrrArray() {
    return crrrDAOImp.getCrrrArray();
  }

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
  public ArrayList<FurnitureRequest> getFurnitureArray() {
    return furnitureDAOImp.getFurnitureArray();
  }

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

  // user dao functions
  public void createUserTable() {
    userDAOImp.createUserTable();
  }

  public void addUser(
      int adminYes, String userName, String password, String firstName, String lastName)
      throws IncorrectLengthException {
    userDAOImp.addUser(adminYes, userName, password, firstName, lastName);
  }

  public User checkUser(String userName, String password) {
    return userDAOImp.checkUser(userName, password);
  }

  public void updatePassword(
      String password1, String password2, String newPassword1, String newPassword2) {
    userDAOImp.updatePassword(password1, password2, newPassword1, newPassword2);
  }
  // Employee related methods

  public HashMap<String, Employee> getEmployeeMap() {
    return employeeDAOImp.getEmployeeMap();
  }

  public HashMap<String, Employee> loadEmployeesFromDatabaseInMap() {
    return employeeDAOImp.loadEmployeesFromDatabaseInMap();
  }

  public Employee getEmployee(String username) {
    return employeeDAOImp.getEmployee(username);
  }

  public void modifyEmployee(Employee employee) {
    employeeDAOImp.modifyEmployee(employee);
  }

  public void addEmployee(Employee employee) {
    employeeDAOImp.addEmployee(employee);
  }

  public void removeEmployee(Employee employee) {
    employeeDAOImp.removeEmployee(employee);
  }
}
