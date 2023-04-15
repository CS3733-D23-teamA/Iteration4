package edu.wpi.teamA.controllers.Map;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DAOImps.EdgeDAOImp;
import edu.wpi.teamA.database.DAOImps.LocNameDAOImp;
import edu.wpi.teamA.database.DAOImps.MoveDAOImp;
import edu.wpi.teamA.database.DAOImps.NodeDAOImp;
import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Move;
import edu.wpi.teamA.database.ORMclasses.Node;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.Getter;

public class MapEditorEntity {
  // MapEditorController controller = new MapEditorController();
  private final NodeDAOImp nodeDAO = new NodeDAOImp();
  private final LocNameDAOImp locNameDAO = new LocNameDAOImp();
  private final MoveDAOImp moveDAO = new MoveDAOImp();
  private final EdgeDAOImp edgeDAO = new EdgeDAOImp();
  @Getter private ArrayList<Node> nodeArray = nodeDAO.loadNodesFromDatabase();
  // @Getter private ArrayList<Node> levelGNodeArray = new ArrayList<Node>(); // =
  // getFloorNodes(nodeArray, "G");
  @Getter
  private ArrayList<Node> levelL1NodeArray =
      new ArrayList<Node>(); // = getFloorNodes(nodeArray, "L1");

  @Getter
  private ArrayList<Node> levelL2NodeArray =
      new ArrayList<Node>(); // = getFloorNodes(nodeArray, "L2");

  @Getter
  private ArrayList<Node> level1NodeArray =
      new ArrayList<Node>(); // = getFloorNodes(nodeArray, "1");

  @Getter
  private ArrayList<Node> level2NodeArray =
      new ArrayList<Node>(); // = getFloorNodes(nodeArray, "2");

  @Getter
  private ArrayList<Node> level3NodeArray =
      new ArrayList<Node>(); // = getFloorNodes(nodeArray, "3");

  @Getter private ArrayList<Edge> edgeArray = edgeDAO.loadEdgesFromDatabase();
  @Getter private ArrayList<Edge> levelL1EdgeArray = new ArrayList<Edge>();

  @Getter private ArrayList<Edge> levelL2EdgeArray = new ArrayList<Edge>();

  @Getter private ArrayList<Edge> level1EdgeArray = new ArrayList<Edge>();

  @Getter private ArrayList<Edge> level2EdgeArray = new ArrayList<Edge>();

  @Getter private ArrayList<Edge> level3EdgeArray = new ArrayList<Edge>();

  public ArrayList<Node> getFloorNodes(ArrayList<Node> nodeArray, String floor) {
    ArrayList<Node> updatedArray = new ArrayList<Node>();
    for (Node node : nodeArray) {
      if (node.getFloor().equals(floor)) {
        updatedArray.add(node);
      }
    }
    return updatedArray;
  }

  public ArrayList<Edge> getFloorEdges(ArrayList<Edge> edgeArray, String floor) {
    ArrayList<Edge> updatedArray = new ArrayList<Edge>();
    for (int i = 0; i < edgeArray.size(); i++) {
      Node startNode = nodeDAO.getNode(edgeArray.get(i).getStartNode());
      Node endNode = nodeDAO.getNode(edgeArray.get(i).getEndNode());
      if (startNode.getFloor().equals(floor) && endNode.getFloor().equals(floor)) {
        updatedArray.add(edgeArray.get(i));
      }
    }
    return updatedArray;
  }

  public void loadFloorNodes() {
    for (Node node : nodeArray) {
      if (node.getFloor().equals("L1")) {
        levelL1NodeArray.add(node);
      } else if (node.getFloor().equals("L2")) {
        levelL2NodeArray.add(node);
      } else if (node.getFloor().equals("1")) {
        level1NodeArray.add(node);
      } else if (node.getFloor().equals("2")) {
        level2NodeArray.add(node);
      } else if (node.getFloor().equals("3")) {
        level3NodeArray.add(node);
      }
    }
  }

  public void loadFloorEdges() {
    for (Edge edge : edgeArray) {
      Node node = nodeDAO.getNode(edge.getStartNode());
      if (node.getFloor().equals("L1")) {
        levelL1EdgeArray.add(edge);
      } else if (node.getFloor().equals("L2")) {
        levelL2EdgeArray.add(edge);
      } else if (node.getFloor().equals("1")) {
        level1EdgeArray.add(edge);
      } else if (node.getFloor().equals("2")) {
        level2EdgeArray.add(edge);
      } else if (node.getFloor().equals("3")) {
        level3EdgeArray.add(edge);
      }
    }
  }

  public ArrayList<Edge> determineEdgeArray(String level) {
    if (level.equals("Level L1")) {
      return levelL1EdgeArray;
    } else if (level.equals("Level L2")) {
      return levelL2EdgeArray;
    } else if (level.equals("Level 1")) {
      return level1EdgeArray;
    } else if (level.equals("Level 2")) {
      return level2EdgeArray;
    } else if (level.equals("Level 3")) {
      return level3EdgeArray;
    } else {
      return null;
    }
  }

