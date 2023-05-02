package edu.wpi.teamA.navigation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.util.Duration;
import lombok.Getter;

public class IdleChecker {
  private final Timeline idleTimeline;
  @Getter private final EventHandler<Event> userEventHandler;

  public IdleChecker(Duration idleTime, Runnable notifier, boolean startMonitoring) {
    idleTimeline = new Timeline(new KeyFrame(idleTime, e -> notifier.run()));
    idleTimeline.setCycleCount(Animation.INDEFINITE);
    userEventHandler = e -> notIdle();

    if (startMonitoring) {
      startMonitoring();
    }
  }

  public void register(Scene scene, EventType<? extends Event> eventType) {
    scene.addEventFilter(eventType, userEventHandler);
  }

  public void unregister(Scene scene, EventType<? extends Event> eventType) {
    scene.removeEventFilter(eventType, userEventHandler);
  }

  public void notIdle() {
    if (idleTimeline.getStatus() == Animation.Status.RUNNING) {
      idleTimeline.playFromStart();
    }
  }

  public void startMonitoring() {
    idleTimeline.playFromStart();
  }

  public void stopMonitoring() {
    idleTimeline.stop();
  }
}
