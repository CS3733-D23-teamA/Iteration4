package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
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

  @FXML
  public void initialize() {

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
    Navigation.navigate(Screen.ABOUT_HOME);
  }

  public void openCredits() {
    // Navigation.navigate(Screen.CREDITS_HOME);
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
