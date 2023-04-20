package edu.wpi.teamA.controllers.Map;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Move;
import edu.wpi.teamA.database.ORMclasses.Node;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.Getter;

public class MapEditorEntity {
  private final DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

  @Getter private HashMap<Integer, Node> levelL1NodeMap = new HashMap<Integer, Node>();
  @Getter private HashMap<Integer, Node> levelL2NodeMap = new HashMap<Integer, Node>();
  @Getter private HashMap<Integer, Node> level1NodeMap = new HashMap<Integer, Node>();
  @Getter private HashMap<Integer, Node> level2NodeMap = new HashMap<Integer, Node>();
  @Getter private HashMap<Integer, Node> level3NodeMap = new HashMap<Integer, Node>();

  @Getter private HashMap<String, Edge> levelL1EdgeMap = new HashMap<String, Edge>();
  @Getter private HashMap<String, Edge> levelL2EdgeMap = new HashMap<String, Edge>();
  @Getter private HashMap<String, Edge> level1EdgeMap = new HashMap<String, Edge>();
  @Getter private HashMap<String, Edge> level2EdgeMap = new HashMap<String, Edge>();
  @Getter private HashMap<String, Edge> level3EdgeMap = new HashMap<String, Edge>();

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

  public Circle addCircle(double X, double Y) {
    Circle circle = new Circle();
    circle.setCenterX(X);
    circle.setCenterY(Y);
    circle.setFill(Color.web("0x012D5A"));
    circle.setRadius(10);
    circle.setVisible(true);
    return circle;
  }

  public Text addText(Node node) {
    double xCoord = node.getXcoord() + 20;
    double yCoord = node.getYcoord() - 10;
    String locText = getLocationName(node.getNodeID()).getShortName();
    Text text = new Text(locText);
    text.setX(xCoord);
    text.setY(yCoord);
    text.setFill(Color.web("0x151515"));
    text.setFont(new Font("Open Sans", 20));
    return text;
  }

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

  public Node getNodeInfo(int nodeID) {
    return databaseRepo.getNodeMap().get(nodeID);
  }

  public LocationName getLocationName(int nodeID) {
    Move move = databaseRepo.getMove(nodeID);
    return databaseRepo.getLocName(move.getLongName());
  }

  public void determineAddAction(
      String level,
      int x,
      int y,
      String longName,
      String shortName,
      String floor,
      String building,
      String nodeType) {
    int newNodeID = databaseRepo.getLargestNodeID().getNodeID() + 5;
    Node node = databaseRepo.addNode(newNodeID, x, y, floor, building);
    String month = Integer.toString(LocalDate.now().getMonthValue());
    String day = Integer.toString(LocalDate.now().getDayOfMonth());
    if (month.length() == 1) {
      month = "0" + month;
    }
    if (day.length() == 1) {
      day = "0" + day;
    }

    String dateString = month + "/" + day + "/" + LocalDate.now().getYear();
    databaseRepo.addLocName(longName, shortName, nodeType);
    databaseRepo.addMove(newNodeID, longName, dateString);
    determineNodeMap(level).put(newNodeID, node);
  }

  public void determineRemoveAction(int nodeID, String level) {
    ArrayList<Edge> edgesToRemove = databaseRepo.deleteEdgesWithNode(nodeID);
    for (Edge edge : edgesToRemove) {
      String key = edge.getStartNode().toString() + edge.getEndNode().toString();
      determineEdgeMap(level).remove(key);
    }
    databaseRepo.deleteNode(nodeID);
    databaseRepo.deleteLocName(databaseRepo.getMove(nodeID).getLongName());
    databaseRepo.deleteMove(nodeID);
    determineNodeMap(level).remove(nodeID);
  }

  public void determineModifyAction(
      String level,
      int nodeID,
      int x,
      int y,
      String oldLongName,
      String oldShortName,
      String longName,
      String shortName,
      String floor,
      String building,
      String nodeType) {
    databaseRepo.updateNode(nodeID, x, y, floor, building);
    String month = Integer.toString(LocalDate.now().getMonthValue());
    String day = Integer.toString(LocalDate.now().getDayOfMonth());
    if (month.length() == 1) {
      month = "0" + month;
    }
    if (day.length() == 1) {
      day = "0" + day;
    }

    String dateString = month + "/" + day + "/" + LocalDate.now().getYear();
    databaseRepo.updateLocName(oldLongName, oldShortName, longName, shortName, nodeType);
    databaseRepo.updateMove(nodeID, longName, dateString);

    Node node = databaseRepo.getNode(nodeID);
    determineNodeMap(level).put(nodeID, node);
  }

  public boolean determineModifyEdgeAction(Node firstNode, Node secondNode, String level) {
    String key = firstNode.getNodeID().toString() + secondNode.getNodeID().toString();
    Edge temp = databaseRepo.getEdgeMap().get(key);
    if (!(firstNode.getNodeID().toString().equals(secondNode.getNodeID().toString()))) {
      if (temp == null) { // if there is no edge between the nodes, we must add edges
        Edge edge = databaseRepo.addEdge(firstNode.getNodeID(), secondNode.getNodeID());
        determineEdgeMap(level).put(key, edge);
        return true;
      } else { // there is an edge, so we must remove the edge
        databaseRepo.deleteEdge(firstNode.getNodeID(), secondNode.getNodeID());
        determineEdgeMap(level).remove(key);
        return false;
      }
    }
    return false;
  }

  public boolean determineLongNameExists(String longName) {
    if (databaseRepo.getLocName(longName) == null) {
      return false;
    }
    return true;
  }

  public void importExport(boolean imported, String DAOimp) {
    if (imported) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open CSV File");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
      File selectedFile = fileChooser.showOpenDialog(App.getPrimaryStage());
      if (DAOimp.equals("Node")) {
        databaseRepo.importData(selectedFile.getPath(), DAOimp);
      } else if (DAOimp.equals("LocationName")) {
        databaseRepo.importData(selectedFile.getPath(), DAOimp);
      } else if (DAOimp.equals("Move")) {
        databaseRepo.importData(selectedFile.getPath(), DAOimp);
      } else if (DAOimp.equals("Edge")) {
        databaseRepo.importData(selectedFile.getPath(), DAOimp);
      }
    } else {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Export CSV File to");
      File selectedDirectory = directoryChooser.showDialog(App.getPrimaryStage());
      if (DAOimp.equals("Node")) {
        databaseRepo.exportData(selectedDirectory.getPath(), DAOimp);
      } else if (DAOimp.equals("LocationName")) {
        databaseRepo.exportData(selectedDirectory.getPath(), DAOimp);
      } else if (DAOimp.equals("Move")) {
        databaseRepo.exportData(selectedDirectory.getPath(), DAOimp);
      } else if (DAOimp.equals("Edge")) {
        databaseRepo.exportData(selectedDirectory.getPath(), DAOimp);
      }
    }
  }
}
