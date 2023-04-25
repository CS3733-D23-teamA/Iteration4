package edu.wpi.teamA.database.Interfaces;

import java.util.ArrayList;
import java.util.HashMap;

public interface IServiceDAO<T> {

  HashMap map = new HashMap<>();

  HashMap loadDataFromDatabaseInMap();

  HashMap Import(String filepath);

  void Export(String filepath);

  void add(T obj);

  T get(int ID);

  void delete(T obj);

  void update(T obj);

  ArrayList<T> getAssigned(String username);

  ArrayList<T> getCreated(String username);

  int getNextID();
}
