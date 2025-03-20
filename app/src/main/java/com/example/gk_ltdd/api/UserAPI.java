package com.example.gk_ltdd.api;

import android.content.Context;
import com.example.gk_ltdd.DatabaseHelper;
import com.example.gk_ltdd.model.User;

public class UserAPI {
    private DatabaseHelper db;

    public UserAPI(Context context) {
        db = new DatabaseHelper(context);
    }

    public boolean register(String username, String email, String password, String image, String otp) {
        return db.insertUser(username, email, password, image, otp);
    }

    public User login(String username, String password) {
        User user = db.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.isActivated()) {
            return user;
        }
        return null;
    }

    public boolean activateUser(String email, String otp) {
        return db.verifyOTP(email, otp);
    }
}
