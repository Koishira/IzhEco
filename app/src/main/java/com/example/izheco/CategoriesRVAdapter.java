package com.example.izheco;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoriesRVAdapter extends RecyclerView.Adapter<CategoriesRVAdapter.MyViewHolder> {

    private ArrayList<Category> categories;
    private Context context;
    private static int currentPosition = 8;

    public CategoriesRVAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoriesRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d("CHECK", "onCreateViewHolder");
//        int optionId = 0;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_opened111, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesRVAdapter.MyViewHolder holder, int position) {
//        Log.d("CHECK", "onBindViewHolder");
        Category category = categories.get(position);
        holder.imageView.setImageResource(categories.get(position).getImage());
        holder.categoriesNames.setText(categories.get(position).getCategory_name());
        holder.give.setVisibility(View.GONE);
        holder.sell.setVisibility(View.GONE);
        holder.exchange.setVisibility(View.GONE);
        holder.constraintLayout.setVisibility(View.GONE);
        if (categories.get(position).getGive() == 1)
            holder.give.setVisibility(View.VISIBLE);
        else
            holder.give.setVisibility(View.GONE);
        if (categories.get(position).getSell() == 1)
            holder.sell.setVisibility(View.VISIBLE);
        else
            holder.sell.setVisibility(View.GONE);
        if (categories.get(position).getExchange() == 1)
            holder.exchange.setVisibility(View.VISIBLE);
        else
            holder.exchange.setVisibility(View.GONE);
//        if (categories.get(position).getGive() == 1 && categories.get(position).getSell() == 1 && categories.get(currentPosition).getExchange() == 1) {
//            holder.give.setVisibility(View.VISIBLE);
//            holder.sell.setVisibility(View.VISIBLE);
//            holder.exchange.setVisibility(View.VISIBLE);
//        }
//        else {
//            if (categories.get(position).getGive() == 1 && categories.get(position).getSell() == 1 && categories.get(currentPosition).getExchange() == 0) {
//                holder.give.setVisibility(View.VISIBLE);
//                holder.sell.setVisibility(View.VISIBLE);
//                holder.exchange.setVisibility(View.GONE);
//            }
//            else {
//                holder.give.setVisibility(View.GONE);
//                holder.sell.setVisibility(View.GONE);
//                holder.exchange.setVisibility(View.GONE);
//            }
//        }
        if (currentPosition == position) {
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_left_fade);
            holder.constraintLayout.setVisibility(View.VISIBLE);
            holder.constraintLayout.startAnimation(slideDown);
        }
        holder.unfolded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("CHECK", "onClick");
                currentPosition = position;
                notifyDataSetChanged();
            }
        });
        holder.give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "give", Toast.LENGTH_SHORT).show();
            }
        });
        holder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "sell", Toast.LENGTH_SHORT).show();
            }
        });
        holder.exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "exchange", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView categoriesNames;
        ConstraintLayout constraintLayout;
        ConstraintLayout unfolded;
        CardView give, sell, exchange;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoriesNames = itemView.findViewById(R.id.category_name);
            imageView = itemView.findViewById(R.id.logo);
            constraintLayout = itemView.findViewById(R.id.expander);
            unfolded = itemView.findViewById(R.id.unfolded);
            give = itemView.findViewById(R.id.option1);
            sell = itemView.findViewById(R.id.option2);
            exchange = itemView.findViewById(R.id.option3);
        }
    }
}