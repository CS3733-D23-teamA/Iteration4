package edu.wpi.teamA.pathfinding;

public enum SearchAlgorithm {
  ASTAR,
  BFS,
  DFS;

  public String toString() {

    switch (this) {
      case ASTAR:
        return "A*";
      case BFS:
        return "Breadth-First Search";
      case DFS:
        return "Depth-First Search";
    }

    return null;
  }
}
