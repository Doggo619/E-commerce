package com.base.e_com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BuyerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BuyerProductAdapter productAdapter;
    private ProductViewModel productViewModel;
    private ProductEntity productEntity;
    private SharedPreferences cartPrefs;
    private FloatingActionButton cartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        recyclerView = findViewById(R.id.recyclerView2);
        cartButton = findViewById(R.id.btn_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new BuyerProductAdapter(this);


        cartPrefs = getSharedPreferences("cart_prefs", Context.MODE_PRIVATE);

        SharedPreferences preferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", "");
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, new Observer<List<ProductEntity>>() {
            @Override
            public void onChanged(List<ProductEntity> products) {
                productAdapter.setProducts(products);
            }
        });
        productAdapter.setOnItemClickListener(new BuyerProductAdapter.OnItemClickListener() {
            @Override
            public void onAddToCartClick(int position) {
                ProductEntity product = productAdapter.getProductAt(position);
                if (product != null) {
                    if (product.isInCart()) {
                        productViewModel.removeFromCart(product);
                    } else {
                        productViewModel.addToCart(product);
                    }
                }
            }
        });
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(BuyerActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }
        });

        recyclerView.setAdapter(productAdapter);
    }


}

