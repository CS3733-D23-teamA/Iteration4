package edu.wpi.teamA.pathfinding;

import static edu.wpi.teamA.pathfinding.PathfindingAlgorithm.BFS;
import static edu.wpi.teamA.pathfinding.PathfindingAlgorithm.DFS;
import static edu.wpi.teamA.pathfinding.PathfindingAlgorithm.ASTAR;

public class PathfindingSingleton {

    private PathfindingAlgorithm algo;
    private static PathfindingSingleton INSTANCE = new PathfindingSingleton();

    private PathfindingSingleton() {
        algo = ASTAR;
    }

    public void setAlgo(PathfindingAlgorithm algo) {
        this.algo = algo;
    }

    public PathfindingAlgorithm getAlgo() {
        return this.algo;
    }
}
