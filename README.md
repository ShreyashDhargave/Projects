# Bank Management System

This is a simple banking application I built using Java and MySQL. It handles basic banking operations like creating accounts, depositing money, withdrawing, and transferring funds between accounts.

## Tech Stack
- Java (JDK 11)
- MySQL Database
- Maven for project management

## How to Run

1. **Database Setup**
   - Make sure you have MySQL installed and running.
   - Create the database by running the script in `database/schema.sql`.
   - Update `src/main/resources/database.properties` with your MySQL username and password.

2. **Build and Run**
   You can run the project using Maven:
   ```
   mvn clean compile
   mvn exec:java -Dexec.mainClass="com.bank.ui.BankManagementSystem"
   ```

## Features
- Register new customers
- Create accounts (Savings, Current)
- Deposit and Withdraw money
- Transfer money between accounts
- View transaction history

## Project Structure
- `src/main/java`: Contains all the Java source code.
- `database`: SQL scripts for setting up the database.
