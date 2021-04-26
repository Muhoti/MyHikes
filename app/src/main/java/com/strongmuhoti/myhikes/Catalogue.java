package com.strongmuhoti.myhikes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Catalogue extends AppCompatActivity {

    GridView gridView;
    private ArrayList<Objects> objectsList;
    //Firebase
    private DatabaseReference myRef;
    GridAdapter adapter;
    Context context;

//    String letterList[] = { "Park", "Bomas", "Karura Forest", "Galton Fenzi", "Park", "Bomas", "Karura Forest", "Galton Fenzi", "Park", "Bomas", "Karura Forest", "Galton Fenzi", "Park", "Bomas", "Karura Forest", "Galton Fenzi", "Park", "Bomas", "Karura Forest", "Galton Fenzi", "Park", "Bomas", "Karura Forest", "Galton Fenzi", "Park", "Bomas", "Karura Forest", "Galton Fenzi"};

//    int lettersIcon[] = {R.drawable.animal, R.drawable.park, R.drawable.recreational, R.drawable.reserve, R.drawable.animal, R.drawable.park, R.drawable.recreational, R.drawable.reserve, R.drawable.animal, R.drawable.park, R.drawable.recreational, R.drawable.reserve, R.drawable.animal, R.drawable.park, R.drawable.recreational, R.drawable.reserve, R.drawable.animal, R.drawable.park, R.drawable.recreational, R.drawable.reserve, R.drawable.animal, R.drawable.park, R.drawable.recreational, R.drawable.reserve, R.drawable.animal, R.drawable.park, R.drawable.recreational, R.drawable.reserve };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        gridView = (GridView) findViewById(R.id.gridView);

        //Firebase
        myRef = FirebaseDatabase.getInstance().getReference();

        //ArrayList
        objectsList = new ArrayList<>();

        //Clear ArrayList
        ClearAll();


        //Get Data Method
        GetDataFromFirebase();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Catalogue.this, Home.class));
                Toast.makeText(Catalogue.this, "Selected :"  + objectsList, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void GetDataFromFirebase() {

        Query query = myRef.child("Features");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Objects objects = new Objects();

                    objects.setImage(snapshot.child("imageUrl").getValue().toString());
                    //objects.setAbout(snapshot.child("about").getValue().toString());
                    objects.setName(snapshot.child("name").getValue().toString());

                    objectsList.add(objects);
                }

                adapter = new GridAdapter(getApplicationContext(), objectsList);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ClearAll(){
        if (objectsList != null){
            objectsList.clear();

            if (adapter != null){
                adapter.notifyDataSetChanged();
            }
        }

        objectsList = new ArrayList<>();
    }
}