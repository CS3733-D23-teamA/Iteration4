package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import edu.wpi.teamA.database.ORMclasses.Employee;
import edu.wpi.teamA.database.ORMclasses.FlowerEntity;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ServiceRequestController extends PageController {

  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();
  // private FlowerDAOImpl fdao = new FlowerDAOImpl();

  @FXML private MFXComboBox<String> chooseServiceRequestEmployee;
  @FXML private MFXComboBox<String> chooseServiceRequestStatus;
  @FXML private MFXComboBox<String> chooseEmployee;
  @FXML private MFXComboBox<String> chooseStatus;
  @FXML private MFXButton submitButtonEmployee;
  @FXML private MFXButton submitButtonStatus;

  @FXML private TableView<FlowerEntity> flowerTable;
  @FXML private TableColumn<FlowerEntity, Integer> idCol;
  @FXML private TableColumn<FlowerEntity, String> roomCol;
  @FXML private TableColumn<FlowerEntity, String> dateCol;
  @FXML private TableColumn<FlowerEntity, Integer> timeCol;
  @FXML private TableColumn<FlowerEntity, String> flowerCol;
  @FXML private TableColumn<FlowerEntity, String> commentCol;
  @FXML private TableColumn<FlowerEntity, String> employeeCol;
  @FXML private TableColumn<FlowerEntity, String> statusCol;

  // private CRRRDAOImp cdao = new CRRRDAOImp();
  @FXML private TableView<ConferenceRoomResRequest> roomTable;
  @FXML private TableColumn<ConferenceRoomResRequest, String> roomCol1;
  @FXML private TableColumn<ConferenceRoomResRequest, String> dateCol1;
  @FXML private TableColumn<ConferenceRoomResRequest, String> startCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> endCol;
  @FXML private TableColumn<ConferenceRoomResRequest, String> commentCol1;
  @FXML private TableColumn<ConferenceRoomResRequest, String> statusCol1;

  @FXML
  public void initialize() {
    displayFlowerRequests();
    displayRoomRequests(databaseRepo.getAllCRRR());

    flowerTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection != null) {
                roomTable.getSelectionModel().clearSelection();
                Navigation.navigate(Screen.FLOWER_REQUEST);
                FlowerEntity f = newSelection;
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

    // TODO ADMIN ONLY
    ArrayList<String> allServiceRequests = new ArrayList<>();
    // HashMap<Integer, FlowerEntity> flowerRequests = databaseRepo.getFlowerMap();
    for (Map.Entry<Integer, FlowerEntity> entry : databaseRepo.getFlowerMap().entrySet()) {
      FlowerEntity flower = entry.getValue();
      allServiceRequests.add("Flower " + flower.getId());
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
    String serviceRequest = chooseServiceRequestStatus.getSelectedItem();

    // get the id of the service request
    int id = Integer.parseInt(serviceRequest.substring(serviceRequest.indexOf(" ") + 1));

    // figure out which service request we are dealing with
    if (serviceRequest.substring(0, serviceRequest.indexOf(" ")).equals("Flower")) {
      // use service request type and name to get the service request
      FlowerEntity flower = databaseRepo.getFlower(id);

      // update status in the service request
      flower.setStatus(chooseStatus.getSelectedItem());
      databaseRepo.updateFlower(flower);

      // update display
      displayFlowerRequests();
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
      FlowerEntity flower = databaseRepo.getFlower(id);

      // update assigned employee
      flower.setEmployee(assignedEmployee);
      databaseRepo.updateFlower(flower);

      // update display
      displayFlowerRequests();
    }
  }

  public void displayFlowerRequests() {
    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
    flowerCol.setCellValueFactory(new PropertyValueFactory<>("flowerType"));
    commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
    employeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

    flowerTable.setItems(FXCollections.observableArrayList(databaseRepo.getFlowerMap().values()));
    flowerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  public void displayRoomRequests(List<ConferenceRoomResRequest> CRRRArrayList) {
    roomCol1.setCellValueFactory(new PropertyValueFactory<>("room"));
    dateCol1.setCellValueFactory(new PropertyValueFactory<>("date"));
    startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
    endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    commentCol1.setCellValueFactory(new PropertyValueFactory<>("comment"));
    statusCol1.setCellValueFactory(new PropertyValueFactory<>("status"));

    roomTable.setItems(FXCollections.observableArrayList(CRRRArrayList));
    roomTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }
}
