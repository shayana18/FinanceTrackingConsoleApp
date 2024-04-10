# _Personal Expense Tracker_
## Project Description
This application will include an income, bills (rent, phone bill and utilities), food,
transportation, health and personal enjoyment categories. Users will be asked to input a monthly income and a budget
for each of the expense categories. Everytime the user has an expense they can add it to the app along with a category
that the expense belongs to. Users will be able to see an ongoing expense report showing how much of their monthly 
income has been used and how much of each budget has been used up. This application is usable by essentially any working
individual looking to monitor their monthly expense, I am personally interested in building this for my 210 project as 
I would like to build an investment dashboard application in the future and I see this as a good starting point for such
an app. 

## User Stories
As a user:
-  I want to, be able to add my income
- I want to, be able to add a monthly budget
- I want to be able to add a type of expense budget (ie. Bills, Health, Transportation etc)
- I want to be able to add an expense and its type
- I want to be able to see a list of all my expenses and what types they belong to
- I want to have the option to save the entire state of my application (expenses, budgets and income) 
- at the end of session in the Expense tracker App
- When running the app, I want to have the option to load in past state

## Phase 4 Task 2

Sample Event log:

Sun Apr 07 23:17:57 PDT 2024
You loaded in data in data
Sun Apr 07 23:17:57 PDT 2024
You have added an expense with the name Expense name and type: BILLS
Sun Apr 07 23:17:59 PDT 2024
You saved in data

## Phase 4 Task 3

If given more time I would likely improve my error handling as currently the error handling for incorrect input such as
a monthly budget greater than or equal to the income is handled by a loop which could be better optmized to be handled 
by exceptions. Additionally, I would improve my coupling by reducing the reliance on the GUI class to implement code 
functionality like the filtering for list of expenses. Lastly I would prefer to improve saving data to store my data in
a more secure place such as cloud based storage.
