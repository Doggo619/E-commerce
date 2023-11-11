package com.base.e_com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddProducts extends AppCompatActivity {
    private TextInputLayout tvProductName, tvProductPrice, tvDiscountedPrice, tvImageUrl;
    private TextInputEditText etProductName, etProductPrice, etDiscountedPrice, etImageUrl, etDescription;
    private MaterialButton btnAddProduct;
    ProductEntity productEntity;

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
