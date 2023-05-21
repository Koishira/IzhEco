package com.example.izheco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.izheco.update.AppUpdate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseRemoteConfig remoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.category:
                        selectedFragment = new CategoriesFragment();
                        break;
//                    case R.id.favourites:
//                        selectedFragment = new FavouritesFragment();
//                        break;
                    case R.id.info:
                        selectedFragment = new InfoFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesFragment()).commit();

        int currentVersionCode;
        currentVersionCode = getCurrentVersionCode();
        Log.d("IzhEco", String.valueOf(currentVersionCode));
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    final String new_version_code = remoteConfig.getString("new_version_code");
                    if (Integer.parseInt(new_version_code) > getCurrentVersionCode()) {
                        showUpdateDialog();
                    }
                }
            }
        });
    }

//    https://qr-code-styling.com
    private void showUpdateDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Доступна новая версия")
                .setMessage("Обновить сейчас")
                .setPositiveButton("Обновить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/drive/folders/1RmhXAHm6i4JWdq2t3s4HP8Z0xnbi4Jue")));
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Что-то пошло не так, попробуйте позже", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Напомнить позже", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
        dialog.setCancelable(false);
    }

    private int getCurrentVersionCode() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {
            Log.d("IzhEco", e.getMessage());
        }
        return packageInfo.versionCode;
    }
}