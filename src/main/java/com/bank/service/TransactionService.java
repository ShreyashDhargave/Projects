package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.util.DatabaseConnection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for Transaction operations
 * Contains business logic for transactions (deposit, withdrawal, transfer)
 */
public class TransactionService {
    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;
    
    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.accountDAO = new AccountDAO();
    }
    
    /**
     * Deposit money into an account
     */
    public Transaction deposit(int accountId, double amount, String description) 
            throws SQLException, AccountNotFoundException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        
        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account with ID " + accountId + " not found");
        }
        
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new SQLException("Cannot deposit to inactive or closed account");
        }
        
        try {
            double newBalance = account.getBalance() + amount;
            
            // Update account balance
            accountDAO.updateBalance(accountId, newBalance);
            
            // Create transaction record
            Transaction transaction = new Transaction(
                accountId,
                Transaction.TransactionType.DEPOSIT,
                amount,
                newBalance,
                description != null ? description : "Deposit"
            );
            transaction.setTransactionDate(LocalDateTime.now());
            
            int transactionId = transactionDAO.createTransaction(transaction);
            transaction.setTransactionId(transactionId);
            
            DatabaseConnection.commit();
            return transaction;
            
        } catch (SQLException e) {
            DatabaseConnection.rollback();
            throw e;
        }
    }
    
    /**
     * Withdraw money from an account
     */
    public Transaction withdraw(int accountId, double amount, String description) 
            throws SQLException, AccountNotFoundException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        
        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account with ID " + accountId + " not found");
        }
        
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new SQLException("Cannot withdraw from inactive or closed account");
        }
        
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException(
                "Insufficient balance. Current balance: " + account.getBalance() + 
                ", Required: " + amount
            );
        }
        
        try {
            double newBalance = account.getBalance() - amount;
            
            // Update account balance
            accountDAO.updateBalance(accountId, newBalance);
            
            // Create transaction record
            Transaction transaction = new Transaction(
                accountId,
                Transaction.TransactionType.WITHDRAWAL,
                amount,
                newBalance,
                description != null ? description : "Withdrawal"
            );
            transaction.setTransactionDate(LocalDateTime.now());
            
            int transactionId = transactionDAO.createTransaction(transaction);
            transaction.setTransactionId(transactionId);
            
            DatabaseConnection.commit();
            return transaction;
            
        } catch (SQLException e) {
            DatabaseConnection.rollback();
            throw e;
        }
    }
    
    /**
     * Transfer money between two accounts
     */
    public Transaction transfer(int fromAccountId, int toAccountId, double amount, String description) 
            throws SQLException, AccountNotFoundException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        Account fromAccount = accountDAO.getAccountById(fromAccountId);
        Account toAccount = accountDAO.getAccountById(toAccountId);
        
        if (fromAccount == null) {
            throw new AccountNotFoundException("Source account with ID " + fromAccountId + " not found");
        }
        
        if (toAccount == null) {
            throw new AccountNotFoundException("Destination account with ID " + toAccountId + " not found");
        }
        
        if (fromAccount.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new SQLException("Cannot transfer from inactive or closed account");
        }
        
        if (toAccount.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new SQLException("Cannot transfer to inactive or closed account");
        }
        
        if (fromAccount.getBalance() < amount) {
            throw new InsufficientBalanceException(
                "Insufficient balance in source account. Current balance: " + 
                fromAccount.getBalance() + ", Required: " + amount
            );
        }
        
        try {
            // Update source account balance
            double fromNewBalance = fromAccount.getBalance() - amount;
            accountDAO.updateBalance(fromAccountId, fromNewBalance);
            
            // Create withdrawal transaction for source account
            Transaction withdrawalTransaction = new Transaction(
                fromAccountId,
                Transaction.TransactionType.TRANSFER_OUT,
                amount,
                fromNewBalance,
                description != null ? description : "Transfer to " + toAccount.getAccountNumber()
            );
            withdrawalTransaction.setRelatedAccountId(toAccountId);
            withdrawalTransaction.setTransactionDate(LocalDateTime.now());
            transactionDAO.createTransaction(withdrawalTransaction);
            
            // Update destination account balance
            double toNewBalance = toAccount.getBalance() + amount;
            accountDAO.updateBalance(toAccountId, toNewBalance);
            
            // Create deposit transaction for destination account
            Transaction depositTransaction = new Transaction(
                toAccountId,
                Transaction.TransactionType.TRANSFER_IN,
                amount,
                toNewBalance,
                description != null ? description : "Transfer from " + fromAccount.getAccountNumber()
            );
            depositTransaction.setRelatedAccountId(fromAccountId);
            depositTransaction.setTransactionDate(LocalDateTime.now());
            int transactionId = transactionDAO.createTransaction(depositTransaction);
            depositTransaction.setTransactionId(transactionId);
            
            DatabaseConnection.commit();
            return depositTransaction;
            
        } catch (SQLException e) {
            DatabaseConnection.rollback();
            throw e;
        }
    }
    
    /**
     * Get transaction history for an account
     */
    public List<Transaction> getTransactionHistory(int accountId) throws SQLException {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }
    
    /**
     * Get recent transaction history with limit
     */
    public List<Transaction> getRecentTransactions(int accountId, int limit) throws SQLException {
        return transactionDAO.getTransactionHistory(accountId, limit);
    }
}
