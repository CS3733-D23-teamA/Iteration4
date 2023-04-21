package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IServiceDAO<T> {

  HashMap map = new HashMap<>();

  HashMap loadDataFromDatabaseInMap();
  HashMap Import(String filepath);
  void Export(String filepath);
  void add(T obj);
  void delete(T obj);
  void update(T obj);
}
