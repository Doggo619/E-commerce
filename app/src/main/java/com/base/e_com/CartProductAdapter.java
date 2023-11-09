package com.base.e_com;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartItemViewHolder> {

    private List<CartItem> cartItems;
    private List<CartEntity> cart;
    private Context context;
    private OnItemClickListener listener;
    CartActivity cartActivity;

    public CartProductAdapter(Context context, CartActivity cartActivity) {
        this.context = context;
        this.cartActivity = cartActivity;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        if (cartItems != null) {
            CartItem cartItem = cartItems.get(position);
            ProductEntity product = cartItem.getProduct();

            holder.productNameTextView.setText(product.getName());
            holder.productQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
            holder.productPriceTextView.setText("Price: ₹" + product.getPrice());
            Picasso.get()
                    .load(product.getImageUrl())
                            .into(holder.productImageView);

            // Set onClickListener for your buttons
            holder.incrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CartAdapter", "Increment button clicked");
                    if (listener != null) {
                        listener.onIncrementClick(holder.getAdapterPosition());
                        notifyItemChanged(holder.getAdapterPosition());
                        }
                    }
            });

            holder.decrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDecrementClick(holder.getAdapterPosition());
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                }
            });

            holder.removeFromCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("remove button", "clicked r");
                    Toast.makeText(cartActivity, "clicked remove from cart", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRemoveFromCartClick(position);
                            notifyDataSetChanged();
                            Toast.makeText(context.getApplicationContext(), "touched", Toast.LENGTH_SHORT).show();
                            Log.d("ButtonClicked", "Remove from Cart clicked");
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (cartItems != null) ? cartItems.size() : 0;
    }
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    public CartEntity getProductAt(int position) {
        if (cart != null && position >= 0 && position < cart.size()) {
            return cart.get(position);
        }
        return null;
    }



    public class CartItemViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView productNameTextView, productQuantityTextView, productPriceTextView;
        ImageButton incrementButton, decrementButton;
        MaterialButton removeFromCartButton;
        ImageView productImageView;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.iv_productImage);
            productNameTextView = itemView.findViewById(R.id.tv_productName);
            productQuantityTextView = itemView.findViewById(R.id.tv_quantity);
            productPriceTextView = itemView.findViewById(R.id.tv_productPrice);
            incrementButton = itemView.findViewById(R.id.btn_increment);
            decrementButton = itemView.findViewById(R.id.btn_decrement);
            removeFromCartButton = itemView.findViewById(R.id.btn_removefromcart);
        }
    }

    public interface OnItemClickListener {
        void onIncrementClick(int position);
        void onDecrementClick(int position);
        void onRemoveFromCartClick(int position);
    }
}

