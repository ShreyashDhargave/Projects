package com.bank.exception;

/**
 * Exception thrown when account has insufficient balance
 */
public class InsufficientBalanceException extends BankException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
