package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DAOImps.*;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.*;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import java.awt.*;
import java.time.LocalDate;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javax.swing.text.html.ImageView;

public class HomeController {

  @FXML private VBox alertsContainer;
  @FXML private ImageView imageCredits;

  @FXML private VBox serviceRequestsContainer;

  @FXML
  public void initialize() {

    List<String> alerts = getAlerts(); // Get your alerts here

    // Reverse the list to display in reverse chronological order
    Collections.reverse(alerts);

    getAlerts(alertsContainer);
    loadServiceRequests(serviceRequestsContainer);
  }

  public void openAbout() {
    Navigation.navigate(Screen.ABOUT_HOME);
  }

  public void openCredits() {
    Navigation.navigate(Screen.ABOUT_CREDITPAGE);
  }

  private void getAlerts(VBox alertsContainer) {
    alertsContainer.getChildren().clear();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate currentDate = LocalDate.now(); // currentDate is of type LocalDate

    // getting alert info from db
    AlertDAOImp alertDAOImp = new AlertDAOImp();
    HashMap<Integer, Alert> alertMap = alertDAOImp.getAlertMap();

    // adding alerts to a list
    List<Alert> alertsList = new ArrayList<>(alertMap.values());

    // sorting alerts by date in descending order
    alertsList.sort((alert1, alert2) -> alert2.getDate().compareTo(alert1.getDate()));

    // looping through data and adding content
    for (Alert alert : alertsList) {
      // skip alerts with future dates
      if (alert.getDate().isAfter(currentDate)) {
        continue;
      }

      // setting up content
      VBox content = new VBox(5);
      content.setStyle(
          "-fx-background-color: #f1f1f1; -fx-background-radius: 10; -fx-padding: 10; -fx-alignment: center-left; -fx-pref-width: 500");
      Label lblTicketNumber = new Label("ID: " + alert.getTicketNum());
      Label lblUsername = new Label("Username: " + alert.getUsername());
      Label lblDate = new Label("Date: " + alert.getDate().format(formatter));
      Label lblMessage = new Label("Message: " + alert.getMessage());
      content.getChildren().addAll(lblTicketNumber, lblUsername, lblDate, lblMessage);

      alertsContainer.getChildren().add(content);
    }
  }

  private void loadServiceRequests(VBox serviceRequestsContainer) {
    serviceRequestsContainer.getChildren().clear();

    DataBaseRepository dbRepo = new DataBaseRepository();

    // Get service requests created by the logged-in user
    ArrayList<Flower> createdFlowers =
        dbRepo.getCreatedFlower(AccountSingleton.INSTANCE.getValue().getUserName());
    ArrayList<ConferenceRoomResRequest> createdCRRs =
        dbRepo.getCreatedCRR(AccountSingleton.INSTANCE.getValue().getUserName());
    ArrayList<FurnitureRequest> createdFurniture =
        dbRepo.getCreatedFurniture(AccountSingleton.INSTANCE.getValue().getUserName());
    ArrayList<Meal> createdMeals =
        dbRepo.getCreatedMeal(AccountSingleton.INSTANCE.getValue().getUserName());

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    FlowerDAOImp flowerDAOImp = new FlowerDAOImp();
    HashMap<Integer, Flower> flowerMap = flowerDAOImp.getFlowerMap();

    CRRRDAOImp crrrDAOImp = new CRRRDAOImp();
    HashMap<Integer, ConferenceRoomResRequest> crrrMap = crrrDAOImp.getCrrrMap();

    FurnitureDAOImp furnitureDAOImp = new FurnitureDAOImp();
    HashMap<Integer, FurnitureRequest> furnitureMap = furnitureDAOImp.getFurnitureMap();

    MealDAOImp mealDAOImp = new MealDAOImp();
    HashMap<Integer, Meal> mealMap = mealDAOImp.getMealMap();

    // Load Flower Requests
    for (Flower flower : createdFlowers) {

      // Create content container for each request
      VBox content = new VBox(5);
      content.setStyle(
          "-fx-background-color: #f1f1f1; -fx-background-radius: 10; -fx-padding: 10; -fx-alignment: center-left; -fx-pref-width: 500");
      Label lblTicketNumber = new Label("ID: " + flower.getId());
      Label lblRequestType = new Label("Flower Request");
      Label lblStatus = new Label("Status: " + flower.getStatus());

      content.getChildren().addAll(lblTicketNumber, lblRequestType, lblStatus);
      serviceRequestsContainer.getChildren().add(content);

      content.setOnMouseClicked(
          (e) -> {
            Navigation.navigate(Screen.SERVICE_REQUEST);
          });
    }

    // Load CRRR Requests
    for (ConferenceRoomResRequest crrr : createdCRRs) {

      // Create content container for each request
      VBox content = new VBox(5);
      content.setStyle(
          "-fx-background-color: #f1f1f1; -fx-background-radius: 10; -fx-padding: 10; -fx-alignment: center-left; -fx-pref-width: 500");
      Label lblTicketNumber = new Label("ID: " + crrr.getId());
      Label lblRequestType = new Label("Conference Room Reservation Request");
      Label lblStatus = new Label("Status: " + crrr.getStatus());

      content.getChildren().addAll(lblTicketNumber, lblRequestType, lblStatus);
      serviceRequestsContainer.getChildren().add(content);

      content.setOnMouseClicked(
          (e) -> {
            Navigation.navigate(Screen.SERVICE_REQUEST);
          });
    }

    // Load Furniture Requests
    for (FurnitureRequest furniture : createdFurniture) {

      // Create content container for each request
      VBox content = new VBox(5);
      content.setStyle(
          "-fx-background-color: #f1f1f1; -fx-background-radius: 10; -fx-padding: 10; -fx-alignment: center-left; -fx-pref-width: 500");
      Label lblTicketNumber = new Label("ID: " + furniture.getId());
      Label lblRequestType = new Label("Furniture Request");
      Label lblStatus = new Label("Status: " + furniture.getStatus());
      content.getChildren().addAll(lblTicketNumber, lblRequestType, lblStatus);
      serviceRequestsContainer.getChildren().add(content);

      content.setOnMouseClicked(
          (e) -> {
            Navigation.navigate(Screen.SERVICE_REQUEST);
          });
    }
    // Load Meal Requests
    for (Meal meal : createdMeals) {
      // Create content container for each request
      VBox content = new VBox(5);
      content.setStyle(
          "-fx-background-color: #f1f1f1; -fx-background-radius: 10; -fx-padding: 10; -fx-alignment: center-left; -fx-pref-width: 500");
      Label lblTicketNumber = new Label("ID: " + meal.getId());
      Label lblRequestType = new Label("Meal Request");
      Label lblStatus = new Label("Status: " + meal.getStatus());

      content.getChildren().addAll(lblTicketNumber, lblRequestType, lblStatus);
      serviceRequestsContainer.getChildren().add(content);

      content.setOnMouseClicked(
          (e) -> {
            Navigation.navigate(Screen.SERVICE_REQUEST);
          });
    }
  }
}
