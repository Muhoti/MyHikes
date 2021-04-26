package com.strongmuhoti.myhikes;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    private Button logout, button;
    TextView topic, subject, latSpace, longSpace;
    ImageView scene;
    String topic_home, subject_home, scene_home, hLatitude, hLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout = (Button) findViewById(R.id.logout);
        button = (Button) findViewById(R.id.nav_btn);
        topic = findViewById(R.id.topic);
        subject = findViewById(R.id.subject);
        scene = findViewById(R.id.scene);
        latSpace = findViewById(R.id.latSpace);
        longSpace = findViewById(R.id.longSpace);

        latSpace.setVisibility(View.GONE);
        longSpace.setVisibility(View.GONE);

        //Receive data
        topic_home = getIntent().getStringExtra("Name");
        subject_home = getIntent().getStringExtra("About");
        scene_home = getIntent().getStringExtra("Image");
        hLatitude = getIntent().getStringExtra("Latitude");
        hLongitude = getIntent().getStringExtra("Longitude");

        //Bind data
        topic.setText(topic_home);
        subject.setText(subject_home);
        latSpace.setText(hLatitude);
        longSpace.setText(hLongitude);
        subject.setMovementMethod(new ScrollingMovementMethod());


        Glide.with(this).load(scene_home).into(scene);
        //Picasso.get().load(scene_home).into(scene);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, Login.class));
            }
        });

        //Navigation Button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast here the value of lat and long from gridAdapter from firebase...
                Intent intent = new Intent(Home.this, MapsActivity.class);
                intent.putExtra("Latitude", hLatitude);
                intent.putExtra("Longitude", hLongitude);
                intent.putExtra("Name", topic_home);
                startActivity(intent);
            }
        });
    }
}