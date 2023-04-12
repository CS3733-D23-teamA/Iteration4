package edu.wpi.teamA.database.ORMclasses;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

public class FurnitureRequest {

    @Getter
    @Setter
    private String name;
    @Getter @Setter private int room;
    @Getter @Setter private Date date;
    @Getter @Setter private int time;
    @Getter @Setter private String furnitureType;
    @Getter @Setter private String comment;
    @Getter @Setter private String status;

    public FurnitureRequest(String a, int b, Date c, int d, String e, String f, String g) {
        this.name = a;
        this.room = b;
        this.date = c;
        this.time = d;
        this.furnitureType = e;
        this.comment = f;
        this.status = g;
    }
}
