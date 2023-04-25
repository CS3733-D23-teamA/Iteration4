package edu.wpi.teamA.database.Singletons;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.scene.shape.Rectangle;

public enum CalendarSingleton {
  INSTANCE;
  ArrayList<Rectangle> rects = null;

  Date d;

  public ArrayList<Rectangle> getValue() {
    if (rects == null) {
      rects = new ArrayList<>();
    }
    Collections.sort(
        rects,
        new Comparator<Rectangle>() {
          @Override
          public int compare(Rectangle r1, Rectangle r2) {
            return r1.getId().compareTo(r2.getId());
          }
        });
    return rects;
  }

  public void addValue(Rectangle r1) {
    if (rects == null) {
      rects = new ArrayList<>();
    }
    this.rects.add(r1);
  }

  public void setNull() {
    this.rects = null;
  }

  public void setDate(Date d1) {
    this.d = d1;
  }

  public Date getDate() {
    return this.d;
  }
}
