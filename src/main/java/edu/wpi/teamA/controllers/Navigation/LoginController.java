package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.AccountSingleton;
import edu.wpi.teamA.database.DAOImps.UserDAOImp;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {
  @FXML private Button loginButton;
  @FXML private Button exitButton;
  @FXML private Label loginMessageLabel;
  @FXML private TextField usernameTextField;
  @FXML private PasswordField passwordTextField;
  @FXML private AnchorPane imagePane;

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
    System.out.println(username + " " + password);
    System.out.println(username.isBlank());
    System.out.println(password.isBlank());
    // Checking username and password inpput fields
    if (username.isBlank() == true) {
      if (password.isBlank() == true) {
        loginMessageLabel.setText(
            "Please enter username and password"); // request username and password if neither are
        // entered
        return;
      } else {
        loginMessageLabel.setText(
            "Please enter username"); // requests username if one is not entered
        return;
      }
    }
    if (password.isBlank() == true) {
      loginMessageLabel.setText("Please enter password"); // requests password if one is not entered
      return;
    }
    // Setting up User
    User user =
        checker.checkUser(username, password); // Make a user object to send to Home Page controller
    User wrongPassword = new User(2, "N", "N", "N", "N"); // creates no existing user object
    if (user != null) { // checks if a user was returned by check user (the username exists)
      if (user.equals(wrongPassword)) { // checks if returned user is the wrong password user
        loginMessageLabel.setText("Your password is incorrect.");
      } else if (user.getAdminYes() // else because it returned a user the password was correct
          == 1) { // checks if user is an admin, if so, do following commands

        AccountSingleton.INSTANCE1.setValue(user);
        Navigation.navigate(Screen.HOME);
      } else { // actions to be made if user is not an admin
        AccountSingleton.INSTANCE1.setValue(user);
        Navigation.navigate(Screen.HOME);
      }
    } else {
      loginMessageLabel.setText("Your username does not exist.");
    }
  }

  public void exit() {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }
}
