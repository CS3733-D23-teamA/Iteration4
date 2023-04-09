package edu.wpi.teamA.controllers.Map;

import edu.wpi.teamA.database.ORMclasses.Node;
import java.util.ArrayList;

public class MapEditorEntity {
  // MapEditorController controller = new MapEditorController();

  public ArrayList<Node> getFloorNodes(ArrayList<Node> nodeArray, String floor) {
    ArrayList<Node> updatedArray = new ArrayList<Node>();
    for (Node node : nodeArray) {
      if (node.getFloor().equals(floor)) {
        updatedArray.add(node);
      }
    }
    return updatedArray;
  }
}
