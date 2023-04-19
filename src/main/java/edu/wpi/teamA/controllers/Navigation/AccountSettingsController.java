package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.AccountSingleton;
import edu.wpi.teamA.database.DAOImps.UserDAOImp;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AccountSettingsController {
  @FXML private Button exitApplicationButton;
  @FXML private Button logoutButton;
  @FXML private Button changePasswordButton;
  @FXML private Button updateNameButton;

  @FXML private Button submitButton;

  @FXML private MFXPasswordField password1;
  @FXML private MFXPasswordField password2;
  @FXML private MFXPasswordField newPassword1;
  @FXML private MFXPasswordField newPassword2;
  UserDAOImp checker = new UserDAOImp();

  @FXML
  public void initialize() {}

  public void updatePassword() {
    String oldpassword1 = password1.getText();
    String oldpassword2 = password2.getText();
    String newpassword1 = newPassword1.getText();
    String newpassword2 = newPassword2.getText();
    checker.updatePassword(oldpassword1, oldpassword2, newpassword1, newpassword2);
    System.out.println("password changed");
    Navigation.navigate(Screen.HOME);
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
