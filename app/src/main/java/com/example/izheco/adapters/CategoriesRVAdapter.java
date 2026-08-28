package com.example.izheco.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.izheco.R;
import com.example.izheco.models.Category;

import java.util.List;

public class CategoriesRVAdapter extends RecyclerView.Adapter<CategoriesRVAdapter.ViewHolder> {

    private RecyclerView recyclerView;
    private Context context;
    private List<Category> categories;
    private int expandedPosition = -1;
    private OnCategoryClickListener listener;

    public CategoriesRVAdapter(Context context, List<Category> categories, RecyclerView recyclerView) {
        this.context = context;
        this.categories = categories;
        this.recyclerView = recyclerView;
    }

    // Добавляем метод для установки слушателя
    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);

        // Установка названия категории
        holder.textCategoryName.setText(category.getName());

        // Установка изображения категории
        String imageName = category.getImageName();
        int imageResId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName()
        );
        if (imageResId != 0) {
            holder.imageCategory.setImageResource(imageResId);
        }

        // Показ/скрытие опций в зависимости от категории
        boolean showOptions = category.getHasGive() == 1 ||
                category.getHasSell() == 1 ||
                category.getHasExchange() == 1;

        if (showOptions) {
            // Категория имеет подкатегории
            holder.optionGive.setVisibility(category.getHasGive() == 1 ? View.VISIBLE : View.GONE);
            holder.optionSell.setVisibility(category.getHasSell() == 1 ? View.VISIBLE : View.GONE);
            holder.optionExchange.setVisibility(category.getHasExchange() == 1 ? View.VISIBLE : View.GONE);

            // Обработка развертывания/свертывания
            boolean isExpanded = position == expandedPosition;
            // 1. СНАЧАЛА УБЕДИМСЯ, ЧТО ВСЕ ПАРАМЕТРЫ СБРОШЕНЫ
            holder.layoutOptions.setTranslationY(0f);
            holder.layoutOptions.setScaleY(1f);
            holder.layoutOptions.setAlpha(1f);

            if (isExpanded) {
                // 2. АНИМАЦИЯ РАСКРЫТИЯ ВНИЗ
                holder.layoutOptions.setVisibility(View.VISIBLE);

                // Начальное состояние: сжато и поднято НАММЕТО ВЫШЕ себя
                holder.layoutOptions.setScaleY(0f);
                holder.layoutOptions.setPivotY(0f); // Точка сжатия - верхняя граница

                // Анимация развертывания
                holder.layoutOptions.animate()
                        .scaleY(1f)
                        .setDuration(300)
                        .setInterpolator(new OvershootInterpolator(0.6f))
                        .start();

            } else {
                // 3. ЕСЛИ ЭТО БЫЛА РАСКРЫТАЯ КАТЕГОРИЯ - АНИМАЦИЯ ЗАКРЫТИЯ
                if (holder.layoutOptions.getVisibility() == View.VISIBLE) {
                    holder.layoutOptions.animate()
                            .scaleY(0f)
                            .setDuration(200)
                            .setInterpolator(new AccelerateInterpolator())
                            .withEndAction(() -> {
                                holder.layoutOptions.setVisibility(View.GONE);
                                holder.layoutOptions.setScaleY(1f); // Сбрасываем
                            })
                            .start();
                } else {
                    // Если и так скрыта - просто скрываем
                    holder.layoutOptions.setVisibility(View.GONE);
                }
            }

            // 4. ОБРАБОТКА КЛИКА С АНИМАЦИЕЙ ЗАКРЫТИЯ ПРЕДЫДУЩЕЙ
            holder.itemView.setOnClickListener(v -> {
                int previousExpanded = expandedPosition;
                expandedPosition = isExpanded ? -1 : position;

                // Если есть предыдущая раскрытая категория - закрываем её с анимацией
                if (previousExpanded != -1 && previousExpanded != position) {
                    notifyItemChanged(previousExpanded);
                }

                // Обновляем текущую
                notifyItemChanged(position);
                if (expandedPosition != -1 && recyclerView != null) {
                    recyclerView.postDelayed(() -> {
                        // Плавная прокрутка к раскрытой категории
                        recyclerView.smoothScrollToPosition(position);

                        // ИЛИ более точная прокрутка к конкретному View:
                        // View view = recyclerView.getLayoutManager().findViewByPosition(position);
                        // if (view != null) {
                        //     recyclerView.smoothScrollBy(0, view.getTop());
                        // }
                    }, 150); // Задержка 150мс, чтобы анимация раскрытия успела начаться
                }
            });

            // Клики по опциям
            holder.optionGive.setOnClickListener(v -> {
                openPlacesFragment(category.getId(), "Отдать", category.getName() + ": отдать");
            });

            holder.optionSell.setOnClickListener(v -> {
                openPlacesFragment(category.getId(), "Продать", category.getName() + ": продать");
            });

            holder.optionExchange.setOnClickListener(v -> {
                openPlacesFragment(category.getId(), "Обменять", category.getName() + ": обменять");
            });
        } else {
            // Категория не имеет подкатегорий
            holder.layoutOptions.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                openPlacesFragment(category.getId(), "none", category.getName());
            });
        }
    }

    private void openPlacesFragment(int categoryId, String subcategoryType, String title) {
        if (listener != null) {
            Log.d("CategoriesRVAdapter", "Opening places: categoryId=" + categoryId +
                    ", type=" + subcategoryType + ", title=" + title);
            listener.onCategorySelected(categoryId, subcategoryType, title);
        } else {
            Log.e("CategoriesRVAdapter", "Listener is null!");
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    // Изменяем интерфейс - упрощаем его
    public interface OnCategoryClickListener {
        void onCategorySelected(int categoryId, String subcategoryType, String title);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCategory;
        TextView textCategoryName;
        View layoutOptions;
        View optionGive, optionSell, optionExchange;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCategory = itemView.findViewById(R.id.imageCategory);
            textCategoryName = itemView.findViewById(R.id.textCategoryName);
            layoutOptions = itemView.findViewById(R.id.layoutOptions);
            optionGive = itemView.findViewById(R.id.optionGive);
            optionSell = itemView.findViewById(R.id.optionSell);
            optionExchange = itemView.findViewById(R.id.optionExchange);
            // Сбрасываем все анимации при создании ViewHolder
            itemView.clearAnimation();
            if (layoutOptions != null) {
                layoutOptions.clearAnimation();
            }
        }
    }
}