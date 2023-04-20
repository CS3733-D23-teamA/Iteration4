package edu.wpi.teamA.database.DAOImps;

import java.util.HashMap;

public interface IDatabaseDAO {

  HashMap loadDataFromDatabaseInMap();

  HashMap Import(String filepath);

  void Export(String filepath);
}
