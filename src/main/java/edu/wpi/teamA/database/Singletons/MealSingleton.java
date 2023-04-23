package edu.wpi.teamA.database.Singletons;

import edu.wpi.teamA.database.ORMclasses.Meal;

public enum MealSingleton implements IRequestSingleton<Meal> {
  INSTANCE;
  // example of how attributes are added to the Enum
  Meal m;

  @Override
  public Meal getValue() {
    return m;
  }

  @Override
  public void setValue(Meal obj) {
    this.m = obj;
  }
}
