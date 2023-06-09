package edu.wpi.teamA.entities;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Move;
import edu.wpi.teamA.database.ORMclasses.Node;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

public class MapEntity {
  private final DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

  @Getter private HashMap<Integer, Node> levelL1NodeMap = new HashMap<>();
  @Getter private HashMap<Integer, Node> levelL2NodeMap = new HashMap<>();
  @Getter private HashMap<Integer, Node> level1NodeMap = new HashMap<>();
  @Getter private HashMap<Integer, Node> level2NodeMap = new HashMap<>();
  @Getter private HashMap<Integer, Node> level3NodeMap = new HashMap<>();

  @Getter private HashMap<String, Edge> levelL1EdgeMap = new HashMap<>();
  @Getter private HashMap<String, Edge> levelL2EdgeMap = new HashMap<>();
  @Getter private HashMap<String, Edge> level1EdgeMap = new HashMap<>();
  @Getter private HashMap<String, Edge> level2EdgeMap = new HashMap<>();
  @Getter private HashMap<String, Edge> level3EdgeMap = new HashMap<>();

  // List of all nodes
  private final ArrayList<Node> nodeList = databaseRepo.loadNodesFromDatabaseInArray();
  private HashMap<String, Integer> nameMap;
  private final LevelEntity levels = App.getLevelEntity();

  /** Loads all the node maps for each floor */
  public void loadFloorNodes() {
    for (Map.Entry<Integer, Node> entry : databaseRepo.getNodeMap().entrySet()) {
      Node node = entry.getValue();
      if (node.getFloor().equals("L1")) {
        levelL1NodeMap.put(node.getNodeID(), node);
      } else if (node.getFloor().equals("L2")) {
        levelL2NodeMap.put(node.getNodeID(), node);
      } else if (node.getFloor().equals("1")) {
        level1NodeMap.put(node.getNodeID(), node);
      } else if (node.getFloor().equals("2")) {
        level2NodeMap.put(node.getNodeID(), node);
      } else if (node.getFloor().equals("3")) {
        level3NodeMap.put(node.getNodeID(), node);
      }
    }
  }

  /** Loads all the edge maps for each floor */
  public void loadFloorEdges() {
    for (Map.Entry<String, Edge> entry : databaseRepo.getEdgeMap().entrySet()) {
      Edge edge = entry.getValue();
      Node node = databaseRepo.getNodeMap().get(edge.getStartNode());
      String temp = edge.getStartNode().toString() + edge.getEndNode().toString();
      if (node.getFloor().equals("L1")) {
        levelL1EdgeMap.put(temp, edge);
      } else if (node.getFloor().equals("L2")) {
        levelL2EdgeMap.put(temp, edge);
      } else if (node.getFloor().equals("1")) {
        level1EdgeMap.put(temp, edge);
      } else if (node.getFloor().equals("2")) {
        level2EdgeMap.put(temp, edge);
      } else if (node.getFloor().equals("3")) {
        level3EdgeMap.put(temp, edge);
      }
    }
  }

  /**
   * Determines which edge map to use
   *
   * @param level floor level
   * @return the edge map we want to use
   */
  public HashMap<String, Edge> determineEdgeMap(String level) {
    if (level.equals("L1")) {
      return levelL1EdgeMap;
    } else if (level.equals("L2")) {
      return levelL2EdgeMap;
    } else if (level.equals("1")) {
      return level1EdgeMap;
    } else if (level.equals("2")) {
      return level2EdgeMap;
    } else if (level.equals("3")) {
      return level3EdgeMap;
    } else {
      return new HashMap<>();
    }
  }

  /**
   * Determines which node map we want
   *
   * @param level floor level
   * @return the node map for that floor
   */
  public HashMap<Integer, Node> determineNodeMap(String level) {
    if (level.equals("L1")) {
      return levelL1NodeMap;
    } else if (level.equals("L2")) {
      return levelL2NodeMap;
    } else if (level.equals("1")) {
      return level1NodeMap;
    } else if (level.equals("2")) {
      return level2NodeMap;
    } else if (level.equals("3")) {
      return level3NodeMap;
    } else {
      return new HashMap<>();
    }
  }

