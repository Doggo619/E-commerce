package com.base.e_com;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CartViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Context context;

    public CartViewModelFactory(Context context) {
        this.context = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass == CartViewModel.class) {
            return (T) new CartViewModel(context);
        }
        return null;
    }
}

