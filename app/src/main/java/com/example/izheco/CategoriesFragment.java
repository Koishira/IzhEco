package com.example.izheco;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Category> categories = new ArrayList<>();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.tabs);
        setCategories();
        CategoriesRVAdapter adapter = new CategoriesRVAdapter(categories, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setCategories() {
        String[] categoriesNames = getResources().getStringArray(R.array.Categories);
        for (int i = 0; i < categoriesNames.length; i++) {
            categories.add(new Category(categoriesNames[i], categoriesImages[i], tabTypes[i][0], tabTypes[i][1], tabTypes[i][2]));
        }
    }
}
