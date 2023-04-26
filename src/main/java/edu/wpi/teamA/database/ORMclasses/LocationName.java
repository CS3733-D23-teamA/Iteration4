package edu.wpi.teamA.database.ORMclasses;

import lombok.Getter;
import lombok.Setter;

public class LocationName {
  @Getter @Setter String longName;
  @Getter @Setter String shortName;

  @Getter @Setter String nodeType;

  public LocationName(String longName, String shortName, String nodeType) {
    this.longName = longName;
    this.shortName = shortName;
    this.nodeType = nodeType;
  }
}
