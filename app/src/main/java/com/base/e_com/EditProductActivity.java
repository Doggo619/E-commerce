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

import java.util.Arrays;
import java.util.List;

public class EditProductActivity extends AppCompatActivity {

    private TextInputEditText name, price, discountedPrice, image, description, image2, image3, image4, image5;
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
        image2 = findViewById(R.id.et_image2);
        image3 = findViewById(R.id.et_image3);
        image4 = findViewById(R.id.et_image4);
        image5 = findViewById(R.id.et_image5);
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
        String imageUrl2 = getIntent().getStringExtra("imageUrl2");
        String imageUrl3 = getIntent().getStringExtra("imageUrl3");
        String imageUrl4 = getIntent().getStringExtra("imageUrl4");
        String imageUrl5 = getIntent().getStringExtra("imageUrl5");

        name.setText(productName);
        discountedPrice.setText(productDiscountedPrice);
        price.setText(productPrice);
        name.setText(productName);
        discountedPrice.setText(productDiscountedPrice);
        price.setText(productPrice);
        image.setText(productImage);
        description.setText(productDescription);
        image2.setText(imageUrl2);
        image3.setText(imageUrl3);
        image4.setText(imageUrl4);
        image5.setText(imageUrl5);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = name.getText().toString();
                String updatedPrice = price.getText().toString();
                String updatedDiscountedPrice = discountedPrice.getText().toString();
                String updatedImage = image.getText().toString();
                String updatedDescription = description.getText().toString();
                String updatedImage2 = image2.getText().toString();
                String updatedImage3 = image3.getText().toString();
                String updatedImage4 = image4.getText().toString();
                String updatedImage5 = image5.getText().toString();
                List<String> updatedImageUrls = Arrays.asList(updatedImage2, updatedImage3, updatedImage4, updatedImage5);

                ProductEntity updatedProduct = new ProductEntity();
                updatedProduct.setId(productId);
                updatedProduct.setUserId(userId);
                updatedProduct.setName(updatedName);
                updatedProduct.setPrice(updatedPrice);
                updatedProduct.setDiscountedPrice(updatedDiscountedPrice);
                updatedProduct.setImageUrl(updatedImage);
                updatedProduct.setDescription(updatedDescription);
                updatedProduct.setImageUrls(updatedImageUrls);

                productViewModel.updateProduct(updatedProduct);

                Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}