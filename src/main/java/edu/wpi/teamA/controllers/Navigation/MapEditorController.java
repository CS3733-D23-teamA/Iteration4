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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

public class MapEditorController {
  private final MapEntity entity = App.getMapEntity();

  // larger panes
  @FXML private ImageView mapImageView = new ImageView();
  @FXML private Pane topPane = new Pane();
  @FXML private GesturePane mapGesturePane;
  @FXML private VBox mapEditorControls;
  @FXML private MFXGenericDialog inputDialog;

  @FXML private MFXGenericDialog movesInputDialog;

  @FXML private StackPane mapStackPane = new StackPane(mapImageView, topPane);

  // level buttons and image
  @FXML private MFXButton levelL1Button;
  @FXML private MFXButton levelL2Button;
  @FXML private MFXButton level1Button;
  @FXML private MFXButton level2Button;
  @FXML private MFXButton level3Button;
  @FXML private Image mapImage = App.getMapL1();
  private String level = "L1";

  // buttons and toggles
  @FXML private MFXButton modifyEdgeButton;
  @FXML private MFXToggleButton locationNameToggle;

  // text displays
  @FXML private Text locationDisplay;
  @FXML private Text editMapDirections;
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
  @FXML private MFXButton hAlignmentButton;
  @FXML private MFXButton vAlignmentButton;

  // moves input dialog

  // @FXML private MFXFilterComboBox<String> movesNodeTypeCombo;
  @FXML private DatePicker movesDateForMove;
  @FXML private MFXButton movesSubmitButton;
  @FXML private Text moveToText;
  @FXML private MFXFilterComboBox<String> locationMoving;

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
  // private boolean addMoveClicked;
  private boolean moveNodeMoveToBool;

  // variables for storing important data
  private int currentNodeID;
  private int[] XYCoords = new int[2];
  private Circle currentCircle;
  private Circle currentPositionClicked;
  private Node firstNode;
  private Node moveNodeMoveTo;

  /** Used to initialize the screen and inputs */
  public void initialize() {
    mapGesturePane.setGestureEnabled(true);
    // set up level buttons
    levelL1Button.setOnAction(event -> changeLevelText(levelL1Button));
    levelL2Button.setOnAction(event -> changeLevelText(levelL2Button));
    level1Button.setOnAction(event -> changeLevelText(level1Button));
    level2Button.setOnAction(event -> changeLevelText(level2Button));
    level3Button.setOnAction(event -> changeLevelText(level3Button));

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

    // center and zoom onto map content
    Platform.runLater(
        () -> {
          mapGesturePane.centreOn(new Point2D(2265, 950));
          mapGesturePane.zoomTo(0.5, new Point2D(2265, 950));
        });

    // set up dialog box visiblity
    clearDialogBoxes();

    changeLevelText(levelL1Button);

    inputDialog.setOnClose(
        event -> {
          clearDialogBoxes();
        });
    movesInputDialog.setOnClose(
        event -> {
          clearDialogBoxes();
        });

    // set up input grid
    floorField.getItems().addAll("G", "L1", "L2", "1", "2", "3");
    buildingField.getItems().addAll("15 Francis", "45 Francis", "BTM", "Shapiro", "Tower");
    nodeTypeField
        .getItems()
        .addAll(
            "CONF", "DEPT", "ELEV", "EXIT", "HALL", "INFO", "LABS", "REST", "RETL", "SERV", "STAI");

    // set up moves input dialog
    locationMoving.getItems().addAll(entity.getAllLongNames());
  }

  /**
   * Used to change the level chooser text based on which floor
   *
   * @param button button chosen
   */
  private void changeLevelText(MFXButton button) {
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

    // display new dots and edges
    //
    //    displayEdgeData(Objects.requireNonNull(entity.determineEdgeMap(level)));
    //    displayNodeData(Objects.requireNonNull(entity.determineNodeMap(level)));
  }

