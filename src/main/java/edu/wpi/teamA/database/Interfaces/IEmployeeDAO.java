package edu.wpi.teamA.database.Interfaces;

import edu.wpi.teamA.database.ORMclasses.Employee;

import java.util.HashMap;
import java.util.List;

public interface IEmployeeDAO {
  public HashMap<String, Employee> getAllEmployees();

  public Employee getEmployee(String name);

  public void modifyEmployee(Employee employee);

  public void addEmployee(Employee employee);

  public void removeEmployee(Employee employee);
}
