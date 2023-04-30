package edu.wpi.teamA.controllers.Navigation.Requests;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import edu.wpi.teamA.database.Singletons.CRRRSingleton;
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

public class CRRequestEditController {
  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();
  @FXML private MFXButton updateButton;
  @FXML private MFXTextField nameField;
  @FXML private MFXComboBox<String> roomCombo;
  @FXML private DatePicker datePicker;
  @FXML private MFXComboBox<String> endCombo;
  @FXML private MFXComboBox<String> startCombo;
  @FXML private MFXTextField commentField;

  public void initialize() {
    updateButton.setDisable(false);
    populateCombos();
    populateFields();
  }

  public void populateCombos() {
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

    ArrayList<String> allRooms = new ArrayList<>();
    allRooms.addAll(databaseRepo.filterLocType("CONF"));
    Collections.sort(allRooms);
    roomCombo.getItems().addAll(allRooms);
  }

  public void populateFields() {
    nameField.setText(CRRRSingleton.INSTANCE.getValue().getName());
    commentField.setText(CRRRSingleton.INSTANCE.getValue().getComment());
    roomCombo.setText(CRRRSingleton.INSTANCE.getValue().getRoom());
    endCombo.setText(convertInt(CRRRSingleton.INSTANCE.getValue().getEndTime()));
    datePicker.setValue(CRRRSingleton.INSTANCE.getValue().getDate().toLocalDate());
    startCombo.setText(convertInt(CRRRSingleton.INSTANCE.getValue().getStartTime()));
  }

  public void back() {
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  @FXML
  public void validateButton() {
    if (nameField.getText().isEmpty()
        || datePicker.getValue() == null
        || checkAvailible(
            Date.valueOf(datePicker.getValue()),
            startCombo.getSelectionModel().getSelectedItem(),
            endCombo.getSelectionModel().getSelectedItem(),
            roomCombo.getSelectionModel().getSelectedItem())) {
      updateButton.setDisable(true);
    } else {
      updateButton.setDisable(false);
    }
  }

  public void edit() {
    ConferenceRoomResRequest crrr =
        new ConferenceRoomResRequest(
            CRRRSingleton.INSTANCE.getValue().getId(),
            nameField.getText(),
            roomCombo.getText(),
            Date.valueOf(datePicker.getValue()),
            convertTime(startCombo.getText()),
            convertTime(endCombo.getText()),
            commentField.getText(),
            CRRRSingleton.INSTANCE.getValue().getEmployee(),
            CRRRSingleton.INSTANCE.getValue().getStatus(),
            CRRRSingleton.INSTANCE.getValue().getCreator());
    databaseRepo.updateCRRR(crrr);
    Navigation.navigate(Screen.SERVICE_REQUEST);
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

  public void delete() {
    databaseRepo.deleteCRRR(CRRRSingleton.INSTANCE.getValue());
    Navigation.navigate(Screen.SERVICE_REQUEST);
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
}
