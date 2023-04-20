package edu.wpi.teamA.database.DAOImps;
import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.ISignageDAO;
import edu.wpi.teamA.database.ORMclasses.Employee;
import edu.wpi.teamA.database.ORMclasses.SignageComponent;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.util.HashMap;
import java.util.Objects;

public class SignageDAOImp implements ISignageDAO {

    @Getter @Setter private HashMap<String, SignageComponent> signageMap = new HashMap<>();
    static DBConnectionProvider signageProvider = new DBConnectionProvider();

    public SignageDAOImp() {
        this.signageMap = loadSignagesFromDatabaseInMap();
    }

    public SignageDAOImp(HashMap<String, SignageComponent> signageMap) {
        this.signageMap = signageMap;
    }

    @Override
    public SignageComponent getSignage(String locationName) {
        return signageMap.get(locationName);
    }

    public HashMap<String, SignageComponent> loadSignagesFromDatabaseInMap() {
        try {
            Statement st =
                    Objects.requireNonNull(DBConnectionProvider.createConnection()).createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"SignageComponent\"");

            while (rs.next()) {
                String locationName = rs.getString("locationName");
                String direction = rs.getString("direction");
                Date date = rs.getDate("date");
                Time time = rs.getTime("time");

                SignageComponent signage = new SignageComponent(locationName, direction, date, time);
                signageMap.put(locationName, signage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return signageMap;
    }

    @Override
    public void modifySignage(SignageComponent signage) {
        try {
            String locationName = signage.getLocationName();
            String direction = signage.getDirection();
            Date date = signage.getDate();
            Time time = signage.getTime();

            PreparedStatement ps =
                    signageProvider
                            .createConnection()
                            .prepareStatement(
                                    "UPDATE \"Prototype2_schema\".\"SignageComponent\" SET direction = ?, date = ?, time = ? WHERE locationName = ?");
            ps.setString(1, direction);
            ps.setDate(2, date);
            ps.setTime(3, time);
            ps.setString(4, locationName);

            signageMap.put(signage.getLocationName(), new SignageComponent(signage.getLocationName(), direction, date, time));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addSignage(SignageComponent signage) {

    }

    @Override
    public void removeSignage(SignageComponent signage) {

    }
}
