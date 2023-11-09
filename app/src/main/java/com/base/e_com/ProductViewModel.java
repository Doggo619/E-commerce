package com.base.e_com;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private ProductsRepository repository;
    private LiveData<List<ProductEntity>> allProducts;

    public ProductViewModel(Application application) {
        super(application);
        repository = new ProductsRepository(application);
        allProducts = repository.getAllProducts();
    }

    public LiveData<List<ProductEntity>> getAllProducts() {
        return allProducts;
    }
    public LiveData<List<ProductEntity>> getProductsForUser(String userId) {
        return repository.getProductsForUser(userId);
    }
    public void deleteProduct(ProductEntity product) {
        repository.deleteProduct(product);
    }
    public void updateProduct(ProductEntity product) {
        repository.updateProduct(product);
    }

    public void addToCart(ProductEntity product) {
        product.setInCart(true);
        product.setQuantity(product.getQuantity() + 1);
        updateProduct(product);
    }

    public void removeFromCart(ProductEntity product) {
        if (product.getQuantity() > 0) {
            product.setQuantity(product.getQuantity() - 1);
            if (product.getQuantity() == 0) {
                product.setInCart(false);
            }
            updateProduct(product);
        }
    }

}
