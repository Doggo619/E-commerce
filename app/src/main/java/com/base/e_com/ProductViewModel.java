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
        updateProduct(product);
    }
    public LiveData<ProductEntity> getProductById(int productId) {
        return repository.getProductById(productId);
    }
    public void removeFromCart(ProductEntity product) {
        product.setInCart(false);
        updateProduct(product);
    }
    public LiveData<List<ProductEntity>> getCartProducts() {
        return repository.getCartProducts();
    }
    public void addImagePathToProduct(int productId, String imagePath) {
        repository.addImagePathToProduct(productId, imagePath);
    }
    public LiveData<List<ProductImageEntity>> getImagesForProduct(int productId) {
        return repository.getImagesForProduct(productId);
    }

    public void insertImage(ProductImageEntity image) {
        repository.insertImage(image);
    }
}
