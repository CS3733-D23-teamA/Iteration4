package edu.wpi.teamA.database.ORMclasses;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class User {

  @Getter @Setter private int accessLevel;

  @Getter @Setter private String userName;

  @Getter @Setter private String password;

  @Getter @Setter private String firstName;

  @Getter @Setter private String lastName;

  @Getter @Setter private String userID;

  public User(
      int adminYes,
      String userName,
      String password,
      String firstName,
      String lastName,
      String userID) {
    this.accessLevel = adminYes;
    this.userName = userName;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userID = userID;
  }

  public Boolean isAdmin() {
    return accessLevel == 1;
  }

  public boolean equals(User user2) { // .equals override to compare two users
    if (this.accessLevel == user2.accessLevel) {
      if (Objects.equals(this.userName, user2.userName)) {
        if (Objects.equals(this.password, user2.password)) {
          if (Objects.equals(this.firstName, user2.firstName)) {
            if (Objects.equals(this.lastName, user2.lastName)) {
              if (Objects.equals(this.userID, user2.userID)) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }
}
