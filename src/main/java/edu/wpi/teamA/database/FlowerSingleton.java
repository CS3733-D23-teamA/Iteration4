package edu.wpi.teamA.database;

import edu.wpi.teamA.database.ORMclasses.FlowerEntity;

public enum FlowerSingleton {
  INSTANCE;
  // example of how attributes are added to the Enum
  FlowerEntity f;

  public FlowerEntity getValue() {
    return f;
  }

  public void setValue(FlowerEntity f1) {
    this.f = f1;
  }
}
