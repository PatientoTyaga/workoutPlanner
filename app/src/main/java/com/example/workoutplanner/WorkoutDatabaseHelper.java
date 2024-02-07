package com.example.workoutplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WorkoutDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workout_database";
    private static final int DATABASE_VERSION = 9;

    // Table names
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_EXERCISES = "exercises";
    private static final String TABLE_SELECTED_CATEGORIES = "selected_categories";
    private static final String TABLE_COMPLETED_EXERCISES = "completed_exercises";

    // Common column names
    private static final String COLUMN_ID = "id";

    // Columns for categories table
    private static final String COLUMN_CATEGORY_NAME = "name";

    // Columns for exercises table
    private static final String COLUMN_EXERCISE_NAME = "name";
    private static final String COLUMN_CATEGORY_ID_FK = "category_id";
    private static final String COLUMN_NUMBER_OF_SETS = "number_of_sets";
    private static final String COLUMN_NUMBER_OF_REPS = "number_of_reps";

    // Columns for selected_categories table
    private static final String COLUMN_SELECTED_CATEGORY_NAME = "name";
    private static final String COLUMN_IS_RANDOMIZED = "is_randomized";

    // Columns for completed_exercises table
    private static final String COLUMN_COMPLETED_EXERCISE_NAME = "exercise_name";
    private static final String COLUMN_COMPLETED_DATE = "completed_date";

    //Columns for randomized categories
    public static final String TABLE_RANDOMIZED_CATEGORY_EXERCISES = "randomized_category_exercises";
    public static final String COLUMN_RANDOMIZED_CATEGORY_NAME = "randomized_category_name";
    public static final String COLUMN_EXERCISES_JSON = "exercises_json";

    public WorkoutDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createCategoriesTable(db);
        createExercisesTable(db);
        createSelectedCategoriesTable(db);
        createCompletedExercisesTable(db);
        createRandomizedCategories(db);
    }

    public void createRandomizedCategories(SQLiteDatabase db) {
        // Existing table creation code...
        db.execSQL("CREATE TABLE " + TABLE_RANDOMIZED_CATEGORY_EXERCISES + " (" +
                COLUMN_RANDOMIZED_CATEGORY_NAME + " TEXT PRIMARY KEY," +
                COLUMN_EXERCISES_JSON + " TEXT)");
    }

    private void createCategoriesTable(SQLiteDatabase db) {
        String createCategoriesTableQuery = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT NOT NULL)";
        db.execSQL(createCategoriesTableQuery);
    }

    private void createExercisesTable(SQLiteDatabase db) {
        String createExercisesTableQuery = "CREATE TABLE " + TABLE_EXERCISES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE_NAME + " TEXT NOT NULL, " +
                COLUMN_CATEGORY_ID_FK + " INTEGER NOT NULL, " +
                COLUMN_NUMBER_OF_SETS + " INTEGER, " +
                COLUMN_NUMBER_OF_REPS + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CATEGORY_ID_FK + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_ID + "))";
        db.execSQL(createExercisesTableQuery);
    }

    private void createSelectedCategoriesTable(SQLiteDatabase db) {
        String createSelectedCategoriesTableQuery = "CREATE TABLE " + TABLE_SELECTED_CATEGORIES + " (" +
                COLUMN_SELECTED_CATEGORY_NAME + " TEXT NOT NULL, " +
                COLUMN_IS_RANDOMIZED + " INTEGER NOT NULL DEFAULT 0)";
        db.execSQL(createSelectedCategoriesTableQuery);
    }

    private void createCompletedExercisesTable(SQLiteDatabase db) {
        String createCompletedExercisesTableQuery = "CREATE TABLE " + TABLE_COMPLETED_EXERCISES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COMPLETED_EXERCISE_NAME + " TEXT NOT NULL, " +
                COLUMN_COMPLETED_DATE + " TEXT NOT NULL)";
        db.execSQL(createCompletedExercisesTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLETED_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RANDOMIZED_CATEGORY_EXERCISES); // Add this line

        // Recreate the tables
        onCreate(db);
    }

}
