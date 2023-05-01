package edu.wpi.teamA.pathfinding;

import java.util.ArrayList;

public abstract class Search {

  protected Graph graph = new Graph();
  protected int startID;
  protected int endID;
  protected boolean accessibilitySetting;

  protected ArrayList<Integer> path = new ArrayList<Integer>();

  public ArrayList<Integer> getPath() {
    return path;
  }

  protected abstract ArrayList<Integer> setPath();

  protected ArrayList<Integer> getOrder(int startID, int endID) {
    ArrayList<Integer> path = new ArrayList<>();
    int currentID = endID;
    GraphNode currentGNode = graph.getGraphNode(currentID);
    while (currentID != startID) { // while the path still has a previous path
      path.add(0, currentID);
      currentGNode = currentGNode.getPrev();
      currentID = currentGNode.getNodeID();
    }
    path.add(0, startID);
    return path;
  }

  protected void resetNodes(ArrayList<Integer> resetList) {
    for (int i : resetList) {
      System.out.println("RESET NODES: i-" + i);
      graph.getGraphNode(i).reset();
    }
  }

  /**
   * access level: 0 = none, 1 = no elevator, 2 = no stairs
   *
   * @param nodeID takes an int representing a node id
   * @returns true if inputted node is equal to set accessibility level
   */
  protected boolean accessibilityCheck(int nodeID) {
    // TODO handle empty string for null move
    return !(accessibilitySetting && graph.getNodeType(nodeID).equals("STAI"));
  }

  public GraphNode getGraphNode(int key) {
    return graph.getGraphNode(key);
  }

  public String toString() {

    String stringPath = "Wow! You're already there! Good Job!";

    if (startID != endID) {

      stringPath = "Start at node " + path.get(0);

      for (int i = 1; i < path.size(); i++) {
        stringPath += ", then go to node " + path.get(i);
      }

      stringPath += ". You have reached your destination.";
    }

    return stringPath;
  }
}
