package edu.wpi.teamA.controllers.Menus;

import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class ServiceRequestMenuBarController {
  @FXML private MFXButton serviceRequestsButton;

  @FXML
  public void initialize() {
    if (!AccountSingleton.INSTANCE1.getValue().getIsAdmin()) {
      serviceRequestsButton.setText("My Service Requests");
      // handle service request admin functionality in SR controller
    }
  }

  public void openFlower() {
    Navigation.navigate(Screen.FLOWER_REQUEST);
  }

  public void openRoom() {
    Navigation.navigate(Screen.CONFERENCE_REQUEST);
  }

  public void openFurniture() {
    Navigation.navigate(Screen.FURNITURE_REQUEST);
  }

  public void openMeal() {
    Navigation.navigate(Screen.MEAL_REQUEST);
  }

  public void openServiceRequests() {
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }
}
