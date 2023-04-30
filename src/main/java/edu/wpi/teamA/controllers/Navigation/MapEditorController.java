package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Move;
import edu.wpi.teamA.database.ORMclasses.Node;
import edu.wpi.teamA.entities.MapEntity;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import java.time.LocalDate;
import java.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

public class MapEditorController {
  private final MapEntity entity = App.getMapEntity();

  // larger panes
  @FXML private final ImageView mapImageView = new ImageView();
  @FXML private final Pane topPane = new Pane();
  @FXML private GesturePane mapGesturePane;
  @FXML private MFXGenericDialog inputDialog;

  @FXML private MFXGenericDialog movesInputDialog;

  @FXML private final StackPane mapStackPane = new StackPane(mapImageView, topPane);

  // level toggle buttons
  private ToggleGroup levelToggles = new ToggleGroup();
  @FXML private MFXRectangleToggleNode levelL1Toggle;
  @FXML private MFXRectangleToggleNode levelL2Toggle;
  @FXML private MFXRectangleToggleNode level1Toggle;
  @FXML private MFXRectangleToggleNode level2Toggle;
  @FXML private MFXRectangleToggleNode level3Toggle;
  @FXML private Image mapImage = App.getMapL1();
  // private Level currentLevel = Level.LOWERLEVELL1;
  private String level = "L1";

  // buttons and toggles
  @FXML private MFXButton modifyEdgeButton;
  @FXML private MFXToggleButton locationNameToggle;
  @FXML private MFXToggleButton secondNameToggle;

  // text displays
  @FXML private Text locationDisplay;
  @FXML private Text levelDisplay;

  // input dialog box
  @FXML private MFXTextField longNameField;
  @FXML private MFXTextField shortNameField;
  @FXML private MFXComboBox<String> floorField;
  @FXML private MFXComboBox<String> buildingField;
  @FXML private MFXComboBox<String> nodeTypeField;
  @FXML private MFXButton submitButton;

  // alignment variables
  @FXML private MFXButton AlignNodesButton;
  @FXML private HBox alignmentHBox;

  // moves input dialog
  @FXML private DatePicker movesDateForMove;
  @FXML private MFXButton movesSubmitButton;
  @FXML private Text moveToText;
  @FXML private MFXFilterComboBox<String> locationMoving;
  @FXML private Text moveError;

  // alignment variables
  private boolean alignNodesClicked;
  private boolean stopAlignment;
  private boolean horizontal;
  private boolean vertical;
  private ArrayList<Node> nodesToAlign = new ArrayList<>();

  // booleans for add, remove, modify
  private boolean removeNodeClicked;
  private boolean addNodeClicked;
  private boolean modifyNodeClicked;
  private boolean modifyEdgeClicked;
  private boolean secondNodeClicked;
  private boolean moveNodeMoveToBool;

  // variables for storing important data
  private int currentNodeID;
  private int[] XYCoords = new int[2];
  private Circle currentCircle;
  private int currentModifyNodeID;
  private Circle currentEdgeCircle;
  private Circle currentPositionClicked;
  private Node firstNode;
  private Node moveNodeMoveTo;

