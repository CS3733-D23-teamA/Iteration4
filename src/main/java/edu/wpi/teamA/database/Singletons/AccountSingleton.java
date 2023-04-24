package edu.wpi.teamA.database.Singletons;

import edu.wpi.teamA.database.ORMclasses.User;

public enum AccountSingleton {
  INSTANCE;
  static User user;

  public User getValue() {
    return user;
  }

  public void setValue(User user1) {
    this.user = user1;
  }

  public static Boolean isAdmin() {
    return user.isAdmin();
  }

  public static String getUsername() {
    return user.getUserName();
  }

  public static String getFirstName() {
    return user.getFirstName();
  }

  public static String getLastName() {
    return user.getLastName();
  }
}
