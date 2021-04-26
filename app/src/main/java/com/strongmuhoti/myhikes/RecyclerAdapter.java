package com.strongmuhoti.myhikes;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String Tag = "RecyclerView";
    //how to display it
    private Context mContext;
    //data from firebase
    private ArrayList<Objects>objectsList;

    public RecyclerAdapter(Context mContext, ArrayList<Objects> objectsList) {
        this.mContext = mContext;
        this.objectsList = objectsList;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.object_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //TextView
        holder.textView.setText(objectsList.get(position).getName());
        holder.textViewAbout.setText(objectsList.get(position).getAbout());
        holder.mLatitude.setText(objectsList.get(position).getLatitude());
        holder.mLongitude.setText(objectsList.get(position).getLongitude());

        //ImageView:Glide Library
        Glide.with(mContext).load(objectsList.get(position).getImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Name", objectsList.get(position).getName());
                intent.putExtra("About", objectsList.get(position).getAbout());
                intent.putExtra("Image", objectsList.get(position).getImage());
                intent.putExtra("Latitude", objectsList.get(position).getLatitude());
                intent.putExtra("Longitude", objectsList.get(position).getLongitude());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return objectsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Widgets
        ImageView imageView;
        TextView textViewAbout;
        TextView textView;
        TextView mLatitude;
        TextView mLongitude;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewAbout = itemView.findViewById(R.id.textViewAbout);
            textView = itemView.findViewById(R.id.textViewy);
            mLatitude = itemView.findViewById(R.id.mLatitude);
            mLongitude = itemView.findViewById(R.id.mLongitude);

            textViewAbout.setVisibility(View.GONE);
            mLatitude.setVisibility(View.GONE);
            mLongitude.setVisibility(View.GONE);
        }
    }

}
