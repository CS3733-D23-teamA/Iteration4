package edu.wpi.teamA.controllers.Menus;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SettingsMenuBarController {
  @FXML private Button exitApplicationButton;
  @FXML private Button logoutButton;
  @FXML private Button accountSettingsButton;
  @FXML private Button employeeSettingsButton;
  @FXML private VBox AccountSettingsDropdown;

  @FXML
  public void initialize() {
    if (!AccountSingleton.isAdmin()) {
      employeeSettingsButton.setVisible(false);
      employeeSettingsButton.setManaged(false);
    }
    AccountSettingsDropdown.setVisible(false);
    AccountSettingsDropdown.setManaged(false);
  }

  public void openEmployeeSettings() {
    Navigation.navigate(Screen.EMPLOYEE_SETTINGS);
  }

  public void openImportExport() {
    Navigation.navigate(Screen.IMPORT_EXPORT);
  }

  public void openAccountSettings() {
    AccountSettingsDropdown.setVisible(!AccountSettingsDropdown.isVisible());
    AccountSettingsDropdown.setManaged(!AccountSettingsDropdown.isManaged());
  }

  public void openChangePassword() {
    Navigation.navigate(Screen.CHANGEPASSWORD);
  }

  public void openUpdateName() {
    Navigation.navigate(Screen.UPDATENAME);
  }

  public void logout() {
    User loggedOut = new User(2, "N", "N", "N", "N");
    AccountSingleton.INSTANCE.setValue(loggedOut);
    Navigation.navigate(Screen.LOGIN);
  }

  public void exitApp() {
    App.getPrimaryStage().hide();
  }
}
