package com.strongmuhoti.myhikes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MainFrame extends AppCompatActivity {

    Spinner spinner;
    String[] SPINNERVALUES = {"List", "Grid"};
    String SpinnerValue;
    //Widgets
    RecyclerView recyclerView;

    //Firebase
    private DatabaseReference myRef;

    //Variables
    private ArrayList<Objects>objectsList;
    private RecyclerAdapter recyclerAdapter;
    private Context mContext;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);

        spinner = (Spinner) findViewById(R.id.spinner1);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Firebase
        myRef = FirebaseDatabase.getInstance().getReference();

        //ArrayList
        objectsList = new ArrayList<>();

        //This is for the spinner fragment
        ArrayAdapter<String> sAdapter = new ArrayAdapter<String>(MainFrame.this, android.R.layout.simple_list_item_1, SPINNERVALUES);
        spinner.setAdapter(sAdapter);


        //Adding setOnItemSelectedListener method on spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerValue = (String) spinner.getSelectedItem();

                if (SpinnerValue == "Grid"){
                    intent = new Intent(MainFrame.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODOs Auto-generated method stub
            }
        });

        //Clear ArrayList
        ClearAll();


        //Get Data Method
        GetDataFromFirebase();
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
                    objects.setAbout(snapshot.child("about").getValue().toString());
                    objects.setName(snapshot.child("name").getValue().toString());
                    objects.setLatitude(snapshot.child("latitude").getValue().toString());
                    objects.setLongitude(snapshot.child("longitude").getValue().toString());

                    objectsList.add(objects);
                }

                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), objectsList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ClearAll(){
        if (objectsList != null){
            objectsList.clear();

            if (recyclerAdapter != null){
                recyclerAdapter.notifyDataSetChanged();
            }
        }

        objectsList = new ArrayList<>();
    }
}