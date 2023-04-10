package edu.wpi.teamA.controllers;

import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class NavigationBarController {

  @FXML private MFXButton serviceRequestsButton;
  @FXML private MFXButton mapEditorButton;

  @FXML private MFXButton signageButton;
  @FXML private MFXButton pathfindingButton;
  @FXML private MenuItem exitMenu;
  @FXML private ImageView bwhLogo;
  @FXML private Circle profile;

  @FXML
  public void initialize() {
    serviceRequestsButton.setOnAction(event -> Navigation.navigate(Screen.SERVICE_REQUEST));
    mapEditorButton.setOnAction(event -> Navigation.navigate(Screen.MAP));
    signageButton.setOnAction(event -> Navigation.navigate(Screen.SIGNAGE));
    pathfindingButton.setOnAction(event -> Navigation.navigate(Screen.PATHFINDING));
    // exitMenu.setOnAction(event -> App.getPrimaryStage().hide());
    bwhLogo.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    // profile.setOnMouseClicked();
  }
}
