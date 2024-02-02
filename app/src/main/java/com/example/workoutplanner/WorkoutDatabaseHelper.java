package com.example.workoutplanner;

import static com.example.workoutplanner.DatabaseManager.COLUMN_NUMBER_OF_REPS;
import static com.example.workoutplanner.DatabaseManager.COLUMN_NUMBER_OF_SETS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WorkoutDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "workout_database";
    private static final int DATABASE_VERSION = 2;

    // Category table
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";

    // Exercise table
    private static final String TABLE_EXERCISES = "exercises";
    private static final String COLUMN_EXERCISE_ID = "id";
    private static final String COLUMN_EXERCISE_NAME = "name";
    private static final String COLUMN_CATEGORY_ID_FK = "category_id";

    // Constants for SELECTED_CATEGORIES table
    public static final String TABLE_SELECTED_CATEGORIES = "selected_categories";
    public static final String COLUMN_SELECTED_CATEGORY_NAME = "name";

    public WorkoutDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("WorkoutDatabaseHelper", "Creating tables...");

        // Create CATEGORIES table
        String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT NOT NULL);";
        db.execSQL(createCategoriesTable);

        // Create EXERCISES table
        String createExercisesTable = "CREATE TABLE " + TABLE_EXERCISES + " (" +
                COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE_NAME + " TEXT NOT NULL, " +
                COLUMN_CATEGORY_ID_FK + " INTEGER NOT NULL, " +
                COLUMN_NUMBER_OF_SETS + " INTEGER, " +
                COLUMN_NUMBER_OF_REPS + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CATEGORY_ID_FK + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + "));";
        db.execSQL(createExercisesTable);

        // Create SELECTED_CATEGORIES table
        String createSelectedCategoriesTable = "CREATE TABLE " + TABLE_SELECTED_CATEGORIES + " (" +
                COLUMN_SELECTED_CATEGORY_NAME + " TEXT NOT NULL);";
        db.execSQL(createSelectedCategoriesTable);

        // Log to check if the "selected_categories" table is created
        Log.d("WorkoutDatabaseHelper", "Table created: " + TABLE_SELECTED_CATEGORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_CATEGORIES);

        // Recreate the tables
        onCreate(db);
    }
}