  /** Used to initialize the screen and inputs */
  public void initialize() {
    mapGesturePane.setGestureEnabled(true);

    // level toggle buttons set up
    levelL1Toggle.setToggleGroup(levelToggles);
    levelL2Toggle.setToggleGroup(levelToggles);
    level1Toggle.setToggleGroup(levelToggles);
    level2Toggle.setToggleGroup(levelToggles);
    level3Toggle.setToggleGroup(levelToggles);

    levelL1Toggle.setOnAction(event -> changeLevelText(levelL1Toggle));
    levelL2Toggle.setOnAction(event -> changeLevelText(levelL2Toggle));
    level1Toggle.setOnAction(event -> changeLevelText(level1Toggle));
    level2Toggle.setOnAction(event -> changeLevelText(level2Toggle));
    level3Toggle.setOnAction(event -> changeLevelText(level3Toggle));

    // set up page
    mapGesturePane.setContent(mapStackPane);
    mapGesturePane.setScrollMode(GesturePane.ScrollMode.ZOOM);
    removeNodeClicked = false;
    addNodeClicked = false;
    modifyNodeClicked = false;
    alignNodesClicked = false;
    this.mapImageView.setImage(mapImage);
    modifyEdgeClicked = false;
    secondNodeClicked = false;
    stopAlignment = false;
    nodesToAlign = new ArrayList<>();
    alignmentHBox.setVisible(false);
    changeLevelText(levelL1Toggle);
    levelL1Toggle.setSelected(true);

    // center and zoom onto map content
    Platform.runLater(
        () -> {
          mapGesturePane.centreOn(new Point2D(2265, 950));
          mapGesturePane.zoomTo(0.5, new Point2D(2265, 950));
        });

    // set up dialog box visiblity
    clearDialogBoxes();

    inputDialog.setOnClose(event -> clearDialogBoxes());
    movesInputDialog.setOnClose(event -> clearDialogBoxes());

    // set up input grid
    floorField.getItems().addAll("L1", "L2", "1", "2", "3");
    buildingField.getItems().addAll("15 Francis", "45 Francis", "BTM", "Shapiro", "Tower");
    nodeTypeField
        .getItems()
        .addAll(
            "CONF", "DEPT", "ELEV", "EXIT", "HALL", "INFO", "LABS", "REST", "RETL", "SERV", "STAI");

    // set up moves input dialog
    locationMoving.getItems().addAll(entity.getAllLongNames());
    moveError.setVisible(false);
  }

  /**
   * Used to change the level chooser text based on which floor
   *
   * @param button button chosen
   */
  private void changeLevelText(MFXRectangleToggleNode button) {
    // get pre-loaded map image from App
    switch (button.getText()) {
      case "L1":
        mapImage = App.getMapL1();
        levelDisplay.setText("Level L1");
        break;
      case "L2":
        mapImage = App.getMapL2();
        levelDisplay.setText("Level L2");
        break;
      case "1":
        mapImage = App.getMap1();
        levelDisplay.setText("Level 1");
        break;
      case "2":
        mapImage = App.getMap2();
        levelDisplay.setText("Level 2");
        break;
      case "3":
        mapImage = App.getMap3();
        levelDisplay.setText("Level 3");
        break;
    }

    // set map image
    this.mapImageView.setImage(Objects.requireNonNull(mapImage));

    // hide old dots
    topPane.getChildren().clear();

    // button
    level = button.getText();
    changeLocationNameDisplay();
  }

  /**
   * Displays the node data for that floor in the form as dots on the map
   *
   * @param nodeMapForFloor the array with the data for that floor
   */
  private void displayNodeData(HashMap<Integer, Node> nodeMapForFloor) {

    for (Map.Entry<Integer, Node> entry : nodeMapForFloor.entrySet()) {
      Node node = entry.getValue();
      if (node != null) {
        Circle circle = entity.addCircle(mapGesturePane, node);

        if (node.getNodeID() == currentModifyNodeID) {
          circle.setFill(Color.web("0xf74c4c"));
        }

        circle.setOnMouseReleased(
            mouseEvent -> {
              entity.dragReleased(circle, node, mapGesturePane);
              topPane.getChildren().clear();
              displayEdgeData(entity.determineEdgeMap(node.getFloor()));
              displayNodeData(entity.determineNodeMap(node.getFloor()));
              mapGesturePane.setGestureEnabled(true);
            });

        circle.setOnMouseEntered(event -> dotHover(circle, node.getNodeID()));
        circle.setOnMouseExited(event -> dotUnhover(circle, node.getNodeID()));
        circle.setOnMousePressed(
            event -> {
              mapGesturePane.setGestureEnabled(false);
              dotClicked(circle, node.getNodeID());
            });

        topPane.getChildren().add(circle);

        if (!entity
                .getLocationName(node.getNodeID(), secondNameToggle.isSelected())
                .getNodeType()
                .equals("HALL")
            && locationNameToggle.isSelected()) {
          Text text = entity.addText(node, secondNameToggle.isSelected());
          topPane.getChildren().add(text);
        }
      }
    }
    App.getPrimaryStage().show();
  }

