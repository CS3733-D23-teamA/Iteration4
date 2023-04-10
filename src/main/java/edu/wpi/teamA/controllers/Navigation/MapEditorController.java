package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.Main;
import edu.wpi.teamA.controllers.Map.MapEditorEntity;
import edu.wpi.teamA.database.ORMclasses.Node;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.ArrayList;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

public class MapEditorController {
  private final MapEditorEntity entity = new MapEditorEntity();

  @FXML private BorderPane borderPane;
  @FXML private ImageView mapImage;
  @FXML private StackPane mapStackPane;
  @FXML private AnchorPane dotsAnchorPane;
  @FXML private GesturePane mapGesturePane;

  @FXML private MFXButton levelButton;
  @FXML private MFXButton levelGButton;
  @FXML private MFXButton levelL1Button;
  @FXML private MFXButton levelL2Button;
  @FXML private MFXButton level1Button;
  @FXML private MFXButton level2Button;
  @FXML private MFXButton level3Button;

  @FXML private VBox levelMenu;

  @FXML private Text locationDisplay;

  public void initialize() {
    //    double xCoord = mapImage.getBoundsInParent().getMinX();
    //    double yCoord = mapImage.getBoundsInParent().getMinY();
    //    double width = mapImage.getBoundsInParent().getWidth();
    //    double height = mapImage.getBoundsInParent().getHeight();
    //    System.out.println(xCoord);
    //    System.out.println(yCoord);
    //    System.out.println(width);
    //    System.out.println(height);

    levelGButton.setOnAction(event -> changeLevelText(levelGButton));
    levelL1Button.setOnAction(event -> changeLevelText(levelL1Button));
    levelL2Button.setOnAction(event -> changeLevelText(levelL2Button));
    level1Button.setOnAction(event -> changeLevelText(level1Button));
    level2Button.setOnAction(event -> changeLevelText(level2Button));
    level3Button.setOnAction(event -> changeLevelText(level3Button));

    // set up page
    mapGesturePane.setContent(mapStackPane);
    mapGesturePane.setScrollMode(GesturePane.ScrollMode.ZOOM);
    levelMenu.setVisible(false);
    changeLevelText(levelGButton);
  }

  /** for level chooser button */
  @FXML
  public void levelChooser() {
    // make Vbox visible
    levelMenu.setVisible(true);
  }

  /**
   * Used to change the level chooser text based on which floor
   *
   * @param button button chosen
   */
  private void changeLevelText(MFXButton button) {

    // get text from clicked button
    levelButton.setText(button.getText());

    // hide buttons in Vbox
    levelMenu.setVisible(false);

    // change map image
    String image = "images/map-page/" + button.getText() + ".png";
    mapImage.setImage(new Image(Objects.requireNonNull(Main.class.getResource(image)).toString()));

    // hide old dots
    dotsAnchorPane.getChildren().clear();

    // display new dots
    displayNodeData(Objects.requireNonNull(entity.determineArray(button.getText())));
  }

  private void displayNodeData(ArrayList<Node> nodeArrayForFloor) {
    // scalar for determine position on map
    double scalar = 0.144;

    for (Node node : nodeArrayForFloor) {
      int originalNodeX = node.getXcoord();
      int originalNodeY = node.getYcoord();
      double newXCoord = originalNodeX * scalar;
      double newYCoord = originalNodeY * scalar;

      Circle circle = entity.addCircle(newXCoord, newYCoord);
      circle.setOnMouseEntered(event -> dotClicked(circle, node.getNodeID()));
      circle.setOnMouseExited(event -> dotUnclicked(circle, node.getNodeID()));
      // borderPane.setOnMouseClicked(event -> dotUnclicked(circle, node.getNodeID()));
      dotsAnchorPane.getChildren().add(circle);
    }

    App.getPrimaryStage().show();
  }

  private void dotClicked(Circle circle, int nodeID) {
    circle.setFill(Color.web("0xEEBD28"));
    locationDisplay.setText(entity.getLocationName(nodeID));
  }

  @FXML
  public void dotUnclicked(Circle circle, int nodeID) {
    circle.setFill(Color.web("0x000000"));
  }
}
