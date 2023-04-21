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

  public void createNodeTable() {
    nodeDAOImp.createTable();
  }

  public HashMap<Integer, Node> loadNodesFromDatabaseInMap() {
    return nodeDAOImp.loadDataFromDatabaseInMap();
  }

  public ArrayList<Node> loadNodesFromDatabaseInArray() {
    return nodeDAOImp.loadNodesFromDatabaseInArray();
  }

  public Node addNode(Node node) {
    return nodeDAOImp.Add(node);
  }

  public void deleteNode(Node node) {
    nodeDAOImp.Delete(node);
    edgeDAOImp.deleteEdgesWithNode(node);
  }

  public void updateNode(Node node) {
    nodeDAOImp.Update(node);
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

  public void createEdgeTable() {
    edgeDAOImp.createTable();
  }

  public HashMap<String, Edge> loadEdgesFromDatabaseInMap() {
    return edgeDAOImp.loadDataFromDatabaseInMap();
  }

  public ArrayList<Edge> loadEdgesFromDatabaseInArray() {
    return edgeDAOImp.loadEdgesFromDatabaseInArray();
  }

  public Edge addEdge(Edge edge) {
    return edgeDAOImp.Add(edge);
  }

  public void deleteEdge(Edge edge) {
    edgeDAOImp.Delete(edge);
  }

  public void updateEdge(Edge edge) {
    //edgeDAOImp.Update(oldStartNode, oldEndNode, newStartNode, newEndNode);
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

  public void createLocNameTable() {
    locNameDAOImp.createTable();
  }

  public HashMap<String, LocationName> loadLocNameFromDatabaseInMap() {
    return locNameDAOImp.loadDataFromDatabaseInMap();
  }

  public void addLocName(LocationName locName) {
    locNameDAOImp.Add(locName);
  }

  public void deleteLocName(LocationName locName) {
    locNameDAOImp.Delete(locName);
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

  public void createMoveTable() {
    moveDAOImp.createTable();
  }

  public HashMap<Integer, Move> loadMovesFromDatabaseInMap() {
    return moveDAOImp.loadDataFromDatabaseInMap();
  }

  public void addMove(Move move) {
    moveDAOImp.Add(move);
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
      HashMap<Integer, Node> importedNodes = nodeDAOImp.Import(filepath);
      nodeDAOImp = new NodeDAOImp(importedNodes);
    } else if (type.equals("LocName")) {
      HashMap<String, LocationName> importedLocationNames = locNameDAOImp.Import(filepath);
      locNameDAOImp = new LocNameDAOImp(importedLocationNames);
    } else if (type.equals("Move")) {
      HashMap<Integer, Move> importedMoves = moveDAOImp.Import(filepath);
      moveDAOImp = new MoveDAOImp(importedMoves);
    } else if (type.equals("Edge")) {
      HashMap<String, Edge> importedEdges = edgeDAOImp.Import(filepath);
      edgeDAOImp = new EdgeDAOImp(importedEdges);
    }
  }

  public void exportData(String folderExportPath, String type) {
    if (type.equals("Node")) {
      nodeDAOImp.Export(folderExportPath);
    } else if (type.equals("LocName")) {
      locNameDAOImp.Export(folderExportPath);
    } else if (type.equals("Move")) {
      moveDAOImp.Export(folderExportPath);
    } else if (type.equals("Edge")) {
      edgeDAOImp.Export(folderExportPath);
    }
  }

  // Flower related methods
  public HashMap<Integer, Flower> getFlowerMap() {
    return flowerDAOImpl.getFlowerMap();
  }

  public HashMap<Integer, Flower> loadFlowersFromDatabaseInMap() {
    return flowerDAOImpl.loadFlowersFromDatabaseInMap();
  }

  public void addFlower(Flower flower) {
    flowerDAOImpl.addFlower(flower);
  }

  public void deleteFlower(Flower flower) {
    flowerDAOImpl.deleteFlower(flower);
  }

  public Flower getFlower(int id) {
    return flowerDAOImpl.getFlower(id);
  }

  public void updateFlower(Flower flower) {
    flowerDAOImpl.updateFlower(flower);
  }

  public int getNextID() {
    return flowerDAOImpl.getNextID();
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
