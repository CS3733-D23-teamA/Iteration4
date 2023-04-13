package edu.wpi.teamA.controllers;

import edu.wpi.teamA.App;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class NavigationBarController {

  //  @FXML private MenuButton serviceRequestsButton;
  //  @FXML private MenuItem flowerMenu;
  //  @FXML private MenuItem roomMenu;
  //  @FXML private MenuItem otherMenu;
  //  @FXML private MenuItem myRequestsMenu;

  @FXML private MFXButton mapEditorButton;
  @FXML private MFXButton signageButton;
  @FXML private MFXButton pathfindingButton;
  @FXML private MenuItem exitMenu;
  @FXML private MenuItem furnitureMenu;
  @FXML private ImageView bwhLogo;
  @FXML private Circle profile;

  @FXML
  public void initialize() {
    //    serviceRequestsButton.setOnAction(event -> Navigation.navigate(Screen.SERVICE_REQUEST));
    //    flowerMenu.setOnAction(event -> Navigation.navigate(Screen.FLOWER_REQUEST));
    //    roomMenu.setOnAction(event -> Navigation.navigate(Screen.CONFERENCE_REQUEST));
    //    myRequestsMenu.setOnAction(event -> Navigation.navigate(Screen.SERVICE_REQUEST));
    //    // TODO otherMenu.setOnAction(event -> Navigation.navigate(Screen.SERVICE_REQUEST));

    mapEditorButton.setOnAction(event -> Navigation.navigate(Screen.MAP_EDITOR));
    signageButton.setOnAction(event -> Navigation.navigate(Screen.SIGNAGE));
    pathfindingButton.setOnAction(event -> Navigation.navigate(Screen.PATHFINDING));
    exitMenu.setOnAction(event -> App.getPrimaryStage().hide());
    bwhLogo.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    furnitureMenu.setOnAction(event -> Navigation.navigate(Screen.FURNITURE));
    // profile.setOnMouseClicked();
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

  public void openOtherRequest() {
    Navigation.navigate((Screen.FURNITURE));
  }

  public void openMyRequests() {
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }
}
