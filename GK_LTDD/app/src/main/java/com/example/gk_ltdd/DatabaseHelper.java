package com.example.gk_ltdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.gk_ltdd.model.Product;
import com.example.gk_ltdd.model.User;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gk_ltdd.db";
    private static final int DATABASE_VERSION = 1;

    // Table User
    private static final String TABLE_USER = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_IS_ACTIVATED = "is_activated";
    private static final String COLUMN_OTP = "otp";

    // Table Product
    private static final String TABLE_PRODUCT = "products";
    private static final String COLUMN_PRODUCT_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_PRODUCT_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USER + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT UNIQUE, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_IMAGE + " TEXT, "
                + COLUMN_IS_ACTIVATED + " INTEGER DEFAULT 0, "
                + COLUMN_OTP + " TEXT);";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCT + " ("
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_PRICE + " REAL, "
                + COLUMN_PRODUCT_IMAGE + " TEXT);";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        insertTestUsers(db);
        insertTestProducts(db);
    }

    public void insertTestUsers(SQLiteDatabase db) {
        insertUser(db, "user1", "user1@example.com", "123456", "", "");
        insertUser(db, "user2", "user2@example.com", "password", "", "");
        insertUser(db, "user3", "user3@example.com", "abc123", "", "");
    }

    private void insertUser(SQLiteDatabase db, String username, String email, String password, String image, String otp) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_OTP, otp);
        db.insert(TABLE_USER, null, values);
    }

    private void insertTestProducts(SQLiteDatabase db) {
        insertProduct(db, "Product A", "Category 1", 100.0, "image_a.png");
        insertProduct(db, "Product B", "Category 2", 150.0, "image_b.png");
        insertProduct(db, "Product C", "Category 3", 200.0, "image_c.png");
    }

    private void insertProduct(SQLiteDatabase db, String name, String category, double price, String image) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_PRODUCT_IMAGE, image);
        db.insert(TABLE_PRODUCT, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(db);
    }

    public User getUserByUsername(String username) {
    return null;
    }

    // 📌 Lấy danh sách tất cả sản phẩm
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCT, null);
        if (cursor.moveToFirst()) {
            do {
                products.add(new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    // 📌 Lấy sản phẩm mới nhất
    public Product getLastProduct() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCT + " ORDER BY " + COLUMN_PRODUCT_ID + " DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            Product product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE))
            );
            cursor.close();
            db.close();
            return product;
        }
        return null;
    }

    // 📌 Lấy tất cả danh mục sản phẩm
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COLUMN_CATEGORY + " FROM " + TABLE_PRODUCT, null);
        while (cursor.moveToNext()) {
            categories.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return categories;
    }
}