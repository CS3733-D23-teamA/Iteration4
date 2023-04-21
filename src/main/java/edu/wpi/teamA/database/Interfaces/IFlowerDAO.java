package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.ORMclasses.FlowerEntity;

public interface IFlowerDAO {

  public FlowerEntity getFlower(int id);

  public void updateFlower(FlowerEntity flower);

  public void addFlower(FlowerEntity flower);

  public void deleteFlower(FlowerEntity flower);

  public void editFlower(FlowerEntity oldFlower, FlowerEntity newFlower);
}
