package edu.wpi.teamA.controllers.Navigation.Requests;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.*;
import edu.wpi.teamA.database.Singletons.*;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.util.ArrayList;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ServiceRequestController {

  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();
  @FXML private StackPane requestStack;
  @FXML private MFXComboBox<String> requestCombo;
  @FXML private Text title;
  @FXML private MFXComboBox<String> chooseServiceRequestEmployee;
  @FXML private MFXComboBox<String> chooseServiceRequestStatus;
  @FXML private MFXComboBox<String> chooseEmployee;
  @FXML private MFXComboBox<String> chooseStatus;
  @FXML private MFXButton submitButtonEmployee;
  @FXML private MFXButton submitButtonStatus;
  @FXML private VBox employeeVbox;
  @FXML private VBox statusVbox;
  @FXML private MFXComboBox<String> acCombo;
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
  @FXML private TableColumn<Flower, String> flowerCreatorCol;

  @FXML private TableView<ConferenceRoomResRequest> roomTable;
  @FXML private TableColumn<ConferenceRoomResRequest, String> roomIDCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> roomNameCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> roomRoomCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> roomDateCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> roomStartCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> roomEndCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> roomCommentCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> roomEmployeeCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> roomStatusCol;
  @FXML private TableColumn<Flower, String> roomCreatorCol;

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
  @FXML private TableColumn<Flower, String> furnitureCreatorCol;

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
  @FXML private TableColumn<Flower, String> mealCreatorCol;

  ChangeListener<Flower> flowerSelectionListener =
      (obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
          FlowerSingleton.INSTANCE.setValue(newSelection);
          Navigation.navigate(Screen.FLOWER_REQUEST_EDIT);
        }
      };
  ChangeListener<ConferenceRoomResRequest> CRSelectionListener =
      (obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
          CRRRSingleton.INSTANCE.setValue(newSelection);
          Navigation.navigate(Screen.CR_REQUEST_EDIT);
        }
      };
  ChangeListener<FurnitureRequest> FurnitureSelectionListener =
      (obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
          FurnitureSingleton.INSTANCE.setValue(newSelection);
          Navigation.navigate(Screen.FURNITURE_REQUEST_EDIT);
        }
      };
  ChangeListener<Meal> MealSelectionListener =
      (obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
          MealSingleton.INSTANCE.setValue(newSelection);
          Navigation.navigate(Screen.MEAL_REQUEST_EDIT);
        }
      };

  @FXML
  public void initialize() {
    // requestCombo.setPromptText("Select Request");
    requestCombo
        .getItems()
        .addAll(
            "Flower Request",
            "Conference Room Request",
            "Meal Delivery Request",
            "Furniture Delivery Request");
    requestCombo.getSelectionModel().selectItem("Flower Request");
    if (AccountSingleton.isAdmin()) {
      acCombo.hide();
      acCombo.setDisable(true);
    }
    acCombo.getItems().addAll("Created", "Assigned");
    acCombo.getSelectionModel().selectItem("Created");
    displayFlowerRequests(acCombo.getSelectedItem());
    flowerTable.toFront();

    if (AccountSingleton.isAdmin()) {
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

      chooseStatus.getItems().addAll("new", "assigned", "in progress", "in delivery", "done");
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
      displayFlowerRequests(acCombo.getSelectedItem());
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Conference")) {
      // use service request type and name to get the service request
      ConferenceRoomResRequest crrr = databaseRepo.getCRRR(id);

      // update status in the service request
      crrr.setStatus(status);
      databaseRepo.updateCRRR(crrr);

      // update display
      displayCRRRRequests(acCombo.getSelectedItem());
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Furniture")) {
      // use service request type and name to get the service request
      FurnitureRequest furniture = databaseRepo.getFurniture(id);

      // update status in the service request
      furniture.setStatus(status);
      databaseRepo.updateFurniture(furniture);

      // update display
      displayFurnitureRequests(acCombo.getSelectedItem());
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Meal")) {
      // use service request type and name to get the service request
      Meal meal = databaseRepo.getMeal(id);

      // update status in the service request
      meal.setStatus(status);
      databaseRepo.updateMeal(meal);

      // update display
      displayMealRequests(acCombo.getSelectedItem());
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
      displayFlowerRequests(acCombo.getSelectedItem());
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Conference")) {
      // use service request type and name to get the service request
      ConferenceRoomResRequest crrr = databaseRepo.getCRRR(id);

      // update status in the service request
      crrr.setEmployee(assignedEmployee);
      databaseRepo.updateCRRR(crrr);

      // update display
      displayCRRRRequests(acCombo.getSelectedItem());
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Furniture")) {
      // use service request type and name to get the service request
      FurnitureRequest furniture = databaseRepo.getFurniture(id);

      // update status in the service request
      furniture.setEmployee(assignedEmployee);
      databaseRepo.updateFurniture(furniture);

      // update display
      displayFurnitureRequests(acCombo.getSelectedItem());
    } else if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Meal")) {
      // use service request type and name to get the service request
      Meal meal = databaseRepo.getMeal(id);

      // update status in the service request
      meal.setEmployee(assignedEmployee);
      databaseRepo.updateMeal(meal);

      // update display
      displayMealRequests(acCombo.getSelectedItem());
    }
  }

  public void displayFlowerRequests(String x) {
    flowerTable.getSelectionModel().selectedItemProperty().removeListener(flowerSelectionListener);
    flowerIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    flowerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    flowerRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
    flowerDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    flowerTimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
    flowerTypeCol.setCellValueFactory(new PropertyValueFactory<>("items"));
    flowerCommentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
    flowerEmployeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    flowerStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    flowerCreatorCol.setCellValueFactory(new PropertyValueFactory<>("creator"));

    if (AccountSingleton.INSTANCE.getValue().isAdmin()) {
      flowerTable.setItems(FXCollections.observableArrayList(databaseRepo.getFlowerMap().values()));
      flowerTable.getSelectionModel().selectedItemProperty().addListener(flowerSelectionListener);
    } else {
      ArrayList<Flower> items = new ArrayList<>();
      if (x.equals("Created")) {
        items = databaseRepo.getCreatedFlower(AccountSingleton.INSTANCE.getValue().getUserName());
      } else if (x.equals("Assigned")) {
        items = databaseRepo.getAssignedFlower(AccountSingleton.INSTANCE.getValue().getUserName());
      }
      if (!items.isEmpty()) {
        flowerTable.setItems(FXCollections.observableArrayList(items));
        if (x.equals("Created")) {
          flowerTable
              .getSelectionModel()
              .selectedItemProperty()
              .addListener(flowerSelectionListener);
        }
      } else {
        flowerTable.getItems().clear();
      }
    }
    flowerTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  public void displayCRRRRequests(String x) {
    roomTable.getSelectionModel().selectedItemProperty().removeListener(CRSelectionListener);
    roomIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    roomNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    roomRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
    roomDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    roomStartCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
    roomEndCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    roomCommentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
    roomEmployeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    roomStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    roomCreatorCol.setCellValueFactory(new PropertyValueFactory<>("creator"));

    if (AccountSingleton.INSTANCE.getValue().isAdmin()) {
      roomTable.setItems(FXCollections.observableArrayList(databaseRepo.getCrrrMap().values()));
      roomTable.getSelectionModel().selectedItemProperty().addListener(CRSelectionListener);
    } else {
      ArrayList<ConferenceRoomResRequest> items = new ArrayList<>();
      if (x.equals("Created")) {
        items = databaseRepo.getCreatedCRR(AccountSingleton.INSTANCE.getValue().getUserName());
      } else if (x.equals("Assigned")) {
        items = databaseRepo.getAssignedCRRR(AccountSingleton.INSTANCE.getValue().getUserName());
      }
      if (!items.isEmpty()) {
        roomTable.setItems(FXCollections.observableArrayList(items));
        if (x.equals("Created")) {
          roomTable.getSelectionModel().selectedItemProperty().addListener(CRSelectionListener);
        }
      } else {
        roomTable.getItems().clear();
      }
    }
    roomTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  public void displayFurnitureRequests(String x) {
    furnitureTable
        .getSelectionModel()
        .selectedItemProperty()
        .removeListener(FurnitureSelectionListener);
    furnitureIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    furnitureNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    furnitureRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
    furnitureDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    furnitureTimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
    furnitureTypeCol.setCellValueFactory(new PropertyValueFactory<>("items"));
    furnitureCommentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
    furnitureEmployeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    furnitureStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    furnitureCreatorCol.setCellValueFactory(new PropertyValueFactory<>("creator"));

    if (AccountSingleton.INSTANCE.getValue().isAdmin()) {
      furnitureTable.setItems(
          FXCollections.observableArrayList(databaseRepo.getFurnitureMap().values()));
      furnitureTable
          .getSelectionModel()
          .selectedItemProperty()
          .addListener(FurnitureSelectionListener);
    } else {
      ArrayList<FurnitureRequest> items = new ArrayList<>();
      if (x.equals("Created")) {
        items =
            databaseRepo.getCreatedFurniture(AccountSingleton.INSTANCE.getValue().getUserName());
      } else if (x.equals("Assigned")) {
        items =
            databaseRepo.getAssignedFurniture(AccountSingleton.INSTANCE.getValue().getUserName());
        furnitureTable
            .getSelectionModel()
            .selectedItemProperty()
            .removeListener(FurnitureSelectionListener);
      }
      if (!items.isEmpty()) {
        furnitureTable.setItems(FXCollections.observableArrayList(items));
        if (x.equals("Created")) {
          furnitureTable
              .getSelectionModel()
              .selectedItemProperty()
              .addListener(FurnitureSelectionListener);
        }
      } else {
        furnitureTable.getItems().clear();
      }
    }
    furnitureTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  public void displayMealRequests(String x) {
    mealTable.getSelectionModel().selectedItemProperty().removeListener(MealSelectionListener);
    mealIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    mealNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    mealRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
    mealDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    mealTimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
    mealTypeCol.setCellValueFactory(new PropertyValueFactory<>("items"));
    mealCommentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
    mealEmployeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    mealStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    mealCreatorCol.setCellValueFactory(new PropertyValueFactory<>("creator"));

    if (AccountSingleton.INSTANCE.getValue().isAdmin()) {
      mealTable.setItems(FXCollections.observableArrayList(databaseRepo.getMealMap().values()));
      mealTable.getSelectionModel().selectedItemProperty().addListener(MealSelectionListener);
    } else {
      ArrayList<Meal> items = new ArrayList<>();
      if (x.equals("Created")) {
        items = databaseRepo.getCreatedMeal(AccountSingleton.INSTANCE.getValue().getUserName());
      } else if (x.equals("Assigned")) {
        items = databaseRepo.getAssignedMeal(AccountSingleton.INSTANCE.getValue().getUserName());
        mealTable.getSelectionModel().selectedItemProperty().removeListener(MealSelectionListener);
      }
      if (!items.isEmpty()) {
        mealTable.setItems(FXCollections.observableArrayList(items));
        if (x.equals("Created")) {
          mealTable.getSelectionModel().selectedItemProperty().addListener(MealSelectionListener);
        }
      } else {
        mealTable.getItems().clear();
      }
    }
    mealTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  public void updateTable() {
    int request = requestCombo.getSelectedIndex();
    if (request == 0) {
      displayFlowerRequests(acCombo.getSelectedItem());
      //      chooseStatus
      //          .getItems()
      //          .addAll("new", "in progress", "picking flowers", "flowers ready", "delivered!");
      flowerTable.toFront();

    } else if (request == 1) {
      displayCRRRRequests(acCombo.getSelectedItem());
      roomTable.toFront();

    } else if (request == 2) {
      displayMealRequests(acCombo.getSelectedItem());
      //      chooseStatus
      //          .getItems()
      //          .addAll("new", "in progress", "in preparation", "meal prepared", "delivered!");
      mealTable.toFront();

    } else if (request == 3) {
      //      chooseStatus.getItems().removeAll();
      displayFurnitureRequests(acCombo.getSelectedItem());
      //      chooseStatus
      //          .getItems()
      //          .addAll("new", "in progress", "furniture packed", "being shipped", "delivered!");
      furnitureTable.toFront();
    }
  }

  public void updateType() {
    displayFlowerRequests(acCombo.getSelectedItem());
    displayCRRRRequests(acCombo.getSelectedItem());
    displayMealRequests(acCombo.getSelectedItem());
    displayFurnitureRequests(acCombo.getSelectedItem());
  }
}
