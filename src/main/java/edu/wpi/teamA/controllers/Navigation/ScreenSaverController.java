package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lombok.SneakyThrows;

public class ScreenSaverController {
  Random rand = new Random();
  @FXML private ImageView iv = new ImageView();
  @FXML private Circle circle;
  @FXML private Rectangle paddle;
  @FXML private Rectangle bottomZone;
  @FXML private AnchorPane scene;
  Robot robot = new Robot();
  ArrayList<Rectangle> bricks = new ArrayList<>();
  ArrayList<Rectangle> bricksTest = new ArrayList<>();
  double deltaX = -1;
  double deltaY = -3;

  @FXML
  Rectangle a1,
      a2,
      a3,
      a4,
      a5,
      a6,
      a7,
      a8,
      a9,
      b1,
      b2,
      b3,
      b4,
      b5,
      b6,
      b7,
      b8,
      b9,
      c1,
      c2,
      c3,
      c4,
      c5,
      c6,
      c7,
      c8,
      c9,
      d1,
      d2,
      d3,
      d4,
      d5,
      d6,
      d7,
      d8,
      d9,
      e1,
      e2,
      e3,
      e4,
      e5,
      e6,
      e7,
      e8,
      e9,
      f1,
      f2,
      f3,
      f4,
      f5,
      f6,
      f7,
      f8,
      f9,
      g1,
      g2,
      g3,
      g4,
      g5,
      g6,
      g7,
      g8,
      g9,
      h1,
      h2,
      h3,
      h4,
      h5,
      h6,
      h7,
      h8,
      h9,
      i1,
      i2,
      i3,
      i4,
      i5,
      i6,
      i7,
      i8,
      i9,
      j1,
      j2,
      j3,
      j4,
      j5,
      j6,
      j7,
      j8,
      j9,
      k1,
      k2,
      k3,
      k4,
      k5,
      k6,
      k7,
      k8,
      k9,
      l1,
      l2,
      l3,
      l4,
      l5,
      l6,
      l7,
      l8,
      l9,
      l0;
  @FXML MFXGenericDialog popUp;
  int count = 0;

