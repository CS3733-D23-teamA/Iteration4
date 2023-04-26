package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AccountSettingsController {

  @FXML private MFXButton updateButton;
  @FXML private MFXPasswordField password1;
  @FXML private MFXPasswordField password2;
  @FXML private MFXPasswordField newPassword1;
  @FXML private MFXPasswordField newPassword2;
  @FXML private MFXTextField oldFirst;
  @FXML private MFXTextField oldLast;
  @FXML private MFXTextField newFirst;
  @FXML private MFXTextField newLast;
  @FXML private Label welcomeMessage;
  DataBaseRepository checker = new DataBaseRepository();

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

  public void updateName() {
    String oldFirstName = oldFirst.getText();
    String oldLastName = oldLast.getText();
    String newFirstName = newFirst.getText();
    String newLastName = newLast.getText();
    checker.updateName(oldFirstName, oldLastName, newFirstName, newLastName);
    System.out.println("Name Updated");
    Navigation.navigate(Screen.ACCOUNT_SETTINGS);
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

  public void validateChangeName() {
    if (newFirst.getText().isEmpty()
        || newLast.getText().isEmpty()
        || oldFirst.getText().isEmpty()
        || oldLast.getText().isEmpty()) {
      updateButton.setDisable(true);
    } else {
      updateButton.setDisable(false);
    }
  }
}
