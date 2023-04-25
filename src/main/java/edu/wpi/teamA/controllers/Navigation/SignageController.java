package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.SignageComponent;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.ArrayList;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SignageController extends PageController {

  private DataBaseRepository db = DataBaseRepository.getInstance();
  @FXML private MFXButton Screen1;
  @FXML private MFXButton Screen2;
  @FXML private VBox ScreenVBox;
  @FXML private HBox screenHBox;
  @FXML private GridPane screenDialog;
  private boolean screen1;
  private boolean screen2;

  ArrayList<SignageComponent> allSignageScreen1 = new ArrayList<>();
  ArrayList<SignageComponent> allSignageScreen2 = new ArrayList<>();

  public void initialize() {
    ScreenVBox.setVisible(false);
    screenHBox.setVisible(true);
    screen1 = false;
    screen2 = false;


    for (Map.Entry<String, SignageComponent> entry : db.getSignageMap().entrySet()) {
      SignageComponent signage = entry.getValue();
      if (signage.getScreen() == 1) {
        allSignageScreen1.add(signage);

      } else if (signage.getScreen() == 2) {
        allSignageScreen1.add(signage);
      }
    }
  }

  public boolean screen1(){
    return true;
  }
  public boolean  screen2(){
    return true;
  }
  public void screenDisplay() {
    ScreenVBox.setVisible(true);
    screenHBox.setVisible(false);
    ArrayList<SignageComponent> signageToDisplay = new ArrayList<>();
    if(screen1()){
      signageToDisplay = allSignageScreen1;
    }
    else if(screen2()){
      signageToDisplay = allSignageScreen1;
    }
    ArrayList<String> allSignageLocNames = new ArrayList<>();
    ArrayList<String> allSignageDates = new ArrayList<>();

    for (SignageComponent signage : signageToDisplay) {
      allSignageLocNames.add(signage.getLocationName());
      allSignageDates.add(signage.getDate().toString());
    }
  }

  public void screen2Display() {
    ScreenVBox.setVisible(true);
    screenHBox.setVisible(false);
    ArrayList<String> allSignageLocNames = new ArrayList<>();
    ArrayList<String> allSignageDates = new ArrayList<>();

    for (SignageComponent signage : allSignageScreen2) {
      allSignageLocNames.add(signage.getLocationName());
      allSignageDates.add(signage.getDate().toString());
    }
  }
}
