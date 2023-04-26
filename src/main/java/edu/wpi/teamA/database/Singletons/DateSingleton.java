package edu.wpi.teamA.database.Singletons;

import java.sql.Date;

public enum DateSingleton {
  INSTANCE;
  // example of how attributes are added to the Enum
  Date d;

  public Date getValue() {
    return d;
  }

  public void setValue(Date d1) {
    this.d = d1;
  }
}
