package com.salikkim.seller.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.salikkim.seller.Interfaces.ImageClick;
import com.salikkim.seller.Models.Images;
import com.salikkim.seller.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Images> imagesLists;
    private ImageClick imageClick;

    public ImageAdapter(Context context, ArrayList<Images> imagesLists, ImageClick imageClick) {
        this.context = context;
        this.imagesLists = imagesLists;
        this.imageClick = imageClick;
    }

    public void setImageLists(ArrayList<Images> imagesLists) {
        this.imagesLists = imagesLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_photo, parent, false);
        return new AddedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int pos = position;
        AddedViewHolder addedViewHolder = (AddedViewHolder) holder;
        // addedViewHolder.imageView.setImageURI(photoLists.get(pos));

        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#F3F3F3"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#E7E7E7"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        Glide.with(context).
                load(imagesLists.get(pos))
                .placeholder(shimmerDrawable)
                .into(addedViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return imagesLists.size();
    }

    class AddedViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageView btn_remove;

        public AddedViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.added_image);
            btn_remove = itemView.findViewById(R.id.btn_remove_photo);
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageClick.onDeleteClick(getAdapterPosition(), imagesLists.get(getAdapterPosition()));
                }
            });
        }
    }
}
