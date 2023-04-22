package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.AccountSingleton;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.*;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.util.ArrayList;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ServiceRequestController extends PageController {

  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();
  @FXML private Text title;
  @FXML private MFXComboBox<String> chooseServiceRequestEmployee;
  @FXML private MFXComboBox<String> chooseServiceRequestStatus;
  @FXML private MFXComboBox<String> chooseEmployee;
  @FXML private MFXComboBox<String> chooseStatus;
  @FXML private MFXButton submitButtonEmployee;
  @FXML private MFXButton submitButtonStatus;
  @FXML private VBox employeeVbox;
  @FXML private VBox statusVbox;

  @FXML private TableView<Flower> flowerTable;
  @FXML private TableColumn<Flower, Integer> flowerIDCol;
  @FXML private TableColumn<Flower, String> flowerNameCol;
  @FXML private TableColumn<Flower, String> flowerRoomCol;
  @FXML private TableColumn<Flower, String> flowerDateCol;
  @FXML private TableColumn<Flower, Integer> flowerTimeCol;
  @FXML private TableColumn<Flower, String> flowerTypeCol;
  @FXML private TableColumn<Flower, String> flowerCommentCol;
  @FXML private TableColumn<Flower, String> flowerEmployeeCol;
  @FXML private TableColumn<Flower, String> flowerStatusCol;

  @FXML private TableView<ConferenceRoomResRequest> roomTable;
  @FXML private TableColumn<ConferenceRoomResRequest, String> crrrIDCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> crrrNameCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> crrrRoomCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> crrrDateCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> crrrStartCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> crrrEndCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> crrrCommentCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> crrrEmployeeCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> crrrStatusCol;

  @FXML private TableView<FurnitureRequest> furnitureTable;
  @FXML private TableColumn<FurnitureRequest, Integer> furnitureIDCol;
  @FXML private TableColumn<FurnitureRequest, String> furnitureNameCol;
  @FXML private TableColumn<FurnitureRequest, String> furnitureRoomCol;
  @FXML private TableColumn<FurnitureRequest, String> furnitureDateCol;
  @FXML private TableColumn<FurnitureRequest, Integer> furnitureTimeCol;
  @FXML private TableColumn<FurnitureRequest, String> furnitureTypeCol;
  @FXML private TableColumn<FurnitureRequest, String> furnitureCommentCol;
  @FXML private TableColumn<FurnitureRequest, String> furnitureEmployeeCol;
  @FXML private TableColumn<FurnitureRequest, String> furnitureStatusCol;

  @FXML private TableView<Meal> mealTable;
  @FXML private TableColumn<Meal, Integer> mealIDCol;
  @FXML private TableColumn<Meal, String> mealNameCol;
  @FXML private TableColumn<Meal, String> mealRoomCol;
  @FXML private TableColumn<Meal, String> mealDateCol;
  @FXML private TableColumn<Meal, Integer> mealTimeCol;
  @FXML private TableColumn<Meal, String> mealTypeCol;
  @FXML private TableColumn<Meal, String> mealCommentCol;
  @FXML private TableColumn<Meal, String> mealEmployeeCol;
  @FXML private TableColumn<Meal, String> mealStatusCol;

  @FXML
  public void initialize() {
    displayFlowerRequests();
    displayCRRRRequests();
    displayFurnitureRequests();
    displayMealRequests();

    flowerTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection != null) {
                roomTable.getSelectionModel().clearSelection();
                Navigation.navigate(Screen.FLOWER_REQUEST);
                Flower f = newSelection;
              }
            });
    roomTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection != null) {
                flowerTable.getSelectionModel().clearSelection();
              }
            });

    if (AccountSingleton.INSTANCE1.getValue().getIsAdmin()) {
      title.setText("Open Service Requests");
      ArrayList<String> allServiceRequests = new ArrayList<>();
      // load flowers
      for (Map.Entry<Integer, Flower> entry : databaseRepo.getFlowerMap().entrySet()) {
        Flower flower = entry.getValue();
        allServiceRequests.add("Flower " + flower.getId());
      }

      // load CRRRs
      for (Map.Entry<Integer, ConferenceRoomResRequest> entry :
          databaseRepo.getCrrrMap().entrySet()) {
        ConferenceRoomResRequest crrr = entry.getValue();
        allServiceRequests.add("Conference " + crrr.getId());
      }

      // load furniture
      for (Map.Entry<Integer, FurnitureRequest> entry : databaseRepo.getFurnitureMap().entrySet()) {
        FurnitureRequest furniture = entry.getValue();
        allServiceRequests.add("Furniture " + furniture.getId());
      }

      // load meals
      for (Map.Entry<Integer, Meal> entry : databaseRepo.getMealMap().entrySet()) {
        Meal meal = entry.getValue();
        allServiceRequests.add("Meal " + meal.getId());
      }

      ArrayList<String> allEmployeeUsernames = new ArrayList<>();
      for (Map.Entry<String, Employee> entry : databaseRepo.getEmployeeMap().entrySet()) {
        Employee employee = entry.getValue();
        allEmployeeUsernames.add(employee.getUsername());
      }

      chooseServiceRequestEmployee.getItems().addAll(allServiceRequests);
      chooseEmployee.getItems().addAll(allEmployeeUsernames);

      chooseServiceRequestStatus.getItems().addAll(allServiceRequests);
      chooseStatus.getItems().addAll("new", "in progress", "done");
    } else {
      employeeVbox.setVisible(false);
      employeeVbox.setManaged(false);
      statusVbox.setVisible(false);
      statusVbox.setManaged(false);
    }
  }

  @FXML
  public void validateButtonEmployee() {
    if (chooseServiceRequestEmployee.getSelectedIndex() == -1
        || chooseEmployee.getSelectedIndex() == -1) {
      submitButtonEmployee.setDisable(true);
    } else {
      submitButtonEmployee.setDisable(false);
    }
  }

  @FXML
  public void validateButtonStatus() {
    if (chooseServiceRequestStatus.getSelectedIndex() == -1
        || chooseStatus.getSelectedIndex() == -1) {
      submitButtonStatus.setDisable(true);
    } else {
      submitButtonStatus.setDisable(false);
    }
  }

  @FXML
  public void submitStatus() {
    String status = chooseStatus.getSelectedItem();
    String serviceRequest = chooseServiceRequestStatus.getSelectedItem();

    // get the id of the service request
    int id = Integer.parseInt(serviceRequest.substring(serviceRequest.indexOf(" ") + 1));

    // figure out which service request we are dealing with
    if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Flower")) {
      // use service request type and name to get the service request
      Flower flower = databaseRepo.getFlower(id);

      // update status in the service request
      flower.setStatus(status);
      databaseRepo.updateFlower(flower);

      // update display
      displayFlowerRequests();
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Conference")) {
      // use service request type and name to get the service request
      ConferenceRoomResRequest crrr = databaseRepo.getCRRR(id);

      // update status in the service request
      crrr.setStatus(status);
      databaseRepo.updateCRRR(crrr);

      // update display
      displayCRRRRequests();
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Furniture")) {
      // use service request type and name to get the service request
      FurnitureRequest furniture = databaseRepo.getFurniture(id);

      // update status in the service request
      furniture.setStatus(status);
      databaseRepo.updateFurniture(furniture);

      // update display
      displayFurnitureRequests();
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Meal")) {
      // use service request type and name to get the service request
      Meal meal = databaseRepo.getMeal(id);

      // update status in the service request
      meal.setStatus(status);
      databaseRepo.updateMeal(meal);

      // update display
      displayMealRequests();
    }
  }

  @FXML
  public void submitEmployee() {
    String assignedEmployee = chooseEmployee.getSelectedItem();
    String serviceRequest = chooseServiceRequestEmployee.getSelectedItem();

    // get the id of the service request
    int id = Integer.parseInt(serviceRequest.substring(serviceRequest.indexOf(" ") + 1));

    // figure out which service request we are dealing with
    if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Flower")) {
      // use service request type and name to get the service request
      Flower flower = databaseRepo.getFlower(id);

      // update assigned employee
      flower.setEmployee(assignedEmployee);
      databaseRepo.updateFlower(flower);

      // update display
      displayFlowerRequests();
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Conference")) {
      // use service request type and name to get the service request
      ConferenceRoomResRequest crrr = databaseRepo.getCRRR(id);

      // update status in the service request
      crrr.setEmployee(assignedEmployee);
      databaseRepo.updateCRRR(crrr);

      // update display
      displayCRRRRequests();
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Furniture")) {
      // use service request type and name to get the service request
      FurnitureRequest furniture = databaseRepo.getFurniture(id);

      // update status in the service request
      furniture.setEmployee(assignedEmployee);
      databaseRepo.updateFurniture(furniture);

      // update display
      displayFurnitureRequests();
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Meal")) {
      // use service request type and name to get the service request
      Meal meal = databaseRepo.getMeal(id);

      // update status in the service request
      meal.setEmployee(assignedEmployee);
      databaseRepo.updateMeal(meal);

      // update display
      displayMealRequests();
    }
  }

  public void displayFlowerRequests() {
    flowerIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    flowerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    flowerRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
    flowerDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    flowerTimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
    flowerTypeCol.setCellValueFactory(new PropertyValueFactory<>("flowerType"));
    flowerCommentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
    flowerEmployeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    flowerStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

    flowerTable.setItems(FXCollections.observableArrayList(databaseRepo.getFlowerMap().values()));
    flowerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  public void displayCRRRRequests() {
    crrrIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    crrrNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    crrrRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
    crrrDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    crrrStartCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
    crrrEndCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    crrrCommentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
    crrrEmployeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    crrrStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

    roomTable.setItems(FXCollections.observableArrayList(databaseRepo.getCrrrMap().values()));
    roomTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  public void displayFurnitureRequests() {
    furnitureIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    furnitureNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    furnitureRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
    furnitureDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    furnitureTimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
    furnitureTypeCol.setCellValueFactory(new PropertyValueFactory<>("furnitureType"));
    furnitureCommentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
    furnitureEmployeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    furnitureStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

    furnitureTable.setItems(
        FXCollections.observableArrayList(databaseRepo.getFurnitureMap().values()));
    furnitureTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  public void displayMealRequests() {
    mealIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    mealNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    mealRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
    mealDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    mealTimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
    mealTypeCol.setCellValueFactory(new PropertyValueFactory<>("mealType"));
    mealCommentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
    mealEmployeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    mealStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

    mealTable.setItems(FXCollections.observableArrayList(databaseRepo.getMealMap().values()));
    mealTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }
}
