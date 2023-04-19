package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.Move;
import edu.wpi.teamA.database.ORMclasses.Node;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

public class MovesController extends PageController {

  @FXML private TableView<Move> moveTable;

  // Move table
  private DataBaseRepository dataBaseRepository = DataBaseRepository.getInstance();
  @FXML private TableColumn<Move, Integer> nodeCol;
  @FXML private TableColumn<Move, String> nameCol;
  @FXML private TableColumn<Move, LocalDate> dateCol;

  @FXML
  public void initialize() {
    Collection<Move> moveColl = dataBaseRepository.getMoveMap().values();
    displayFutureMoves(new ArrayList<Move>(moveColl));
  }

  private File importHelper() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open CSV File");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    File selectedFile = fileChooser.showOpenDialog(App.getPrimaryStage());
    System.out.println(selectedFile.getPath());
    return selectedFile;
  }

  // display list of future moves
  public void displayFutureMoves(ArrayList<Move> moveArray) {
    nodeCol.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("longName"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

    ArrayList<Move> updatedArray = new ArrayList<Move>();
    for (Map.Entry<Integer, Move> entry : dataBaseRepository.getMoveMap().entrySet()) {
      Move move = entry.getValue();
      if (move.getDate().isAfter(LocalDate.now())) {
        updatedArray.add(move);
      }
    }

    moveTable.setItems(FXCollections.observableArrayList(updatedArray));
    moveTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  // enter a move along with date it becomes effective
  public void enterMove(Node node, String name, LocalDate date) {}

  // allow node up to 2 locations on specific date
  public void updateLoc(Move move, LocalDate date) {}
}
