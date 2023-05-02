package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Alert;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AlertSettingsController {

  private DataBaseRepository db = DataBaseRepository.getInstance();

  @FXML private DatePicker dateAddInput;
  @FXML private MFXTextField messageAddInputText;
  @FXML private MFXComboBox<Integer> TicketNumModifyCombo;
  @FXML private MFXComboBox<Integer> TicketNumRemoveCombo;
  @FXML private DatePicker modifyDateInput;
  @FXML private MFXTextField messageModifyText;
  @FXML private MFXButton addButton;
  @FXML private MFXButton removeButton;
  @FXML private MFXButton modifyButton;

  @FXML private TableView<Alert> alertTableView;
  @FXML private TableColumn<Alert, Integer> ticketNumCol;
  @FXML private TableColumn<Alert, String> usernameCol;
  @FXML private TableColumn<Alert, LocalDate> dateCol;
  @FXML private TableColumn<Alert, String> messageCol;

  public void initialize() {

    ArrayList<Integer> allTicketNum = new ArrayList<>();
    for (Map.Entry<Integer, Alert> entry : db.getAlertMap().entrySet()) {
      Alert alert = entry.getValue();

      allTicketNum.add(alert.getTicketNum());
    }

    TicketNumRemoveCombo.getItems().addAll(allTicketNum);
    TicketNumModifyCombo.getItems().addAll(allTicketNum);

    displayAlerts();
    addButton.setDisable(true);
    removeButton.setDisable(true);
    modifyButton.setDisable(true);
  }

  public void displayAlerts() {
    ticketNumCol.setCellValueFactory(new PropertyValueFactory<>("ticketNum"));
    usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

    alertTableView.setItems(FXCollections.observableArrayList(db.getAlertMap().values()));
    alertTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  @FXML
  public void addAlert() {
    String currentUsername = AccountSingleton.INSTANCE.getValue().getUserName();
    Alert alert =
        new Alert(
            db.getNextAlertTicket(),
            currentUsername,
            Date.valueOf(dateAddInput.getValue()).toLocalDate(),
            messageAddInputText.getText());
    db.addAlert(alert);
    Navigation.navigate(Screen.ALERT_SETTINGS);
  }

  @FXML
  public void removeAlert() {
    Alert alert = db.getAlert(TicketNumRemoveCombo.getSelectedItem());
    db.removeAlert(alert);
    Navigation.navigate(Screen.ALERT_SETTINGS);
  }

  @FXML
  public void modifyAlert() {
    Alert alert = db.getAlert(TicketNumModifyCombo.getSelectedItem());
    alert.setMessage(messageModifyText.getText());
    alert.setDate(Date.valueOf(modifyDateInput.getValue()).toLocalDate());
    db.modifyAlert(alert);
    Navigation.navigate(Screen.ALERT_SETTINGS);
  }

  @FXML
  public void validateAdd() {
    if (dateAddInput.getValue() == null || messageAddInputText.getText().isEmpty()) {
      addButton.setDisable(true);
    } else {
      addButton.setDisable(false);
    }
  }

  @FXML
  public void validateRemove() {
    if (TicketNumRemoveCombo.getSelectedIndex() == -1) {
      removeButton.setDisable(true);
    } else {
      removeButton.setDisable(false);
    }
  }

  @FXML
  public void validateModify() {
    if (TicketNumModifyCombo.getSelectedItem() == -1) {
      modifyButton.setDisable(true);
    } else if (modifyDateInput.getValue() == null || messageModifyText.getText().isEmpty()) {
      modifyButton.setDisable(true);
    } else {
      modifyButton.setDisable(false);
    }
  }
}
