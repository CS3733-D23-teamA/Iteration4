package edu.wpi.teamA.entities;

import javafx.scene.image.Image;

public enum Level {
  LOWERLEVELL1("L1", new Image("edu/wpi/teamA/images/map-page/Level L1.png")),
  LOWERLEVELL2("L2", new Image("edu/wpi/teamA/images/map-page/Level L2.png")),
  LEVEL1("1", new Image("edu/wpi/teamA/images/map-page/Level 1.png")),
  LEVEL2("2", new Image("edu/wpi/teamA/images/map-page/Level 2.png")),
  LEVEL3("3", new Image("edu/wpi/teamA/images/map-page/Level 3.png"));

  private final String levelName;
  private final Image mapImage;

  Level(String name, Image mapImage) {
    this.levelName = name;
    this.mapImage = mapImage;
  }

  @Override
  public String toString() {
    return levelName;
  }

  public Image getMapImage() {
    return this.mapImage;
  }

  public String getName() {
    return this.levelName;
  }
}
