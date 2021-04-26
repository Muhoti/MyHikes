package com.strongmuhoti.myhikes;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_REQUEST = 500;
    private GoogleMap mMap;
    TextView latSpace, longSpace;
    String hLatitude, hLongitude, mTitle;
    Double latitude, longitude, mLatitude, mLongitude;
    LatLng myLocation, featureLocation;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        latSpace = findViewById(R.id.latSpace);
        longSpace = findViewById(R.id.longSpace);

        // Receive data from GridAdapter
        hLatitude = getIntent().getStringExtra("Latitude");
        hLongitude = getIntent().getStringExtra("Longitude");
        mTitle = getIntent().getStringExtra("Name");

        //Bind data
        latSpace.setText(hLatitude);
        longSpace.setText(hLongitude);
//        latSpace.setVisibility(View.GONE);
//        longSpace.setVisibility(View.GONE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        latitude = Double.parseDouble(hLatitude);
        longitude = Double.parseDouble(hLongitude);

        featureLocation = new LatLng((latitude), (longitude));


    }

    public void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    mLatitude = location.getLatitude();
                    mLongitude = location.getLongitude();
                    myLocation = new LatLng(mLatitude, mLongitude);

                    Toast.makeText(MapsActivity.this, "Your location is: " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //Show marker on screen and adjust zoom levels
        mMap.addMarker(new MarkerOptions().position(myLocation).title("Origin").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.addMarker(new MarkerOptions().position(featureLocation).title(mTitle).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 8f));
        new TaskDirectionRequest().execute(buildRequestUrl(myLocation,featureLocation));

    }

    private String buildRequestUrl(LatLng origin, LatLng destination) {
        // Value of myLocation
        String stringOrigin = "origin=" + origin.latitude + ", " + origin.longitude;
        // Value of destination
        String stringDestination = "destination=" + destination.latitude + ", " + destination.longitude;
        // Set value and enable the sensor
        String sensor = "sensor=false";
        // Mode for finding direction
        String mode = "mode=driving";
        // Build the full param
        String param = stringOrigin + "&" + stringDestination + "&" + sensor + "&" + mode;
        // Output format
        String output = "json";
        // API KEY
        String APIKEY = getResources().getString(R.string.google_maps_key);

        // Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + APIKEY;
        return url;
    }

    private String requestDirection(String requestedUrl) {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(requestedUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();;
            inputStreamReader.close();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        httpURLConnection.disconnect();
        return responseString;
    }

    public class TaskDirectionRequest extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            // Parse json here
            TaskParseDirection  parseResult = new TaskParseDirection();
            parseResult.execute(responseString);
        }

        public class TaskParseDirection extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... jsonString) {
                List<List<HashMap<String, String>>> routes = null;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonString[0]);
                    DirectionsParser parser = new DirectionsParser();
                    routes = parser.parse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return routes;
            }

            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
                super.onPostExecute(lists);
                ArrayList points = null;

                PolylineOptions polylineOptions = null;

                for (List<HashMap<String, String>> path: lists){
                    points = new ArrayList<>();
                    polylineOptions = new PolylineOptions();

                    for (HashMap<String, String> point: path){
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));

                        points.add(new LatLng(lat, lng));
                    }

                    polylineOptions.addAll(points);
                    polylineOptions.width(15);
                    polylineOptions.color(Color.BLUE);
                    polylineOptions.geodesic(true);
                }

                if (polylineOptions != null){
                    mMap.addPolyline(polylineOptions);
                } else {
                    Toast.makeText(MapsActivity.this, "Directions not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}


