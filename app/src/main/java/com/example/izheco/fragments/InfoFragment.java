package com.example.izheco.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.izheco.R;

public class InfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        // Находим все элементы
        TextView textVersion = view.findViewById(R.id.textVersion);
        CardView btnShareCard = view.findViewById(R.id.btnShareCard);

        // Устанавливаем версию приложения
        try {
            String versionName = requireContext().getPackageManager()
                    .getPackageInfo(requireContext().getPackageName(), 0).versionName;
            textVersion.setText("Версия приложения: " + versionName);
        } catch (Exception e) {
            textVersion.setText("Версия приложения: 1.0");
        }

        // Обработка клика на "Поделиться"
        btnShareCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "Скачивайте приложение IzhEco - в нем можно найти информацию о том, где отдать/продать/обменять ненужные вещи в Ижевске!\nhttps://clck.ru/34Ymib";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Поделиться с помощью"));
            }
        });

        return view;
    }
}