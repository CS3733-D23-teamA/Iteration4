package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IEmployeeDAO;
import edu.wpi.teamA.database.ORMclasses.Employee;
import edu.wpi.teamA.database.ORMclasses.FlowerEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImp implements IEmployeeDAO {
  ArrayList<Employee> employeeArray;
  static DBConnectionProvider employeeProvider = new DBConnectionProvider();

  public EmployeeDAOImp() {
    this.employeeArray = new ArrayList<>();
  }

  public EmployeeDAOImp(ArrayList<Employee> employeeArray) {
    this.employeeArray = employeeArray;
  }

  @Override
  public List<Employee> getAllEmployees() {
    ArrayList<Employee> tempList = new ArrayList<>();
    try {
      Statement stmt = employeeProvider.createConnection().createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Employee\"");

      while (rs.next()) {
        String name = rs.getString("name");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String ID = rs.getString("ID");

        Employee temp = new Employee();
        temp.setName(name);
        temp.setUsername(username);
        temp.setPassword(password);
        temp.setID(ID);

        tempList.add(temp);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return tempList;
  }

  @Override
  public Employee getEmployee(String name) {
    Employee temp = new Employee();
    try {
      PreparedStatement ps =
              employeeProvider
                      .createConnection()
                      .prepareStatement("SELECT FROM \"Prototype2_schema\".\"Employee\" WHERE name = ?");
      ps.setString(1, name);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        String idk = rs.getString("name");
        String username = rs.getString("username");
        String password= rs.getString("password");
        String ID = rs.getString("ID");

        temp.setName(idk);
        temp.setUsername(username);
        temp.setPassword(password);
        temp.setID(ID);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return temp;
  }

  @Override
  public void modifyEmployee(Employee employee) {}

  @Override
  public void addEmployee(Employee employee) {
    try {
      String name = employee.getName();
      String username = employee.getUsername();
      String password = employee.getPassword();
      String ID = employee.getID();

      String sqlCreateEdge =
          "Create Table if not exists \"Prototype2_schema\".\"Employee\""
              + "(name    Varchar(600),"
              + "username    VarChar(600),"
              + "password    VarChar(600),"
              + "ID     VarChar(600)";
      Statement stmtEmployee = employeeProvider.createConnection().createStatement();
      stmtEmployee.execute(sqlCreateEdge);

      PreparedStatement ps =
          employeeProvider
              .createConnection()
              .prepareStatement(
                  "INSERT INTO \"Prototype2_schema\".\"Employee\" VALUES (?, ?, ?, ?)");
      ps.setString(1, name);
      ps.setString(2, username);
      ps.setString(3, password);
      ps.setString(4, ID);
      ps.executeUpdate();

      employeeArray.add(new Employee(name, username, password, ID));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void removeEmployee(Employee employee) {
    try {
      PreparedStatement ps =
          employeeProvider
              .createConnection()
              .prepareStatement("DELETE FROM \"Prototype2_schema\".\"Employee\" WHERE name = ?");
      ps.setString(1, employee.getName());
      ps.executeUpdate();

      employeeArray.removeIf(Employee -> Employee.getName().equals(employee.getName()));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
