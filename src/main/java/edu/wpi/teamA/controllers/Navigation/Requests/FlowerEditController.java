package edu.wpi.teamA.controllers.Navigation.Requests;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Flower;
import edu.wpi.teamA.database.Singletons.FlowerSingleton;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class FlowerEditController {
  @FXML private StackPane infoDisplay;
  @FXML private StackPane cartDisplay;
  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();
  private ServiceRequestEntity entity = App.getServiceRequestEntity();
  @FXML private MFXButton nextButton;
  @FXML private MFXButton updateButton;
  @FXML private MFXTextField nameField;
  @FXML private MFXComboBox<String> roomCombo;
  @FXML private DatePicker datePicker;
  @FXML private MFXComboBox<String> timeCombo;
  @FXML private MFXTextField commentField;

  @FXML private MFXComboBox<String> flowerCombo;
  @FXML private MFXComboBox<Integer> flowerQuantity;
  @FXML private MFXButton addFlower;

  @FXML private TableView<ServiceRequestItem> itemsTable;
  @FXML private TableColumn<ServiceRequestItem, String> itemsCol;
  @FXML private TableColumn<ServiceRequestItem, Integer> quantityCol;

  @FXML private HBox statusBarHBox;
  @FXML private Rectangle newStatusRect;
  @FXML private Rectangle orderBeginStatusRect;
  @FXML private Rectangle pickFlowersStatusRect;
  @FXML private Rectangle flowersReadyStatusRect;
  @FXML private Rectangle deliveredStatusRect;

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
    statusBarHBox.setVisible(true);
    newStatusRect.setVisible(true);
    orderBeginStatusRect.setVisible(true);
    pickFlowersStatusRect.setVisible(true);
    flowersReadyStatusRect.setVisible(true);
    deliveredStatusRect.setVisible(true);
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

    flowerCombo.getItems().addAll("Roses", "Tulips", "Daises", "Sunflowers");
    flowerQuantity.getItems().addAll(-3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  }

  public void populateFields() {
    nameField.setText(FlowerSingleton.INSTANCE.getValue().getName());
    commentField.setText(FlowerSingleton.INSTANCE.getValue().getComment());
    roomCombo.setText(FlowerSingleton.INSTANCE.getValue().getRoom());
    datePicker.setValue(FlowerSingleton.INSTANCE.getValue().getDate().toLocalDate());
    timeCombo.setText(convertInt(FlowerSingleton.INSTANCE.getValue().getTime()));
  }

  public void populateTable() {
    String items = FlowerSingleton.INSTANCE.getValue().getItems();
    String[] parts = items.split(",");
    System.out.println(parts[1]);
    for (String item : parts) {
      if (item.length() > 2) {
        String[] subParts = item.split(" ");
        itemsTable
            .getItems()
            .add(new ServiceRequestItem(subParts[0], Integer.parseInt(subParts[1])));
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
    if (nameField.getText().isEmpty() || datePicker.getValue() == null) {
      nextButton.setDisable(true);
    } else {
      nextButton.setDisable(false);
    }
  }

  @FXML
  public void validateAddFlower() {
    if (flowerCombo.getSelectedIndex() == -1 || flowerQuantity.getSelectedIndex() == -1) {
      addFlower.setDisable(true);
    } else {
      addFlower.setDisable(false);
    }
  }

  @FXML
  public void validateUpdate() {
    updateButton.setDisable(itemsTable.getItems().isEmpty());
  }

  // TODO
  public void update() {
    String items = entity.appendItemsIntoString(itemsTable);
    Flower flower =
        new Flower(
            FlowerSingleton.INSTANCE.getValue().getId(),
            nameField.getText(),
            roomCombo.getText(),
            Date.valueOf(datePicker.getValue()),
            entity.convertTime(timeCombo.getText()),
            items,
            commentField.getText(),
            FlowerSingleton.INSTANCE.getValue().getEmployee(),
            FlowerSingleton.INSTANCE.getValue().getStatus(),
            FlowerSingleton.INSTANCE.getValue().getCreator());
    databaseRepo.updateFlower(flower);
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  public String convertInt(int num) {
    String time = "";

    if (num < 100) {
      time += "00";
    } else {
      time += (num / 100);
    }
    time += ":00";
    return time;
  }

  @FXML
  public void delete() {
    databaseRepo.deleteFlower(FlowerSingleton.INSTANCE.getValue());
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
  public void addFlower() {
    String flower = flowerCombo.getSelectedItem();
    int quantity = flowerQuantity.getSelectedItem();
    entity.addItemsToTable(itemsTable, flower, quantity);
    validateUpdate();
  }
}
