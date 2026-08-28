package com.example.izheco.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.izheco.models.Category;
import com.example.izheco.models.Place;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        try {
            // Создаем/копируем базу при необходимости
            dbHelper.createDatabase();
            // Открываем базу
            database = dbHelper.openDatabase();
        } catch (Exception e) {
            // Если не удалось открыть из assets, создаем пустую
            database = dbHelper.getWritableDatabase();
            dbHelper.createTables(database);
        }
    }

    public void close() {
        dbHelper.close();
    }

    // Получение всех категорий
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_CATEGORIES +
                " ORDER BY " + DatabaseHelper.COLUMN_NAME + " ASC";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                category.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)));
                category.setImageName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_NAME)));
                category.setHasGive(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HAS_GIVE)));
                category.setHasSell(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HAS_SELL)));
                category.setHasExchange(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HAS_EXCHANGE)));

                categories.add(category);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return categories;
    }

    // Получение мест по категории и типу подкатегории
    public List<Place> getPlacesByCategoryAndType(int categoryId, String subcategoryType) {
        List<Place> places = new ArrayList<>();

        // Используем точные имена столбцов из вашей базы
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PLACES +
                " WHERE " + DatabaseHelper.COLUMN_CATEGORY_ID + " = ?" +
                " AND " + DatabaseHelper.COLUMN_SUBCATEGORY_TYPE + " = ?" +
                " ORDER BY place_name ASC";  // Изменено на place_name

        String[] selectionArgs = {String.valueOf(categoryId), subcategoryType};

        Log.d("DatabaseManager", "Query: " + query);
        Log.d("DatabaseManager", "Args: categoryId=" + categoryId + ", subcategoryType=" + subcategoryType);

        Cursor cursor = null;
        try {
            cursor = database.rawQuery(query, selectionArgs);

            if (cursor != null) {
                Log.d("DatabaseManager", "Cursor count: " + cursor.getCount());

                // Логируем имена столбцов для отладки
                String[] columnNames = cursor.getColumnNames();
                Log.d("DatabaseManager", "Available columns:");
                for (String columnName : columnNames) {
                    Log.d("DatabaseManager", " - " + columnName);
                }

                if (cursor.moveToFirst()) {
                    do {
                        Place place = new Place();

                        // Используем безопасные методы получения данных
                        try {
                            place.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                            place.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID)));
                            place.setSubcategoryType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBCATEGORY_TYPE)));

                            // Пробуем оба возможных имени столбца
                            String placeName;
                            try {
                                placeName = cursor.getString(cursor.getColumnIndexOrThrow("place_name"));
                            } catch (IllegalArgumentException e) {
                                // Пробуем другое возможное имя
                                placeName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                            }
                            place.setName(placeName);

                            place.setLogoName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOGO_NAME)));
                            place.setExtraInfo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXTRA_INFO)));
                            place.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE)));
                            place.setWebsite(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WEBSITE)));
                            place.setVk(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VK)));

                            places.add(place);
                            Log.d("DatabaseManager", "Loaded place: " + place.getName());

                        } catch (Exception e) {
                            Log.e("DatabaseManager", "Error reading place data", e);
                        }
                    } while (cursor.moveToNext());
                } else {
                    Log.d("DatabaseManager", "No places found for categoryId=" + categoryId + ", type=" + subcategoryType);
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error executing query", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d("DatabaseManager", "Total places loaded: " + places.size());
        return places;
    }
}