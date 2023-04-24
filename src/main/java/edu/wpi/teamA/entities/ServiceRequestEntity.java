package edu.wpi.teamA.entities;

import javafx.scene.control.TableView;

public class ServiceRequestEntity {
  public int convertTime(String time) {
    int num;
    String newString;
    int length = time.length();
    if (time.equals("00:00")) {
      return 0;
    } else if (length == 4) {
      newString = time.charAt(0) + time.substring(2);
    } else {
      newString = time.substring(0, 2) + time.substring(3);
    }
    num = Integer.parseInt(newString);
    return num;
  }

  public ServiceRequestItem tableContainsItem(TableView<ServiceRequestItem> items, String item) {
    for (ServiceRequestItem currentItem : items.getItems()) {
      if (currentItem.getItem().equals(item)) {
        return currentItem;
      }
    }
    return null;
  }
}
