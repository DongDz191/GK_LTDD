package com.example.gk_ltdd.api;

import android.content.Context;
import com.example.gk_ltdd.DatabaseHelper;
import com.example.gk_ltdd.model.Product;
import java.util.List;

public class ProductAPI {
    private DatabaseHelper db;

    public ProductAPI(Context context) {
        db = new DatabaseHelper(context);
    }

    public List<Product> getAllProducts() {
        return db.getAllProducts();
    }

    public Product getLastProduct() {
        return db.getLastProduct();
    }

    public List<String> getAllCategories() {
        return db.getAllCategories();
    }
}
