package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DAOImps.MoveDAOImp;
import edu.wpi.teamA.database.DAOImps.NodeDAOImp;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Node;
import edu.wpi.teamA.database.Singletons.AccountSingleton;
import edu.wpi.teamA.entities.MapEntity;
import edu.wpi.teamA.pathfinding.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.util.ArrayList;
import java.util.HashMap;
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

  // Node implementation

  private ArrayList<String> nodeOptions = new ArrayList<>();
  private ArrayList<String> searchOptions = new ArrayList<>();
  private ArrayList<Node> nodeList;
  private final NodeDAOImp nodeDAO = new NodeDAOImp();
  private final MoveDAOImp moveDAO = new MoveDAOImp();
  private HashMap<String, Integer> nameMap = new HashMap<String, Integer>();
  private final MapEntity map = new MapEntity();
  private String floor = "L1";
  // private final NodeDAOImp nodeDAO = new NodeDAOImp();

  private final DataBaseRepository databaseRepo = new DataBaseRepository();

  public void initialize() {

    // setting search algorithim selection visibility based on access level
    if (!AccountSingleton.isAdmin()) {
      searchAlgorithmVbox.setVisible(false);
      searchAlgorithmVbox.setManaged(false);
    }
    // Set up Map in Gesture pane using a StackPane
    gesturePane.setContent(stackPane);

    // center and zoom onto map content
    Platform.runLater(
        () -> {
          gesturePane.centreOn(new Point2D(2265, 950));
          gesturePane.zoomTo(0.5, new Point2D(2265, 950));
        });

    // Getting Nodes from Database
    nodeList = databaseRepo.loadNodesFromDatabaseInArray();
    for (Node node : nodeList) {
      int id = node.getNodeID();
      String name = moveDAO.getMove(id).getLongName();
      nodeOptions.add(name);
      nameMap.put(name, id);
    }

    searchOptions.add("A*");
    searchOptions.add("Breadth-First Search");
    searchOptions.add("Depth-First Search");

    // Setting ComboBox Selection Options (for start + end locations)

    startSelection.setItems(FXCollections.observableArrayList(nodeOptions));
    endSelection.setItems(FXCollections.observableArrayList(nodeOptions));
    searchAlgorithmSelection.setItems(FXCollections.observableArrayList(searchOptions));

    // Buttons to set floor level of map
    levelL1Button.setOnAction(event -> changLevel(levelL1Button));
    levelL2Button.setOnAction(event -> changLevel(levelL2Button));
    level1Button.setOnAction(event -> changLevel(level1Button));
    level2Button.setOnAction(event -> changLevel(level2Button));
    level3Button.setOnAction(event -> changLevel(level3Button));
  }

  private void changLevel(MFXButton button) {

    // get pre-loaded map image from App
    switch (button.getText()) {
      case "L1":
        mapImage = App.getMapL1();
        floor = "L1";
        checkPath();
        break;
      case "L2":
        mapImage = App.getMapL2();
        floor = "L2";
        checkPath();
        break;
      case "1":
        mapImage = App.getMap1();
        floor = "1";
        checkPath();
        break;
      case "2":
        mapImage = App.getMap2();
        floor = "2";
        checkPath();
        break;
      case "3":
        mapImage = App.getMap3();
        floor = "3";
        checkPath();
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
    try {
      clearPath();
      String startName = startSelection.getSelectedItem();
      String endName = endSelection.getSelectedItem();
      String searchAlgorithm = searchAlgorithmSelection.getSelectedItem();

      int startID = nameMap.get(startName);
      int endID = nameMap.get(endName);

      Search search;
      if (AccountSingleton.INSTANCE.getValue().getAccessLevel() == 1) {
        if (searchAlgorithm.equals("Breadth-First Search")) {
          search = new BFS(startID, endID);
          searchAlgorithmTextDirections.setText("Using Breadth-First Search");
        } else if (searchAlgorithm.equals("Depth-First Search")) {
          search = new DFS(startID, endID);
          searchAlgorithmTextDirections.setText("Using Depth-First Search");
        } else {
          search = new AStar(startID, endID);
          searchAlgorithmTextDirections.setText("Using A* Search");
        }
      } else {
        search = new AStar(startID, endID);
      }
      directions.setText(generatePathString(search.getPath()));
      directions.setFill(Color.web("#f1f1f1"));

      System.out.println("Nodes submitted");

      drawPath(search);

    } catch (NullPointerException e) {
      System.out.println("Null Value " + e.getMessage());
    }
  }

  @FXML
  public void checkPath() {
    System.out.println(AccountSingleton.INSTANCE.getValue().getAccessLevel());
    if (startSelection.getSelectedItem() != null && endSelection.getSelectedItem() != null) {
      if (AccountSingleton.INSTANCE.getValue().getAccessLevel() == 1) {
        if (searchAlgorithmSelection.getSelectedItem() != null) {
          submit();
        }
      } else {
        submit();
      }
    }
  }

  /**
   * Helper method for submit, draws graphical path
   *
   * @param search Astar object implementation to determine path
   */
  public void drawPath(Search search) {

    ArrayList<Integer> nodePathIDs = search.getPath();
    GraphNode gNode = search.getGraphNode(nodePathIDs.get(0));

    int lastX = gNode.getXcoord();
    int lastY = gNode.getYcoord();

    String startFloor = gNode.getFloor();
    int startX = lastX;
    int startY = lastY;

    Line line;

    for (int i = 1; i < nodePathIDs.size(); i++) {
      gNode = search.getGraphNode(nodePathIDs.get(i));
      if (gNode.getFloor().equals(floor)) {
        line = new Line(lastX, lastY, gNode.getXcoord(), gNode.getYcoord());
        line.setFill(Color.web("0x012D5A"));
        line.setStrokeWidth(7);

        topPane
            .getChildren()
            .addAll(
                line, new Circle(gNode.getXcoord(), gNode.getYcoord(), 6, Color.web("0x012D5A")));
      }
      lastX = gNode.getXcoord();
      lastY = gNode.getYcoord();
    }

    if (gNode.getFloor().equals(floor)) {
      Rectangle end = new Rectangle(lastX - 8, lastY - 8, 16, 16);
      end.setFill(Color.web("0xEEBD28"));
      topPane.getChildren().add(end);
    }
    if (startFloor.equals(floor)) {
      topPane.getChildren().add(new Circle(startX, startY, 8, Color.web("0xEEBD28")));
    }
  }

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

  public void clearPath() {
    topPane.getChildren().clear();
  }
}
