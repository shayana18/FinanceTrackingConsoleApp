package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetTest {
    private Budget testBudget;

    @BeforeEach
    public void runBefore() {
        testBudget = new Budget(1000);
    }

    @Test
    public void budgetConstructorTest() {
        assertEquals(testBudget.getMonthlyBudget(), 1000.0);
    }

    @Test
    public void budgetMonthlyBudgetTest() {
        //testing setting and getting
        assertEquals(testBudget.getMonthlyBudget(), 1000.0); // inital consturctor val
        testBudget.setMonthlyBudget(4000.5);
        assertEquals(testBudget.getMonthlyBudget(), 4000.5);
        testBudget.setMonthlyBudget(244.23);
        assertEquals(testBudget.getMonthlyBudget(), 244.23);
        testBudget.setMonthlyBudget(1000.0);
        assertEquals(testBudget.getMonthlyBudget(), 1000.0);
        testBudget.setMonthlyBudget(1000.0);
        assertEquals(testBudget.getMonthlyBudget(),
                1000.0); //testing same val to ensure no error is coming through
    }

    @Test
    public void typeBudgetTest() {
        assertEquals(testBudget.getTypeBudget(Type.BILLS), 0.0); // will no keys so should default
        testBudget.setTypeBudget(Type.FOOD,200.25); // adding a key value pair
        testBudget.setTypeBudget(Type.TRANSPORTATION,40.13);
        assertEquals(testBudget.getTypeBudget(Type.BILLS), 0.0); // will not find key
        assertEquals(testBudget.getTypeBudget(Type.FOOD), 200.25); // will find key
        assertEquals(testBudget.getTypeBudget(Type.TRANSPORTATION), 40.13); // will find key
        testBudget.setTypeBudget(Type.HEALTH,500.004);






    }



}
