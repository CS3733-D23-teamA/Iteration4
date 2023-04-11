package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.Main;
import edu.wpi.teamA.controllers.Map.MapEditorEntity;
import edu.wpi.teamA.database.DAOImps.EdgeDAOImp;
import edu.wpi.teamA.database.DAOImps.LocNameDAOImp;
import edu.wpi.teamA.database.DAOImps.MoveDAOImp;
import edu.wpi.teamA.database.DAOImps.NodeDAOImp;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Node;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import net.kurobako.gesturefx.GesturePane;

public class MapEditorController {
  private final MapEditorEntity entity = new MapEditorEntity();
  public static EventType<MouseEvent> MOUSE_CLICKED;

  @FXML private BorderPane borderPane;
  @FXML private ImageView mapImage;
  @FXML private StackPane mapStackPane;
  @FXML private AnchorPane dotsAnchorPane;
  @FXML private GesturePane mapGesturePane;

  @FXML private MFXButton levelButton;
  @FXML private MFXButton levelGButton;
  @FXML private MFXButton levelL1Button;
  @FXML private MFXButton levelL2Button;
  @FXML private MFXButton level1Button;
  @FXML private MFXButton level2Button;
  @FXML private MFXButton level3Button;

  @FXML private VBox levelMenu;

  @FXML private Text locationDisplay;
  @FXML private Text editMapDirections;

  @FXML private HBox mapEditorControls;
  @FXML private MFXGenericDialog inputDialog;

  @FXML private MFXTextField longNameField;
  @FXML private MFXTextField shortNameField;
  @FXML private MFXComboBox<String> floorField;
  @FXML private MFXComboBox<String> buildingField;
  @FXML private MFXComboBox<String> nodeTypeField;
  private boolean removeNodeClicked;
  private boolean addNodeClicked;
  private boolean modifyNodeClicked;
  @FXML private MFXButton submitButton;
  @FXML private MFXButton changeLocationButton;
  private final double scalar = 0.144;

  private int currentNodeID;
  private int[] XYCoords = new int[2];

  private boolean imported = false;
  private boolean exported = false;

  @FXML private MFXGenericDialog impExpDialog;

  public void initialize() {

    levelGButton.setOnAction(event -> changeLevelText(levelGButton));
    levelL1Button.setOnAction(event -> changeLevelText(levelL1Button));
    levelL2Button.setOnAction(event -> changeLevelText(levelL2Button));
    level1Button.setOnAction(event -> changeLevelText(level1Button));
    level2Button.setOnAction(event -> changeLevelText(level2Button));
    level3Button.setOnAction(event -> changeLevelText(level3Button));

    // set up page
    mapGesturePane.setContent(mapStackPane);
    mapGesturePane.setScrollMode(GesturePane.ScrollMode.ZOOM);
    levelMenu.setVisible(false);
    changeLevelText(levelGButton);
    removeNodeClicked = false;
    addNodeClicked = false;
    modifyNodeClicked = false;

    mapEditorControls.setVisible(true);
    mapEditorControls.setDisable(false);
    inputDialog.setVisible(false);
    inputDialog.setDisable(true);
    submitButton.setDisable(true);
    impExpDialog.setVisible(false);
    impExpDialog.setDisable(true);

    // set up input grid
    floorField.getItems().addAll("G", "L1", "L2", "1", "2", "3");
    buildingField.getItems().addAll("15 Francis", "45 Francis", "BTM", "Shapiro", "Tower");
    nodeTypeField
        .getItems()
        .addAll(
            "CONF", "DEPT", "ELEV", "EXIT", "HALL", "INFO", "LABS", "REST", "RETL", "SERV", "STAI");
  }

  /** for level chooser button */
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
    dotsAnchorPane.getChildren().clear();

