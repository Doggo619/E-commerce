package com.base.e_com;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    Context context;
    List<String> imagePaths;
    OnItemClickListener onItemClickListener;
    private final static int YOUR_REQUEST_CODE = 1000, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3000;

    public ImageAdapter(Context context, List<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.carousel_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (imagePaths == null || imagePaths.isEmpty()) {
            Picasso.get()
                    .load(R.drawable.ic_add)
                    .error(R.drawable.ic_password)
                    .into(holder.imageView);
            Toast.makeText(context, "imagePath is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagePath = imagePaths.get(position);
        String[] uriArray = imagePath.split(",");
        if (uriArray.length > 0) {
            loadAndDisplayImage(holder, uriArray[0]);
        }
    }

    private void loadAndDisplayImage(ViewHolder holder, String imagePath) {
        Log.d("ImageAdapter", "Loading image: " + imagePath);

        if (!TextUtils.isEmpty(imagePath)) {
            Picasso.get()
                    .load(Uri.parse(imagePath))
                    .error(R.drawable.ic_image)
                    .into(holder.imageView);
        } else {
            Log.e("ImageAdapter", "Image path is empty");
            Toast.makeText(context, "Image path is empty", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return (imagePaths != null) ? imagePaths.size() : 0 ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_item_image);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(ImageView imageView, String path);
    }
}

