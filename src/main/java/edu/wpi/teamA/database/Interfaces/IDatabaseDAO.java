package edu.wpi.teamA.database.Interfaces;

import java.util.HashMap;

public interface IDatabaseDAO<T> {

  /**
   * Loads data from the database into the DAO's hashmap
   *
   * @return the hashmap with the database data
   */
  HashMap loadDataFromDatabaseInMap();

  /**
   * Imports data into the database
   *
   * @param filepath where the CSV file to import data from is
   * @return the updated hashmap
   */
  HashMap Import(String filepath);

  /**
   * Exports data from the database
   *
   * @param filepath where the CSV file will end up on your machine
   */
  void Export(String filepath);

  /**
   * Adds an object into the database
   *
   * @param obj object to add
   * @return the object added
   */
  T Add(T obj);

  /**
   * Deletes an object from the database
   *
   * @param obj object to delete
   */
  void Delete(T obj);

  /**
   * Updates the database with the object
   *
   * @param obj the object beting updated
   */
  void Update(T obj);
}
