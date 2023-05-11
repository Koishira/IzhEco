package com.example.izheco;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoriesRVAdapter extends RecyclerView.Adapter<CategoriesRVAdapter.MyViewHolder> {

    private final RVInterface rvInterface;
    Context context;
    ArrayList <Category> Categories;

    public CategoriesRVAdapter(Context context, ArrayList <Category> Categories, RVInterface rvInterface) {
        this.context = context;
        this.Categories = Categories;
        this.rvInterface = rvInterface;
    }

    @NonNull
    @Override
    public CategoriesRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tab_opened111, parent, false);
        return new CategoriesRVAdapter.MyViewHolder(view, rvInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesRVAdapter.MyViewHolder holder, int position) {
        holder.categoriesNames.setText(Categories.get(position).getCategory_name());
        holder.imageView.setImageResource(Categories.get(position).getImage());
        boolean isExpanded = Categories.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return Categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView categoriesNames;
        TextView give, sell, exchange;
        ConstraintLayout expandableLayout;

        public MyViewHolder(@NonNull View itemView, RVInterface rvInterface) {
            super(itemView);

            imageView = itemView.findViewById(R.id.logo);
            categoriesNames = itemView.findViewById(R.id.category_name);
            expandableLayout = itemView.findViewById(R.id.expander);
//            give = itemView.findViewById(R.id.option1);
//            sell = itemView.findViewById(R.id.option2);
//            exchange = itemView.findViewById(R.id.option3);

            itemView.setOnClickListener(view -> {
                if (rvInterface != null) {
                    int pos = getAdapterPosition();
                    if (pos !=RecyclerView.NO_POSITION) {
                        rvInterface.onClick(pos);
                    }
                }
                Category category = Categories.get(getAdapterPosition());
                category.setExpanded(!category.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}
