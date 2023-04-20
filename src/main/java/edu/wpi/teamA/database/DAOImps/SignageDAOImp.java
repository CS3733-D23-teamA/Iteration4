package edu.wpi.teamA.database.DAOImps;
import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.ISignageDAO;
import edu.wpi.teamA.database.ORMclasses.SignageComponent;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.util.HashMap;
import java.util.Objects;

public class SignageDAOImp implements ISignageDAO {

    @Getter @Setter private HashMap<String, SignageComponent> signageMap = new HashMap<>();
    static DBConnectionProvider employeeProvider = new DBConnectionProvider();

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
}
