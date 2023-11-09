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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class BuyerActivity extends AppCompatActivity implements BuyerProductAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private BuyerProductAdapter productAdapter;
    private ProductViewModel productViewModel;
    private ProductEntity productEntity;
    private SharedPreferences cartPrefs;
    private CartDatabase cartDatabase;
    private CartDao cartDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new BuyerProductAdapter(this);
        cartDatabase = CartDatabase.getInstance(this);
        cartDao = cartDatabase.cartDao();

        productAdapter.setOnItemClickListener(this);

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
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.cart) {
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.settings) {
                Intent intent1 = new Intent(this, BuyerActivity.class);
                startActivity(intent1);
                return true;
            }
            return false;
        });

        recyclerView.setAdapter(productAdapter);
    }

    @Override
    public void onAddToCartClick(int position) {
        ProductEntity product = productAdapter.getProductAt(position);

        if (product != null) {
            boolean isProductInCart = product.isInCart();

            if (isProductInCart) {
                // Product is in the cart, remove it from the cart

                // Fetch the existing cart item from the database
                CartEntity existingCartItem = cartDao.getCartItem(product.getId());

                if (existingCartItem != null) {
                    // Create and start a new thread for database operations
                    Thread backgroundThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            cartDao.deleteCartItem(existingCartItem);

                            // Update the UI on the main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BuyerActivity.this, "Removed from Cart", Toast.LENGTH_SHORT).show();
                                    // Update the button state to "Add to Cart"
                                    productAdapter.updateCartItemStatus(position,false);
                                    product.setInCart(false);
                                    productAdapter.notifyItemChanged(position);
                                }
                            });
                        }
                    });
                    backgroundThread.start();
                }

            } else {
                // Product is not in the cart, add it with quantity 1
                CartEntity cartItem = new CartEntity(product.getId(), 1);

                Thread backgroundThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cartDao.insertCartItem(cartItem);

                        // Update the UI on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BuyerActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                                // Update the button state to "Remove from Cart"
                                productAdapter.updateCartItemStatus(position,true);
                                product.setInCart(true);
                                productAdapter.notifyItemChanged(position);
                            }
                        });
                    }
                });
                backgroundThread.start();
            }
        }
    }




}

