package edu.wpi.teamA.database.ORMclasses;

import lombok.Getter;
import lombok.Setter;

public class User {

  @Getter @Setter private int adminYes;

  @Getter @Setter private String userName;

  @Getter @Setter private String password;

  @Getter @Setter private String firstName;

  @Getter @Setter private String lastName;

  public User(int adminYes, String userName, String password, String firstName, String lastName) {
    this.adminYes = adminYes;
    this.userName = userName;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public boolean equals(User user2) { // .equals override to compare two users
    if (this.adminYes == user2.adminYes) {
      if (this.userName == user2.userName) {
        if (this.password == user2.password) {
          if (this.firstName == user2.firstName) {
            if (this.lastName == user2.lastName) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
}
