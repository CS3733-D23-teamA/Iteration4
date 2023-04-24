package edu.wpi.teamA.entities;

import java.util.ArrayList;

public class LevelEntity {
  private ArrayList<Level> levelOrder;

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
}
