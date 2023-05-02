package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class CreditsController {

  @FXML private ImageView test;
  @FXML private MFXButton backButton;
  @FXML private VBox imageURLs;

  @FXML
  public void initialize() {
    // test.setImage(App.getHomeYello());
    setLink();
  }

  private void setLink() {
    Hyperlink link =
        new Hyperlink(
            "https://www.pinterest.com/pin/brigham-and-womens-hospital-building-for-transformative-medicine--686306430686734156/");
    Hyperlink link2 = new Hyperlink("https://ca.linkedin.com/in/fernanda-ortega");
    Hyperlink link3 =
        new Hyperlink(
            "https://nextcity.org/urbanist-news/hospitals-play-key-role-building-pathways-out-of-poverty");
    Hyperlink link4 =
        new Hyperlink(
            "https://www.hcinnovationgroup.com/policy-value-based-care/article/13028910/at-brigham-and-womens-hospital-plunging-ahead-into-wearable-technology-and-care-redesign");
    Hyperlink link5 = new Hyperlink("https://www.brighamandwomens.org/ ");

    imageURLs.getChildren().add(link);
    imageURLs.getChildren().add(link2);
    imageURLs.getChildren().add(link3);
    imageURLs.getChildren().add(link4);
    imageURLs.getChildren().add(link5);

    //    link.setOnAction(new EventHandler<ActionEvent>() {
    //      @Override
    //      public void handle(ActionEvent e) {
    //        webEngine.load();
    //        System.out.println("This link is clicked");
    //      }
    //    });
  }

  public void back() {
    Navigation.navigate(Screen.HOME);
  }
}
