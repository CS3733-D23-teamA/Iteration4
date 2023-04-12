package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DAOImps.NodeDAOImp;
import edu.wpi.teamA.database.ORMclasses.Node;
import edu.wpi.teamA.pathfinding.AStar;
import edu.wpi.teamA.pathfinding.GraphNode;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

public class PathfindingController extends PageController {
  @FXML private MFXFilterComboBox<Integer> startSelection;
  @FXML private MFXFilterComboBox<Integer> endSelection;
  @FXML private Text directions;
  @FXML private MFXButton submitButton;
  private ArrayList<Integer> nodeIDOptions = new ArrayList<Integer>();
  private NodeDAOImp nodeDAO = new NodeDAOImp();
  @FXML private GesturePane gp;
  @FXML private ArrayList<Node> nodeList;
  @FXML private Pane topPane = new Pane();

  @FXML
  private StackPane sp =
      new StackPane(new ImageView("edu/wpi/teamA/images/map-page/00_thelowerlevel1.png"), topPane);

  @Override
  public void initialize() {
    gp.setContent(sp);
    nodeList = nodeDAO.loadNodesFromDatabase();
    for (Node node : nodeList) {
      if (node.getFloor().equals("L1")) {
        nodeIDOptions.add(node.getNodeID());
      }
    }
    startSelection.setItems(FXCollections.observableArrayList(nodeIDOptions));
    endSelection.setItems(FXCollections.observableArrayList(nodeIDOptions));
  }

  public void submit() {
    try {
      AStar a = new AStar(startSelection.getSelectedItem(), endSelection.getSelectedItem());
      directions.setText(a.toString());
      directions.setFill(Color.BLACK);
      System.out.println("Nodes submitted");
      drawPath(a);
    } catch (NullPointerException e) {
      System.out.println("Null Value");
      // System.out.println(pr);
    }
  }

  private void clearPane() {
    sp.getChildren().remove(topPane);
    sp.getChildren().add(topPane);
  }

  public void drawPath(AStar a) {
    ArrayList<Integer> nodePathIDs = a.getPath();
    GraphNode gNode = a.getGraphNode(nodePathIDs.get(0));
    int lastX = gNode.getXcoord();
    int lastY = gNode.getYcoord();
    Line line;
    topPane.getChildren().add(new Circle(lastX, lastY, 3, Color.DARKRED));
    for (int i = 1; i < nodePathIDs.size(); i++) {
      gNode = a.getGraphNode(nodePathIDs.get(i));
      line = new Line(lastX, lastY, gNode.getXcoord(), gNode.getYcoord());
      line.setFill(Color.DARKRED);
      line.setStrokeWidth(8);
      topPane
          .getChildren()
          .addAll(line, new Circle(gNode.getXcoord(), gNode.getYcoord(), 6, Color.DARKRED));
      lastX = gNode.getXcoord();
      lastY = gNode.getYcoord();
    }
  }
}
