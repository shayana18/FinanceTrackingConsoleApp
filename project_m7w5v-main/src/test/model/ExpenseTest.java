package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;



import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTest {
    private Expense Housing;
    private Expense Food;
    private Expense Fun;

    private LocalDate today;

    @BeforeEach
    public void runBefore(){

        today = LocalDate.parse("2024-02-15");
        Housing = new Expense("Rent",today, 1150.0, Type.BILLS);
        Food = new Expense("Ordered Lunch",today, 32.5, Type.FOOD);
        Fun = new Expense("Arcade",today, 25.0, Type.PERSONAL_ENJOYMENT);
    }

    @Test
    public void TestExpenseConstructor() {
        // check all information came through correctly
        assertEquals(Housing.getExpenseName(), "Rent");
        assertEquals(Housing.getExpenseDate(), today);
        assertEquals(Housing.getExpenseAmount(), 1150.0);

    }

    @Test
    public void TestExpenseName() {
        // testing both set and get here
        assertEquals(Food.getExpenseName(), "Ordered Lunch"); // ensuring it is able to get intialized value
        Food.setExpenseName("Ordered Dinner");
        assertEquals(Food.getExpenseName(), "Ordered Dinner"); // ensuring it works after name change
        Food.setExpenseName("Ordered Lunch");
        assertEquals(Food.getExpenseName(), "Ordered Lunch"); //setting it back to the original value to
    }

    @Test
    public void TestExpenseDate() {
        //Testing both getting and setting
        LocalDate yesterday = LocalDate.parse("2024-02-14");
        LocalDate lastMonth = LocalDate.parse("2024-01-15");
        LocalDate lastYear = LocalDate.parse("2023-02-14");
        LocalDate allDiff = LocalDate.parse("2023-01-14");

        assertEquals(Food.getExpenseDate(), today); // able to retrieve unchanged data
        Food.setExpenseDate(yesterday); //setting it to previous day
        assertEquals(Food.getExpenseDate(), yesterday);
        Food.setExpenseDate(lastMonth); //setting it to previous month
        assertEquals(Food.getExpenseDate(), lastMonth);
        Food.setExpenseDate(lastYear); //setting it to previous year
        assertEquals(Food.getExpenseDate(), lastYear);
        Food.setExpenseDate(allDiff); // changing to a date with all three different components
        assertEquals(Food.getExpenseDate(), allDiff);
        Food.setExpenseDate(today);
        assertEquals(Food.getExpenseDate(), today); // checking it doese return back to original

    }

    @Test
    public void TestExpenseAmount() {
        //Testing getting and setting
        assertEquals(Housing.getExpenseAmount(), 1150); // getting original amount
        Housing.setExpenseAmount(2000.4); // increasing amount
        assertEquals(Housing.getExpenseAmount(), 2000.4); // testing if it gets increased amount
        assertNotEquals(Housing.getExpenseAmount(),2000.3); // ensuring decimal is read correctly
        Housing.setExpenseAmount(1100.3); // decreasing
        assertEquals(Housing.getExpenseAmount(), 1100.3);
        Housing.setExpenseAmount(0.1); // should not throw exception
        assertEquals(Housing.getExpenseAmount(), 0.1);
    }

    @Test
    public void TestExpenseType() {
        //Testing Setters and getters
        assertEquals(Food.getExpenseType(), Type.FOOD); //getting original
        Food.setExpenseType(Type.PERSONAL_ENJOYMENT);
        assertEquals(Food.getExpenseType(), Type.PERSONAL_ENJOYMENT);
        Food.setExpenseType(Type.FOOD);
        assertEquals(Food.getExpenseType(), Type.FOOD);
    }


    @Test
    public void TestExpenseToString() {
        assertEquals(Food.makeExpenseString(), "(Date: 2024-02-15) Ordered Lunch of type FOOD costing $32.5\n");
        Food.setExpenseName("Ordered Dinner");// change name
        assertEquals(Food.makeExpenseString(), "(Date: 2024-02-15) Ordered Dinner of type FOOD costing $32.5\n");
        Food.setExpenseAmount(10.0);// change amount
        assertEquals(Food.makeExpenseString(), "(Date: 2024-02-15) Ordered Dinner of type FOOD costing $10.0\n");
        Food.setExpenseType(Type.PERSONAL_ENJOYMENT); // change type
        assertEquals(Food.makeExpenseString(),
                "(Date: 2024-02-15) Ordered Dinner of type PERSONAL_ENJOYMENT costing $10.0\n");
        Food.setExpenseDate(LocalDate.parse("2024-03-07")); // change date
        assertEquals("(Date: 2024-03-07) Ordered Dinner of type PERSONAL_ENJOYMENT costing $10.0\n",Food.makeExpenseString());

        assertEquals(Housing.makeExpenseString(),
                "(Date: 2024-02-15) Rent of type BILLS costing $1150.0\n");

    }
}
