package com.base.e_com;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ProductViewHolder> {
    private List<ProductEntity> products;
    private Context context;
    private OnItemClickListener listener;
    public CartProductAdapter(Context context){
        this.context = context;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onRemoveFromCartClick(int position);
        void onIncrementClick(int position);
        void onDecrementClick(int position);
    }
    @NonNull
    @Override
    public CartProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductAdapter.ProductViewHolder holder, int position) {
        if (products != null) {
            ProductEntity product = products.get(position);
            holder.productName.setText(product.getName());
            double price = Double.parseDouble(product.getPrice());
            double totalPrice = price * product.getQuantity();
            holder.productPrice.setText("Price: ₹" + String.format("%.2f", totalPrice));
            holder.productQuantity.setText(String.valueOf(product.getQuantity()));

            if (!TextUtils.isEmpty(product.getImageUrl())) {
                Picasso.get()
                        .load(product.getImageUrl())
                        .error(R.drawable.ic_delete)
                        .into(holder.productImage);
            } else {
                holder.productImage.setImageResource(R.drawable.ic_email);
            }
            holder.increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onIncrementClick(position);
                        }
                    }
                }
            });

            holder.decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDecrementClick(position);
                        }
                    }
                }
            });
            holder.removeFromCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRemoveFromCartClick(position);
                        }
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return (products != null) ? products.size() : 0;
    }

    public void setProducts(List<ProductEntity> cartProducts) {
        this.products = cartProducts;
        notifyDataSetChanged();
    }
    public ProductEntity getProductAt(int position) {
        if (products != null && position >= 0 && position < products.size()) {
            return products.get(position);
        }
        return null;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        MaterialTextView productName, productPrice, productQuantity;
        ImageButton increment, decrement;
        MaterialButton removeFromCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_productImage);
            productName = itemView.findViewById(R.id.tv_productName);
            productPrice = itemView.findViewById(R.id.tv_productPrice);
            productQuantity = itemView.findViewById(R.id.tv_quantity);
            increment = itemView.findViewById(R.id.btn_increment);
            decrement = itemView.findViewById(R.id.btn_decrement);
            removeFromCart = itemView.findViewById(R.id.btn_removefromcart);
        }
    }
}

