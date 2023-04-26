package edu.wpi.teamA.controllers.Navigation.Requests;

import edu.wpi.teamA.App;
import edu.wpi.teamA.controllers.Navigation.PageController;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
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
        || roomCombo.getSelectedIndex() == -1
        || checkAvailible(
            Date.valueOf(datePicker.getValue()),
            startCombo.getSelectionModel().getSelectedItem(),
            endCombo.getSelectionModel().getSelectedItem(),
            roomCombo.getSelectionModel().getSelectedItem())) {
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
              "new",
              AccountSingleton.INSTANCE.getValue().getUserName());

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

  public boolean checkAvailible(Date date, String st, String et, String room) {
    boolean taken = false;
    int startInt = convertTime(st);
    int endInt = convertTime(et);
    ArrayList<ConferenceRoomResRequest> rr = databaseRepo.filterDateCRRR(date);
    for (ConferenceRoomResRequest cr : rr) {
      if (cr.getRoom().equals(room)) {
        if ((startInt < cr.getStartTime() && endInt > cr.getStartTime())
            || (startInt < cr.getEndTime() && endInt > cr.getEndTime())
            || (startInt >= cr.getStartTime() && endInt <= cr.getEndTime())) {
          taken = true;
        }
      }
    }
    if (startInt >= endInt) {
      taken = true;
    }
    return taken;
  }

  public int convertTime(String time) {
    int num;
    String newString;
    int length = time.length();
    if (time.equals("00:00")) {
      return 0;
    } else if (length == 4) {
      newString = time.charAt(0) + time.substring(2);
    } else {
      newString = time.substring(0, 2) + time.substring(3);
    }
    num = Integer.parseInt(newString);
    return num;
  }

  public void cal() {
    Navigation.navigate(Screen.CR_REQUEST);
  }
}
