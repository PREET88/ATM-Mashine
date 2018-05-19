package com.centennail.atm.server.models.api;

import com.centennail.atm.server.models.data.UserData;
import com.centennail.atm.server.models.data.UsersList;
import com.centennail.atm.shared.enums.Operation;


public class AtmBL {

    private UsersList usersList;

    public AtmBL(UsersList usersList) {

        this.usersList = usersList;
    }

    public double performATMOperation(String userId, Operation operation, double amount) throws Exception {
        double balance = 0;
        switch (operation) {
            case DEPOSIT:
                balance = deposit(userId, amount);
                break;
            case WITHDRAW:
                balance = withdraw(userId, amount);
                break;
        }
        return balance;
    }

    public boolean isUserExist(String userId, int pin) {
        return usersList.isUserExist(userId, pin);
    }

    public double getUserBalance(String userId) throws Exception{
        System.out.println("Getting user " + userId + " balance...");
        UserData userData = usersList.getUserById(userId);
        System.out.println("User " + userId + " balance is: " + userData.getBalance());
        return userData.getBalance();
    }
    private double deposit(String userId, double amount) throws Exception {
        UserData userData = usersList.getUserById(userId);
        userData.setBalance(userData.getBalance() + amount);
        return userData.getBalance();
    }

    private double withdraw(String userId, double amount) throws Exception {
        UserData userData = usersList.getUserById(userId);
        userData.setBalance(userData.getBalance() - amount);
        return userData.getBalance();
    }


}
