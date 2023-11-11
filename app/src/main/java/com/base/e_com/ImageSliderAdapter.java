package com.base.e_com;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {
    private List<String> imagePaths;

    public ImageSliderAdapter(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_item, parent, false);
        return new ImageSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        String imagePath = imagePaths.get(position);
        Log.d("ImageSliderAdapter", "Loading image from: " + imagePath);
        String filePath = UriUtils.getPathFromUri(holder.itemView.getContext(), Uri.parse(imagePath));
        Picasso.get()
                .load(new File(filePath))
                .error(R.drawable.ic_email)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
            return (imagePaths != null) ? imagePaths.size() : 0 ;
    }

    public static class ImageSliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImageView);
        }
    }
}

