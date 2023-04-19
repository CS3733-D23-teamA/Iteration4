package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DAOImps.UserDAOImp;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.fxml.FXML;

public class AccountSettingsController {

  @FXML private MFXButton updateButton;

  @FXML private MFXPasswordField password1;
  @FXML private MFXPasswordField password2;
  @FXML private MFXPasswordField newPassword1;
  @FXML private MFXPasswordField newPassword2;
  UserDAOImp checker = new UserDAOImp();

  @FXML
  public void initialize() {
    updateButton.setDisable(true);
  }

  public void updatePassword() {
    String oldpassword1 = password1.getText();
    String oldpassword2 = password2.getText();
    String newpassword1 = newPassword1.getText();
    String newpassword2 = newPassword2.getText();
    checker.updatePassword(oldpassword1, oldpassword2, newpassword1, newpassword2);
    System.out.println("password changed");
    Navigation.navigate(Screen.HOME);
  }

  public void validateUpdate() {
    if (password1.getText().isEmpty()
        || password2.getText().isEmpty()
        || newPassword1.getText().isEmpty()
        || newPassword2.getText().isEmpty()) {
      updateButton.setDisable(true);
    } else {
      updateButton.setDisable(false);
    }
  }
}
