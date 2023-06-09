package edu.wpi.teamA.database.ORMclasses;

import lombok.Getter;
import lombok.Setter;

public class Employee {
  @Getter @Setter private String name;
  @Getter @Setter private String username;
  @Getter @Setter private String password;

  public Employee(String name, String username, String password) {
    this.name = name;
    this.username = username;
    this.password = password;
  }

  public Employee() {
    this.name = null;
    this.username = null;
    this.password = null;
  }
}
