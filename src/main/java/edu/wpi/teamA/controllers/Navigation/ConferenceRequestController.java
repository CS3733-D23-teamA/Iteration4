package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DAOImps.CRRRDAOImp;
import edu.wpi.teamA.database.DAOImps.LocNameDAOImp;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
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

public class ConferenceRequestController extends PageController implements IServiceController {
  @FXML MFXButton submitButton;
  @FXML MFXTextField nameField;
  @FXML MFXComboBox roomCombo;
  @FXML DatePicker datePicker;
  @FXML MFXComboBox startCombo;
  @FXML MFXComboBox endCombo;
  @FXML MFXTextField commentField;

  LocNameDAOImp locs = new LocNameDAOImp();

  @Override
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
    ArrayList<String> allRooms = new ArrayList<>();
    allRooms.addAll(locs.filterLocType("CONF"));
    Collections.sort(allRooms);
    roomCombo.getItems().addAll(allRooms);
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

  public void submit() {
    System.out.println("Submit button clicked");
    try {
      ConferenceRoomResRequest crrr =
          new ConferenceRoomResRequest(
              nameField.getText(),
              roomCombo.getText(),
              Date.valueOf(datePicker.getValue()),
              convertTime(startCombo.getText()),
              convertTime(endCombo.getText()),
              commentField.getText(),
              "new");
      System.out.println("ConferenceRoomResRequest created: " + crrr.toString());

      CRRRDAOImp cd = new CRRRDAOImp();
      cd.addCRRR(crrr);
      System.out.println("ConferenceRoomResRequest added");

      clear();
      System.out.println("Fields cleared");
    } catch (Exception e) {
      e.printStackTrace();
    }
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
}
