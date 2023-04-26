package edu.wpi.teamA.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  LOGIN("views/LoginPage.fxml"),
  HOME("views/Home.fxml"),

  SERVICE_REQUEST("views/ServiceRequest.fxml"),
  MAP("views/Map.fxml"),
  SIGNAGE("views/Signage.fxml"),
  PATHFINDING("views/Pathfinding.fxml"),
  HEADER("views/NavigationBar.fxml"),
  FLOWER_REQUEST("views/FlowerRequest.fxml"),
  CONFERENCE_REQUEST("views/ConferenceRequest.fxml"),
  MAP_EDITOR("views/MapEditor.fxml"),
  FURNITURE_REQUEST("views/FurnitureRequest.fxml"),

  MEAL_REQUEST("views/MealRequest.fxml"),
  MOVES("views/Moves.fxml"),
  ACCOUNT("views/AccountSettings.fxml"),
  FLOWER_EDIT("views/FlowerEdit.fxml"),
  ROOM_EDIT("views/RoomEdit.fxml"),
  MEAL_EDIT("views/MealEdit.fxml"),
  FURNITURE_EDIT("views/FurnitureEdit.fxml"),
  EMPLOYEE_SETTINGS("views/EmployeeSettings.fxml"),
  SIGNAGE_ADMIN("views/SignageAdmin.fxml"),
  CALENDAR("views/CRCalendar.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
