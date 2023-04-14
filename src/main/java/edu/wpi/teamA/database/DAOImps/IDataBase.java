package edu.wpi.teamA.database.DAOImps;

public interface IDataBase<T> {

  void Import(T impT);

  void Export(T exT);

  void Add(T addT);

  void Delete(T delT);

  void Update(T upT);

  // 1.get all
  // 2.update
  // 3.delete

  // app start: instantiate and import all csv
  // if csv already exists, drag it from the database

  // pass rare data to the DAO, create new object in the DAO

}
