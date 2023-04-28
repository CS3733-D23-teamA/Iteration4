package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Move;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MovesController extends PageController {

  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

  @FXML private TableView<Move> moveTable;
  @FXML private TableColumn<Move, Integer> nodeCol;
  @FXML private TableColumn<Move, String> nameCol;
  @FXML private TableColumn<Move, LocalDate> dateCol;

  @FXML private DatePicker currentDatePicker;

  @FXML
  public void initialize() {
    currentDatePicker.setValue(App.getCurrentDate());
    displayFutureMoves();
  }

  // display list of future moves
  public void displayFutureMoves() {
    nodeCol.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("longName"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

    ArrayList<Move> updatedArray = new ArrayList<>();
    for (Map.Entry<Integer, LinkedList<Move>> entry : databaseRepo.getNodeMoveMap().entrySet()) {
      LinkedList<Move> linkedList = entry.getValue();
      for (Move move : linkedList) {
        if (move.getDate().isAfter(App.getCurrentDate())) {
          updatedArray.add(move);
        }
      }
    }

    moveTable.setItems(FXCollections.observableArrayList(updatedArray));
    moveTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  @FXML
  public void submitCurrentDate() {
    if (currentDatePicker.getValue() != null) {
      App.setCurrentDate(currentDatePicker.getValue());
      displayFutureMoves();
      databaseRepo.setCurrentMoveMap(databaseRepo.loadCurrentMovesMap(App.getCurrentDate()));
    }
  }
}
