package edu.wpi.teamA;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.pathfinding.DFS;
import edu.wpi.teamA.pathfinding.Graph;
import edu.wpi.teamA.pathfinding.GraphNode;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DFSTest {

  Graph graph = new Graph();
  /*
  GraphNode nodeA = new GraphNode(11, 0, 0, "", "");
  GraphNode nodeB = new GraphNode(12, 0, 0, "", "");
  GraphNode nodeC = new GraphNode(13, 0, 0, "", "");
  GraphNode nodeD = new GraphNode(14, 0, 0, "", "");
  GraphNode nodeE = new GraphNode(15, 0, 0, "", "");
  */

  @BeforeEach
  protected void setUp() throws Exception {
    System.out.println("Setting it up...");
    // Graph graph = new Graph();
    GraphNode nodeA = new GraphNode(11, 0, 0, "", "");
    GraphNode nodeB = new GraphNode(12, 0, 0, "", "");
    GraphNode nodeC = new GraphNode(13, 0, 0, "", "");
    GraphNode nodeD = new GraphNode(14, 0, 0, "", "");
    GraphNode nodeE = new GraphNode(15, 0, 0, "", "");
    GraphNode nodeF = new GraphNode(16, 0, 0, "", "");
    GraphNode nodeG = new GraphNode(17, 0, 0, "", "");
    GraphNode nodeH = new GraphNode(18, 0, 0, "", "");

    graph.addNodeToGraph(nodeA);
    graph.addNodeToGraph(nodeB);
    graph.addNodeToGraph(nodeC);
    graph.addNodeToGraph(nodeD);
    graph.addNodeToGraph(nodeE);
    graph.addNodeToGraph(nodeF);
    graph.addNodeToGraph(nodeG);
    graph.addNodeToGraph(nodeH);

    Edge edgeAB = new Edge(nodeA.getNodeID(), nodeB.getNodeID());
    Edge edgeAC = new Edge(nodeA.getNodeID(), nodeC.getNodeID());
    Edge edgeAD = new Edge(nodeA.getNodeID(), nodeD.getNodeID());
    Edge edgeBE = new Edge(nodeB.getNodeID(), nodeE.getNodeID());
    Edge edgeCF = new Edge(nodeC.getNodeID(), nodeF.getNodeID());
    Edge edgeDG = new Edge(nodeD.getNodeID(), nodeG.getNodeID());
    Edge edgeHE = new Edge(nodeH.getNodeID(), nodeE.getNodeID());
    Edge edgeHF = new Edge(nodeH.getNodeID(), nodeF.getNodeID());
    Edge edgeHG = new Edge(nodeH.getNodeID(), nodeG.getNodeID());

    // Tutorials Point DFS Graph
    nodeA.addEdge(edgeAB);
    nodeB.addEdge(edgeAB);
    nodeB.addEdge(edgeBE);
    nodeE.addEdge(edgeBE);
    //  nodeH.addEdge(edgeHE);
    //  nodeE.addEdge(edgeHE);
    nodeF.addEdge(edgeHF);
    nodeH.addEdge(edgeHF);
    nodeF.addEdge(edgeCF);
    nodeC.addEdge(edgeCF);
    // nodeC.addEdge(edgeAC);
    // nodeA.addEdge(edgeAC);
    nodeA.addEdge(edgeAD);
    nodeD.addEdge(edgeAD);
    nodeD.addEdge(edgeDG);
    nodeG.addEdge(edgeDG);
    nodeH.addEdge(edgeHG);
    nodeG.addEdge(edgeHG);
  }

  @Test
  public void testSetPathDFS() {
    DFS dfs1 = new DFS(graph, 11, 13); // moves down the right branch and up the middle
    ArrayList<Integer> expected_path = new ArrayList<Integer>();
    expected_path.add(11);
    expected_path.add(14);
    expected_path.add(17);
    expected_path.add(18);
    expected_path.add(16);
    expected_path.add(13);
    ArrayList<Integer> actual_path = dfs1.getPath();
    assertEquals(expected_path, actual_path);

    DFS dfs2 = new DFS(graph, 11, 15); // moves down the left branch
    ArrayList<Integer> expected_path2 = new ArrayList<Integer>();
    expected_path2.add(11);
    expected_path2.add(12);
    expected_path2.add(15);

    ArrayList<Integer> actual_path2 = dfs2.getPath();
    assertEquals(expected_path2, actual_path2);
  }

  @Test
  public void testToStringDFS() {
    DFS dfs1 = new DFS(graph, 11, 13);
    String expectedString =
        "Start at node 11, then go to node 14, then go to node 17, then go to node 18, then go to node 16, then go to node 13. You have reached your destination.";
    String actualString = dfs1.toString();
    assertEquals(expectedString, actualString);

    DFS dfs2 = new DFS(graph, 11, 11);
    expectedString = "Wow! You're already there! Good Job!";
    actualString = dfs2.toString();
    assertEquals(expectedString, actualString);
  }
}
