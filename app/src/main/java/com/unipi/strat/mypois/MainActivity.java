package com.unipi.strat.mypois;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DBHelper DB;
    ArrayList<Poi> poiArrayList;

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        DB = new DBHelper(this);

        //initializing arrays from db
        storeDataInArrays();

        //Initializing the recycler view
        customAdapter = new CustomAdapter(this,this, poiArrayList);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    //We store all data from Sqlite DB to arrayList<Poi>
    void storeDataInArrays(){
        Cursor cursor = DB.readAllData();
        poiArrayList = new ArrayList<>();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                poiArrayList.add(new Poi(cursor.getString(0), Timestamp.valueOf(cursor.getString(1)),Double.parseDouble(cursor.getString(2)),
                        Double.parseDouble(cursor.getString(3)),cursor.getString(4),cursor.getString(5)));
            }
        }
    }

    //button to add new poi
    public void addNewPoi(View view){
        //we ask for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        }else{
            Intent intent = new Intent(this, InsertPoiActivity.class);
            startActivity(intent);
        }
    }

    //Code for search of poi title
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem menuItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search a title of a Poi here!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            //When user types
            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}