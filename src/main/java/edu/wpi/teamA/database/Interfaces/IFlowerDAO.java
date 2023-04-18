package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.ORMclasses.FlowerEntity;

import java.util.HashMap;
import java.util.List;

public interface IFlowerDAO {
  public HashMap<Integer, FlowerEntity> getAllFlowers();

  public FlowerEntity getFlower(int id);

  public void updateFlower(FlowerEntity flower);

  public void addFlower(FlowerEntity flower);

  public void deleteFlower(FlowerEntity flower);
}
