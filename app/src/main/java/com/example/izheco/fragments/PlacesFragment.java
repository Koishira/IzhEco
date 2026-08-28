package com.example.izheco.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.izheco.R;
import com.example.izheco.adapters.PlacesRVAdapter;
import com.example.izheco.database.DatabaseManager;
import com.example.izheco.models.Category;
import com.example.izheco.models.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlacesFragment extends Fragment {

    private static final String TAG = "PlacesFragment";

    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String ARG_SUBCATEGORY_TYPE = "subcategory_type";
    private static final String ARG_TITLE = "title";

    private RecyclerView recyclerView;
    private TextView textTitle;
    private DatabaseManager databaseManager;
    private PlacesRVAdapter adapter;
    private Map<Integer, String> categoryIconsCache = new HashMap<>(); // Кэш иконок категорий

    // Добавляем переменные класса
    private int categoryId = -1;
    private String subcategoryType = "";
    private String title = "";

    public static PlacesFragment newInstance(int categoryId, String subcategoryType, String title) {
        PlacesFragment fragment = new PlacesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        args.putString(ARG_SUBCATEGORY_TYPE, subcategoryType);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");

        // Получаем аргументы в onCreate
        Bundle args = getArguments();
        if (args != null) {
            categoryId = args.getInt(ARG_CATEGORY_ID, -1);
            subcategoryType = args.getString(ARG_SUBCATEGORY_TYPE, "");
            title = args.getString(ARG_TITLE, "");

            Log.d(TAG, "Received arguments: categoryId=" + categoryId +
                    ", subcategoryType=" + subcategoryType + ", title=" + title);
        } else {
            Log.e(TAG, "No arguments found!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        Log.d(TAG, "onCreateView called");

        textTitle = view.findViewById(R.id.textTitle);
        recyclerView = view.findViewById(R.id.recyclerViewPlaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Кнопка назад
        CardView btnBackCard = view.findViewById(R.id.btnBackCard);
        btnBackCard.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Устанавливаем заголовок
        if (!title.isEmpty()) {
            textTitle.setText(title);
        } else {
            textTitle.setText("Места");
        }

        // Проверяем, есть ли необходимые данные
        if (categoryId == -1 || subcategoryType.isEmpty()) {
            Log.e(TAG, "Missing required data: categoryId=" + categoryId + ", subcategoryType=" + subcategoryType);
            Toast.makeText(getContext(), "Ошибка: отсутствуют данные", Toast.LENGTH_SHORT).show();
            return view;
        }

        Log.d(TAG, "Loading places for categoryId=" + categoryId + ", subcategoryType=" + subcategoryType);

        try {
            // Загрузка данных из базы
            databaseManager = new DatabaseManager(getContext());
            databaseManager.open();

            // Загружаем иконку категории в кэш
            loadCategoryIconToCache();

            List<Place> places = databaseManager.getPlacesByCategoryAndType(categoryId, subcategoryType);

            Log.d(TAG, "Loaded " + places.size() + " places");

            if (places.isEmpty()) {
                Log.w(TAG, "No places found for categoryId=" + categoryId + ", type=" + subcategoryType);
                Toast.makeText(getContext(), "Нет мест для отображения", Toast.LENGTH_SHORT).show();
                places = new ArrayList<>();
            }

            // Инициализация адаптера с передачей categoryId и кэша иконок
            adapter = new PlacesRVAdapter(getContext(), places, categoryId, categoryIconsCache);

            // Устанавливаем слушатель кликов
            adapter.setOnPlaceClickListener(place -> {
                Log.d(TAG, "Place selected: " + place.getName());

                // Создаем фрагмент деталей
                PlaceDetailsFragment fragment = PlaceDetailsFragment.newInstance(place);

                // Передаем управление в Activity через интерфейс
                if (getActivity() instanceof CategoriesFragment.OnFragmentInteractionListener) {
                    ((CategoriesFragment.OnFragmentInteractionListener) getActivity()).replaceFragment(fragment);
                } else {
                    Log.e(TAG, "Activity doesn't implement OnFragmentInteractionListener");
                }
            });

            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            Log.e(TAG, "Error loading places", e);
            Toast.makeText(getContext(), "Ошибка загрузки данных: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            // Создаем пустой адаптер, чтобы избежать краша
            adapter = new PlacesRVAdapter(getContext(), new ArrayList<>(), categoryId, categoryIconsCache);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    /**
     * Загружает иконку категории в кэш
     */
    private void loadCategoryIconToCache() {
        if (databaseManager != null) {
            List<Category> categories = databaseManager.getAllCategories();
            for (Category category : categories) {
                categoryIconsCache.put(category.getId(), category.getImageName());
            }
            Log.d(TAG, "Loaded " + categoryIconsCache.size() + " category icons to cache");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView called");

        if (databaseManager != null) {
            databaseManager.close();
            databaseManager = null;
        }

        if (adapter != null) {
            adapter.setOnPlaceClickListener(null);
        }

        // Очищаем кэш
        categoryIconsCache.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }
}