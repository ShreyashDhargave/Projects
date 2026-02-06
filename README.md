# Bank Management System

A comprehensive banking application built with Java (J2SE) and MySQL, designed to demonstrate core banking operations including customer onboarding, account management, transactions, and secure data handling.

## 📋 Project Overview

**Platform:** J2SE (Java Standard Edition)  
**Duration:** 2 Months  
**Database:** MySQL with JDBC

This project implements a complete banking system with the following features:
- Customer onboarding and management
- Account creation and management
- Deposit operations
- Withdrawal operations
- Fund transfers between accounts
- Transaction history and logging
- Secure database operations with transaction management

## 🏗️ Architecture

The project follows a **layered architecture** pattern:

```
┌─────────────────────────────────┐
│      Presentation Layer         │
│   (BankManagementSystem.java)   │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│       Service Layer             │
│  (Business Logic & Validation)  │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│        DAO Layer                │
│   (Data Access Operations)      │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│      Database Layer             │
│         (MySQL)                 │
└─────────────────────────────────┘
```

### Key Components

1. **Model Layer** (`com.bank.model`)
   - `Customer.java` - Customer entity
   - `Account.java` - Account entity
   - `Transaction.java` - Transaction entity

2. **DAO Layer** (`com.bank.dao`)
   - `CustomerDAO.java` - Customer CRUD operations
   - `AccountDAO.java` - Account CRUD operations
   - `TransactionDAO.java` - Transaction CRUD operations

3. **Service Layer** (`com.bank.service`)
   - `CustomerService.java` - Customer business logic
   - `AccountService.java` - Account business logic
   - `TransactionService.java` - Transaction business logic

4. **Exception Handling** (`com.bank.exception`)
   - Custom exceptions for better error handling
   - `BankException.java` - Base exception class
   - `AccountNotFoundException.java`
   - `CustomerNotFoundException.java`
   - `InsufficientBalanceException.java`

5. **Utilities** (`com.bank.util`)
   - `DatabaseConnection.java` - Database connection management

6. **Configuration** (`com.bank.config`)
   - `DatabaseConfig.java` - Database configuration management

## 🚀 Prerequisites

Before running this project, ensure you have:

- **Java Development Kit (JDK)** 11 or higher
- **Apache Maven** 3.6+ (for dependency management)
- **MySQL Server** 8.0+ (or compatible version)
- **MySQL Connector/J** (automatically downloaded via Maven)

## 📦 Installation & Setup

### Step 1: Clone/Download the Project

```bash
cd BankManagement
```

### Step 2: Database Setup

1. **Start MySQL Server**
   ```bash
   # Windows (if MySQL is in PATH)
   mysql -u root -p
   
   # Or start MySQL service from Services
   ```

2. **Create Database and Tables**
   ```bash
   mysql -u root -p < database/schema.sql
   ```
   
   Or manually execute the SQL script:
   ```sql
   source database/schema.sql;
   ```

3. **Configure Database Connection**
   
   Edit `src/main/resources/database.properties`:
   ```properties
   db.url=jdbc:mysql://localhost:3306/bank_management?useSSL=false&serverTimezone=UTC
   db.username=your_username
   db.password=your_password
   db.driver=com.mysql.cj.jdbc.Driver
   ```

### Step 3: Build the Project

```bash
# Compile and download dependencies
mvn clean compile

# Or create executable JAR
mvn clean package
```

### Step 4: Run the Application

**Option 1: Using Maven**
```bash
mvn exec:java -Dexec.mainClass="com.bank.ui.BankManagementSystem"
```

**Option 2: Using Java directly**
```bash
# First, compile
mvn compile

# Then run
java -cp "target/classes:target/dependency/*" com.bank.ui.BankManagementSystem
```

**Option 3: Using JAR file**
```bash
# After packaging
java -jar target/bank-management-system-1.0.0.jar
```

## 💻 Usage Guide

### Main Menu Options

1. **Register New Customer**
   - Create a new customer profile
   - Required: Name, Email, Phone, Address, Date of Birth

2. **Create New Account**
   - Open a new account for existing customer
   - Account types: SAVINGS, CURRENT, FIXED_DEPOSIT

3. **Deposit Money**
   - Add funds to an account
   - Requires: Account number, Amount, Description (optional)

4. **Withdraw Money**
   - Withdraw funds from an account
   - Validates sufficient balance
   - Requires: Account number, Amount, Description (optional)

5. **Transfer Money**
   - Transfer funds between two accounts
   - Creates transaction records for both accounts
   - Requires: Source account, Destination account, Amount

