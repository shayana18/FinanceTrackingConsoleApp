package ui;

import model.*;
import model.Type;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

//Expense tracking application UI
public class ExpenseTrackerUI {
    private Scanner scanner;
    private int nextStep;
    private boolean exitStatus;
    private ExpenseTracker tracker;
    private JsonWriter dataSaver;
    private JsonReader dataLoader;

    // Constructs a UI instance with a new scanner instance for user input and false exitStatus
    public ExpenseTrackerUI() throws IOException {
        String prevData = "./data/prevData.json";
        scanner = new Scanner(System.in);
        exitStatus = false;
        dataSaver = new JsonWriter(prevData);
        dataLoader = new JsonReader(prevData);
        runExpenseTracker();
    }

    // Runs Expense Tracking App
    private void runExpenseTracker() throws IOException {

        System.out.println("Welcome to your Personal Expense Tracker!");
        System.out.println("Select one of options below to begin your setup!");
        initialOptions();
        tracker = new ExpenseTracker(0);
        int nextStep = scanner.nextInt();
        initialStateMachine(scanner, nextStep);
        while (!exitStatus) {
            stateMachine(tracker,scanner);
        }
    }

    //Modifies: this
    //Effect: Control state Machine for initial steps initialOptions()
    private void initialStateMachine(Scanner scanner, int nextStep) throws IOException {
        switch (nextStep) {
            case 1:
                System.out.println(tracker.setIncome(askForIncome(tracker,scanner)));
                break;
            case 2:
                System.out.println("Loading previous data!");
                this.tracker = dataLoader.read();
                break;

        }
    }

    //Modifies: this
    //Effect: is a state machine that chooses user path based on input
    private void stateMachine(ExpenseTracker tracker, Scanner scanner) throws IOException {
        printOptions();
        nextStep = scanner.nextInt();
        switch (nextStep) {
            case 1:
                System.out.println(tracker.setIncome(askForIncome(tracker,scanner)));
                break;
            case 2:
                double budget = askMonthlyBudget(tracker,scanner);
                System.out.println(tracker.setMonthlyBudget(budget));
                break;
            case 3:
                setTypeBudget(tracker, scanner);
                break;
            case 4:
                addNewExpense(tracker, scanner);
                break;
            case 5:
                printExpenseList();
                break;
            case 6:
                stateMachineContinued(tracker,scanner);
        }
    }

    //Modifies: this
    //Effect: presents additional options to user (created to meet checkstyle)
    private void stateMachineContinued(ExpenseTracker tracker, Scanner scanner) throws IOException {
        printMoreOptions();
        nextStep = scanner.nextInt();
        switch (nextStep) {
            case 1:
                break;
            case 2:
                System.out.println("Saving your data now!");
                dataSaver.checkFile();
                dataSaver.trackerToJson(tracker);
                dataSaver.closeFile();
                break;
            case 3:
                System.out.println("Loading previous data!");
                this.tracker = dataLoader.read();
                break;
            case 4:
                System.out.println("See you next time!");
                this.exitStatus = true;
        }

    }

    //Effect:Provides a list of initial options
    private static void initialOptions() {
        System.out.println("1. SetIncome");
        System.out.println("2. Load previous data");
    }

    //Effect: prints out all the options available for the user
    private static void printOptions() {
        System.out.println("Please select your next action from the list below:");
        System.out.println("1. Set/Change Income");
        System.out.println("2. Set a monthly budget (enter a valid income before adding a budget)");
        System.out.println("3. Set a type monthly budget (enter a valid income before adding a budget)");
        System.out.println("4. Add an expense");
        System.out.println("5. View tracked expense");
        System.out.println("6. More options");
    }

    //Effect: extending stateMachine functionality
    private static void printMoreOptions() {
        System.out.println("1. See previous options");
        System.out.println("2. Save data");
        System.out.println("3. Load previous data");
        System.out.println("4. Exit app");
    }

    //Effect: Prompts user for income to be added into their budget tracker
    private static double askForIncome(ExpenseTracker tracker, Scanner scanner) {
        System.out.println("Please enter a monthly income greater than zero:");
        double income = scanner.nextDouble();
        while (!tracker.incomeInRange(income)) {
            System.out.println("Please enter a monthly income greater than zero:");
            income = scanner.nextDouble();
        }
        return income;
    }

    //Effect: Prompts user for a monthly budget to be added into their budget tracker
    private static double askMonthlyBudget(ExpenseTracker tracker,Scanner scanner) {
        System.out.println("Enter a budget less than your income:");
        double monthlyBudget = scanner.nextDouble();
        while (!tracker.monthlyBudgetInRange(monthlyBudget)) {
            System.out.println("Enter a budget less than your income:");
            monthlyBudget = scanner.nextDouble();
        }
        return monthlyBudget;
    }

    //Effect: Prompts user for an expense type budget to be added into their budget tracker
    private static void setTypeBudget(ExpenseTracker tracker, Scanner scanner) {
        System.out.println("Select the category you want to set a budget for from the options below:");
        for (Type type : Type.values()) {
            System.out.println((type.ordinal() + 1)  + "." + type);
        }

        int selectedTypeIndex = scanner.nextInt() - 1;
        Type type = Type.values()[selectedTypeIndex];

        System.out.println("Enter a budget for " + type);
        double typeBudget = scanner.nextDouble();
        while (!tracker.typeBudgetInRange(typeBudget)) {
            System.out.println("Enter a budget less than your monthly budget for " + type);
            typeBudget = scanner.nextDouble();
        }

        tracker.setTypeBudget(type, typeBudget);
    }

    //Modifies: Tracker
    //Effects: Adds an expense to the user expense list
    private static void addNewExpense(ExpenseTracker tracker,Scanner scanner) {
        scanner.nextLine(); // clearing scanner buffer

        System.out.println("Enter the expense name:");
        String name = scanner.nextLine();
        System.out.println("Enter the expense amount:");
        double amount = scanner.nextDouble();

        while (!tracker.validExpenseAmount(amount)) {
            System.out.println("Enter an expense amount greater than 0:");
            amount = scanner.nextDouble();
        }
        System.out.println("Select the category you want to add an expense for from the options below:");
        for (Type type : Type.values()) {
            System.out.println((type.ordinal() + 1)  + "." + type);
        }

        int selectedTypeIndex = scanner.nextInt() - 1;
        Type type = Type.values()[selectedTypeIndex];

        System.out.println(tracker.addExpense(name, LocalDate.now(), amount, type));
    }

    //Effect: prints user expense list
    public void printExpenseList() {
        System.out.println("Please find your expenses below:");
        for (Expense expense : tracker.getExpenses()) {
            System.out.println(expense.makeExpenseString());
        }
    }

}
