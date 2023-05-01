package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.DataBaseRepository;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class DataSettingsController {

  @FXML private ToggleButton changeLocalDatabase;
  @FXML private ToggleButton changeAWSDatabase;
  private ToggleGroup changeDatabaseGroup = new ToggleGroup();

  @FXML
  public void initialize() {
    changeLocalDatabase.setToggleGroup(changeDatabaseGroup);
    changeAWSDatabase.setToggleGroup(changeDatabaseGroup);
    changeLocalDatabase.setSelected(true);
  }

  private final DataBaseRepository databaseRepo = App.getDatabaseRepo();

  private File importCSV(String type) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open CSV File for " + type);
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    return fileChooser.showOpenDialog(App.getPrimaryStage());
  }

  private File exportCSV(String type) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Export CSV File for " + type + " to");
    return directoryChooser.showDialog(App.getPrimaryStage());
  }

  @FXML
  public void changeLocalDatabase() {
    DBConnectionProvider.createLocalConnection();
  }

  @FXML
  public void changeAWSDatabase() {
    // DBConnectionProvider.createAWSConnection();
  }

  @FXML
  public void importAll() {
    importNodes();
    importLocNames();
    importMoves();
    importEdges();
    importFlowers();
    importCRRR();
    importFurniture();
    importMeals();
    importEmployees();
    importUsers();
    importSignage();
  }

  @FXML
  public void exportAll() {
    exportNodes();
    exportLocNames();
    exportMoves();
    exportEdges();
    exportFlowers();
    exportCRRR();
    exportFurniture();
    exportMeals();
    exportEmployees();
    exportUsers();
    exportSignage();
  }

  @FXML
  public void importNodes() {
    databaseRepo.importData(importCSV("Node").getPath(), "Node");
  }

  @FXML
  public void importLocNames() {
    databaseRepo.importData(importCSV("Location Name").getPath(), "LocName");
  }

  @FXML
  public void importMoves() {
    databaseRepo.importData(importCSV("Move").getPath(), "Move");
  }

  @FXML
  public void importEdges() {
    databaseRepo.importData(importCSV("Edge").getPath(), "Edge");
  }

  @FXML
  public void importFlowers() {
    databaseRepo.importData(importCSV("Flower").getPath(), "Flower");
  }

  @FXML
  public void importCRRR() {
    databaseRepo.importData(importCSV("CRRR").getPath(), "CRRR");
  }

  @FXML
  public void importFurniture() {
    databaseRepo.importData(importCSV("Furniture").getPath(), "Furniture");
  }

  @FXML
  public void importMeals() {
    databaseRepo.importData(importCSV("Meal").getPath(), "Meal");
  }

  @FXML
  public void importEmployees() {
    databaseRepo.importData(importCSV("Employee").getPath(), "Employee");
  }

  @FXML
  public void importUsers() {
    databaseRepo.importData(importCSV("User").getPath(), "User");
  }

  @FXML
  public void importSignage() {
    databaseRepo.importData(importCSV("Signage").getPath(), "Signage");
  }

  @FXML
  public void exportNodes() {
    databaseRepo.exportData(exportCSV("Node").getPath(), "Node");
  }

  @FXML
  public void exportLocNames() {
    databaseRepo.exportData(exportCSV("Location Name").getPath(), "LocName");
  }

  @FXML
  public void exportMoves() {
    databaseRepo.exportData(exportCSV("Move").getPath(), "Move");
  }

  @FXML
  public void exportEdges() {
    databaseRepo.exportData(exportCSV("Edge").getPath(), "Edge");
  }

  @FXML
  public void exportFlowers() {
    databaseRepo.exportData(exportCSV("Flower").getPath(), "Flower");
  }

  @FXML
  public void exportCRRR() {
    databaseRepo.exportData(exportCSV("CRRR").getPath(), "CRRR");
  }

  @FXML
  public void exportFurniture() {
    databaseRepo.exportData(exportCSV("Furniture").getPath(), "Furniture");
  }

  @FXML
  public void exportMeals() {
    databaseRepo.exportData(exportCSV("Meal").getPath(), "Meal");
  }

  @FXML
  public void exportEmployees() {
    databaseRepo.exportData(exportCSV("Employee").getPath(), "Employee");
  }

  @FXML
  public void exportUsers() {
    databaseRepo.exportData(exportCSV("User").getPath(), "User");
  }

  @FXML
  public void exportSignage() {
    databaseRepo.exportData(exportCSV("Signage").getPath(), "Signage");
  }
}
