package com.centennail.atm.server.models.data;

import java.util.ArrayList;

public class UserData {
    private String id;
    private int pin;
    private double balance;

    public UserData(String id, int pin, double balance) {
        this.id = id;
        this.pin = pin;
        this.balance = balance;
    }

    public UserData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
