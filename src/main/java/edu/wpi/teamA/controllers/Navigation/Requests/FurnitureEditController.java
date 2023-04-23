package edu.wpi.teamA.controllers.Navigation.Requests;

import edu.wpi.teamA.controllers.Navigation.PageController;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.FurnitureRequest;
import edu.wpi.teamA.database.Singletons.FurnitureSingleton;
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

public class FurnitureEditController extends PageController {
  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();
  @FXML private MFXButton updateButton;
  @FXML private MFXTextField nameField;
  @FXML private MFXComboBox roomCombo;
  @FXML private DatePicker datePicker;
  @FXML private MFXComboBox timeCombo;
  @FXML private MFXComboBox furnitureCombo;
  @FXML private MFXTextField commentField;

  public void initialize() {
    updateButton.setDisable(false);
    populateCombos();
    populateFields();
  }

  public void populateCombos() {
    furnitureCombo.getItems().addAll("Arm Chair", "Couch", "Coffee Table");
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
    Collections.sort(allRooms);
    roomCombo.getItems().addAll(allRooms);
  }

  public void populateFields() {
    nameField.setText(FurnitureSingleton.INSTANCE.getValue().getName());
    commentField.setText(FurnitureSingleton.INSTANCE.getValue().getComment());
    roomCombo.setText(FurnitureSingleton.INSTANCE.getValue().getRoom());
    furnitureCombo.setText(FurnitureSingleton.INSTANCE.getValue().getFurnitureType());
    datePicker.setValue(FurnitureSingleton.INSTANCE.getValue().getDate().toLocalDate());
    timeCombo.setText(convertInt(FurnitureSingleton.INSTANCE.getValue().getTime()));
  }

  public void back() {
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  @FXML
  public void validateButton() {
    if (nameField.getText().isEmpty() || datePicker.getValue() == null) {
      updateButton.setDisable(true);
    } else {
      updateButton.setDisable(false);
    }
  }

  public void edit() {
    FurnitureRequest fr =
        new FurnitureRequest(
            FurnitureSingleton.INSTANCE.getValue().getId(),
            nameField.getText(),
            roomCombo.getText(),
            Date.valueOf(datePicker.getValue()),
            convertTime(timeCombo.getText()),
            furnitureCombo.getText(),
            commentField.getText(),
            FurnitureSingleton.INSTANCE.getValue().getEmployee(),
            FurnitureSingleton.INSTANCE.getValue().getStatus(),
            FurnitureSingleton.INSTANCE.getValue().getCreator());
    databaseRepo.editFurniture(FurnitureSingleton.INSTANCE.getValue(), fr);
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
    databaseRepo.deleteFurniture(FurnitureSingleton.INSTANCE.getValue());
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }
}
