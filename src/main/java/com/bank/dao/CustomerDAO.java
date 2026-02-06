package com.bank.dao;

import com.bank.model.Customer;
import com.bank.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Customer operations
 * Handles all database operations related to customers
 */
public class CustomerDAO {
    
    /**
     * Create a new customer
     */
    public int createCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone, address, date_of_birth) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getAddress());
            pstmt.setDate(6, Date.valueOf(customer.getDateOfBirth()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int customerId = generatedKeys.getInt(1);
                    DatabaseConnection.commit();
                    return customerId;
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }
        }
    }
    
    /**
     * Get customer by ID
     */
    public Customer getCustomerById(int customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
                return null;
            }
        }
    }
    
    /**
     * Get customer by email
     */
    public Customer getCustomerByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM customers WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
                return null;
            }
        }
    }
    
    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customers ORDER BY customer_id";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        
        return customers;
    }
    
    /**
     * Update customer information
     */
    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, " +
                     "phone = ?, address = ?, date_of_birth = ? WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getAddress());
            pstmt.setDate(6, Date.valueOf(customer.getDateOfBirth()));
            pstmt.setInt(7, customer.getCustomerId());
            
            int affectedRows = pstmt.executeUpdate();
            DatabaseConnection.commit();
            return affectedRows > 0;
        }
    }
    
    /**
     * Delete customer
     */
    public boolean deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            int affectedRows = pstmt.executeUpdate();
            DatabaseConnection.commit();
            return affectedRows > 0;
        }
    }
    
    /**
     * Map ResultSet to Customer object
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        return customer;
    }
}
