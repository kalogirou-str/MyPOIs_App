package com.unipi.strat.mypois;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePoiActivity extends AppCompatActivity implements LocationListener {
    Context context;
    EditText title_input, description_input;
    Spinner spinner_input;
    DBHelper DB;
    String title, description, category;
    LocationManager locationManager;
    double latitude;
    double longitude;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_poi);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        title_input = findViewById(R.id.editTextPoiTitle1);
        description_input = findViewById(R.id.editTextPoiDescription1);

        //Code for spinner
        spinner_input = findViewById(R.id.spinner1);

        //We call the location getter method
        getLocation();
        getAndSetIntentData();
    }

    //Code useful to update recycler view when we press the back button if there have been changes
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        activity.startActivity(intent);
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("title") && getIntent().hasExtra("description") && getIntent().hasExtra("category")){
            //Getting data from intent
            title = getIntent().getStringExtra("title");
            category = getIntent().getStringExtra("category");
            description = getIntent().getStringExtra("description");

            //Setting data from intent
            title_input.setText(title);
            description_input.setText(description);

            String[] categories = getResources().getStringArray(R.array.categories_options);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_input.setAdapter(adapter);
            int spinnerPosition = adapter.getPosition(category);
            spinner_input.setSelection(spinnerPosition);


        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    public void updatePoi(View view){
        DB = new DBHelper(this);

        String titleTXT = title_input.getText().toString();
        String categoryTXT = spinner_input.getSelectedItem().toString();
        String descriptionTXT = description_input.getText().toString();

        Boolean checkupdatedata = DB.updateuserdata(titleTXT, longitude, latitude, categoryTXT, descriptionTXT);
        if (checkupdatedata) {
            Toast.makeText(this, "updated!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "not updated", Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //locationManager.removeUpdates(this);
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

    public void deletePoi(View view){
        DB = new DBHelper(this);

        String titleTXT = title_input.getText().toString();
        Boolean checkdeletedata = DB.deletedata(titleTXT);
        if (checkdeletedata) {
            //we use context because it deletes also the record from recycler view in Main
            Toast.makeText(context, "deleted!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "not deleted", Toast.LENGTH_SHORT).show();
        }
    }
}