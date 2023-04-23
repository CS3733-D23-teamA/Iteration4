package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.SignageComponent;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SignageAdminController {
  private DataBaseRepository db = DataBaseRepository.getInstance();

  @FXML private MFXTextField locNameAddInput;
  @FXML private MFXFilterComboBox directionAddInput;
  @FXML private DatePicker dateAddInput;
  @FXML private MFXComboBox<String> chooseSignageRemove;
  @FXML private MFXComboBox<String> chooseSignageModify;
  @FXML private MFXTextField modifyLocNameInput;
  @FXML private MFXTextField modifyDirectionInput;
  @FXML private DatePicker modifyDateInput;
  @FXML private MFXButton addButton;
  @FXML private MFXButton removeButton;
  @FXML private MFXButton modifyButton;

  @FXML private TableView<SignageComponent> signageTableView;
  @FXML private TableColumn<SignageComponent, String> locationNameCol;
  @FXML private TableColumn<SignageComponent, String> directionCol;
  @FXML private TableColumn<SignageComponent, String> dateCol;

  public void initialize() {
    ArrayList<String> allSignageLocationNames = new ArrayList<>();
    for (Map.Entry<String, SignageComponent> entry : db.getSignageMap().entrySet()) {
      SignageComponent signage = entry.getValue();
      allSignageLocationNames.add(signage.getLocationName());
    }

    chooseSignageRemove.getItems().addAll(allSignageLocationNames);
    chooseSignageModify.getItems().addAll(allSignageLocationNames);
    directionAddInput.getItems().addAll("up", "down", "right", "left", "stop right here");

    displaySignages();
    addButton.setDisable(true);
    removeButton.setDisable(true);
    modifyButton.setDisable(true);
  }

  public void displaySignages() {
    locationNameCol.setCellValueFactory(new PropertyValueFactory<>("locationName"));
    directionCol.setCellValueFactory(new PropertyValueFactory<>("direction"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

    signageTableView.setItems(FXCollections.observableArrayList(db.getSignageMap().values()));
    signageTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  public void addSignage() {
    // Date date = dateAddInput.getValue();

    SignageComponent signage =
        new SignageComponent(
            locNameAddInput.getText(),
            directionAddInput.getText(),
            Date.valueOf(dateAddInput.getValue()));
    db.addSignage(signage);
    Navigation.navigate(Screen.ACCOUNT);
  }

  public void removeSignage() {
    SignageComponent signage = db.getSignage(chooseSignageRemove.getSelectedItem());
    db.removeSignage(signage);
    Navigation.navigate(Screen.ACCOUNT);
  }

  public void modifySignage() {
    SignageComponent signage = db.getSignage(chooseSignageModify.getSelectedItem());
    signage.setLocationName(modifyLocNameInput.getText());
    signage.setDirection(modifyDirectionInput.getText());
    // signage.setDirection(modifyDirectionInput.getText()); for date
    db.modifySignage(signage);
    Navigation.navigate(Screen.ACCOUNT);
  }

  public void validateAdd() {
    if (locNameAddInput.getText().isEmpty()
        || directionAddInput.getText().isEmpty()
        || dateAddInput.getValue() == null) {
      removeButton.setDisable(true);
    } else {
      removeButton.setDisable(false);
    }
  }

  public void validateRemove() {
    if (chooseSignageRemove.getSelectedIndex() == -1) {
      removeButton.setDisable(true);
    } else {
      removeButton.setDisable(false);
    }
  }
}
