package edu.wpi.teamA.controllers.Navigation.singleton;

import edu.wpi.teamA.database.ORMclasses.Flower;

public class flowerHolder {
  private Flower f;
  private static final flowerHolder INSTANCE = new flowerHolder();

  private flowerHolder() {}

  public static flowerHolder getInstance() {
    return INSTANCE;
  }

  public void setUser(Flower flower) {
    this.f = flower;
  }

  public Flower getUser() {
    return this.f;
  }
}
