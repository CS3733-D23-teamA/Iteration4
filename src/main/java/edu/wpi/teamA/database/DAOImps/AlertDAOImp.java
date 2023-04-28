package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IAlertDAO;
import edu.wpi.teamA.database.ORMclasses.Alert;
import lombok.Getter;
import lombok.Setter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

public class AlertDAOImp implements IAlertDAO {
    @Getter @Setter private HashMap<Integer, Alert> alertMap = new HashMap<>();
    static DBConnectionProvider alertProvider = new DBConnectionProvider();
    public AlertDAOImp() {
        this.alertMap = loadAlertsFromDatabaseInMap();
    }
    public AlertDAOImp(HashMap<Integer, Alert> signageMap) {
        this.alertMap = alertMap;
    }

    public Alert getAlert(int ticketNum) {
        return alertMap.get(ticketNum);
    }

    public HashMap<Integer, Alert> loadAlertsFromDatabaseInMap() {
        try {
            Statement st =
                    Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Alert\"");

            while (rs.next()) {
                int ticketNum = rs.getInt("ticket_num");
                String username = rs.getString("username");
                Date date = rs.getDate("date");
                String message = rs.getString("message");

                Alert alert =
                        new Alert(ticketNum, username, date.toLocalDate(), message);
                alertMap.put(ticketNum, alert);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return alertMap;
    }


    public HashMap<Integer, Alert> Import(String filePath) {
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
            csvReader.readLine();
            String row;

            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");

                int ticketNum = Integer.parseInt(data[0]);
                String username = data[1];
                Date date = Date.valueOf((data[2]));
                String message = data[3];

                PreparedStatement ps =
                        Objects.requireNonNull(DBConnectionProvider.createConnection())
                                .prepareStatement(
                                        "INSERT INTO \"Teama_schema\".\"SignageComponent\" VALUES (?, ?, ?, ?)");
                ps.setInt(1, ticketNum);
                ps.setString(2, username);
                ps.setDate(3, date);
                ps.setString(4, message);
                ps.executeUpdate();

                Alert alert =
                        new Alert(ticketNum, username, date.toLocalDate(), message);
                alertMap.put(ticketNum, alert);
            }
            csvReader.close();
        } catch (SQLException | IOException e) {

            throw new RuntimeException(e);
        }
        return alertMap;
    }


    public void Export(String folderExportPath) {
        try {
            String newFile = folderExportPath + "/Alert.csv";
            Statement st =
                    Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM \"Teama_schema\".\"Alert\"");

            FileWriter csvWriter = new FileWriter(newFile);

            csvWriter.append("ticket_num,username,date,message\n");

            while (rs.next()) {
                csvWriter.append((rs.getInt("ticket_num")) + (","));
                csvWriter.append((rs.getString("username")) + (","));
                csvWriter.append((rs.getDate("date").toLocalDate()) + (","));
                csvWriter.append((rs.getString("message"))+ "\n");
            }

            csvWriter.flush();
            csvWriter.close();

            System.out.println("Alert table exported to Alert.csv");

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifyAlert(Alert alert) {
        try {
            int ticketNum = alert.getTicketNum();
            String username = alert.getUsername();
            LocalDate date = alert.getDate(); //local date object
            String message = alert.getMessage();


            PreparedStatement ps =
                    alertProvider
                            .createConnection()
                            .prepareStatement(
                                    "UPDATE \"Teama_schema\".\"Alert\" SET username = ?, date = ?, message = ? WHERE ticketNum = ?");
            ps.setString(1, username);
            ps.setDate(2, Date.valueOf(date.now())); // date object
            ps.setString(3, message);
            ps.setInt(4, ticketNum);

            alertMap.put(
                    alert.getTicketNum(),
                    new Alert(alert.getTicketNum(), username, date, message));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addAlert(Alert alert) {
        try {
            int ticketNum = alert.getTicketNum();
            String username = alert.getUsername();
            Date date = Date.valueOf(alert.getDate());
            String message = alert.getMessage();

            PreparedStatement ps =
                    Objects.requireNonNull(DBConnectionProvider.createConnection())
                            .prepareStatement(
                                    "INSERT INTO \"Teama_schema\".\"Alert\" VALUES (?, ?, ?, ?)");
            ps.setInt(1, ticketNum);
            ps.setString(2, username);
            ps.setDate(3, date);
            ps.setString(4, message);
            ps.executeUpdate();


            alertMap.put(
                    ticketNum, new Alert(ticketNum, username, date.toLocalDate(), message));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeAlert(Alert alert) {
        try {
            PreparedStatement ps =
                    Objects.requireNonNull(DBConnectionProvider.createConnection())
                            .prepareStatement(
                                    "DELETE FROM \"Teama_schema\".\"Alert\" WHERE ticketNum = ?");
            ps.setInt(1, alert.getTicketNum());
            ps.executeUpdate();

            // String signageID = signage.getLocationName() + signage.getDate().toString();
            alertMap.remove(alert.getTicketNum());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
