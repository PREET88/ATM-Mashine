package com.centennail.atm.server.models.data;

import javax.swing.*;
import java.util.ArrayList;

public class UsersList extends ArrayList<UserData> {

    public UserData getUserById(String id) throws Exception {
        for (UserData userData : this) {
            if (userData != null && userData.getId().equals(id)) {
                return userData;
            }
        }

        throw new Exception("User id: " + id + " Not Found!!!");
    }

    public boolean isUserExist(String id, int pin) {
        for (UserData userData : this) {
            if (userData != null && userData.getId().equals(id) && userData.getPin() == pin) {
                return true;
            }
        }
        return false;
    }
}
