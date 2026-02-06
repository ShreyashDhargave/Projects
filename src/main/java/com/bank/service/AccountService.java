package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.dao.CustomerDAO;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.CustomerNotFoundException;
import com.bank.model.Account;
import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for Account operations
 * Contains business logic for account management
 */
public class AccountService {
    private AccountDAO accountDAO;
    private CustomerDAO customerDAO;
    
    public AccountService() {
        this.accountDAO = new AccountDAO();
        this.customerDAO = new CustomerDAO();
    }
    
    /**
     * Create a new account for a customer
     */
    public int createAccount(int customerId, String accountNumber, Account.AccountType accountType) 
            throws SQLException, CustomerNotFoundException {
        // Verify customer exists
        if (customerDAO.getCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
        
        // Check if account number already exists
        Account existingAccount = accountDAO.getAccountByNumber(accountNumber);
        if (existingAccount != null) {
            throw new SQLException("Account number " + accountNumber + " already exists");
        }
        
        Account account = new Account(customerId, accountNumber, accountType);
        return accountDAO.createAccount(account);
    }
    
    /**
     * Get account by ID
     */
    public Account getAccount(int accountId) throws SQLException, AccountNotFoundException {
        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account with ID " + accountId + " not found");
        }
        return account;
    }
    
    /**
     * Get account by account number
     */
    public Account getAccountByNumber(String accountNumber) throws SQLException, AccountNotFoundException {
        Account account = accountDAO.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account with number " + accountNumber + " not found");
        }
        return account;
    }
    
    /**
     * Get all accounts for a customer
     */
    public List<Account> getCustomerAccounts(int customerId) throws SQLException {
        return accountDAO.getAccountsByCustomerId(customerId);
    }
    
    /**
     * Get account balance
     */
    public double getBalance(int accountId) throws SQLException, AccountNotFoundException {
        Account account = getAccount(accountId);
        return account.getBalance();
    }
    
    /**
     * Update account status
     */
    public boolean updateAccountStatus(int accountId, Account.AccountStatus status) 
            throws SQLException, AccountNotFoundException {
        Account account = getAccount(accountId);
        return accountDAO.updateAccountStatus(accountId, status);
    }
    
    /**
     * Close account
     */
    public boolean closeAccount(int accountId) throws SQLException, AccountNotFoundException {
        return updateAccountStatus(accountId, Account.AccountStatus.CLOSED);
    }
}
