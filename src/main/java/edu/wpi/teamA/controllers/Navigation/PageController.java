package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class PageController {
  @FXML MFXButton backButton = new MFXButton();

  @FXML
  public void initialize() {}

  public void back() {
    Navigation.navigate(Screen.HOME);
  }
}
