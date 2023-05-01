package edu.wpi.teamA.pathfinding;

import edu.wpi.teamA.database.ORMclasses.Edge;
import java.util.ArrayList;

// vincent and ryan
public class DFS extends Search {

  public DFS(int startID, int endID) {
    this(startID, endID, false);
  }

  public DFS(int startID, int endID, boolean accessiblitySetting) {
    this.graph.prepGraph();
    this.startID = startID;
    this.endID = endID;
    this.accessibilitySetting = accessiblitySetting;
    setPath();
  }

  public DFS(Graph graph, int startID, int endID) {
    this.graph = graph;
    this.startID = startID;
    this.endID = endID;
    this.accessibilitySetting = false;
    setPath();
  }

  /**
   * setPath: DFS Algorithm Implementation
   *
   * @return path of nodes as integer IDs
   */
  protected ArrayList<Integer> setPath() {

    ArrayList<Integer> queue = new ArrayList<>();
    ArrayList<Integer> nodesToReset = new ArrayList<>();

    nodesToReset.add(startID);

    int currentID = startID;

    GraphNode currentGNode = graph.getGraphNode(currentID);

    while (currentID != endID) {
      for (int i = 0; i < currentGNode.edgeCount(); i++) {

        Edge currentEdge = currentGNode.getEdge(i); // go through all edges for the node
        int otherNodeID; // why is this defined in a loop?

        if (currentEdge.getStartNode()
            == currentID) { // check whether node is starting node or ending node in the edge
          otherNodeID = currentEdge.getEndNode();
        } else {
          otherNodeID = currentEdge.getStartNode();
        }
        GraphNode otherGNode = graph.getGraphNode(otherNodeID);
        //                boolean otherVisited = false;
        //                for (int j = 0; j < visited.size(); j++) {
        //                    if (otherNode
        //                            .getNodeID() == visited.get(j).getNodeID()) { // check if node
        // has been visited
        //                        otherVisited = true;
        //                    }
        //                }

        if (!otherGNode.isVisited()
            && accessibilityCheck(
                otherNodeID)) { // if not visited, add to queue and add to wrapping queue
          otherGNode.setPrev(currentGNode);
          nodesToReset.add(otherNodeID);
          queue.add(otherNodeID);
          otherGNode.setVisited(true);
        }
      }

      currentGNode.setVisited(true);
      currentID = queue.remove(queue.size() - 1);
      currentGNode = graph.getGraphNode(currentID);
    }

    ArrayList<Integer> path = getOrder(startID, endID);

    resetNodes(nodesToReset);

    this.path = path;

    return path;
  }

  /**
   * getOrder: ???
   *
   * @param startID
   * @param endID
   * @return ????
   */

  /**
   * resetNodes: ???
   *
   * @param ??
   * @return ????
   */
}
