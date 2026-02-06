package com.bank.ui;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.CustomerNotFoundException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.Transaction;
import com.bank.service.AccountService;
import com.bank.service.CustomerService;
import com.bank.service.TransactionService;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Main Application Class
 * Provides menu-driven interface for Bank Management System
 */
public class BankManagementSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static CustomerService customerService = new CustomerService();
    private static AccountService accountService = new AccountService();
    private static TransactionService transactionService = new TransactionService();
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   BANK MANAGEMENT SYSTEM");
        System.out.println("========================================\n");
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getChoice();
            
            try {
                switch (choice) {
                    case 1:
                        registerCustomer();
                        break;
                    case 2:
                        createAccount();
                        break;
                    case 3:
                        deposit();
                        break;
                    case 4:
                        withdraw();
                        break;
                    case 5:
                        transfer();
                        break;
                    case 6:
                        viewAccountBalance();
                        break;
                    case 7:
                        viewTransactionHistory();
                        break;
                    case 8:
                        viewCustomerDetails();
                        break;
                    case 9:
                        viewAllCustomers();
                        break;
                    case 10:
                        viewCustomerAccounts();
                        break;
                    case 0:
                        running = false;
                        System.out.println("\nThank you for using Bank Management System!");
                        break;
                    default:
                        System.out.println("\nInvalid choice! Please try again.");
                }
            } catch (SQLException e) {
                System.err.println("\nDatabase Error: " + e.getMessage());
            } catch (CustomerNotFoundException | AccountNotFoundException | InsufficientBalanceException e) {
                System.err.println("\nError: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("\nUnexpected Error: " + e.getMessage());
                e.printStackTrace();
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1.  Register New Customer");
        System.out.println("2.  Create New Account");
        System.out.println("3.  Deposit Money");
        System.out.println("4.  Withdraw Money");
        System.out.println("5.  Transfer Money");
        System.out.println("6.  View Account Balance");
        System.out.println("7.  View Transaction History");
        System.out.println("8.  View Customer Details");
        System.out.println("9.  View All Customers");
        System.out.println("10. View Customer Accounts");
        System.out.println("0.  Exit");
        System.out.println("===============================");
        System.out.print("Enter your choice: ");
    }
    
    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void registerCustomer() throws SQLException {
        System.out.println("\n========== REGISTER CUSTOMER ==========");
        
        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();
        
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dobStr = scanner.nextLine().trim();
        
        try {
            LocalDate dateOfBirth = LocalDate.parse(dobStr, DateTimeFormatter.ISO_DATE);
            Customer customer = new Customer(firstName, lastName, email, phone, address, dateOfBirth);
            int customerId = customerService.registerCustomer(customer);
            System.out.println("\n✓ Customer registered successfully! Customer ID: " + customerId);
        } catch (DateTimeParseException e) {
            System.out.println("\n✗ Invalid date format! Please use YYYY-MM-DD format.");
        }
    }
    
    private static void createAccount() throws SQLException, CustomerNotFoundException {
        System.out.println("\n========== CREATE ACCOUNT ==========");
        
        System.out.print("Customer ID: ");
        int customerId = getChoice();
        
        System.out.print("Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        System.out.println("Account Type:");
        System.out.println("1. SAVINGS");
        System.out.println("2. CURRENT");
        System.out.println("3. FIXED_DEPOSIT");
        System.out.print("Select (1-3): ");
        int typeChoice = getChoice();
        
        Account.AccountType accountType;
        switch (typeChoice) {
            case 1:
                accountType = Account.AccountType.SAVINGS;
                break;
            case 2:
                accountType = Account.AccountType.CURRENT;
                break;
            case 3:
                accountType = Account.AccountType.FIXED_DEPOSIT;
                break;
            default:
                System.out.println("\n✗ Invalid account type!");
                return;
        }
        
        int accountId = accountService.createAccount(customerId, accountNumber, accountType);
        System.out.println("\n✓ Account created successfully! Account ID: " + accountId);
    }
    
    private static void deposit() throws SQLException, AccountNotFoundException {
        System.out.println("\n========== DEPOSIT MONEY ==========");
        
        System.out.print("Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        System.out.print("Amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\n✗ Invalid amount!");
            return;
        }
        
        System.out.print("Description (optional): ");
        String description = scanner.nextLine().trim();
        
        Account account = accountService.getAccountByNumber(accountNumber);
        Transaction transaction = transactionService.deposit(account.getAccountId(), amount, description);
        
        System.out.println("\n✓ Deposit successful!");
        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("New Balance: " + transaction.getBalanceAfter());
    }
    
    private static void withdraw() throws SQLException, AccountNotFoundException, InsufficientBalanceException {
        System.out.println("\n========== WITHDRAW MONEY ==========");
        
        System.out.print("Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        System.out.print("Amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\n✗ Invalid amount!");
            return;
        }
        
        System.out.print("Description (optional): ");
        String description = scanner.nextLine().trim();
        
        Account account = accountService.getAccountByNumber(accountNumber);
        Transaction transaction = transactionService.withdraw(account.getAccountId(), amount, description);
        
        System.out.println("\n✓ Withdrawal successful!");
        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("New Balance: " + transaction.getBalanceAfter());
    }
    
    private static void transfer() throws SQLException, AccountNotFoundException, InsufficientBalanceException {
        System.out.println("\n========== TRANSFER MONEY ==========");
        
        System.out.print("From Account Number: ");
        String fromAccountNumber = scanner.nextLine().trim();
        
        System.out.print("To Account Number: ");
        String toAccountNumber = scanner.nextLine().trim();
        
        System.out.print("Amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\n✗ Invalid amount!");
            return;
        }
        
        System.out.print("Description (optional): ");
        String description = scanner.nextLine().trim();
        
        Account fromAccount = accountService.getAccountByNumber(fromAccountNumber);
        Account toAccount = accountService.getAccountByNumber(toAccountNumber);
        
        Transaction transaction = transactionService.transfer(
            fromAccount.getAccountId(), 
            toAccount.getAccountId(), 
            amount, 
            description
        );
        
        System.out.println("\n✓ Transfer successful!");
        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("From Account Balance: " + 
            accountService.getBalance(fromAccount.getAccountId()));
        System.out.println("To Account Balance: " + 
            accountService.getBalance(toAccount.getAccountId()));
    }
    
    private static void viewAccountBalance() throws SQLException, AccountNotFoundException {
        System.out.println("\n========== ACCOUNT BALANCE ==========");
        
        System.out.print("Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        Account account = accountService.getAccountByNumber(accountNumber);
        System.out.println("\nAccount Details:");
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Balance: " + account.getBalance());
        System.out.println("Status: " + account.getStatus());
    }
    
    private static void viewTransactionHistory() throws SQLException {
        System.out.println("\n========== TRANSACTION HISTORY ==========");
        
        System.out.print("Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        try {
            Account account = accountService.getAccountByNumber(accountNumber);
            List<Transaction> transactions = transactionService.getTransactionHistory(account.getAccountId());
            
            if (transactions.isEmpty()) {
                System.out.println("\nNo transactions found for this account.");
            } else {
                System.out.println("\nTransaction History:");
                System.out.println("------------------------------------------------------------");
                System.out.printf("%-5s %-15s %-12s %-12s %-15s %-20s%n", 
                    "ID", "Type", "Amount", "Balance After", "Date", "Description");
                System.out.println("------------------------------------------------------------");
                
                for (Transaction t : transactions) {
                    System.out.printf("%-5d %-15s %-12.2f %-12.2f %-15s %-20s%n",
                        t.getTransactionId(),
                        t.getTransactionType(),
                        t.getAmount(),
                        t.getBalanceAfter(),
                        t.getTransactionDate().toLocalDate(),
                        t.getDescription() != null ? t.getDescription() : "-");
                }
                System.out.println("------------------------------------------------------------");
            }
        } catch (AccountNotFoundException e) {
            System.out.println("\n✗ " + e.getMessage());
        }
    }
    
    private static void viewCustomerDetails() throws SQLException, CustomerNotFoundException {
        System.out.println("\n========== CUSTOMER DETAILS ==========");
        
        System.out.print("Customer ID: ");
        int customerId = getChoice();
        
        Customer customer = customerService.getCustomer(customerId);
        System.out.println("\nCustomer Details:");
        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Name: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone: " + customer.getPhone());
        System.out.println("Address: " + customer.getAddress());
        System.out.println("Date of Birth: " + customer.getDateOfBirth());
    }
    
    private static void viewAllCustomers() throws SQLException {
        System.out.println("\n========== ALL CUSTOMERS ==========");
        
        List<Customer> customers = customerService.getAllCustomers();
        
        if (customers.isEmpty()) {
            System.out.println("\nNo customers found.");
        } else {
            System.out.println("\nCustomer List:");
            System.out.println("-------------------------------------------------------------------");
            System.out.printf("%-5s %-20s %-25s %-15s%n", 
                "ID", "Name", "Email", "Phone");
            System.out.println("-------------------------------------------------------------------");
            
            for (Customer c : customers) {
                System.out.printf("%-5d %-20s %-25s %-15s%n",
                    c.getCustomerId(),
                    c.getFirstName() + " " + c.getLastName(),
                    c.getEmail(),
                    c.getPhone());
            }
            System.out.println("-------------------------------------------------------------------");
        }
    }
    
    private static void viewCustomerAccounts() throws SQLException {
        System.out.println("\n========== CUSTOMER ACCOUNTS ==========");
        
        System.out.print("Customer ID: ");
        int customerId = getChoice();
        
        try {
            Customer customer = customerService.getCustomer(customerId);
            List<Account> accounts = accountService.getCustomerAccounts(customerId);
            
            if (accounts.isEmpty()) {
                System.out.println("\nNo accounts found for this customer.");
            } else {
                System.out.println("\nAccounts for " + customer.getFirstName() + " " + customer.getLastName() + ":");
                System.out.println("-------------------------------------------------------------------");
                System.out.printf("%-5s %-15s %-15s %-15s %-10s%n", 
                    "ID", "Account Number", "Type", "Balance", "Status");
                System.out.println("-------------------------------------------------------------------");
                
                for (Account a : accounts) {
                    System.out.printf("%-5d %-15s %-15s %-15.2f %-10s%n",
                        a.getAccountId(),
                        a.getAccountNumber(),
                        a.getAccountType(),
                        a.getBalance(),
                        a.getStatus());
                }
                System.out.println("-------------------------------------------------------------------");
            }
        } catch (CustomerNotFoundException e) {
            System.out.println("\n✗ " + e.getMessage());
        }
    }
}
