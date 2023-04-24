package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class SignageComponent {
  @Getter @Setter private String locationName;
  @Getter @Setter private String direction;
  @Getter @Setter private Date date;
  @Getter @Setter private int screen;
  @Getter @Setter private String signageID;

  public SignageComponent(
      String locationName, String direction, Date date, int screen, String signageID) {
    this.locationName = locationName;
    this.direction = direction;
    this.date = date;
    this.screen = screen;
    this.signageID = locationName + date.toString();
  }

  public SignageComponent() {
    this.locationName = null;
    this.direction = null;
    this.date = null;
    this.screen = 0;
    this.signageID = null;
  }
}
