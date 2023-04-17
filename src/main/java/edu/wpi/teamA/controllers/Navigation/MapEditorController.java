package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.controllers.Map.MapEditorEntity;
import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Node;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
  private final MapEditorEntity entity = App.getMapEditorEntity();

  // larger panes
  @FXML private ImageView mapImageView = new ImageView();
  @FXML private Pane topPane = new Pane();
  @FXML private GesturePane mapGesturePane;
  @FXML private HBox mapEditorControls;
  @FXML private MFXGenericDialog inputDialog;
  @FXML private MFXGenericDialog impExpDialog;

  @FXML private StackPane mapStackPane = new StackPane(mapImageView, topPane);

  // level buttons and image
  @FXML private MFXButton levelL1Button;
  @FXML private MFXButton levelL2Button;
  @FXML private MFXButton level1Button;
  @FXML private MFXButton level2Button;
  @FXML private MFXButton level3Button;

  @FXML private Image mapImage = App.getMapL1();

  private String level = "L1";
  // @FXML private MFXButton stopModificationButton;

  @FXML private MFXButton modifyEdgeButton;

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

  // booleans for add, remove, modify
  private boolean removeNodeClicked;
  private boolean addNodeClicked;
  private boolean modifyNodeClicked;
  private boolean modifyEdgeClicked;
  private boolean secondNodeClicked;

  // booleans to determine importing or exporting
  private boolean imported = false;

  private int currentNodeID;
  private int[] XYCoords = new int[2];

  private String oldLongName;
  private String oldShortName;

  private Circle currentCircle;

  private Circle currentPositionClicked;

  private Node firstNode;

  // private final double scalar = 0.144;

  /** Used to initialize the screen and inputs */
  public void initialize() {
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
    this.mapImageView.setImage(mapImage);
    modifyEdgeClicked = false;
    secondNodeClicked = false;

    // set up dialog box visiblity
    mapEditorControls.setVisible(true);
    mapEditorControls.setDisable(false);
    inputDialog.setVisible(false);
    inputDialog.setDisable(true);
    submitButton.setDisable(true);
    impExpDialog.setVisible(false);
    impExpDialog.setDisable(true);
    //    stopModificationButton.setDisable(true);
    //    stopModificationButton.setVisible(false);
    // nodeDescriptionVBox.setVisible(false);

    changeLevelText(levelL1Button);

    inputDialog.setOnClose(
        event -> {
          clear();
        });
    impExpDialog.setOnClose(
        event -> {
          impExpDialog.setVisible(false);
          impExpDialog.setDisable(true);
          imported = false;
        });

    // set up input grid
    floorField.getItems().addAll("G", "L1", "L2", "1", "2", "3");
    buildingField.getItems().addAll("15 Francis", "45 Francis", "BTM", "Shapiro", "Tower");
    nodeTypeField
        .getItems()
        .addAll(
            "CONF", "DEPT", "ELEV", "EXIT", "HALL", "INFO", "LABS", "REST", "RETL", "SERV", "STAI");
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

    // display new dots and edges
    displayEdgeData(Objects.requireNonNull(entity.determineEdgeMap(level)));
    displayNodeData(Objects.requireNonNull(entity.determineNodeMap(level)));
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
        Circle circle = entity.addCircle(node.getXcoord(), node.getYcoord());
        circle.setOnMouseEntered(event -> dotHover(circle, node.getNodeID()));
        circle.setOnMouseExited(event -> dotUnhover(circle, node.getNodeID()));
        circle.setOnMouseClicked(event -> dotClicked(circle, node.getNodeID()));
        //      circle.setOnDragDetected(this::handleSetOnDragDetected);
        //      circle.setOnDragOver(event -> handleSetOnDragOver(circle, event));
        //      circle.setOnDragEntered(event -> handleSetOnDragEntered(circle, event));
        //      circle.setOnDragExited(event -> handleSetOnDragExited(circle, event));
        //      circle.setOnDragDropped(event -> handleSetOnDragDropped(circle, event));
        topPane.getChildren().add(circle);
      }
    }
    App.getPrimaryStage().show();
  }

  //  public void handleSetOnDragDetected(MouseEvent event) {
  //    if (modifyNodeClicked) {
  //      Dragboard db = topPane.startDragAndDrop(TransferMode.ANY);
  //
  //      event.consume();
  //    }
  //  }
  //
  //  public void handleSetOnDragOver(Circle circle, DragEvent event) {
  //    /* data is dragged over the target */
  //    /* accept it only if it is not dragged from the same node
  //     * and if it has a string data */
  //    if (event.getGestureSource() != circle) {
  //      /* allow for both copying and moving, whatever user chooses */
  //      event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
  //    }
  //    event.consume();
  //  }
  //
  //  public void handleSetOnDragEntered(Circle circle, DragEvent event) {
  //    /* the drag-and-drop gesture entered the target */
  //    /* show to the user that it is an actual gesture target */
  //    if (event.getGestureSource() != circle) {
  //      circle.setFill(Color.GREEN);
  //    }
  //    event.consume();
  //  }
  //
  //  public void handleSetOnDragExited(Circle circle, DragEvent event) {
  //    /* mouse moved away, remove the graphical cues */
  //    circle.setFill(Color.web("0x012D5A"));
  //    event.consume();
  //  }
  //
  //  public void handleSetOnDragDropped(Circle circle, DragEvent event) {
  //    /* data dropped */
  //    /* if there is a string data on dragboard, read it and use it */
  //    Dragboard db = event.getDragboard();
  //    /* let the source know whether the string was successfully
  //     * transferred and used */
  //    event.setDropCompleted(true);
  //    event.consume();
  //  }

  private void displayEdgeData(HashMap<String, Edge> edgeMapForFloor) {
    for (Map.Entry<String, Edge> entry : edgeMapForFloor.entrySet()) {
      Edge edge = entry.getValue();
      Line line = entity.addLine(edge.getStartNode(), edge.getEndNode());
      topPane.getChildren().add(line);
    }
    App.getPrimaryStage().show();
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
    // nodeDescriptionVBox.setVisible(false);
  }

  /**
   * Defines behavior for when you click a dot on the map
   *
   * @param circle dot that you are clicking
   * @param nodeID nodeID of that node the dot is linked too
   */
  @FXML
  public void dotClicked(Circle circle, int nodeID) {
    currentNodeID = nodeID;
    currentCircle = circle;

    if (removeNodeClicked) {
      entity.determineRemoveAction(nodeID, level);
      circle.setDisable(true);
      circle.setVisible(false);
      removeNodeClicked = false;
      editMapDirections.setText("");
    }

    if (modifyNodeClicked) {
      popUpMainDialogBox();

      // get node information
      Node node = entity.getNodeInfo(nodeID);
      LocationName locName = entity.getLocationName(nodeID);

      // Preload information
      preLoadDialogInfo(node, locName);
      validateButton();

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
      //      stopModificationButton.setDisable(false);
      //      stopModificationButton.setVisible(true);
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

    // editMapDirections.setText("");
  }

  //  @FXML
  //  public void stopModify() {
  //    secondNodeClicked = false;
  //    modifyEdgeClicked = false;
  //    // hide button
  //    stopModificationButton.setDisable(true);
  //    stopModificationButton.setVisible(false);
  //    editMapDirections.setText("");
  //  }

  /** Sets up screen for the user to remove a node */
  @FXML
  public void removeNode() {
    removeNodeClicked = true;
    modifyNodeClicked = false;
    addNodeClicked = false;
    modifyEdgeClicked = false;
    editMapDirections.setText("Click a dot on the map to remove");
  }

  /** Sets up the screen for the user to modify a node */
  @FXML
  public void modifyNode() {
    removeNodeClicked = false;
    modifyNodeClicked = true;
    addNodeClicked = false;
    modifyEdgeClicked = false;
    editMapDirections.setText("Click a dot on the map to modify");
  }

  @FXML
  public void modifyEdge() {
    if (modifyEdgeButton.getText().equals("Modify Edge")) {
      modifyEdgeClicked = true;
      removeNodeClicked = false;
      modifyNodeClicked = false;
      addNodeClicked = false;

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
  private void shutDownInputDialogBox() {
    if (currentPositionClicked != null) {
      topPane.getChildren().remove(currentPositionClicked);
    }
    inputDialog.setDisable(true);
    inputDialog.setVisible(false);
    mapEditorControls.setVisible(true);
    mapEditorControls.setDisable(false);
    removeNodeClicked = false;
    modifyNodeClicked = false;
    addNodeClicked = false;
    topPane.setOnMouseClicked(null);
  }

  /** Preload information in dialog box for the user to modify a node */
  private void preLoadDialogInfo(Node node, LocationName locName) {
    oldLongName = locName.getLongName();
    oldShortName = locName.getShortName();
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
    modifyEdgeClicked = false;
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
  public void validateButton() {
    if (longNameField.getText().isEmpty()
        || shortNameField.getText().isEmpty()
        || floorField.getSelectedIndex() == -1
        || buildingField.getSelectedIndex() == -1
        || nodeTypeField.getSelectedIndex() == -1) {
      submitButton.setDisable(true);
    } else {
      submitButton.setDisable(addNodeClicked && (XYCoords[0] < 0 || XYCoords[1] < 0));
    }
  }

  /**
   * Clears the dialog box selection after submit button is clicked and shuts down the dialog box
   */
  private void clear() {
    submitButton.setDisable(true);
    longNameField.clear();
    longNameField.setDisable(false);
    shortNameField.clear();
    floorField.getSelectionModel().clearSelection();
    buildingField.getSelectionModel().clearSelection();
    nodeTypeField.getSelectionModel().clearSelection();
    shutDownInputDialogBox();
  }

  @FXML
  public void submit() {
    // once submit button has been clicked, update database

    if (modifyNodeClicked) {
      entity.determineModifyAction(
          level,
          currentNodeID,
          XYCoords[0],
          XYCoords[1],
          oldLongName,
          oldShortName,
          longNameField.getText(),
          shortNameField.getText(),
          floorField.getText(),
          buildingField.getText(),
          nodeTypeField.getText());
      currentCircle.setVisible(false);
      currentCircle.setDisable(true);
    } else if (addNodeClicked) {
      entity.determineAddAction(
          level,
          XYCoords[0],
          XYCoords[1],
          longNameField.getText(),
          shortNameField.getText(),
          floorField.getText(),
          buildingField.getText(),
          nodeTypeField.getText());
    }

    clear();
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

            Circle circle = entity.addCircle(X, Y);
            circle.setFill(Color.web("0xf74c4c"));
            topPane.getChildren().add(circle);
            currentPositionClicked = circle;
            validateButton();
          }
        });
  }

  /**
   * once import button is clicked, sets import boolean to true and pops up options of what the user
   * wants to import to the database
   */
  @FXML
  public void importFile() {
    imported = true;
    impExpDialog.setVisible(true);
    impExpDialog.setDisable(false);
  }

  /**
   * once export button is clicked, sets export boolean to true and pops up options of what the user
   * wants to export to their computer
   */
  @FXML
  public void exportFile() {
    imported = false;
    impExpDialog.setVisible(true);
    impExpDialog.setDisable(false);
  }

  @FXML
  public void MoveImpExp() {
    entity.importExport(imported, "Move");

    impExpDialog.setVisible(false);
    impExpDialog.setDisable(true);
  }

  @FXML
  public void NodeImpExp() {
    entity.importExport(imported, "Node");

    impExpDialog.setVisible(false);
    impExpDialog.setDisable(true);
  }

  @FXML
  public void LocationImpExp() {
    entity.importExport(imported, "LocationName");

    impExpDialog.setVisible(false);
    impExpDialog.setDisable(true);
  }

  @FXML
  public void EdgeImpExp() {
    entity.importExport(imported, "Edge");

    impExpDialog.setVisible(false);
    impExpDialog.setDisable(true);
  }
}
