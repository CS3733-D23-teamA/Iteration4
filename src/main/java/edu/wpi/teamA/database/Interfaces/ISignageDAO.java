package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.ORMclasses.SignageComponent;

import java.util.HashMap;

public interface ISignageDAO{

    public SignageComponent getSignage(String locationName);

    public HashMap<String, SignageComponent> loadSignagesFromDatabaseInMap();

    public void modifySignage(SignageComponent signage);

    public void addSignage(SignageComponent signage);

    public void removeSignage(SignageComponent signage);
}
