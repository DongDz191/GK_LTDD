package com.example.gk_ltdd.api;

import android.content.Context;
import com.example.gk_ltdd.DatabaseHelper;
import com.example.gk_ltdd.model.User;

public class LoginAPI {
    private DatabaseHelper db;

    public LoginAPI(Context context) {
        db = new DatabaseHelper(context);
    }

    public User login(String username, String password) {
        User user = db.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.isActivated()) {
            return user;
        }
        return null;
    }
/*
    public boolean sendOTP(String email, String otp) {
        return db.updateUserOTP(email, otp);
    }

    public boolean verifyOTP(String email, String otp) {
        return db.verifyOTP(email, otp);
    }

 */
}
