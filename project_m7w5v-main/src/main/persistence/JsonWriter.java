package persistence;

import model.Event;
import model.EventLog;
import org.json.*;
import org.json.JSONObject;
import model.ExpenseTracker;
import org.stjs.javascript.JSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JsonWriter {
    private String fileLoc;
    private ExpenseTracker expenseTracker;
    private PrintWriter writer;
    private static final int indentSpacing = 4;

    private static EventLog eventLog = EventLog.getInstance();

    // initialize a Writer object with a file location to write data to
    public JsonWriter(String fileLocation) {
        this.fileLoc = fileLocation;
    }

    //Effect: creates a writer instances with a file location, throws exception if file location is not found
    public void checkFile() throws FileNotFoundException {
        writer = new PrintWriter(fileLoc);
    }

    //Effect: initializes a json object and saves it to a file as a string
    public void trackerToJson(ExpenseTracker tracker) {
        JSONObject jsonTracker = tracker.toJson();
        saveToFile(jsonTracker.toString(indentSpacing));
    }

    //Effect: saves data to file by printing it as a string
    public void saveToFile(String dataToSave) {

        eventLog.logEvent(new Event("You saved in data"));
        writer.print(dataToSave);
    }

    //Effect: Closes file
    public void closeFile() {
        writer.close();
    }
}
