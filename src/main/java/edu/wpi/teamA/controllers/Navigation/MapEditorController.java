package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.Main;
import edu.wpi.teamA.controllers.Map.MapEditorEntity;
import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Node;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import java.util.ArrayList;
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
  private final MapEditorEntity entity = new MapEditorEntity();

  // larger panes
  //  @FXML private ImageView mapImage;
  //  @FXML private StackPane mapStackPane;
  //  @FXML private AnchorPane dotsAnchorPane;
  @FXML private GesturePane mapGesturePane;
  @FXML private HBox mapEditorControls;
  @FXML private MFXGenericDialog inputDialog;
  @FXML private MFXGenericDialog impExpDialog;

  @FXML private Pane topPane = new Pane();
  @FXML private ImageView mapImage = new ImageView();

  @FXML private StackPane mapStackPane = new StackPane(mapImage, topPane);

  // level buttons
  @FXML private MFXButton levelButton;
  @FXML private MFXButton levelGButton;
  @FXML private MFXButton levelL1Button;
  @FXML private MFXButton levelL2Button;
  @FXML private MFXButton level1Button;
  @FXML private MFXButton level2Button;
  @FXML private MFXButton level3Button;
  @FXML private VBox levelMenu;
  private String level = "L1";

  // text displays
  @FXML private Text locationDisplay;
  @FXML private Text editMapDirections;

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

  // booleans to determine importing or exporting
  private boolean imported = false;

  private int currentNodeID;
  private int[] XYCoords = new int[2];

  private String oldLongName;
  private String oldShortName;

  private Circle currentCircle;

  // private final double scalar = 0.144;

  /** Used to initialize the screen and inputs */
  public void initialize() {
    // set up level buttons
    levelGButton.setOnAction(event -> changeLevelText(levelGButton));
    levelL1Button.setOnAction(event -> changeLevelText(levelL1Button));
    levelL2Button.setOnAction(event -> changeLevelText(levelL2Button));
    level1Button.setOnAction(event -> changeLevelText(level1Button));
    level2Button.setOnAction(event -> changeLevelText(level2Button));
    level3Button.setOnAction(event -> changeLevelText(level3Button));

    // set up entity arrays
    entity.loadFloorEdges();
    entity.loadFloorNodes();

    // set up page
    mapGesturePane.setContent(mapStackPane);
    mapGesturePane.setScrollMode(GesturePane.ScrollMode.ZOOM);
    levelMenu.setVisible(false);
    removeNodeClicked = false;
    addNodeClicked = false;
    modifyNodeClicked = false;
    mapImage.setImage(
        new Image(
            Objects.requireNonNull(Main.class.getResource("images/map-page/Level G.png"))
                .toString()));
    changeLevelText(levelL1Button);

    // set up dialog box visiblity
    mapEditorControls.setVisible(true);
    mapEditorControls.setDisable(false);
    inputDialog.setVisible(false);
    inputDialog.setDisable(true);
    submitButton.setDisable(true);
    impExpDialog.setVisible(false);
    impExpDialog.setDisable(true);

    inputDialog.setOnClose(
        event -> {
          shutDownMainDialogBox();
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

  /** For level chooser button */
  @FXML
  public void levelChooser() {
    // make Vbox visible
    levelMenu.setVisible(true);
  }

  /**
   * Used to change the level chooser text based on which floor
   *
   * @param button button chosen
   */
  private void changeLevelText(MFXButton button) {

    // get text from clicked button
    levelButton.setText(button.getText());

    // hide buttons in Vbox
    levelMenu.setVisible(false);

    // change map image
    String image = "images/map-page/" + button.getText() + ".png";
    mapImage.setImage(new Image(Objects.requireNonNull(Main.class.getResource(image)).toString()));

    // hide old dots
    topPane.getChildren().clear();

    // button
    level = button.getText();

    // display new dots and edges
    displayEdgeData(Objects.requireNonNull(entity.determineEdgeArray(level)));
    displayNodeData(Objects.requireNonNull(entity.determineNodeArray(level)));
  }

  /**
   * Displays the node data for that floor in the form as dots on the map
   *
   * @param nodeArrayForFloor the array with the data for that floor
   */
  private void displayNodeData(ArrayList<Node> nodeArrayForFloor) {
    for (Node node : nodeArrayForFloor) {
      Circle circle = entity.addCircle(node.getXcoord(), node.getYcoord());
      circle.setOnMouseEntered(event -> dotHover(circle, node.getNodeID()));
      circle.setOnMouseExited(event -> dotUnhover(circle, node.getNodeID()));
      circle.setOnMouseClicked(event -> dotClicked(circle, node.getNodeID()));
      topPane.getChildren().add(circle);
    }
    App.getPrimaryStage().show();
  }

  private void displayEdgeData(ArrayList<Edge> edgeArrayForFloor) {
    for (Edge edge : edgeArrayForFloor) {
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
      entity.determineRemoveAction(nodeID);
      circle.setDisable(true);
      circle.setVisible(false);
      removeNodeClicked = false;
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
    }

    editMapDirections.setText("");
  }

  /** Sets up screen for the user to remove a node */
  @FXML
  public void removeNode() {
    removeNodeClicked = true;
    modifyNodeClicked = false;
    addNodeClicked = false;
    editMapDirections.setText("Click a dot on the map to remove");
  }

  /** Sets up the screen for the user to modify a node */
  @FXML
  public void modifyNode() {
    removeNodeClicked = false;
    modifyNodeClicked = true;
    addNodeClicked = false;
    editMapDirections.setText("Click a dot on the map to modify");
  }

  /** Pops up the input dialog page and hides map editor controls */
  private void popUpMainDialogBox() {
    mapEditorControls.setVisible(false);
    mapEditorControls.setDisable(true);
    inputDialog.setVisible(true);
    inputDialog.setDisable(false);
  }

  /** Hides the input dialog page and brings back map editor controls */
  private void shutDownMainDialogBox() {
    inputDialog.setDisable(true);
    inputDialog.setVisible(false);
    mapEditorControls.setVisible(true);
    mapEditorControls.setDisable(false);
    removeNodeClicked = false;
    modifyNodeClicked = false;
    addNodeClicked = false;
  }

  /** Preload information in dialog box for the user to modify a node */
  private void preLoadDialogInfo(Node node, LocationName locName) {
    oldLongName = locName.getLongName();
    oldShortName = locName.getShortName();
    longNameField.setText(oldLongName);
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
    editMapDirections.setText("Add node");
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
    shortNameField.clear();
    floorField.getSelectionModel().clearSelection();
    buildingField.getSelectionModel().clearSelection();
    nodeTypeField.getSelectionModel().clearSelection();
    shutDownMainDialogBox();
  }

  @FXML
  public void submit() {
    // once submit button has been clicked, update database

    if (modifyNodeClicked) {
      entity.determineModifyAction(
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
    displayEdgeData(entity.determineEdgeArray(level));
    displayNodeData(entity.determineNodeArray(level));
  }

  /**
   * Used to store the coordinates of when the user clicks on the map for adding or modifying a node
   */
  private void mouseClickForNewLocation() {
    topPane.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            double X = event.getX();
            double Y = event.getY();
            int newX = (int) X;
            int newY = (int) Y;
            XYCoords = new int[2];
            XYCoords[0] = (int) (newX);
            XYCoords[1] = (int) (newY);
            System.out.println("New X:" + newX);
            System.out.println("New Y:" + newY);
            // new red circle indicating new location

            Circle circle = entity.addCircle(newX, newY);
            circle.setFill(Color.rgb(128, 0, 0, 0.87));
            circle.setVisible(true);
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
