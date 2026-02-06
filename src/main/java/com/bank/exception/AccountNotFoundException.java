package com.bank.exception;

/**
 * Exception thrown when account is not found
 */
public class AccountNotFoundException extends BankException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
