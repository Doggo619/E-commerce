package com.base.e_com;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddProducts extends AppCompatActivity {
    private TextInputLayout tvProductName, tvProductPrice, tvDiscountedPrice, tvImageUrl;
    private TextInputEditText etProductName, etProductPrice, etDiscountedPrice, etImageUrl, etDescription;
    private MaterialButton btnAddProduct, btnMoreImages;
    ProductEntity productEntity;
    private List<String> selectedImagePaths = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    private ConstraintLayout constraintLayout;
    private LinearLayout imagesLayout;
    private int imageCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        tvProductName = findViewById(R.id.tv_productName);
        tvProductPrice = findViewById(R.id.tv_price);
        tvDiscountedPrice = findViewById(R.id.tv_discountedPrice);
        tvImageUrl = findViewById(R.id.tv_image);
        etProductName = findViewById(R.id.et_productName);
        etProductPrice = findViewById(R.id.et_price);
        etDiscountedPrice = findViewById(R.id.et_discountedPrice);
        etImageUrl = findViewById(R.id.et_image);
        etDescription = findViewById(R.id.et_description);
        btnAddProduct = findViewById(R.id.btn_add);
        constraintLayout = findViewById(R.id.constraintLayout);
        btnMoreImages = findViewById(R.id.btn_moreImages);
        imagesLayout = findViewById(R.id.layout_images);

        btnMoreImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageCount <= 5) {
                    addImageUrlField();
                    imageCount++;
                }
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                String userId = preferences.getString("userId", "");

                productEntity = new ProductEntity();
                productEntity.setUserId(userId);
                productEntity.setName(etProductName.getText().toString());
                productEntity.setPrice(etProductPrice.getText().toString());
                productEntity.setDiscountedPrice(etDiscountedPrice.getText().toString());
                productEntity.setImageUrl(etImageUrl.getText().toString());
                productEntity.setDescription(etDescription.getText().toString());
                productEntity.setImagePaths(selectedImagePaths);
                saveImageUrls();
                productEntity.setImageUrls(imageUrls);


                if (validateProductData(productEntity)) {
                   ProductDatabase productDatabase = ProductDatabase.getInstance(getApplicationContext());
                   ProductDao productDao = productDatabase.productDao();

                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           long productId = productDao.insertProduct(productEntity);
                           if (productId > 0) {
                               Integer productIntegerId = (int) productId;
                               UserProductMapping userProductMapping = new UserProductMapping();
                               userProductMapping.setUserId(userId);
                               userProductMapping.setProductId(productIntegerId);

                               productDao.addUserProductMapping(userProductMapping);
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       Toast.makeText(getApplicationContext(), "Product is Added!", Toast.LENGTH_SHORT).show();
                                       finish();
                                   }
                               });
                           } else {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       Toast.makeText(getApplicationContext(), "Failed to add the product", Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }
                       }
                   }).start();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addImageUrlField() {
        TextInputLayout imageUrlLayout = new TextInputLayout(this);
        imageUrlLayout.setVisibility(View.VISIBLE);
        imageUrlLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        imageUrlLayout.setHint("Image URL " + imageCount);
        TextInputEditText imageUrlEditText = new TextInputEditText(imageUrlLayout.getContext());
        imageUrlEditText.setMaxLines(3);
        imageUrlEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        imageUrlEditText.setId(View.generateViewId());
        imageUrlLayout.setStartIconDrawable(getResources().getDrawable(R.drawable.ic_image));
        imageUrlLayout.addView(imageUrlEditText);
        LinearLayout imagesLayout = findViewById(R.id.layout_images);
        imagesLayout.addView(imageUrlLayout);
    }
    private void saveImageUrls() {
        imageUrls.clear(); // Clear existing URLs before adding
        for (int i = 0; i < imagesLayout.getChildCount(); i++) {
            View childView = imagesLayout.getChildAt(i);
            if (childView instanceof TextInputLayout) {
                TextInputLayout textInputLayout = (TextInputLayout) childView;
                EditText editText = textInputLayout.getEditText();
                if (editText != null) {
                    String imageUrl = editText.getText().toString();
                    if (!TextUtils.isEmpty(imageUrl)) {
                        imageUrls.add(imageUrl);
                    }
                }
            }
        }
    }

    private boolean validateProductData(ProductEntity productEntity) {
        boolean isValid = true;

        if (productEntity.getName().isEmpty()) {
            tvProductName.setError("Product name is required");
            isValid = false;
        } else {
            tvProductName.setError(null);
        }
        Integer productPrice = Integer.valueOf(productEntity.getDiscountedPrice());
        if (productPrice == null || productPrice <= 0) {
            tvProductPrice.setError("Product price is required");
            isValid = false;
        } else {
            tvProductPrice.setError(null);
        }
        return isValid;
    }
}
