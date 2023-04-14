package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.Main;
import edu.wpi.teamA.database.DAOImps.NodeDAOImp;
import edu.wpi.teamA.database.ORMclasses.Node;
import edu.wpi.teamA.pathfinding.AStar;
import edu.wpi.teamA.pathfinding.GraphNode;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.util.ArrayList;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

public class PathfindingController extends PageController {
  @FXML private GesturePane gesturePane;
  @FXML private Pane topPane = new Pane();

  @FXML
  private StackPane stackPane =
      new StackPane(
          new ImageView(
              new Image(
                  Objects.requireNonNull(Main.class.getResource("graphics/map-page/Level L1.png"))
                      .toString())),
          topPane);

  @FXML private MFXFilterComboBox<Integer> startSelection;
  @FXML private MFXFilterComboBox<Integer> endSelection;
  @FXML private Text directions;
  @FXML private MFXButton submitButton;
  //  @FXML private MFXButton levelButton;
  //  @FXML private MFXButton levelGButton;
  //  @FXML private MFXButton levelL1Button;
  //  @FXML private MFXButton levelL2Button;
  //  @FXML private MFXButton level1Button;
  //  @FXML private MFXButton level2Button;
  //  @FXML private MFXButton level3Button;

  private ArrayList<Integer> nodeIDOptions = new ArrayList<Integer>();
  private ArrayList<Node> nodeList;
  private NodeDAOImp nodeDAO = new NodeDAOImp();

  @Override
  public void initialize() {
    // Set up Map in Gesture pane using a StackPane
    gesturePane.setContent(stackPane);
    gesturePane.setScrollMode(GesturePane.ScrollMode.ZOOM);

    // Getting Nodes from Database
    nodeList = nodeDAO.loadNodesFromDatabase();
    for (Node node : nodeList) {
      if (node.getFloor().equals("L1")) { // TODO add nodes for all floors
        nodeIDOptions.add(node.getNodeID());
      }
    }

    // Setting ComboBox Selection Options (for start + end locations)
    startSelection.setItems(FXCollections.observableArrayList(nodeIDOptions));
    endSelection.setItems(FXCollections.observableArrayList(nodeIDOptions));

    //    // Buttons to set floor level of map
    //    levelGButton.setOnAction(event -> changeLevelText(levelGButton));
    //    levelL1Button.setOnAction(event -> changeLevelText(levelL1Button));
    //    levelL2Button.setOnAction(event -> changeLevelText(levelL2Button));
    //    level1Button.setOnAction(event -> changeLevelText(level1Button));
    //    level2Button.setOnAction(event -> changeLevelText(level2Button));
    //    level3Button.setOnAction(event -> changeLevelText(level3Button));
  }

  /**
   * Used to change the level chooser text based on which floor
   *
   * @param button button chosen
   */
  //  private void changeLevelText(MFXButton button) {
  //
  //    // get text from clicked button
  //    levelButton.setText(button.getText());
  //
  //    // change map image
  //    String image = "graphics/map-page/" + button.getText() + ".png";
  //    mapImage.setImage(new
  // Image(Objects.requireNonNull(Main.class.getResource(image)).toString()));
  //
  //    // hide old dots
  //    dotsAnchorPane.getChildren().clear();
  //
  //    // button
  //    level = button.getText();
  //
  //    // display new dots
  //    displayNodeData(Objects.requireNonNull(entity.determineArray(level)));
  //  }

  /**
   * Called upon user submit, clears the pane of drawings and implements Astar on start and end
   * selection, sends directions to the user and draws graphical path.
   */
  public void submit() {
    try {
      topPane.getChildren().clear();
      AStar a = new AStar(startSelection.getSelectedItem(), endSelection.getSelectedItem());

      directions.setText(a.toString());
      directions.setFill(Color.web("#f1f1f1"));

      System.out.println("Nodes submitted");

      drawPath(a);

    } catch (NullPointerException e) {
      System.out.println("Null Value");
    }
  }
  /**
   * Helper method for submit, draws graphical path
   *
   * @param a Astar object implementation to determine path
   */
  public void drawPath(AStar a) {
    ArrayList<Integer> nodePathIDs = a.getPath();
    GraphNode gNode = a.getGraphNode(nodePathIDs.get(0));

    int lastX = gNode.getXcoord();
    int lastY = gNode.getYcoord();
    Line line;

    topPane.getChildren().add(new Circle(lastX, lastY, 3, Color.DARKRED));

    for (int i = 1; i < nodePathIDs.size(); i++) {
      gNode = a.getGraphNode(nodePathIDs.get(i));

      line = new Line(lastX, lastY, gNode.getXcoord(), gNode.getYcoord());
      line.setFill(Color.DARKRED);
      line.setStrokeWidth(8);

      topPane
          .getChildren()
          .addAll(line, new Circle(gNode.getXcoord(), gNode.getYcoord(), 6, Color.DARKRED));
      lastX = gNode.getXcoord();
      lastY = gNode.getYcoord();
    }
  }
}
