package edu.wpi.teamA.database;

import edu.wpi.teamA.database.DAOImps.*;
import edu.wpi.teamA.database.ORMclasses.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class DataBaseRepository {

  private static DataBaseRepository instance = null;
  private NodeDAOImp nodeDAOImp;
  private EdgeDAOImp edgeDAOImp;
  private LocNameDAOImp locNameDAOImp;
  private MoveDAOImp moveDAOImp;
  private FlowerDAOImp flowerDAOImp;
  private CRRRDAOImp crrrDAOImp;
  private FurnitureDAOImp furnitureDAOImp;
  private MealDAOImp mealDAOImp;
  private UserDAOImp userDAOImp;
  private EmployeeDAOImp employeeDAOImp;
  private SignageDAOImp signageDAOImp;
  private AlertDAOImp alertDAOImp;

  public DataBaseRepository() {
    nodeDAOImp = new NodeDAOImp();
    edgeDAOImp = new EdgeDAOImp();
    locNameDAOImp = new LocNameDAOImp();
    moveDAOImp = new MoveDAOImp();
    flowerDAOImp = new FlowerDAOImp();
    crrrDAOImp = new CRRRDAOImp();
    furnitureDAOImp = new FurnitureDAOImp();
    mealDAOImp = new MealDAOImp();
    userDAOImp = new UserDAOImp();
    employeeDAOImp = new EmployeeDAOImp();
    signageDAOImp = new SignageDAOImp();
    alertDAOImp = new AlertDAOImp();
  }

  public static DataBaseRepository getInstance() {
    if (instance == null) {
      instance = new DataBaseRepository();
    }
    return instance;
  }

  /** Resets all the DAO's hashmaps (called every minute) */
  public void updateCache() {
    nodeDAOImp = new NodeDAOImp();
    edgeDAOImp = new EdgeDAOImp();
    locNameDAOImp = new LocNameDAOImp();
    moveDAOImp = new MoveDAOImp();
    flowerDAOImp = new FlowerDAOImp();
    crrrDAOImp = new CRRRDAOImp();
    furnitureDAOImp = new FurnitureDAOImp();
    mealDAOImp = new MealDAOImp();
    userDAOImp = new UserDAOImp();
    employeeDAOImp = new EmployeeDAOImp();
    signageDAOImp = new SignageDAOImp();
    alertDAOImp = new AlertDAOImp();
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
    // edgeDAOImp.Update();
  }

  public Edge getEdge(int startNode, int endNode) {
    return edgeDAOImp.getEdge(startNode, endNode);
  }

  public ArrayList<Edge> deleteEdgesWithNode(Node node) {
    return edgeDAOImp.deleteEdgesWithNode(node);
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

  public void updateLocName(LocationName locName) {
    locNameDAOImp.Update(locName);
  }

  public LocationName getLocName(String longName) {
    return locNameDAOImp.getLocName(longName);
  }

  public ArrayList<String> filterLocType(String type) {
    return locNameDAOImp.filterLocType(type);
  }

  // Move related methods

  public HashMap<Integer, LinkedList<Move>> getNodeMoveMap() {
    return moveDAOImp.getNodeMoveMap();
  }

  //  public HashMap<String, LinkedList<Move>> getLocationMoveMap() {
  //    return moveDAOImp.getLocationMoveMap();
  //  }

  public HashMap<String, Move> getCurrentMoveMap() {
    return moveDAOImp.getCurrentMoveMap();
  }

  public void setCurrentMoveMap(HashMap<String, Move> currentMoveMap) {
    moveDAOImp.setCurrentMoveMap(currentMoveMap);
  }

  public void createMoveTable() {
    moveDAOImp.createTable();
  }

  public HashMap<Integer, LinkedList<Move>> loadMovesFromDatabaseInMap() {
    return moveDAOImp.loadDataFromDatabaseInMap();
  }

  public HashMap<String, Move> loadCurrentMovesMap(LocalDate date) {
    return moveDAOImp.loadCurrentMoveMap(date);
  }

  public void addMove(Move move) {
    moveDAOImp.Add(move);
  }

  public void deleteMove(Move move) {
    moveDAOImp.Delete(move);
  }

  public void updateMove(Move oldMove, Move newMove) {
    moveDAOImp.Update(oldMove, newMove);
  }

  public Move getFirstMoveForNode(int nodeID) {
    return moveDAOImp.getFirstMoveForNode(nodeID);
  }

  public Move getSecondMoveForNode(int nodeID) {
    return moveDAOImp.getSecondMoveForNode(nodeID);
  }

  // Import and Export methods
  public void importData(String filepath, String type) {
    if (type.equals("Node")) {
      nodeDAOImp.Import(filepath);
    } else if (type.equals("LocName")) {
      locNameDAOImp.Import(filepath);
    } else if (type.equals("Move")) {
      moveDAOImp.Import(filepath);
    } else if (type.equals("Edge")) {
      edgeDAOImp.Import(filepath);
    } else if (type.equals("Flower")) {
      flowerDAOImp.Import(filepath);
    } else if (type.equals("CRRR")) {
      crrrDAOImp.Import(filepath);
    } else if (type.equals("Furniture")) {
      furnitureDAOImp.Import(filepath);
    } else if (type.equals("Meal")) {
      mealDAOImp.Import(filepath);
    } else if (type.equals("Employee")) {
      employeeDAOImp.Import(filepath);
    } else if (type.equals("User")) {
      userDAOImp.Import(filepath);
    } else if (type.equals("Signage")) {
      signageDAOImp.Import(filepath);
    } else if (type.equals("Alert")) {
      alertDAOImp.Import(filepath);
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
    } else if (type.equals("Flower")) {
      flowerDAOImp.Export(folderExportPath);
    } else if (type.equals("CRRR")) {
      crrrDAOImp.Export(folderExportPath);
    } else if (type.equals("Furniture")) {
      furnitureDAOImp.Export(folderExportPath);
    } else if (type.equals("Meal")) {
      mealDAOImp.Export(folderExportPath);
    } else if (type.equals("Employee")) {
      employeeDAOImp.Export(folderExportPath);
    } else if (type.equals("User")) {
      userDAOImp.Export(folderExportPath);
    } else if (type.equals("Signage")) {
      signageDAOImp.Export(folderExportPath);
    } else if (type.equals("Alert")) {
      alertDAOImp.Export(folderExportPath);
    }
  }

  // Flower related methods
  public HashMap<Integer, Flower> getFlowerMap() {
    return flowerDAOImp.getFlowerMap();
  }

  public void createFlowerTable() {
    flowerDAOImp.createTable();
  }

  public HashMap<Integer, Flower> loadFlowersFromDatabaseInMap() {
    return flowerDAOImp.loadDataFromDatabaseInMap();
  }

  public void addFlower(Flower flower) {
    flowerDAOImp.add(flower);
  }

  public void deleteFlower(Flower flower) {
    flowerDAOImp.delete(flower);
  }

  public void updateFlower(Flower flower) {
    flowerDAOImp.update(flower);
  }

  public Flower getFlower(int id) {
    return flowerDAOImp.get(id);
  }

  public ArrayList<Flower> getAssignedFlower(String username) {
    return flowerDAOImp.getAssigned(username);
  }

  public ArrayList<Flower> getCreatedFlower(String username) {
    return flowerDAOImp.getCreated(username);
  }

  public int getNextFlowerID() {
    return flowerDAOImp.getNextID();
  }

  // Conference room service request related methods
  public HashMap<Integer, ConferenceRoomResRequest> getCrrrMap() {
    return crrrDAOImp.getCrrrMap();
  }

  public HashMap<Integer, ConferenceRoomResRequest> loadCRRRFromDatabaseInMap() {
    return crrrDAOImp.loadDataFromDatabaseInMap();
  }

  public void addCRRR(ConferenceRoomResRequest crrr) {
    crrrDAOImp.add(crrr);
  }

  public void deleteCRRR(ConferenceRoomResRequest crrr) {
    crrrDAOImp.delete(crrr);
  }

  public void updateCRRR(ConferenceRoomResRequest crrr) {
    crrrDAOImp.update(crrr);
  }

  public ConferenceRoomResRequest getCRRR(int id) {
    return crrrDAOImp.get(id);
  }

  public ArrayList<ConferenceRoomResRequest> getAssignedCRRR(String username) {
    return crrrDAOImp.getAssigned(username);
  }

  public ArrayList<ConferenceRoomResRequest> getCreatedCRR(String username) {
    return crrrDAOImp.getCreated(username);
  }

  public int getNextCRRRID() {
    return crrrDAOImp.getNextID();
  }

  public ArrayList<ConferenceRoomResRequest> filterDateCRRR(Date d) {
    return crrrDAOImp.filterDate(d);
  }

  // Furniture related methods
  public HashMap<Integer, FurnitureRequest> getFurnitureMap() {
    return furnitureDAOImp.getFurnitureMap();
  }

  public void createCRRRTable() {
    crrrDAOImp.createTable();
  }

  public HashMap<Integer, FurnitureRequest> loadFurnitureFromDatabaseInMap() {
    return furnitureDAOImp.loadDataFromDatabaseInMap();
  }

  public void addFurniture(FurnitureRequest furniture) {
    furnitureDAOImp.add(furniture);
  }

  public void createFurnitureTable() {
    furnitureDAOImp.createTable();
  }

  public void deleteFurniture(FurnitureRequest furniture) {
    furnitureDAOImp.delete(furniture);
  }

  public void updateFurniture(FurnitureRequest furniture) {
    furnitureDAOImp.update(furniture);
  }

  public FurnitureRequest getFurniture(int id) {
    return furnitureDAOImp.getFurniture(id);
  }

  public ArrayList<FurnitureRequest> getAssignedFurniture(String username) {
    return furnitureDAOImp.getAssigned(username);
  }

  public ArrayList<FurnitureRequest> getCreatedFurniture(String username) {
    return furnitureDAOImp.getCreated(username);
  }

  public int getNextFurnitureID() {
    return furnitureDAOImp.getNextID();
  }

  // Meal related methods
  public HashMap<Integer, Meal> getMealMap() {
    return mealDAOImp.getMealMap();
  }

  public void createMealTable() {
    mealDAOImp.createTable();
  }

  public HashMap<Integer, Meal> loadMealsFromDatabaseInMap() {
    return mealDAOImp.loadDataFromDatabaseInMap();
  }

  public void addMeal(Meal meal) {
    mealDAOImp.add(meal);
  }

  public void deleteMeal(Meal meal) {
    mealDAOImp.delete(meal);
  }

  public void updateMeal(Meal meal) {
    mealDAOImp.update(meal);
  }

  public Meal getMeal(int id) {
    return mealDAOImp.getMeal(id);
  }

  public ArrayList<Meal> getAssignedMeal(String username) {
    return mealDAOImp.getAssigned(username);
  }

  public ArrayList<Meal> getCreatedMeal(String username) {
    return mealDAOImp.getCreated(username);
  }

  public int getNextMealID() {
    return mealDAOImp.getNextID();
  }

  // user dao functions
  public HashMap<String, User> getUserMap() {
    return userDAOImp.getUserMap();
  }

  public void createUserTable() {
    userDAOImp.createUserTable();
  }

  public HashMap<String, User> loadUsersFromDatabaseInMap() {
    return userDAOImp.loadUsersFromDatabaseInMap();
  }

  public void addUser(
      int adminYes,
      String userName,
      String password,
      String firstName,
      String lastName,
      String userID) {
    userDAOImp.addUser(adminYes, userName, password, firstName, lastName, userID);
  }

  public User checkUser(String userName, String password) {

    return userDAOImp.checkUser(userName, password);
  }

  public User checkUserByID(String userID) {

    return userDAOImp.checkUserByID(userID);
  }

  public void updatePassword(
      String password1, String password2, String newPassword1, String newPassword2) {
    userDAOImp.updatePassword(password1, password2, newPassword1, newPassword2);
  }

  public void updateName(
      String oldFirstName, String oldLastName, String newFirstName, String newLastName) {
    userDAOImp.updateName(oldFirstName, oldLastName, newFirstName, newLastName);
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

  // signage
  public void createSignageTable() {
    System.out.println("AHHHHHHHHHHHHHHHH");
    signageDAOImp.createTable();
  }

  public HashMap<Integer, SignageComponent> getSignageMap() {
    return signageDAOImp.getSignageMap();
  }

  public HashMap<Integer, SignageComponent> loadSignagesFromDatabaseInMap() {
    return signageDAOImp.loadSignagesFromDatabaseInMap();
  }

  public SignageComponent getSignage(int signageID) {
    return signageDAOImp.getSignage(signageID);
  }

  public void modifySignage(SignageComponent signage) {
    signageDAOImp.modifySignage(signage);
  }

  public void addSignage(SignageComponent signage) {
    signageDAOImp.addSignage(signage);
  }

  public void removeSignage(SignageComponent signage) {
    signageDAOImp.removeSignage(signage);
  }

  public int getNextSignageID() {
    return signageDAOImp.getNextID();
  }

  // Alerts

  public HashMap<Integer, Alert> getAlertMap() {
    return alertDAOImp.getAlertMap();
  }

  public HashMap<Integer, Alert> loadAlertsFromDatabaseInMap() {
    return alertDAOImp.loadAlertsFromDatabaseInMap();
  }

  public Alert getAlert(int ticketNum) {
    return alertDAOImp.getAlert(ticketNum);
  }

  public void modifyAlert(Alert alert) {
    alertDAOImp.modifyAlert(alert);
  }

  public void addAlert(Alert alert) {
    alertDAOImp.addAlert(alert);
  }

  public void removeAlert(Alert alert) {
    alertDAOImp.removeAlert(alert);
  }

  public int getNextAlertTicket() {
    return alertDAOImp.getNextID();
  }
}
