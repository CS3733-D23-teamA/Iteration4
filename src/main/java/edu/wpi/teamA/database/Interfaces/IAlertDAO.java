package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.ORMclasses.Alert;
import java.util.HashMap;

public interface IAlertDAO {
    public Alert getAlert(int ticketNum);

    public HashMap<Integer, Alert> loadAlertsFromDatabaseInMap();

    public void modifyAlert(Alert alert);

    public void addAlert(Alert alert);

    public void removeAlert(Alert alert);
}
