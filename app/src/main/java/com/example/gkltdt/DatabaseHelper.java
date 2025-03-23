package com.example.gkltdt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.gkltdt.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_database.db";
    private static final int DATABASE_VERSION = 2;

    // Bảng user
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_IMAGE = "image";

    private static final String TABLE_CREATE_USER =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_IMAGE + " BLOB" +
                    ");";

    // Bảng category
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_ID = "_id";
    public static final String COLUMN_CATEGORY_NAME = "name";

    private static final String TABLE_CREATE_CATEGORY =
            "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_NAME + " TEXT" +
                    ");";

    // Bảng product
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCT_ID = "_id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_IMAGE = "image";
    public static final String COLUMN_PRODUCT_CATEGORY_ID = "category_id";

    private static final String TABLE_CREATE_PRODUCT =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT, " +
                    COLUMN_PRODUCT_PRICE + " REAL, " +
                    COLUMN_PRODUCT_IMAGE + " BLOB, " +
                    COLUMN_PRODUCT_CATEGORY_ID + " INTEGER" +
                    ");";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USER);
        db.execSQL(TABLE_CREATE_CATEGORY);
        db.execSQL(TABLE_CREATE_PRODUCT);
        // Seed dữ liệu mẫu ngay khi tạo database
        seedSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng cũ và tạo lại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // Methods cho User
    public long insertUser(String username, String email, String password, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_IMAGE, image);

        long newRowId = db.insert(TABLE_USERS, null, values);
        if (newRowId != -1) {
            Toast.makeText(context, "User registered locally", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to register user locally", Toast.LENGTH_SHORT).show();
        }
        return newRowId;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
            User user = new User(username, email, password, image);
            cursor.close();
            return user;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // Các hàm cho Category
    public long insertCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        return db.insert(TABLE_CATEGORIES, null, values);
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CATEGORIES, null, null, null, null, null, null);
    }

    // Các hàm cho Product
    public long insertProduct(String name, double price, byte[] image, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_PRODUCT_PRICE, price);
        values.put(COLUMN_PRODUCT_IMAGE, image);
        values.put(COLUMN_PRODUCT_CATEGORY_ID, categoryId);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public Cursor getProductsByCategory(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PRODUCTS, null, COLUMN_PRODUCT_CATEGORY_ID + "=?",
                new String[]{String.valueOf(categoryId)}, null, null, null);
    }

    // Hàm seed dữ liệu mẫu cho Category và Product
    private void seedSampleData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // Thêm các Category mẫu
        values.put(COLUMN_CATEGORY_NAME, "Electronics");
        long idElectronics = db.insert(TABLE_CATEGORIES, null, values);

        values.clear();
        values.put(COLUMN_CATEGORY_NAME, "Fashion");
        long idFashion = db.insert(TABLE_CATEGORIES, null, values);

        values.clear();
        values.put(COLUMN_CATEGORY_NAME, "Home & Garden");
        long idHome = db.insert(TABLE_CATEGORIES, null, values);

        // Thêm các Product mẫu cho Electronics
        values.clear();
        values.put(COLUMN_PRODUCT_NAME, "Smartphone");
        values.put(COLUMN_PRODUCT_PRICE, 699.99);
        values.put(COLUMN_PRODUCT_IMAGE, (byte[]) null); // hoặc có thể dùng ảnh mẫu dưới dạng byte[]
        values.put(COLUMN_PRODUCT_CATEGORY_ID, idElectronics);
        db.insert(TABLE_PRODUCTS, null, values);

        values.clear();
        values.put(COLUMN_PRODUCT_NAME, "Laptop");
        values.put(COLUMN_PRODUCT_PRICE, 1299.99);
        values.put(COLUMN_PRODUCT_IMAGE, (byte[]) null);
        values.put(COLUMN_PRODUCT_CATEGORY_ID, idElectronics);
        db.insert(TABLE_PRODUCTS, null, values);

        // Thêm các Product mẫu cho Fashion
        values.clear();
        values.put(COLUMN_PRODUCT_NAME, "T-shirt");
        values.put(COLUMN_PRODUCT_PRICE, 19.99);
        values.put(COLUMN_PRODUCT_IMAGE, (byte[]) null);
        values.put(COLUMN_PRODUCT_CATEGORY_ID, idFashion);
        db.insert(TABLE_PRODUCTS, null, values);

        values.clear();
        values.put(COLUMN_PRODUCT_NAME, "Jeans");
        values.put(COLUMN_PRODUCT_PRICE, 49.99);
        values.put(COLUMN_PRODUCT_IMAGE, (byte[]) null);
        values.put(COLUMN_PRODUCT_CATEGORY_ID, idFashion);
        db.insert(TABLE_PRODUCTS, null, values);

        // Thêm các Product mẫu cho Home & Garden
        values.clear();
        values.put(COLUMN_PRODUCT_NAME, "Sofa");
        values.put(COLUMN_PRODUCT_PRICE, 399.99);
        values.put(COLUMN_PRODUCT_IMAGE, (byte[]) null);
        values.put(COLUMN_PRODUCT_CATEGORY_ID, idHome);
        db.insert(TABLE_PRODUCTS, null, values);

        values.clear();
        values.put(COLUMN_PRODUCT_NAME, "Coffee Table");
        values.put(COLUMN_PRODUCT_PRICE, 89.99);
        values.put(COLUMN_PRODUCT_IMAGE, (byte[]) null);
        values.put(COLUMN_PRODUCT_CATEGORY_ID, idHome);
        db.insert(TABLE_PRODUCTS, null, values);
    }
}
