package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Move;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
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
import javafx.scene.text.Text;

public class MovesController {

  private final DataBaseRepository databaseRepo = DataBaseRepository.getInstance();

  @FXML private TableView<Move> moveTable;
  @FXML private TableColumn<Move, Integer> nodeCol;
  @FXML private TableColumn<Move, String> nameCol;
  @FXML private TableColumn<Move, LocalDate> dateCol;

  @FXML private DatePicker currentDatePicker;

  @FXML private MFXGenericDialog editRemoveDialog;
  @FXML private Text nodeMovingTo;
  @FXML private Text locationMoving;
  @FXML private DatePicker editDate;
  @FXML private Text moveError;

  private Move currentMove;

  /** Initializes the move page screen */
  @FXML
  public void initialize() {
    currentDatePicker.setValue(App.getCurrentDate());

    editRemoveDialog.setVisible(false);
    editRemoveDialog.setDisable(true);
    moveError.setVisible(false);

    editRemoveDialog.setOnClose(
        event -> {
          editRemoveDialog.setVisible(false);
          editRemoveDialog.setDisable(true);
        });

    moveTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection != null) {
                currentMove = newSelection;
                preloadDialog(currentMove);
                moveError.setVisible(false);
                editRemoveDialog.setVisible(true);
                editRemoveDialog.setDisable(false);
              }
            });

    moveError.setVisible(false);

    displayFutureMoves();
  }

  /** Displays all the future moves in the table */
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

  /** Changes the current date based on the date picker */
  @FXML
  public void submitCurrentDate() {
    if (currentDatePicker.getValue() != null) {
      App.setCurrentDate(currentDatePicker.getValue());
      displayFutureMoves();
      databaseRepo.setCurrentMoveMap(databaseRepo.loadCurrentMovesMap(App.getCurrentDate()));
    }
  }

  /**
   * Preloads the dialog with the move
   *
   * @param move the move to preload with
   */
  private void preloadDialog(Move move) {
    nodeMovingTo.setText(move.getNodeID().toString());
    locationMoving.setText(move.getLongName());
    editDate.setValue(move.getDate());
  }

  /** Removes the current move */
  @FXML
  public void removeMove() {
    databaseRepo.deleteMove(currentMove);
    currentMove = null;
    editRemoveDialog.setVisible(false);
    editRemoveDialog.setDisable(true);
    displayFutureMoves();
  }

  /** Updates the move */
  @FXML
  public void submitEdit() {
    Move newMove =
        new Move(currentMove.getNodeID(), currentMove.getLongName(), editDate.getValue());
    if (currentMove.getDate().isEqual(newMove.getDate())) {
      moveError.setVisible(true);
    } else {
      databaseRepo.updateMove(currentMove, newMove);
      moveError.setVisible(false);
    }

    currentMove = null;
    editRemoveDialog.setVisible(false);
    editRemoveDialog.setDisable(true);
    displayFutureMoves();
  }
}
