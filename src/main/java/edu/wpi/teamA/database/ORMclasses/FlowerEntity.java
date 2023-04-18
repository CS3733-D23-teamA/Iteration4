package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class FlowerEntity {
  @Getter @Setter private int id;
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private Date date;
  @Getter @Setter private int time;
  @Getter @Setter private String flowerType;
  @Getter @Setter private String comment;
  @Getter @Setter private String employee;
  @Getter @Setter private String status;

  public FlowerEntity(
      int id, String a, String b, Date c, int d, String e, String f, String g, String h) {
    this.id = id;
    this.name = a;
    this.room = b;
    this.date = c;
    this.time = d;
    this.flowerType = e;
    this.comment = f;
    this.employee = g;
    this.status = h;
  }

  public FlowerEntity() {
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
