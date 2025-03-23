package com.example.gkltdt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gkltdt.API.CategoryAPI;
import com.example.gkltdt.API.ProductAPI;
import com.example.gkltdt.API.UserAPI;
import com.example.gkltdt.model.Category;
import com.example.gkltdt.model.Product;
import com.example.gkltdt.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ImageView ivUserImage;
    private TextView tvUsername;
    private RecyclerView rvCategories, rvProducts;
    private BottomNavigationView bottomNavigationView;

    private UserAPI userAPI;
    private CategoryAPI categoryAPI;
    private ProductAPI productAPI;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        // Ánh xạ view từ layout
        ivUserImage = findViewById(R.id.ivUserImage);
        tvUsername = findViewById(R.id.tvUsername);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        rvCategories = findViewById(R.id.rvCategories);
        rvProducts = findViewById(R.id.rvProducts);

        // Khởi tạo API
        userAPI = new UserAPI(this);
        categoryAPI = new CategoryAPI(this);
        productAPI = new ProductAPI(this);

        // Lấy dữ liệu người dùng được truyền qua Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        if (username != null) {
            User user = userAPI.getUserByUsername(username);
            if (user != null) {
                tvUsername.setText(user.getUsername());
                if (user.getImage() != null && user.getImage().length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
                    ivUserImage.setImageBitmap(bitmap);
                }
            } else {
                tvUsername.setText("User not found");
            }
        } else {
            tvUsername.setText("No username provided");
        }

        // Thiết lập RecyclerView cho Categories (nằm ngang)
        List<Category> categoryList = categoryAPI.getAllCategories();
        categoryAdapter = new CategoryAdapter(categoryList, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {
                // Khi click vào category, load danh sách product tương ứng
                List<Product> productList = productAPI.getProductsByCategory(category.getId());
                productAdapter.updateList(productList);
            }
        });
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(categoryAdapter);

        // Thiết lập RecyclerView cho Products (lưới 3 cột)
        rvProducts.setLayoutManager(new GridLayoutManager(this, 3));
        // Ban đầu load sản phẩm của category đầu tiên nếu có
        if (categoryList != null && !categoryList.isEmpty()) {
            List<Product> productList = productAPI.getProductsByCategory(categoryList.get(0).getId());
            productAdapter = new ProductAdapter(productList);
        } else {
            productAdapter = new ProductAdapter(new ArrayList<Product>());
        }
        rvProducts.setAdapter(productAdapter);

        // Thiết lập listener cho Bottom Navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Xử lý các lựa chọn trong Bottom Navigation tại đây
                switch (item.getItemId()) {
                    // Ví dụ xử lý chuyển sang Home, Profile, Settings, ...
                    default:
                        return false;
                }
            }
        });
    }
}
