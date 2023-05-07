package com.example.izheco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoriesRVAdapter extends RecyclerView.Adapter<CategoriesRVAdapter.MyViewHolder> {

    Context context;
    ArrayList <Category> Categories;

    public CategoriesRVAdapter(Context context, ArrayList <Category> Categories) {
        this.context = context;
        this.Categories = Categories;
    }

    @NonNull
    @Override
    public CategoriesRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tab, parent, false);
        return new CategoriesRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesRVAdapter.MyViewHolder holder, int position) {
        holder.categoriesNames.setText(Categories.get(position).getCategory_name());
        holder.imageView.setImageResource(Categories.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return Categories.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView categoriesNames;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.logo);
            categoriesNames = itemView.findViewById(R.id.category_name);
        }
    }
}
