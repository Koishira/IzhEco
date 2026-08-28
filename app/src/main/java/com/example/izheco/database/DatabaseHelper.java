package com.example.izheco.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper"; // Добавьте этот тег
    private static final String DATABASE_NAME = "izheco.db";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = "";
    private final Context context;
    private SQLiteDatabase database;

    // Таблица категорий
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE_NAME = "image_name";
    public static final String COLUMN_HAS_GIVE = "has_give";
    public static final String COLUMN_HAS_SELL = "has_sell";
    public static final String COLUMN_HAS_EXCHANGE = "has_exchange";

    // Таблица мест - ИСПРАВЛЕНО!
    public static final String TABLE_PLACES = "places";
    public static final String COLUMN_PLACE_ID = "id"; // ИЗМЕНЕНО С place_id на id
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_SUBCATEGORY_TYPE = "subcategory_type";
    public static final String COLUMN_PLACE_NAME = "place_name"; // или "name", проверьте вашу базу
    public static final String COLUMN_LOGO_NAME = "logo_name";
    public static final String COLUMN_EXTRA_INFO = "extra_info";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_WEBSITE = "website";
    public static final String COLUMN_VK = "vk";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
    }

    // Создание базы, если её нет
    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();

        if (!dbExist) {
            // Создаем пустую базу
            this.getReadableDatabase();
            this.close();

            try {
                // Копируем базу из assets
                copyDatabase();
                Log.d(TAG, "Database copied from assets");
            } catch (IOException e) {
                Log.e(TAG, "Error copying database from assets", e);
                throw new Error("Error copying database from assets");
            }
        } else {
            Log.d(TAG, "Database already exists");
        }
    }

    // Проверка существования базы
    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;

        try {
            String path = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Log.d(TAG, "Database exists at: " + path);
        } catch (SQLException e) {
            Log.e(TAG, "Database doesn't exist or can't be opened", e);
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    // Копирование базы из assets
    private void copyDatabase() throws IOException {
        // Открываем локальную БД как поток
        InputStream input = context.getAssets().open("databases/" + DATABASE_NAME);
        Log.d(TAG, "Opening database from assets");

        // Путь к созданной БД
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        Log.d(TAG, "Output file: " + outFileName);

        // Создаем директорию если её нет
        new java.io.File(DATABASE_PATH).mkdirs();

        // Открываем пустую БД
        OutputStream output = new FileOutputStream(outFileName);

        // Копируем байт за байтом
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        // Закрываем потоки
        output.flush();
        output.close();
        input.close();

        Log.d(TAG, "Database copied successfully");
    }

    // Открытие базы данных
    public SQLiteDatabase openDatabase() throws SQLException {
        String path = DATABASE_PATH + DATABASE_NAME;
        Log.d(TAG, "Opening database at: " + path);
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

        // Логируем структуру таблиц для отладки
        logTableStructure();

        return database;
    }

    // Метод для логирования структуры таблиц
    private void logTableStructure() {
        try {
            // Логируем структуру таблицы places
            android.database.Cursor cursor = database.rawQuery("PRAGMA table_info(places)", null);
            if (cursor != null) {
                Log.d(TAG, "=== Structure of 'places' table ===");
                while (cursor.moveToNext()) {
                    String columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String columnType = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                    Log.d(TAG, "Column: " + columnName + " | Type: " + columnType);
                }
                cursor.close();
            }

            // Логируем структуру таблицы categories
            cursor = database.rawQuery("PRAGMA table_info(categories)", null);
            if (cursor != null) {
                Log.d(TAG, "=== Structure of 'categories' table ===");
                while (cursor.moveToNext()) {
                    String columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String columnType = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                    Log.d(TAG, "Column: " + columnName + " | Type: " + columnType);
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error logging table structure", e);
        }
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate called");
        createTables(db);
    }

    public void createTables(SQLiteDatabase db) {
        // Создание таблицы категорий
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_IMAGE_NAME + " TEXT NOT NULL,"
                + COLUMN_HAS_GIVE + " INTEGER DEFAULT 0,"
                + COLUMN_HAS_SELL + " INTEGER DEFAULT 0,"
                + COLUMN_HAS_EXCHANGE + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_CATEGORIES_TABLE);
        Log.d(TAG, "Categories table created");

        // Создание таблицы мест - ИСПРАВЛЕНО в соответствии с вашей SQL-схемой
        String CREATE_PLACES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PLACES + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"  // Изменено с COLUMN_PLACE_ID на id
                + COLUMN_CATEGORY_ID + " INTEGER NOT NULL,"
                + COLUMN_SUBCATEGORY_TYPE + " TEXT NOT NULL,"
                + "place_name TEXT NOT NULL,"  // Используем точное имя из вашей схемы
                + COLUMN_LOGO_NAME + " TEXT NOT NULL,"
                + COLUMN_EXTRA_INFO + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_WEBSITE + " TEXT,"
                + COLUMN_VK + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES "
                + TABLE_CATEGORIES + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_PLACES_TABLE);
        Log.d(TAG, "Places table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        createTables(db);
    }
}