package com.bank.exception;

/**
 * Exception thrown when customer is not found
 */
public class CustomerNotFoundException extends BankException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
