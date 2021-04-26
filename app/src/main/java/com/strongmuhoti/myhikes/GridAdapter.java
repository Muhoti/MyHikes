package com.strongmuhoti.myhikes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Objects>objectsList;
    private LayoutInflater inflater;

    public GridAdapter(Context context, ArrayList<Objects> objectsList) {
        this.context = context;
        this.objectsList = objectsList;
    }

    @Override
    public int getCount() {
        return objectsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    //The Below function implements the GridView...
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = convertView;

        if (convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.custom_layout,null);
        }

        ImageView icon = (ImageView) gridView.findViewById(R.id.icons);
        TextView letter = (TextView) gridView.findViewById(R.id.letters);
        TextView story = (TextView) gridView.findViewById(R.id.story);
        TextView mLatitude = (TextView) gridView.findViewById(R.id.mLatitude);
        TextView mLongitude = (TextView) gridView.findViewById(R.id.mLongitude);

        story.setVisibility(View.GONE);
        mLatitude.setVisibility(View.GONE);
        mLongitude.setVisibility(View.GONE);

        //ImageView:Glide Library
        Glide.with(context).load(objectsList.get(position).getImage())
                .into(icon);

        //TextView
        letter.setText(objectsList.get(position).getName());
        story.setText(objectsList.get(position).getAbout());
//        mLatitude.setText(objectsList.get(position).getLatitude());
//        mLongitude.setText(objectsList.get(position).getLongitude());

        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Name", objectsList.get(position).getName());
                intent.putExtra("About", objectsList.get(position).getAbout());
                intent.putExtra("Image", objectsList.get(position).getImage());
                intent.putExtra("Latitude", objectsList.get(position).getLatitude());
                intent.putExtra("Longitude", objectsList.get(position).getLongitude());
                context.startActivity(intent);
            }
        });

        return gridView;

    }
}






