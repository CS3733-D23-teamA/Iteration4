package edu.wpi.teamA.database;

import edu.wpi.teamA.database.ORMclasses.User;

public enum AccountSingleton {
  INSTANCE1;
  // example of how attributes are added to the Enum
  // could we make seperate instances for admin/non-admins?
  User user1;

  public User getValue() {
    return user1;
  }

  public void setValue(User user1) {
    this.user1 = user1;
  }
}
