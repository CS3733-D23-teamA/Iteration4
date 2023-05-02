package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class SignageComponent {
  @Getter @Setter private String locationName;
  @Getter @Setter private String direction;
  @Getter @Setter private Date date;
  @Getter @Setter private int screen;
  @Getter @Setter private int signageID;

  public SignageComponent(
      String locationName, String direction, Date date, int screen, int signageID) {
    this.locationName = locationName;
    this.direction = direction;
    this.date = date;
    this.screen = screen;
    this.signageID = signageID;
  }

  public SignageComponent() {
    this.locationName = null;
    this.direction = null;
    this.date = null;
    this.screen = -1;
    this.signageID = -1;
  }
}
