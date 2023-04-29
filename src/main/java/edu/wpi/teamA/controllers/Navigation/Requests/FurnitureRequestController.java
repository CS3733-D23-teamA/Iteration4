package edu.wpi.teamA.controllers.Navigation.Requests;

import edu.wpi.teamA.App;
import edu.wpi.teamA.controllers.Navigation.PageController;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.FurnitureRequest;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.entities.ServiceRequestEntity;
import edu.wpi.teamA.entities.ServiceRequestItem;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import java.sql.Date;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

public class FurnitureRequestController extends PageController implements IServiceController {

  @FXML private StackPane infoDisplay;
  @FXML private StackPane cartDisplay;

  @FXML private MFXButton nextButton;
  @FXML private MFXTextField nameField;
  @FXML private MFXComboBox<String> roomComboBox;
  @FXML private DatePicker datePicker;
  @FXML private MFXComboBox<String> timeCombo;
  @FXML private MFXTextField commentField;
  @FXML private MFXGenericDialog confirmationDialog;

  @FXML private MFXComboBox<String> furnitureCombo;
  @FXML private MFXComboBox<Integer> furnitureQuantity;

  @FXML private TableView<ServiceRequestItem> itemsTable;
  @FXML private TableColumn<ServiceRequestItem, String> itemsCol;
  @FXML private TableColumn<ServiceRequestItem, Integer> quantityCol;

  @FXML private MFXButton backButton;
  @FXML private MFXButton submitButton;
  @FXML private MFXButton addFurniture;

  private final ServiceRequestEntity entity = App.getServiceRequestEntity();
  private final DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

  public void initialize() {
    cartDisplay.setDisable(true);
    cartDisplay.setVisible(false);
    infoDisplay.setDisable(false);
    infoDisplay.setVisible(true);

    itemsCol.setCellValueFactory(new PropertyValueFactory<>("item"));
    quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

    furnitureCombo
        .getItems()
        .addAll(
            "Arm Chair",
            "Couch",
            "Coffee Table",
            "Massage Table",
            "Plastic chair",
            "Circular Table",
            "Bean bag chair",
            "Step Stool",
            "Rocking Chair",
            "Garden furniture",
            "Chaise longue",
            "Console Table",
            "Cupboard",
            "Garden furniture");
    timeCombo
        .getItems()
        .addAll(
            "00:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00",
            "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
            "19:00", "20:00", "21:00", "22:00", "23:00");
    ArrayList<String> rooms = new ArrayList<>();
    rooms.addAll(databaseRepo.filterLocType("BATH"));
    rooms.addAll(databaseRepo.filterLocType("CONF"));
    rooms.addAll(databaseRepo.filterLocType("DEPT"));
    rooms.addAll(databaseRepo.filterLocType("LABS"));
    rooms.addAll(databaseRepo.filterLocType("REST"));
    roomComboBox.getItems().addAll(rooms);

    furnitureQuantity.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    confirmationDialog.setVisible(false);
    confirmationDialog.setDisable(true);
    confirmationDialog.setOnClose(
        event -> {
          confirmationDialog.setVisible(false);
          confirmationDialog.setDisable(true);
        });
  }

  @FXML
  public void validateNext() {
    if (nameField.getText().isEmpty()
        || datePicker.getValue() == null
        || timeCombo.getSelectedIndex() == -1
        || roomComboBox.getSelectedIndex() == -1) {
      nextButton.setDisable(true);
    } else {
      nextButton.setDisable(false);
    }
  }

  @FXML
  public void validateAddFurniture() {
    if (furnitureCombo.getSelectedIndex() == -1 || furnitureCombo.getSelectedIndex() == -1) {
      addFurniture.setDisable(true);
    } else {
      addFurniture.setDisable(false);
    }
  }

  // TODO make validateSubmitButton
  @FXML
  public void validateButton() {
    submitButton.setDisable(itemsTable.getItems().isEmpty());
  }

  @FXML
  public void clear() {
    submitButton.setDisable(true);
    nameField.clear();
    roomComboBox.clear();
    commentField.clear();
    timeCombo.getSelectionModel().clearSelection();
    datePicker.setValue(null);

    furnitureCombo.getSelectionModel().clearSelection();
    furnitureQuantity.getSelectionModel().clearSelection();
    itemsTable.getItems().clear();
  }

  @FXML
  public void next() {
    infoDisplay.setDisable(true);
    infoDisplay.setVisible(false);
    cartDisplay.setDisable(false);
    cartDisplay.setVisible(true);
  }

  @FXML
  public void back() {
    cartDisplay.setDisable(true);
    cartDisplay.setVisible(false);
    infoDisplay.setDisable(false);
    infoDisplay.setVisible(true);
  }

  @FXML
  public void addFurniture() {
    String furniture = furnitureCombo.getSelectedItem();
    int quantity = furnitureQuantity.getSelectedItem();
    entity.addItemsToTable(itemsTable, furniture, quantity);
    validateButton();
  }

  @FXML
  public void submit() {
    String items = entity.appendItemsIntoString(itemsTable);
    FurnitureRequest furniture =
        new FurnitureRequest(
            databaseRepo.getNextFurnitureID(),
            nameField.getText(),
            roomComboBox.getText(),
            Date.valueOf(datePicker.getValue()),
            entity.convertTime(timeCombo.getText()),
            items,
            commentField.getText(),
            "not assigned",
            "new",
            AccountSingleton.INSTANCE.getValue().getUserName());
    databaseRepo.addFurniture(furniture);
    clear();
    back();
    confirmationDialog.setVisible(true);
    confirmationDialog.setDisable(false);
  }

  @FXML
  public void closeConfirmation() {
    confirmationDialog.setVisible(false);
    confirmationDialog.setDisable(true);
  }
}
