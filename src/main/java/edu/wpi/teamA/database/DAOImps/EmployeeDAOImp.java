package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IEmployeeDAO;
import edu.wpi.teamA.database.ORMclasses.Employee;

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
        return null;
    }

    @Override
    public Employee getEmployee(String name) {
        return null;
    }

    @Override
    public void modifyEmployee(Employee employee) {

    }

    @Override
    public void addEmployee(Employee employee) {
        try {
            String name = employee.getName();
            String username = employee.getUsername();
            String password = employee.getPassword();
            String ID = employee.getID();

            String sqlCreateEdge =
                    "Create Table if not exists \"Prototype2_schema\".\"Employee\""
                            + "(namee    Varchar(600),"
                            + "usernamee    VarChar(600),"
                            + "password    VarChar(600),"
                            + "ID     VarChar(600)";
            Statement stmtEmployee = employeeProvider.createConnection().createStatement();
            stmtEmployee.execute(sqlCreateEdge);

            PreparedStatement ps =
                    employeeProvider.createConnection()
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
                    employeeProvider.createConnection()
                            .prepareStatement("DELETE FROM \"Prototype2_schema\".\"Employee\" WHERE namee = ?");
            ps.setString(1, employee.getName());
            ps.executeUpdate();

            employeeArray.removeIf(Employee -> Employee.getName().equals(employee.getName()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
