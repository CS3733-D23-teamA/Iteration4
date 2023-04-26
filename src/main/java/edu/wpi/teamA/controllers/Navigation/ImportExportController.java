package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import java.io.File;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class ImportExportController {
  private final DataBaseRepository databaseRepo = App.getDatabaseRepo();

  private File importCSV() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open CSV File");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    return fileChooser.showOpenDialog(App.getPrimaryStage());
  }

  private File exportCSV() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Export CSV File to");
    return directoryChooser.showDialog(App.getPrimaryStage());
  }

  @FXML
  public void importNodes() {
    databaseRepo.importData(importCSV().getPath(), "Node");
  }

  @FXML
  public void importLocNames() {
    databaseRepo.importData(importCSV().getPath(), "LocName");
  }

  @FXML
  public void importMoves() {
    databaseRepo.importData(importCSV().getPath(), "Move");
  }

  @FXML
  public void importEdges() {
    databaseRepo.importData(importCSV().getPath(), "Edge");
  }

  @FXML
  public void importFlowers() {
    databaseRepo.importData(importCSV().getPath(), "Flower");
  }

  @FXML
  public void importCRRR() {
    databaseRepo.importData(importCSV().getPath(), "CRRR");
  }

  @FXML
  public void importFurniture() {
    databaseRepo.importData(importCSV().getPath(), "Furniture");
  }

  @FXML
  public void importMeals() {
    databaseRepo.importData(importCSV().getPath(), "Meal");
  }

  @FXML
  public void importEmployees() {
    databaseRepo.importData(importCSV().getPath(), "Employee");
  }

  @FXML
  public void importUsers() {
    databaseRepo.importData(importCSV().getPath(), "User");
  }

  @FXML
  public void importSignage() {
    databaseRepo.importData(importCSV().getPath(), "Signage");
  }

  @FXML
  public void exportNodes() {
    databaseRepo.exportData(exportCSV().getPath(), "Node");
  }

  @FXML
  public void exportLocNames() {
    databaseRepo.exportData(exportCSV().getPath(), "LocName");
  }

  @FXML
  public void exportMoves() {
    databaseRepo.exportData(exportCSV().getPath(), "Move");
  }

  @FXML
  public void exportEdges() {
    databaseRepo.exportData(exportCSV().getPath(), "Edge");
  }

  @FXML
  public void exportFlowers() {
    databaseRepo.exportData(exportCSV().getPath(), "Flower");
  }

  @FXML
  public void exportCRRR() {
    databaseRepo.exportData(exportCSV().getPath(), "CRRR");
  }

  @FXML
  public void exportFurniture() {
    databaseRepo.exportData(exportCSV().getPath(), "Furniture");
  }

  @FXML
  public void exportMeals() {
    databaseRepo.exportData(exportCSV().getPath(), "Meal");
  }

  @FXML
  public void exportEmployees() {
    databaseRepo.exportData(exportCSV().getPath(), "Employee");
  }

  @FXML
  public void exportUsers() {
    databaseRepo.exportData(exportCSV().getPath(), "User");
  }

  @FXML
  public void exportSignage() {
    databaseRepo.exportData(exportCSV().getPath(), "Signage");
  }
}
