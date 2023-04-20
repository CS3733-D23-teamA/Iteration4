package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class Flower {
  @Getter @Setter private int id;
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private Date date;
  @Getter @Setter private int time;
  @Getter @Setter private String flowerType;
  @Getter @Setter private String comment;
  @Getter @Setter private String employee;
  @Getter @Setter private String status;

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