6. **View Account Balance**
   - Display account details and current balance

7. **View Transaction History**
   - View all transactions for an account
   - Shows transaction type, amount, balance after, date

8. **View Customer Details**
   - Display complete customer information

9. **View All Customers**
   - List all registered customers

10. **View Customer Accounts**
    - Display all accounts for a specific customer

## 🔒 Security Features

- **Prepared Statements**: All database queries use prepared statements to prevent SQL injection
- **Transaction Management**: ACID properties maintained for data consistency
- **Input Validation**: Business logic validates all inputs
- **Exception Handling**: Comprehensive error handling with custom exceptions
- **Connection Security**: Database credentials stored in configuration file (not hardcoded)

## 📊 Database Schema

### Tables

1. **customers**
   - `customer_id` (Primary Key)
   - `first_name`, `last_name`
   - `email` (Unique)
   - `phone`, `address`
   - `date_of_birth`
   - `created_at`, `updated_at`

2. **accounts**
   - `account_id` (Primary Key)
   - `customer_id` (Foreign Key)
   - `account_number` (Unique)
   - `account_type` (ENUM: SAVINGS, CURRENT, FIXED_DEPOSIT)
   - `balance` (with CHECK constraint: balance >= 0)
   - `status` (ENUM: ACTIVE, INACTIVE, CLOSED)
   - `created_at`, `updated_at`

3. **transactions**
   - `transaction_id` (Primary Key)
   - `account_id` (Foreign Key)
   - `transaction_type` (ENUM: DEPOSIT, WITHDRAWAL, TRANSFER_OUT, TRANSFER_IN)
   - `amount` (with CHECK constraint: amount > 0)
   - `balance_after`
   - `description`
   - `related_account_id` (Foreign Key, nullable)
   - `transaction_date`

## 🧪 Testing

Sample test data is included in `database/schema.sql`. You can use:
- Customer IDs: 1, 2
- Account Numbers: ACC001, ACC002

## 📝 Project Structure

```
BankManagement/
├── database/
│   └── schema.sql                 # Database schema and sample data
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── bank/
│       │           ├── config/     # Configuration classes
│       │           ├── dao/       # Data Access Objects
│       │           ├── exception/ # Custom exceptions
│       │           ├── model/     # Entity classes
│       │           ├── service/  # Business logic layer
│       │           ├── ui/        # User interface
│       │           └── util/      # Utility classes
│       └── resources/
│           └── database.properties # Database configuration
├── pom.xml                        # Maven configuration
├── .gitignore                    # Git ignore rules
└── README.md                     # This file
```

## 🎯 Key Features Demonstrated

✅ **Modular Code Architecture**
- Separation of concerns (DAO, Service, UI layers)
- Reusable components
- Clean code principles

✅ **Exception Handling**
- Custom exception classes
- Proper error messages
- Transaction rollback on errors

✅ **Database Operations**
- CRUD operations for all entities
- Transaction management (commit/rollback)
- Prepared statements for security

✅ **Data Consistency**
- Foreign key constraints
- Check constraints
- Transaction atomicity

✅ **User-Friendly Interface**
- Menu-driven console interface
- Clear prompts and messages
- Error handling and validation

## 👥 Team Collaboration

This project is designed for a team of 4 members. Suggested division:

1. **Member 1**: Database design, DAO layer, and database utilities
2. **Member 2**: Model classes, Service layer (Customer & Account)
3. **Member 3**: Service layer (Transaction), Exception handling
4. **Member 4**: UI layer, Main application, Testing, Documentation

## 🔧 Troubleshooting

### Common Issues

1. **Connection Refused**
   - Ensure MySQL server is running
   - Check database credentials in `database.properties`

2. **ClassNotFoundException**
   - Run `mvn clean compile` to download dependencies
   - Ensure MySQL Connector is in classpath

3. **SQL Syntax Errors**
   - Verify MySQL version compatibility
   - Check schema.sql for syntax errors

4. **Transaction Failures**
   - Check database connection
   - Verify account status (must be ACTIVE)
   - Ensure sufficient balance for withdrawals/transfers

## 📚 Technologies Used

- **Java 11** - Core programming language
- **MySQL 8.0** - Relational database
- **JDBC** - Database connectivity
- **Maven** - Dependency management and build tool

## 📄 License

This project is created for educational and interview demonstration purposes.

## 👨‍💻 Author

Bank Management System - Group Project  
Developed for interview demonstration

---

**Note**: Remember to update `database.properties` with your MySQL credentials before running the application.
