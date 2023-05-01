package edu.wpi.teamA;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.IncorrectLengthException;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.entities.LevelEntity;
import edu.wpi.teamA.entities.MapEntity;
import edu.wpi.teamA.entities.ServiceRequestEntity;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {
  @Setter @Getter private static Stage primaryStage;
  @Setter @Getter private static BorderPane rootPane;
  @Setter @Getter private static LocalDate currentDate = LocalDate.now();
  @Setter @Getter private static LocationName currentLocation;

  // map entities + images

  @Getter private static Image mapL1 = new Image("edu/wpi/teamA/images/map-page/Level L1.png");
  @Getter private static Image mapL2 = new Image("edu/wpi/teamA/images/map-page/Level L2.png");
  @Getter private static Image map1 = new Image("edu/wpi/teamA/images/map-page/Level 1.png");
  @Getter private static Image map2 = new Image("edu/wpi/teamA/images/map-page/Level 2.png");
  @Getter private static Image map3 = new Image("edu/wpi/teamA/images/map-page/Level 3.png");

  @Getter
  private static Image homeWhite = new Image("edu/wpi/teamA/images/icons/bwh-logo-white.png");

  @Getter
  private static Image homeYello = new Image("edu/wpi/teamA/images/icons/bwh-logo-yello.png");

  @Getter
  private static Image signageRight = new Image("edu/wpi/teamA/images/icons/signage-right.png");

  @Getter
  private static Image signageLeft = new Image("edu/wpi/teamA/images/icons/signage-left.png");

  @Getter private static Image signageUp = new Image("edu/wpi/teamA/images/icons/signage-up.png");

  @Getter
  private static Image signageDown = new Image("edu/wpi/teamA/images/icons/signage-down.png");

  @Getter
  private static Image signageStop = new Image("edu/wpi/teamA/images/icons/signage-stop.png");

  // BWH Colors
  @Getter private static Color YELLOWBWH = Color.web("F0C747");
  @Getter private static Color BLUEBWH = Color.web("012D5A");
  @Getter private static Color LIGHTBLUEBWH = Color.web("98AABC");

  // entities + repository initiated - CALL LAST
  @Getter private static LevelEntity levelEntity = new LevelEntity();
  @Getter private static MapEntity mapEntity = new MapEntity();
  @Getter private static ServiceRequestEntity serviceRequestEntity = new ServiceRequestEntity();
  @Getter private static DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

  private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

  @Override
  public void init() {
    log.info("Starting Up");
    databaseRepo.createNodeTable();
    databaseRepo.createEdgeTable();
    databaseRepo.createLocNameTable();
    databaseRepo.createMoveTable();

    executor.scheduleAtFixedRate(() -> databaseRepo.updateCache(), 0, 60, TimeUnit.SECONDS);
  }

  @Override
  public void start(Stage primaryStage) throws IOException, IncorrectLengthException {
    /* primaryStage is generally only used if one of your components require the stage to display */
    App.primaryStage = primaryStage;
    final FXMLLoader loader = new FXMLLoader(App.class.getResource("views/Root.fxml"));
    final BorderPane root = loader.load();
    App.rootPane = root;

    // move to init?
    databaseRepo.createUserTable();

    // set up map entity arrays for edges and nodes
    mapEntity.loadFloorEdges();
    mapEntity.loadFloorNodes();

    // setting kiosk default location
    currentLocation = databaseRepo.getLocName("Garden Cafe");

    // setting up scene and stylesheets
    final Scene scene = new Scene(root);
    scene.getStylesheets().add("edu/wpi/teamA/views/stylesheets/main.css");
    primaryStage.setScene(scene);
    primaryStage.show();

    // navigate to login screen
    Navigation.navigate(Screen.LOGIN);
  }

  @Override
  public void stop() {

    log.info("Shutting Down");
    DBConnectionProvider.closeConnection();
    executor.shutdown();
  }
}
