package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import javafx.fxml.FXML;

public class ServiceRequestController extends PageController {
  @FXML
  public void initialize() {}

  @FXML
  public void flowerPage() {
    Navigation.navigate(Screen.FLOWER_REQUEST);
  }

  @FXML
  public void conferencePage() {
    Navigation.navigate(Screen.CONFERENCE_REQUEST);
  }
}
