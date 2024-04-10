package persistence;

import model.Expense;
import model.ExpenseTracker;
import model.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
    private ExpenseTracker testTracker;
    private Expense ex1;
    private Expense ex2;

    private JsonWriter fileWriter;

    private final LocalDate testDate = LocalDate.of(2024,01,01);

    @BeforeEach
    public void runBefore(){
        testTracker = new ExpenseTracker(3000);
    }

    @Test
    public void testThrowException() {
        fileWriter = new JsonWriter("");
        assertThrows(FileNotFoundException.class, ()-> {
            fileWriter.checkFile();
        });
    }

    @Test
    public void testNoExceptionThrownEmpty() throws FileNotFoundException {
        fileWriter = new JsonWriter(
                ".\\data\\testSave.json");
        try{
            fileWriter.checkFile();
        }catch (FileNotFoundException e ){
            fail("Assertion should not be thrown");
        }
    }
    @Test
    public void testNoExceptionThrownWriteData() throws FileNotFoundException {
        testTracker.addExpense("Breaky", testDate, 30.2, Type.FOOD);
        testTracker.addExpense("rent", testDate, 1125.3, Type.BILLS);
        testTracker.setTypeBudget(Type.FOOD,400);
        fileWriter =
                new JsonWriter("./data/testWriteData.json");

        fileWriter.checkFile();
        fileWriter.trackerToJson(testTracker);
        fileWriter.closeFile();

    }
}

