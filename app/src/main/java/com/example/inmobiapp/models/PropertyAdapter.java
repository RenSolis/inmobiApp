package com.example.inmobiapp.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {
    Context mContext;
    ArrayList<Property> mProperties;
    ItemClickListener mItemClickListener; // del recyclerView

    public PropertyAdapter(Context context, ArrayList<Property> properties, ItemClickListener itemClickListener) {
        mContext = context;
        mProperties = properties;
        mItemClickListener = itemClickListener;
    }

    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PropertyViewHolder (LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PropertyViewHolder holder, int position) {
        Property property = mProperties.get(position);

        holder.type.setText(property.getType());
        holder.acquisition.setText(property.getAcquisition());
        holder.address.setText(property.getAddress());
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

    class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView acquisition;
        TextView address;
        ImageView imgURL;
        Button mBSeeMore;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.item_TVType);
            acquisition = itemView.findViewById(R.id.item_TVAdq);
            address = itemView.findViewById(R.id.item_TVAddress);
            imgURL = itemView.findViewById(R.id.item_Img);
            mBSeeMore = itemView.findViewById(R.id.item_BttnSeeMore);
        }
    }
}
