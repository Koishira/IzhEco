package com.example.izheco;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlacesRVAdapter extends RecyclerView.Adapter<PlacesRVAdapter.MyViewHolder>{

    private ArrayList<Place> places;
    private Context context;
    private static int currentPosition = 8;

    public PlacesRVAdapter(ArrayList<Place> places, Context context) {
        this.places = places;
        this.context = context;
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
                context.startActivity(new Intent(context, MainActivity3.class));
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
