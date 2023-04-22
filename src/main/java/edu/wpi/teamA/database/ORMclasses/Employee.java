package edu.wpi.teamA.database.ORMclasses;

import lombok.Getter;
import lombok.Setter;

public class Employee {
  @Getter @Setter String name;
  @Getter @Setter String username;
  @Getter @Setter String password;

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
