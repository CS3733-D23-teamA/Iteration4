package edu.wpi.teamA.controllers.Navigation;

import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HomeController {

  @FXML private VBox alertsContainer;
  @FXML private MFXGenericDialog aboutDialog;
  @FXML private MFXGenericDialog creditDialog;

  @FXML
  public void initialize() {
    aboutDialog.setVisible(false);
    aboutDialog.setDisable(true);
    creditDialog.setVisible(false);

    aboutDialog.setOnClose(
        event -> {
          aboutDialog.setDisable(true);
          aboutDialog.setVisible(false);
        });

    creditDialog.setOnClose(
        event -> {
          creditDialog.setDisable(true);
          creditDialog.setVisible(false);
        });

    List<String> alerts = getAlerts(); // Get your alerts here

    // Reverse the list to display in reverse chronological order
    Collections.reverse(alerts);

    for (String alert : alerts) {
      Label alertLabel = new Label(alert);
      alertLabel.setFont(new Font("Arial", 16));
      alertLabel.setStyle("-fx-text-fill: #f1f1f1;");

      alertsContainer.getChildren().add(alertLabel);
    }
  }

  public void openAbout() {
    aboutDialog.setDisable(false);
    aboutDialog.setVisible(true);
  }

  public void openCredits() {
    creditDialog.setDisable(false);
    creditDialog.setVisible(true);
  }

  private List<String> getAlerts() {
    // This is just an example of alerts list with date and time.
    // Replace this method with your actual implementation to fetch alerts.
    List<String> alerts = new ArrayList<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    alerts.add("Alert 1: " + LocalDateTime.now().minusHours(2).format(formatter));
    alerts.add("Alert 2: " + LocalDateTime.now().minusHours(4).format(formatter));
    alerts.add("Alert 3: " + LocalDateTime.now().minusHours(6).format(formatter));

    return alerts;
  }
}
