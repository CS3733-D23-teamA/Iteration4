package edu.wpi.teamA.controllers.Map;

import edu.wpi.teamA.database.DAOImps.LocNameDAOImp;
import edu.wpi.teamA.database.DAOImps.MoveDAOImp;
import edu.wpi.teamA.database.DAOImps.NodeDAOImp;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Move;
import edu.wpi.teamA.database.ORMclasses.Node;
import java.util.ArrayList;
import javafx.scene.shape.Circle;

public class MapEditorEntity {
  // MapEditorController controller = new MapEditorController();
  private final NodeDAOImp nodeDAO = new NodeDAOImp();
  private final LocNameDAOImp locNameDAO = new LocNameDAOImp();
  private final MoveDAOImp moveDAO = new MoveDAOImp();
  private ArrayList<Node> nodeArray = nodeDAO.loadNodesFromDatabase();
  private ArrayList<Node> levelGNodeArray = getFloorNodes(nodeArray, "G");
  private ArrayList<Node> levelL1NodeArray = getFloorNodes(nodeArray, "L1");
  private ArrayList<Node> levelL2NodeArray = getFloorNodes(nodeArray, "L2");
  private ArrayList<Node> level1NodeArray = getFloorNodes(nodeArray, "1");
  private ArrayList<Node> level2NodeArray = getFloorNodes(nodeArray, "2");
  private ArrayList<Node> level3NodeArray = getFloorNodes(nodeArray, "3");

  public ArrayList<Node> getFloorNodes(ArrayList<Node> nodeArray, String floor) {
    ArrayList<Node> updatedArray = new ArrayList<Node>();
    for (Node node : nodeArray) {
      if (node.getFloor().equals(floor)) {
        updatedArray.add(node);
      }
    }
    return updatedArray;
  }

  public ArrayList<Node> determineArray(String level) {
    if (level.equals("Level G")) {
      return levelGNodeArray;
    } else if (level.equals("Level L1")) {
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
    circle.setRadius(2);
    return circle;
  }

  public LocationName getLocationName(int nodeID) {
    Move move = moveDAO.getMove(nodeID);
    return locNameDAO.getLocName(move.getLongName());

    /*if (locName.getNodeType().equals("HALL")) {
      return locName.getLongName();
    } else {
      return locName.getShortName();
    }*/
  }

  public void determineAddAction(String longName, String shortName, String floor, String building, String nodeType) {}

  public void determineRemoveAction(boolean removeNodeClicked, int nodeID) {
    // nodeDAO.Delete(nodeID);
  }

  public void determineModifyAction(String longName, String shortName, String floor, String building, String nodeType) {}

  public void modifyNodeDatabase(
      String longName, String shortName, String floor, String building, String nodeType) {}

  public Node getNodeInfo(int nodeID) {
    return nodeDAO.getNode(nodeID);
  }
}
