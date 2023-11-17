package com.base.e_com;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewProducts extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;
    private ProductEntity productEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this);

        productAdapter.setOnItemClickListener(this);

        SharedPreferences preferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", "");

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProductsForUser(userId).observe(this, new Observer<List<ProductEntity>>() {
            @Override
            public void onChanged(List<ProductEntity> products) {
                productAdapter.setProducts(products);
            }


        });
        recyclerView.setAdapter(productAdapter);
    }

    @Override
    public void onEditClick(int position) {
        ProductEntity product = productAdapter.getProductAt(position);

        if (product != null) {
            String name = product.getName();
            String price = product.getPrice();
            String discountedPrice = product.getDiscountedPrice();
            String image = product.getImageUrl();
            String description = product.getDescription();
            List<String> imageUrls = product.getImageUrls();
            String imageUrl2 = (imageUrls.size() > 0) ? imageUrls.get(0) : "";
            String imageUrl3 = (imageUrls.size() > 1) ? imageUrls.get(1) : "";
            String imageUrl4 = (imageUrls.size() > 2) ? imageUrls.get(2) : "";
            String imageUrl5 = (imageUrls.size() > 3) ? imageUrls.get(3) : "";


            Intent intent = new Intent(ViewProducts.this, EditProductActivity.class);
            intent.putExtra("productId", product.getId());
            intent.putExtra("userId", product.getUserId());
            intent.putExtra("name", name);
            intent.putExtra("price", price);
            intent.putExtra("discountedPrice", discountedPrice);
            intent.putExtra("image", image);
            intent.putExtra("description", description);
            intent.putExtra("imageUrl2", imageUrl2);
            intent.putExtra("imageUrl3", imageUrl3);
            intent.putExtra("imageUrl4", imageUrl4);
            intent.putExtra("imageUrl5", imageUrl5);

            startActivity(intent);
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDeleteClick(int position) {
        ProductEntity product = productAdapter.getProductAt(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this product?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                productViewModel.deleteProduct(product);
                Toast.makeText(ViewProducts.this, "Product deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null); // Do nothing on cancel
        builder.create().show();
    }

}
