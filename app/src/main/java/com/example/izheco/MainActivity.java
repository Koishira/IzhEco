package com.example.izheco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.izheco.update.AppUpdate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList <Category> categories = new ArrayList<>();
    int[] categoriesImages = {R.drawable.book, R.drawable.responsive, R.drawable.toys, R.drawable.tshirt};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.tabs);
        setCategories();
        CategoriesRVAdapter adapter = new CategoriesRVAdapter(this, categories);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCategories() {
        String[] categoriesNames = getResources().getStringArray(R.array.Categories);
        for (int i = 0; i < categoriesNames.length; i++) {
            categories.add(new Category(categoriesNames[i], categoriesImages[i]));
        }
    }
}