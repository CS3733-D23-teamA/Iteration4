package edu.wpi.teamA.controllers;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class NavigationBarController {

  @FXML private Button serviceRequestsButton;
  @FXML private Button pathfindingButton;
  @FXML private Button signageButton;
  @FXML private Button mapEditorButton;
  @FXML private Button movesButton;
  @FXML private Button homeButton;
  @FXML private MenuButton profileButton;

  @FXML
  public void initialize() {
    // get first and last initial for user and set label over profile avatar
    char a = AccountSingleton.INSTANCE.getValue().getFirstName().charAt(0);
    char b = AccountSingleton.INSTANCE.getValue().getLastName().charAt(0);
    profileButton.setText(new StringBuilder().append(a).append(b).toString());
    // sets isAdmin to true if admin value is set to 1 in singleton

    switch (App.getPrimaryStage().getTitle()) {
      case "SERVICE_REQUEST":
        serviceRequestsButton.setStyle("-fx-background-color: #F0C747");
        System.out.println(App.getPrimaryStage().getTitle()); // set style mouse entered
        break;
      case "PATHFINDING":
        pathfindingButton.setStyle("-fx-background-color: #F0C747");
        System.out.println(App.getPrimaryStage().getTitle());

        break;
      case "SIGNAGE":
        signageButton.setStyle("-fx-background-color: #F0C747");
        System.out.println(App.getPrimaryStage().getTitle());
        break;
      case "MAP_EDITOR":
        mapEditorButton.setStyle("-fx-background-color: #F0C747");
        System.out.println(App.getPrimaryStage().getTitle());
        break;
      case "MOVES":
        movesButton.setStyle("-fx-background-color: #F0C747");
        System.out.println(App.getPrimaryStage().getTitle());
        break;
    }

    // diables moves and map editor for non-admin
    if (!AccountSingleton.isAdmin()) {
      movesButton.setVisible(false);
      movesButton.setManaged(false);
      mapEditorButton.setVisible(false);
      mapEditorButton.setManaged(false);
    }

    // trying to figure out how to get screen value for yellow buttons
    Platform.runLater(
        () -> {
          switch (App.getPrimaryStage().getTitle()) {
            case "SERVICE_REQUEST":
              serviceRequestsButton.setBackground(Background.fill(Color.web("F0C747")));
              break;
            case "PATHFINDING":
              pathfindingButton.setBackground(Background.fill(Color.web("F0C747")));
              break;
            case "SIGNAGE":
              signageButton.setBackground(Background.fill(Color.web("F0C747")));
              break;
            case "MAP_EDITOR":
              mapEditorButton.setBackground(Background.fill(Color.web("F0C747")));
              break;
            case "MOVES":
              movesButton.setBackground(Background.fill(Color.web("F0C747")));
              break;
          }
        });
  }

  // Are we running everything through initialize or onAction methods?
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
    if (AccountSingleton.isAdmin()) {
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

  public void logout() {
    User loggedOut = new User(2, "N", "N", "N", "N");
    AccountSingleton.INSTANCE.setValue(loggedOut);
    Navigation.navigate(Screen.LOGIN);
  }

  public void openAccount() {
    Navigation.navigate(Screen.ACCOUNTSETTINGS);
  }
}
