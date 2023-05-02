package edu.wpi.teamA.entities;

import java.util.ArrayList;

public class LevelEntity {
  private ArrayList<Level> levelOrder;

  int levelIndex = 0;

  public LevelEntity() {
    setOrder();
  }

  /** Level entity sets a default level order corresponding to the floor maps */
  void setOrder() {
    levelOrder = new ArrayList<Level>();
    levelOrder.add(Level.LOWERLEVELL1);
    levelOrder.add(Level.LOWERLEVELL2);
    levelOrder.add(Level.LEVEL1);
    levelOrder.add(Level.LEVEL2);
    levelOrder.add(Level.LEVEL3);
  }

  /**
   * Over written setOrder() w newOrder param sets a new order for all levels depending on levels
   * traversed
   *
   * @param newOrder list of Strings of levels representing levels traversed
   */
  void setOrder(ArrayList<String> newOrder) {
    levelIndex = 0;
    levelOrder = new ArrayList<Level>();
    for (String level : newOrder) {
      switch (level) {
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

  /**
   * gets a level from order based off index in set order
   *
   * @param index int of index in order
   * @returns level object from order
   */
  Level getOrderedLevel(int index) {
    return levelOrder.get(index);
  }

  /**
   * helper method
   *
   * @returns number of all levels
   */
  int getTotalLevels() {
    return levelOrder.size();
  }

  /**
   * uses global index to determine and return level with next index + updates index to increase as
   * it gets called
   *
   * @returns level with next index in order, or current level if at the end of levels
   */
  Level getNext() {
    if (levelIndex < getTotalLevels() - 1) {
      levelIndex++;
    }
    return getOrderedLevel(levelIndex);
  }

  /**
   * uses global index to determine and return level with previous index + updates index to decrease
   * as it gets called
   *
   * @returns level with prev index in order, or current level if at the beginning of levels
   */
  Level getPrev() {
    if (levelIndex > 0) {
      levelIndex--;
    }
    return getOrderedLevel(levelIndex);
  }

  Boolean hasNext() {
    return levelIndex < levelOrder.size() - 1;
  }

  Boolean hasPrev() {
    return levelIndex > 0;
  }
}
