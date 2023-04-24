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
  @FXML private MFXFilterComboBox<String> directionAddInputCombo;
  @FXML private MFXFilterComboBox<Integer> screenAddInputCombo;
  @FXML private DatePicker dateAddInput;
  @FXML private MFXComboBox<String> SignageIDRemoveCombo;
  @FXML private MFXComboBox<String> signageIDModifyCombo;
  @FXML private MFXTextField locNameModifyText;
  @FXML private MFXComboBox<String> directionModifyCombo;
  @FXML private MFXComboBox<Integer> screenModifyCombo;
  @FXML private DatePicker modifyDateInput;
  @FXML private MFXButton addButton;
  @FXML private MFXButton removeButton;
  @FXML private MFXButton modifyButton;

  @FXML private TableView<SignageComponent> signageTableView;
  @FXML private TableColumn<SignageComponent, String> locationNameCol;
  @FXML private TableColumn<SignageComponent, String> directionCol;
  @FXML private TableColumn<SignageComponent, String> dateCol;

  public void initialize() {
    // ArrayList<String> allSignageLocationNames = new ArrayList<>();
    ArrayList<String> allSignageIDs = new ArrayList<>();
    for (Map.Entry<String, SignageComponent> entry : db.getSignageMap().entrySet()) {
      SignageComponent signage = entry.getValue();
      // allSignageLocationNames.add(signage.getLocationName());
      allSignageIDs.add(signage.getSignageID());
    }

    SignageIDRemoveCombo.getItems().addAll(allSignageIDs);
    signageIDModifyCombo.getItems().addAll(allSignageIDs);
    directionAddInputCombo.getItems().addAll("up", "down", "right", "left", "stop right here");
    directionModifyCombo.getItems().addAll("up", "down", "right", "left", "stop right here");
    screenAddInputCombo.getItems().addAll(1, 2);
    screenModifyCombo.getItems().addAll(1, 2);

    displaySignages();
    addButton.setDisable(true);
    removeButton.setDisable(true);
    modifyButton.setDisable(true);
    // validateAdd();
    // validateModify();
    // validateRemove();
  }

  public void displaySignages() {
    locationNameCol.setCellValueFactory(new PropertyValueFactory<>("locationName"));
    directionCol.setCellValueFactory(new PropertyValueFactory<>("direction"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

    signageTableView.setItems(FXCollections.observableArrayList(db.getSignageMap().values()));
    signageTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  public void addSignage() {
    String signageID = locNameAddInput.getText() + Date.valueOf(dateAddInput.getValue()).toString();
    SignageComponent signage =
        new SignageComponent(
            locNameAddInput.getText(),
            directionAddInputCombo.getText(),
            Date.valueOf(dateAddInput.getValue()),
            screenAddInputCombo.getValue(),
            signageID);
    db.addSignage(signage);
    Navigation.navigate(Screen.SIGNAGE_ADMIN);
  }

  public void removeSignage() {
    SignageComponent signage = db.getSignage(SignageIDRemoveCombo.getSelectedItem());
    db.removeSignage(signage);
    Navigation.navigate(Screen.SIGNAGE_ADMIN);
  }

  public void modifySignage() {
    SignageComponent signage = db.getSignage(signageIDModifyCombo.getSelectedItem());
    if (!locNameModifyText.getText().isEmpty()) {
      signage.setLocationName(locNameModifyText.getText());
    }
    if (directionModifyCombo.getSelectedIndex() != -1) {
      signage.setDirection(directionModifyCombo.getText());
    }
    if (modifyDateInput.getValue() != null) {
      signage.setDate(Date.valueOf(modifyDateInput.getValue()));
    }
    signage.setSignageID(signage.getLocationName() + signage.getDate().toString());
    // signage.setDirection(modifyDirectionInput.getText()); for date
    db.modifySignage(signage);
    Navigation.navigate(Screen.SIGNAGE_ADMIN);
  }

  public void validateAdd() {
    if (locNameAddInput.getText().isEmpty()
        || directionAddInputCombo.getText().isEmpty()
        || dateAddInput.getValue() == null) {
      addButton.setDisable(true);
    } else {
      addButton.setDisable(false);
    }
  }

  public void validateRemove() {
    if (SignageIDRemoveCombo.getSelectedIndex() == -1) {
      removeButton.setDisable(true);
    } else {
      removeButton.setDisable(false);
    }
  }

  public void validateModify() {
    if (signageIDModifyCombo.getSelectedIndex() == -1
        || (locNameModifyText.getText().isEmpty()
            && directionModifyCombo.getSelectedIndex() == -1
            && modifyDateInput.getValue() == null)) {
      modifyButton.setDisable(true);
    } else {
      modifyButton.setDisable(false);
    }
  }
}
