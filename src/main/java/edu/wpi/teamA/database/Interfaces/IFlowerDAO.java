package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.ORMclasses.FlowerEntity;
import java.util.List;

public interface IFlowerDAO {
  public List<FlowerEntity> getAllFlowers();

  public FlowerEntity getFlower(String name);

  public void updateFlower(FlowerEntity flower);

  public void addFlower(FlowerEntity flower);

  public void deleteFlower(FlowerEntity flower);
}