  /**
   * Adds a temporary circle to show where the user clicked
   *
   * @param X center X for circle
   * @param Y center Y for cicle
   * @return the circle to display
   */
  public Circle addTempCircle(double X, double Y) {
    Circle circle = new Circle();
    circle.setCenterX(X);
    circle.setCenterY(Y);
    circle.setFill(Color.web("0x4cde61"));
    circle.setRadius(10);
    circle.setVisible(true);
    return circle;
  }

  /**
   * Makes a new circle to represent the node
   *
   * @param mapGesturePane gesture pane to place the circle on
   * @param node node to make a circle for
   * @return the circle to display
   */
  public Circle addCircle(GesturePane mapGesturePane, Node node) {
    Circle circle = new Circle();
    circle.setCenterX(node.getXcoord());
    circle.setCenterY(node.getYcoord());
    circle.setRadius(10);
    circle.setVisible(true);

    if (numberOfLocationsOnNode(node.getNodeID()) == 0) {
      circle.setFill(Color.web("0xf74c4c"));
    } else if (numberOfLocationsOnNode(node.getNodeID()) == 1) {
      circle.setFill(Color.web("0x012D5A"));
    } else {
      circle.setFill(Color.web("0x4cde61"));
    }

    circle.setOnMouseDragged(
        mouseEvent -> {
          circle.setCenterX(mouseEvent.getX());
          circle.setCenterY(mouseEvent.getY());
        });

    return circle;
  }

  /**
   * Determines the action for when you drag release a circle
   *
   * @param circle the circle you're performing on
   * @param node the node the circle represents
   * @param mapGesturePane the gesture pane the circle is on
   */
  public void dragReleased(Circle circle, Node node, GesturePane mapGesturePane) {
    if ((node.getXcoord() != (int) circle.getCenterX())
        && (node.getYcoord() != (int) circle.getCenterY())) {
      node.setXcoord((int) circle.getCenterX());
      node.setYcoord((int) circle.getCenterY());
      mapGesturePane.setGestureEnabled(true);
      // update database and big hashmap
      Move move = databaseRepo.getFirstMoveForNode(node.getNodeID());
      determineModifyAction(node.getFloor(), node, databaseRepo.getLocName(move.getLongName()));
      // update level hashmap
      // determineNodeMap(node.getFloor()).remove(node.getNodeID());
      determineNodeMap(node.getFloor()).put(node.getNodeID(), node);
    }
  }

  /**
   * Adds the text onto the map display
   *
   * @param node node you're adding text too
   * @param second whether we are displaying the first or second name on the node
   * @return the text object to display
   */
  public Text addText(Node node, boolean second) {
    double xCoord = node.getXcoord() + 10;
    double yCoord = node.getYcoord() - 10;
    String locText = getLocationName(node.getNodeID(), second).getShortName();
    Text text = new Text(locText);
    text.setX(xCoord);
    text.setY(yCoord);
    text.setFill(Color.web("0x151515"));
    text.setFont(new Font("Open Sans", 20));
    return text;
  }

  /**
   * Adds a line to represent an edge
   *
   * @param startNodeID start node for edge
   * @param endNodeID end node for edge
   * @return the line object
   */
  public Line addLine(int startNodeID, int endNodeID) {

    Node startNode = databaseRepo.getNodeMap().get(startNodeID);
    Node endNode = databaseRepo.getNodeMap().get(endNodeID);

    Line line =
        new Line(
            startNode.getXcoord(), startNode.getYcoord(), endNode.getXcoord(), endNode.getYcoord());

    line.setStroke(Color.web("0x012D5A"));
    line.setStrokeWidth(6);
    line.setVisible(true);
    return line;
  }

  /**
   * Node getter
   *
   * @param nodeID node to get
   * @return the node
   */
  public Node getNodeInfo(int nodeID) {
    return databaseRepo.getNodeMap().get(nodeID);
  }

