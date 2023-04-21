package edu.wpi.teamA.pathfinding;

import static edu.wpi.teamA.pathfinding.PathfindingAlgorithm.ASTAR;

public class PathfindingSingleton {

  private static PathfindingAlgorithm algo;
  private static PathfindingSingleton INSTANCE = new PathfindingSingleton();

  private PathfindingSingleton() {
    algo = ASTAR;
  }

  public static void setAlgo(PathfindingAlgorithm algorithm) {
    algo = algorithm;
  }

  public static PathfindingAlgorithm getAlgo() {
    return algo;
  }
}
