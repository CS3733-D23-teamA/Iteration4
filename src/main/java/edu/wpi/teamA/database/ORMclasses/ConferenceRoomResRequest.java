package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class ConferenceRoomResRequest {
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private Date date;
  @Getter @Setter private int startTime;
  @Getter @Setter private int endTime;
  @Getter @Setter private String comment;
  @Getter @Setter private String status;

  public ConferenceRoomResRequest(
      String a, String b, Date c, int d, int e, String f, String status) {
    this.name = a;
    this.room = b;
    this.date = c;
    this.startTime = d;
    this.endTime = e;
    this.comment = f;
    this.status = "new";
  }

  public ConferenceRoomResRequest() {
    this.name = null;
    this.room = null;
    this.date = null;
    this.startTime = 0;
    this.endTime = 0;
    this.comment = null;
    this.status = "new";
  }
}
