package edu.wpi.teamA.controllers;

import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import javafx.fxml.FXML;

public class AdminNavigationBarController extends NavigationBarController {

  @FXML
  public void initialize() {}

  public void openMoves() {
    Navigation.navigate(Screen.HOME);
  }
}