  /**
   * Displays the edge data for that floor in the form as lines on the map
   *
   * @param edgeMapForFloor the array with the data for that floor
   */
  private void displayEdgeData(HashMap<String, Edge> edgeMapForFloor) {
    for (Map.Entry<String, Edge> entry : edgeMapForFloor.entrySet()) {
      Edge edge = entry.getValue();
      if (entity
          .getNodeInfo(edge.getStartNode())
          .getFloor()
          .equals(entity.getNodeInfo(edge.getEndNode()).getFloor())) {
        Line line = entity.addLine(edge.getStartNode(), edge.getEndNode());
        topPane.getChildren().add(line);
      }
    }
    App.getPrimaryStage().show();
  }

  /** Resets the display for the map editor display */
  @FXML
  public void changeLocationNameDisplay() {
    topPane.getChildren().clear();
    displayEdgeData(Objects.requireNonNull(entity.determineEdgeMap(level)));
    displayNodeData(Objects.requireNonNull(entity.determineNodeMap(level)));
  }

  /**
   * Defines behavior for when you hover over a dot on the map
   *
   * @param circle dot that you are hover over
   * @param nodeID nodeID of that node the dot is linked too
   */
  private void dotHover(Circle circle, int nodeID) {
    circle.setFill(Color.web("0xEEBD28"));
    if (entity
        .getLocationName(nodeID, secondNameToggle.isSelected())
        .getNodeType()
        .equals("HALL")) {
      locationDisplay.setText(
          entity.getLocationName(nodeID, secondNameToggle.isSelected()).getLongName());
    } else {
      locationDisplay.setText(
          entity.getLocationName(nodeID, secondNameToggle.isSelected()).getShortName());
    }
  }

  /**
   * Defines behavior for when you are not hovering over a dot on the map
   *
   * @param circle dot that you are hover over
   * @param nodeID nodeID of that node the dot is linked too
   */
  @FXML
  public void dotUnhover(Circle circle, int nodeID) {
    int code = entity.numberOfLocationsOnNode(nodeID);

    if (nodeID == currentModifyNodeID) {
      circle.setFill(Color.web("0xf74c4c"));
    } else {
      if (code == 0) {
        circle.setFill(Color.web("0xf74c4c"));
      } else if (code == 1) {
        circle.setFill(Color.web("0x012D5A"));
      } else {
        circle.setFill(Color.web("0x4cde61"));
      }
    }
  }

