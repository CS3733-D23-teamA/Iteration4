package edu.wpi.teamA.database.Singletons;

import edu.wpi.teamA.database.ORMclasses.User;

public enum AccountSingleton {
  INSTANCE;
  User user;

  public User getValue() {
    return user;
  }

  public void setValue(User user1) {
    this.user = user1;
  }
}
