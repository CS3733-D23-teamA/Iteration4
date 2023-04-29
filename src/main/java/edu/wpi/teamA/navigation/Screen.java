package edu.wpi.teamA.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  LOGIN("views/LoginPage.fxml"),
  HOME("views/Home.fxml"),
  SERVICE_REQUEST("views/ServiceRequest.fxml"),
  SIGNAGE("views/Signage.fxml"),
  PATHFINDING("views/Pathfinding.fxml"),
  MAP_EDITOR("views/MapEditor.fxml"),
  MOVES("views/Moves.fxml"),
  FLOWER_REQUEST("views/FlowerRequest.fxml"),
  FLOWER_REQUEST_EDIT("views/FlowerEdit.fxml"),
  CR_REQUEST("views/CRResquest.fxml"),
  FURNITURE_REQUEST("views/FurnitureRequest.fxml"),
  FURNITURE_REQUEST_EDIT("views/FurnitureEdit.fxml"),
  MEAL_REQUEST("views/MealRequest.fxml"),
  MEAL_REQUEST_EDIT("views/MealEdit.fxml"),
  ACCOUNT_SETTINGS("views/AccountSettings.fxml"),
  SIGNAGE_ADMIN("views/SignageAdmin.fxml"),
  DATA_SETTINGS("views/DataSettings.fxml"),
  USER_SETTINGS("views/UserSettings.fxml"),
  LOCATION_SETTINGS("views/LocationSettings.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
