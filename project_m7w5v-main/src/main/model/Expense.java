package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;
import persistence.Writable;

public class Expense implements Writable {

    private String expenseName;
    private LocalDate expenseDate;
    private double expenseAmount;
    private Type expenseType;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    // Effect: creates an expense with the provided date, amount and category. Sets it be tracked by default
    public Expense(String name,LocalDate date,double amount, Type type) {
        this.expenseName = name;
        this.expenseDate = LocalDate.parse(date.format(formatter));
        setExpenseAmount(amount); // wanted to have exception handling for requries
        this.expenseType = type;
    }

    //Getters and Setters

    //Effect: Gets name of expense
    public String getExpenseName() {
        return this.expenseName;
    }


    //Modifies: this
    //Effect: sets name of expense
    public void setExpenseName(String newName) {
        this.expenseName = newName;
    }

    //Effect: Gets date of expense
    public LocalDate getExpenseDate() {
        return this.expenseDate;
    }

    //Modifies: this
    //Effect: changes expense date to the provided date
    public void setExpenseDate(LocalDate newDate) {
        this.expenseDate = newDate;
    }

    //Effect: Gets amount of expense
    public double getExpenseAmount() {
        return this.expenseAmount;
    }

    //Modifies: this
    //Effect: changes expense amount to the provided amount
    public void setExpenseAmount(Double newAmount) {
        this.expenseAmount = newAmount;
    }


    //Effect: Gets category of expense
    public Type getExpenseType() {
        return this.expenseType;
    }

    //Modifies: this
    //Effect: changes expense type to the provided type
    public void setExpenseType(Type newType) {
        this.expenseType = newType;
    }

    public String makeExpenseString() {
        return "(" + "Date: " + expenseDate.toString() + ")" + " "
                + this.expenseName
                + " of type " + this.expenseType + " costing $" + Double.toString(expenseAmount) + "\n";
    }

    //Effect: create JsonObjects and arrays out off all attributes of this class
    @Override
    public JSONObject toJson() {
        JSONObject jsonExpense = new JSONObject();
        jsonExpense.put("expenseName", expenseName);
        jsonExpense.put("expenseDate", expenseDate.toString());
        jsonExpense.put("expenseAmount", expenseAmount);
        jsonExpense.put("expenseType", expenseType.toString());
        return jsonExpense;
    }

}