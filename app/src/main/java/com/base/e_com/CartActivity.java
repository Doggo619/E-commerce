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
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private FloatingActionButton homeButton;
    private CartProductAdapter cartProductAdapter;
    private RecyclerView recyclerView;
    private ProductViewModel productViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartProductAdapter = new CartProductAdapter(this);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getCartProducts().observe(this, new Observer<List<ProductEntity>>() {
            @Override
            public void onChanged(List<ProductEntity> cartProducts) {
                cartProductAdapter.setProducts(cartProducts);
            }
        });

        cartProductAdapter.setOnItemClickListener(new CartProductAdapter.OnItemClickListener() {
            @Override
            public void onRemoveFromCartClick(int position) {
                ProductEntity product = cartProductAdapter.getProductAt(position);
                if (product != null) {
                    productViewModel.removeFromCart(product);
                }
            }
        });

        homeButton = findViewById(R.id.btn_home);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(CartActivity.this, BuyerActivity.class);
                startActivity(homeIntent);
            }
        });

        recyclerView.setAdapter(cartProductAdapter);


    }

}

