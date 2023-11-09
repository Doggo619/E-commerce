package com.base.e_com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class SellerActivity extends AppCompatActivity {

    private MaterialTextView tvName;
    private MaterialButton btnAddProducts, btnViewProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        tvName = findViewById(R.id.tv_name);
        btnAddProducts = findViewById(R.id.btn_addProducts);
        btnViewProducts = findViewById(R.id.btn_viewProducts);

        String userName = getIntent().getStringExtra("userName");

        if (userName != null) {
            String name = capitalizeFirstLetter(userName);
            tvName.setText("Welcome " + name + "!!!");
        }

        btnAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProductsIntent = new Intent(SellerActivity.this, AddProducts.class);
                startActivity(addProductsIntent);
            }
        });

        btnViewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewProductsIntent = new Intent(SellerActivity.this, ViewProducts.class);
                startActivity(viewProductsIntent);
            }
        });
    }
    private String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}