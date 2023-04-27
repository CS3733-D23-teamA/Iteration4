package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class Meal {
  @Getter @Setter int id;
  @Getter @Setter String name;
  @Getter @Setter String room;
  @Getter @Setter Date date;
  @Getter @Setter int time;
  @Getter @Setter String items;
  @Getter @Setter String comment;
  @Getter @Setter String employee;
  @Getter @Setter String status;
  @Getter @Setter String creator;

  public Meal(
      int id,
      String name,
      String room,
      Date date,
      int time,
      String items,
      String comment,
      String employee,
      String status,
      String creator) {
    this.id = id;
    this.name = name;
    this.room = room;
    this.date = date;
    this.time = time;
    this.items = items;
    this.comment = comment;
    this.employee = employee;
    this.status = status;
    this.creator = creator;
  }
}
