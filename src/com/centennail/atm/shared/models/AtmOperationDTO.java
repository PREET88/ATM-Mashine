package com.centennail.atm.shared.models;

import com.centennail.atm.shared.enums.Operation;

import java.io.Serializable;
// Data Transfer Object
public class AtmOperationDTO implements Serializable {
    private String userId;
    private int pin;
    private boolean isUserExist;
    private Operation operation;
    private double amount;
    private double balance;

    // Constractor for for deposit or withdraw
    public AtmOperationDTO(Operation operation, String userId, int pin, double amount) {
        this.setUserId(userId);
        this.setPin(pin);
        this.setOperation(operation);
        this.setAmount(amount);
    }
    // Constructor for Login
    public AtmOperationDTO(Operation operation, String userId, int pin) {
        this.setUserId(userId);
        this.setPin(pin);
        this.setOperation(operation);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isUserExist() {
        return isUserExist;
    }

    public void setIsUserExist(boolean validUser) {
        this.isUserExist = validUser;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AtmOperationDTO{" +
                "userId='" + userId + '\'' +
                ", pin=" + pin +
                ", isUserExist=" + isUserExist +
                ", operation=" + operation +
                ", amount=" + amount +
                ", balance=" + balance +
                '}';
    }
}
