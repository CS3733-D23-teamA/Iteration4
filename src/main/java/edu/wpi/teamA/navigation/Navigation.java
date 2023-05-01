package edu.wpi.teamA.navigation;

import edu.wpi.teamA.App;
import java.io.IOException;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.util.Duration;

public class Navigation {
  private static IdleChecker idleMonitor =
      new IdleChecker(Duration.seconds(60), () -> Navigation.navigate(Screen.SCREEN_SAVER), true);

  public static void navigate(final Screen screen) {
    final String filename = screen.getFilename();
    Scene old = App.getPrimaryStage().getScene();
    idleMonitor.unregister(old, Event.ANY);
    try {
      final var resource = App.class.getResource(filename);
      final FXMLLoader loader = new FXMLLoader(resource);

      // sets stage title to title of screen
      App.getPrimaryStage().setTitle(String.valueOf(screen));

      App.getRootPane().setCenter(loader.load());
      if (!screen.getFilename().equals("views/ScreenSaver.fxml")) {
        idleMonitor.register(App.getPrimaryStage().getScene(), Event.ANY);
      }
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
  }
}
