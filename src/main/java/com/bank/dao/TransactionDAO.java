package com.bank.dao;

import com.bank.model.Transaction;
import com.bank.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Transaction operations
 * Handles all database operations related to transactions
 */
public class TransactionDAO {
    
    /**
     * Create a new transaction
     */
    public int createTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, " +
                     "balance_after, description, related_account_id, transaction_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, transaction.getAccountId());
            pstmt.setString(2, transaction.getTransactionType().name());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setDouble(4, transaction.getBalanceAfter());
            pstmt.setString(5, transaction.getDescription());
            
            if (transaction.getRelatedAccountId() != null) {
                pstmt.setInt(6, transaction.getRelatedAccountId());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            
            pstmt.setTimestamp(7, Timestamp.valueOf(transaction.getTransactionDate()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int transactionId = generatedKeys.getInt(1);
                    DatabaseConnection.commit();
                    return transactionId;
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
        }
    }
    
    /**
     * Get transaction by ID
     */
    public Transaction getTransactionById(int transactionId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transactionId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransaction(rs);
                }
                return null;
            }
        }
    }
    
    /**
     * Get all transactions for an account
     */
    public List<Transaction> getTransactionsByAccountId(int accountId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }
        
        return transactions;
    }
    
    /**
     * Get transaction history with pagination
     */
    public List<Transaction> getTransactionHistory(int accountId, int limit) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_id = ? " +
                     "ORDER BY transaction_date DESC LIMIT ?";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }
        
        return transactions;
    }
    
    /**
     * Map ResultSet to Transaction object
     */
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setAccountId(rs.getInt("account_id"));
        transaction.setTransactionType(
            Transaction.TransactionType.valueOf(rs.getString("transaction_type"))
        );
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setBalanceAfter(rs.getDouble("balance_after"));
        transaction.setDescription(rs.getString("description"));
        
        int relatedAccountId = rs.getInt("related_account_id");
        if (!rs.wasNull()) {
            transaction.setRelatedAccountId(relatedAccountId);
        }
        
        Timestamp timestamp = rs.getTimestamp("transaction_date");
        if (timestamp != null) {
            transaction.setTransactionDate(timestamp.toLocalDateTime());
        }
        
        return transaction;
    }
}
