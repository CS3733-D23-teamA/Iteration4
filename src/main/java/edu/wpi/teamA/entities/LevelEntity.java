package edu.wpi.teamA.entities;

import java.util.ArrayList;

public class LevelEntity {
  private ArrayList<Level> levelOrder;

  private int index = 0;

  public LevelEntity() {
    levelOrder = new ArrayList<Level>();
    levelOrder.add(Level.LOWERLEVELL1);
    levelOrder.add(Level.LOWERLEVELL2);
    levelOrder.add(Level.LEVEL1);
    levelOrder.add(Level.LEVEL2);
    levelOrder.add(Level.LEVEL3);
  }

  /**
   * gets a level from order based of index in set order
   *
   * @param index int of index in order
   */
  public Level getOrderedLevel(int index) {
    return levelOrder.get(index);
  }

  /** get level index */
  public int getIndex(Level level) {
    return levelOrder.indexOf(level);
  }

  public int getTotalLevels() {
    return levelOrder.size();
  }

  public void setOrder(ArrayList<String> newOrder) {
    index = 0;
    levelOrder = new ArrayList<Level>();
    for (int i = 0; i < newOrder.size(); i++) {
      switch (newOrder.get(i)) {
        case "L1":
          levelOrder.add(Level.LOWERLEVELL1);
          break;
        case "L2":
          levelOrder.add(Level.LOWERLEVELL2);
          break;
        case "1":
          levelOrder.add(Level.LEVEL1);
          break;
        case "2":
          levelOrder.add(Level.LEVEL2);
          break;
        case "3":
          levelOrder.add(Level.LEVEL3);
          break;
      }
    }
  }

  public Level nextIndex() {
    if (index < getTotalLevels() - 1) {
      index++;
    }
    return getOrderedLevel(index);
  }

  public Level lastIndex() {
    if (index > 0) {
      index--;
    }
    return getOrderedLevel(index);
  }
}
