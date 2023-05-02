package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class SignageComponent {
  @Getter @Setter String locationName;
  @Getter @Setter String direction;
  @Getter @Setter Date date;
  @Getter @Setter int screen;
  @Getter @Setter int signageID;

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
