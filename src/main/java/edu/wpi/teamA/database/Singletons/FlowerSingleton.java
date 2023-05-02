package edu.wpi.teamA.database.Singletons;

import edu.wpi.teamA.database.ORMclasses.Flower;

public enum FlowerSingleton implements IRequestSingleton<Flower> {
  INSTANCE;
  // example of how attributes are added to the Enum
  Flower f;

  public Flower getValue() {
    return f;
  }

  public void setValue(Flower f1) {
    this.f = f1;
  }

  public String getStatus() {
    return f.getStatus();
  }
}
