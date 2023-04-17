package edu.wpi.teamA.database.ORMclasses;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class MealEntity {
    @Getter @Setter private String name;
    @Getter @Setter private String room;
    @Getter @Setter private Date date;
    @Getter @Setter private int time;
    @Getter @Setter private String mealType;
    @Getter @Setter private String comment;
    @Getter @Setter private String status;

    public MealEntity(String name, String room, Date date, int time, String mealType, String comment, String status) {
        this.name = name;
        this.room = room;
        this.date = date;
        this.time = time;
        this.mealType = mealType;
        this.comment = comment;
        this.status = status;
    }

    public MealEntity() {
        this.name = null;
        this.room = null;
        this.date = null;
        this.time = 0;
        this.mealType = null;
        this.comment = null;
        this.status = null;
    }
}
