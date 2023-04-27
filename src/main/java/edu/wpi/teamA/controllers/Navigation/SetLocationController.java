package edu.wpi.teamA.controllers.Navigation;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.fxml.FXML;

public class SetLocationController {
@FXML private MFXFilterComboBox locationComboBox;
@FXML private MFXButton updateButton;

public void setLocation(){
    App.setcurrLocation(locationComboBox);
}
}
