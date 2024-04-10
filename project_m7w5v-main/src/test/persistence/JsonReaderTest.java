package persistence;

import model.ExpenseTracker;
import model.Type;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
             ExpenseTracker recieveTracker = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    void testReaderFileNoException() {
        JsonReader reader = new JsonReader("./data/testWriteData.json");
        try {
            ExpenseTracker recieveTracker = reader.read();
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderEnsureData() throws IOException {
        JsonReader reader = new JsonReader("./data/testWriteData.json");

        ExpenseTracker recieveTracker = reader.read();
        assertEquals(3000, recieveTracker.getIncome());
        assertEquals(2, recieveTracker.getExpenses().size());
        assertEquals(0, recieveTracker.getMonthlyBudget());
        assertEquals(400, recieveTracker.getTypeBudget(Type.FOOD));
        assertEquals(0, recieveTracker.getTypeBudget(Type.BILLS));
    }
}
