package edu.wpi.teamA.databaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import edu.wpi.teamA.database.DAOImps.MealDAOImp;
import edu.wpi.teamA.database.ORMclasses.Meal;
import java.sql.Date;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MealDAOImpTest {
  private MealDAOImp mealDAOImp;
  private Meal meal1;
  private Meal meal2;

  @BeforeEach
  public void setUp() {
    mealDAOImp = new MealDAOImp();

    // Create sample Meal objects
    meal1 =
        new Meal(
            10,
            "John Doe",
            "A101",
            Date.valueOf("2023-04-26"),
            12,
            "Pizza",
            "Extra cheese",
            "employee1",
            "Assigned",
            "creator1");
    meal2 =
        new Meal(
            11,
            "Jane Smith",
            "B102",
            Date.valueOf("2023-04-27"),
            13,
            "Burger",
            "No onions",
            "employee2",
            "Assigned",
            "creator2");
  }

  // Add other test methods here

  @Test
  public void testAddAndGet() {
    mealDAOImp.add(meal1);
    mealDAOImp.add(meal2);

    assertEquals(meal1, mealDAOImp.getMeal(1));
    assertEquals(meal2, mealDAOImp.getMeal(2));
  }

  @Test
  public void testDelete() {
    mealDAOImp.add(meal1);
    mealDAOImp.add(meal2);

    mealDAOImp.delete(meal1);
    assertNull(mealDAOImp.getMeal(1));
    assertEquals(meal2, mealDAOImp.getMeal(2));
  }

  @Test
  public void testUpdate() {
    mealDAOImp.add(meal1);

    Meal updatedMeal =
        new Meal(
            1,
            "John Doe",
            "C301",
            Date.valueOf("2023-04-28"),
            14,
            "Pasta",
            "Extra sauce",
            "employee3",
            "Completed",
            "creator1");
    mealDAOImp.update(updatedMeal);

    assertEquals(updatedMeal, mealDAOImp.getMeal(1));
  }

  @Test
  public void testGetAssigned() {
    mealDAOImp.add(meal1);
    mealDAOImp.add(meal2);

    ArrayList<Meal> assignedMealsEmployee1 = mealDAOImp.getAssigned("employee1");
    assertFalse(assignedMealsEmployee1.contains(meal1));
    assertTrue(!assignedMealsEmployee1.contains(meal2));

    ArrayList<Meal> assignedMealsEmployee2 = mealDAOImp.getAssigned("employee2");
    assertTrue(!assignedMealsEmployee2.contains(meal1));
    assertTrue(assignedMealsEmployee2.contains(meal2));
  }

  @Test
  public void testGetCreated() {
    mealDAOImp.add(meal1);
    mealDAOImp.add(meal2);

    ArrayList<Meal> createdMealsCreator1 = mealDAOImp.getCreated("creator1");
    assertTrue(createdMealsCreator1.contains(meal1));
    assertTrue(!createdMealsCreator1.contains(meal2));

    ArrayList<Meal> createdMealsCreator2 = mealDAOImp.getCreated("creator2");
    assertTrue(!createdMealsCreator2.contains(meal1));
    assertTrue(createdMealsCreator2.contains(meal2));
  }

  @Test
  public void testGetNextID() {
    int nextID = mealDAOImp.getNextID();
    mealDAOImp.add(meal1);
    assertEquals(nextID, meal1.getId());
    nextID = mealDAOImp.getNextID();
    mealDAOImp.add(meal2);
    assertEquals(nextID, meal2.getId());
  }
}
