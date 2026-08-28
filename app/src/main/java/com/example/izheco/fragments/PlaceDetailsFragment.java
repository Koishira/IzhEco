package com.example.izheco.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.izheco.R;
import com.example.izheco.models.Place;
import com.example.izheco.database.DatabaseManager;
import com.example.izheco.models.Category;

import java.util.List;

public class PlaceDetailsFragment extends Fragment {

    private static final String ARG_PLACE = "place";

    private Place place;
    private DatabaseManager databaseManager;

    public static PlaceDetailsFragment newInstance(Place place) {
        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLACE, place);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            place = (Place) getArguments().getSerializable(ARG_PLACE);
        }

        // Инициализация DatabaseManager для получения категории
        databaseManager = new DatabaseManager(requireContext());
        databaseManager.open();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_details, container, false);

        if (place != null) {
            // Находим все View элементы
            ImageView imageLogo = view.findViewById(R.id.imageLogo);
            TextView textName = view.findViewById(R.id.textName);
            TextView textExtraInfo = view.findViewById(R.id.textExtraInfo);

            // Находим карточки для контактной информации
            CardView extraInfoCard = view.findViewById(R.id.extraInfoCard);
            CardView contactInfoCard = view.findViewById(R.id.textContactInfo);
            CardView phoneCard = view.findViewById(R.id.phoneCard);
            CardView websiteCard = view.findViewById(R.id.websiteCard);
            CardView vkCard = view.findViewById(R.id.vkCard);

            // Находим TextViews внутри карточек
            TextView textPhone = view.findViewById(R.id.textPhone);
            TextView textWebsite = view.findViewById(R.id.textWebsite);
            TextView textVk = view.findViewById(R.id.textVk);

            // Кнопка назад
            CardView btnBackCard = view.findViewById(R.id.btnBackCard);
            btnBackCard.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            });

            // Заполняем данными
            textName.setText(place.getName());

            // Установка логотипа - С ОБНОВЛЕННОЙ ЛОГИКОЙ
            if (place.getLogoName() != null && !place.getLogoName().isEmpty()) {
                int logoResId = getResources().getIdentifier(
                        place.getLogoName(), "drawable", requireContext().getPackageName()
                );
                if (logoResId != 0) {
                    // Иконка места найдена - используем её
                    imageLogo.setImageResource(logoResId);
                    imageLogo.setVisibility(View.VISIBLE);
                } else {
                    // Иконка места не найдена - пробуем найти иконку категории
                    setCategoryIconAsFallback(imageLogo, place.getCategoryId());
                }
            } else {
                // Нет имени иконки места - используем иконку категории
                setCategoryIconAsFallback(imageLogo, place.getCategoryId());
            }

            // Проверяем, есть ли хоть какие-то контактные данные
            boolean hasContactInfo = false;

            // Дополнительная информация
            if (place.getExtraInfo() != null && !place.getExtraInfo().isEmpty() && !place.getExtraInfo().equals("none")) {
                textExtraInfo.setText(place.getExtraInfo());
                extraInfoCard.setVisibility(View.VISIBLE);
            } else {
                extraInfoCard.setVisibility(View.GONE);
            }

            // Телефон
            if (place.getPhone() != null && !place.getPhone().isEmpty() && !place.getPhone().equals("none")) {
                textPhone.setText(formatPhoneNumber(place.getPhone()));
                textPhone.setOnClickListener(v -> {
                    // Открыть звонок (раскомментируйте при необходимости)
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + place.getPhone()));
                    startActivity(intent);
                });
                phoneCard.setVisibility(View.VISIBLE);
                hasContactInfo = true;
            } else {
                phoneCard.setVisibility(View.GONE);
            }

            // Веб-сайт
            if (place.getWebsite() != null && !place.getWebsite().isEmpty() && !place.getWebsite().equals("none")) {
                textWebsite.setText(Html.fromHtml("<a href=\"" + place.getWebsite() + "\">" + place.getWebsite() + "</a>"));
                textWebsite.setMovementMethod(LinkMovementMethod.getInstance());
                websiteCard.setVisibility(View.VISIBLE);
                hasContactInfo = true;
            } else {
                websiteCard.setVisibility(View.GONE);
            }

            // ВКонтакте
            if (place.getVk() != null && !place.getVk().isEmpty() && !place.getVk().equals("none")) {
                textVk.setText(Html.fromHtml("<a href=\"" + place.getVk() + "\">" + place.getVk() + "</a>"));
                textVk.setMovementMethod(LinkMovementMethod.getInstance());
                vkCard.setVisibility(View.VISIBLE);
                hasContactInfo = true;
            } else {
                vkCard.setVisibility(View.GONE);
            }

            if ((place.getPhone() == null || place.getPhone().isEmpty() || place.getPhone().equals("none")) &&
                (place.getWebsite() == null || place.getWebsite().isEmpty() || place.getWebsite().equals("none")) &&
                (place.getVk() == null || place.getVk().isEmpty() || place.getVk().equals("none"))) {
                contactInfoCard.setVisibility(View.GONE);
            }
        }

        return view;
    }

    private void setCategoryIconAsFallback(ImageView imageView, int categoryId) {
        if (databaseManager != null) {
            // Получаем все категории
            List<Category> categories = databaseManager.getAllCategories();

            // Ищем нужную категорию по ID
            for (Category category : categories) {
                if (category.getId() == categoryId) {
                    // Нашли категорию - пробуем загрузить её иконку
                    String categoryImageName = category.getImageName();
                    if (categoryImageName != null && !categoryImageName.isEmpty()) {
                        int categoryIconResId = getResources().getIdentifier(
                                categoryImageName, "drawable", requireContext().getPackageName()
                        );
                        if (categoryIconResId != 0) {
                            // Иконка категории найдена - используем её
                            imageView.setImageResource(categoryIconResId);
                            imageView.setVisibility(View.VISIBLE);
                            return;
                        }
                    }
                    break;
                }
            }
        }

        // Если не нашли иконку категории, скрываем ImageView
        imageView.setVisibility(View.GONE);
    }

    private String formatPhoneNumber(String phone) {
        return com.example.izheco.utils.PhoneFormatter.formatPhone(phone);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем базу данных
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
}