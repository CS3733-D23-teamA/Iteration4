package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.LocationName;
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

  private String floor = "L1";
  private final DataBaseRepository databaseRepo = new DataBaseRepository();
  private Search searchEntity;

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
    initiateAlgorithm();

    // Buttons to set floor level of map
    levelL1Button.setOnAction(event -> changeLevel(levelL1Button));
    levelL2Button.setOnAction(event -> changeLevel(levelL2Button));
    level1Button.setOnAction(event -> changeLevel(level1Button));
    level2Button.setOnAction(event -> changeLevel(level2Button));
    level3Button.setOnAction(event -> changeLevel(level3Button));
  }

  /** */
  private void initiateAlgorithm() {
    switch (PathfindingSingleton.getAlgo()) {
      case DFS:
        searchAlgorithmSelection.setValue("Depth-First Search");
        break;
      case BFS:
        searchAlgorithmSelection.setValue("Breadth-First Search");
        break;
      case ASTAR:
        searchAlgorithmSelection.setValue("A*");
        break;
    }

    searchAlgorithmSelection.setText(PathfindingSingleton.getAlgo().toString());

    if (searchAlgorithmSelection.getValue() != null) {
      setAlgorithm(searchAlgorithmSelection.getValue());
    }
  }

  /** @param algorithm */
  private void setAlgorithm(String algorithm) {
    switch (algorithm) {
      case "Depth-First Search":
        PathfindingSingleton.setAlgo(PathfindingAlgorithm.DFS);
        break;
      case "Breadth-First Search":
        PathfindingSingleton.setAlgo(PathfindingAlgorithm.BFS);
        break;
      case "A*":
        PathfindingSingleton.setAlgo(PathfindingAlgorithm.ASTAR);
        break;
    }
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
        floor = "L1";
        checkSelections();
        break;
      case "L2":
        mapImage = App.getMapL2();
        floor = "L2";
        checkSelections();
        break;
      case "1":
        mapImage = App.getMap1();
        floor = "1";
        checkSelections();
        break;
      case "2":
        mapImage = App.getMap2();
        floor = "2";
        checkSelections();
        break;
      case "3":
        mapImage = App.getMap3();
        floor = "3";
        checkSelections();
        break;
    }

    // set map image
    this.mapView.setImage(Objects.requireNonNull(mapImage));
  }

  /**
   * Creates a search object based off user inputed start and end, and global algorithm object
   *
   * @param endID int value of starting node ID
   * @param startID int value of ending node ID
   */
  private void createSearch(int startID, int endID) {
    if (AccountSingleton.INSTANCE1.getValue().getIsAdmin()) {
      if (PathfindingSingleton.getAlgo() == PathfindingAlgorithm.BFS) {
        searchEntity = new BFS(startID, endID);
        searchAlgorithmTextDirections.setText("Using Breadth-First Search");
      } else if (PathfindingSingleton.getAlgo() == PathfindingAlgorithm.DFS) {
        searchEntity = new DFS(startID, endID);
        searchAlgorithmTextDirections.setText("Using Depth-First Search");
      } else {
        searchEntity = new AStar(startID, endID);
        searchAlgorithmTextDirections.setText("Using A* Search");
      }
    }
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

    // sends to create search
    setAlgorithm(searchAlgorithmSelection.getValue());
    createSearch(startID, endID);

    // set text directions
    directions.setText(generatePathString(searchEntity.getPath()));
    directions.setFill(Color.web("#151515"));

    // indicate floor buttons
    ArrayList<Integer> test = searchEntity.getPath();

    drawPath();
  }

  /**
   * wrapper for submit called upon user input, runs submit when user sets something for all inputs
   */
  @FXML
  public void checkSelections() {
    System.out.println(AccountSingleton.INSTANCE1.getValue().getAdminYes());
    if (startSelection.getSelectedItem() != null && endSelection.getSelectedItem() != null) {
      if (AccountSingleton.INSTANCE1.getValue().getIsAdmin()) {
        if (searchAlgorithmSelection.getValue() != null) {
          submit();
        }
      } else {
        submit();
      }
    }
  }

  /** Helper method for submit, draws graphical path and includes the starting and ending node */
  public void drawPath() {

    // get list of node IDs and set graph node to gnode from search
    ArrayList<Integer> pathIDs = searchEntity.getPath();
    GraphNode gNode = searchEntity.getGraphNode(pathIDs.get(0));

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
      gNode = searchEntity.getGraphNode(pathIDs.get(i));
      if (gNode.getFloor().equals(floor)) {
        line = new Line(lastX, lastY, gNode.getXcoord(), gNode.getYcoord());
        line.setFill(Color.web("#012D5A"));
        line.setStrokeWidth(7);
        topPane.getChildren().add(line);
      }
      lastX = gNode.getXcoord();
      lastY = gNode.getYcoord();
    }

    // sets end node is current floor is displaying the end of the path
    if (gNode.getFloor().equals(floor)) {
      Rectangle end = new Rectangle(lastX - 8, lastY - 8, 16, 16);
      end.setFill(Color.web("#F0C747"));
      topPane.getChildren().add(end);
    }

    // sets the start node if current floor is displaying the start of the path
    if (startFloor.equals(floor)) {
      topPane.getChildren().add(new Circle(startX, startY, 8, Color.web("#151515")));
    }
  }

  /**
   * Helper method for submit, returns a string of graphical directions
   *
   * @param path takes an array list of Node IDs to represent the path
   */
  public String generatePathString(ArrayList<Integer> path) {
    String stringPath = "Wow! You're already there! Good Job!";
    if (path.size() > 1) {
      MapEntity mapEd = new MapEntity();
      LocationName locName = mapEd.getLocationName(path.get(0));
      stringPath = "Start at " + locName.getLongName();

      for (int i = 1; i < path.size(); i++) {
        locName = mapEd.getLocationName(path.get(i));
        stringPath += ", then go to " + locName.getLongName();
      }
      stringPath += ". You have reached your destination.";
    }

    return stringPath;
  }

  /** Helper method to clear path */
  public void clearPath() {
    topPane.getChildren().clear();
  }
}
