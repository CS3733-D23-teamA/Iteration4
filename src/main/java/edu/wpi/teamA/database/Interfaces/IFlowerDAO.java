package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.ORMclasses.Flower;

public interface IFlowerDAO {

  public Flower getFlower(int id);

  public void updateFlower(Flower flower);

  public void addFlower(Flower flower);

  public void deleteFlower(Flower flower);
}
