package com.example.izheco.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.izheco.R;
import com.example.izheco.adapters.CategoriesRVAdapter;
import com.example.izheco.database.DatabaseManager;
import com.example.izheco.models.Category;

import java.util.List;

public class CategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoriesRVAdapter adapter;
    private DatabaseManager databaseManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Инициализация базы данных
        databaseManager = new DatabaseManager(getContext());
        databaseManager.open();

        // Загрузка категорий из базы данных
        List<Category> categories = databaseManager.getAllCategories();

        // Инициализация адаптера
        adapter = new CategoriesRVAdapter(getContext(), categories, recyclerView);

        // Устанавливаем слушатель кликов
        adapter.setOnCategoryClickListener((categoryId, subcategoryType, title) -> {
            Log.d("CategoriesFragment", "Category clicked: categoryId=" + categoryId +
                    ", type=" + subcategoryType + ", title=" + title);

            // Создаем фрагмент мест
            PlacesFragment fragment = PlacesFragment.newInstance(categoryId, subcategoryType, title);

            // Передаем управление в Activity через интерфейс
            if (getActivity() instanceof OnFragmentInteractionListener) {
                ((OnFragmentInteractionListener) getActivity()).replaceFragment(fragment);
            } else {
                Log.e("CategoriesFragment", "Activity doesn't implement OnFragmentInteractionListener");
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (databaseManager != null) {
            databaseManager.close();
        }
    }

    // Интерфейс для коммуникации с Activity
    public interface OnFragmentInteractionListener {
        void replaceFragment(Fragment fragment);
    }
}