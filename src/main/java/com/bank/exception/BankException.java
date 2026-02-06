package com.bank.exception;

/**
 * Custom exception for Bank Management System
 */
public class BankException extends Exception {
    public BankException(String message) {
        super(message);
    }
    
    public BankException(String message, Throwable cause) {
        super(message, cause);
    }
}
