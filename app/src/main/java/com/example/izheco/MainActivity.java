package com.example.izheco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.izheco.update.AppUpdate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RVInterface {

    RecyclerView recyclerView;
    ArrayList <Category> categories = new ArrayList<>();
    int[] categoriesImages = {R.drawable.fabric_green, R.drawable.recycle_sign_green, R.drawable.toys_green, R.drawable.book_green,
            R.drawable.bottle_cap_green, R.drawable.armchair_green, R.drawable.boot_green, R.drawable.tshirt_green,
            R.drawable.compost_green, R.drawable.handicrafts_green, R.drawable.brick_green, R.drawable.souvenir_green, R.drawable.taxi_green, R.drawable.responsive_green};
    // отдать, продать, обменять
    int [][] tabTypes = {
            {1, 1, 0},//Ветошь
            {1, 1, 0},//Вторсырье
            {1, 1, 1},//Игрушки
            {1, 1, 1},//Книги
            {1, 1, 0},//Крышечки
            {1, 1, 0},//Мебель
            {1, 1, 1},//Обувь
            {1, 1, 1},//Одежда
            {0, 0, 0},//Органика
            {1, 1, 1},//Рукоделие и творчество
            {1, 1, 0},//Строительные материалы
            {1, 1, 1},//Сувениры, бижутерия, сумки
            {0, 0, 0},//Экотакси
            {1, 1, 0}//Электроника и техника
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.tabs);
        setCategories();
        CategoriesRVAdapter adapter = new CategoriesRVAdapter(this, categories, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void setCategories() {
        String[] categoriesNames = getResources().getStringArray(R.array.Categories);
        for (int i = 0; i < categoriesNames.length; i++) {
            categories.add(new Category(categoriesNames[i], categoriesImages[i], tabTypes[i][0], tabTypes[i][1], tabTypes[i][2]));
        }
    }

    @Override
    public void onClick(int position) {
        if (tabTypes[position][0] == 1 && tabTypes[position][1] == 1 && tabTypes[position][2] == 1) {
            Toast.makeText(getApplicationContext(), "1 1 1", Toast.LENGTH_SHORT).show();
        }
    }
}