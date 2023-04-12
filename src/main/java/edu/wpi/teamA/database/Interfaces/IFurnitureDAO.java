package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.ORMclasses.FurnitureRequest;

public interface IFurnitureDAO {

  public void updateFurniture(FurnitureRequest furniture);

  public void addFurniture(FurnitureRequest furniture);

  public void deleteFurniture(FurnitureRequest furniture);

  public void editFurniture(FurnitureRequest furniture);
}
