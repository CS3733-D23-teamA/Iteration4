package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
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
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
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

  // Boolean for admin settings
  private Boolean isAdmin = AccountSingleton.isAdmin();

  // Boolean for user submit
  private Boolean isSubmitted = false;

  // Current Level Object
  private Level currentLevel = Level.LOWERLEVELL1;

  private DataBaseRepository db = DataBaseRepository.getInstance();

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

  @FXML private VBox errorMessage;

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
  @FXML private MFXToggleButton noStairs;

  private boolean accessibilityLevel = false;

  @FXML ImageView locationImage;
  @FXML VBox turnDirections;

  public void initialize() {

    locationImage.setImage(App.getLocationPF());

    // setting search algortihm selection visibility based on access level
    if (!isAdmin) {
      searchAlgorithmVbox.setVisible(false);
      searchAlgorithmVbox.setManaged(false);
    }
    errorMessage.setVisible(false);

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
    noStairs.setOnAction(event -> setAccessibility());
  }

  /**
   * sets the accessibility for the path
   *
   * <p>elevator, 2 = no stairs
   */
  private void setAccessibility() {
    accessibilityLevel = !accessibilityLevel;
    if (isSubmitted) {
      submit();
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
    if (mapEntity.nextLevelExists()) {
      nextLevel.setStyle("-fx-fill: #012D5A");
    } else { // else set next to grey
      nextLevel.setStyle("-fx-fill: #98AABC");
    }

    // set prev blue as we paginate next
    if (mapEntity.prevLevelExists()) {
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
        levelL1Toggle.setSelected(true);
        break;
      case "L2":
        setCurrentLevel(Level.LOWERLEVELL2);
        levelL2Toggle.setSelected(true);
        break;
      case "1":
        setCurrentLevel(Level.LEVEL1);
        level1Toggle.setSelected(true);
        break;
      case "2":
        setCurrentLevel(Level.LEVEL2);
        level2Toggle.setSelected(true);
        break;
      case "3":
        setCurrentLevel(Level.LEVEL3);
        level3Toggle.setSelected(true);
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

    resetButtonIndicator();

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

    if (setTurnDirections()) {
      isSubmitted = true;
      // Sets the Order for paginator
      mapEntity.setLevelOrder(SearchSingleton.getPath());
      levelButtonIndicator(SearchSingleton.getPath());
      // Changes the levels for pathfinding globally
      changeLevel(mapEntity.getFirstLevel().toString());

      // centers map on path
      centerMap(
          mapEntity.getNodeInfo(startID).getXcoord(),
          mapEntity.getNodeInfo(startID).getYcoord(),
          1);

      drawPath();
    } else {
      isSubmitted = false;
      clearPath();
      resetPaginator();
    }
  }

  private void resetPaginator() {
    mapEntity.setLevelOrder();
    switch (currentLevel) {
      case LOWERLEVELL1:
        setCurrentLevel(Level.LOWERLEVELL1);
        break;
      case LOWERLEVELL2:
        setCurrentLevel(Level.LOWERLEVELL2);
        getNextLevel();
        break;
      case LEVEL1:
        setCurrentLevel(Level.LEVEL1);
        getNextLevel();
        getNextLevel();
        break;
      case LEVEL2:
        setCurrentLevel(Level.LEVEL2);
        getNextLevel();
        getNextLevel();
        getNextLevel();
        break;
      case LEVEL3:
        setCurrentLevel(Level.LEVEL3);
        getNextLevel();
        getNextLevel();
        getNextLevel();
        getNextLevel();
        break;
    }
    setPaginator();
  }

  private boolean setTurnDirections() {
    turnDirections.getChildren().clear();
    errorMessage.setVisible(false);

    // loop through array of directions
    ArrayList<String> path = SearchSingleton.simplePathArray(secondNameToggle.isSelected());
    if (path == null) {
      ImageView icon = new ImageView();
      icon.setFitHeight(50);
      icon.setFitWidth(50);
      icon.setImage(App.getFrown());
      Label directions = new Label();
      HBox turn = new HBox(15);
      turn.setStyle(
          "-fx-background-color: #f1f1f1; -fx-background-radius: 10; -fx-padding: 10; -fx-alignment: center-left");

      directions.setText("There is no available path.");
      directions.setWrapText(true);
      turn.getChildren().addAll(icon, directions);
      turnDirections.getChildren().add(turn);
      return false;
    } else if (path.size() == 1) {
      ImageView icon = new ImageView();
      icon.setFitHeight(50);
      icon.setFitWidth(50);
      icon.setImage(App.getSmile());
      Label directions = new Label();
      HBox turn = new HBox(15);
      turn.setStyle(
          "-fx-background-color: #f1f1f1; -fx-background-radius: 10; -fx-padding: 10; -fx-alignment: center-left");

      directions.setText(path.get(0));
      directions.setWrapText(true);
      turn.getChildren().addAll(icon, directions);
      turnDirections.getChildren().add(turn);
    } else {
      for (int i = 0; i < path.size(); i++) {
        ImageView icon = new ImageView();
        icon.setFitHeight(50);
        icon.setFitWidth(50);
        Label directions = new Label();
        HBox turn = new HBox(15);
        turn.setStyle(
            "-fx-background-color: #f1f1f1; -fx-background-radius: 10; -fx-padding: 10; -fx-alignment: center-left");
        if (path.get(i).contains("up")) {
          icon.setImage(App.getUp());
        } else if (path.get(i).contains("down")) {
          icon.setImage(App.getDown());
        } else if (path.get(i).contains("left")) {
          icon.setImage(App.getLeft());
        } else {
          icon.setImage(App.getRight());
        }

        directions.setText(path.get(i));
        directions.setWrapText(true);
        turn.getChildren().addAll(icon, directions);
        turnDirections.getChildren().add(turn);
      }
    }
    return true;
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
        }
      }
    } else {
      if (startSelection.getValue() != null && endSelection.getValue() != null) {
        submit();
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
      GraphNode pastNode = SearchSingleton.getGraphNode(pathIDs.get(i - 1));
      if (currentLevel.toString().equals(gNode.getFloor())
          && currentLevel.toString().equals(pastNode.getFloor())) {
        Circle circle = drawAnimatedLine(lastX, lastY, gNode.getXcoord(), gNode.getYcoord());
        topPane.getChildren().add(circle);
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

  private Circle drawAnimatedLine(int startX, int startY, int endX, int endY) {
    Circle pathDashedLine = new Circle();
    pathDashedLine.setRadius(8);
    pathDashedLine.setFill(Color.web("#012D5A"));

    Path path = new Path();
    path.getElements().add(new MoveTo(startX, startY));
    path.getElements().add(new LineTo(endX, endY));

    PathTransition animation = new PathTransition();
    animation.setPath(path);
    animation.setDuration(Duration.millis(1000));
    animation.setNode(pathDashedLine);
    animation.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.play();

    return pathDashedLine;
  }

  private Line drawRegularLine(int startX, int startY, int endX, int endY) {
    Line line = new Line(startX, startY, endX, endY);
    line.setFill(Color.web("#012D5A"));
    line.setStrokeWidth(7);
    return line;
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
    String highlight = "-fx-border-width: 5; -fx-border-color: #F0C747; -fx-border-radius: 3";

    ArrayList<String> stringList = mapEntity.floorsTravelledTo(nodeIDS);
    if (stringList.contains("L1")) {
      levelL1Toggle.setStyle(highlight);
    }
    if (stringList.contains("L2")) {
      levelL2Toggle.setStyle(highlight);
    }
    if (stringList.contains("1")) {
      level1Toggle.setStyle(highlight);
    }
    if (stringList.contains("2")) {
      level2Toggle.setStyle(highlight);
    }
    if (stringList.contains("3")) {
      level3Toggle.setStyle(highlight);
    }
  }

  private void resetButtonIndicator() {
    String highlight = "-fx-border-width: 5; -fx-border-color: TRANSPARENT; -fx-border-radius: 3";
    levelL1Toggle.setStyle(highlight);
    levelL2Toggle.setStyle(highlight);
    level1Toggle.setStyle(highlight);
    level2Toggle.setStyle(highlight);
    level3Toggle.setStyle(highlight);
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
