package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.AccountSingleton;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import edu.wpi.teamA.database.ORMclasses.Employee;
import edu.wpi.teamA.database.ORMclasses.Flower;
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
  // private FlowerDAOImpl fdao = new FlowerDAOImpl();
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

  // private CRRRDAOImp cdao = new CRRRDAOImp();
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

  @FXML
  public void initialize() {
    displayFlowerRequests();
    displayCRRRRequests();

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

    // TODO ADMIN ONLY
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
}
