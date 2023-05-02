package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.SignageComponent;
import java.util.ArrayList;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SignageController {
  private DataBaseRepository db = DataBaseRepository.getInstance();

  @FXML private VBox ScreenVBox;
  @FXML private HBox screenHBox;
  @FXML private GridPane screenDialog;

  ArrayList<SignageComponent> allSignageScreen1 = new ArrayList<>();
  ArrayList<SignageComponent> allSignageScreen2 = new ArrayList<>();

  public void initialize() {
    ScreenVBox.setVisible(false);
    screenHBox.setVisible(true);

    for (Map.Entry<Integer, SignageComponent> entry : db.getSignageMap().entrySet()) {
      SignageComponent signage = entry.getValue();

      if (signage.getScreen() == 1) {
        allSignageScreen1.add(signage);

      } else if (signage.getScreen() == 2) {
        allSignageScreen2.add(signage);
      }
    }
  }

  public void screen1() {
    ScreenVBox.setVisible(true);
    screenHBox.setVisible(false);
    screenDisplay(true);
  }

  public void screen2() {
    ScreenVBox.setVisible(true);
    screenHBox.setVisible(false);
    screenDisplay(false);
  }

  public void screenDisplay(boolean screen1) {
    ArrayList<SignageComponent> signageToDisplay = new ArrayList<>();
    if (screen1) {
      signageToDisplay.addAll(allSignageScreen1);
    } else {
      signageToDisplay.addAll(allSignageScreen2);
    }

    int numRow = 1;
    for (SignageComponent signage : signageToDisplay) {
      screenDialog.addRow(numRow);
      Text locName = new Text(signage.getLocationName());
      System.out.println(signage.getLocationName());
      System.out.println(locName.getText());

      ImageView imgView = new ImageView();
      if (signage.getDirection().equals("right")) {
        imgView = new ImageView(App.getSignageRight());
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
      } else if (signage.getDirection().equals("left")) {
        imgView = new ImageView(App.getSignageLeft());
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
      } else if (signage.getDirection().equals("up")) {
        imgView = new ImageView(App.getSignageUp());
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
      } else if (signage.getDirection().equals("down")) {
        imgView = new ImageView(App.getSignageDown());
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
      } else {
        imgView = new ImageView(App.getSignageStop());
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
      }
      locName.setFont(Font.font("Open Sans", FontWeight.BOLD, 30));
      locName.setFill(Color.web("0x012d5a"));
      screenDialog.setHalignment(locName, HPos.LEFT);
      screenDialog.add(imgView, 0, numRow);
      screenDialog.add(locName, 1, numRow);
      screenDialog.setVgap(20);

      numRow++;
    }
  }
}
