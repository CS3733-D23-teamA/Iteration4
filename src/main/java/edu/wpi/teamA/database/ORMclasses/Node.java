package edu.wpi.teamA.database.ORMclasses;

import lombok.Getter;
import lombok.Setter;

public class Node {
  @Getter @Setter Integer nodeID;

  @Getter @Setter Integer xcoord;

  @Getter @Setter Integer ycoord;
  @Getter @Setter String floor;
  @Getter @Setter String building;

  public Node(Integer nodeID, Integer xcoord, Integer ycoord, String floor, String building) {
    this.nodeID = nodeID;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
  }
}
