package edu.wpi.teamA.databaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.teamA.database.DAOImps.MealDAOImp;
import edu.wpi.teamA.database.ORMclasses.Meal;
import java.sql.Date;
import org.junit.jupiter.api.Test;

public class MealReuqestTest {

  MealDAOImp mealDAOImp = new MealDAOImp();

  @Test
  public void testMealDAOImpGetMeal() {
    Meal meal1 = mealDAOImp.getMeal(1);
    Date date1 = new Date(123, 3, 21);
    assertEquals(date1, meal1.getDate());
    // meal1.getDate();
    // meal1.getEmployee();
    // meal1.getItems();
  }
}
