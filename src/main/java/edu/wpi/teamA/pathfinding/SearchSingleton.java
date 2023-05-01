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

    if (nodeIDs == null) {
      return "There is no available path.";
    } else {

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
    }
    return stringPath;
  }

  /**
   * Returns an arraylist of simple directions indicating left, right, up, and down
   *
   * @param second whether to get the second locations of the date
   */
  public static ArrayList<String> simplePathArray(boolean second) {
    ArrayList<String> simplePath = new ArrayList<String>();
    ArrayList<Integer> pathIDs = getPath();
    MapEntity mapEd = new MapEntity();

    if (pathIDs == null) {
      return null;
    } else {

      // Setting some variables to represent the previous node
      int prevID = pathIDs.get(0);
      GraphNode prevNode = getGraphNode(prevID);

      int currID = pathIDs.get(1);
      GraphNode currNode = getGraphNode(currID);

      String currDir = getDirection(prevNode, currNode);

      for (int i = 2; i < pathIDs.size(); i++) {
        prevID = currID;
        prevNode = currNode;

        currID = pathIDs.get(i);
        currNode = getGraphNode(currID);

        String newDir = getDirection(prevNode, currNode);

        if (newDir.equals("u")) {
          simplePath.add("Go up to floor " + currNode.getFloor());
        } else if (newDir.equals("d")) {
          simplePath.add("Go down to floor " + currNode.getFloor());
        } else {

          String turn = getTurn(currDir, newDir);

          if (turn.equals("f")) {

          } else if (turn.equals("l")) {
            simplePath.add("Turn left at " + mapEd.getLocationName(currID, false).getShortName());
          } else if (turn.equals("r")) {
            simplePath.add("Turn right at " + mapEd.getLocationName(currID, false).getShortName());
          } else {
            simplePath.add("Something seems off?");
          }
        }
        currDir = newDir;
      }
    }
    return simplePath;
  }

  public static String simplePathString(boolean second) {
    ArrayList<String> pathArray = simplePathArray(second);

    String stringPath = "Wow! You're already there! Good Job!";

    if (pathArray == null) {
      return "There is no available path.";
    } else {

      if (pathArray.size() > 1) {
        stringPath = pathArray.get(0);

        for (int i = 1; i < pathArray.size(); i++) {
          stringPath += ". " + pathArray.get(i);
        }
        stringPath += ". You have reached your destination.";
      }
    }
    return stringPath;
  }

  private static String getDirection(GraphNode startNode, GraphNode endNode) {

    int x1 = startNode.getXcoord();
    int y1 = startNode.getYcoord();
    int x2 = endNode.getXcoord();
    int y2 = endNode.getYcoord();
    int x = x2 - x1;
    int y = y2 - y1;
    int absX = Math.abs(x);
    int absY = Math.abs(y);
    String startFloor = startNode.getFloor();
    String endFloor = endNode.getFloor();

    String dir = "";

    if (!startFloor.equals(endFloor)) {
      if (startFloor.equals("L1")) {
        dir = "u";
      } else if (startFloor.equals("L2")) {
        if (endFloor.equals("L1")) {
          dir = "d";
        } else {
          dir = "u";
        }
      } else if (startFloor.equals("1")) {
        if (endFloor.equals("L1") || endFloor.equals("L2")) {
          dir = "d";
        } else {
          dir = "u";
        }
      } else if (startFloor.equals("2")) {
        if (endFloor.equals("3")) {
          dir = "u";
        } else {
          dir = "d";
        }
      } else {
        dir = "d";
      }
    } else {

      if (absX > absY) {
        if (x > 0) {
          dir = "e";
        } else {
          dir = "w";
        }
      } else {
        if (y > 0) {
          dir = "s";
        } else {
          dir = "n";
        }
      }
    }
    return dir;
  }

  private static String getTurn(String prevDir, String newDir) {
    String turn = "";

    if (prevDir.equals(newDir)) {
      turn = "f";
    } else if (prevDir.equals("e")) {
      if (newDir.equals("n")) {
        turn = "l";
      } else if (newDir.equals("s")) {
        turn = "r";
      }
    } else if (prevDir.equals("w")) {
      if (newDir.equals("n")) {
        turn = "r";
      } else if (newDir.equals("s")) {
        turn = "l";
      }
    } else if (prevDir.equals("n")) {
      if (newDir.equals("w")) {
        turn = "l";
      } else if (newDir.equals("e")) {
        turn = "r";
      }
    } else if (prevDir.equals("s")) {
      if (newDir.equals("w")) {
        turn = "r";
      } else if (newDir.equals("e")) {
        turn = "l";
      }
    }

    return turn;
  }
}
