package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class AboutController {

  @FXML Label name;
  @FXML Text funFactText;
  @FXML Text major;
  @FXML MFXButton funFacts;
  @FXML MFXGenericDialog pop;
  @FXML MFXButton XButton;

  @FXML
  public void initialize() {
    pop.toFront();
    pop.setVisible(false);
    pop.setDisable(true);
    // pop.;
    pop.setOnClose(
        event -> {
          pop.setVisible(false);
          pop.setDisable(true);
        });
  }

  public void back() {
    Navigation.navigate(Screen.HOME);
  }

  public void pooja() {
    pop.setVisible(true);
    pop.setDisable(false);
    name.setText("Pooja");
    major.setText("Major: Computer Science w/ Data Science");
    funFactText.setText("Pooja is on a WPI dance team.");
  }

  public void alex() {
    pop.setVisible(true);
    pop.setDisable(false);
    name.setText("Alex");
    major.setText("Major: Computer Science ");
    funFactText.setText("Alex's favorite movie is the Fast and Furious.");
  }

  public void vincent() {
    pop.setVisible(true);
    pop.setDisable(false);
    name.setText("Vincent");
    major.setText("Major: Computer Science");
    funFactText.setText("Vincent likes to go hiking at Mount Wachusett.");
  }

  public void ayden() {
    pop.setVisible(true);
    pop.setDisable(false);
    name.setText("Ayden");
    major.setText("Major: Computer Science and Robotics");
    funFactText.setText("Ayden likes to go skiing.");
  }

  public void ashleigh() {
    pop.setVisible(true);
    pop.setDisable(false);
    name.setText("Ashleigh");
    major.setText("Major: Computer Science and Data Science");
    funFactText.setText("Ashleigh is from New Jersey.");
  }

  public void xiao() {
    pop.setVisible(true);
    pop.setDisable(false);
    name.setText("Xiao");
    major.setText("Major: Computer Science");
    funFactText.setText("Xiao likes to play team fight tactics.");
  }

  public void ryan() {
    pop.setVisible(true);
    pop.setDisable(false);
    name.setText("Ryan");
    major.setText("Major: Computer Science");
    funFactText.setText("Ryan likes to go to the gym.");
  }

  public void seth() {
    pop.setVisible(true);
    pop.setDisable(false);
    name.setText("Seth");
    major.setText("Major: Computer Science");
    funFactText.setText("Seth likes to listen classical music.");
  }

  public void maryna() {
    pop.setVisible(true);
    pop.setDisable(false);
    name.setText("Maryna");
    major.setText("Major: Computer Science");
    funFactText.setText("Maryna likes spicy food.");
  }

  public void close() {
    pop.setVisible(false);
  }

  public void ExitDialog() {
    pop.setVisible(false);
    pop.setDisable(true);
  }
}