  /**
   * Defines behavior for when you click a dot on the map
   *
   * @param circle dot that you are clicking
   * @param nodeID nodeID of that node the dot is linked too
   */
  @FXML
  public void dotClicked(Circle circle, Integer nodeID) {
    currentNodeID = nodeID;
    currentCircle = circle;

    // for if removing a node
    if (removeNodeClicked) {
      entity.determineRemoveAction(nodeID, level);
      circle.setDisable(true);
      circle.setVisible(false);
      removeNodeClicked = false;

      topPane.getChildren().clear();
      displayEdgeData(entity.determineEdgeMap(level));
      displayNodeData(entity.determineNodeMap(level));
    }

    // for if modifying a node
    if (modifyNodeClicked) {
      currentModifyNodeID = nodeID;
      circle.setFill(Color.web("0xf74c4c"));
      popUpInputDialogBox();

      // get node information
      Node node = entity.getNodeInfo(nodeID);
      LocationName locName = entity.getLocationName(nodeID, secondNameToggle.isSelected());

      // Preload information
      preLoadDialogInfo(node, locName);
      validateInputSubmit();

      // new location clicked
      mouseClickForNewLocation();
    }

    // for if modifying edges
    if (modifyEdgeClicked) {
      // click another node
      // currentEdgeCircle = circle;
      currentModifyNodeID = nodeID;
      circle.setFill(Color.web("0xf74c4c"));
      secondNodeClicked = true;
      firstNode = entity.getNodeInfo(nodeID);
      modifyEdgeClicked = false;
      modifyEdgeButton.setText("Stop Modify Edge");
    }

    // for when adding or removing edges from a node
    if (secondNodeClicked) {
      if (entity.determineModifyEdgeAction(firstNode, entity.getNodeInfo(nodeID), level)) {
        // add a line for the new edge
        Line line = entity.addLine(firstNode.getNodeID(), nodeID);
        topPane.getChildren().add(line);
      }
      topPane.getChildren().clear();
      displayEdgeData(entity.determineEdgeMap(level));
      displayNodeData(entity.determineNodeMap(level));
    }

    if (alignNodesClicked && !stopAlignment) {
      // if stop alignment is not clicked then continue to add the nodes to the arraylist
      nodesToAlign.add(entity.getNodeInfo(nodeID));
    }

    // for when choosing a node to move to when adding a move
    if (moveNodeMoveToBool) {
      moveNodeMoveTo = entity.getNodeInfo(nodeID);
      circle.setOnMouseEntered(null);
      circle.setOnMouseExited(null);
      circle.setFill(Color.web("0xf74c4c"));
      XYCoords[0] = (int) circle.getCenterX();
      XYCoords[1] = (int) circle.getCenterY();
      moveToText.setText(nodeID.toString());
      moveNodeMoveToBool = false;
      validateMovesInputSubmit();
    }
  }

  /** Sets up screen for the user to remove a node */
  @FXML
  public void removeNode() {
    shutDownAllDialogBoxes();
    removeNodeClicked = true;
    modifyNodeClicked = false;
    addNodeClicked = false;
    modifyEdgeClicked = false;
    alignNodesClicked = false;
  }

  /** Sets up the screen for the user to modify a node */
  @FXML
  public void modifyNode() {
    shutDownAllDialogBoxes();
    removeNodeClicked = false;
    modifyNodeClicked = true;
    addNodeClicked = false;
    modifyEdgeClicked = false;
    alignNodesClicked = false;
  }

  /** Sets up the screen for the user to modify an edge */
  @FXML
  public void modifyEdge() {
    shutDownAllDialogBoxes();
    if (modifyEdgeButton.getText().equals("Modify Edge")) {
      modifyEdgeClicked = true;
      removeNodeClicked = false;
      modifyNodeClicked = false;
      addNodeClicked = false;
      alignNodesClicked = false;

    } else {
      secondNodeClicked = false;
      modifyEdgeClicked = false;
      currentModifyNodeID = -1;
      modifyEdgeButton.setText("Modify Edge");
      displayEdgeData(entity.determineEdgeMap(level));
      displayNodeData(entity.determineNodeMap(level));
    }
  }

  /** Pops up the input dialog page */
  private void popUpInputDialogBox() {
    inputDialog.setVisible(true);
    inputDialog.setDisable(false);
  }

  /** Hides all dialog boxes */
  private void shutDownAllDialogBoxes() {
    if (currentPositionClicked != null) {
      topPane.getChildren().remove(currentPositionClicked);
    }
    inputDialog.setDisable(true);
    inputDialog.setVisible(false);
    movesInputDialog.setDisable(true);
    movesInputDialog.setVisible(false);

    removeNodeClicked = false;
    modifyNodeClicked = false;
    addNodeClicked = false;
    topPane.setOnMouseClicked(null);
  }

