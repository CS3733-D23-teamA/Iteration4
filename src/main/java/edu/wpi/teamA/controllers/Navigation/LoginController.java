package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DAOImps.UserDAOImp;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
  @FXML private Button loginButton;

  @FXML private Button exitButton;
  @FXML private Label loginMessageLabel;
  @FXML private TextField usernameTextField;
  @FXML private PasswordField passwordTextField;

  UserDAOImp checker = new UserDAOImp();

  @FXML
  public void initialize() {
    // Enter Key functionality
    //    loginButton.setOnKeyPressed(
    //        event -> {
    //          if (event.getCode().equals(KeyCode.ENTER)) {
    //            loginButton.onActionProperty();
    //          }
    //        });
  }

  public void login() {
    String username = usernameTextField.getText();
    String password = passwordTextField.getText();
    if (username.isBlank() == true) {
      loginMessageLabel.setText("Please enter username");
    } else if (password.isBlank() == true) {
      loginMessageLabel.setText("Please enter password");
    } else {
      if (checker.checkUser(username, password)) {
        Navigation.navigate(Screen.HOME);
      }
      loginMessageLabel.setText("Your username or password is incorrect.");
    }
  }

  public void exit() {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }
}
