package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.SignageComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
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

    for (Map.Entry<String, SignageComponent> entry : db.getSignageMap().entrySet()) {
      SignageComponent signage = entry.getValue();
      System.out.println("signage.getScreen(): " + signage.getScreen());

      if (signage.getScreen() == 1) {
        allSignageScreen1.add(signage);
        System.out.println("screen1");

      } else if (signage.getScreen() == 2) {
        allSignageScreen2.add(signage);
        System.out.println("screen2");
      } else {
        System.out.println("k");
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
    System.out.println("in screen display");
    ArrayList<SignageComponent> signageToDisplay = new ArrayList<>();
    if (screen1) {
      signageToDisplay.addAll(allSignageScreen1);
    } else if (!screen1) {
      signageToDisplay.addAll(allSignageScreen2);
    }
    // ArrayList<String> allSignageLocNames = new ArrayList<>();
    // ArrayList<String> allSignageDates = new ArrayList<>();

    int numRow = 1;
    for (SignageComponent signage : signageToDisplay) {
      screenDialog.addRow(numRow);
      Text locName = new Text(signage.getLocationName());
      System.out.println(signage.getLocationName());
      System.out.println(locName.getText());
      Text date = new Text(signage.getDate().toString());

      ImageView imgView = new ImageView();
      if (signage.getDirection().equals("right")) {
        imgView = new ImageView(App.getSignageRight());
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
      } else if (signage.getDirection().equals("left")) {
        imgView = new ImageView(App.getSignageLeft());
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
        // direction.setText("<-");
      } else if (signage.getDirection().equals("up")) {
        imgView = new ImageView(App.getSignageUp());
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
        // direction.setText("^");
      } else if (signage.getDirection().equals("down")) {
        imgView = new ImageView(App.getSignageDown());
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
        // direction.setText("v");
      } else {
        // direction.setText("Stop here for");
        imgView = new ImageView(App.getSignageStop());
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
      }
      locName.setFont(Font.font("Open Sans", FontWeight.BOLD, 30));
      locName.setFill(Color.web("0x012d5a"));
      screenDialog.setHalignment(locName, HPos.CENTER);
      date.setFont(Font.font("Open Sans", FontWeight.BOLD, 30));
      date.setFill(Color.web("0x012d5a"));
      screenDialog.setHalignment(date, HPos.CENTER);

      screenDialog.setBackground(Background.fill(Color.web("0x98AABC")));
      screenDialog.setStyle("-fx-border: 2px solid;");
      screenDialog.add(imgView, 0, numRow);
      screenDialog.add(locName, 1, numRow);
      screenDialog.add(date, 2, numRow);
      numRow++;
      // Navigation.navigate(Screen.SIGNAGE);
      // App.getPrimaryStage().show();
    }
  }
}
