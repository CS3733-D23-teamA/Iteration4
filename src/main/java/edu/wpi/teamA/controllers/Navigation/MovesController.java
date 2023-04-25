package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Move;
import edu.wpi.teamA.entities.MapEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class MovesController extends PageController {

  private DataBaseRepository databaseRepo = DataBaseRepository.getInstance();
  private MapEntity entity = App.getMapEntity();

  @FXML private MFXFilterComboBox<String> prevLongnameCombo;
  @FXML private MFXTextField longnameField;
  @FXML private MFXTextField shortnameField;
  @FXML private MFXFilterComboBox<String> nodetypeCombo;
  @FXML private DatePicker dateForMove;

  @FXML private MFXButton clearButton;
  @FXML private MFXButton submitButton;

  @FXML private TableView<Move> moveTable;
  @FXML private TableColumn<Move, Integer> nodeCol;
  @FXML private TableColumn<Move, String> nameCol;
  @FXML private TableColumn<Move, LocalDate> dateCol;

  @FXML
  public void initialize() {
    Collection<LinkedList<Move>> moveLinkedLists = databaseRepo.getMoveMap().values();

    ArrayList<Move> moves = new ArrayList<>();
    for (LinkedList<Move> linkedList : moveLinkedLists) {
      moves.addAll(linkedList);
    }

    for (Map.Entry<String, LocationName> entry : databaseRepo.getLocNameMap().entrySet()) {
      prevLongnameCombo.getItems().add(entry.getKey());
    }

    nodetypeCombo
        .getItems()
        .addAll(
            "CONF", "DEPT", "ELEV", "EXIT", "HALL", "INFO", "LABS", "REST", "RETL", "SERV", "STAI");

    displayFutureMoves();
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
  public void displayFutureMoves() {
    nodeCol.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("longName"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

    ArrayList<Move> updatedArray = new ArrayList<Move>();
    for (Map.Entry<Integer, LinkedList<Move>> entry : databaseRepo.getMoveMap().entrySet()) {
      LinkedList<Move> linkedList = entry.getValue();
      for (Move move : linkedList) {
        if (move.getDate().isAfter(LocalDate.now())) {
          updatedArray.add(move);
        }
      }
    }

    moveTable.setItems(FXCollections.observableArrayList(updatedArray));
    moveTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  @FXML
  public void validateSubmit() {
    if (entity.determineLongNameExists(longnameField.getText())) {
      longnameField.setBorder(Border.stroke(Color.web("0xf74c4c")));
    } else {
      longnameField.setBorder(Border.stroke(Color.web("0x000000")));
    }
    submitButton.setDisable(
        prevLongnameCombo.getSelectedIndex() == -1
            || longnameField.getText().isEmpty()
            || (entity.determineLongNameExists(longnameField.getText()))
            || shortnameField.getText().isEmpty()
            || nodetypeCombo.getSelectedIndex() == -1
            || dateForMove.getValue() == null);
  }

  @FXML
  public void clear() {
    prevLongnameCombo.getSelectionModel().clearSelection();
    longnameField.clear();
    shortnameField.clear();
    nodetypeCombo.getSelectionModel().clearSelection();
    dateForMove.setValue(null);
  }

  @FXML
  public void submit() {
    String prevLongName = prevLongnameCombo.getValue();
    int nodeID = databaseRepo.getMoveForLocName(prevLongName).getNodeID();
    String longName = longnameField.getText();
    String shortName = shortnameField.getText();
    String nodeType = nodetypeCombo.getValue();
    LocalDate localDate = dateForMove.getValue();

    Move move = new Move(nodeID, longName, localDate);
    LocationName locationName = new LocationName(longName, shortName, nodeType);

    databaseRepo.addMove(move);
    databaseRepo.addLocName(locationName);
    clear();
    displayFutureMoves();
  }
}
