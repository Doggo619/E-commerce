package com.base.e_com;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartProductAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private CartProductAdapter cartProductAdapter;
    private CartDao cartDao;
    private ProductDao productDao;
    private CartViewModel cartViewModel;
    private CartItem cartItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        CartViewModelFactory factory = new CartViewModelFactory(this);
        cartViewModel = new ViewModelProvider(this, factory).get(CartViewModel.class);

        recyclerView = findViewById(R.id.recyclerView2); // Replace with your RecyclerView ID
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartProductAdapter = new CartProductAdapter(this, this);

        // Initialize your CartDao
        cartDao = CartDatabase.getInstance(this).cartDao();
        productDao = ProductDatabase.getInstance(this).productDao();

        // Load cart items from the database and set them in the adapter
        loadCartItemsInBackground();
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(this, BuyerActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.settings) {
                Intent intent1 = new Intent(this, BuyerActivity.class);
                startActivity(intent1);
                return true;
            }
            return false;
        });

        recyclerView.setAdapter(cartProductAdapter);
    }

    private void loadCartItemsInBackground() {
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Use your CartDao to retrieve cart items from the database
                List<CartEntity> cartItems = cartDao.getCartItemsSynchronously(); // Replace with your actual synchronous method

                // Update the UI on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Convert CartEntity objects to CartItem objects if needed
                        List<CartItem> items = new ArrayList<>();
                        for (CartEntity cartItem : cartItems) {
                            // Retrieve the corresponding product details from your product database
                            ProductEntity product = getProductFromDatabase(cartItem.getProductId());
                            if (product != null) {
                                items.add(new CartItem(product, cartItem.getQuantity()));
                            }
                        }

                        cartProductAdapter.setCartItems(items);
                    }
                });
            }
        });
        backgroundThread.start();
    }

    // Implement this method to retrieve the corresponding product details from your product database
    private ProductEntity getProductFromDatabase(int productId) {
        final ProductEntity[] product = new ProductEntity[1];

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                product[0] = productDao.getProductById(productId);
            }
        });

        thread.start();

        try {
            thread.join(); // Wait for the background operation to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Update the UI with the retrieved product
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // You can update the UI with the product here
                if (product[0] != null) {
                    // Update your UI with the product data
                }
            }
        });

        return product[0];
    }

    @Override
    public void onIncrementClick(int position) {
        if (position != RecyclerView.NO_POSITION) {
            CartItem cartItem = cartProductAdapter.getCartItems().get(position);
            int updatedQuantity = cartItem.getQuantity() + 1;
            cartItem.setQuantity(updatedQuantity);
            cartProductAdapter.notifyItemChanged(position);

            // Update the CartEntity in the database with the new quantity
            CartEntity cartEntity = cartDao.getCartItem(cartItem.getProduct().getId());
            if (cartEntity != null) {
                cartEntity.setQuantity(updatedQuantity);
                cartViewModel.updateCartItem(cartEntity);
            }
        }
    }

    @Override
    public void onDecrementClick(int position) {
        if (position != RecyclerView.NO_POSITION) {
            CartItem cartItem = cartProductAdapter.getCartItems().get(position);
            int updatedQuantity = cartItem.getQuantity() - 1;
            if (updatedQuantity >= 0) {
                cartItem.setQuantity(updatedQuantity);
                cartProductAdapter.notifyItemChanged(position);

                // Update the CartEntity in the database with the new quantity
                CartEntity cartEntity = cartDao.getCartItem(cartItem.getProduct().getId());
                if (cartEntity != null) {
                    cartEntity.setQuantity(updatedQuantity);
                    cartViewModel.updateCartItem(cartEntity);
                }
            }
        }
    }

    @Override
    public void onRemoveFromCartClick(int position) {
        CartEntity cartEntity = cartProductAdapter.getProductAt(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this product?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cartViewModel.removeCartItem(cartEntity);
                Toast.makeText(getApplicationContext(), "Product deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null); // Do nothing on cancel
        builder.create().show();
    }


    }

