package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IEmployeeDAO;
import edu.wpi.teamA.database.ORMclasses.Employee;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class EmployeeDAOImp implements IEmployeeDAO {

  @Getter @Setter private HashMap<String, Employee> employeeMap = new HashMap<>();

  public EmployeeDAOImp() {
    this.employeeMap = loadEmployeesFromDatabaseInMap();
  }

  @Override
  public Employee getEmployee(String username) {
    return employeeMap.get(username);
  }

  public HashMap<String, Employee> loadEmployeesFromDatabaseInMap() {
    try {
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Employee\"");

      while (rs.next()) {
        String name = rs.getString("name");
        String username = rs.getString("username");
        String password = rs.getString("password");

        Employee employee = new Employee(name, username, password);
        employeeMap.put(username, employee);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return employeeMap;
  }

  public HashMap<String, Employee> Import(String filePath) {
    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        String name = data[0];
        String username = data[1];
        String password = data[2];

        PreparedStatement ps =
            Objects.requireNonNull(DBConnectionProvider.getInstance())
                .prepareStatement("INSERT INTO \"Teama_schema\".\"Employee\" VALUES (?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, username);
        ps.setString(3, password);
        ps.executeUpdate();

        Employee employee = new Employee(name, username, password);
        employeeMap.put(username, employee);
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return employeeMap;
  }

  public void Export(String folderExportPath) {
    try {
      String newFile = folderExportPath + "/Employee.csv";
      Statement st = Objects.requireNonNull(DBConnectionProvider.getInstance()).createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Employee\"");

      FileWriter csvWriter = new FileWriter(newFile);

      csvWriter.append("name,username,password\n");

      while (rs.next()) {
        csvWriter.append(rs.getString("name")).append(",");
        csvWriter.append(rs.getString("username")).append(",");
        csvWriter.append(rs.getString("password")).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("Employee table exported to Employee.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void modifyEmployee(Employee employee) {
    try {
      String name = employee.getName();
      String username = employee.getUsername();
      String password = employee.getPassword();

      PreparedStatement ps =
          DBConnectionProvider.getInstance()
              .prepareStatement(
                  "UPDATE \"Teama_schema\".\"Employee\" SET name = ?, password = ? WHERE username = ?");
      ps.setString(1, name);
      ps.setString(2, password);
      ps.setString(3, username);

      employeeMap.put(employee.getUsername(), new Employee(name, employee.getUsername(), password));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addEmployee(Employee employee) {
    try {
      String name = employee.getName();
      String username = employee.getUsername();
      String password = employee.getPassword();

      String sqlCreateEdge =
          "Create Table if not exists \"Teama_schema\".\"Employee\""
              + "(namee     Varchar(600),"
              + "username    Varchar(600),"
              + "password    VarChar(600))";
      Statement stmtEmployee = DBConnectionProvider.getInstance().createStatement();
      stmtEmployee.execute(sqlCreateEdge);

      PreparedStatement ps =
          DBConnectionProvider.getInstance()
              .prepareStatement("INSERT INTO \"Teama_schema\".\"Employee\" VALUES (?, ?, ?)");
      ps.setString(1, name);
      ps.setString(2, username);
      ps.setString(3, password);
      ps.executeUpdate();

      employeeMap.put(employee.getUsername(), new Employee(name, employee.getUsername(), password));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void removeEmployee(Employee employee) {
    try {
      PreparedStatement ps =
          DBConnectionProvider.getInstance()
              .prepareStatement("DELETE FROM \"Teama_schema\".\"Employee\" WHERE username = ?");
      ps.setString(1, employee.getUsername());
      ps.executeUpdate();

      employeeMap.remove(employee.getUsername());

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
