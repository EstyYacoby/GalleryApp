package com.example.galleryapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageRecAdapter extends RecyclerView.Adapter<ImageRecAdapter.ImageRecAdapterViewHolder> {

    private Context context;
    private ArrayList<ImageItem> imagesList;

    public ImageRecAdapter(Context context, ArrayList<ImageItem> imagesList){
        this.context = context;
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public ImageRecAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.photo_item,parent,false);
        return new ImageRecAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageRecAdapterViewHolder holder, final int i) {
        final ImageItem currImage = imagesList.get(i);
        String imageUrl = currImage.getImageUrl();
        Picasso.get().load(imageUrl).fit().into(holder.imageView);

        if (!currImage.isSelected()) {
            holder.favImageView.setImageResource(R.drawable.ic_star_border_red_24dp);
        } else {
            holder.favImageView.setImageResource(R.drawable.ic_star_red_24dp);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.imageView.isSelected()) {
                    holder.favImageView.setImageResource(R.drawable.ic_star_border_red_24dp);
                    holder.imageView.setSelected(false);
                    currImage.setSelected(false);
                } else {
                    holder.favImageView.setImageResource(R.drawable.ic_star_red_24dp);
                    holder.imageView.setSelected(true);
                    currImage.setSelected(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ImageRecAdapterViewHolder extends RecyclerView.ViewHolder{

        public ImageButton imageView;
        public ImageView favImageView;

        public ImageRecAdapterViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            favImageView = itemView.findViewById(R.id.favorite_view);
        }
    }

}
