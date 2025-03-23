package com.example.gkltdt.API;

import android.content.Context;
import android.database.Cursor;

import com.example.gkltdt.DatabaseHelper;
import com.example.gkltdt.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAPI {
    private DatabaseHelper dbHelper;

    public CategoryAPI(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllCategories();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME));
                categoryList.add(new Category(id, name));
            }
            cursor.close();
        }
        return categoryList;
    }
}
