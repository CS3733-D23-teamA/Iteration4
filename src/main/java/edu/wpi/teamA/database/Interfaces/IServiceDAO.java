package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import java.util.ArrayList;
import java.util.List;

public interface IServiceDAO<T> {
  ArrayList<Object> aList = new ArrayList<>();

  static DBConnectionProvider con = new DBConnectionProvider();

  public List<T> getAll();

  public Object get(String key);

  public void update(T obj);

  public void add(T obj);

  public void delete(T obj);
}
