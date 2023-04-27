package edu.wpi.teamA.controllers.Menus;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class SettingsMenuBarController {
  @FXML private MFXButton exitApplicationButton;
  @FXML private MFXButton logoutButton;
  @FXML private MFXButton accountSettingsButton;
  @FXML private MFXButton employeeSettingsButton;
  @FXML public VBox AccountSettingsDropdown;
  @FXML private MFXButton importExportButton;
  @FXML private MFXButton addUserButton;

  @FXML
  public void initialize() {
    if (!AccountSingleton.isAdmin()) {
      employeeSettingsButton.setVisible(false);
      employeeSettingsButton.setManaged(false);
      importExportButton.setVisible(false);
      importExportButton.setManaged(false);
      addUserButton.setVisible(false);
      addUserButton.setManaged(false);
    }

    AccountSettingsDropdown.setVisible(false);
    AccountSettingsDropdown.setManaged(false);
  }

  public void openEmployeeSettings() {
    Navigation.navigate(Screen.EMPLOYEE_SETTINGS);
  }

  public void openAccountSettings() {
    // Navigation.navigate(Screen.ACCOUNT_SETTINGS);
    AccountSettingsDropdown.setVisible(!AccountSettingsDropdown.isVisible());
    AccountSettingsDropdown.setManaged(!AccountSettingsDropdown.isManaged());
  }

  public void openChangePassword() {
    Navigation.navigate(Screen.CHANGE_PASSWORD_SETTINGS);
  }

  public void openUpdateName() {
    Navigation.navigate(Screen.UPDATE_NAME_SETTINGS);
  }

  public void openImportExport() {
    Navigation.navigate(Screen.IMPORT_EXPORT_SETTINGS);
  }

  public void openAddUser() {
    Navigation.navigate(Screen.ADD_USER);
  }
  public void openSetLocation(){
    Navigation.navigate(Screen.SET_LOCATION);
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
