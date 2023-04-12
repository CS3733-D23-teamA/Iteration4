package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.ORMclasses.Node;

public interface INodeDAO {
  Node getNode(int nodeID);

  void Add(int nodeID, int xcoord, int ycoord, String floor, String building);

  void Delete(int nodeID);
}
