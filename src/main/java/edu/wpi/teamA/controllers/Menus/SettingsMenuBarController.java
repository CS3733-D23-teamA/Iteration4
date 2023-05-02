package edu.wpi.teamA.controllers.Menus;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class SettingsMenuBarController {
  @FXML private MFXButton alertSettingsButton;
  @FXML private MFXButton dataSettingsButton;
  @FXML private MFXButton userSettingsButton;
  @FXML private MFXButton locationSettingsButton;

  @FXML
  public void initialize() {
    if (!AccountSingleton.isAdmin()) {
      alertSettingsButton.setVisible(false);
      alertSettingsButton.setManaged(false);
      dataSettingsButton.setVisible(false);
      dataSettingsButton.setManaged(false);
      userSettingsButton.setVisible(false);
      userSettingsButton.setManaged(false);
      locationSettingsButton.setVisible(false);
      locationSettingsButton.setManaged(false);
    }
  }

  public void openAlertSettings() {
    Navigation.navigate(Screen.ALERT_SETTINGS);
  }

  public void openAccountSettings() {
    Navigation.navigate(Screen.ACCOUNT_SETTINGS);
  }

  public void openDataSettings() {
    Navigation.navigate(Screen.DATA_SETTINGS);
  }

  public void openUserSettings() {
    Navigation.navigate(Screen.USER_SETTINGS);
  }

  public void openLocationSettings() {
    Navigation.navigate(Screen.LOCATION_SETTINGS);
  }

  public void logout() {
    User loggedOut = new User(2, "N", "N", "N", "N", "N");
    AccountSingleton.INSTANCE.setValue(loggedOut);
    Navigation.navigate(Screen.LOGIN);
  }

  public void exitApp() {
    App.getPrimaryStage().hide();
  }
}
