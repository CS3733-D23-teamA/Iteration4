package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.ORMclasses.Node;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.entities.Level;
import edu.wpi.teamA.entities.MapEntity;
import edu.wpi.teamA.pathfinding.*;
import io.github.palexdev.materialfx.controls.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

public class PathfindingController {
  // Map helper entity
  private final MapEntity mapEntity = App.getMapEntity();

  // level toggle buttons
  private ToggleGroup levelToggles = new ToggleGroup();
  @FXML private MFXRectangleToggleNode levelL1Toggle;
  @FXML private MFXRectangleToggleNode levelL2Toggle;
  @FXML private MFXRectangleToggleNode level1Toggle;
  @FXML private MFXRectangleToggleNode level2Toggle;
  @FXML private MFXRectangleToggleNode level3Toggle;

  private ToggleGroup accessibilityToggles = new ToggleGroup();
  @FXML private MFXRectangleToggleNode regularPathToggle;
  @FXML private MFXRectangleToggleNode noElevatorPathToggle;
  @FXML private MFXRectangleToggleNode noStairsPathToggle;

  // Boolean for admin settings
  private Boolean isAdmin = AccountSingleton.isAdmin();

  // Boolean for user submit
  private Boolean isSubmitted = false;

  // Current Level Object
  private Level currentLevel = Level.LOWERLEVELL1;

  // level pagination
  @FXML private Label currentLevelLabel;
  @FXML private SVGPath nextLevel;
  @FXML private SVGPath prevLevel;

  // Search and location options
  private ArrayList<String> locationOptions = new ArrayList<>();
  private ArrayList<String> searchOptions = new ArrayList<>();

  // Gesture pane setup
  @FXML private GesturePane gesturePane;
  @FXML private Pane topPane = new Pane();
  @FXML private Image mapImage;
  @FXML private ImageView mapView = new ImageView(mapImage);
  @FXML private StackPane stackPane = new StackPane(mapView, topPane);

  // comboboxes, on screen text directions + Search Algorithm
  @FXML private MFXFilterComboBox<String> startSelection;
  @FXML private MFXFilterComboBox<String> endSelection;
  @FXML private MFXFilterComboBox<String> searchAlgorithmSelection;
  @FXML private Text directions;
  @FXML private Text searchAlgorithmTextDisplay;

  // vbox containing all search algo UI for !admin disable
  @FXML private VBox searchAlgorithmVbox;

  // vbox for admin messaging
  @FXML private HBox adminMessageInput;
  @FXML private Label adminMessage;
  @FXML private MFXButton adminMessageSubmit;
  @FXML private MFXTextField adminMessageTextField;
  @FXML private VBox levelControls;
  @FXML private HBox messageVbox;

  // location toggle
  @FXML private MFXToggleButton locationToggle;
  @FXML private MFXToggleButton secondNameToggle;

  private int accessibilityLevel;

