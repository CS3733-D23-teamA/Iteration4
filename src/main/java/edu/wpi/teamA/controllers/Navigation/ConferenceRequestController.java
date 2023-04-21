package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import edu.wpi.teamA.entities.ServiceRequestEntity;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

public class ConferenceRequestController extends PageController implements IServiceController {
  @FXML private MFXButton submitButton;
  @FXML private MFXTextField nameField;
  @FXML private MFXComboBox<String> roomCombo;
  @FXML private DatePicker datePicker;
  @FXML private MFXComboBox<String> startCombo;
  @FXML private MFXComboBox<String> endCombo;
  @FXML private MFXTextField commentField;
  @FXML private MFXGenericDialog confirmationDialog;

  private final ServiceRequestEntity entity = App.getServiceRequestEntity();
  private final DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

  public void initialize() {
    startCombo
        .getItems()
        .addAll(
            "00:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00",
            "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
            "19:00", "20:00", "21:00", "22:00", "23:00");
    endCombo
        .getItems()
        .addAll(
            "00:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00",
            "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
            "19:00", "20:00", "21:00", "22:00", "23:00");
    ArrayList<String> allRooms = new ArrayList<>(databaseRepo.filterLocType("CONF"));
    Collections.sort(allRooms);
    roomCombo.getItems().addAll(allRooms);
    confirmationDialog.setVisible(false);
    confirmationDialog.setDisable(true);
    confirmationDialog.setOnClose(
        event -> {
          confirmationDialog.setVisible(false);
          confirmationDialog.setDisable(true);
        });
  }

  @FXML
  public void validateButton() {
    if (nameField.getText().isEmpty()
        || datePicker.getValue() == null
        || startCombo.getSelectedIndex() == -1
        || endCombo.getSelectedIndex() == -1
        || roomCombo.getSelectedIndex() == -1) {
      submitButton.setDisable(true);
    } else {
      try {
        submitButton.setDisable(false);
      } catch (NumberFormatException error) {
        submitButton.setDisable(true);
      }
    }
  }

  @Override
  public void back() {
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  public void clear() {
    submitButton.setDisable(true);
    nameField.clear();
    roomCombo.getSelectionModel().clearSelection();
    commentField.clear();
    startCombo.getSelectionModel().clearSelection();
    endCombo.getSelectionModel().clearSelection();
    datePicker.setValue(null);
  }

  public void submit() {
    System.out.println("Submit button clicked");
    try {
      ConferenceRoomResRequest crrr =
          new ConferenceRoomResRequest(
              databaseRepo.getNextCRRRID(),
              nameField.getText(),
              roomCombo.getText(),
              Date.valueOf(datePicker.getValue()),
              entity.convertTime(startCombo.getText()),
              entity.convertTime(endCombo.getText()),
              commentField.getText(),
              "not assigned",
              "new");

      databaseRepo.addCRRR(crrr);
      clear();
      confirmationDialog.setVisible(true);
      confirmationDialog.setDisable(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void closeConfirmation() {
    confirmationDialog.setVisible(false);
    confirmationDialog.setDisable(true);
  }
}
