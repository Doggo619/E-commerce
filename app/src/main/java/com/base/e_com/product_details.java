package com.base.e_com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

public class product_details extends AppCompatActivity {
    ImageView image;
    TextView name, price, discountedPrice, description;
    MaterialButton addToCart;
    ProductViewModel productViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        name = findViewById(R.id.tv_name);
        price = findViewById(R.id.tv_price);
        discountedPrice = findViewById(R.id.tv_discountedPrice);
        description = findViewById(R.id.tv_description);
        image = findViewById(R.id.iv_image);
        addToCart = findViewById(R.id.btn_addtocart);

        Intent intent = getIntent();
        int productId = intent.getIntExtra("productId", 0);
        String productName = intent.getStringExtra("name");
        String productPrice = intent.getStringExtra("price");
        String productDiscountPrice = intent.getStringExtra("discountedPrice");
        String productDescription = intent.getStringExtra("description");
        String productImage = intent.getStringExtra("image");

        name.setText(productName);
        price.setText("Original Price : ₹" + productDiscountPrice);
        discountedPrice.setText("₹" + productPrice + "/-");
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        description.setText(productDescription);
        Picasso.get()
                .load(productImage)
                .error(R.drawable.ic_email)
                .into(image);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveData<ProductEntity> productLiveData = productViewModel.getProductById(productId);

                // Observe the LiveData
                productLiveData.observe(product_details.this, new Observer<ProductEntity>() {
                    @Override
                    public void onChanged(ProductEntity product) {
                        if (product != null) {
                            if (product.isInCart()) {
                                productViewModel.removeFromCart(product);
                                addToCart.setText("Add to Cart");
                            } else {
                                productViewModel.addToCart(product);
                                addToCart.setText("Remove from Cart");
                            }

                            // Remove the observer to avoid memory leaks
                            productLiveData.removeObserver(this);
                        }
                    }
                });
            }
        });

    }
}