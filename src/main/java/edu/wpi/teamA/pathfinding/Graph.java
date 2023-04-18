package edu.wpi.teamA.pathfinding;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.database.ORMclasses.Node;
import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
  private HashMap<Integer, GraphNode> pathfindingArea;

  public Graph() {
    this.pathfindingArea = new HashMap<Integer, GraphNode>();
  }

  public void prepGraph() {

    //    NodeDAOImp nodeDAO = new NodeDAOImp();
    //    EdgeDAOImp edgeDAO = new EdgeDAOImp();

    DataBaseRepository databaseRepo = new DataBaseRepository();

    ArrayList<Node> allNodes = databaseRepo.loadNodesFromDatabaseInArray();
    ArrayList<Edge> allEdges = databaseRepo.loadEdgesFromDatabaseInArray();

    for (Node n : allNodes) {
      GraphNode g =
          new GraphNode(n.getNodeID(), n.getXcoord(), n.getYcoord(), n.getFloor(), n.getBuilding());
      addNodeToGraph(g);
    }

    for (Edge e : allEdges) {
      getGraphNode(e.getStartNode()).addEdge(e);
      getGraphNode(e.getEndNode()).addEdge(e);
    }

    // traverse through the node entities  (l1nodes)

    /**
     * for (l1nodes node: allNodes) { GraphNode graphNode; graphNode - new GraphNode(node.getNodeID,
     * nodegetxCoord(), node.getYCoord()); }
     */
  }

  public void addNodeToGraph(GraphNode graphNode) {
    pathfindingArea.put(graphNode.getNodeID(), graphNode);
  }

  public GraphNode getGraphNode(int key) {
    return pathfindingArea.get(key);
  }
}
