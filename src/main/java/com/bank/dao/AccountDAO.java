package com.bank.dao;

import com.bank.model.Account;
import com.bank.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Account operations
 * Handles all database operations related to accounts
 */
public class AccountDAO {
    
    /**
     * Create a new account
     */
    public int createAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (customer_id, account_number, account_type, balance, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, account.getCustomerId());
            pstmt.setString(2, account.getAccountNumber());
            pstmt.setString(3, account.getAccountType().name());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setString(5, account.getStatus().name());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int accountId = generatedKeys.getInt(1);
                    DatabaseConnection.commit();
                    return accountId;
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        }
    }
    
    /**
     * Get account by ID
     */
    public Account getAccountById(int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
                return null;
            }
        }
    }
    
    /**
     * Get account by account number
     */
    public Account getAccountByNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
                return null;
            }
        }
    }
    
    /**
     * Get all accounts for a customer
     */
    public List<Account> getAccountsByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE customer_id = ? ORDER BY account_id";
        List<Account> accounts = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
        }
        
        return accounts;
    }
    
    /**
     * Update account balance
     */
    public boolean updateBalance(int accountId, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, accountId);
            
            int affectedRows = pstmt.executeUpdate();
            DatabaseConnection.commit();
            return affectedRows > 0;
        }
    }
    
    /**
     * Update account status
     */
    public boolean updateAccountStatus(int accountId, Account.AccountStatus status) throws SQLException {
        String sql = "UPDATE accounts SET status = ? WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            pstmt.setInt(2, accountId);
            
            int affectedRows = pstmt.executeUpdate();
            DatabaseConnection.commit();
            return affectedRows > 0;
        }
    }
    
    /**
     * Get account balance
     */
    public double getBalance(int accountId) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }
                throw new SQLException("Account not found");
            }
        }
    }
    
    /**
     * Map ResultSet to Account object
     */
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setCustomerId(rs.getInt("customer_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setAccountType(Account.AccountType.valueOf(rs.getString("account_type")));
        account.setBalance(rs.getDouble("balance"));
        account.setStatus(Account.AccountStatus.valueOf(rs.getString("status")));
        return account;
    }
}
