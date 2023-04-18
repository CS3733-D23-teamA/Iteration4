package edu.wpi.teamA.controllers;

import edu.wpi.teamA.App;
import edu.wpi.teamA.controllers.Navigation.AccountSingleton;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class NavigationBarController {

  //  @FXML private MenuButton serviceRequestsButton;
  //  @FXML private MenuItem flowerMenu;
  //  @FXML private MenuItem roomMenu;
  //  @FXML private MenuItem otherMenu;
  //  @FXML private MenuItem myRequestsMenu;

  // @FXML private MFXButton adminButton;
  @FXML private ImageView bwhLogo;
  @FXML private Label userNameLabel;

  @FXML
  public void initialize() {
    StringBuilder sb = new StringBuilder();
    char a = AccountSingleton.INSTANCE1.getValue().getFirstName().charAt(0);
    char b = AccountSingleton.INSTANCE1.getValue().getLastName().charAt(0);
    sb.append(a).append(b);
    userNameLabel.setText(sb.toString());
  }

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

  public void openMealRequest() {
    Navigation.navigate(Screen.MEAL_REQUEST);
  }

  public void goHome() {
    Navigation.navigate(Screen.HOME);
  }

  public void logout() {
    User loggedOut = new User(2, "N", "N", "N", "N");
    AccountSingleton.INSTANCE1.setValue(loggedOut);
    Navigation.navigate(Screen.LOGIN);
  }

  public void openAccount() {
    Navigation.navigate(Screen.ACCOUNT);
  }
}
