package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import java.util.ArrayList;
import java.util.List;

public interface IServiceDAO {
  ArrayList<Object> aList = new ArrayList<>();
  static DBConnectionProvider con = new DBConnectionProvider();

  public List<Object> getAll();

  public Object get(String key);

  public void update(Object obj);

  public void add(Object obj);

  public void delete(Object obj);
}