  public void initialize() {

    // setting search algortihm selection visibility based on access level
    if (!isAdmin) {
      searchAlgorithmVbox.setVisible(false);
      searchAlgorithmVbox.setManaged(false);
    }

    // Getting LongNames from Database
    locationOptions = mapEntity.makeListOfLongNames();
    mapEntity.initializeNameIDHashMap();

    // Adding search options
    searchOptions.add("A*");
    searchOptions.add("Breadth-First Search");
    searchOptions.add("Depth-First Search");

    // Setting ComboBox Selection Options (for start + end locations)
    startSelection.setItems(FXCollections.observableArrayList(locationOptions));
    startSelection.setText(App.getCurrentLocation().getLongName());
    startSelection.setValue(App.getCurrentLocation().getLongName());
    endSelection.setItems(FXCollections.observableArrayList(locationOptions));
    searchAlgorithmSelection.setItems(FXCollections.observableArrayList(searchOptions));
    searchAlgorithmSelection.setText(SearchSingleton.getSearchAlgorithm().toString());
    searchAlgorithmSelection.setValue(SearchSingleton.getSearchAlgorithm().toString());

    // setting all levels in levelButtons
    levelL1Toggle.setToggleGroup(levelToggles);
    levelL2Toggle.setToggleGroup(levelToggles);
    level1Toggle.setToggleGroup(levelToggles);
    level2Toggle.setToggleGroup(levelToggles);
    level3Toggle.setToggleGroup(levelToggles);

    // Buttons to set floor level of map
    levelL1Toggle.setOnAction(event -> changeLevel(levelL1Toggle.getText()));
    levelL2Toggle.setOnAction(event -> changeLevel(levelL2Toggle.getText()));
    level1Toggle.setOnAction(event -> changeLevel(level1Toggle.getText()));
    level2Toggle.setOnAction(event -> changeLevel(level2Toggle.getText()));
    level3Toggle.setOnAction(event -> changeLevel(level3Toggle.getText()));
    levelL1Toggle.setSelected(true);

    // setting up accessibility toggle
    regularPathToggle.setToggleGroup(accessibilityToggles);
    noElevatorPathToggle.setToggleGroup(accessibilityToggles);
    noStairsPathToggle.setToggleGroup(accessibilityToggles);
    regularPathToggle.setGraphic(new ImageView(App.getDefaultPF()));
    noElevatorPathToggle.setGraphic(new ImageView(App.getElevatorPF()));
    noStairsPathToggle.setGraphic(new ImageView(App.getStairsPF()));

    // Buttons to set accessibility of map
    regularPathToggle.setOnAction(event -> setAccessibility(0));
    noElevatorPathToggle.setOnAction(event -> setAccessibility(1));
    noStairsPathToggle.setOnAction(event -> setAccessibility(2));
    regularPathToggle.setSelected(true);

    // Pagination buttons setup
    nextLevel.setOnMouseClicked(event -> changeLevel(getNextLevel()));
    prevLevel.setOnMouseClicked(event -> changeLevel(getPrevLevel()));
    mapEntity.setLevelOrder();

    // Set up Map in Gesture pane using a StackPane
    changeLevel(String.valueOf(currentLevel));
    gesturePane.setContent(stackPane);
    gesturePane.setGestureEnabled(true);

    // center and zoom onto map content
    centerMap(2265, 950, 0.5);

    locationToggle.setOnAction(event -> toggleNodeNames());
  }

  /**
   * sets the accessibility for the path
   *
   * @param accessibility is an in representing an accessibility level where 0 = none, 1 = no
   *     elevator, 2 = no stairs
   */
  private void setAccessibility(int accessibility) {
    accessibilityLevel = accessibility;
    if (isSubmitted) {
      submit();
      drawPath();
    }
  }

  /**
   * if selections are present, returns next level in a path
   *
   * @return String of next level using shortened level name
   */
  private String getNextLevel() {
    return mapEntity.getNextLevel().toString();
  }

  /**
   * if selections are present, returns previous level in a path
   *
   * @return String of next prev using shortened level name
   */
  private String getPrevLevel() {
    return mapEntity.getPrevLevel().toString();
  }

  /**
   * Helper for change level Updates map image, current level, disabling paginators, and label
   *
   * @param level takes the current user selected level
   */
  private void setCurrentLevel(Level level) {
    currentLevel = level;
    mapImage = currentLevel.getMapImage(); // set image
    setPaginator();
    currentLevelLabel.setText("Level " + currentLevel); // set current level label
    // draws path for each level if submitted
    if (isSubmitted) {
      drawPath();
    }
  }

  /**
   * Helper for set current level, disables paginators when user paginates to end or beginning of
   * the path
   */
  private void setPaginator() {
    // set next to blue if next level exists
    if (mapEntity.nextLevelExists(currentLevel)) {
      nextLevel.setStyle("-fx-fill: #012D5A");
    } else { // else set next to grey
      nextLevel.setStyle("-fx-fill: #98AABC");
    }

    // set prev blue as we paginate next
    if (mapEntity.prevLevelExists(currentLevel)) {
      prevLevel.setStyle("-fx-fill: #012D5A");
    } else { // else set next to grey
      prevLevel.setStyle("-fx-fill: #98AABC");
    }
  }

