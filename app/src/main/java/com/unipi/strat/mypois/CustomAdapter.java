package com.unipi.strat.mypois;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import android.os.Build;
import androidx.annotation.RequiresApi;

//We use this class as a bridge between the UI and and data sources
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {

    Context context;
    Activity activity;
    ArrayList<Poi> poiArrayList;
    ArrayList<Poi> poiArrayListFull;//Helper ArrayList for search

    //Adapters constructor
    CustomAdapter(Activity activity, Context context, ArrayList<Poi> poiArrayList){
        this.activity = activity;
        this.context = context;
        this.poiArrayListFull = poiArrayList;
        this.poiArrayList = new ArrayList<>(poiArrayListFull);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Initializing values of recycler view
        holder.poi_title_txt.setText(poiArrayList.get(position).getTitle());
        holder.poi_timestamp_txt.setText(String.valueOf(poiArrayList.get(position).getTimestamp()));
        holder.poi_location_txt.setText(poiArrayList.get(position).getLatitude()+" "+poiArrayList.get(position).getLongitude());
        holder.poi_category_txt.setText(poiArrayList.get(position).getCategory());
        holder.poi_description_txt.setText(poiArrayList.get(position).getDescription());

        //For update
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdatePoiActivity.class);
                intent.putExtra("title", String.valueOf(poiArrayList.get(position).getTitle()));
                intent.putExtra("category", String.valueOf(poiArrayList.get(position).getCategory()));
                intent.putExtra("description", String.valueOf(poiArrayList.get(position).getDescription()));
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return poiArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView poi_title_txt, poi_category_txt, poi_description_txt, poi_location_txt, poi_timestamp_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            poi_title_txt = itemView.findViewById(R.id.poi_title_txt);
            poi_description_txt = itemView.findViewById(R.id.poi_description_txt);
            poi_category_txt = itemView.findViewById(R.id.poi_category_txt);
            poi_location_txt = itemView.findViewById(R.id.poi_location_txt);
            poi_timestamp_txt = itemView.findViewById(R.id.poi_timestamp_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

    @Override
    public Filter getFilter() {
        return poiFilter;
    }

    //Code for search bar
    private final Filter poiFilter = new Filter() {
        //Constraint is what user has typed
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Poi> filteredPoiArrayList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredPoiArrayList.addAll(poiArrayListFull);
            }else {

                //toLowerCase() is used to ignore upper case or lower case
                //trim() erase any space between first and last character
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Poi poi : poiArrayListFull){

                    if(poi.getTitle().toLowerCase().contains(filterPattern)){
                        filteredPoiArrayList.add(poi);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredPoiArrayList;
            results.count = filteredPoiArrayList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            poiArrayList.clear();
            poiArrayList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

}

