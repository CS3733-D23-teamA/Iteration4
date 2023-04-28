package edu.wpi.teamA.database.ORMclasses;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

public class Alert {
    @Getter @Setter int ticketNum;
    @Getter @Setter String username;
    @Getter @Setter LocalDate date;
    @Getter @Setter String message;


    public Alert(int ticketNum, String username, LocalDate date, String message) {
        this.ticketNum = ticketNum;
        this.username = username;
        this.date = date;
        this.message = message;
    }

    public Alert() {
        this.ticketNum = 0;
        this.username = null;
        this.date = null;
        this.message = null;
    }
}
