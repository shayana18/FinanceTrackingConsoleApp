package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class ExpenseTracker implements Writable {
    private double income;

    private Budget budget;

    private List<Expense> expenses;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    private static EventLog eventLog = EventLog.getInstance();


    //Effect: Creates an expense tracker with a monthly budget initalized to 0 and an empty expenses list
    public ExpenseTracker(double income) {
        this.income = income;
        this.budget = new Budget(0);
        this.expenses = new ArrayList<>();
    }

    //Effect: gets user income
    public double getIncome() {
        return this.income;
    }

    //Requires: income > 0
    //Modifies: this
    //Effect: sets income to user provided income
    public String setIncome(double income) {
        this.income = income;
        eventLog.logEvent(new Event("You have set your income to $" + this.income));
        return "Your income has been added as $" + this.income + "\n";
    }

    //Effect: Checks if the user provided income is in range. If not return false
    public boolean incomeInRange(double income) {
        if (income <= 0) {
            return false;
        }
        return true;
    }

    //Modifies: this
    //Effect: creates an expense and adds it to our list of expenses
    public String addExpense(String name, LocalDate date, double amount, Type type) {
        Expense newExpense = new Expense(name, LocalDate.parse(date.format(formatter)),  amount, type);
        this.expenses.add(newExpense);
        eventLog.logEvent(new Event("You have added an expense with the name " + name + " and type: " + type));
        return "Your expense has been added: " + newExpense.makeExpenseString() + "\n"
                + "Your left over monthly budget is: $ " + (this.getMonthlyBudget() - amount) + "\n"
                + "You have $" + (this.getTypeBudget(type) - amount) + " left for " + type + "\n";
    }

    //Effect: checks if expense amount is > 0 if not return false
    public boolean validExpenseAmount(double amount) {
        if (amount <= 0) {
            return false;
        }
        return true;
    }

    //Effect: returns monthly budget
    public double getMonthlyBudget() {
        return this.budget.getMonthlyBudget();
    }

    //Requires: monthlyBudget > 0
    //Modifies: this
    //Effect: sets monthly budget to user provided monthlyBudget
    public String setMonthlyBudget(double monthlyBudget) {
        this.budget.setMonthlyBudget(monthlyBudget);
        eventLog.logEvent(new Event("You set your monthly budget to $" + this.budget.getMonthlyBudget()));
        return "Your monthly budget is $" + this.budget.getMonthlyBudget() + "\n";
    }

    //Effect: ensures monthlyBudget is greater than 0 but less than income
    public boolean monthlyBudgetInRange(double monthlyBudget) {
        return (monthlyBudget < getIncome() && monthlyBudget > 0);
    }

    //Effect: gets budget of user provided type
    public double getTypeBudget(Type type) {
        return this.budget.getTypeBudget(type);
    }

    //Modifies: this
    //Effect: sets expense type budget for specified type to specified income
    public String setTypeBudget(Type type, double typeBudget) {

        this.budget.setTypeBudget(type, typeBudget);
        eventLog.logEvent(new Event("You set your " + type + " budget to $ "
                + this.budget.getTypeBudget(type)));
        return "Your budget for " + type + " is $" + this.budget.getTypeBudget(type) + "\n";
    }


    //Effect: returns true if  user provided type budget is <= monthly budget and > 0. Else false.
    public boolean typeBudgetInRange(double typeBudget) {
        return (typeBudget <= this.budget.getMonthlyBudget() && typeBudget > 0);
    }

    //Effect: returns an ArrayList of all the user expenses
    public List<Expense> getExpenses() {
        eventLog.logEvent(new Event("You viewed your expenses"));
        return this.expenses;
    }

    //Effect: provided a sum of the cost of all the user expenses
    public double getSumOfExpenses() {
        double sum = 0;
        for (Expense expense : this.expenses) {
            sum += expense.getExpenseAmount();
        }
        return sum;
    }

    //Effect: provided a sum of the cost of all the user expenses for a given type of expense
    public double getSumOfTypeExpenses(Type type) {
        double sum = 0;
        for (Expense expense : this.expenses) {
            if (expense.getExpenseType() == type) {
                sum += expense.getExpenseAmount();
            }
        }
        return sum;
    }

    //Effect: returns a string of all the events that are logged
    public String printEventLog() {
        StringBuilder log = new StringBuilder();
        for (Event event : eventLog) {
            log.append(event.toString()).append("\n");
        }
        return log.toString();
    }

    //Effect: create JsonObjects and arrays out off all attributes of this class
    @Override
    public JSONObject toJson() {
        JSONObject jsonTracker = new JSONObject();
        JSONArray jsonExpenseList = new JSONArray();

        jsonTracker.put("income", this.income);
        jsonTracker.put("budget", this.budget.toJson());

        for (Expense expense : this.expenses) {
            jsonExpenseList.put(expense.toJson());
        }

        jsonTracker.put("expenseList", jsonExpenseList);
        return jsonTracker;
    }

}
