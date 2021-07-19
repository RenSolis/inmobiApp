package com.example.inmobiapp.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    Context mContext;
    ArrayList<Property> mFavorites;
    ItemClickListener mItemClickListener;

    public FavoriteAdapter(Context context, ArrayList<Property> favorites, ItemClickListener itemClickListener) {
        mContext = context;
        mFavorites = favorites;
        mItemClickListener = itemClickListener;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(LayoutInflater.from(mContext).inflate(R.layout.favorite_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Property property = mFavorites.get(position);

        holder.address.setText(property.getAddress());
        holder.type.setText(property.getType());
        holder.price.setText(Integer.toString(property.getPrice()));
        Picasso.get().load(property.getImage()).into(holder.imgURL);

        holder.itemView.setOnClickListener(view -> {
            mItemClickListener.onItemClickListener(property);
        });
    }

    @Override
    public int getItemCount() {
        return mFavorites.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(Property property);
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView address;
        TextView type;
        TextView price;
        ImageView imgURL;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.favorite_address);
            type = itemView.findViewById(R.id.favorite_type);
            price = itemView.findViewById(R.id.favorite_price);
            imgURL = itemView.findViewById(R.id.favorite_img);
        }
    }
}
