package com.example.izheco;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.izheco.adapters.PlacesRVAdapter;
import com.example.izheco.fragments.CategoriesFragment;
import com.example.izheco.fragments.InfoFragment;
import com.example.izheco.fragments.PlaceDetailsFragment;
import com.example.izheco.fragments.PlacesFragment;
import com.example.izheco.models.Place;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity
        implements CategoriesFragment.OnFragmentInteractionListener,
        PlacesRVAdapter.OnPlaceClickListener {

    private FirebaseRemoteConfig remoteConfig; // ДОБАВИЛ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ДОБАВИЛ - инициализация проверки версии
        initFirebaseRemoteConfig();

        // Настройка нижней навигации
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

            int id = item.getItemId();

            // Проверяем, не пытаемся ли перейти на ту же вкладку
            if (id == R.id.nav_categories) {
                if (currentFragment instanceof CategoriesFragment) {
                    return false;
                }
                selectedFragment = new CategoriesFragment();
            } else if (id == R.id.nav_info) {
                if (currentFragment instanceof InfoFragment) {
                    return false;
                }
                selectedFragment = new InfoFragment();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
                return true;
            }
            return false;
        });

        // Загружаем начальный фрагмент
        if (savedInstanceState == null) {
            replaceFragment(new CategoriesFragment());
            bottomNav.setSelectedItemId(R.id.nav_categories);
        }
    }

    // ДОБАВИЛ ВЕСЬ ЭТОТ МЕТОД
    private void initFirebaseRemoteConfig() {
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5) // 5 секунд для разработки
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);

        // Устанавливаем значения по умолчанию (создай файл remote_config_defaults.xml)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        // Загружаем конфигурацию
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        checkAppUpdate();
                    }
                });
    }

    // ДОБАВИЛ
    private void checkAppUpdate() {
        try {
            String newVersionCodeStr = remoteConfig.getString("new_version_code");
            int newVersionCode = Integer.parseInt(newVersionCodeStr);
            int currentVersionCode = getCurrentVersionCode();

            if (newVersionCode > currentVersionCode) {
                showUpdateDialog();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error checking update", e);
        }
    }

    // ДОБАВИЛ
    private void showUpdateDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Доступна новая версия")
                .setMessage("Обновить сейчас")
                .setPositiveButton("Обновить", (dialog, which) -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://drive.google.com/drive/folders/1RmhXAHm6i4JWdq2t3s4HP8Z0xnbi4Jue"));
                        startActivity(intent);
                    } catch (Exception e) {
                        android.widget.Toast.makeText(getApplicationContext(),
                                "Что-то пошло не так, попробуйте позже",
                                android.widget.Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Напомнить позже", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    // ДОБАВИЛ
    private int getCurrentVersionCode() {
        try {
            return getPackageManager()
                    .getPackageInfo(getPackageName(), 0)
                    .versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    // Реализация интерфейса из CategoriesFragment (ОСТАВИЛ БЕЗ ИЗМЕНЕНИЙ)
    @Override
    public void replaceFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Определяем направление анимации
        if (currentFragment instanceof InfoFragment && fragment instanceof CategoriesFragment) {
            // "О приложении" → "Категории" = возврат (справа налево)
            transaction.setCustomAnimations(
                    R.anim.slide_in_left,     // Категории появляются слева
                    R.anim.slide_out_right,   // "О приложении" уходит вправо
                    R.anim.slide_in_right,    // Для будущего возврата
                    R.anim.slide_out_left     // Для будущего возврата
            );
        } else if (currentFragment instanceof CategoriesFragment && fragment instanceof InfoFragment) {
            // "Категории" → "О приложении" = вперёд (слева направо)
            transaction.setCustomAnimations(
                    R.anim.slide_in_right,    // "О приложении" появляется справа
                    R.anim.slide_out_left,    // Категории уходят влево
                    R.anim.slide_in_left,     // Для будущего возврата
                    R.anim.slide_out_right    // Для будущего возврата
            );
        } else {
            // Для остальных переходов (детали места и т.д.) - стандартная анимация
            transaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );
        }

        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Реализация интерфейса из PlacesRVAdapter
    @Override
    public void onPlaceSelected(Place place) {
        PlaceDetailsFragment fragment = PlaceDetailsFragment.newInstance(place);
        replaceFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (currentFragment instanceof CategoriesFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}