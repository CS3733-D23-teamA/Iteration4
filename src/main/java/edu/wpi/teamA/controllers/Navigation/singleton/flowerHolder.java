package edu.wpi.teamA.controllers.Navigation.singleton;

import edu.wpi.teamA.database.ORMclasses.FlowerEntity;

public class flowerHolder {
  private FlowerEntity f;
  private static final flowerHolder INSTANCE = new flowerHolder();

  private flowerHolder() {}

  public static flowerHolder getInstance() {
    return INSTANCE;
  }

  public void setUser(FlowerEntity flower) {
    this.f = flower;
  }

  public FlowerEntity getUser() {
    return this.f;
  }
}