  // 1 Frame evey 10 millis, which means 100 FPS
  Timeline timeline =
      new Timeline(
          new KeyFrame(
              Duration.millis(10),
              new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                  movePaddle();
                  paddle.setWidth(100.0);
                  checkCollisionPaddle(paddle);
                  circle.setCenterX(circle.getCenterX() + deltaX);
                  circle.setCenterY(circle.getCenterY() + deltaY);

                  if (!bricksTest.isEmpty()) {
                    bricksTest.removeIf(brickTest -> checkCollisionBrick(brickTest));
                  } else {
                    timeline.stop();
                    postGame();
                  }

                  checkCollisionScene(scene);
                  checkCollisionBottomZone();
                }
              }));
  Timeline brickTimeline =
      new Timeline(
          new KeyFrame(
              Duration.millis(10),
              new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                  if (count < bricksTest.size()) {
                    bricksTest.get(count).setVisible(true);
                    count++;
                  } else {
                    brickTimeline.stop();
                  }
                }
              }));

  public void initialize() {
    Image image = App.getMapL1();
    iv.setImage(image);
    hideBricks();
    brickTimeline.setCycleCount(Animation.INDEFINITE);
    timeline.setCycleCount(Animation.INDEFINITE);
  }

  public void startGameButtonAction() {
    popUp.setDisable(true);
    popUp.setVisible(false);

    startGame(scene);
  }

  @SneakyThrows
  public void startGame(AnchorPane node) {
    Bounds bounds = node.getBoundsInLocal();
    createBricks();
    timeline.play();
  }

  public void checkCollisionScene(AnchorPane node) {
    Bounds bounds = node.getBoundsInLocal();
    int right = 1165;
    int left = 115;
    int top = 720;
    boolean rightBorder = circle.getCenterX() >= (bounds.getMaxX() - circle.getRadius());
    boolean leftBorder = circle.getCenterX() <= (bounds.getMinX() + circle.getRadius());
    boolean topBorder = circle.getCenterY() <= (bounds.getMinY() + circle.getRadius());

    if (rightBorder || leftBorder) {
      System.out.println(circle.getCenterX() + " side");
      deltaX *= -1;
    }
    if (topBorder) {
      System.out.println(circle.getCenterY() + " top");
      deltaY *= -1;
    }
  }

  public boolean checkCollisionBrick(Rectangle brick) {

    if (circle.getBoundsInParent().intersects(brick.getBoundsInParent())) {
      boolean rightBorder =
          circle.getCenterX() >= ((brick.getX() + brick.getWidth()) - circle.getRadius());
      boolean leftBorder = circle.getCenterX() <= (brick.getX() + circle.getRadius());
      boolean bottomBorder =
          circle.getCenterY() >= ((brick.getY() + brick.getHeight()) - circle.getRadius());
      boolean topBorder = circle.getCenterY() <= (brick.getY() + circle.getRadius());

      if (rightBorder || leftBorder) {
        deltaX *= -1;
      }
      if (bottomBorder || topBorder) {
        deltaY *= -1;
      }

      paddle.setWidth(paddle.getWidth() - (0.10 * paddle.getWidth()));
      scene.getChildren().remove(brick);

      return true;
    }
    return false;
  }

  public void hideBricks() {
    bricks = setBricks();
    for (Rectangle x : bricks) {
      x.setVisible(false);
    }
  }

  public void createBricks() throws InterruptedException {
    for (Rectangle x : bricks) {
      Rectangle x1 = new Rectangle(x.getLayoutX(), x.getLayoutY(), x.getWidth(), x.getHeight());
      x1.setVisible(false);
      x1.setFill(Paint.valueOf("012D5A"));
      scene.getChildren().add(x1);
      bricksTest.add(x1);
    }
    brickTimeline.play();
    count = 0;
  }

  public ArrayList<Rectangle> setBricks() {
    Collections.addAll(
        bricks, a1, a2, a3, a4, a5, a6, a7, a8, a9, b1, b2, b3, b4, b5, b6, b7, b8, b9, c1, c2, c3,
        c4, c5, c6, c7, c8, c9, d1, d2, d3, d4, d5, d6, d7, d8, d9, e1, e2, e3, e4, e5, e6, e7, e8,
        e9, f1, f2, f3, f4, f5, f6, f7, f8, f9, g1, g2, g3, g4, g5, g6, g7, g8, g9, h1, h2, h3, h4,
        h5, h6, h7, h8, h9, i1, i2, i3, i4, i5, i6, i7, i8, i9, j1, j2, j3, j4, j5, j6, j7, j8, j9,
        k1, k2, k3, k4, k5, k6, k7, k8, k9, l1, l2, l3, l4, l5, l6, l7, l8, l9, l0);

    return bricks;
  }

  public void movePaddle() {
    Bounds bounds = scene.localToScreen(scene.getBoundsInLocal());
    double sceneXPos = bounds.getMinX();

    double xPos = robot.getMouseX();
    double paddleWidth = paddle.getWidth();

    if (xPos >= sceneXPos + (paddleWidth / 2)
        && xPos <= (sceneXPos + scene.getWidth()) - (paddleWidth / 2)) {
      paddle.setLayoutX(xPos - sceneXPos - (paddleWidth / 2));
    } else if (xPos < sceneXPos + (paddleWidth / 2)) {
      paddle.setLayoutX(0);
    } else if (xPos > (sceneXPos + scene.getWidth()) - (paddleWidth / 2)) {
      paddle.setLayoutX(scene.getWidth() - paddleWidth);
    }
  }

  public void checkCollisionPaddle(Rectangle paddle) {

    if (circle.getBoundsInParent().intersects(paddle.getBoundsInParent())) {

      boolean rightBorder =
          circle.getCenterX() >= ((paddle.getLayoutX() + paddle.getWidth()) - circle.getRadius());
      boolean leftBorder = circle.getCenterX() <= (paddle.getLayoutX() + circle.getRadius());
      boolean bottomBorder =
          circle.getCenterY() >= ((paddle.getLayoutY() + paddle.getHeight()) - circle.getRadius());
      boolean topBorder = circle.getCenterY() <= (paddle.getLayoutY() + circle.getRadius());

      if (rightBorder || leftBorder) {
        deltaX *= -1;
      }
      if (bottomBorder || topBorder) {
        deltaY *= -1;
      }
    }
  }

  public void checkCollisionBottomZone() {
    if (circle.getBoundsInParent().intersects(bottomZone.getBoundsInParent())) {
      postGame();
    }
  }

  public void postGame() {
    timeline.stop();
    bricksTest.forEach(brick -> scene.getChildren().remove(brick));
    bricksTest.clear();
    popUp.setVisible(true);
    popUp.setDisable(false);

    deltaX = -1;
    deltaY = -3;

    circle.setCenterX(640);
    circle.setCenterY(650);
  }

  public void login() {
    Navigation.navigate(Screen.LOGIN);
  }

  public void power() {
    App.getPrimaryStage().hide();
  }
}
