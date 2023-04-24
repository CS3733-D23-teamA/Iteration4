package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.entities.MapEntity;
import edu.wpi.teamA.pathfinding.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

public class PathfindingController extends PageController {
  // map panes setup
  @FXML private GesturePane gesturePane;
  @FXML private Pane topPane = new Pane();
  @FXML private Image mapImage = App.getMapL1();
  @FXML private ImageView mapView = new ImageView(Objects.requireNonNull(App.getMapL1()));
  @FXML private StackPane stackPane = new StackPane(mapView, topPane);

  // comboboxes, on screen text directions, and submit button
  @FXML private MFXFilterComboBox<String> startSelection;
  @FXML private MFXFilterComboBox<String> endSelection;
  @FXML private MFXFilterComboBox<String> searchAlgorithmSelection;
  @FXML private Text searchAlgorithmText;
  @FXML private Text directions;
  @FXML private Text searchAlgorithmTextDirections;
  @FXML private VBox searchAlgorithmVbox;

  // level buttons
  @FXML private MFXButton levelL1Button;
  @FXML private MFXButton levelL2Button;
  @FXML private MFXButton level1Button;
  @FXML private MFXButton level2Button;
  @FXML private MFXButton level3Button;

  // Search and location options
  private ArrayList<String> locationOptions = new ArrayList<>();
  private ArrayList<String> searchOptions = new ArrayList<>();

  // Map helper entity
  private final MapEntity map = new MapEntity();
  private String currentLevel = "L1";

  public void initialize() {

    // setting search algorithim selection visibility based on access level
    if (!AccountSingleton.INSTANCE1.getValue().getIsAdmin()) {
      searchAlgorithmVbox.setVisible(false);
      searchAlgorithmVbox.setManaged(false);
      searchAlgorithmTextDirections.setVisible(false);
    }
    // Set up Map in Gesture pane using a StackPane
    gesturePane.setContent(stackPane);

    // center and zoom onto map content
    Platform.runLater(
        () -> {
          gesturePane.centreOn(new Point2D(2265, 950));
          gesturePane.zoomTo(0.5, new Point2D(2265, 950));
        });

    // Getting LongNames from Database
    locationOptions = map.makeListOfLongNames();
    map.initializeNameIDHashMap();

    // Adding search options
    searchOptions.add("A*");
    searchOptions.add("Breadth-First Search");
    searchOptions.add("Depth-First Search");

    // Setting ComboBox Selection Options (for start + end locations)
    startSelection.setItems(FXCollections.observableArrayList(locationOptions));
    endSelection.setItems(FXCollections.observableArrayList(locationOptions));
    searchAlgorithmSelection.setItems(FXCollections.observableArrayList(searchOptions));
    searchAlgorithmSelection.setText(SearchSingleton.getSearchAlgorithm().toString());

    // Buttons to set floor level of map
    levelL1Button.setOnAction(event -> changeLevel(levelL1Button));
    levelL2Button.setOnAction(event -> changeLevel(levelL2Button));
    level1Button.setOnAction(event -> changeLevel(level1Button));
    level2Button.setOnAction(event -> changeLevel(level2Button));
    level3Button.setOnAction(event -> changeLevel(level3Button));
  }

  /**
   * Sets corresponding map level image and global floor variale, sends check path to check each
   * floor for a path
   *
   * @param button takes the user selected level button to access button text
   */
  private void changeLevel(MFXButton button) {

    // get pre-loaded map image from App
    switch (button.getText()) {
      case "L1":
        mapImage = App.getMapL1();
        currentLevel = "L1";
        checkSelections();
        break;
      case "L2":
        mapImage = App.getMapL2();
        currentLevel = "L2";
        checkSelections();
        break;
      case "1":
        mapImage = App.getMap1();
        currentLevel = "1";
        checkSelections();
        break;
      case "2":
        mapImage = App.getMap2();
        currentLevel = "2";
        checkSelections();
        break;
      case "3":
        mapImage = App.getMap3();
        currentLevel = "3";
        checkSelections();
        break;
    }

    // set map image
    this.mapView.setImage(Objects.requireNonNull(mapImage));
  }

  /**
   * Called upon user submit, clears the pane of drawings and implements Astar on start and end
   * selection, sends directions to the user and draws graphical path.
   */
  public void submit() {
    clearPath();

    // get user input for start and end
    String startName = startSelection.getValue();
    String endName = endSelection.getValue();

    // TODO whatttt
    int startID = map.getIDFromLongName(startName);
    int endID = map.getIDFromLongName(endName);

    // sends user input starting and ending IDs and set search algorithm to singleton
    SearchSingleton.setSearchAlgorithm(searchAlgorithmSelection.getValue());
    SearchSingleton.createSearch(startID, endID);

    // set algorithm text
    searchAlgorithmSelection.setValue(SearchSingleton.getSearchAlgorithm().toString());

    // set text directions
    directions.setText(SearchSingleton.pathString());
    directions.setFill(Color.web("#151515"));

    // indicate floor buttons
    // ArrayList<Integer> test = searchEntity.getPath();

    drawPath();
  }

  /**
   * wrapper for submit called upon user input, runs submit when user sets something for all inputs
   */
  @FXML
  public void checkSelections() {
    if (AccountSingleton.INSTANCE1.getValue().getIsAdmin()) {
      if (searchAlgorithmSelection.getValue() != null) {
        SearchSingleton.setSearchAlgorithm(searchAlgorithmSelection.getValue());
        if (startSelection.getSelectedItem() != null && endSelection.getSelectedItem() != null) {
          submit();
        }
      }
    } else {
      if (startSelection.getSelectedItem() != null && endSelection.getSelectedItem() != null) {
        submit();
      }
    }
  }

  /** Helper method for submit, draws graphical path and includes the starting and ending node */
  public void drawPath() {

    // get list of node IDs and set graph node to gnode from search
    System.out.println(SearchSingleton.getSearchAlgorithm());
    ArrayList<Integer> pathIDs = SearchSingleton.getPath();
    GraphNode gNode = SearchSingleton.getGraphNode(pathIDs.get(0));

    // set last x and y coords from gnode
    int lastX = gNode.getXcoord();
    int lastY = gNode.getYcoord();

    // set starting floor and x and y coords //TODO @ vincent what
    String startFloor = gNode.getFloor();
    int startX = lastX;
    int startY = lastY;

    // create a line
    Line line;

    // draw the path
    for (int i = 1; i < pathIDs.size(); i++) {
      gNode = SearchSingleton.getGraphNode(i);
      if (gNode.getFloor().equals(currentLevel)) {
        line = new Line(lastX, lastY, gNode.getXcoord(), gNode.getYcoord());
        line.setFill(Color.web("#012D5A"));
        line.setStrokeWidth(7);
        topPane.getChildren().add(line);
      }
      lastX = gNode.getXcoord();
      lastY = gNode.getYcoord();
    }

    // sets end node is current floor is displaying the end of the path
    if (gNode.getFloor().equals(currentLevel)) {
      Rectangle end = new Rectangle(lastX - 8, lastY - 8, 16, 16);
      end.setFill(Color.web("#F0C747"));
      topPane.getChildren().add(end);
    }

    // sets the start node if current floor is displaying the start of the path
    if (startFloor.equals(currentLevel)) {
      topPane.getChildren().add(new Circle(startX, startY, 8, Color.web("#151515")));
    }
  }

  /** Helper method to clear path */
  public void clearPath() {
    topPane.getChildren().clear();
  }
}
