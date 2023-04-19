package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DAOImps.FurnitureDAOImp;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.FurnitureRequest;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

public class FurnitureRequestController extends PageController implements IServiceController {

  @FXML private MFXButton submitButton;
  @FXML private MFXTextField nameField;
  @FXML private MFXComboBox roomComboBox;

  @FXML private DatePicker datePicker;
  @FXML private MFXComboBox timeCombo;
  @FXML private MFXComboBox furnitureCombo;
  @FXML private MFXTextField commentField;
  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

  public void initialize() {
    furnitureCombo.getItems().addAll("Arm Chair", "Couch", "Coffee Table");
    timeCombo
        .getItems()
        .addAll(
            "00:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00",
            "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
            "19:00", "20:00", "21:00", "22:00", "23:00");
    ArrayList<String> rooms = new ArrayList<>();
    rooms.addAll(databaseRepo.filterLocType("BATH"));
    rooms.addAll(databaseRepo.filterLocType("CONF"));
    rooms.addAll(databaseRepo.filterLocType("DEPT"));
    rooms.addAll(databaseRepo.filterLocType("LABS"));
    rooms.addAll(databaseRepo.filterLocType("REST"));
    roomComboBox.getItems().addAll(rooms);
  }

  @Override
  public void back() {
    Navigation.navigate(Screen.SERVICE_REQUEST);
  }

  @FXML
  public void validateButton() {
    if (nameField.getText().isEmpty()
        || datePicker.getValue() == null
        || timeCombo.getSelectedIndex() == -1
        || furnitureCombo.getSelectedIndex() == -1
        || roomComboBox.getSelectedIndex() == -1) {
      submitButton.setDisable(true);
    } else {
      submitButton.setDisable(false);
    }
  }

  public void clear() {
    submitButton.setDisable(true);
    nameField.clear();
    roomComboBox.clear();
    commentField.clear();
    timeCombo.getSelectionModel().clearSelection();
    furnitureCombo.getSelectionModel().clearSelection();
    datePicker.setValue(null);
  }

  public void submit() {
    FurnitureRequest furniture =
        new FurnitureRequest(
            nameField.getText(),
            roomComboBox.getText(),
            Date.valueOf(datePicker.getValue()),
            convertTime(timeCombo.getText()),
            furnitureCombo.getText(),
            commentField.getText(),
            "new");
    FurnitureDAOImp fd = new FurnitureDAOImp();
    fd.addFurniture(furniture);
    clear();
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
      newString = time.substring(0, 2) + time.substring(2);
    }
    num = Integer.parseInt(newString);
    return num;
  }
}