  /**
   * Sets corresponding map level image and global floor variable, sends check path to check each
   * floor for a path
   *
   * @param level takes the current user selected level as a level object
   */
  private void changeLevel(String level) {
    clearPath();

    switch (level) {
      case "L1":
        setCurrentLevel(Level.LOWERLEVELL1);
        break;
      case "L2":
        setCurrentLevel(Level.LOWERLEVELL2);
        break;
      case "1":
        setCurrentLevel(Level.LEVEL1);
        break;
      case "2":
        setCurrentLevel(Level.LEVEL2);
        break;
      case "3":
        setCurrentLevel(Level.LEVEL3);
        break;
    }

    toggleNodeNames();

    // set map image in image view
    this.mapView.setImage(Objects.requireNonNull(mapImage));
  }

  /**
   * Called upon user submit clears the pane of drawings and implements admin selected search (A* by
   * default) on start and end selection, sends directions to the user and draws graphical path.
   */
  public void submit() {

    // get user input for start and end
    String startName = startSelection.getValue();
    String endName = endSelection.getValue();

    int startID = mapEntity.getIDFromLongName(startName);
    int endID = mapEntity.getIDFromLongName(endName);

    // sends user input starting and ending IDs and set search algorithm to singleton
    SearchSingleton.setSearchAlgorithm(searchAlgorithmSelection.getValue());
    SearchSingleton.createSearch(startID, endID, accessibilityLevel);

    // set algorithm text
    searchAlgorithmSelection.setValue(SearchSingleton.getSearchAlgorithm().toString());

    setTextDirections();

    // Sets the Order for paginator
    mapEntity.setLevelOrder(SearchSingleton.getPath());

    // Changes the levels for pathfinding globally
    changeLevel(mapEntity.getFirstLevel().toString());

    // centers map on path
    centerMap(
        mapEntity.getNodeInfo(startID).getXcoord(), mapEntity.getNodeInfo(startID).getYcoord(), 1);

    drawPath();
  }

  /** Helper method for submit sends text directions to the user */
  private void setTextDirections() {
    directions.setText(SearchSingleton.pathString(secondNameToggle.isSelected()));
    searchAlgorithmTextDisplay.setText("Path found using " + SearchSingleton.getSearchAlgorithm());
    directions.setFill(Color.web("#151515"));
  }

  /**
   * Helper method to wrap for submit called upon user input, runs submit when user sets something
   * for all inputs
   *
   * @return true if starting and ending locations are set by user, false otherwise
   */
  @FXML
  public void checkSelections() {
    if (isAdmin) {
      if (searchAlgorithmSelection.getValue() != null) {
        SearchSingleton.setSearchAlgorithm(searchAlgorithmSelection.getValue());
        if (startSelection.getValue() != null && endSelection.getValue() != null) {
          submit();
          isSubmitted = true;
        }
      }
    } else {
      if (startSelection.getValue() != null && endSelection.getValue() != null) {
        submit();
        isSubmitted = true;
      }
    }
  }

