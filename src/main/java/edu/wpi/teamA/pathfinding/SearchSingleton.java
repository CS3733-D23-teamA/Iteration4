package edu.wpi.teamA.pathfinding;

import static edu.wpi.teamA.pathfinding.SearchAlgorithm.ASTAR;

import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.entities.MapEntity;
import java.util.ArrayList;

public class SearchSingleton {
  private static SearchAlgorithm searchAlgorithm;
  private static Search searchEntity;
  private static SearchSingleton INSTANCE = new SearchSingleton();

  /** SearchSingleton constructor sets search algorithm to ASTAR by default */
  private SearchSingleton() {
    searchAlgorithm = ASTAR;
  }

  /**
   * setSearchAlgorithm to set using a SearchAlgorithm
   *
   * @param algorithm takes a SearchAlgorithm
   */
  public static void setSearchAlgorithm(SearchAlgorithm algorithm) {
    searchAlgorithm = algorithm;
  }

  /**
   * Overwritten setSearchAlgorithm to set using a String
   *
   * @param algorithm takes a String to set to algorithm
   */
  public static void setSearchAlgorithm(String algorithm) {
    switch (algorithm) {
      case "Depth-First Search":
        setSearchAlgorithm(SearchAlgorithm.DFS);
        break;
      case "Breadth-First Search":
        setSearchAlgorithm(SearchAlgorithm.BFS);
        break;
      case "A*":
        setSearchAlgorithm(SearchAlgorithm.ASTAR);
        break;
    }
  }

  /** Returns the search algorithm as a SearchAlgorithm object */
  public static SearchAlgorithm getSearchAlgorithm() {
    return searchAlgorithm;
  }

  /**
   * Creates the search depending on set algorithm and given starting and ending point access level:
   * 0 = none, 1 = no elevator, 2 = no stairs
   *
   * @param startID takes starting Node ID as an int
   * @param endID takes ending Node ID as an int
   */
  public static void createSearch(int startID, int endID, boolean accessiblity) {
    if (getSearchAlgorithm() == SearchAlgorithm.BFS) {
      searchEntity = new BFS(startID, endID, accessiblity);
    } else if (getSearchAlgorithm() == SearchAlgorithm.DFS) {
      searchEntity = new DFS(startID, endID, accessiblity);
    } else {
      searchEntity = new AStar(startID, endID, accessiblity);
    }
  }

  /**
   * Returns corresponding GraphNode for a given Node in searchEntity
   *
   * @param nodeID takes a Node ID as an int
   */
  public static GraphNode getGraphNode(int nodeID) {
    return searchEntity.getGraphNode(nodeID);
  }

  /** Returns path from search entity */
  public static ArrayList<Integer> getPath() {
    return searchEntity.getPath();
  }

  // TODO change falses
  /** Returns path as a string of directions from set path */
  public static String pathString(boolean second) {
    ArrayList<Integer> nodeIDs = getPath();

    String stringPath = "Wow! You're already there! Good Job!";
    if (nodeIDs.size() > 1) {
      MapEntity mapEd = new MapEntity();
      LocationName locName = mapEd.getLocationName(nodeIDs.get(0), second);
      stringPath = "Start at " + locName.getLongName();

      for (int i = 1; i < nodeIDs.size(); i++) {
        locName = mapEd.getLocationName(nodeIDs.get(i), second);
        stringPath += ", then go to " + locName.getLongName();
      }
      stringPath += ". You have reached your destination.";
    }

    return stringPath;
  }
}
