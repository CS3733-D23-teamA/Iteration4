package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Alert;
import edu.wpi.teamA.database.ORMclasses.Employee;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class AlertSettingsController {

  private DataBaseRepository db = DataBaseRepository.getInstance();
  @FXML private MFXTextField nameAddInput;
  @FXML private MFXTextField userAddInput;
  @FXML private MFXTextField passwordAddInput;
  @FXML private MFXComboBox<String> chooseEmployeeRemove;
  @FXML private MFXComboBox<String> chooseEmployeeModify;
  @FXML private MFXTextField modifyNameInput;
  @FXML private MFXTextField modifyPassInput;
  @FXML private MFXButton addButton;
  @FXML private MFXButton removeButton;
  @FXML private MFXButton modifyButton;

  @FXML private TableView<Alert> alertTableView;
  @FXML private TableColumn<Alert, Integer> ticketNumCol;
  @FXML private TableColumn<Alert, String> usernameCol;
  @FXML private TableColumn<Alert, LocalDate> dateCol;
  @FXML private TableColumn<Alert, String> messageCol;


  public void initialize() {
    ArrayList<String> allEmployeeUsernames = new ArrayList<>();
    for (Map.Entry<String, Employee> entry : db.getEmployeeMap().entrySet()) {
      Employee employee = entry.getValue();
      allEmployeeUsernames.add(employee.getUsername());
    }

    chooseEmployeeRemove.getItems().addAll(allEmployeeUsernames);
    chooseEmployeeModify.getItems().addAll(allEmployeeUsernames);

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

  public void addAlert() {
    Employee employee =
        new Employee(nameAddInput.getText(), userAddInput.getText(), passwordAddInput.getText());
    db.addEmployee(employee);
    Navigation.navigate(Screen.ACCOUNT_SETTINGS);
  }

  public void removeAlert() {
    Employee employee = db.getEmployee(chooseEmployeeRemove.getSelectedItem());
    db.removeEmployee(employee);
    Navigation.navigate(Screen.ACCOUNT_SETTINGS);
  }

  public void modifyAlert() {
    Employee employee = db.getEmployee(chooseEmployeeModify.getSelectedItem());
    employee.setName(modifyNameInput.getText());
    employee.setPassword(modifyPassInput.getText());
    db.modifyEmployee(employee);
    Navigation.navigate(Screen.ACCOUNT_SETTINGS);
  }

  public void validateAdd() {
    if (nameAddInput.getText().isEmpty()
        || userAddInput.getText().isEmpty()
        || passwordAddInput.getText().isEmpty()) {
      removeButton.setDisable(true);
    } else {
      removeButton.setDisable(false);
    }
  }

  public void validateRemove() {
    if (chooseEmployeeRemove.getSelectedIndex() == -1) {
      removeButton.setDisable(true);
    } else {
      removeButton.setDisable(false);
    }
  }

  public void validateModify() {
    if (chooseEmployeeModify.getSelectedIndex() == -1
        || modifyNameInput.getText().isEmpty()
        || modifyPassInput.getText().isEmpty()) {
      modifyButton.setDisable(true);
    } else {
      modifyButton.setDisable(false);
    }
  }
}
