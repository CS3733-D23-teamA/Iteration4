package edu.wpi.teamA.entities;

import edu.wpi.teamA.database.DataBaseRepository;
import javafx.scene.shape.Circle;

public class DraggableMaker {
  private double mouseAnchorX;
  private double mouseAnchorY;

  private final DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

  public void makeDraggable(Circle node) {

    //    node.setOnMousePressed(
    //        mouseEvent -> {
    //          mouseAnchorX = mouseEvent.getX();
    //          mouseAnchorY = mouseEvent.getY();
    //          System.out.println(mouseAnchorX);
    //          System.out.println(mouseAnchorY);
    //        });

    node.setOnMouseDragged(
        mouseEvent -> {
          node.setCenterX(mouseEvent.getX());
          node.setCenterY(mouseEvent.getY());
        });

    node.setOnMouseReleased(
        mouseEvent -> {
          System.out.println(node.getCenterX());
          System.out.println(node.getCenterY());
        });
  }
}
