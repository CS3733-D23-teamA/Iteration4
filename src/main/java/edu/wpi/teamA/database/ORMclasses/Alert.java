package edu.wpi.teamA.database.ORMclasses;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

public class Alert {
  @Getter @Setter private int ticketNum;
  @Getter @Setter private String username;
  @Getter @Setter private LocalDate date;
  @Getter @Setter private String message;

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
