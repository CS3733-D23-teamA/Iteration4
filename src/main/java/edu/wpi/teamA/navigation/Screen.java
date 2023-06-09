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
  CR_REQUEST("views/CRRequest.fxml"),
  CR_REQUEST_EDIT("views/CRRequestEdit.fxml"),
  FURNITURE_REQUEST("views/FurnitureRequest.fxml"),
  FURNITURE_REQUEST_EDIT("views/FurnitureEdit.fxml"),
  MEAL_REQUEST("views/MealRequest.fxml"),
  MEAL_REQUEST_EDIT("views/MealEdit.fxml"),
  ACCOUNT_SETTINGS("views/AccountSettings.fxml"),
  EMPLOYEE_SETTINGS("views/EmployeeSettings.fxml"),
  CHANGE_PASSWORD_SETTINGS("views/ChangePassword.fxml"),
  UPDATE_NAME_SETTINGS("views/UpdateName.fxml"),
  SIGNAGE_ADMIN("views/SignageAdmin.fxml"),
  DATA_SETTINGS("views/DataSettings.fxml"),
  USER_SETTINGS("views/UserSettings.fxml"),
  LOCATION_SETTINGS("views/LocationSettings.fxml"),
  IMPORT_EXPORT_SETTINGS("views/ImportExport.fxml"),
  ALERT_SETTINGS("views/AlertSettings.fxml"),
  ADD_USER("views/AddUser.fxml"),
  SCREEN_SAVER("views/ScreenSaver.fxml"),
  ABOUT_HOME("views/AboutPage.fxml"),
  CREDIT_HOME("views/CreditPage.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
