package com.base.e_com;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CartViewModel extends ViewModel {
    private CartRepository cartRepository;
    private LiveData<List<CartEntity>> cartItems;

    public CartViewModel(Context context) {
        cartRepository = new CartRepository(context);
        cartItems = cartRepository.getCartItems();
    }

    public LiveData<List<CartEntity>> getCartItems() {
        return cartItems;
    }

    public void insertCartItem(CartEntity cartItem) {
        cartRepository.insertCartItem(cartItem);
    }

    public void updateCartItem(CartEntity cartItem) {
        cartRepository.updateCartItem(cartItem);
    }
    public void removeCartItem(CartEntity cartItem) {

            cartRepository.removeCartItem(cartItem);
    }

}


