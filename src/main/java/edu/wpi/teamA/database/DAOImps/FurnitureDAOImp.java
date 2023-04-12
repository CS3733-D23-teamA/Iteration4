package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.IFurnitureDAO;
import edu.wpi.teamA.database.ORMclasses.FurnitureRequest;

import java.sql.*;
import java.util.ArrayList;

public class FurnitureDAOImp implements IFurnitureDAO {
    ArrayList<FurnitureRequest> furnitureArray;
    static DBConnectionProvider furnitureProvider = new DBConnectionProvider();

    public FurnitureDAOImp() {
        this.furnitureArray = new ArrayList<>();
    }

    public FurnitureDAOImp(ArrayList<FurnitureRequest> furnitureArray) {
        this.furnitureArray = furnitureArray;
    }

    @Override
    public void addFurniture(FurnitureRequest furniture) {
        /** Insert new node object to the existing node table */
        try {
            String name = furniture.getName();
            int room = furniture.getRoom();
            Date date = furniture.getDate();
            int time = furniture.getTime();
            String type = furniture.getFurnitureType();
            String comment = furniture.getComment();
            String status = furniture.getStatus();

            String sqlCreateEdge =
                    "Create Table if not exists \"Prototype2_schema\".\"Furniture\""
                            + "(namee   Varchar(600),"
                            + "room    int,"
                            + "datee    date,"
                            + "timee     int,"
                            + "furnitureType     Varchar(600),"
                            + "comment     Varchar(600),"
                            + "status  Varchar(600))";
            Statement stmtFurniture = furnitureProvider.createConnection().createStatement();
            stmtFurniture.execute(sqlCreateEdge);

            PreparedStatement ps =
                    furnitureProvider
                            .createConnection()
                            .prepareStatement(
                                    "INSERT INTO \"Prototype2_schema\".\"Furniture\" VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setInt(2, room);
            ps.setDate(3, date);
            ps.setInt(4, time);
            ps.setString(5, type);
            ps.setString(6, comment);
            ps.setString(7, status);
            ps.executeUpdate();

            furnitureArray.add(new FurnitureRequest(name, room, date, time, type, comment, status));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFurniture(FurnitureRequest furniture) {

        try {
            PreparedStatement ps =
                    furnitureProvider
                            .createConnection()
                            .prepareStatement("DELETE FROM \"Prototype2_schema\".\"Furniture\" WHERE name = ?");
            ps.setString(1, furniture.getName());
            ps.executeUpdate();

            furnitureArray.removeIf(furnitureRequest -> furnitureRequest.getName().equals(furniture.getName()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateFurniture(FurnitureRequest furniture) {}

    @Override
    public void editFurniture(FurnitureRequest furniture) {}
}