  /**
   * Helper method for submit Resets the pane, draws graphical path and includes the starting and
   * ending node
   */
  public void drawPath() {

    if (isAdmin && isSubmitted) {
      resetAdminMessageInput();
    }

    // get list of node IDs and set graph node to gnode from search and indicate levels
    ArrayList<Integer> pathIDs = SearchSingleton.getPath();
    GraphNode gNode = SearchSingleton.getGraphNode(pathIDs.get(0));
    levelButtonIndicator(pathIDs);

    // set last x and y coords from gnode
    int lastX = gNode.getXcoord();
    int lastY = gNode.getYcoord();

    // set starting floor and x and y coords
    String startFloor = gNode.getFloor();
    int startX = lastX;
    int startY = lastY;

    // create a line
    Line line;

    // draw the path
    for (int i = 1; i < pathIDs.size(); i++) {
      gNode = SearchSingleton.getGraphNode(pathIDs.get(i));
      if (currentLevel.toString().equals(gNode.getFloor())) {
        line = new Line(lastX, lastY, gNode.getXcoord(), gNode.getYcoord());
        line.setFill(Color.web("#012D5A"));
        line.setStrokeWidth(7);
        topPane.getChildren().add(line);
      }
      lastX = gNode.getXcoord();
      lastY = gNode.getYcoord();
    }

    // sets end node is current floor is displaying the end of the path
    if (currentLevel.toString().equals(gNode.getFloor())) {
      Rectangle end = new Rectangle(lastX - 8, lastY - 8, 16, 16);
      end.setFill(Color.web("#F0C747"));
      topPane.getChildren().add(end);
    }

    // sets the start node if current floor is displaying the start of the path
    if (currentLevel.toString().equals(startFloor)) {
      topPane.getChildren().add(new Circle(startX, startY, 8, Color.web("#151515")));
    }
  }

  @FXML
  private void toggleNodeNames() {
    clearPath();
    HashMap<Integer, Node> nodeMap = mapEntity.getNodeMap(currentLevel);
    for (Map.Entry<Integer, Node> entry : nodeMap.entrySet()) {
      Node node = entry.getValue();
      if (!mapEntity
              .getLocationName(node.getNodeID(), secondNameToggle.isSelected())
              .getNodeType()
              .equals("HALL")
          && locationToggle.isSelected()) {
        Text text = mapEntity.addText(node, secondNameToggle.isSelected());
        topPane.getChildren().add(text);
      }
    }
    if (isSubmitted) {
      drawPath();
    }
  }

  /**
   * Helper method Indicates level buttons
   *
   * @param nodeIDS takes a list of node IDs that map a path
   */
  private void levelButtonIndicator(ArrayList<Integer> nodeIDS) {
    // set up a highlight object
    BorderStroke highlight =
        new BorderStroke(
            Color.web("F0C747"), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(5));

    ArrayList<String> stringList = mapEntity.floorsTravelledTo(nodeIDS);
    if (stringList.contains("L1")) {
      levelL1Toggle.setBorder(new Border(highlight));
    }
    if (stringList.contains("L2")) {
      levelL2Toggle.setBorder(new Border(highlight));
    }
    if (stringList.contains("1")) {
      level1Toggle.setBorder(new Border(highlight));
    }
    if (stringList.contains("2")) {
      level2Toggle.setBorder(new Border(highlight));
    }
    if (stringList.contains("3")) {
      level3Toggle.setBorder(new Border(highlight));
    }
  }

  /**
   * Helper method centers map in the gesture pane given X and Y input
   *
   * @param x takes x coordinate as int to center about
   * @param y takes Y coordinate as int to center about
   * @param zoom double to scale zoom
   */
  public void centerMap(int x, int y, double zoom) {
    Platform.runLater(
        () -> {
          gesturePane.centreOn(new Point2D(x, y));
          gesturePane.zoomTo(zoom, new Point2D(x, y));
        });
  }

  /** Helper method clears path */
  public void clearPath() {
    topPane.getChildren().clear();
  }

  /** Helper method displays admin message fields upon drawing path */
  public void displayAdminMessage() {
    adminMessage.setText(adminMessageTextField.getText());
    messageVbox.setVisible(true);
    adminMessageInput.setVisible(false);
    levelControls.setVisible(false);
  }

  /** Helper method resets admin message fields after drawing path */
  private void resetAdminMessageInput() {
    // display admin message input
    adminMessage.setText("");
    messageVbox.setVisible(false);
    adminMessageInput.setVisible(true);
    levelControls.setVisible(true);
  }
}
