package com.example.gkltdt.API;

import android.content.Context;

import com.example.gkltdt.DatabaseHelper;
import com.example.gkltdt.model.User;

public class UserAPI {
    private DatabaseHelper db;

    public UserAPI(Context context) {
        db = new DatabaseHelper(context);
    }

    public boolean checklogin(String username, String password) {
        User user = db.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
    public User getUserByUsername(String username){
        return db.getUserByUsername(username);
    }



}