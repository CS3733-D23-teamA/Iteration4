package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.Main;
import edu.wpi.teamA.controllers.Map.MapEditorEntity;
import edu.wpi.teamA.database.DAOImps.NodeDAOImp;
import edu.wpi.teamA.database.ORMclasses.Node;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import net.kurobako.gesturefx.GesturePane;

public class MapEditorController {
  MapEditorEntity entity = new MapEditorEntity();
  private NodeDAOImp nodeDAO = new NodeDAOImp();
  private ArrayList<Node> levelGNodeArray;
  private ArrayList<Node> levelL1NodeArray;
  private ArrayList<Node> levelL2NodeArray;
  private ArrayList<Node> level1NodeArray;
  private ArrayList<Node> level2NodeArray;
  private ArrayList<Node> level3NodeArray;

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

  public void initialize() {
    double xCoord = mapImage.getBoundsInParent().getMinX();
    double yCoord = mapImage.getBoundsInParent().getMinY();
    double width = mapImage.getBoundsInParent().getWidth();
    double height = mapImage.getBoundsInParent().getHeight();
    System.out.println(xCoord);
    System.out.println(yCoord);
    System.out.println(width);
    System.out.println(height);
    mapGesturePane.setContent(mapStackPane);
    // displayNodeData(nodeDAO.loadNodesFromDatabase());

    levelMenu.setVisible(false);

    levelGButton.setOnAction(event -> changeLevelText(levelGButton));
    levelL1Button.setOnAction(event -> changeLevelText(levelL1Button));
    levelL2Button.setOnAction(event -> changeLevelText(levelL2Button));
    level1Button.setOnAction(event -> changeLevelText(level1Button));
    level2Button.setOnAction(event -> changeLevelText(level2Button));
    level3Button.setOnAction(event -> changeLevelText(level3Button));

    ArrayList<Node> nodeArray = nodeDAO.loadNodesFromDatabase();
    // get node arrays
    levelGNodeArray = entity.getFloorNodes(nodeArray, "G");
    levelL1NodeArray = entity.getFloorNodes(nodeArray, "L1");
    levelL2NodeArray = entity.getFloorNodes(nodeArray, "L2");
    level1NodeArray = entity.getFloorNodes(nodeArray, "1");
    level2NodeArray = entity.getFloorNodes(nodeArray, "2");
    level3NodeArray = entity.getFloorNodes(nodeArray, "3");

    changeLevelText(levelGButton);
  }

  @FXML
  public void levelChooser() {
    // make Vbox visible
    levelMenu.setVisible(true);
  }

  public void changeLevelText(MFXButton button) {

    // get text from clicked button
    levelButton.setText(button.getText());
    // System.out.println(button.getText());

    // hide buttons in Vbox
    levelMenu.setVisible(false);

    String image = "images/map-page/" + button.getText() + ".png";

    // change map image
    mapImage.setImage(new Image(Main.class.getResource(image).toString()));

    // hide old dots
    dotsAnchorPane.getChildren().clear();

    // display new dots
    displayNodeData(determineArray(button.getText()));
  }

  private ArrayList<Node> determineArray(String level) {
    if (level.equals("Level G")) {
      return levelGNodeArray;
    } else if (level.equals("Level L1")) {
      return levelL1NodeArray;
    } else if (level.equals("Level L2")) {
      return levelL2NodeArray;
    } else if (level.equals("Level 1")) {
      return level1NodeArray;
    } else if (level.equals("Level 2")) {
      return level2NodeArray;
    } else if (level.equals("Level 3")) {
      return level3NodeArray;
    } else {
      return null;
    }
  }

  public void displayNodeData(ArrayList<Node> nodeArrayForFloor) {
    double scalar = 0.144;
    for (Node node : nodeArrayForFloor) {
      int originalNodeX = node.getXcoord();
      int originalNodeY = node.getYcoord();
      double newXCoord = originalNodeX * scalar;
      double newYCoord = originalNodeY * scalar;

      Circle circle = addCircle(newXCoord, newYCoord);
      System.out.println(circle.getCenterX());
      System.out.println(circle.getCenterY());
      dotsAnchorPane.getChildren().add(circle);
    }

    App.getPrimaryStage().show();
  }

  public Circle addCircle(double X, double Y) {
    Circle circle = new Circle();
    circle.setCenterX(X);
    circle.setCenterY(Y);
    circle.setRadius(2);
    return circle;
  }

  @FXML
  public void zoomOut() {}

  @FXML
  public void zoomIn() {}
}
