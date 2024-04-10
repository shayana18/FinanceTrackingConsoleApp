package persistence;

import model.*;
import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class JsonReader {
    private String fileLoc;

    // sets provided file location as location to read from
    public JsonReader(String source) {
        fileLoc = source;
    }

    //Effect: reads from file and stores data as  a json object. Returns parsed file infomration
    public ExpenseTracker read() throws IOException {
        String jsonData = readFile(fileLoc);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseExpenseTracker(jsonObject);
    }

    //reads a file and formats its string to follow StandardCharsets.UTF_8
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    //Modifies Budget.monthlyBudget, Budget.typeBudget, ExpenseTracker.income, ExpenseTracker.budget
    // ExpenseTracker.expenses,
    private ExpenseTracker parseExpenseTracker(JSONObject jsonInfo) {

        Double income = jsonInfo.getDouble("income");
        ExpenseTracker prevTracker = new ExpenseTracker(income);

        JSONArray jsonExpenseList = jsonInfo.getJSONArray("expenseList");
        for (int i = 0; i < jsonExpenseList.length(); i++) {
            JSONObject jsonExpenseObject = jsonExpenseList.getJSONObject(i);
            String name = jsonExpenseObject.getString("expenseName");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(jsonExpenseObject.getString("expenseDate"), formatter);
            double amount = jsonExpenseObject.getDouble("expenseAmount");
            Type type = Type.valueOf(jsonExpenseObject.getString("expenseType"));
            prevTracker.addExpense(name,date,amount,type);
        }

        JSONObject jsonBudget = jsonInfo.getJSONObject("budget");
        double monthlyBudget = jsonBudget.getDouble("monthlyBudget");

        JSONObject jsonTypeBudget = jsonBudget.getJSONObject("typeBudgetMap");
        for (String key : jsonTypeBudget.keySet()) {
            Type type = Type.valueOf(key);
            double typeBudget = jsonTypeBudget.getDouble(key);
            prevTracker.setTypeBudget(type,typeBudget);
        }

        return prevTracker;
    }
}
