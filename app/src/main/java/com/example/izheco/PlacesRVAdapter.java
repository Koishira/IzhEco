package com.example.izheco;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class PlacesRVAdapter extends RecyclerView.Adapter<PlacesRVAdapter.MyViewHolder>{

    private ArrayList<Place> places;
    private Context context;
    private static int currentPosition = 8;
    private String category_name;

    public static String SpaceAndCommas(String s) {
        String s2="";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= 'a' && s.charAt(i) <= 'z' || s.charAt(i) >= 'A' && s.charAt(i) <= 'Z' || s.charAt(i) >= 'а' && s.charAt(i) <= 'я' || s.charAt(i) >= 'А' && s.charAt(i) <= 'Я')
                s2 += s.charAt(i);
        }
        return s2;
    }

    public PlacesRVAdapter(ArrayList<Place> places, Context context, String category_name) {
        this.places = places;
        this.context = context;
        this.category_name = category_name;
    }

    @NonNull
    @Override
    public PlacesRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d("CHECK", "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_closed, parent, false);
        return new PlacesRVAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Place place = places.get(position);
        holder.imageView.setImageResource(places.get(position).getImage());
        holder.categoriesNames.setText(places.get(position).getPlace_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, SpaceAndCommas(places.get(position).getPlace_name() + category_name), Toast.LENGTH_SHORT).show();
                int placeArrayInt = 0;
                try {
                    placeArrayInt = R.array.class.getField(SpaceAndCommas(places.get(position).getPlace_name() + category_name)).getInt(null);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
                String[] placeInfo = context.getResources().getStringArray(placeArrayInt);
                Intent intent = new Intent(context, MainActivity3.class);
                intent.putExtra("logo", placeInfo[0]);
                intent.putExtra("name", placeInfo[1]);
                intent.putExtra("extra_info", placeInfo[2]);
                intent.putExtra("phone_number", placeInfo[3]);
                intent.putExtra("website", placeInfo[4]);
                intent.putExtra("vk", placeInfo[5]);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return places.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView categoriesNames;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoriesNames = itemView.findViewById(R.id.category_name);
            imageView = itemView.findViewById(R.id.logo);
        }
    }
}
