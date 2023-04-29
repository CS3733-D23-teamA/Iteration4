package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.entities.MapEntity;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

public class LocationSettingsController {
  @FXML private MFXFilterComboBox locationComboBox;
  private MapEntity mapEntity = App.getMapEntity();
  private ArrayList<String> locationOptions;

  private DataBaseRepository db = DataBaseRepository.getInstance();

  public void initialize() {
    // Getting LongNames from Database
    locationOptions = mapEntity.makeListOfLongNames();
    mapEntity.initializeNameIDHashMap();

    // setting combobox items
    locationComboBox.setItems(FXCollections.observableArrayList(locationOptions));

    // setting current location as value
    locationComboBox.setText(App.getCurrentLocation().getLongName());
    locationComboBox.setValue(App.getCurrentLocation().getLongName());
  }

  public void setLocation() {
    App.setCurrentLocation(db.getLocName(locationComboBox.getValue().toString()));
  }
}
