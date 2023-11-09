package com.base.e_com;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCartItem(CartEntity cartItem);

    @Update
    void updateCartItem(CartEntity cartItem);
    @Delete
    void deleteCartItem(CartEntity cartItem);

    @Query("SELECT * FROM cart")
    LiveData<List<CartEntity>> getCartItems();

    @Query("SELECT * FROM cart")
    List<CartEntity> getCartItemsSynchronously();

    @Query("SELECT * FROM cart WHERE productId = :productId")
    CartEntity getCartItem(long productId);
    @Query("SELECT * FROM cart WHERE productId = :productId")
    CartEntity findCartItemByProductId(int productId);

}
