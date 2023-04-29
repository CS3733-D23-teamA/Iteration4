package edu.wpi.teamA.pathfinding;

import java.util.ArrayList;

public abstract class Search {

  protected Graph graph = new Graph();
  protected int startID;
  protected int endID;
  protected String accessibilitySetting;

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

  // Takes in a node and returns false if it is something that cannot be travelled through based on
  // accessibility settings
  protected boolean accessibilityCheck(int nodeID) {
    return !(graph.getNodeType(nodeID).equals("ELEV")
            && accessibilitySetting.equals("No Elevators"))
        && !(graph.getNodeType(nodeID).equals("STAI") && accessibilitySetting.equals("No Stairs"));
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
