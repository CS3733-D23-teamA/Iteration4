package edu.wpi.teamA.controllers.Navigation.Requests;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.database.Singletons.CalendarSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import java.sql.Date;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.shape.Rectangle;

public class ConferenceCalendarController {
  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();
  @FXML private MFXButton submitButton;
  @FXML private MFXGenericDialog confirmationDialog;
  @FXML private MFXTextField nameField;
  @FXML private MFXComboBox roomCombo;
  @FXML private DatePicker datePicker;
  @FXML private MFXComboBox endCombo;
  @FXML private MFXComboBox startCombo;
  @FXML private MFXTextField commentField;

  public void initialize() {
    submitButton.setDisable(true);
    populateFields();
    confirmationDialog.setVisible(false);
    confirmationDialog.setDisable(true);
    confirmationDialog.setOnClose(
        event -> {
          confirmationDialog.setVisible(false);
          confirmationDialog.setDisable(true);
        });
  }

  public void populateFields() {
    ArrayList<Rectangle> list = CalendarSingleton.INSTANCE.getValue();
    roomCombo.setText(rowConvert(list.get(0).getId().charAt(1)));
    startCombo.setText(convertInt(extractTime(list.get(0).getId())));
    endCombo.setText(convertInt(extractTime(list.get(list.size() - 1).getId())+100));
    datePicker.setValue(CalendarSingleton.INSTANCE.getDate().toLocalDate());

    roomCombo.setDisable(true);
    startCombo.setDisable(true);
    endCombo.setDisable(true);
    datePicker.setDisable(true);
  }

  public void back() {
    Navigation.navigate(Screen.CALENDAR);
  }

  @FXML
  public void validateButton() {
    if (nameField.getText().isEmpty() || datePicker.getValue() == null) {
      submitButton.setDisable(true);
    } else {
      submitButton.setDisable(false);
    }
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
              convertTime(startCombo.getText()),
              convertTime(endCombo.getText()),
              commentField.getText(),
              "not assigned",
              "new",
              AccountSingleton.INSTANCE.getValue().getUserName());
      databaseRepo.addCRRR(crrr);
      confirmationDialog.setVisible(true);
      confirmationDialog.setDisable(false);
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

  public String rowConvert(char x) {
    String name = null;
    if (x == '1') {
      name = "Abrams Conference Room";
    } else if (x == '2') {
      name = "Anesthesia Conf Floor L1";
    } else if (x == '3') {
      name = "Carrie M. Hall Conference Center Floor 2";
    } else if (x == '4') {
      name = "Medical Records Conference Room Floor L1";
    } else if (x == '5') {
      name = "Shapiro Board Room MapNode 20 Floor 1";
    } else if (x == '6') {
      name = "BTM Conference Center";
    } else if (x == '7') {
      name = "Duncan Reid Conference Room";
    }
    return name;
  }

  public int extractTime(String x) {
    int num1 = (int) x.charAt(3);
    int num2 = (int) x.charAt(4);
    return ((((num1 - 49) * 4) + (num2 - 49)) * 100);
  }
}
