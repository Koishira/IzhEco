package com.example.izheco.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.izheco.R;
import com.example.izheco.models.Place;

import java.util.List;
import java.util.Map;

public class PlacesRVAdapter extends RecyclerView.Adapter<PlacesRVAdapter.ViewHolder> {

    private Context context;
    private List<Place> places;
    private OnPlaceClickListener listener;
    private int categoryId;
    private Map<Integer, String> categoryIconsCache;

    public PlacesRVAdapter(Context context, List<Place> places, int categoryId, Map<Integer, String> categoryIconsCache) {
        this.context = context;
        this.places = places;
        this.categoryId = categoryId;
        this.categoryIconsCache = categoryIconsCache;
    }

    // Старый конструктор для обратной совместимости
    public PlacesRVAdapter(Context context, List<Place> places) {
        this(context, places, -1, null);
    }

    // Добавляем метод для установки слушателя
    public void setOnPlaceClickListener(OnPlaceClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);

        // Установка названия места
        holder.textPlaceName.setText(place.getName());

        // Установка изображения места - С ОБНОВЛЕННОЙ ЛОГИКОЙ
        String logoName = place.getLogoName();
        boolean placeIconFound = false;

        if (logoName != null && !logoName.isEmpty()) {
            int logoResId = context.getResources().getIdentifier(
                    logoName, "drawable", context.getPackageName()
            );
            if (logoResId != 0) {
                // Иконка места найдена - используем её
                holder.imagePlace.setImageResource(logoResId);
                placeIconFound = true;
            }
        }

        // Если иконка места не найдена, пробуем использовать иконку категории
        if (!placeIconFound && categoryIconsCache != null) {
            String categoryIconName = categoryIconsCache.get(categoryId);
            if (categoryIconName != null && !categoryIconName.isEmpty()) {
                int categoryIconResId = context.getResources().getIdentifier(
                        categoryIconName, "drawable", context.getPackageName()
                );
                if (categoryIconResId != 0) {
                    // Иконка категории найдена - используем как fallback
                    holder.imagePlace.setImageResource(categoryIconResId);
                    placeIconFound = true;
                }
            }
        }

        // Если ничего не найдено, можно установить дефолтную иконку
        if (!placeIconFound) {
            // Установите дефолтную иконку или скройте ImageView
            // holder.imagePlace.setImageResource(R.drawable.placeholder_logo);
            // Или скрыть: holder.imagePlace.setVisibility(View.GONE);
        }

        // Клик по элементу списка
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlaceSelected(place);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    // Изменяем интерфейс - передаем сам объект Place
    public interface OnPlaceClickListener {
        void onPlaceSelected(Place place);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePlace;
        TextView textPlaceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePlace = itemView.findViewById(R.id.imagePlace);
            textPlaceName = itemView.findViewById(R.id.textPlaceName);
        }
    }
}