  /** Preload information in dialog box for the user to modify a node */
  private void preLoadDialogInfo(Node node, LocationName locName) {
    String oldLongName = locName.getLongName();
    String oldShortName = locName.getShortName();
    longNameField.setText(oldLongName);
    longNameField.setDisable(true);
    shortNameField.setText(oldShortName);
    floorField.getSelectionModel().selectItem(node.getFloor());
    buildingField.getSelectionModel().selectItem(node.getBuilding());
    nodeTypeField.getSelectionModel().selectItem(locName.getNodeType());
    XYCoords[0] = node.getXcoord();
    XYCoords[1] = node.getYcoord();
  }

  /** Sets up screen for the user to add a node */
  @FXML
  public void addNode() {
    shutDownAllDialogBoxes();
    removeNodeClicked = false;
    modifyNodeClicked = false;
    addNodeClicked = true;
    alignNodesClicked = false;
    modifyEdgeClicked = false;
    floorField.selectItem(level);
    floorField.setDisable(true);
    // pop up dialog box
    popUpInputDialogBox();

    // resets to negative so we know if the user has clicked on a location yet
    XYCoords[0] = -1;
    XYCoords[1] = -1;

    // set map click thingy
    mouseClickForNewLocation();
  }

  /** Enables the submit button once user has put in sufficient information in the dialog box */
  @FXML
  public void validateInputSubmit() {
    if (entity.determineLongNameExists(longNameField.getText()) && !longNameField.isDisable()) {
      longNameField.setBorder(Border.stroke(Color.web("0xf74c4c")));
    } else {
      longNameField.setBorder(Border.stroke(Color.web("0x000000")));
    }
    submitButton.setDisable(
        longNameField.getText().isEmpty()
            || (entity.determineLongNameExists(longNameField.getText())
                && !longNameField.isDisable())
            || shortNameField.getText().isEmpty()
            || floorField.getSelectedIndex() == -1
            || buildingField.getSelectedIndex() == -1
            || nodeTypeField.getSelectedIndex() == -1
            || XYCoords[0] < 0
            || XYCoords[1] < 0);
  }

  /**
   * Clears the dialog box selection after submit button is clicked and shuts down the dialog box
   */
  private void clearDialogBoxes() {
    submitButton.setDisable(true);
    longNameField.clear();
    longNameField.setDisable(false);
    shortNameField.clear();
    floorField.getSelectionModel().clearSelection();
    floorField.setDisable(false);
    buildingField.getSelectionModel().clearSelection();
    nodeTypeField.getSelectionModel().clearSelection();

    moveToText.setText("");
    locationMoving.getSelectionModel().clearSelection();
    movesDateForMove.setValue(null);
    movesSubmitButton.setDisable(true);
    shutDownAllDialogBoxes();
  }

  /** Submits the data in the input dialog depending on if it's modifying or adding */
  @FXML
  public void submit() {
    // once submit button has been clicked, update database
    Node node =
        new Node(
            currentNodeID, XYCoords[0], XYCoords[1], floorField.getText(), buildingField.getText());
    LocationName locName =
        new LocationName(
            longNameField.getText(), shortNameField.getText(), nodeTypeField.getText());
    if (modifyNodeClicked) {
      entity.determineModifyAction(level, node, locName);
      currentCircle.setVisible(false);
      currentCircle.setDisable(true);
    } else if (addNodeClicked) {
      Move move = new Move(currentNodeID, longNameField.getText(), App.getCurrentDate());
      entity.determineAddAction(level, node, locName, move);
    }

    clearDialogBoxes();
    topPane.getChildren().clear();
    topPane.setOnMouseClicked(null);
    currentModifyNodeID = -1;
    displayEdgeData(entity.determineEdgeMap(level));
    displayNodeData(entity.determineNodeMap(level));
  }