  public ArrayList<Node> determineNodeArray(String level) {
    if (level.equals("Level L1")) {
      return levelL1NodeArray;
    } else if (level.equals("Level L2")) {
      return levelL2NodeArray;
    } else if (level.equals("Level 1")) {
      return level1NodeArray;
    } else if (level.equals("Level 2")) {
      return level2NodeArray;
    } else if (level.equals("Level 3")) {
      return level3NodeArray;
    } else {
      return null;
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

  public Line addLine(int startNodeID, int endNodeID) {
    Node startNode = nodeDAO.getNode(startNodeID);
    Node endNode = nodeDAO.getNode(endNodeID);

    LocationName startLocName = locNameDAO.getLocName(moveDAO.getMove(startNodeID).getLongName());
    LocationName endLocName = locNameDAO.getLocName(moveDAO.getMove(endNodeID).getLongName());

    Line line =
        new Line(
            startNode.getXcoord(), startNode.getYcoord(), endNode.getXcoord(), endNode.getYcoord());

    if (startLocName.getNodeType().equals("ELEV") && endLocName.getNodeType().equals("ELEV")) {
      line.setStroke(Color.web("0xEEBD28"));
      line.setStrokeWidth(4);
    } else if (startLocName.getNodeType().equals("STAI")
        && endLocName.getNodeType().equals("STAI")) {
      line.setStroke(Color.web("0xf74c4c"));
      line.setStrokeWidth(10);
    } else {
      line.setStroke(Color.web("0x012D5A"));
      line.setStrokeWidth(7);
    }
    line.setVisible(true);
    return line;
  }

  public Node getNodeInfo(int nodeID) {
    return nodeDAO.getNode(nodeID);
  }

  public LocationName getLocationName(int nodeID) {
    Move move = moveDAO.getMove(nodeID);
    return locNameDAO.getLocName(move.getLongName());
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
    int newNodeID = nodeDAO.getLargestNodeID().getNodeID() + 5;
    //    System.out.println(nodeArray.size());
    //    System.out.println(newNodeID);
    nodeDAO.Add(newNodeID, x, y, floor, building);
    String month = Integer.toString(LocalDate.now().getMonthValue());
    String day = Integer.toString(LocalDate.now().getDayOfMonth());
    if (month.length() == 1) {
      month = "0" + month;
    }
    if (day.length() == 1) {
      day = "0" + day;
    }

    String dateString = month + "/" + day + "/" + LocalDate.now().getYear();
    locNameDAO.Add(longName, shortName, nodeType);
    moveDAO.Add(newNodeID, longName, dateString);
    determineNodeArray(level).add(nodeDAO.getNode(newNodeID));
  }

  public void determineRemoveAction(int nodeID) {
    nodeDAO.Delete(nodeID);
    locNameDAO.Delete(moveDAO.getMove(nodeID).getLongName());
    moveDAO.Delete(nodeID);
    updateArrays();
  }

  public void determineModifyAction(
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
    nodeDAO.Update(nodeID, x, y, floor, building);
    String month = Integer.toString(LocalDate.now().getMonthValue());
    String day = Integer.toString(LocalDate.now().getDayOfMonth());
    if (month.length() == 1) {
      month = "0" + month;
    }
    if (day.length() == 1) {
      day = "0" + day;
    }

    String dateString = month + "/" + day + "/" + LocalDate.now().getYear();
    locNameDAO.Update(oldLongName, oldShortName, longName, shortName, nodeType);
    moveDAO.Update(nodeID, longName, dateString);
    updateArrays();
  }

  private void updateArrays() {
    nodeArray = nodeDAO.loadNodesFromDatabase();
    levelL1NodeArray.clear();
    levelL2NodeArray.clear();
    level1NodeArray.clear();
    level2NodeArray.clear();
    level3NodeArray.clear();
    loadFloorNodes();
  }

  public void importExport(boolean imported, String DAOimp) {
    if (imported) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open CSV File");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
      File selectedFile = fileChooser.showOpenDialog(App.getPrimaryStage());
      if (DAOimp.equals("Node")) {
        NodeDAOImp.Import(selectedFile.getPath());
      } else if (DAOimp.equals("LocationName")) {
        LocNameDAOImp.Import(selectedFile.getPath());
      } else if (DAOimp.equals("Move")) {
        MoveDAOImp.Import(selectedFile.getPath());
      } else if (DAOimp.equals("Edge")) {
        EdgeDAOImp.Import(selectedFile.getPath());
      }
    } else {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Export CSV File to");
      File selectedDirectory = directoryChooser.showDialog(App.getPrimaryStage());
      if (DAOimp.equals("Node")) {
        NodeDAOImp.Export(selectedDirectory.getPath());
      } else if (DAOimp.equals("LocationName")) {
        LocNameDAOImp.Export(selectedDirectory.getPath());
      } else if (DAOimp.equals("Move")) {
        MoveDAOImp.Export(selectedDirectory.getPath());
      } else if (DAOimp.equals("Edge")) {
        EdgeDAOImp.Export(selectedDirectory.getPath());
      }
    }
  }
}
