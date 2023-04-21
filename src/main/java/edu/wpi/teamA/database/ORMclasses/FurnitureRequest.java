package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class FurnitureRequest {
  @Getter @Setter private int id;
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private Date date;
  @Getter @Setter private int time;
  @Getter @Setter private String furnitureType;
  @Getter @Setter private String comment;
  @Getter @Setter private String employee;
  @Getter @Setter private String status;

  public FurnitureRequest(
      int id,
      String name,
      String room,
      Date date,
      int time,
      String furnitureType,
      String comment,
      String employee,
      String status) {
    this.id = id;
    this.name = name;
    this.room = room;
    this.date = date;
    this.time = time;
    this.furnitureType = furnitureType;
    this.comment = comment;
    this.status = status;
  }
}
