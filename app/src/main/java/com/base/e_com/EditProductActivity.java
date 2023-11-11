package com.base.e_com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class EditProductActivity extends AppCompatActivity {

    private TextInputEditText name, price, discountedPrice, image, description;
    private TextInputLayout tvName, tvPrice, tvDiscountedPrice, tvImage;
    private MaterialButton updateButton;
    private ProductViewModel productViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        name = findViewById(R.id.et_productName);
        discountedPrice = findViewById(R.id.et_discountedPrice);
        price = findViewById(R.id.et_price);
        image = findViewById(R.id.et_image);
        description = findViewById(R.id.et_description);
        updateButton = findViewById(R.id.btn_add);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        int productId = getIntent().getIntExtra("productId", -1);
        String userId = getIntent().getStringExtra("userId");
        String productName = getIntent().getStringExtra("name");
        String productPrice = getIntent().getStringExtra("price");
        String productDiscountedPrice = getIntent().getStringExtra("discountedPrice");
        String productImage = getIntent().getStringExtra("image");
        String productDescription = getIntent().getStringExtra("description");

        name.setText(productName);
        discountedPrice.setText(productDiscountedPrice);
        price.setText(productPrice);
        name.setText(productName);
        discountedPrice.setText(productDiscountedPrice);
        price.setText(productPrice);
        image.setText(productImage);
        description.setText(productDescription);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = name.getText().toString();
                String updatedPrice = price.getText().toString();
                String updatedDiscountedPrice = discountedPrice.getText().toString();
                String updatedImage = image.getText().toString();
                String updatedDescription = description.getText().toString();

                ProductEntity updatedProduct = new ProductEntity();
                updatedProduct.setId(productId);
                updatedProduct.setUserId(userId);
                updatedProduct.setName(updatedName);
                updatedProduct.setPrice(updatedPrice);
                updatedProduct.setDiscountedPrice(updatedDiscountedPrice);
                updatedProduct.setImageUrl(updatedImage);
                updatedProduct.setDescription(updatedDescription);

                productViewModel.updateProduct(updatedProduct);

                Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}