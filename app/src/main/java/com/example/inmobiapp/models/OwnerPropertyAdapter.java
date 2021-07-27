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

public class OwnerPropertyAdapter extends RecyclerView.Adapter<OwnerPropertyAdapter.OwnerPropertyViewHolder> {
    Context mContext;
    ArrayList<Property> mProperties;
    OwnerPropertyAdapter.ItemClickListener mItemClickListener;

    public OwnerPropertyAdapter(Context context, ArrayList<Property> favorites, OwnerPropertyAdapter.ItemClickListener itemClickListener) {
        mContext = context;
        mProperties = favorites;
        mItemClickListener = itemClickListener;
    }

    @Override
    public OwnerPropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OwnerPropertyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.favorite_item, parent, false));
    }

    @Override
    public void onBindViewHolder(OwnerPropertyAdapter.OwnerPropertyViewHolder holder, int position) {
        Property property = mProperties.get(position);

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
        return mProperties.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(Property property);
    }

    class OwnerPropertyViewHolder extends RecyclerView.ViewHolder {
        TextView address;
        TextView type;
        TextView price;
        ImageView imgURL;

        public OwnerPropertyViewHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.favorite_address);
            type = itemView.findViewById(R.id.favorite_type);
            price = itemView.findViewById(R.id.favorite_price);
            imgURL = itemView.findViewById(R.id.favorite_img);
        }
    }
}
