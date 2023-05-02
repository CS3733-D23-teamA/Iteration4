package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.entities.MapEntity;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

public class LocationSettingsController {
  @FXML private MFXFilterComboBox<String> locationComboBox;
  private final MapEntity mapEntity = App.getMapEntity();

  private final DataBaseRepository db = DataBaseRepository.getInstance();

  public void initialize() {
    // Getting LongNames from Database
    ArrayList<String> locationOptions = mapEntity.makeListOfLongNames();
    mapEntity.initializeNameIDHashMap();

    // setting combobox items
    locationComboBox.setItems(FXCollections.observableArrayList(locationOptions));

    // setting current location as value
    locationComboBox.setText(App.getCurrentLocation().getLongName());
    locationComboBox.setValue(App.getCurrentLocation().getLongName());
  }

  public void setLocation() {
    App.setCurrentLocation(db.getLocName(locationComboBox.getValue()));
  }
}
