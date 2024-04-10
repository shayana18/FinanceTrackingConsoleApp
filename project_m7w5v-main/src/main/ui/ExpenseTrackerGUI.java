package ui;

import model.Expense;
import model.ExpenseTracker;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static model.Type.*;

public class ExpenseTrackerGUI extends JFrame implements ActionListener {
    private JLabel label;
    private JTextField field;
    private JPanel contentPane;

    private ExpenseTracker tracker;

    private static Font fontSetting = new Font("Serif", Font.PLAIN, 14);

    private static int spacing = 5;

    private static JComboBox dropDown;

    private JsonWriter dataWriter;
    private JsonReader dataLoader;

    private JTextField name;

    private static ImageIcon expenseImage =
            new ImageIcon("./210_img.jpg");

    private static ImageIcon loadImage =
            new ImageIcon("./data/loading.jpg");

    private static ImageIcon saveImage =
            new ImageIcon("./data/saving.png");

    private static String dataFileLocation = "./data/prevData.json";

    private  List<Expense> expenses;

    private String type;

    //Modifies: this
    //Effect:set background conditions of the screen
    public ExpenseTrackerGUI() {
        super("Personal Expense Tracker");
        tracker = new ExpenseTracker(0);
        dataLoader = new JsonReader(dataFileLocation);
        dataWriter = new JsonWriter(dataFileLocation);
        blankPage();
        initialContext();
    }

