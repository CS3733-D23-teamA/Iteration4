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
  @FXML MFXButton funFacts;
  @FXML MFXGenericDialog pop;

  @FXML
  public void initialize() {
    // pop.toFront();
    pop.setVisible(false);
    pop.setDisable(true);

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
    name.setText("Pooja");
    funFactText.setText("Pooja likes to dance.");
  }

  public void alex() {
    pop.setVisible(true);
    name.setText("Alex");
    funFacts.setText("Alex's favorite movie is the Fast and Furious.");
  }

  public void vincent() {
    pop.setVisible(true);
    name.setText("Vincent");
    funFacts.setText("Vincent likes to go hiking at Mount Wachusett.");
  }

  public void ayden() {
    pop.setVisible(true);
    name.setText("Ayden");
    funFacts.setText("Ayden likes to go skiing.");
  }

  public void ashleigh() {
    pop.setVisible(true);
    name.setText("Ashleigh");
    funFacts.setText("Ashleigh is from New Jersey.");
  }

  public void xiao() {
    pop.setVisible(true);
    name.setText("Xiao");
    funFacts.setText("Xiao likes to play team fight tactics.");
  }

  public void ryan() {
    pop.setVisible(true);
    name.setText("Ryan");
    funFacts.setText("Ryan likes to go to the gym.");
  }

  public void seth() {
    pop.setVisible(true);
    name.setText("Seth");
    funFacts.setText("Seth likes to listen classical music.");
  }

  public void maryna() {
    pop.setVisible(true);
    name.setText("Maryna");
    funFacts.setText("Maryna likes spicy food.");
  }

  public void close() {
    pop.setVisible(false);
  }
}
