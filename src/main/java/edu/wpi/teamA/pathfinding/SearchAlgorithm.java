package edu.wpi.teamA.pathfinding;

public enum SearchAlgorithm {
  ASTAR("A*"),
  BFS("Breadth-First Search"),
  DFS("Depth-First Search"),
  DIJKSTRA("Dijkstra");

  String algorithmName;

  SearchAlgorithm(String algoName) {
    this.algorithmName = algoName;
  }

  public String toString() {
    return algorithmName;
  }
}
