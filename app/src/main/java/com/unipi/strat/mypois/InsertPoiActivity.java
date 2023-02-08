package com.unipi.strat.mypois;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InsertPoiActivity extends AppCompatActivity implements LocationListener {
    EditText tile, description;
    DBHelper DB;
    LocationManager locationManager;
    double latitude;
    double longitude;
    private Activity activity;
    private Spinner spinnerCategory;//spinner is used to make a dropdown menu about Poi categories

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_poi);

        //Instantiations
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        tile = findViewById(R.id.editTextPoiTitle1);
        description = findViewById(R.id.editTextPoiDescription1);

        //Code for spinner
        spinnerCategory = findViewById(R.id.spinner);
        String[] categories = getResources().getStringArray(R.array.categories_options);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        //We get the position of Poi when we open this activity
        getLocation();
    }


    public void insertNewPoi(View view) {
        DB = new DBHelper(this);

        String titleTXT = tile.getText().toString();
        String categoryTXT = spinnerCategory.getSelectedItem().toString();
        String descriptionTXT = description.getText().toString();

        if(titleTXT.trim().matches("")){
            Toast.makeText(this, "You must add Poi's title", Toast.LENGTH_SHORT).show();
        }else{
            //Checking if the user have not touched location button
            if(longitude == 0.0 && latitude == 0.0){
                Toast.makeText(this, "Please wait to get your location", Toast.LENGTH_SHORT).show();
            }else{
                Boolean checkinsertdata = DB.insertuserdata(titleTXT, longitude, latitude, categoryTXT, descriptionTXT);
                if (checkinsertdata) {
                    Toast.makeText(this, "inserted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "not inserted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Code useful to update recycler view when we press the back button if there have been changes
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        activity.startActivity(intent);
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }
}