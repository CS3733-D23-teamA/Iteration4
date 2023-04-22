package edu.wpi.teamA.database.ORMclasses;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

public class Move {
  @Getter @Setter Integer nodeID;

  @Getter @Setter String longName;

  @Getter @Setter LocalDate date;

  public Move(Integer nodeID, String longName, LocalDate date) {
    this.nodeID = nodeID;
    this.longName = longName;
    this.date = date;
  }
}
