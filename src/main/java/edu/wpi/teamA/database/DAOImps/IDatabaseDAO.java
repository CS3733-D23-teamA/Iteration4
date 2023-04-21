package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.ORMclasses.Node;

import java.util.HashMap;

public interface IDatabaseDAO<T> {

  HashMap loadDataFromDatabaseInMap();

  HashMap Import(String filepath);

  void Export(String filepath);

  T Add(T obj);

  void Delete(T obj);

  void Update(T obj);
}
