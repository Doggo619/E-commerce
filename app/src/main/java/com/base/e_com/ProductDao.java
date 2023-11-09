package com.base.e_com;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    long insertProduct(ProductEntity product);

    @Update
    void updateProducts(ProductEntity product);
    @Delete
    void deleteProducts(ProductEntity product);

    @Insert
    void addUserProductMapping(UserProductMapping mapping);

    @Query("SELECT productId FROM user_product_mapping WHERE userId = :userId")
    List<Integer> getProductIdsForUser(String userId);

    @Query("SELECT * FROM products")
    LiveData<List<ProductEntity>> getAllProducts();
    @Query("SELECT * FROM products WHERE userId = :userId")
    LiveData<List<ProductEntity>> getProductsForUser(String userId);
    @Query("SELECT * FROM products WHERE isInCart = 1")
    LiveData<List<ProductEntity>> getCartProducts();


    @Query("SELECT * FROM products WHERE id = :productId")
    ProductEntity getProductById(int productId);

}

