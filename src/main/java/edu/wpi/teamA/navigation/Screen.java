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
  FURNITURE("views/FurnitureRequest.fxml"),
  MOVES("views/Moves.fxml"),
  ACCOUNT("views/Account.fxml"),
  MEAL_REQUEST("views/MealRequest.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
