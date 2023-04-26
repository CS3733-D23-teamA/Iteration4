package edu.wpi.teamA.databaseTest;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamA.database.DAOImps.FurnitureDAOImp;
import edu.wpi.teamA.database.ORMclasses.FurnitureRequest;
import java.sql.Date;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FurnitureDAOImpTest {

  private FurnitureDAOImp furnitureDAOImp;
  private FurnitureRequest furnitureRequest1;
  private FurnitureRequest furnitureRequest2;

  @BeforeEach
  void setUp() {
    furnitureDAOImp = new FurnitureDAOImp();

    furnitureRequest1 =
        new FurnitureRequest(
            furnitureDAOImp.getNextID(),
            "John Doe",
            "Room 101",
            Date.valueOf("2023-04-25"),
            10,
            "Table, Chair",
            "Please deliver the items by 10 AM.",
            "Employee1",
            "Pending",
            "Creator1");

    furnitureRequest2 =
        new FurnitureRequest(
            furnitureDAOImp.getNextID(),
            "Jane Doe",
            "Room 102",
            Date.valueOf("2023-04-26"),
            15,
            "Sofa, Coffee Table",
            "Deliver items by 3 PM.",
            "Employee2",
            "Completed",
            "Creator2");
  }

  @Test
  void testAddAndGet() {
    furnitureDAOImp.add(furnitureRequest1);
    FurnitureRequest fetchedFurnitureRequest = furnitureDAOImp.get(furnitureRequest1.getId());
    assertEquals(furnitureRequest1, fetchedFurnitureRequest);
  }

  @Test
  void testUpdate() {
    furnitureDAOImp.add(furnitureRequest1);

    FurnitureRequest updatedFurnitureRequest =
        new FurnitureRequest(
            furnitureRequest1.getId(),
            "John Smith",
            "Room 103",
            Date.valueOf("2023-04-25"),
            10,
            "Table, Chair",
            "Please deliver the items by 10 AM.",
            "Employee1",
            "Pending",
            "Creator1");

    furnitureDAOImp.update(updatedFurnitureRequest);
    FurnitureRequest fetchedFurnitureRequest = furnitureDAOImp.get(furnitureRequest1.getId());
    assertEquals(updatedFurnitureRequest, fetchedFurnitureRequest);
  }

  @Test
  void testDelete() {
    furnitureDAOImp.add(furnitureRequest1);
    furnitureDAOImp.delete(furnitureRequest1);
    assertNull(furnitureDAOImp.get(furnitureRequest1.getId()));
  }

  @Test
  void testGetAssigned() {
    furnitureDAOImp.add(furnitureRequest1);
    furnitureDAOImp.add(furnitureRequest2);

    ArrayList<FurnitureRequest> assignedToEmployee1 = furnitureDAOImp.getAssigned("Employee1");
    assertTrue(assignedToEmployee1.contains(furnitureRequest1));
    assertFalse(assignedToEmployee1.contains(furnitureRequest2));

    ArrayList<FurnitureRequest> assignedToEmployee2 = furnitureDAOImp.getAssigned("Employee2");
    assertFalse(assignedToEmployee2.contains(furnitureRequest1));
    assertTrue(assignedToEmployee2.contains(furnitureRequest2));
  }

  @Test
  void testGetCreated() {
    furnitureDAOImp.add(furnitureRequest1);
    furnitureDAOImp.add(furnitureRequest2);

    ArrayList<FurnitureRequest> createdByCreator1 = furnitureDAOImp.getCreated("Creator1");
    assertTrue(createdByCreator1.contains(furnitureRequest1));
    assertFalse(createdByCreator1.contains(furnitureRequest2));

    ArrayList<FurnitureRequest> createdByCreator2 = furnitureDAOImp.getCreated("Creator2");
    assertFalse(createdByCreator2.contains(furnitureRequest1));
    assertTrue(createdByCreator2.contains(furnitureRequest2));
  }
}
