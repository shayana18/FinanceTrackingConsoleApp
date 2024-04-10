package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTrackerTest {

    private static LocalDate date1 = LocalDate.parse("2023-10-01");
    private static LocalDate date2 = LocalDate.parse("2024-10-11");
    private ExpenseTracker testTracker;
    private EventLog eventLog = EventLog.getInstance();

    @BeforeEach
    public void runBefore() {
        testTracker = new ExpenseTracker(0);
    }

    @Test
    public void expenseTrackerConstructorTest() {
        assertEquals(testTracker.getSumOfExpenses(), 0);
        assertTrue(testTracker.getExpenses().isEmpty());

    }

    @Test
    public void IncomeTest() {
        testTracker.setIncome(0); // setting it to 0
        assertEquals(testTracker.getIncome(),0);
        testTracker.setIncome(100);
        assertEquals(testTracker.getIncome(),100);
        testTracker.setIncome(10000); // setting it to 0
        assertEquals(testTracker.getIncome(),10000);
        testTracker.setIncome(2000.002);
        assertEquals(testTracker.getIncome(),2000.002);
        testTracker.setIncome(-100); // negative values should go through
        assertEquals(testTracker.getIncome(),-100);
    }

    @Test
    public void inRangeTest() {
        assertFalse(testTracker.incomeInRange(0));
        assertFalse(testTracker.incomeInRange(-1));
        assertFalse(testTracker.incomeInRange(-2));
        assertFalse(testTracker.incomeInRange(-1000));
        assertFalse(testTracker.incomeInRange(-14231.12345));
        assertTrue(testTracker.incomeInRange(1));
        assertTrue(testTracker.incomeInRange(2));
        assertTrue(testTracker.incomeInRange(1000));
        assertTrue(testTracker.incomeInRange(1400.0123));
    }

    @Test
    public void addSumExpenseTest() {

        //add and sum is tested here
        testTracker.addExpense("rent", LocalDate.now(), 1150.023, Type.BILLS);
        assertEquals(testTracker.getExpenses().size(), 1);
        assertEquals(testTracker.getSumOfExpenses(), 1150.023);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.BILLS), 1150.023);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.HEALTH), 0.0); // other types still sum 0;

        testTracker.addExpense("evo", LocalDate.now(), 40, Type.TRANSPORTATION);
        testTracker.addExpense("grocceries", LocalDate.now(), 201.10, Type.FOOD);
        assertEquals(testTracker.getExpenses().size(), 3);
        assertEquals(testTracker.getSumOfExpenses(), 1150.023 + 40 + 201.10);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.BILLS), 1150.023);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.HEALTH), 0.0);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.FOOD), 201.10);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.TRANSPORTATION), 40);

        //Adding two of one type
        testTracker.addExpense("UBC Increased Rent!", LocalDate.now(), 50.01, Type.BILLS);
        assertEquals(testTracker.getExpenses().size(), 4);
        assertEquals(testTracker.getSumOfExpenses(), 1150.023 + 40 + 201.10 + 50.01);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.BILLS), 1150.023 + 50.01);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.HEALTH), 0.0);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.FOOD), 201.10);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.TRANSPORTATION), 40);

        //Adding atleast 1 of each type
        testTracker.addExpense("we went to the bar!", LocalDate.now(), 30, Type.PERSONAL_ENJOYMENT);
        testTracker.addExpense("Mexico Trip!!!", LocalDate.now(), 1500.2, Type.VACATION);
        testTracker.addExpense("Advil", LocalDate.now(), 5.07, Type.HEALTH);
        assertEquals(testTracker.getExpenses().size(), 7);
        assertEquals(testTracker.getSumOfExpenses(), 1150.023 + 40 + 201.10 + 50.01 + 30 + 1500.2 + 5.07);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.BILLS), 1150.023 + 50.01);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.HEALTH), 5.07);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.FOOD), 201.10);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.TRANSPORTATION), 40);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.PERSONAL_ENJOYMENT), 30);
        assertEquals(testTracker.getSumOfTypeExpenses(Type.VACATION), 1500.2);
    }

    @Test
    public void validExpenseTest() {
        assertFalse(testTracker.validExpenseAmount(0));
        assertFalse(testTracker.validExpenseAmount(-1));
        assertFalse(testTracker.validExpenseAmount(-2));
        assertFalse(testTracker.validExpenseAmount(-100));
        assertFalse(testTracker.validExpenseAmount(-3454.678));

        assertTrue(testTracker.validExpenseAmount(1));
        assertTrue(testTracker.validExpenseAmount(2));
        assertTrue(testTracker.validExpenseAmount(100));
        assertTrue(testTracker.validExpenseAmount(3000));
        assertTrue(testTracker.validExpenseAmount(34005.123456));
    }

    @Test
    public void MonthlyBudgetTest() {
        //testing both setters getters

        testTracker.setMonthlyBudget(200);
        assertEquals(testTracker.getMonthlyBudget(), 200);

        testTracker.setMonthlyBudget(0);
        assertEquals(testTracker.getMonthlyBudget(), 0);

        testTracker.setMonthlyBudget(-20120.222222);
        assertEquals(testTracker.getMonthlyBudget(), -20120.222222);

        testTracker.setMonthlyBudget(12345.321223);
        assertEquals(testTracker.getMonthlyBudget(), 12345.321223);

        testTracker.setMonthlyBudget(1);
        assertEquals(testTracker.getMonthlyBudget(), 1);
    }

    @Test
    public void rangeMonthlyBudget() {
        testTracker.setIncome(1);
        assertFalse(testTracker.monthlyBudgetInRange(0)); // is less than income but not > 0

        testTracker.setIncome(1000);
        assertFalse(testTracker.monthlyBudgetInRange(-2));

        testTracker.setIncome(2000);
        assertFalse(testTracker.monthlyBudgetInRange(4000)); // > 0 but more than income

        testTracker.setIncome(2000);
        assertTrue(testTracker.monthlyBudgetInRange(1000));

        testTracker.setIncome(2000);
        assertTrue(testTracker.monthlyBudgetInRange(1999.999999));

        testTracker.setIncome(2000);
        assertFalse(testTracker.monthlyBudgetInRange(2000.000001));

    }

    @Test
    public void setTypeBudgetTest() {
        //testing setters, getters and range
        testTracker.setMonthlyBudget(200);
        assertTrue(testTracker.typeBudgetInRange(200)); // equal to monthly budget
        testTracker.setTypeBudget(Type.BILLS, 200);
        assertEquals(testTracker.getTypeBudget(Type.BILLS), 200);

        testTracker.setMonthlyBudget(100);
        assertFalse(testTracker.typeBudgetInRange(200)); // greater than monthly budget but still set
        testTracker.setTypeBudget(Type.HEALTH, 200);
        assertEquals(testTracker.getTypeBudget(Type.HEALTH), 200);

        testTracker.setMonthlyBudget(12345);
        assertFalse(testTracker.typeBudgetInRange(0)); // less than monthly budget but 0 still set
        testTracker.setTypeBudget(Type.VACATION, 0);
        assertEquals(testTracker.getTypeBudget(Type.VACATION), 0);

        testTracker.setMonthlyBudget(1);
        assertFalse(testTracker.typeBudgetInRange(-1)); // less than monthly budget but less than 0 still set
        testTracker.setTypeBudget(Type.FOOD, -1);
        assertEquals(testTracker.getTypeBudget(Type.FOOD), -1);

        testTracker.setMonthlyBudget(12345);
        assertTrue(testTracker.typeBudgetInRange(1200)); // less than monthly budget and greater than zero
        testTracker.setTypeBudget(Type.PERSONAL_ENJOYMENT, 1200);
        assertEquals(testTracker.getTypeBudget(Type.PERSONAL_ENJOYMENT), 1200);
    }

    @Test
    public void getExpenseTest() {
        assertTrue(testTracker.getExpenses().isEmpty()); // empty initially
        testTracker.addExpense("rent", LocalDate.now(), 1150.023, Type.BILLS);
        testTracker.addExpense("evo", LocalDate.parse("2023-01-01"), 60.12, Type.TRANSPORTATION);
        testTracker.addExpense("advil", LocalDate.parse("2022-03-12"), 12, Type.HEALTH);
        testTracker.addExpense("Rec Room",
                LocalDate.parse("2024-03-22"), 40, Type.PERSONAL_ENJOYMENT);
        assertEquals(4, testTracker.getExpenses().size());
        testTracker.addExpense("rent", LocalDate.now(), 1150.023, Type.BILLS); // add identical expenses
        assertEquals(5, testTracker.getExpenses().size());
    }

    @Test
    public void getSumTests() {
        //testing both type sum and expense sum here

        assertEquals(0 ,testTracker.getSumOfExpenses());
        // all types should be 0
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.BILLS));
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.FOOD));
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.TRANSPORTATION));
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.HEALTH));
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.PERSONAL_ENJOYMENT));
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.VACATION));

        testTracker.addExpense("rent", LocalDate.now(), 1150.023, Type.BILLS);
        testTracker.addExpense("evo", LocalDate.parse("2023-01-01"), 0, Type.TRANSPORTATION);
        testTracker.addExpense("advil", LocalDate.parse("2022-03-12"), 12, Type.HEALTH);

        assertEquals(1150.023 + 12 ,testTracker.getSumOfExpenses());
        assertEquals(1150.023, testTracker.getSumOfTypeExpenses(Type.BILLS));
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.FOOD));
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.TRANSPORTATION)); // Recognizes the 0 amount
        assertEquals(12, testTracker.getSumOfTypeExpenses(Type.HEALTH));
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.PERSONAL_ENJOYMENT));
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.VACATION));

        testTracker.addExpense("Rec Room",
                LocalDate.parse("2024-03-22"), -5, Type.PERSONAL_ENJOYMENT); // testing negative amounts
        testTracker.addExpense("Mexico Trip!!!", LocalDate.now(), 1500.2, Type.VACATION);

        assertEquals(1150.023 + 12 -5 + 1500.2 ,testTracker.getSumOfExpenses());
        assertEquals(1150.023, testTracker.getSumOfTypeExpenses(Type.BILLS));
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.FOOD));
        assertEquals(0, testTracker.getSumOfTypeExpenses(Type.TRANSPORTATION)); // Recognizes the 0 amount
        assertEquals(12, testTracker.getSumOfTypeExpenses(Type.HEALTH));
        assertEquals(-5, testTracker.getSumOfTypeExpenses(Type.PERSONAL_ENJOYMENT));
        assertEquals(1500.2, testTracker.getSumOfTypeExpenses(Type.VACATION));

    }

    @Test
    public void printLogTest() {
        testTracker.setIncome(200);
        testTracker.setMonthlyBudget(100);
        testTracker.addExpense("rent", date1, 1150.023, Type.BILLS);
        testTracker.addExpense("evo", date2, 60.12, Type.TRANSPORTATION);
        testTracker.getExpenses(); // This will add a "You viewed your expenses" event

        // Assuming eventLog is a field on testTracker that we can access. If not, this needs to be adjusted.
        StringBuilder expectedOutcome = new StringBuilder();
        for (Event event : eventLog) { // If getEventLog() is the correct way to access the events
            expectedOutcome.append(event.toString()).append("\n");
        }

        assertEquals(expectedOutcome.toString(), testTracker.printEventLog());
        assertNotEquals(expectedOutcome, testTracker.printEventLog());
    }


}
