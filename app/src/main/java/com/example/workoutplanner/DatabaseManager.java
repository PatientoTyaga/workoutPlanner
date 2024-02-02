package com.example.workoutplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private SQLiteDatabase database;
    private WorkoutDatabaseHelper dbHelper;

    // Table and Column names for CATEGORIES table
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_ID = "id";
    public static final String COLUMN_CATEGORY_NAME = "name";

    // Table and Column names for EXERCISES table
    public static final String TABLE_EXERCISES = "exercises";
    public static final String COLUMN_EXERCISE_ID = "id";
    public static final String COLUMN_EXERCISE_NAME = "name";
    public static final String COLUMN_CATEGORY_ID_FK = "category_id";
    public static final String COLUMN_NUMBER_OF_SETS = "number_of_sets";
    public static final String COLUMN_NUMBER_OF_REPS = "number_of_reps";

    //Table and Column names for SELECTED_CATEGORIES table
    public static final String TABLE_SELECTED_CATEGORIES = "selected_categories";
    public static final String COLUMN_SELECTED_CATEGORY_ID = "id";
    public static final String COLUMN_SELECTED_CATEGORY_NAME = "name";

    public DatabaseManager(Context context) {
        dbHelper = new WorkoutDatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_CATEGORY_NAME, category.getName());
        return database.insert(DatabaseManager.TABLE_CATEGORIES, null, values);
    }

    public long addExercise(Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.COLUMN_EXERCISE_NAME, exercise.getName());
        values.put(DatabaseManager.COLUMN_CATEGORY_ID_FK, exercise.getCategoryId());
        values.put(DatabaseManager.COLUMN_NUMBER_OF_SETS, exercise.getNumberOfSets());
        values.put(DatabaseManager.COLUMN_NUMBER_OF_REPS, exercise.getNumberOfReps());
        return database.insert(DatabaseManager.TABLE_EXERCISES, null, values);
    }

    public void initializeDatabase() {

        //Open the database
        open();

        //add categories
        addCategory(new Category("Chest"));
        addCategory(new Category("Back"));
        addCategory(new Category("Shoulders"));
        addCategory(new Category("Biceps"));
        addCategory(new Category("Triceps"));
        addCategory(new Category("Legs"));
        addCategory(new Category("Abs"));
        addCategory(new Category("Calisthenics"));
        addCategory(new Category("Cardio"));

        //add predefined exercises for chest
        addExercise(new Exercise("Push ups", getCategoryId("Chest"), 3, 10));
        addExercise(new Exercise("Inclined Bench Press", getCategoryId("Chest"), 3, 10));
        addExercise(new Exercise("Dumbbell bench press", getCategoryId("Chest"), 3, 10));
        addExercise(new Exercise("Dumbbell Flyes", getCategoryId("Chest"), 3, 10));
        addExercise(new Exercise("Cable crossover", getCategoryId("Chest"), 3, 10));
        addExercise(new Exercise("Low cable crossover", getCategoryId("Chest"), 3, 10));
        addExercise(new Exercise("Machine fly", getCategoryId("Chest"), 3, 10));
        addExercise(new Exercise("Dip", getCategoryId("Chest"), 3, 10));

        //add predefined exercises for back
        addExercise(new Exercise("Pull ups", getCategoryId("Back"), 3, 10));
        addExercise(new Exercise("Bent-over row", getCategoryId("Back"), 3, 10));
        addExercise(new Exercise("Lat pull downs", getCategoryId("Back"), 3, 10));
        addExercise(new Exercise("Seated cable rows", getCategoryId("Back"), 3, 10));
        addExercise(new Exercise("One-arm dumbbell row", getCategoryId("Back"), 3, 10));
        addExercise(new Exercise("Straight-arm pull down", getCategoryId("Back"), 3, 10));
        addExercise(new Exercise("Face pull", getCategoryId("Back"), 3, 10));
        addExercise(new Exercise("Deadlift", getCategoryId("Back"), 3, 10));

        //add predefined exercises for Shoulders
        addExercise(new Exercise("Dive bomber push-up", getCategoryId("Shoulders"), 3, 10));
        addExercise(new Exercise("Seated dumbbell press", getCategoryId("Shoulders"), 3, 10));
        addExercise(new Exercise("Side lateral raise", getCategoryId("Shoulders"), 3, 10));
        addExercise(new Exercise("Front raise", getCategoryId("Shoulders"), 3, 10));
        addExercise(new Exercise("Face pull", getCategoryId("Shoulders"), 3, 10));
        addExercise(new Exercise("Overhead press (smith machine or not)", getCategoryId("Shoulders"), 3, 10));
        addExercise(new Exercise("shoulder shrug", getCategoryId("Shoulders"), 3, 10));
        addExercise(new Exercise("Dips", getCategoryId("Shoulders"), 3, 10));

    }

    private int getCategoryId(String categoryName) {
        String[] columns = {DatabaseManager.COLUMN_CATEGORY_ID};
        String selection = DatabaseManager.COLUMN_CATEGORY_NAME + " = ?";
        String[] selectionArgs = {categoryName};

        Cursor cursor = database.query(
                DatabaseManager.TABLE_CATEGORIES,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int categoryId = -1;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DatabaseManager.COLUMN_CATEGORY_ID);

            // Check if the column index is valid
            if (columnIndex != -1) {
                categoryId = cursor.getInt(columnIndex);
            }
        }

        cursor.close();

        return categoryId;
    }

    // Method to get exercises for a specific category
    public List<String> getExercisesForCategory(String categoryName) {
        List<String> exercisesList = new ArrayList<>();

        // Query to retrieve exercise names for the given category (case-insensitive)
        String query = "SELECT " + COLUMN_EXERCISE_NAME +
                " FROM " + TABLE_EXERCISES +
                " WHERE " + "LOWER(" + COLUMN_CATEGORY_ID_FK + ") = (SELECT " + COLUMN_CATEGORY_ID +
                " FROM " + TABLE_CATEGORIES +
                " WHERE LOWER(" + COLUMN_CATEGORY_NAME + ") = ?)";

        // Add logging to check the SQL query being executed
        Log.d("DatabaseManager", "Executing query: " + query);

        Cursor cursor = database.rawQuery(query, new String[]{categoryName});

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_EXERCISE_NAME);

            // Check if the column index is valid
            if (columnIndex != -1) {
                do {
                    String exerciseName = cursor.getString(columnIndex);
                    exercisesList.add(exerciseName);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();

        // Add logging to check the result
        Log.d("DatabaseManager", "Exercises for category " + categoryName + ": " + exercisesList);

        return exercisesList;
    }

    //the code below is for saving selected categories
    public void saveSelectedCategories(List<String> selectedCategories) {
        // Open the database
        open();

        // Clear existing entries in the SELECTED_CATEGORIES table
        database.delete(TABLE_SELECTED_CATEGORIES, null, null);

        // Save the selected categories to the SELECTED_CATEGORIES table
        for (String categoryName : selectedCategories) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_SELECTED_CATEGORY_NAME, categoryName);
            database.insert(TABLE_SELECTED_CATEGORIES, null, values);
        }

        // Close the database
        close();
    }

    public List<String> loadSelectedCategories() {
        // Open the database
        open();

        // Retrieve selected categories from the SELECTED_CATEGORIES table
        List<String> selectedCategories = new ArrayList<>();

        String[] columns = {COLUMN_SELECTED_CATEGORY_NAME};
        Cursor cursor = database.query(
                TABLE_SELECTED_CATEGORIES,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_SELECTED_CATEGORY_NAME);

            // Check if the column index is valid
            if (columnIndex != -1) {
                do {
                    String categoryName = cursor.getString(columnIndex);
                    selectedCategories.add(categoryName);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();

        // Close the database
        close();

        return selectedCategories;
    }
}
