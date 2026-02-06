package com.bank.service;

import com.bank.dao.CustomerDAO;
import com.bank.exception.CustomerNotFoundException;
import com.bank.model.Customer;
import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for Customer operations
 * Contains business logic for customer management
 */
public class CustomerService {
    private CustomerDAO customerDAO;
    
    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }
    
    /**
     * Register a new customer
     */
    public int registerCustomer(Customer customer) throws SQLException {
        // Validate email uniqueness
        Customer existingCustomer = customerDAO.getCustomerByEmail(customer.getEmail());
        if (existingCustomer != null) {
            throw new SQLException("Customer with email " + customer.getEmail() + " already exists");
        }
        
        return customerDAO.createCustomer(customer);
    }
    
    /**
     * Get customer by ID
     */
    public Customer getCustomer(int customerId) throws SQLException, CustomerNotFoundException {
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
        return customer;
    }
    
    /**
     * Get customer by email
     */
    public Customer getCustomerByEmail(String email) throws SQLException, CustomerNotFoundException {
        Customer customer = customerDAO.getCustomerByEmail(email);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with email " + email + " not found");
        }
        return customer;
    }
    
    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.getAllCustomers();
    }
    
    /**
     * Update customer information
     */
    public boolean updateCustomer(Customer customer) throws SQLException, CustomerNotFoundException {
        // Verify customer exists
        Customer existingCustomer = customerDAO.getCustomerById(customer.getCustomerId());
        if (existingCustomer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customer.getCustomerId() + " not found");
        }
        
        return customerDAO.updateCustomer(customer);
    }
    
    /**
     * Delete customer
     */
    public boolean deleteCustomer(int customerId) throws SQLException, CustomerNotFoundException {
        Customer existingCustomer = customerDAO.getCustomerById(customerId);
        if (existingCustomer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
        
        return customerDAO.deleteCustomer(customerId);
    }
}
