package com.example.gk_ltdd.api;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.gk_ltdd.DatabaseHelper;
import com.example.gk_ltdd.model.User;

public class UserAPI {
    private DatabaseHelper db;


    public UserAPI(Context context) {
        db = new DatabaseHelper(context);
    }

    public User login(String username, String password) {
        User user = db.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

}
