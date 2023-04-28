package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;

public class AddUserController {
  @FXML private MFXTextField firstNameTextField;
  @FXML private MFXTextField lastNameTextField;
  @FXML private MFXTextField passwordTextField;
  @FXML private MFXTextField usernameTextField;
  @FXML private MFXCheckbox isAdminCheckbox;
  @FXML private MFXButton updateButton;
  DataBaseRepository checker = new DataBaseRepository();

  public void initialize() {}

  public void addUser() {
    int isAdmin = 0;
    if (isAdminCheckbox.isSelected()) {
      isAdmin = 1;
    }
    if (firstNameTextField.getText().isBlank()
        || lastNameTextField.getText().isBlank()
        || usernameTextField.getText().isBlank()
        || passwordTextField.getText().isBlank()) {
      System.out.println("somthing has not been entered");
    } else {
      checker.addUser(
          isAdmin,
          usernameTextField.getText(),
          passwordTextField.getText(),
          firstNameTextField.getText(),
          lastNameTextField.getText());
      System.out.println("User added");
      Navigation.navigate(Screen.ACCOUNT_SETTINGS);
    }
  }
}
