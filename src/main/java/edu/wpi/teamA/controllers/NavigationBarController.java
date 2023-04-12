package edu.wpi.teamA.controllers;

import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class NavigationBarController {

  @FXML private MenuButton serviceRequestsButton;
  @FXML private MenuItem flowerMenu;
  @FXML private MenuItem roomMenu;
  @FXML private MenuItem furnitureMenu;
  @FXML private MenuItem myRequestsMenu;

  @FXML private MFXButton mapEditorButton;
  @FXML private MFXButton signageButton;
  @FXML private MFXButton pathfindingButton;
  // TODO @FXML private MenuItem exitMenu;
  @FXML private ImageView bwhLogo;
  @FXML private Circle profile;

  @FXML
  public void initialize() {
    serviceRequestsButton.setOnAction(event -> Navigation.navigate(Screen.SERVICE_REQUEST));
    flowerMenu.setOnAction(event -> Navigation.navigate(Screen.FLOWER_REQUEST));
    roomMenu.setOnAction(event -> Navigation.navigate(Screen.CONFERENCE_REQUEST));
    myRequestsMenu.setOnAction(event -> Navigation.navigate(Screen.SERVICE_REQUEST));
    furnitureMenu.setOnAction(event -> Navigation.navigate(Screen.FURNITURE_REQUEST));

    mapEditorButton.setOnAction(event -> Navigation.navigate(Screen.MAP));
    signageButton.setOnAction(event -> Navigation.navigate(Screen.SIGNAGE));
    pathfindingButton.setOnAction(event -> Navigation.navigate(Screen.PATHFINDING));
    // exitMenu.setOnAction(event -> App.getPrimaryStage().hide());
    bwhLogo.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    // profile.setOnMouseClicked();
  }

  // Are we running everything through intitalaze or onAction methods?
  public void openFlowerRequest() {}

  public void openRoomRequest() {}

  public void openOtherRequest() {}

  public void openMyRequests() {}
}
