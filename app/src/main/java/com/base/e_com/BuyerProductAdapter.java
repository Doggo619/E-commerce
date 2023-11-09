package com.base.e_com;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class BuyerProductAdapter extends RecyclerView.Adapter<BuyerProductAdapter.ProductViewHolder> {

    private List<ProductEntity> products;
    private Context context;
    private OnItemClickListener listener;


    public BuyerProductAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onAddToCartClick(int position);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_list, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (products != null) {
            ProductEntity product = products.get(position);
            holder.productName.setText(product.getName());
            holder.productDiscountedPrice.setText("Discounted Price: ₹" +  product.getPrice());
            holder.productPrice.setText("Price: ₹" + product.getDiscountedPrice());
            holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (product.isInCart()) {
                holder.addToCartButton.setText("Remove from Cart");
                holder.addToCartButton.setEnabled(true);
            } else {
                holder.addToCartButton.setText("Add to Cart");
                holder.addToCartButton.setEnabled(true);
            }
            if (!TextUtils.isEmpty(product.getImageUrl())) {
                Picasso.get()
                        .load(product.getImageUrl())
                        .error(R.drawable.ic_delete)
                        .into(holder.productImage);
            } else {
                holder.productImage.setImageResource(R.drawable.ic_email);
            }

            holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAddToCartClick(position);

                        }
                    }
                }
            });
        }
    }

    public ProductEntity getProductAt(int position) {
        if (products != null && position >= 0 && position < products.size()) {
            return products.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return (products != null) ? products.size() : 0;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        MaterialTextView productName, productPrice, productDiscountedPrice;

        MaterialButton addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_productImage);
            productName = itemView.findViewById(R.id.tv_productName);
            productPrice = itemView.findViewById(R.id.tv_productDiscountedPrice);
            productDiscountedPrice = itemView.findViewById(R.id.tv_productPrice);
            addToCartButton = itemView.findViewById(R.id.btn_addtocart);
        }
    }

    public void updateCartItemStatus(int position, boolean isInCart) {
        // Update the cart item status (e.g., change button text or style)
        if (position >= 0 && position < products.size()) {
            ProductEntity product = products.get(position);
            product.setInCart(isInCart);
            notifyItemChanged(position);
        }
    }
}

