package edu.wpi.teamA.controllers.Navigation.Requests;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Meal;
import edu.wpi.teamA.database.Singletons.MealSingleton;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class MealEditController {
  @FXML private AnchorPane infoDisplay;
  @FXML private AnchorPane cartDisplay;
  private final DataBaseRepository databaseRepo = DataBaseRepository.getInstance();
  private final ServiceRequestEntity entity = App.getServiceRequestEntity();

  @FXML private MFXButton nextButton;
  @FXML private MFXButton updateButton;
  @FXML private MFXTextField nameField;
  @FXML private MFXComboBox<String> roomCombo;
  @FXML private DatePicker datePicker;
  @FXML private MFXComboBox<String> timeCombo;
  @FXML private MFXTextField commentField;

  @FXML private MFXComboBox<String> drinkCombo;
  @FXML private MFXComboBox<Integer> drinkQuantity;
  @FXML private MFXComboBox<String> foodCombo;
  @FXML private MFXComboBox<Integer> foodQuantity;
  @FXML private MFXButton addDrink;
  @FXML private MFXButton addFood;

  @FXML private TableView<ServiceRequestItem> itemsTable;
  @FXML private TableColumn<ServiceRequestItem, String> itemsCol;
  @FXML private TableColumn<ServiceRequestItem, Integer> quantityCol;

  @FXML private HBox statusBarHBox;
  @FXML private SVGPath newStatus;
  @FXML private VBox assignedStatus;
  @FXML private VBox inProgressStatus;
  @FXML private VBox inDeliveryStatus;
  @FXML private SVGPath doneStatus;

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
    updateProgressBar();
  }

  public void updateProgressBar() {
    if (MealSingleton.INSTANCE.getValue().getStatus().equals("new")) {
      System.out.println("new status");
      newStatus.setFill(Color.web("0x012d5a"));

    } else if (MealSingleton.INSTANCE.getValue().getStatus().equals("assigned")) {
      System.out.println("in progress status");
      newStatus.setFill(Color.web("0x012d5a"));
      assignedStatus.setStyle("-fx-background-color: #012D5A");

      doneStatus.setFill(Color.web("0x98aabc"));
    } else if (MealSingleton.INSTANCE.getValue().getStatus().equals("in progress")) {
      System.out.println("in preparation status");
      newStatus.setFill(Color.web("0x012d5a"));
      assignedStatus.setStyle("-fx-background-color: #012D5A");
      inProgressStatus.setStyle("-fx-background-color: #012D5A");
    } else if (MealSingleton.INSTANCE.getValue().getStatus().equals("in delivery")) {
      System.out.println("meal prepared");
      newStatus.setFill(Color.web("0x012d5a"));
      assignedStatus.setStyle("-fx-background-color: #012D5A");
      inProgressStatus.setStyle("-fx-background-color: #012D5A");
      inDeliveryStatus.setStyle("-fx-background-color: #012D5A");
    } else if (MealSingleton.INSTANCE.getValue().getStatus().equals("done")) {
      System.out.println("delivered status");
      newStatus.setFill(Color.web("0x012d5a"));
      assignedStatus.setStyle("-fx-background-color: #012D5A");
      inProgressStatus.setStyle("-fx-background-color: #012D5A");
      inDeliveryStatus.setStyle("-fx-background-color: #012D5A");
      doneStatus.setStyle("-fx-fill: #012D5A");
    }
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

    drinkCombo.getItems().addAll("Lemonade", "Coca-Cola", "Diet Coca-Cola", "Root Beer", "Water");
    foodCombo.getItems().addAll("Burger", "Cheeseburger", "Hot Dog");

    drinkQuantity.getItems().addAll(-3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    foodQuantity.getItems().addAll(-3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  }

  public void populateFields() {
    nameField.setText(MealSingleton.INSTANCE.getValue().getName());
    commentField.setText(MealSingleton.INSTANCE.getValue().getComment());
    roomCombo.setText(MealSingleton.INSTANCE.getValue().getRoom());
    datePicker.setValue(MealSingleton.INSTANCE.getValue().getDate().toLocalDate());
    timeCombo.setText(entity.convertInt(MealSingleton.INSTANCE.getValue().getTime()));
  }

  public void populateTable() {
    String items = MealSingleton.INSTANCE.getValue().getItems();
    String[] parts = items.split(",");
    for (String item : parts) {
      if (item.length() > 2) {
        String[] subParts = item.split(" ");
        String name = "";
        for (int i = 0; i < subParts.length - 1; i++) {
          name = name.concat(subParts[i] + " ");
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
  public void validateAddDrink() {
    addDrink.setDisable(
        drinkCombo.getSelectedIndex() == -1 || drinkQuantity.getSelectedIndex() == -1);
  }

  @FXML
  public void validateAddFood() {
    addFood.setDisable(foodCombo.getSelectedIndex() == -1 || foodQuantity.getSelectedIndex() == -1);
  }

  public void validateUpdate() {
    updateButton.setDisable(itemsTable.getItems().isEmpty());
  }

  public void update() {
    String items = entity.appendItemsIntoString(itemsTable);
    Meal meal =
        new Meal(
            MealSingleton.INSTANCE.getValue().getId(),
            nameField.getText(),
            roomCombo.getText(),
            Date.valueOf(datePicker.getValue()),
            entity.convertTime(timeCombo.getText()),
            items,
            commentField.getText(),
            MealSingleton.INSTANCE.getValue().getEmployee(),
            MealSingleton.INSTANCE.getValue().getStatus(),
            MealSingleton.INSTANCE.getValue().getCreator());
    databaseRepo.updateMeal(meal);
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  public void delete() {
    databaseRepo.deleteMeal(MealSingleton.INSTANCE.getValue());
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
  public void addDrink() {
    String drink = drinkCombo.getSelectedItem();
    int quantity = drinkQuantity.getSelectedItem();
    entity.addItemsToTable(itemsTable, drink, quantity);
    validateUpdate();
  }

  @FXML
  public void addFood() {
    String food = foodCombo.getSelectedItem();
    int quantity = foodQuantity.getSelectedItem();
    entity.addItemsToTable(itemsTable, food, quantity);
    validateUpdate();
  }
}
