package edu.wpi.teamA.controllers.Navigation.Requests;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.FurnitureRequest;
import edu.wpi.teamA.database.Singletons.FurnitureSingleton;
import edu.wpi.teamA.entities.ServiceRequestEntity;
import edu.wpi.teamA.entities.ServiceRequestItem;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

public class FurnitureEditController {
  @FXML private StackPane infoDisplay;
  @FXML private StackPane cartDisplay;
  private final DataBaseRepository databaseRepo = DataBaseRepository.getInstance();
  private final ServiceRequestEntity entity = App.getServiceRequestEntity();

  @FXML private MFXButton nextButton;
  @FXML private MFXButton updateButton;
  @FXML private MFXTextField nameField;
  @FXML private MFXComboBox<String> roomCombo;
  @FXML private DatePicker datePicker;
  @FXML private MFXComboBox<String> timeCombo;
  @FXML private MFXTextField commentField;

  @FXML private MFXComboBox<String> furnitureCombo;
  @FXML private MFXComboBox<Integer> furnitureQuantity;
  @FXML private MFXButton addFurniture;

  @FXML private TableView<ServiceRequestItem> itemsTable;
  @FXML private TableColumn<ServiceRequestItem, String> itemsCol;
  @FXML private TableColumn<ServiceRequestItem, Integer> quantityCol;

  public void initialize() {
    cartDisplay.setDisable(true);
    cartDisplay.setVisible(false);
    infoDisplay.setDisable(false);
    infoDisplay.setVisible(true);

    itemsCol.setCellValueFactory(new PropertyValueFactory<>("item"));
    quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

    nextButton.setDisable(false);
    updateButton.setDisable(false);
    populateCombos();
    populateFields();
    populateTable();
  }

  public void populateCombos() {
    timeCombo
        .getItems()
        .addAll(
            "00:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00",
            "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
            "19:00", "20:00", "21:00", "22:00", "23:00");

    ArrayList<String> allRooms = new ArrayList<>();
    allRooms.addAll(databaseRepo.filterLocType("CONF"));
    allRooms.addAll(databaseRepo.filterLocType("INFO"));
    allRooms.addAll(databaseRepo.filterLocType("LABS"));
    Collections.sort(allRooms);
    roomCombo.getItems().addAll(allRooms);

    furnitureCombo.getItems().addAll("Arm Chair", "Couch", "Coffee Table");
    furnitureQuantity.getItems().addAll(-3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  }

  public void populateFields() {
    nameField.setText(FurnitureSingleton.INSTANCE.getValue().getName());
    commentField.setText(FurnitureSingleton.INSTANCE.getValue().getComment());
    roomCombo.setText(FurnitureSingleton.INSTANCE.getValue().getRoom());
    datePicker.setValue(FurnitureSingleton.INSTANCE.getValue().getDate().toLocalDate());
    timeCombo.setText(entity.convertInt(FurnitureSingleton.INSTANCE.getValue().getTime()));
  }

  public void populateTable() {
    String items = FurnitureSingleton.INSTANCE.getValue().getItems();
    String[] parts = items.split(",");
    for (String item : parts) {
      if (item.length() > 2) {
        String[] subParts = item.split(" ");
        String name = "";
        for (int i = 0; i < subParts.length - 1; i++) {
          if (!subParts[i].equals(" ")) {
            name = name.concat(subParts[i] + " ");
          }
        }
        itemsTable
            .getItems()
            .add(
                new ServiceRequestItem(
                    name.substring(0, name.length() - 1),
                    Integer.parseInt(subParts[subParts.length - 1])));
      }
    }
  }

  public void back() {
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  public void backFirstPage() {
    cartDisplay.setDisable(true);
    cartDisplay.setVisible(false);
    infoDisplay.setDisable(false);
    infoDisplay.setVisible(true);
  }

  @FXML
  public void validateNext() {
    nextButton.setDisable(nameField.getText().isEmpty() || datePicker.getValue() == null);
  }

  @FXML
  public void validateAddFurniture() {
    addFurniture.setDisable(
        furnitureCombo.getSelectedIndex() == -1 || furnitureQuantity.getSelectedIndex() == -1);
  }

  @FXML
  public void validateUpdate() {
    updateButton.setDisable(itemsTable.getItems().isEmpty());
  }

  public void update() {
    String items = entity.appendItemsIntoString(itemsTable);
    FurnitureRequest furniture =
        new FurnitureRequest(
            FurnitureSingleton.INSTANCE.getValue().getId(),
            nameField.getText(),
            roomCombo.getText(),
            Date.valueOf(datePicker.getValue()),
            entity.convertTime(timeCombo.getText()),
            items,
            commentField.getText(),
            FurnitureSingleton.INSTANCE.getValue().getEmployee(),
            FurnitureSingleton.INSTANCE.getValue().getStatus(),
            FurnitureSingleton.INSTANCE.getValue().getCreator());
    databaseRepo.updateFurniture(furniture);
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  public void delete() {
    databaseRepo.deleteFurniture(FurnitureSingleton.INSTANCE.getValue());
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  @FXML
  public void next() {
    infoDisplay.setDisable(true);
    infoDisplay.setVisible(false);
    cartDisplay.setDisable(false);
    cartDisplay.setVisible(true);
  }

  @FXML
  public void addFurniture() {
    String furniture = furnitureCombo.getSelectedItem();
    int quantity = furnitureQuantity.getSelectedItem();
    entity.addItemsToTable(itemsTable, furniture, quantity);
    validateUpdate();
  }
}
