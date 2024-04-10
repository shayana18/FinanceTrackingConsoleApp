package model;

import org.json.JSONObject;
import org.stjs.javascript.JSON;
import persistence.Writable;

import java.util.HashMap;
import java.util.Map;

public class Budget implements Writable {
    private double monthlyBudget;
    private Map<Type, Double> typeBudget;

    // Effect: Creates a budget with a monthly budget and an empty hashmap for type budgets
    public Budget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
        this.typeBudget = new HashMap<>();
    }

    //setters and getters

    //Effect: Returns the monthly budget
    public double getMonthlyBudget() {
        return this.monthlyBudget;
    }

    //Requires: monthlyBudget > 0
    //Modifies: this
    //Effect: sets monthly budget to provided amount
    public String setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
        return "Your monthly budget is $" + this.monthlyBudget + "\n";
    }

    //Effect: Returns the budget of the specified type of expense
    public double getTypeBudget(Type type) {
        return this.typeBudget.getOrDefault(type,0.0);
    }

    //Requries: Type budget has not been added previously
    //Modifies: this
    //Effect: sets a budget for the specfied type of expense if one does not already exist.
    // If one does, throw an exception
    public void setTypeBudget(Type type, double typeBudget) {
        this.typeBudget.put(type,typeBudget);
    }

    //Effect: create JsonObjects and arrays out off all attributes of this class
    @Override
    public JSONObject toJson() {
        JSONObject jsonBudget = new JSONObject();
        jsonBudget.put("monthlyBudget", this.monthlyBudget);

        JSONObject jsonTypeBudget = new JSONObject();
        for (Map.Entry<Type,Double> entry : this.typeBudget.entrySet()) {
            jsonTypeBudget.put(entry.getKey().toString(), entry.getValue());
        }
        jsonBudget.put("typeBudgetMap", jsonTypeBudget);
        return jsonBudget;
    }

}