  /**
   * Location name getter for node
   *
   * @param nodeID node to get
   * @param second whether to get first or second name associated to the node
   * @return the LocationName object
   */
  public LocationName getLocationName(int nodeID, boolean second) {
    Move move;
    if (second) {
      move = databaseRepo.getSecondMoveForNode(nodeID);
    } else {
      move = databaseRepo.getFirstMoveForNode(nodeID);
    }

    if (move != null) {
      return databaseRepo.getLocName(move.getLongName());
    } else {
      return databaseRepo.getLocName("Empty Node");
    }
  }

  /**
   * Number of locations on a node
   *
   * @param nodeID the node to check
   * @return number of locations on a node
   */
  public int numberOfLocationsOnNode(int nodeID) {
    Move firstMove = databaseRepo.getFirstMoveForNode(nodeID);
    Move secondMove = databaseRepo.getSecondMoveForNode(nodeID);
    if (firstMove != null && secondMove != null) {
      if (firstMove.getLongName().equals(secondMove.getLongName())) {
        return 1;
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  /**
   * Gets data needed to add a new node
   *
   * @param level current level
   * @param node node to add
   * @param locName location name to add
   * @param move move to add
   */
  public void determineAddAction(String level, Node node, LocationName locName, Move move) {
    int newNodeID = databaseRepo.getLargestNodeID().getNodeID() + 5;
    node.setNodeID(newNodeID);
    move.setNodeID(newNodeID);
    databaseRepo.addNode(node);
    databaseRepo.addLocName(locName);
    databaseRepo.addMove(move);
    determineNodeMap(level).put(newNodeID, node);
  }

  /**
   * Gets data to remove a node
   *
   * @param nodeID node ID to remove
   * @param level level it's on
   */
  public void determineRemoveAction(int nodeID, String level) {
    ArrayList<Edge> edgesToRemove = databaseRepo.deleteEdgesWithNode(databaseRepo.getNode(nodeID));

    for (Edge edge : edgesToRemove) {
      String key = edge.getStartNode().toString() + edge.getEndNode().toString();
      determineEdgeMap(level).remove(key);
    }
    databaseRepo.deleteNode(databaseRepo.getNode(nodeID));
    Move move = databaseRepo.getFirstMoveForNode(nodeID);
    if (move != null) {
      databaseRepo.deleteLocName(databaseRepo.getLocName(move.getLongName()));
      databaseRepo.deleteMove(databaseRepo.getFirstMoveForNode(nodeID));
    }
    determineNodeMap(level).remove(nodeID);
  }

  /**
   * Gets data to modify a node
   *
   * @param node node to modify
   * @param locName location name to modify
   */
  public void determineModifyAction(String level, Node node, LocationName locName) {
    databaseRepo.updateNode(node);
    String longname = locName.getLongName();
    if (!longname.equals("Empty Node")) {
      databaseRepo.updateLocName(locName);
    } else {
      databaseRepo.addLocName(locName);
    }
    determineNodeMap(node.getFloor()).put(node.getNodeID(), node);

    if (!node.getFloor().equals(level)) {
      determineNodeMap(level).remove(node.getNodeID());
    }
  }

  /**
   * Gets data to modify an edge
   *
   * @param firstNode start node to add edge
   * @param secondNode end node to add edge
   * @param level level to add on
   * @return true if adding an edge, false if removing
   */
  public boolean determineModifyEdgeAction(Node firstNode, Node secondNode, String level) {
    Edge edge = new Edge(firstNode.getNodeID(), secondNode.getNodeID());
    String key = firstNode.getNodeID().toString() + secondNode.getNodeID().toString();
    Edge temp = databaseRepo.getEdgeMap().get(key);
    if (!(firstNode.getNodeID().toString().equals(secondNode.getNodeID().toString()))) {
      if (temp == null) { // if there is no edge between the nodes, we must add edges
        databaseRepo.addEdge(edge);
        determineEdgeMap(level).put(key, edge);
        return true;
      } else { // there is an edge, so we must remove the edge
        databaseRepo.deleteEdge(edge);
        determineEdgeMap(level).remove(key);
        return false;
      }
    }
    return false;
  }

  /**
   * Gets all the long names
   *
   * @return arraylist of long names
   */
  public ArrayList<String> getAllLongNames() {
    ArrayList<String> locNames = new ArrayList<>();
    for (Map.Entry<String, LocationName> entry : databaseRepo.getLocNameMap().entrySet()) {
      locNames.add(entry.getKey());
    }
    return locNames;
  }

  /**
   * Determines if the long name exists in the database
   *
   * @param longName the longname to check
   * @return true if there, else false
   */
  public boolean determineLongNameExists(String longName) {
    return databaseRepo.getLocName(longName) != null;
  }

  /**
   * Determines horizontal node alignment
   *
   * @param nodesToAlign nodes you want to align
   * @return the first node
   */
  public Node determineHorizontalNodeAlignment(ArrayList<Node> nodesToAlign) {
    Node firstNode = nodesToAlign.get(0);
    for (Node node : nodesToAlign) {
      node.setYcoord(firstNode.getYcoord()); // sets all node XCoords to the first node's XCoord

      determineModifyAction(node.getFloor(), node, getLocationName(node.getNodeID(), false));
      if (!getLocationName(node.getNodeID(), false)
          .equals(getLocationName(node.getNodeID(), true))) {
        determineModifyAction(node.getFloor(), node, getLocationName(node.getNodeID(), true));
      }
    }
    return firstNode;
  }

  /**
   * Determines vertical node alignment
   *
   * @param nodesToAlign nodes you want to align
   * @return the first node
   */
  public Node determineVerticalNodeAlignment(ArrayList<Node> nodesToAlign) {
    Node firstNode = nodesToAlign.get(0);
    for (Node node : nodesToAlign) {
      node.setXcoord(firstNode.getXcoord()); // sets all node YCoords to the first node's YCoord

      determineModifyAction(node.getFloor(), node, getLocationName(node.getNodeID(), false));
      if (!getLocationName(node.getNodeID(), false)
          .equals(getLocationName(node.getNodeID(), true))) {
        determineModifyAction(node.getFloor(), node, getLocationName(node.getNodeID(), true));
      }
    }
    return firstNode;
  }

  /**
   * Adds a new move in the database
   *
   * @param move move to add
   */
  public void submitNewMoves(Move move) {
    databaseRepo.addMove(move);
  }

  /**
   * Checks if the add move is valid
   *
   * @param nodeID nodeID to add
   * @param longName longname to add
   * @param localDate localDate to add
   * @return true if valid, else false
   */
  public boolean checkValidMove(int nodeID, String longName, LocalDate localDate) {
    int nodeCount = 0;
    boolean longNameExists = false;
    boolean moveOnDate = false;
    HashMap<String, Move> currentMoves = databaseRepo.loadCurrentMovesMap(localDate);

    if (currentMoves.get(longName).getNodeID() == nodeID) {
      longNameExists = true;
    }

    if (currentMoves.get(longName).getDate().isEqual(localDate)) {
      moveOnDate = true;
    }

    for (Map.Entry<String, Move> entry : currentMoves.entrySet()) {
      Move move = entry.getValue();
      if (move.getNodeID() == nodeID) {
        nodeCount++;
      }
    }

    return (nodeCount > 1) || longNameExists || moveOnDate;
  }

  public ArrayList<Node> loadAllNodes() {
    return databaseRepo.loadNodesFromDatabaseInArray();
  }

  // sets up the hashmap linking long names to nodeIDs
  public void initializeNameIDHashMap() {
    nameMap = new HashMap<String, Integer>();
    for (Node node : nodeList) {
      // set node ID
      int id = node.getNodeID();

      // puts name and ID into name map
      Move firstMove = databaseRepo.getFirstMoveForNode(id);
      if (firstMove != null) {
        if (!firstMove.getLongName().equals("Empty Node")) {
          nameMap.put(firstMove.getLongName(), id);
        }
      }

      Move secondMove = databaseRepo.getSecondMoveForNode(id);
      if (secondMove != null) {
        if (!secondMove.getLongName().equals("Empty Node")) {
          assert firstMove != null;
          if (!secondMove.getLongName().equals(firstMove.getLongName())) {
            nameMap.put(secondMove.getLongName(), id);
          }
        }
      }
    }
  }

  // Creates a list of the long names that correspond to the nodes
  public ArrayList<String> makeListOfLongNames() {
    ArrayList<String> nameOptions = new ArrayList<String>();
    for (Node node : nodeList) {

      // set node ID
      int id = node.getNodeID();

      // get the locationname object with nodetype
      if (numberOfLocationsOnNode(id) == 1) {
        LocationName loc = getLocationName(id, false);
        if (!loc.getNodeType().equals("HALL")) {
          // Add
          nameOptions.add(loc.getLongName());
        }
      } else if (numberOfLocationsOnNode(id) == 2) {
        LocationName loc = getLocationName(id, false);
        if (!loc.getNodeType().equals("HALL")) {
          // Add
          nameOptions.add(loc.getLongName());
        }

        loc = getLocationName(id, true);
        if (!loc.getNodeType().equals("HALL")) {
          // Add
          nameOptions.add(loc.getLongName());
        }
      }
    }
    return nameOptions;
  }

  // takes a long name and returns the corresponding id
  public int getIDFromLongName(String longName) {
    return nameMap.get(longName);
  }

  // takes a path of node ids and returns a list of levels travelled to
  public ArrayList<String> floorsTravelledTo(ArrayList<Integer> path) {
    ArrayList<String> floors = new ArrayList<String>();

    // Goes through the path checking for floors
    for (int nodeID : path) {
      Node node = databaseRepo.getNode(nodeID);

      // gets the floor
      String nodeFloor = node.getFloor();

      // checks if it was seen before
      boolean checked = false;
      for (String pastFloor : floors) {
        if (pastFloor.equals(nodeFloor)) {
          checked = true;
        }
      }

      // if it wasnt seen before, adds it to the list
      if (!checked) {
        floors.add(nodeFloor);
      }
    }
    return floors;
  }

  // Gives floorstravelledto with repeats
  public ArrayList<String> floorsTravelledToWithRepeats(ArrayList<Integer> path) {
    ArrayList<String> floors = new ArrayList<String>();
    String lastFloor = databaseRepo.getNode(path.get(0)).getFloor();
    floors.add(lastFloor);

    // Goes through the path checking for floors
    for (int i = 0; i < path.size(); i++) {
      int nodeID = path.get(i);
      Node node = databaseRepo.getNode(nodeID);

      // gets the floor
      String nodeFloor = node.getFloor();

      // checks if it was seen before
      if (!lastFloor.equals(nodeFloor)) {
        floors.add(nodeFloor);
        lastFloor = nodeFloor;
      }
    }
    return floors;
  }

  /** @returns next level from set level, returns given level if it's the last level */
  public Level getNextLevel() {
    return levels.getNext();
  }

  public Boolean nextLevelExists() {
    return levels.hasNext();
  }

  /** @returns previous level from set level, returns given level if it's the first level */
  public Level getPrevLevel() {
    return levels.getPrev();
  }

  public Boolean prevLevelExists() {
    return levels.hasPrev();
  }

  /** @returns first level in order */
  public Level getFirstLevel() {
    return levels.getOrderedLevel(0);
  }

  /**
   * sets specified level order in map entity
   *
   * @param path takes path traveled to determine floors traveled to
   */
  public void setLevelOrder(ArrayList<Integer> path) {
    levels.setOrder(floorsTravelledToWithRepeats(path));
  }

  /** sets default level order in map entity */
  public void setLevelOrder() {
    levels.setOrder();
  }

  /**
   * gets a hashmap of level objects to return nodes existing on that level
   *
   * @param level object to find nodes for
   * @return Hashmap of nodeIDs and Nodes for given level
   */
  public HashMap<Integer, Node> getNodeMap(Level level) {
    HashMap<Integer, Node> nodeMap;
    switch (level.toString()) {
      case "L1":
        nodeMap = levelL1NodeMap;
        break;
      case "L2":
        nodeMap = levelL2NodeMap;
        break;
      case "1":
        nodeMap = level1NodeMap;
        break;
      case "2":
        nodeMap = level2NodeMap;
        break;
      default:
        nodeMap = level3NodeMap;
        break;
    }
    return nodeMap;
  }
}
