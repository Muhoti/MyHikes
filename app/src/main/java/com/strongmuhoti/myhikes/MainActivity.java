package com.strongmuhoti.myhikes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.strongmuhoti.myhikes.Features.*;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    String[] SPINNERVALUES = {"Grid", "List"};
    String SpinnerValue;
    GridView gridView;
    private ArrayList<Objects> objectsList;
    //Firebase
    private DatabaseReference myRef;
    GridAdapter adapter;
    Context context;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner1);
        gridView = (GridView) findViewById(R.id.gridView);
        //Firebase
        myRef = FirebaseDatabase.getInstance().getReference();
        //ArrayList
        objectsList = new ArrayList<>();

        //This is for the spinner fragment
        ArrayAdapter<String> sAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, SPINNERVALUES);
        spinner.setAdapter(sAdapter);


        //Adding setOnItemSelectedListener method on spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerValue = (String) spinner.getSelectedItem();

                if (SpinnerValue == "List"){
                    intent = new Intent(MainActivity.this, MainFrame.class);
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