package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.Singletons.AccountSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AccountSettingsWelcomeController {
  @FXML private Label welcomeMessage;

  @FXML
  public void initialize() {
    String message =
        "Hello " + AccountSingleton.getFirstName() + " " + AccountSingleton.getLastName();
    welcomeMessage.setText(message);
  }
}
