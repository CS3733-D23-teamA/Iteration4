package edu.wpi.teamA.entities;

import edu.wpi.teamA.App;
import javafx.scene.image.Image;

public enum Level {
  LOWERLEVELL1("L1", App.getMapL1()),
  LOWERLEVELL2("L2", App.getMapL2()),
  LEVEL1("1", App.getMap1()),
  LEVEL2("2", App.getMap2()),
  LEVEL3("3", App.getMap3());

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
}
