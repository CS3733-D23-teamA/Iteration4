package edu.wpi.teamA.controllers.Navigation;

import edu.wpi.teamA.App;
import edu.wpi.teamA.database.DataBaseRepository;
import javafx.fxml.FXML;

public class ImportExportController {
    private final DataBaseRepository databaseRepo = App.getDatabaseRepo();

    @FXML
    public void importNodes() {
        databaseRepo.importData();

    }

    @FXML public void importLocNames() {

    }

    @FXML public void importMoves() {

    }

    @FXML public void importEdges() {

    }

    @FXML public void importFlowers() {

    }

    @FXML public void importCRRR() {

    }

    @FXML public void importFurniture() {

    }

    @FXML public void importMeals() {

    }

    @FXML public void importEmployees() {

    }

    @FXML public void importUsers() {

    }

    @FXML public void importSignage() {

    }



    @FXML
    public void exportNodes() {

    }

    @FXML public void exportLocNames() {

    }

    @FXML public void exportMoves() {

    }

    @FXML public void exportEdges() {

    }

    @FXML public void exportFlowers() {

    }

    @FXML public void exportCRRR() {

    }

    @FXML public void exportFurniture() {

    }

    @FXML public void exportMeals() {

    }

    @FXML public void exportEmployees() {

    }

    @FXML public void exportUsers() {

    }

    @FXML public void exportSignage() {

    }
}
