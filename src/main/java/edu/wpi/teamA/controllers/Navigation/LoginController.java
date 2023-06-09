package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.User;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController {
  @FXML private Button loginButton;
  @FXML private Button exitButton;
  @FXML private Label loginMessageLabel;
  @FXML private Label swipeMessageLabel;
  @FXML private TextField usernameTextField;
  @FXML private PasswordField passwordTextField;
  @FXML private TextField userIDField;
  @FXML private AnchorPane imagePane;

  @FXML private VBox loginContatiner;
  @FXML private VBox swipeContainer;

  DataBaseRepository dataBaseRepository = DataBaseRepository.getInstance();

  @FXML
  public void initialize() {
    loginContatiner.setVisible(false);
    swipeContainer.setVisible(true);
  }

  public void login() {

    String username = usernameTextField.getText();
    String password = passwordTextField.getText();
    String userID = userIDField.getText();
    // Checking username and password input fields

    if (username.isBlank() && password.isBlank() && userID.isBlank()) {
      loginMessageLabel.setText(
          "Please enter username and password"); // request username and password if neither are
      // entered
      return;
    } else if (username.isBlank() && userID.isBlank()) {
      loginMessageLabel.setText("Please enter username"); // requests username if one is not entered
      return;
    } else if (password.isBlank() && userID.isBlank()) {
      loginMessageLabel.setText("Please enter password"); // requests password if one is not entered
      return;
    }

    if (!userID.isBlank()) {
      User user = dataBaseRepository.checkUserByID(userID);
      if (user != null) {
        AccountSingleton.INSTANCE.setValue(user);
        Navigation.navigate(Screen.HOME);
      } else {
        swipeMessageLabel.setText("User ID doesn't exist, swipe again");
        userIDField.clear();
      }
    } else {
      // Setting up User
      User user =
          dataBaseRepository.checkUser(
              username, password); // Make a user object to send to Home Page controller
      User wrongPassword = new User(2, "N", "N", "N", "N", "N"); // creates no existing user object
      if (user != null) { // checks if a user was returned by check user (the username exists)
        if (user.equals(wrongPassword)) { // checks if returned user is the wrong password user
          loginMessageLabel.setText("Your password is incorrect.");
        } else if (user.getAccessLevel() // else because it returned a user the password was correct
            == 1) { // checks if user is an admin, if so, do following commands

          AccountSingleton.INSTANCE.setValue(user);
          Navigation.navigate(Screen.HOME);
        } else { // actions to be made if user is not an admin
          AccountSingleton.INSTANCE.setValue(user);
          Navigation.navigate(Screen.HOME);
        }
      } else {
        loginMessageLabel.setText("Your username does not exist.");
      }
    }
  }

  public void showLogin() {
    swipeContainer.setVisible(false);
    loginContatiner.setVisible(true);
  }

  public void showSwipe() {
    swipeContainer.setVisible(true);
    loginContatiner.setVisible(false);
  }

  public void exit() throws IOException {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }
}
