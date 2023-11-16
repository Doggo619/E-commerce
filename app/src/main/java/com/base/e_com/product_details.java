package com.base.e_com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import java.util.List;

public class product_details extends AppCompatActivity {
    ImageView image;
    TextView name, price, discountedPrice, description;
    MaterialButton addToCart;
    ProductViewModel productViewModel;
    ImageAdapter adapter;
    private static final int YOUR_REQUEST_CODE = 3000;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =1000;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        name = findViewById(R.id.tv_name);
        price = findViewById(R.id.tv_price);
        discountedPrice = findViewById(R.id.tv_discountedPrice);
        description = findViewById(R.id.tv_description);
        addToCart = findViewById(R.id.btn_addtocart);
        RecyclerView recyclerView = findViewById(R.id.recycler);


        Intent intent = getIntent();
        int productId = intent.getIntExtra("productId", 0);
        String productName = intent.getStringExtra("name");
        String productPrice = intent.getStringExtra("price");
        String productDiscountPrice = intent.getStringExtra("discountedPrice");
        String productDescription = intent.getStringExtra("description");
        String productImage = intent.getStringExtra("image");
        String[] imagePathsArray = intent.getStringArrayExtra("imagePaths");
        String[] imageUrlsArray = intent.getStringArrayExtra("imageUrls");
        List<String> imagePathsList = Arrays.asList(imageUrlsArray);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            adapter = new ImageAdapter(product_details.this, imagePathsList);
            recyclerView.setAdapter(adapter);
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onClick(ImageView imageView, String path) {
                Toast.makeText(product_details.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        name.setText(productName);
        price.setText("Original Price : ₹" + productDiscountPrice);
        discountedPrice.setText("₹" + productPrice + "/-");
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        description.setText(productDescription);

//        Picasso.get()
//                .load(productImage)
//                .error(R.drawable.ic_email)
//                .into(image);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveData<ProductEntity> productLiveData = productViewModel.getProductById(productId);
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
                            productLiveData.removeObserver(this);
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Picasso.get()
                    .load(selectedImageUri)
                    .error(R.drawable.ic_image)
                    .into(image);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Permission denied. Cannot load images.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}