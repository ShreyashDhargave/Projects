package com.bank.model;

/**
 * Account Model Class
 * Represents a bank account
 */
public class Account {
    public enum AccountType {
        SAVINGS, CURRENT, FIXED_DEPOSIT
    }
    
    public enum AccountStatus {
        ACTIVE, INACTIVE, CLOSED
    }
    
    private int accountId;
    private int customerId;
    private String accountNumber;
    private AccountType accountType;
    private double balance;
    private AccountStatus status;
    
    public Account() {}
    
    public Account(int customerId, String accountNumber, AccountType accountType) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = 0.0;
        this.status = AccountStatus.ACTIVE;
    }
    
    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }
    
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public AccountStatus getStatus() {
        return status;
    }
    
    public void setStatus(AccountStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", customerId=" + customerId +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
}
