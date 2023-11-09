package com.base.e_com;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CartRepository {
    private CartDao cartDao;
    private LiveData<List<CartEntity>> cartItems;
    private Executor executor;

    public CartRepository(Context context) {
        cartDao = CartDatabase.getInstance(context).cartDao();
        cartItems = cartDao.getCartItems();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CartEntity>> getCartItems() {
        return cartItems;
    }

    public void insertCartItem(CartEntity cartItem) {
        executor.execute(() -> {
            cartDao.insertCartItem(cartItem);
        });
    }

    public void updateCartItem(CartEntity cartItem) {
        executor.execute(() -> {
            cartDao.updateCartItem(cartItem);
        });
    }
    public void removeCartItem(CartEntity cartItem) {
        executor.execute(() -> {
            cartDao.deleteCartItem(cartItem);
        });
    }

    public CartEntity findCartItemByProduct(ProductEntity product) {
        return cartDao.findCartItemByProductId(product.getId());
    }


}


