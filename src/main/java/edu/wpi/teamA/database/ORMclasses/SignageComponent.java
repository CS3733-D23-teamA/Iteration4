package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import java.sql.Time;
import lombok.Getter;
import lombok.Setter;

public class SignageComponent {
  @Getter @Setter private String locationName;
  @Getter @Setter private String direction;
  @Getter @Setter private Date date;
  @Getter @Setter private Time time;

  public SignageComponent(String locationName, String direction, Date date, Time time) {
    this.locationName = locationName;
    this.direction = direction;
    this.date = date;
    this.time = time;
  }

  public SignageComponent() {
    this.locationName = null;
    this.direction = null;
    this.date = null;
    this.time = null;
  }
}
