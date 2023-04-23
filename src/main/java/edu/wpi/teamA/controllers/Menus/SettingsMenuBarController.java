package edu.wpi.teamA.controllers.Menus;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SettingsMenuBarController {
  @FXML private Button exitApplicationButton;
  @FXML private Button logoutButton;
  @FXML private Button accountSettingsButton;
  @FXML private Button employeeSettingsButton;

  @FXML
  public void initialize() {
    if (!AccountSingleton.INSTANCE1.getValue().getIsAdmin()) {
      employeeSettingsButton.setVisible(false);
      employeeSettingsButton.setManaged(false);
    }
  }

  public void openEmployeeSettings() {
    Navigation.navigate(Screen.EMPLOYEE_SETTINGS);
  }

  public void openAccountSettings() {
    Navigation.navigate(Screen.ACCOUNT);
  }

  public void logout() {
    User loggedOut = new User(2, "N", "N", "N", "N");
    AccountSingleton.INSTANCE1.setValue(loggedOut);
    Navigation.navigate(Screen.LOGIN);
  }

  public void exitApp() {
    App.getPrimaryStage().hide();
  }
}
