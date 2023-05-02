package edu.wpi.teamA.pathfinding;

import edu.wpi.teamA.database.ORMclasses.Edge;
import java.util.ArrayList;

public class Dijkstra extends Search {

  public Dijkstra(int startID, int endID) {
    this(startID, endID, false);
  }

  public Dijkstra(int startID, int endID, boolean accessiblitySetting) {
    this.graph.prepGraph();
    this.startID = startID;
    this.endID = endID;
    this.accessibilitySetting = accessiblitySetting;
    setPath();
  }

  public Dijkstra(Graph graph, int startID, int endID) {
    this.graph = graph;
    this.startID = startID;
    this.endID = endID;
    this.accessibilitySetting = false;
    setPath();
  }

  //    public class Wrapping {
  //        Node currentNode;
  //        Wrapping previousPath;
  //        boolean noPrevPath;
  //
  //        public Wrapping(Node currentNode, Wrapping previousPath) {
  //            this.currentNode = currentNode;
  //            this.previousPath = previousPath;
  //            noPrevPath = false;
  //        }
  //
  //        public Wrapping(Node currentNode) {
  //            this.currentNode = currentNode;
  //            noPrevPath = true;
  //        }
  //
  //        public void addPath(Wrapping path) {
  //            this.previousPath = path;
  //            noPrevPath = false;
  //        }
  //    }

  /**
   * pathOfNodesAStar: A* Algorithm Implementation
   *
   * @return path of nodes
   */
  protected ArrayList<Integer> setPath() {
    ArrayList<Integer> queue = new ArrayList<>();

    ArrayList<Integer> nodesToReset = new ArrayList<>();

    nodesToReset.add(startID);
    // queue.add(startID);

    nodesToReset.add(startID);

    GraphNode endNode = graph.getGraphNode(endID);
    int endX = endNode.getXcoord();
    int endY = endNode.getYcoord();
    GraphNode currentNode = graph.getGraphNode(startID);
    int currentX = currentNode.getXcoord();
    int currentY = currentNode.getYcoord();

    // currentNode info
    currentNode.setgCost(0);

    while (currentNode.getNodeID() != endID) {
      for (int i = 0; i < currentNode.edgeCount(); i++) {
        Edge currentEdge = currentNode.getEdge(i);
        int otherNodeID;
        GraphNode otherGNode;
        if (currentEdge.getStartNode()
            == currentNode
                .getNodeID()) { // check whether node is starting node or ending node in the
          // edge
          otherNodeID = currentEdge.getEndNode();
        } else {
          otherNodeID = currentEdge.getStartNode();
        }
        otherGNode = graph.getGraphNode(otherNodeID);
        int gCost =
                currentNode.getgCost()
                        + (int)
                        Math.hypot(
                                currentX - otherGNode.getXcoord(), currentY - otherGNode.getYcoord());
        if (accessibilityCheck(otherNodeID)) {
          if (!otherGNode.isVisited()) {

            if (otherGNode.getPrev().getNodeID() == otherNodeID) {
              otherGNode.setgCost(gCost);

              otherGNode.setPrev(currentNode);
              queue.add(otherNodeID);
              otherGNode.setVisited(true);
              nodesToReset.add(currentNode.getNodeID());
            }
          } else {
            if (gCost < otherGNode.getgCost()) {
              otherGNode.setgCost(gCost);
              otherGNode.setPrev(currentNode);
            }
          }
        }
      }

      currentNode.setVisited(true);

      try {
        currentNode = graph.getGraphNode(queue.remove(0));
      } catch (Exception e) {
        return null;
      }
      currentX = currentNode.getXcoord();
      currentY = currentNode.getYcoord();
    }

    ArrayList<Integer> path = getOrder(startID, endID);

    resetNodes(nodesToReset);

    this.path = path;

    return path;
  }

}