    public void initialContext() {
        JLabel introMessage = createLabel("Welcome to your personal expense tracker!");
        introMessage.setFont(new Font("Serif", Font.ITALIC, 20));
        getContentPane().add(introMessage);

        JLabel initialChoice = createLabel("Start by clicking one of the options below:");
        getContentPane().add(initialChoice);

        getContentPane().add(Box.createVerticalStrut(spacing * 2));
        JButton option1 = createButton("1. Enter your monthly income", "EnterIncome");

        getContentPane().add(Box.createVerticalStrut(spacing));
        JButton option2 = createButton("2. Load Previous Data", "Load Data");


        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //Effect: prints all the logged events
    private void runBeforeClose() {

        System.out.print(tracker.printEventLog());
    }

    //Modifies: this
    //Effect create blank page
    private void blankPage() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                runBeforeClose();
                System.exit(0);
            }
        });

        setPreferredSize(new Dimension(600, 400));

        getContentPane().removeAll();
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().setBackground(new Color(255, 201, 157));

        getContentPane().revalidate();
        getContentPane().repaint();
    }

    //Effect creates a page to ask the user for their income
    public void enterIncome() {
        JLabel enterIncome = createLabel("Enter an income greater than zero:");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        inputPanel.setBackground(new Color(255, 201, 157));

        JButton btn = createButton("Set Income", "Set Income");

        inputPanel.add(createField());
        inputPanel.add(btn);
        getContentPane().add(inputPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //Effect: state machine used to decide what to do when a button is pressed
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("EnterIncome")) {
            blankPage();
            enterIncome();
        } else if (e.getActionCommand().equals("Set Income")) {
            setIncome();
        } else if (e.getActionCommand().equals("Change Income")) {
            blankPage();
            enterIncome();
        } else if (e.getActionCommand().equals("enter monthly budget")) {
            blankPage();
            enterMonthlyBudget();
        } else if (e.getActionCommand().equals("set monthly budget")) {
            setMonthlyBudget();
        } else if (e.getActionCommand().equals("enter type budget")) {
            blankPage();
            enterTypeBudget();
        } else {
            try {
                moreActions(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    //Effect: extending state machine functionality
    public void moreActions(ActionEvent e) throws IOException {
        if (e.getActionCommand().equals("Set type")) {
            setTypeBudget();
        } else if (e.getActionCommand().equals("Add expense")) {
            blankPage();
            addExpense();
        } else if (e.getActionCommand().equals("Set expense")) {
            setExpense();
        } else if (e.getActionCommand().equals("View Expenses")) {
            blankPage();
            pickExpenseList();
        } else if (e.getActionCommand().equals("back")) {
            mainOptions();
        } else if (e.getActionCommand().equals("Load Data")) {
            loadInData();
        } else if (e.getActionCommand().equals("Save Data")) {
            saveData();
        } else if (e.getActionCommand().equals("type expense")) {
            viewExpenses();
        }
    }

    // Effect: creates a button with set formatting
    private JButton createButton(String buttonMessage, String buttonCommand) {
        JButton btn = new JButton(buttonMessage);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(btn);
        btn.addActionListener(this);
        btn.setActionCommand(buttonCommand);
        return btn;
    }

    // Effect: creates a label with set formatting
    private JLabel createLabel(String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(fontSetting);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(lbl);
        return lbl;
    }

    // Effect: creates a field with set formatting and a place holder value
    private JTextField createField(String fieldPlaceHolder) {
        field = new JTextField(fieldPlaceHolder);
        field.setPreferredSize(new Dimension(60,20));
        field.setMaximumSize(new Dimension(60,20));

        return field;
    }

    // Effect: creates a field with set formatting and no place holder
    private JTextField createField() {
        field = new JTextField();
        field.setPreferredSize(new Dimension(60,20));
        field.setMaximumSize(new Dimension(60,20));

        return field;
    }

    //Effect: checks if the provided income is in range, if so sets tracker income, if not then warn user
    private void setIncome() {
        String incomeInput = field.getText();
        try {
            double income = Double.parseDouble(incomeInput);
            if (tracker.incomeInRange(income)) {
                tracker.setIncome(income);
                mainOptions();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid income greater than zero.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                field.setText("");
            }
        } catch (NumberFormatException e) {
            // Handle case where input is not a valid double
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Effect: creates page to ask user for their monthly income
    private void enterMonthlyBudget() {
        JLabel getMonthlyBudget = createLabel("Enter a monthly budget less than your income");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        inputPanel.setBackground(new Color(255, 201, 157));

        JButton btn = createButton("Set Monthly Budget", "set monthly budget");

        inputPanel.add(createField());
        inputPanel.add(btn);
        getContentPane().add(inputPanel);
    }

    //Modifies: this
    //Effect: checks if the user income is in range, if not stay on page and continue asking from input
    // if yes then set monthly budget
    private void setMonthlyBudget() {
        String budgetInput = field.getText();
        try {
            double budget = Double.parseDouble(budgetInput);
            if (tracker.monthlyBudgetInRange(budget)) {
                tracker.setMonthlyBudget(budget);
                mainOptions();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid budget less than your income.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                field.setText("");
            }
        } catch (NumberFormatException e) {
            // Handle case where input is not a valid double
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Effect: sets up screen for entering type budget
    private void enterTypeBudget() {
        String[] typeEnum = {String.valueOf(BILLS), String.valueOf(FOOD), String.valueOf(TRANSPORTATION),
                String.valueOf(HEALTH),String.valueOf(PERSONAL_ENJOYMENT),  String.valueOf(VACATION)};

        JLabel typeBudgetMessage = createLabel("Enter the details for your type budget below");
        getContentPane().add(typeBudgetMessage);

        JPanel typePanel = new JPanel();
        typePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        typePanel.setBackground(new Color(255, 201, 157));

        typePanel.add(createField());

        dropDown = new JComboBox<>(typeEnum);
        dropDown.setPreferredSize(new Dimension(100,20));
        typePanel.add(dropDown);

        JButton typeButton = createButton("Set Type Budget", "Set type");
        typePanel.add(typeButton);

        getContentPane().add(typePanel);

    }

    //Modifies: this, tracker
    //Effect: sets type budget
    private void setTypeBudget() {
        String inputBudget = field.getText();
        String type = (String) dropDown.getSelectedItem();
        double typeBudget = Double.parseDouble(inputBudget);
        try {
            if (tracker.typeBudgetInRange(typeBudget)) {
                tracker.setTypeBudget(valueOf(type),typeBudget);
                mainOptions();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid budget less than your income.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                field.setText("");
                mainOptions();
            }
        } catch (NumberFormatException e) {
            // Handle case where input is not a valid double
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Effect: Sets up screen for adding an expense
    private void addExpense() {
        String[] typeEnum = {String.valueOf(BILLS), String.valueOf(FOOD), String.valueOf(TRANSPORTATION),
                String.valueOf(HEALTH),String.valueOf(PERSONAL_ENJOYMENT),  String.valueOf(VACATION)};

        JLabel typeBudgetMessage = createLabel("Enter the details for your expense below");
        getContentPane().add(typeBudgetMessage);

        JPanel typePanel = new JPanel();
        typePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        typePanel.setBackground(new Color(255, 201, 157));

        name = new JTextField("Expense name");
        name.setPreferredSize(new Dimension(80,20));
        typePanel.add(name);

        typePanel.add(createField());

        dropDown = new JComboBox<>(typeEnum);
        dropDown.setPreferredSize(new Dimension(100,20));
        typePanel.add(dropDown);

        JButton typeButton = createButton("Set Expense", "Set expense");
        typePanel.add(typeButton);

        getContentPane().add(typePanel);

    }

    //Modifies: this, tracker
    //Effect: adds expense to tracker
    private void setExpense() {
        String expenseName = name.getText();
        String inputExpenseAmount = field.getText();
        String type = (String) dropDown.getSelectedItem();
        double expenseAmount = Double.parseDouble(inputExpenseAmount);
        try {
            if (tracker.validExpenseAmount(expenseAmount)) {
                tracker.addExpense(expenseName,LocalDate.now(),expenseAmount,valueOf(type));
                mainOptions();
                popUpImage(expenseImage, "Your expense has been added!!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid budget less than your income.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                field.setText("");
                mainOptions();
            }
        } catch (NumberFormatException e) {
            // Handle case where input is not a valid double
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Effect: abstracts the process of adding a button to a panel
    private void addtoContentPane(JButton button) {
        getContentPane().add(Box.createVerticalStrut(spacing * 2));
        getContentPane().add(button);
    }

    //Effect: creates a pop up image
    private void popUpImage(ImageIcon img, String title) {
        JLabel label = new JLabel(img);
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.add(label);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    //Effect: Allows user to pick the list of expenses they want to see, all or types
    private void pickExpenseList() {
        JPanel expensePanel = new JPanel();
        expensePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        expensePanel.setBackground(new Color(255, 201, 157));

        String[] typeEnum = {"ALL", String.valueOf(BILLS), String.valueOf(FOOD), String.valueOf(TRANSPORTATION),
                String.valueOf(HEALTH),String.valueOf(PERSONAL_ENJOYMENT),  String.valueOf(VACATION)};

        dropDown = new JComboBox<>(typeEnum);
        dropDown.setPreferredSize(new Dimension(150,20));
        expensePanel.add(dropDown);

        JButton expenseButtn = createButton("Get Expense List", "type expense");
        expensePanel.add(expenseButtn);
        getContentPane().add(expensePanel);

        JButton back = createButton("Back", "back");
        getContentPane().add(back);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    //Effects: displays user specified expense list
    @SuppressWarnings("methodlength")
    private void viewExpenses() {
        type = (String) dropDown.getSelectedItem();
        expenses = new ArrayList<>();
        if (type.equals("ALL")) {
            expenses = tracker.getExpenses();
        } else {
            for (Expense expense: tracker.getExpenses()) {
                if (expense.getExpenseType().equals(valueOf(type))) {
                    expenses.add(expense);
                }
            }
        }
        String[] coloumnTitles = {"Date", "Expense Type", "Expense name", "Amount Spend"};

        Object[][] data = new Object[expenses.size()][4];

        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            data[i][0] = expense.getExpenseDate().toString();
            data[i][1] = expense.getExpenseType().toString();
            data[i][2] = expense.getExpenseName();
            data[i][3] = String.valueOf(expense.getExpenseAmount());
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, coloumnTitles);
        JTable expenseList = new JTable(tableModel);
        expenseList.setPreferredSize(new Dimension(300,400));
        expenseList.setBackground(new Color(255, 201, 157));
        JScrollPane scrollPane = new JScrollPane(expenseList);
        getContentPane().add(scrollPane);
        getContentPane().revalidate();
        getContentPane().repaint();

    }

    //Modifies: Tracker
    // Effect: Loads in past data
    private void loadInData() throws IOException {
        this.tracker = dataLoader.read();
        mainOptions();
        popUpImage(loadImage, "Your data has been loaded!");
    }

    //Effect: saves data
    private void saveData() throws FileNotFoundException {
        dataWriter.checkFile();
        dataWriter.trackerToJson(tracker);
        dataWriter.closeFile();
        mainOptions();
        popUpImage(saveImage,"Your data has been saved");

    }


    //Effect: creates page for main options with buttons to redirect to the option paths
    private void mainOptions() {
        blankPage();

        JLabel mainChoices = createLabel("Please select your next action from the list below:");
        getContentPane().add(mainChoices);

        addtoContentPane(createButton("1. Set/Change Income", "Change Income"));

        addtoContentPane(createButton(
                "2. Set a monthly budget (enter a valid income before adding a budget)",
                "enter monthly budget"));

        addtoContentPane(createButton(
                "3. Set a type monthly budget (enter a valid income before adding a budget)",
                "enter type budget"));

        addtoContentPane(createButton(
                "4. Add an expense", "Add expense"));

        addtoContentPane(createButton(
                "5. View tracked expense", "View Expenses"));

        addtoContentPane(createButton(
                "6. Save data", "Save Data"));
        addtoContentPane(createButton(
                "7. Load data", "Load Data"));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


}
