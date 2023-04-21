package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class ConferenceRoomResRequest {
  @Getter @Setter private int id;
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private Date date;
  @Getter @Setter private int startTime;
  @Getter @Setter private int endTime;
  @Getter @Setter private String comment;
  @Getter @Setter private String employee;
  @Getter @Setter private String status;

  public ConferenceRoomResRequest(
      int id,
      String name,
      String room,
      Date date,
      int startTime,
      int endTime,
      String comment,
      String employee,
      String status) {
    this.id = id;
    this.name = name;
    this.room = room;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.comment = comment;
    this.employee = employee;
    this.status = status;
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
