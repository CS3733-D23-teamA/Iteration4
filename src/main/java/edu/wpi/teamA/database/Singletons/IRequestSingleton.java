package edu.wpi.teamA.database.Singletons;

public interface IRequestSingleton<T> {
  T getValue();

  void setValue(T obj);
}
