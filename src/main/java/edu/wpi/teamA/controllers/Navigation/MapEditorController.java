package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DAOImps.NodeDAOImp;
import edu.wpi.teamA.database.ORMclasses.Node;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import net.kurobako.gesturefx.GesturePane;

public class MapEditorController {
  private NodeDAOImp nodeDAO = new NodeDAOImp();

  @FXML private ImageView mapImage;
  @FXML private StackPane mapStackPane;
  @FXML private AnchorPane dotsAnchorPane;
  @FXML private GesturePane mapGesturePane;

  public void initialize() {
    double xCoord = mapImage.getBoundsInParent().getMinX();
    double yCoord = mapImage.getBoundsInParent().getMinY();
    double width = mapImage.getBoundsInParent().getWidth();
    double height = mapImage.getBoundsInParent().getHeight();
    System.out.println(xCoord);
    System.out.println(yCoord);
    System.out.println(width);
    System.out.println(height);
    mapGesturePane.setContent(mapStackPane);
    displayNodeData(nodeDAO.loadNodesFromDatabase());
  }

  public void displayNodeData(ArrayList<Node> nodeArray) {
    ArrayList<Node> updatedArray = new ArrayList<Node>();
    ArrayList<Circle> dotsToBeDisplayed = new ArrayList<Circle>();
    double scalar = 0.144;
    for (int i = 0; i < nodeArray.size(); i++) {
      Node node = nodeArray.get(i);
      if (nodeArray.get(i).getFloor().equals("L1")) {
        int originalNodeX = node.getXcoord();
        int originalNodeY = node.getYcoord();
        double newXCoord = originalNodeX * scalar;
        double newYCoord = originalNodeY * scalar;

        // System.out.println(newXCoord);
        // System.out.println(newYCoord);

        /// FUNCTION to add circle
        Circle circle = addCircle(newXCoord, newYCoord);
        dotsToBeDisplayed.add(circle);
        System.out.println(circle.getCenterX());
        System.out.println(circle.getCenterY());
        dotsAnchorPane.getChildren().add(circle);
        // circle.setCenterX(newXCoord);
        // circle.setCenterY(newYCoord);
        // circle.setRadius(2);
        // mapStackPane.getChildren().add(addCircle(newXCoord, newYCoord));

        updatedArray.add(nodeArray.get(i));
      }
    }

    App.getPrimaryStage().show();
    /*ObservableList<javafx.scene.Node> allDots = mapStackPane.getChildren();
    allDots = FXCollections.observableArrayList(dotsToBeDisplayed);
    System.out.println(allDots.size());
    mapStackPane.getChildren().add()
    App.getPrimaryStage().getScene();*/
    // App.getPrimaryStage().show();
  }

  public Circle addCircle(double X, double Y) {
    Circle circle = new Circle();
    circle.setCenterX(X);
    circle.setCenterY(Y);
    circle.setRadius(2);
    return circle;
  }

  @FXML
  public void zoomOut() {}

  @FXML
  public void zoomIn() {}
}
