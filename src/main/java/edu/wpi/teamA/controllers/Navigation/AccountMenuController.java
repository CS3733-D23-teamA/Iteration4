package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AccountMenuController {
  @FXML private Button exitButton;
  @FXML private Button logoutButton;
  @FXML private Button changePasswordButton;
  @FXML private Button updateNameButton;

  @FXML
  public void initialize() {}

  public void logout() {
    User loggedOut = new User(2, "N", "N", "N", "N");
    AccountSingleton.INSTANCE1.setValue(loggedOut);
    Navigation.navigate(Screen.LOGIN);
  }

  public void exitApp() {
    App.getPrimaryStage().hide();
  }
}
