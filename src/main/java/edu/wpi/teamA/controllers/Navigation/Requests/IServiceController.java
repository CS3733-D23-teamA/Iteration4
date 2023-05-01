package edu.wpi.teamA.controllers.Navigation.Requests;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public interface IServiceController {
  @FXML MFXButton submitButton = new MFXButton();
  @FXML MFXButton clearButton = new MFXButton();

  @FXML
  public void validateSubmit();

  public void clear();

  public void submit();
}
