package edu.wpi.teamA.controllers.Navigation;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class MapEditorController {

  @FXML private ImageView mapImage;
  @FXML private StackPane mapStackPane;

  public void initialize() {
    double xCoord = mapImage.getBoundsInParent().getMinX();
    double yCoord = mapImage.getBoundsInParent().getMinY();
    double width = mapImage.getBoundsInParent().getWidth();
    double height = mapImage.getBoundsInParent().getHeight();
    System.out.println(xCoord);
    System.out.println(yCoord);
    System.out.println(width);
    System.out.println(height);
  }

  @FXML
  public void zoomOut() {}

  @FXML
  public void zoomIn() {}
}
