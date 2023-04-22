package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class Flower {
  @Getter @Setter int id;
  @Getter @Setter String name;
  @Getter @Setter String room;
  @Getter @Setter Date date;
  @Getter @Setter int time;
  @Getter @Setter String flowerType;
  @Getter @Setter String comment;
  @Getter @Setter String employee;
  @Getter @Setter String status;

  public Flower(
      int id,
      String name,
      String room,
      Date date,
      int time,
      String flowerType,
      String comment,
      String employee,
      String status) {
    this.id = id;
    this.name = name;
    this.room = room;
    this.date = date;
    this.time = time;
    this.flowerType = flowerType;
    this.comment = comment;
    this.employee = employee;
    this.status = status;
  }

  public Flower() {
    this.id = -1;
    this.name = null;
    this.room = null;
    this.date = null;
    this.time = 0;
    this.flowerType = null;
    this.comment = null;
    this.status = null;
  }
}
