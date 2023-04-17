package edu.wpi.teamA;

import edu.wpi.teamA.controllers.Map.MapEditorEntity;
import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.DAOImps.*;
import edu.wpi.teamA.database.IncorrectLengthException;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Setter @Getter private static Stage primaryStage;
  @Setter @Getter private static BorderPane rootPane;
  @Getter private static MapEditorEntity mapEditorEntity = new MapEditorEntity();
  @Getter private static Image mapL1 = new Image("edu/wpi/teamA/images/map-page/Level L1.png");
  @Getter private static Image mapL2 = new Image("edu/wpi/teamA/images/map-page/Level L2.png");
  @Getter private static Image map1 = new Image("edu/wpi/teamA/images/map-page/Level 1.png");
  @Getter private static Image map2 = new Image("edu/wpi/teamA/images/map-page/Level 2.png");
  @Getter private static Image map3 = new Image("edu/wpi/teamA/images/map-page/Level 3.png");

  @Override
  public void init() {
    log.info("Starting Up");
    NodeDAOImp.createTable();
    EdgeDAOImp.createTable();
    LocNameDAOImp.createTable();
    MoveDAOImp.createTable();
  }

  @Override
  public void start(Stage primaryStage) throws IOException, IncorrectLengthException {
    /* primaryStage is generally only used if one of your components require the stage to display */
    App.primaryStage = primaryStage;

    final FXMLLoader loader = new FXMLLoader(App.class.getResource("views/Root.fxml"));
    final BorderPane root = loader.load();

    App.rootPane = root;

    UserDAOImp un = new UserDAOImp();
    un.createUserTable();

    // set up map entity arrays for edges and nodes
    mapEditorEntity.loadFloorEdges();
    mapEditorEntity.loadFloorNodes();

    final Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();

    Navigation.navigate(Screen.LOGIN);
  }

  @Override
  public void stop() {

    log.info("Shutting Down");
    DBConnectionProvider.closeConnection();
  }
}