    // display new dots
    displayNodeData(Objects.requireNonNull(entity.determineArray(button.getText())));
  }

  private void displayNodeData(ArrayList<Node> nodeArrayForFloor) {
    // scalar for determine position on map

    for (Node node : nodeArrayForFloor) {
      int originalNodeX = node.getXcoord();
      int originalNodeY = node.getYcoord();
      double newXCoord = originalNodeX * scalar;
      double newYCoord = originalNodeY * scalar;

      Circle circle = entity.addCircle(newXCoord, newYCoord);
      circle.setOnMouseEntered(event -> dotHover(circle, node.getNodeID()));
      circle.setOnMouseExited(event -> dotUnhover(circle, node.getNodeID()));
      circle.setOnMouseClicked(event -> dotClicked(circle, node.getNodeID()));
      // borderPane.setOnMouseClicked(event -> dotUnclicked(circle, node.getNodeID()));
      dotsAnchorPane.getChildren().add(circle);
    }

    App.getPrimaryStage().show();
  }

  private void dotHover(Circle circle, int nodeID) {
    circle.setFill(Color.web("0xEEBD28"));

    if (entity.getLocationName(nodeID).getNodeType().equals("HALL")) {
      locationDisplay.setText(entity.getLocationName(nodeID).getLongName());
    } else {
      locationDisplay.setText(entity.getLocationName(nodeID).getShortName());
    }
  }

  @FXML
  public void dotUnhover(Circle circle, int nodeID) {
    circle.setFill(Color.web("0x000000"));
  }

  @FXML
  public void dotClicked(Circle circle, int nodeID) {
    currentNodeID = nodeID;
    if (removeNodeClicked) {
      entity.determineRemoveAction(removeNodeClicked, nodeID);
      circle.setDisable(true);
      circle.setVisible(false);
      removeNodeClicked = false;
    }

    if (modifyNodeClicked) {
      mapEditorControls.setVisible(false);
      mapEditorControls.setDisable(true);
      inputDialog.setVisible(true);
      inputDialog.setDisable(false);

      // get node information
      Node node = entity.getNodeInfo(nodeID);
      LocationName locName = entity.getLocationName(nodeID);

      // Preload information
      longNameField.setText(locName.getLongName());
      shortNameField.setText(locName.getShortName());
      floorField.getSelectionModel().selectItem(node.getFloor());
      buildingField.getSelectionModel().selectItem(node.getBuilding());
      nodeTypeField.getSelectionModel().selectItem(locName.getNodeType());

      validateButton();

      // new location clicked
      dotsAnchorPane.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              double X = event.getX();
              double Y = event.getY();
              int newX = (int) X;
              int newY = (int) Y;
              XYCoords = new int[2];
              XYCoords[0] = (int) (newX / scalar);
              XYCoords[1] = (int) (newY / scalar);
              System.out.println("New X:" + newX);
              System.out.println("New Y:" + newY);
              // new red circle indicating new location

              Circle circle = entity.addCircle(newX, newY);
              circle.setFill(Color.rgb(128, 0, 0, 0.87));
            }
          });

      // update the display to show the updated information

    }

    editMapDirections.setText("");
  }

  @FXML
  public void removeNode() {
    removeNodeClicked = true;
    modifyNodeClicked = false;
    addNodeClicked = false;
    editMapDirections.setText("Click a dot on the map to remove");
  }

  @FXML
  public void modifyNode() {
    removeNodeClicked = false;
    modifyNodeClicked = true;
    addNodeClicked = false;
    editMapDirections.setText("Click a dot on the map to modify");
  }

  @FXML
  public void addNode() {
    removeNodeClicked = false;
    modifyNodeClicked = false;
    addNodeClicked = true;
    editMapDirections.setText("Add node");

    // pop up dialog box
    mapEditorControls.setVisible(false);
    mapEditorControls.setDisable(true);
    inputDialog.setVisible(true);
    inputDialog.setDisable(false);

    XYCoords[0] = -1;
    XYCoords[1] = -1;

    // set map click thingy
    dotsAnchorPane.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            double X = event.getX();
            double Y = event.getY();
            int newX = (int) X;
            int newY = (int) Y;
            XYCoords[0] = (int) (newX / scalar);
            XYCoords[1] = (int) (newY / scalar);
            System.out.println("New X:" + newX);
            System.out.println("New Y:" + newY);
            // new red circle indicating new location

            Circle circle = entity.addCircle(newX, newY);
            circle.setFill(Color.rgb(128, 0, 0, 0.87));
          }
        });
  }

  @FXML
  public void validateButton() {
    if (addNodeClicked) {
      if (XYCoords[0] < 0 || XYCoords[1] < 0) {
        submitButton.setDisable(true);
      }
    }

    if (longNameField.getText().isEmpty()
        || shortNameField.getText().isEmpty()
        || floorField.getSelectedIndex() == -1
        || buildingField.getSelectedIndex() == -1
        || nodeTypeField.getSelectedIndex() == -1) {
      submitButton.setDisable(true);
    } else {
      submitButton.setDisable(false);
    }
  }

  public void clear() {
    submitButton.setDisable(true);
    longNameField.clear();
    shortNameField.clear();
    floorField.getSelectionModel().clearSelection();
    buildingField.getSelectionModel().clearSelection();
    nodeTypeField.getSelectionModel().clearSelection();
  }

  @FXML
  public void submit() {
    // once submit button has been clicked, update database

    if (modifyNodeClicked) {
      entity.determineModifyAction(
          currentNodeID,
          XYCoords[0],
          XYCoords[1],
          longNameField.getText(),
          shortNameField.getText(),
          floorField.getText(),
          buildingField.getText(),
          nodeTypeField.getText());
    } else if (addNodeClicked) {
      entity.determineAddAction(
          XYCoords[0],
          XYCoords[1],
          longNameField.getText(),
          shortNameField.getText(),
          floorField.getText(),
          buildingField.getText(),
          nodeTypeField.getText());
    }

    clear();
    inputDialog.setDisable(true);
    inputDialog.setVisible(false);
    mapEditorControls.setVisible(true);
    mapEditorControls.setDisable(false);
  }

  @FXML
  public void importFile() {
    imported = true;
    impExpDialog.setVisible(true);
    impExpDialog.setDisable(false);
  }

  @FXML
  public void exportFile() {
    exported = true;
    impExpDialog.setVisible(true);
    impExpDialog.setDisable(false);
  }

  @FXML
  public void MoveImpExp() {
    if (imported) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open CSV File");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
      File selectedFile = fileChooser.showOpenDialog(App.getPrimaryStage());
      MoveDAOImp.Import(selectedFile.getPath());
    } else if (exported) {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Export CSV File to");
      File selectedDirectory = directoryChooser.showDialog(App.getPrimaryStage());
      MoveDAOImp.Export(selectedDirectory.getPath());
    }

    impExpDialog.setVisible(false);
    impExpDialog.setDisable(true);
  }

  @FXML
  public void NodeImpExp() {
    if (imported) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open CSV File");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
      File selectedFile = fileChooser.showOpenDialog(App.getPrimaryStage());
      NodeDAOImp.Import(selectedFile.getPath());
    } else if (exported) {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Export CSV File to");
      File selectedDirectory = directoryChooser.showDialog(App.getPrimaryStage());
      NodeDAOImp.Export(selectedDirectory.getPath());
    }

    impExpDialog.setVisible(false);
    impExpDialog.setDisable(true);
  }

  @FXML
  public void LocationImpExp() {
    if (imported) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open CSV File");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
      File selectedFile = fileChooser.showOpenDialog(App.getPrimaryStage());
      LocNameDAOImp.Import(selectedFile.getPath());
    } else if (exported) {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Export CSV File to");
      File selectedDirectory = directoryChooser.showDialog(App.getPrimaryStage());
      LocNameDAOImp.Export(selectedDirectory.getPath());
    }
    impExpDialog.setVisible(false);
    impExpDialog.setDisable(true);
  }

  @FXML
  public void EdgeImpExp() {
    if (imported) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open CSV File");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
      File selectedFile = fileChooser.showOpenDialog(App.getPrimaryStage());
      EdgeDAOImp.Import(selectedFile.getPath());
    } else if (exported) {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Export CSV File to");
      File selectedDirectory = directoryChooser.showDialog(App.getPrimaryStage());
      EdgeDAOImp.Export(selectedDirectory.getPath());
    }

    impExpDialog.setVisible(true);
    impExpDialog.setDisable(false);
  }
}
