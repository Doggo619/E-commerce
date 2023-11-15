package com.base.e_com;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    Context context;
    List<String> imagePaths;

    OnItemClickListener onItemClickListener;
    private final static int YOUR_REQUEST_CODE = 1000, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3000;
    private ViewHolder currentHolder;

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
        Log.d("ImageAdapter", "checking imagePaths" + imagePaths.toString());
        currentHolder = holder;
        Log.d("ImageAdapter", "onBindViewHolder - position: " + position);
        if (imagePaths == null || imagePaths.isEmpty()) {
            Log.d("ImageAdapter", "imagePaths is null or empty");
            Picasso.get()
                    .load(R.drawable.ic_add)
                    .error(R.drawable.ic_password)
                    .into(holder.imageView);
            Toast.makeText(context, "imagePath is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagePath = "content://com.android.providers.media.documents/document/image%3A1000001992";
        Log.d("ImageAdapter", "onBindViewHolder - imagePath: " + imagePath);
        List<String> uriList = Arrays.asList(imagePath.split(","));


            for (String uri : uriList) {
                if (!TextUtils.isEmpty(uri.trim())) {
                    loadAndDisplayImage(holder, uri.trim());
                }
            }
    }
    public ViewHolder getCurrentHolder() {
        return currentHolder;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }


    private void loadAndDisplayImage(ViewHolder holder, String imagePath) {
        Log.d("ImageAdapter", "Loading image: " + imagePath);

        if (holder.imageView != null) {
            Picasso.get()
                    .load(Uri.parse("content://com.android.providers.media.documents/document/image%3A1000001992"))
                    .error(R.drawable.ic_image)
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("ImageAdapter", "Image loaded successfully");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("ImageAdapter", "Error loading image", e);
                        }
                    });
        } else {
            Log.e("ImageAdapter", "Image path is empty");
            Toast.makeText(context, "Image path is empty", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        int itemCount = (imagePaths != null) ? imagePaths.size() : 0;
        Log.d("ImageAdapter", "getItemCount: " + itemCount);
        return itemCount;
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