  /**
   * Used to store the coordinates of when the user clicks on the map for adding or modifying a node
   */
  private void mouseClickForNewLocation() {
    currentPositionClicked = null;
    topPane.setOnMouseClicked(
        event -> {
          if (currentPositionClicked != null) {
            topPane.getChildren().remove(currentPositionClicked);
          }
          double X = event.getX();
          double Y = event.getY();
          XYCoords = new int[2];
          XYCoords[0] = (int) (X);
          XYCoords[1] = (int) (Y);
          // new red circle indicating new location

          Circle circle = entity.addTempCircle(X, Y);
          topPane.getChildren().add(circle);
          currentPositionClicked = circle;
          validateInputSubmit();
        });
  }

  /** Sets up the screen for the user to align nodes */
  @FXML
  public void clickAlignNodesButton() {
    shutDownAllDialogBoxes();
    if (AlignNodesButton.getText().equals("Align Nodes")) {
      stopAlignment = false;
      alignNodesClicked = true;
      nodesToAlign =
          new ArrayList<>(); // initializes the array before an alignment is done/before the
      // selection
      AlignNodesButton.setText("Stop Alignment");
      alignmentHBox.setVisible(true);
    } else {
      alignNodesClicked = false;
      stopAlignment = true;
      AlignNodesButton.setText("Align Nodes");
      if (horizontal) {
        Node node = entity.determineHorizontalNodeAlignment(nodesToAlign);
        topPane.getChildren().clear();
        displayEdgeData(entity.determineEdgeMap(node.getFloor()));
        displayNodeData(entity.determineNodeMap(node.getFloor()));

      } else if (vertical) {
        Node node = entity.determineVerticalNodeAlignment(nodesToAlign);
        topPane.getChildren().clear();
        displayEdgeData(entity.determineEdgeMap(node.getFloor()));
        displayNodeData(entity.determineNodeMap(node.getFloor()));
      }
    }
  }

  /** Sets up the screen for horizontal alignment */
  @FXML
  public void horizontalNodeAlignment() {
    horizontal = true;
    vertical = false;
    alignNodesClicked = true;
    stopAlignment = false;
    alignmentHBox.setVisible(false);
  }

  /** Sets up the screen for horizontal alignment */
  @FXML
  public void verticalNodeAlignment() {
    alignNodesClicked = true;
    stopAlignment = false;
    vertical = true;
    horizontal = false;
    alignmentHBox.setVisible(false);
  }

  /** Sets up the screen for the user to add a move */
  @FXML
  public void clickAddMove() {
    shutDownAllDialogBoxes();
    movesInputDialog.setVisible(true);
    movesInputDialog.setDisable(false);

    modifyEdgeClicked = false;
    removeNodeClicked = false;
    modifyNodeClicked = false;
    addNodeClicked = false;
    alignNodesClicked = false;

    moveToText.setText("");
  }

  /** Checks to see if all fields are inputted before submitting a new move */
  @FXML
  public void validateMovesInputSubmit() {
    movesSubmitButton.setDisable(
        moveToText.getText().isEmpty()
            || locationMoving.getSelectedIndex() == -1
            || movesDateForMove.getValue() == null);
  }

  /** Submits the new move */
  @FXML
  public void movesSubmit() {
    int nodeID = moveNodeMoveTo.getNodeID();
    String longName = locationMoving.getSelectedItem();
    LocalDate localDate = movesDateForMove.getValue();

    if (entity.checkValidMove(nodeID, longName, localDate)) {
      moveError.setVisible(true);
    } else {
      Move move = new Move(nodeID, longName, localDate);

      entity.submitNewMoves(move);
      clearDialogBoxes();
      displayEdgeData(entity.determineEdgeMap(level));
      displayNodeData(entity.determineNodeMap(level));
      moveError.setVisible(false);
    }
  }

  /** Sets up screen for the user to choose a node for a move to go to */
  @FXML
  public void chooseMoveToNode() {
    moveNodeMoveToBool = true;
  }
}
