package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DAOImps.MealDAOImp;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Meal;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

public class MealRequestController extends PageController implements IServiceController {
  @FXML MFXButton submitButton;
  @FXML MFXTextField nameField;
  @FXML MFXComboBox<String> roomCombo;
  @FXML DatePicker datePicker;
  @FXML MFXComboBox<String> timeCombo;
  @FXML MFXComboBox<String> mealCombo;
  @FXML MFXTextField commentField;

  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

  public void initialize() {
    mealCombo.getItems().addAll("Fast Food", "Asian Cuisine", "Indian Cuisine");
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
    roomCombo.getItems().addAll(allRooms);
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
        || mealCombo.getSelectedIndex() == -1
        || roomCombo.getSelectedIndex() == -1) {
      submitButton.setDisable(true);
    } else {
      submitButton.setDisable(false);
    }
  }

  public void clear() {
    submitButton.setDisable(true);
    nameField.clear();
    roomCombo.getSelectionModel().clearSelection();
    commentField.clear();
    timeCombo.getSelectionModel().clearSelection();
    mealCombo.getSelectionModel().clearSelection();
    datePicker.setValue(null);
  }

  public void submit() {
    Meal meal =
        new Meal(
            databaseRepo.getNextMealID(),
            nameField.getText(),
            roomCombo.getText(),
            Date.valueOf(datePicker.getValue()),
            convertTime(timeCombo.getText()),
            mealCombo.getText(),
            commentField.getText(),
            "not assigned",
            "new");
    MealDAOImp md = new MealDAOImp();
    md.add(meal);
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
      newString = time.substring(0, 2) + time.substring(3);
    }
    num = Integer.parseInt(newString);
    return num;
  }
}
