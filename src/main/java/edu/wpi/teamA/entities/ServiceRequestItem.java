package edu.wpi.teamA.entities;

import lombok.Getter;
import lombok.Setter;

public class ServiceRequestItem {
  @Getter @Setter private String item;
  @Getter @Setter private int quantity;

  public ServiceRequestItem(String item, int quantity) {
    this.item = item;
    this.quantity = quantity;
  }
}
