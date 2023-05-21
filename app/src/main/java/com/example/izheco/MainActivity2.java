package com.example.izheco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Place> places = new ArrayList<>();
    String category_name_rv = "";
    int category_pic_rv;
    int categoryNameInt = 0;
    int categoryPicInt = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView category_type_name = findViewById(R.id.category_type_name);
        if (getIntent().hasExtra("category_name"))
        {
            category_type_name.setText(getIntent().getStringExtra("category_name"));
        }
        if (getIntent().hasExtra("category_type"))
        {
            category_name_rv = getIntent().getStringExtra("category_type");
        }
        if (getIntent().hasExtra("category_pic"))
        {
            categoryPicInt = getIntent().getIntExtra("category_pic", 0);
        }
        try {
            categoryNameInt = R.array.class.getField(category_name_rv).getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        recyclerView = findViewById(R.id.tabs);
        setPlaces();
        PlacesRVAdapter adapter = new PlacesRVAdapter(places, this, getIntent().getStringExtra("category_name"));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageView back = findViewById(R.id.back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setPlaces() {
        String[] placesNames = getResources().getStringArray(categoryNameInt);
        for (int i = 0; i < placesNames.length; i++) {
            places.add(new Place(placesNames[i], categoryPicInt));
        }
    }
}