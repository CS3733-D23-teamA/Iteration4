package edu.wpi.teamA.database.DAOImps;

import java.util.HashMap;

public interface IDatabaseDAO {

  HashMap loadDataFromDatabaseInMap();

  HashMap Import(String filepath);

  void Export(String filepath);

  // void Add();

  // void Delete();

  // void Update();

  // 1.get all
  // 2.update
  // 3.delete

  // app start: instantiate and import all csv
  // if csv already exists, drag it from the database

  // pass rare data to the DAO, create new object in the DAO

}
