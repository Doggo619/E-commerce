package com.base.e_com;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductEntity> products;
    private Context context;
    private OnItemClickListener listener;

    public ProductAdapter(Context context) {
        this.context = context;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }
    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
//        ProductEntity product = productEntities.get(position);
//
//        holder.productName.setText(product.getName());
//        holder.productPrice.setText("Price: ₹" + product.getPrice());
//        holder.productDiscountedPrice.setText("Discounted Price: ₹" + product.getDiscountedPrice());
//
//        Picasso.get()
//                .load(product.getImageUrl())
//                .error(R.drawable.ic_image)
//                .into(holder.productImage);
        if (products != null) {
            ProductEntity product = products.get(position);
            Log.d("ProductAdapter", "Binding product: " + product.getName());
            holder.productName.setText(product.getName());
            holder.productPrice.setText("Discounted Price: ₹" +  product.getPrice());
            holder.productDiscountedPrice.setText("Price: ₹" + product.getDiscountedPrice());
            holder.productDiscountedPrice.setPaintFlags(holder.productDiscountedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            if (!TextUtils.isEmpty(product.getImageUrl())) {
                Picasso.get()
                        .load(product.getImageUrl())
                        .error(R.drawable.ic_delete)
                        .into(holder.productImage);
            } else {
                holder.productImage.setImageResource(R.drawable.ic_email);
            }

            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
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
        ImageButton editButton, deleteButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_productImage);
            productName = itemView.findViewById(R.id.tv_productName);
            productPrice = itemView.findViewById(R.id.tv_productPrice);
            productDiscountedPrice = itemView.findViewById(R.id.tv_productDiscountedPrice);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }
}
