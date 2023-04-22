package edu.wpi.teamA.controllers.Navigation.Requests;

import edu.wpi.teamA.App;
import edu.wpi.teamA.controllers.Navigation.PageController;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.FurnitureRequest;
import edu.wpi.teamA.entities.ServiceRequestEntity;
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

public class FurnitureRequestController extends PageController implements IServiceController {

  @FXML private MFXButton submitButton;
  @FXML private MFXTextField nameField;
  @FXML private MFXComboBox<String> roomComboBox;
  @FXML private DatePicker datePicker;
  @FXML private MFXComboBox<String> timeCombo;
  @FXML private MFXComboBox<String> furnitureCombo;
  @FXML private MFXTextField commentField;
  @FXML private MFXGenericDialog confirmationDialog;

  private final ServiceRequestEntity entity = App.getServiceRequestEntity();
  private final DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

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
    confirmationDialog.setVisible(false);
    confirmationDialog.setDisable(true);
    confirmationDialog.setOnClose(
        event -> {
          confirmationDialog.setVisible(false);
          confirmationDialog.setDisable(true);
        });
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
            databaseRepo.getNextFurnitureID(),
            nameField.getText(),
            roomComboBox.getText(),
            Date.valueOf(datePicker.getValue()),
            entity.convertTime(timeCombo.getText()),
            furnitureCombo.getText(),
            commentField.getText(),
            "not assigned",
            "new",
            AccountSingleton.INSTANCE1.getValue().getUserName());
    databaseRepo.addFurniture(furniture);
    clear();
    confirmationDialog.setVisible(true);
    confirmationDialog.setDisable(false);
  }

  @FXML
  public void closeConfirmation() {
    confirmationDialog.setVisible(false);
    confirmationDialog.setDisable(true);
  }
}
