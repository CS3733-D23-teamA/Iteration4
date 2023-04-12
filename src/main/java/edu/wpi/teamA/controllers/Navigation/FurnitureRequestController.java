package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DAOImps.FurnitureDAOImp;
import edu.wpi.teamA.database.ORMclasses.FurnitureRequest;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import java.sql.Date;

public class FurnitureRequestController extends PageController implements IServiceController {

    @FXML
    MFXButton submitButton;
    @FXML
    MFXTextField nameField;
    @FXML MFXComboBox roomComboBox;

    @FXML
    DatePicker datePicker;
    @FXML
    MFXComboBox timeCombo;
    @FXML MFXComboBox furnitureCombo;
    @FXML MFXTextField commentField;

    @Override
    public void initialize() {
        furnitureCombo.getItems().addAll("Arm Chair", "Couch", "Coffee Table");
        timeCombo
                .getItems()
                .addAll(
                        "00:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00",
                        "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
                        "19:00", "20:00", "21:00", "22:00", "23:00");
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
                || furnitureCombo.getSelectedIndex() == -1) {
            submitButton.setDisable(true);
        } else {
            try {
                Integer.parseInt(roomComboBox.getText());

                submitButton.setDisable(false);
            } catch (NumberFormatException error) {
                submitButton.setDisable(true);
            }
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
                        Integer.parseInt(roomComboBox.getText()),
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
