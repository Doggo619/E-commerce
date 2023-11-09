package com.base.e_com;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductsRepository {
    private ProductDao productsDao;
    private LiveData<List<ProductEntity>> allProducts;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ProductsRepository(Application application) {
        ProductDatabase database = ProductDatabase.getInstance(application);
        productsDao = database.productDao();
        allProducts = productsDao.getAllProducts();
    }
    public LiveData<List<ProductEntity>> getProductsForUser(String userId) {
        return productsDao.getProductsForUser(userId);
    }

    public LiveData<List<ProductEntity>> getAllProducts() {
        return allProducts;
    }
    public LiveData<List<ProductEntity>> getCartProducts() {
        // Modify this method to return products where isInCart is true
        return productsDao.getCartProducts();
    }

    public void insertProduct(ProductEntity product) {
        executorService.execute(() -> {
            // Execute database insertion in the background thread
            productsDao.insertProduct(product);
        });
    }

    public void updateProduct(ProductEntity product) {
        executorService.execute(() -> {
            // Execute database update in the background thread
            productsDao.updateProducts(product);
        });
    }

    public void deleteProduct(ProductEntity product) {
        executorService.execute(() -> {
            // Execute database deletion in the background thread
            productsDao.deleteProducts(product);
        });
    }
}
