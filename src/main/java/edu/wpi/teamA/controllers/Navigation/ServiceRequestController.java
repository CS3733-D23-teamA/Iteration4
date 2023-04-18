package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import edu.wpi.teamA.database.ORMclasses.FlowerEntity;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ServiceRequestController extends PageController {

  private DataBaseRepository databaseRepo = new DataBaseRepository();
  // private FlowerDAOImpl fdao = new FlowerDAOImpl();

  @FXML private MFXComboBox<String> chooseServiceRequestEmployee;

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
    displayFlowerRequests(databaseRepo.getAllFlowers());
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
    List<FlowerEntity> flowerRequests = databaseRepo.getAllFlowers();
    for (int i = 0; i < flowerRequests.size(); i++) {
      allServiceRequests.add("Flower Request " + flowerRequests.get(i).getId());
    }
    chooseServiceRequestEmployee.getItems().addAll(allServiceRequests);
  }

  public void displayFlowerRequests(List<FlowerEntity> flowerEntityArrayList) {
    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
    flowerCol.setCellValueFactory(new PropertyValueFactory<>("flowerType"));
    commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
    employeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

    flowerTable.setItems(FXCollections.observableArrayList(flowerEntityArrayList));
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
