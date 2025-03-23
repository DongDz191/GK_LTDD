package com.example.gkltdt.API;

import android.content.Context;
import android.database.Cursor;

import com.example.gkltdt.DatabaseHelper;
import com.example.gkltdt.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAPI {
    private DatabaseHelper dbHelper;

    public ProductAPI(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = dbHelper.getProductsByCategory(categoryId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_IMAGE));
                productList.add(new Product(id, name, price, image, categoryId));
            }
            cursor.close();
        }
        return productList;
    }
}
