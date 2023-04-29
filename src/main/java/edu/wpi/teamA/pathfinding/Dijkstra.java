/*package edu.wpi.teamA.pathfinding;

import java.util.*;
import java.util.ArrayList;

public class Dijkstra extends Search {

  public Dijkstra(int startID, int endID) {
    this.graph.prepGraph();
    this.startID = startID;
    this.endID = endID;
    setPath();
  }

  public Dijkstra(Graph graph, int startID, int endID) {
    this.graph = graph;
    this.startID = startID;
    this.endID = endID;

    setPath();
  }

  protected ArrayList<Integer> setPath() {
    ArrayList<Integer> queue = new ArrayList<>();
    ArrayList<Integer> nodesSet = new ArrayList<>();
    int graphSize = this.graph.getNumNodes();
    int[] distances = new int[graphSize];

    queue.add(startID); // add the starting node
    GraphNode endNode = graph.getGraphNode(endID);
    distances[startID] = 0;

    int minDistance = 0;
    // while, not all the nodes have calculated min dist
    while (nodesSet.size() != graphSize) {
      if (queue.isEmpty()) {
        break; // how can I get rid of this?
      }

      for (Integer i : queue) { // remove node with min distance from queue
        if (i < minDistance) {
          minDistance = i;
        }
      }
      queue.remove(minDistance);

      if (nodesSet.contains(minDistance)) {
        continue;
      }

      nodesSet.add(minDistance); // add the min distance for a node to set list

      // Look at neighbors of the current node
      GraphNode currentNode = graph.getGraphNode(minDistance);
      //for (GraphNode current : currentNode.neighborNodes) { // How can I


      }
    }

    return priorityQ;
  }
}*/