  /**
   * Displays the node data for that floor in the form as dots on the map
   *
   * @param nodeMapForFloor the array with the data for that floor
   */
  private void displayNodeData(HashMap<Integer, Node> nodeMapForFloor) {

    for (Map.Entry<Integer, Node> entry : nodeMapForFloor.entrySet()) {
      // Group g = new Group();
      Node node = entry.getValue();
      if (node != null) {
        Circle circle = entity.addCircle(mapGesturePane, node);

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

        if (!entity.getLocationName(node.getNodeID()).getNodeType().equals("HALL")
            && locationNameToggle.isSelected()) {
          Text text = entity.addText(node);
          topPane.getChildren().add(text);
        }
      }
    }
    App.getPrimaryStage().show();
  }

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
    // nodeDescriptionVBox.setVisible(true);
    if (entity.getLocationName(nodeID).getNodeType().equals("HALL")) {
      locationDisplay.setText(entity.getLocationName(nodeID).getLongName());
    } else {
      locationDisplay.setText(entity.getLocationName(nodeID).getShortName());
    }

    /*javafx.scene.Node circleText = circle.getParent().getChildrenUnmodifiable().get(1);
    // String text = circleText.toString();
    Text text = (Text) circleText;
    Font font = new Font("Open Sans", 20);
    text.setFont(font);
    text.setStrokeWidth(2);*/
  }

  /**
   * Defines behavior for when you are not hovering over a dot on the map
   *
   * @param circle dot that you are hover over
   * @param nodeID nodeID of that node the dot is linked too
   */
  @FXML
  public void dotUnhover(Circle circle, int nodeID) {
    circle.setFill(Color.web("0x012D5A"));
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

    if (removeNodeClicked) {
      entity.determineRemoveAction(nodeID, level);
      circle.setDisable(true);
      circle.setVisible(false);
      removeNodeClicked = false;
      editMapDirections.setText("");

      topPane.getChildren().clear();
      displayEdgeData(entity.determineEdgeMap(level));
      displayNodeData(entity.determineNodeMap(level));
    }

    if (modifyNodeClicked) {
      popUpMainDialogBox();

      // get node information
      Node node = entity.getNodeInfo(nodeID);
      LocationName locName = entity.getLocationName(nodeID);

      // Preload information
      preLoadDialogInfo(node, locName);
      validateInputSubmit();

      // new location clicked
      mouseClickForNewLocation();

      editMapDirections.setText("");
    }

    if (modifyEdgeClicked) {
      // click another node
      editMapDirections.setText("Click the nodes you want to modify its edges.");
      secondNodeClicked = true;
      firstNode = entity.getNodeInfo(nodeID);
      modifyEdgeClicked = false;
      modifyEdgeButton.setText("Stop Modify Edge");
    }

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
    removeNodeClicked = true;
    modifyNodeClicked = false;
    addNodeClicked = false;
    modifyEdgeClicked = false;
    alignNodesClicked = false;
    editMapDirections.setText("Click a dot on the map to remove");
  }

  /** Sets up the screen for the user to modify a node */
  @FXML
  public void modifyNode() {
    removeNodeClicked = false;
    modifyNodeClicked = true;
    addNodeClicked = false;
    modifyEdgeClicked = false;
    alignNodesClicked = false;
    editMapDirections.setText("Click a dot on the map to modify");
  }

  @FXML
  public void modifyEdge() {
    if (modifyEdgeButton.getText().equals("Modify Edge")) {
      modifyEdgeClicked = true;
      removeNodeClicked = false;
      modifyNodeClicked = false;
      addNodeClicked = false;
      alignNodesClicked = false;

      editMapDirections.setText("Click a node to modify its edges.");
    } else {
      secondNodeClicked = false;
      modifyEdgeClicked = false;
      editMapDirections.setText("");
      modifyEdgeButton.setText("Modify Edge");
    }
  }

  /** Pops up the input dialog page and hides map editor controls */
  private void popUpMainDialogBox() {
    inputDialog.setVisible(true);
    inputDialog.setDisable(false);
  }

  /** Hides the input dialog page and brings back map editor controls */
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
    removeNodeClicked = false;
    modifyNodeClicked = false;
    addNodeClicked = true;
    alignNodesClicked = false;
    modifyEdgeClicked = false;
    floorField.selectItem(level);
    floorField.setDisable(true);
    // pop up dialog box
    popUpMainDialogBox();

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
    if (longNameField.getText().isEmpty()
        || (entity.determineLongNameExists(longNameField.getText()) && !longNameField.isDisable())
        || shortNameField.getText().isEmpty()
        || floorField.getSelectedIndex() == -1
        || buildingField.getSelectedIndex() == -1
        || nodeTypeField.getSelectedIndex() == -1
        || XYCoords[0] < 0
        || XYCoords[1] < 0) {
      submitButton.setDisable(true);
    } else {
      submitButton.setDisable(false);
    }
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

  @FXML
  public void submit() {
    // once submit button has been clicked, update database
    Node node =
        new Node(
            currentNodeID, XYCoords[0], XYCoords[1], floorField.getText(), buildingField.getText());
    LocationName locName =
        new LocationName(
            longNameField.getText(), shortNameField.getText(), nodeTypeField.getText());
    Move move = new Move(currentNodeID, longNameField.getText(), App.getCurrentDate());
    if (modifyNodeClicked) {
      entity.determineModifyAction(level, node, locName, move);
      currentCircle.setVisible(false);
      currentCircle.setDisable(true);
    } else if (addNodeClicked) {
      entity.determineAddAction(level, node, locName, move);
    }

    clearDialogBoxes();
    topPane.getChildren().clear();
    topPane.setOnMouseClicked(null);
    displayEdgeData(entity.determineEdgeMap(level));
    displayNodeData(entity.determineNodeMap(level));
  }

  /**
   * Used to store the coordinates of when the user clicks on the map for adding or modifying a node
   */
  private void mouseClickForNewLocation() {
    currentPositionClicked = null;
    topPane.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
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
          }
        });
  }

  @FXML
  public void clickAlignNodesButton() {
    if (AlignNodesButton.getText().equals("Align Nodes")) {
      stopAlignment = false;
      alignNodesClicked = true;
      nodesToAlign =
          new ArrayList<>(); // initializes the array before an alignment is done/before the
      // selection

      editMapDirections.setText(
          "Select the nodes you want to align. Then, click how you would like the nodes to be aligned (H/V)");
      AlignNodesButton.setText("Stop Alignment");
      alignmentHBox.setVisible(true);
    } else {
      alignNodesClicked = false;
      stopAlignment = true;
      editMapDirections.setText("");
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

  @FXML
  public void horizontalNodeAlignment() {
    editMapDirections.setText("");
    horizontal = true;
    vertical = false;
    alignNodesClicked = true;
    stopAlignment = false;
    alignmentHBox.setVisible(false);
  }

  @FXML
  public void verticalNodeAlignment() {
    editMapDirections.setText("");
    alignNodesClicked = true;
    stopAlignment = false;
    vertical = true;
    horizontal = false;
    alignmentHBox.setVisible(false);
  }

  @FXML
  public void clickAddMove() {
    movesInputDialog.setVisible(true);
    movesInputDialog.setDisable(false);

    modifyEdgeClicked = false;
    removeNodeClicked = false;
    modifyNodeClicked = false;
    addNodeClicked = false;
    alignNodesClicked = false;

    moveToText.setText("");
  }

  @FXML
  public void validateMovesInputSubmit() {
    if (moveToText.getText().isEmpty()
        || locationMoving.getSelectedItem().isEmpty()
        || movesDateForMove.getValue() == null) {
      movesSubmitButton.setDisable(true);
    } else {
      movesSubmitButton.setDisable(false);
    }
  }

  @FXML
  public void movesSubmit() {
    int nodeID = moveNodeMoveTo.getNodeID();
    String longName = locationMoving.getSelectedItem();
    LocalDate localDate = movesDateForMove.getValue();

    Move move = new Move(nodeID, longName, localDate);

    entity.submitNewMoves(move);
    clearDialogBoxes();
    displayEdgeData(entity.determineEdgeMap(level));
    displayNodeData(entity.determineNodeMap(level));
  }

  @FXML
  public void chooseMoveToNode() {
    moveNodeMoveToBool = true;
  }
}
