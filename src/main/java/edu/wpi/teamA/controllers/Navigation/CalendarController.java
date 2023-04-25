package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.ConferenceRoomResRequest;
import edu.wpi.teamA.database.Singletons.CalendarSingleton;
import edu.wpi.teamA.database.Singletons.DateSingleton;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class CalendarController {
  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();
  @FXML DatePicker dp;
  @FXML Label dl;
  @FXML
  Rectangle r1c11,
      r1c12,
      r1c13,
      r1c14,
      r1c21,
      r1c22,
      r1c23,
      r1c24,
      r1c31,
      r1c32,
      r1c33,
      r1c34,
      r1c41,
      r1c42,
      r1c43,
      r1c44,
      r1c51,
      r1c52,
      r1c53,
      r1c54,
      r1c61,
      r1c62,
      r1c63,
      r1c64;
  @FXML
  Rectangle r2c11,
      r2c12,
      r2c13,
      r2c14,
      r2c21,
      r2c22,
      r2c23,
      r2c24,
      r2c31,
      r2c32,
      r2c33,
      r2c34,
      r2c41,
      r2c42,
      r2c43,
      r2c44,
      r2c51,
      r2c52,
      r2c53,
      r2c54,
      r2c61,
      r2c62,
      r2c63,
      r2c64;
  @FXML
  Rectangle r3c11,
      r3c12,
      r3c13,
      r3c14,
      r3c21,
      r3c22,
      r3c23,
      r3c24,
      r3c31,
      r3c32,
      r3c33,
      r3c34,
      r3c41,
      r3c42,
      r3c43,
      r3c44,
      r3c51,
      r3c52,
      r3c53,
      r3c54,
      r3c61,
      r3c62,
      r3c63,
      r3c64;
  @FXML
  Rectangle r4c11,
      r4c12,
      r4c13,
      r4c14,
      r4c21,
      r4c22,
      r4c23,
      r4c24,
      r4c31,
      r4c32,
      r4c33,
      r4c34,
      r4c41,
      r4c42,
      r4c43,
      r4c44,
      r4c51,
      r4c52,
      r4c53,
      r4c54,
      r4c61,
      r4c62,
      r4c63,
      r4c64;
  @FXML
  Rectangle r5c11,
      r5c12,
      r5c13,
      r5c14,
      r5c21,
      r5c22,
      r5c23,
      r5c24,
      r5c31,
      r5c32,
      r5c33,
      r5c34,
      r5c41,
      r5c42,
      r5c43,
      r5c44,
      r5c51,
      r5c52,
      r5c53,
      r5c54,
      r5c61,
      r5c62,
      r5c63,
      r5c64;
  @FXML
  Rectangle r6c11,
      r6c12,
      r6c13,
      r6c14,
      r6c21,
      r6c22,
      r6c23,
      r6c24,
      r6c31,
      r6c32,
      r6c33,
      r6c34,
      r6c41,
      r6c42,
      r6c43,
      r6c44,
      r6c51,
      r6c52,
      r6c53,
      r6c54,
      r6c61,
      r6c62,
      r6c63,
      r6c64;
  @FXML
  Rectangle r7c11,
      r7c12,
      r7c13,
      r7c14,
      r7c21,
      r7c22,
      r7c23,
      r7c24,
      r7c31,
      r7c32,
      r7c33,
      r7c34,
      r7c41,
      r7c42,
      r7c43,
      r7c44,
      r7c51,
      r7c52,
      r7c53,
      r7c54,
      r7c61,
      r7c62,
      r7c63,
      r7c64;

  ArrayList<Rectangle> blocks;

  @FXML
  public void initialize() {
    LocalDate ld = LocalDate.now();
    Date d = Date.valueOf(ld);
    DateSingleton.INSTANCE.setValue(d);

    setColors();
  }

  public void setColors() {
    blocks = allBlocks();
    for (int count = 0; count < 168; count++) {
      blocks.get(count).setDisable(false);
      blocks.get(count).setFill(Paint.valueOf("#FFFFFF"));
    }
    int time = 0, rNum = 0, st, et, i, e, tFactor;
    String confName;
    Date d = DateSingleton.INSTANCE.getValue();
    dl.setText(d.toString());
    ArrayList<ConferenceRoomResRequest> rr = databaseRepo.filterDateCRRR(d);
    while (rNum < rr.size()) {
      i = 0;
      e = 0;
      confName = rr.get(rNum).getRoom();
      st = rr.get(rNum).getStartTime();
      et = rr.get(rNum).getEndTime();
      if (confName.equals("Abrams Conference Room")) {
        i = 0;
      } else if (confName.equals("Anesthesia Conf Floor L1")) {
        i = 24;
      } else if (confName.equals("Carrie M. Hall Conference Center Floor 2")) {
        i = 48;
      } else if (confName.equals("Medical Records Conference Room Floor L1")) {
        i = 72;
      } else if (confName.equals("Shapiro Board Room MapNode 20 Floor 1")) {
        i = 96;
      } else if (confName.equals("BTM Conference Center")) {
        i = 120;
      } else if (confName.equals("Duncan Reid Conference Room")) {
        i = 144;
      }
      e = i + 24;
      tFactor = 0;
      while (i < e) {
        time = 100 * tFactor;
        if (st <= time && et > time) {
          blocks.get(i).setFill(Paint.valueOf("#FF0000"));
          blocks.get(i).setDisable(true);
        } else if (!blocks.get(i).isDisable()) {
          blocks.get(i).setFill(Paint.valueOf("#FFFFFF"));
        }
        i++;
        tFactor++;
      }
      rNum++;
    }
  }

  public void changeDate() {
    DateSingleton.INSTANCE.setValue(Date.valueOf(dp.getValue()));
    setColors();
  }

  public ArrayList<Rectangle> allBlocks() {
    ArrayList<Rectangle> temp = new ArrayList<>();
    Collections.addAll(
        temp, r1c11, r1c12, r1c13, r1c14, r1c21, r1c22, r1c23, r1c24, r1c31, r1c32, r1c33, r1c34,
        r1c41, r1c42, r1c43, r1c44, r1c51, r1c52, r1c53, r1c54, r1c61, r1c62, r1c63, r1c64, r2c11,
        r2c12, r2c13, r2c14, r2c21, r2c22, r2c23, r2c24, r2c31, r2c32, r2c33, r2c34, r2c41, r2c42,
        r2c43, r2c44, r2c51, r2c52, r2c53, r2c54, r2c61, r2c62, r2c63, r2c64, r3c11, r3c12, r3c13,
        r3c14, r3c21, r3c22, r3c23, r3c24, r3c31, r3c32, r3c33, r3c34, r3c41, r3c42, r3c43, r3c44,
        r3c51, r3c52, r3c53, r3c54, r3c61, r3c62, r3c63, r3c64, r4c11, r4c12, r4c13, r4c14, r4c21,
        r4c22, r4c23, r4c24, r4c31, r4c32, r4c33, r4c34, r4c41, r4c42, r4c43, r4c44, r4c51, r4c52,
        r4c53, r4c54, r4c61, r4c62, r4c63, r4c64, r5c11, r5c12, r5c13, r5c14, r5c21, r5c22, r5c23,
        r5c24, r5c31, r5c32, r5c33, r5c34, r5c41, r5c42, r5c43, r5c44, r5c51, r5c52, r5c53, r5c54,
        r5c61, r5c62, r5c63, r5c64, r6c11, r6c12, r6c13, r6c14, r6c21, r6c22, r6c23, r6c24, r6c31,
        r6c32, r6c33, r6c34, r6c41, r6c42, r6c43, r6c44, r6c51, r6c52, r6c53, r6c54, r6c61, r6c62,
        r6c63, r6c64, r7c11, r7c12, r7c13, r7c14, r7c21, r7c22, r7c23, r7c24, r7c31, r7c32, r7c33,
        r7c34, r7c41, r7c42, r7c43, r7c44, r7c51, r7c52, r7c53, r7c54, r7c61, r7c62, r7c63, r7c64);
    return temp;
  }

  public void selectSquare(MouseEvent event) {
    Rectangle r = (Rectangle) event.getSource();
    ArrayList<Rectangle> test = CalendarSingleton.INSTANCE.getValue();
    if (test.contains(r)) {
      CalendarSingleton.INSTANCE.setDate(Date.valueOf(dl.getText()));
      Navigation.navigate(Screen.CONFERENCE_CALENDAR);
    } else if (checkConnected(r, test)) {
      r.setFill(Paint.valueOf("00ff00"));
      CalendarSingleton.INSTANCE.addValue(r);
    } else {
      r.setFill(Paint.valueOf("00ff00"));
      for (Rectangle or : CalendarSingleton.INSTANCE.getValue()) {
        or.setFill(Paint.valueOf("#FFFFFF"));
      }
      CalendarSingleton.INSTANCE.setNull();
      CalendarSingleton.INSTANCE.addValue(r);
    }
  }

  public boolean checkConnected(Rectangle r, ArrayList<Rectangle> rs) {
    if (rs.size() == 0) {
      return false;
    }
    Rectangle first = rs.get(0);
    Rectangle last = rs.get((rs.size() - 1));
    if (r.getId().charAt(1) != first.getId().charAt(1)) {
      return false;
    }
    if (r.getId().charAt(3) == first.getId().charAt(3)) {
      int rlast = (int) r.getId().charAt(4);
      int flast = (int) first.getId().charAt(4);
      if ((rlast + 1) == flast) {
        return true;
      }
    }
    if (r.getId().charAt(3) == last.getId().charAt(3)) {
      int rlast = (int) r.getId().charAt(4);
      int llast = (int) last.getId().charAt(4);
      if ((rlast - 1) == llast) {
        return true;
      }
    }
    if (r.getId().charAt(3) != first.getId().charAt(3)) {
      int rmid = (int) r.getId().charAt(3); // 1
      int fmid = (int) first.getId().charAt(3); // 2
      int rlast = (int) r.getId().charAt(4); // 4
      int flast = (int) first.getId().charAt(4); // 1
      if ((rlast == 52) && (flast == 49) && ((rmid + 1) == fmid)) {
        return true;
      }
    }
    if (r.getId().charAt(3) != last.getId().charAt(3)) {
      int rmid = (int) r.getId().charAt(3);
      int lmid = (int) last.getId().charAt(3);
      int rlast = (int) r.getId().charAt(4);
      int llast = (int) last.getId().charAt(4);

      if ((rlast == 49) && (llast == 52) && ((rmid - 1) == lmid)) {
        return true;
      }
    }
    return false;
  }
}
