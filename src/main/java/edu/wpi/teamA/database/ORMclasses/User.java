package edu.wpi.teamA.database.ORMclasses;

import lombok.Getter;
import lombok.Setter;

public class User {

  @Getter @Setter int adminYes;
  @Getter Boolean isAdmin;

  @Getter @Setter String userName;

  @Getter @Setter String password;

  @Getter @Setter String firstName;

  @Getter @Setter String lastName;

  public User(int adminYes, String userName, String password, String firstName, String lastName) {
    this.adminYes = adminYes;
    this.isAdmin = adminYes == 1; // 0 = not admin /  1 = admin
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
