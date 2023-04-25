package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.entities.Level;
import edu.wpi.teamA.entities.MapEntity;
import edu.wpi.teamA.pathfinding.*;
import io.github.palexdev.materialfx.controls.*;
import java.util.ArrayList;
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

public class PathfindingController extends PageController {
  // Map helper entity
  private final MapEntity mapEntity = App.getMapEntity();

  // level toggle buttons
  private ToggleGroup levelToggles = new ToggleGroup();
  @FXML private MFXRectangleToggleNode levelL1Toggle;
  @FXML private MFXRectangleToggleNode levelL2Toggle;
  @FXML private MFXRectangleToggleNode level1Toggle;
  @FXML private MFXRectangleToggleNode level2Toggle;
  @FXML private MFXRectangleToggleNode level3Toggle;

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

  public void initialize() {

    // setting search algortihm selection visibility based on access level
    if (!AccountSingleton.INSTANCE.getValue().isAdmin()) {
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

    // Pagination buttons setup
    nextLevel.setOnMouseClicked(event -> changeLevel(getNextLevel()));
    prevLevel.setOnMouseClicked(event -> changeLevel(getPrevLevel()));

    // Set up Map in Gesture pane using a StackPane
    changeLevel(String.valueOf(currentLevel));
    gesturePane.setContent(stackPane);
    gesturePane.setGestureEnabled(true);

    // center and zoom onto map content
    Platform.runLater(
        () -> {
          gesturePane.centreOn(new Point2D(2265, 950));
          gesturePane.zoomTo(0.5, new Point2D(2265, 950));
        });
  }

  private String getNextLevel() {
    if (checkSelectionsForDraw()) {
      // TODO if selections are present, set next to next FROM PATH
      return mapEntity.getNextLevel().toString();
    }
    return mapEntity.getNextLevel().toString();
  }

  private String getPrevLevel() {
    if (checkSelectionsForDraw()) {
      // TODO if selections are present, set prev to prev FROM PATH
      return mapEntity.getPrevLevel().toString();
    }
    return mapEntity.getPrevLevel().toString();
  }

  /**
   * Helper for change level - updates map image and current level text
   *
   * @param level takes the current user selected level
   */
  private void setCurrentLevel(Level level) {
    currentLevel = level;
    mapImage = currentLevel.getMapImage(); // set image
    currentLevelLabel.setText("Level " + currentLevel); // set current level
    checkSelectionsForDraw();
  }

  /**
   * Sets corresponding map level image and global floor variable, sends check path to check each
   * floor for a path
   *
   * @param level takes the current user selected level
   */
  private void changeLevel(String level) {

    // toggle button functionality
    switch (level) {
      case "L1":
        setCurrentLevel(Level.LOWERLEVELL1);
        levelToggles.selectToggle(levelL1Toggle);
        break;
      case "L2":
        setCurrentLevel(Level.LOWERLEVELL2);
        levelToggles.selectToggle(levelL2Toggle);
        break;
      case "1":
        setCurrentLevel(Level.LEVEL1);
        levelToggles.selectToggle(level1Toggle);
        break;
      case "2":
        setCurrentLevel(Level.LEVEL2);
        levelToggles.selectToggle(level2Toggle);
        // levelL1Toggle.setEffect(new ColorAdjust(0, 0, 0.5, 0));
        break;
      case "3":
        setCurrentLevel(Level.LEVEL3);
        levelToggles.selectToggle(level3Toggle);
        break;
    }

    // set map image in image view
    this.mapView.setImage(Objects.requireNonNull(mapImage));
  }

  /**
   * Called upon user submit, clears the pane of drawings and implements Astar on start and end
   * selection, sends directions to the user and draws graphical path.
   */
  public void submit() {

    // get user input for start and end
    String startName = startSelection.getValue();
    String endName = endSelection.getValue();

    int startID = mapEntity.getIDFromLongName(startName);
    int endID = mapEntity.getIDFromLongName(endName);

    // sends user input starting and ending IDs and set search algorithm to singleton
    SearchSingleton.setSearchAlgorithm(searchAlgorithmSelection.getValue());
    SearchSingleton.createSearch(startID, endID);

    // set algorithm text
    searchAlgorithmSelection.setValue(SearchSingleton.getSearchAlgorithm().toString());

    // Sets the paginator
    mapEntity.setOrder(SearchSingleton.getPath());

    // set text directions
    directions.setText(SearchSingleton.pathString());
    searchAlgorithmTextDisplay.setText("Path found using " + SearchSingleton.getSearchAlgorithm());
    directions.setFill(Color.web("#151515"));

    // indicate floor buttons
    // ArrayList<Integer> test = searchEntity.getPath();

    // Sets the paginator
    mapEntity.setOrder(SearchSingleton.getPath());
    changeLevel(mapEntity.getFirstLevel().toString());
  }

  /**
   * wrapper for submit called upon user input, runs submit when user sets something for all inputs
   * returns true if starting and ending locations are set by user, false otherwise
   */
  @FXML
  public Boolean checkSelections() {
    if (AccountSingleton.INSTANCE.getValue().isAdmin()) {
      if (searchAlgorithmSelection.getValue() != null) {
        SearchSingleton.setSearchAlgorithm(searchAlgorithmSelection.getValue());
        if (startSelection.getSelectedItem() != null && endSelection.getSelectedItem() != null) {
          submit();
          return true;
        }
      }
    } else {
      if (startSelection.getSelectedItem() != null && endSelection.getSelectedItem() != null) {
        submit();
        return true;
      }
    }

    return false;
  }

  @FXML
  public Boolean checkSelectionsForDraw() {
    if (AccountSingleton.INSTANCE.getValue().isAdmin()) {
      if (searchAlgorithmSelection.getValue() != null) {
        SearchSingleton.setSearchAlgorithm(searchAlgorithmSelection.getValue());
        if (startSelection.getSelectedItem() != null && endSelection.getSelectedItem() != null) {
          drawPath();
          return true;
        }
      }
    } else {
      if (startSelection.getSelectedItem() != null && endSelection.getSelectedItem() != null) {
        drawPath();
        return true;
      }
    }

    return false;
  }

  /** Helper method for submit, draws graphical path and includes the starting and ending node */
  public void drawPath() {

    clearPath();
    // TODO path level indicator
    // starter code - highlights L1 upon every call of drawPath()
    BorderStroke highlight =
        new BorderStroke(
            Color.web("3788C8"), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(5));
    levelL1Toggle.setBorder(new Border(highlight));

    // TODO save path somehow - maybe in SearchSingleton

    // get list of node IDs and set graph node to gnode from search
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

  /** Helper method to clear path */
  public void clearPath() {
    topPane.getChildren().clear();
  }
}
