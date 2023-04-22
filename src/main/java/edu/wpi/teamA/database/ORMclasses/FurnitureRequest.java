package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class FurnitureRequest {
  @Getter @Setter int id;
  @Getter @Setter String name;
  @Getter @Setter String room;
  @Getter @Setter Date date;
  @Getter @Setter int time;
  @Getter @Setter String furnitureType;
  @Getter @Setter String comment;
  @Getter @Setter String employee;
  @Getter @Setter String status;

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
    this.employee = employee;
    this.status = status;
  }
}
