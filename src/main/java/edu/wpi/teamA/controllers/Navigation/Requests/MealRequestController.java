package edu.wpi.teamA.controllers.Navigation.Requests;

import edu.wpi.teamA.App;
import edu.wpi.teamA.controllers.Navigation.PageController;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Meal;
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

public class MealRequestController extends PageController implements IServiceController {
  @FXML private StackPane infoDisplay;
  @FXML private StackPane cartDisplay;

  @FXML private MFXButton nextButton;
  @FXML private MFXTextField nameField;
  @FXML private MFXComboBox<String> roomCombo;
  @FXML private DatePicker datePicker;
  @FXML private MFXComboBox<String> timeCombo;
  @FXML private MFXTextField commentField;
  @FXML private MFXGenericDialog confirmationDialog;

  @FXML private MFXComboBox<String> drinkCombo;
  @FXML private MFXComboBox<Integer> drinkQuantity;
  @FXML private MFXComboBox<String> foodCombo;
  @FXML private MFXComboBox<Integer> foodQuantity;

  @FXML private TableView<ServiceRequestItem> itemsTable;
  @FXML private TableColumn<ServiceRequestItem, String> itemsCol;
  @FXML private TableColumn<ServiceRequestItem, Integer> quantityCol;

  @FXML private MFXButton backButton;
  @FXML private MFXButton submitButton;
  @FXML private MFXButton addDrink;
  @FXML private MFXButton addFood;

  private final ServiceRequestEntity entity = App.getServiceRequestEntity();
  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

  public void initialize() {
    cartDisplay.setDisable(true);
    cartDisplay.setVisible(false);
    infoDisplay.setDisable(false);
    infoDisplay.setVisible(true);

    itemsCol.setCellValueFactory(new PropertyValueFactory<>("item"));
    quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

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
    roomCombo.getItems().addAll(allRooms);

    drinkCombo.getItems().addAll("Lemonade", "Coca-Cola", "Diet Coca-Cola", "Root Beer", "Water");
    foodCombo.getItems().addAll("Burger", "Cheeseburger", "Hot Dog");

    drinkQuantity.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    foodQuantity.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

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
        || roomCombo.getSelectedIndex() == -1) {
      nextButton.setDisable(true);
    } else {
      nextButton.setDisable(false);
    }
  }

  @FXML
  public void validateAddDrink() {
    if (drinkCombo.getSelectedIndex() == -1 || drinkQuantity.getSelectedIndex() == -1) {
      addDrink.setDisable(true);
    } else {
      addDrink.setDisable(false);
    }
  }

  @FXML
  public void validateAddFood() {
    if (foodCombo.getSelectedIndex() == -1 || foodQuantity.getSelectedIndex() == -1) {
      addFood.setDisable(true);
    } else {
      addFood.setDisable(false);
    }
  }

  @FXML
  public void clear() {
    submitButton.setDisable(true);
    nameField.clear();
    roomCombo.getSelectionModel().clearSelection();
    commentField.clear();
    timeCombo.getSelectionModel().clearSelection();
    datePicker.setValue(null);
    drinkCombo.getSelectionModel().clearSelection();
    drinkQuantity.getSelectionModel().clearSelection();
    foodCombo.getSelectionModel().clearSelection();
    foodQuantity.getSelectionModel().clearSelection();
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
  public void addDrink() {
    String drink = drinkCombo.getSelectedItem();
    int quantity = drinkQuantity.getSelectedItem();

    ServiceRequestItem item = entity.tableContainsItem(itemsTable, drink);
    if (item == null) {
      itemsTable.getItems().add(new ServiceRequestItem(drink, quantity));
    } else {
      int prevQuantity = item.getQuantity();
      item.setQuantity(prevQuantity + quantity);
      itemsTable.getItems().remove(item);
      itemsTable.getItems().add(item);
    }
    validateButton();
  }

  @FXML
  public void addFood() {
    String food = foodCombo.getSelectedItem();
    int quantity = foodQuantity.getSelectedItem();

    ServiceRequestItem item = entity.tableContainsItem(itemsTable, food);
    if (item == null) {
      itemsTable.getItems().add(new ServiceRequestItem(food, quantity));
    } else {
      int prevQuantity = item.getQuantity();
      item.setQuantity(prevQuantity + quantity);
      itemsTable.getItems().remove(item);
      itemsTable.getItems().add(item);
    }
    validateButton();
  }

  // TODO change to validate submit
  public void validateButton() {
    submitButton.setDisable(itemsTable.getItems().isEmpty());
  }

  @FXML
  public void submit() {
    StringBuilder items = new StringBuilder();
    for (ServiceRequestItem item : itemsTable.getItems()) {
      items.append(item.getItem()).append(" ").append(item.getQuantity()).append(", ");
    }

    Meal meal =
        new Meal(
            databaseRepo.getNextMealID(),
            nameField.getText(),
            roomCombo.getText(),
            Date.valueOf(datePicker.getValue()),
            entity.convertTime(timeCombo.getText()),
            items.toString(),
            commentField.getText(),
            "not assigned",
            "new",
            AccountSingleton.INSTANCE1.getValue().getUserName());
    databaseRepo.addMeal(meal);
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
