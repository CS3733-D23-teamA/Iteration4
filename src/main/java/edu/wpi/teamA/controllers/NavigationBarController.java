package edu.wpi.teamA.controllers;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;

public class NavigationBarController {

  @FXML private ImageView homeIcon;
  @FXML private Button serviceRequestsButton;
  @FXML private Button pathfindingButton;
  @FXML private Button signageButton;
  @FXML private Button mapEditorButton;
  @FXML private Button movesButton;

  @FXML private MenuButton profile;

  @FXML private Boolean isAdmin = AccountSingleton.isAdmin();

  @FXML
  public void initialize() {
    // diables moves and map editor for non-admin
    if (!isAdmin) {
      movesButton.setVisible(false);
      movesButton.setManaged(false);
      mapEditorButton.setVisible(false);
      mapEditorButton.setManaged(false);
    }

    // get first and last initial for user and set label over profile avatar
    char a = AccountSingleton.INSTANCE.getValue().getFirstName().charAt(0);
    char b = AccountSingleton.INSTANCE.getValue().getLastName().charAt(0);
    profile.setText(new StringBuilder().append(a).append(b).toString());
    // sets isAdmin to true if admin value is set to 1 in singleton

    homeIcon.setImage(App.getHomeWhite());

    switch (App.getPrimaryStage().getTitle()) {
      case "SERVICE_REQUEST":
        serviceRequestsButton.setStyle("-fx-background-color: #F0C747");
        break;
      case "PATHFINDING":
        pathfindingButton.setStyle("-fx-background-color: #F0C747");
        break;
      case "SIGNAGE":
        signageButton.setStyle("-fx-background-color: #F0C747");
        break;
      case "SIGNAGE_ADMIN":
        signageButton.setStyle("-fx-background-color: #F0C747");
        break;
      case "MAP_EDITOR":
        mapEditorButton.setStyle("-fx-background-color: #F0C747");
        break;
      case "MOVES":
        movesButton.setStyle("-fx-background-color: #F0C747");
        break;
      case "HOME":
        homeIcon.setImage(App.getHomeYello());
        break;
      default:
        setPageIndicator();
        break;
    }
  }

  private void setPageIndicator() {
    if (App.getPrimaryStage().getTitle().contains("REQUEST")) {
      serviceRequestsButton.setStyle("-fx-background-color: #F0C747");
    } else if (App.getPrimaryStage().getTitle().contains("SETTINGS")) {
      profile.setStyle("-fx-border-width: 5; -fx-border-color: #F0C747; -fx-border-insets: -5;");
    } else if (App.getPrimaryStage().getTitle().contains("SETTINGS")) {
      homeIcon.setImage(App.getHomeYello());
    }
  }

  public void openServiceRequests() {
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  public void openMapEditor() {
    Navigation.navigate(Screen.MAP_EDITOR);
  }

  public void openPathfinding() {
    Navigation.navigate(Screen.PATHFINDING);
  }

  public void openSignage() {
    if (isAdmin) {
      Navigation.navigate(Screen.SIGNAGE_ADMIN);
    } else {
      Navigation.navigate(Screen.SIGNAGE);
    }
  }

  public void openMoves() {
    Navigation.navigate(Screen.MOVES);
  }

  public void exitApp() {
    App.getPrimaryStage().hide();
  }

  public void goHome() {
    Navigation.navigate(Screen.HOME);
  }

  public void homeHover() {
    if (!App.getPrimaryStage().getTitle().equals("HOME")) {
      homeIcon.setImage(App.getHomeYello());
    }
  }

  public void homeUnhover() {
    if (!App.getPrimaryStage().getTitle().equals("HOME")) {
      homeIcon.setImage(App.getHomeWhite());
    }
  }

  public void logout() {
    User loggedOut = new User(2, "N", "N", "N", "N", "N");
    AccountSingleton.INSTANCE.setValue(loggedOut);
    Navigation.navigate(Screen.LOGIN);
  }

  public void game() {
    Navigation.navigate(Screen.SCREEN_SAVER);
  }

  public void openSettings() {
    Navigation.navigate(Screen.ACCOUNT_SETTINGS);
  }
}
