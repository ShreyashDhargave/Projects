package com.bank.model;

import java.time.LocalDateTime;

/**
 * Transaction Model Class
 * Represents a bank transaction
 */
public class Transaction {
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER_OUT, TRANSFER_IN
    }
    
    private int transactionId;
    private int accountId;
    private TransactionType transactionType;
    private double amount;
    private double balanceAfter;
    private String description;
    private Integer relatedAccountId;
    private LocalDateTime transactionDate;
    
    public Transaction() {}
    
    public Transaction(int accountId, TransactionType transactionType, 
                      double amount, double balanceAfter, String description) {
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.transactionDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    
    public int getAccountId() {
        return accountId;
    }
    
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    
    public TransactionType getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getRelatedAccountId() {
        return relatedAccountId;
    }
    
    public void setRelatedAccountId(Integer relatedAccountId) {
        this.relatedAccountId = relatedAccountId;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", accountId=" + accountId +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", balanceAfter=" + balanceAfter +
                ", description='" + description + '\'' +
                ", relatedAccountId=" + relatedAccountId +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
