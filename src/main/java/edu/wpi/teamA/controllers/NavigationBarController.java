package edu.wpi.teamA.controllers;

import edu.wpi.teamA.App;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class NavigationBarController {

  //  @FXML private MenuButton serviceRequestsButton;
  //  @FXML private MenuItem flowerMenu;
  //  @FXML private MenuItem roomMenu;
  //  @FXML private MenuItem otherMenu;
  //  @FXML private MenuItem myRequestsMenu;

  // @FXML private MFXButton adminButton;
  @FXML private ImageView bwhLogo;

  @FXML
  public void initialize() {}

  // Are we running everything through intitalaze or onAction methods?
  public void openServiceRequests() {
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  public void openFlowerRequest() {
    Navigation.navigate(Screen.FLOWER_REQUEST);
  }

  public void openRoomRequest() {
    Navigation.navigate(Screen.CONFERENCE_REQUEST);
  }

  public void openFurnitureRequest() {
    Navigation.navigate((Screen.FURNITURE));
  }

  public void openMyRequests() {
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  public void openMapEditor() {
    Navigation.navigate(Screen.MAP_EDITOR);
  }

  public void openPathfinding() {
    Navigation.navigate(Screen.PATHFINDING);
  }

  public void openSignage() {
    Navigation.navigate(Screen.SIGNAGE);
  }

  public void exitApp() {
    App.getPrimaryStage().hide();
  }

  public void goHome() {
    Navigation.navigate(Screen.HOME);
  }
}
