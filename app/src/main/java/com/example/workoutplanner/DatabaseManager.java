package com.example.workoutplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final String COLUMN_IS_RANDOMIZED = "is_randomized";

    //Table and Colum names for TABLE_COMPLETED tables
    public static final String TABLE_COMPLETED_EXERCISES = "completed_exercises";
    public static final String COLUMN_COMPLETED_EXERCISE_ID = "id";
    public static final String COLUMN_COMPLETED_EXERCISE_NAME = "exercise_name";
    public static final String COLUMN_COMPLETED_DATE = "completed_date";



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

        // Clear existing exercises entries
        database.delete(TABLE_EXERCISES, null, null);

        //add predefined exercises for chest
        addExercise(new Exercise("Push ups", getCategoryId("Chest"), 2, 50));
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

        //add predefined exercises for Biceps
        addExercise(new Exercise("Push ups", getCategoryId("Biceps"), 2, 50));
        addExercise(new Exercise("Hammer curl", getCategoryId("Biceps"), 3, 10));
        addExercise(new Exercise("Bicep curl", getCategoryId("Biceps"), 3, 10));
        addExercise(new Exercise("Preacher curl", getCategoryId("Biceps"), 3, 10));
        addExercise(new Exercise("Wide-grip standing barbell curl", getCategoryId("Biceps"), 3, 10));
        addExercise(new Exercise("Dumbbell Flyes", getCategoryId("Biceps"), 3, 10));
        addExercise(new Exercise("Cable crossover", getCategoryId("Biceps"), 3, 10));
        addExercise(new Exercise("Pull ups", getCategoryId("Biceps"), 3, 10));

        //add predefined exercises for Triceps
        addExercise(new Exercise("Close grip push ups", getCategoryId("Triceps"), 2, 50));
        addExercise(new Exercise("Close grip bench press", getCategoryId("Triceps"), 3, 10));
        addExercise(new Exercise("Triceps pushdown-rope", getCategoryId("Triceps"), 3, 10));
        addExercise(new Exercise("Cable rope overhead triceps extension", getCategoryId("Triceps"), 3, 10));
        addExercise(new Exercise("Machine dip", getCategoryId("Triceps"), 3, 10));
        addExercise(new Exercise("Push up", getCategoryId("Triceps"), 3, 10));
        addExercise(new Exercise("Dips", getCategoryId("Triceps"), 3, 10));

        //add predefined exercises for Legs
        addExercise(new Exercise("Squats", getCategoryId("Legs"), 3, 10));
        addExercise(new Exercise("Deadlift", getCategoryId("Legs"), 3, 10));
        addExercise(new Exercise("Leg curl", getCategoryId("Legs"), 3, 10));
        addExercise(new Exercise("Leg press", getCategoryId("Legs"), 3, 10));
        addExercise(new Exercise("Seated leg curl", getCategoryId("Legs"), 3, 10));
        addExercise(new Exercise("Calf raises", getCategoryId("Legs"), 3, 10));
        addExercise(new Exercise("Weighted lunges", getCategoryId("Legs"), 3, 10));

        //add predefined exercises for Abs
        addExercise(new Exercise("Push ups", getCategoryId("Abs"), 2, 50));
        addExercise(new Exercise("Plank", getCategoryId("Abs"), 3, 60));
        addExercise(new Exercise("Leg raise", getCategoryId("Abs"), 3, 50));
        addExercise(new Exercise("Weighted russian twist", getCategoryId("Abs"), 3, 30));
        addExercise(new Exercise("Hanging knee raises", getCategoryId("Abs"), 3, 20));
        addExercise(new Exercise("Cable crunch", getCategoryId("Abs"), 3, 30));
        addExercise(new Exercise("Mountain climbers", getCategoryId("Abs"), 3, 60));

        //add predefined exercises for Calisthenics
        addExercise(new Exercise("Push ups", getCategoryId("Calisthenics"), 3, 50));
        addExercise(new Exercise("Weighted pull ups", getCategoryId("Calisthenics"), 3, 10));
        addExercise(new Exercise("Unweighted Pull ups", getCategoryId("Calisthenics"), 3, 15));
        addExercise(new Exercise("Weighted dips", getCategoryId("Calisthenics"), 4, 10));
        addExercise(new Exercise("Unweighted dips", getCategoryId("Calisthenics"), 4, 20));
        addExercise(new Exercise("Cable crunch", getCategoryId("Calisthenics"), 3, 30));
        addExercise(new Exercise("Weighted russian twist", getCategoryId("Calisthenics"), 3, 30));

        //add predefined exercises for Cardio
        addExercise(new Exercise("Treadmill", getCategoryId("Cardio"), 3, 10));
        addExercise(new Exercise("Weighted lunges", getCategoryId("Cardio"), 3, 10));
        addExercise(new Exercise("Exercise Bike", getCategoryId("Cardio"), 3, 10));

        close();

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

   // Refactored getExercisesForCategory method
    public List<Exercise> getExercisesForCategory(String categoryName) {
        List<Exercise> exercisesList = new ArrayList<>();
        Log.d("DatabaseManager", "CategoryName parameter: " + categoryName);

        // Query to retrieve exercise details for the given category (case-insensitive)
        String query = "SELECT " + COLUMN_EXERCISE_NAME + ", " +
                COLUMN_CATEGORY_ID_FK + ", " +
                COLUMN_NUMBER_OF_SETS + ", " +
                COLUMN_NUMBER_OF_REPS +
                " FROM " + TABLE_EXERCISES +
                " WHERE " + COLUMN_CATEGORY_ID_FK + " = (SELECT " + COLUMN_CATEGORY_ID +
                " FROM " + TABLE_CATEGORIES +
                " WHERE " + COLUMN_CATEGORY_NAME + " = ? COLLATE NOCASE)";

        // Add logging to check the SQL query being executed
        Log.d("DatabaseManager", "Executing query: " + query);

        Cursor cursor = database.rawQuery(query, new String[]{categoryName});

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(COLUMN_EXERCISE_NAME);
            int categoryIdIndex = cursor.getColumnIndex(COLUMN_CATEGORY_ID_FK);
            int setsIndex = cursor.getColumnIndex(COLUMN_NUMBER_OF_SETS);
            int repsIndex = cursor.getColumnIndex(COLUMN_NUMBER_OF_REPS);

            do {
                String name = cursor.getString(nameIndex);
                int categoryId = cursor.getInt(categoryIdIndex);
                int sets = cursor.getInt(setsIndex);
                int reps = cursor.getInt(repsIndex);

                // Create Exercise object with sets and reps
                Exercise exercise = new Exercise(name, categoryId, sets, reps);
                exercisesList.add(exercise);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // Add logging to check the result
        Log.d("DatabaseManager", "Exercises for category " + categoryName + ": " + exercisesList);

        return exercisesList;
    }


/*
    // Method to get exercises for a specific category
    public List<String> getExercisesForCategory(String categoryName) {
        List<String> exercisesList = new ArrayList<>();
        Log.d("DatabaseManager", "CategoryName parameter: " + categoryName);


        // Query to retrieve exercise names for the given category (case-insensitive)
        String query = "SELECT " + COLUMN_EXERCISE_NAME +
                " FROM " + TABLE_EXERCISES +
                " WHERE " + COLUMN_CATEGORY_ID_FK + " = (SELECT " + COLUMN_CATEGORY_ID +
                " FROM " + TABLE_CATEGORIES +
                " WHERE " + COLUMN_CATEGORY_NAME + " = ? COLLATE NOCASE)";


        // Add logging to check the SQL query being executed
        Log.d("DatabaseManager", "Executing query: " + query);

        Cursor cursor = database.rawQuery(query, new String[]{categoryName});

        String[] columnNames = cursor.getColumnNames();
        Log.d("DatabaseManager", "Cursor Columns: " + Arrays.toString(columnNames));

        if (cursor.moveToFirst()) {
            //int columnIndex = cursor.getColumnIndex(COLUMN_EXERCISE_NAME);

            int columnIndex = -1;
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                if (cursor.getColumnName(i).equalsIgnoreCase(COLUMN_EXERCISE_NAME)) {
                    columnIndex = i;
                    Log.d("DatabaseManager", "Found what i am looking for");
                    break;
                }
            }

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

 */


    //the code below is for saving selected categories
    public void saveSelectedCategories(Map<String, Boolean> selectedCategories) {
        // Open the database
        open();

        // Clear existing entries in the SELECTED_CATEGORIES table
        database.delete(TABLE_SELECTED_CATEGORIES, null, null);

        // Save the selected categories and their boolean values to the SELECTED_CATEGORIES table
        for (Map.Entry<String, Boolean> entry : selectedCategories.entrySet()) {
            String categoryName = entry.getKey();
            boolean isRandomized = entry.getValue();

            ContentValues values = new ContentValues();
            values.put(COLUMN_SELECTED_CATEGORY_NAME, categoryName);
            // Assuming you have a column named "is_selected" in SELECTED_CATEGORIES table
            values.put("is_randomized", isRandomized ? 1 : 0); // 1 for true, 0 for false
            database.insert(TABLE_SELECTED_CATEGORIES, null, values);
        }

        // Close the database
        close();
    }


    public Map<String, Boolean> loadSelectedCategories() {
        // Open the database
        open();

        // Retrieve selected categories and their boolean values from the SELECTED_CATEGORIES table
        Map<String, Boolean> selectedCategories = new HashMap<>();

        String[] columns = {COLUMN_SELECTED_CATEGORY_NAME, COLUMN_IS_RANDOMIZED}; // Assuming you have a column named "is_randomized" in SELECTED_CATEGORIES table
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
            int nameIndex = cursor.getColumnIndex(COLUMN_SELECTED_CATEGORY_NAME);
            int isRandomizedIndex = cursor.getColumnIndex(COLUMN_IS_RANDOMIZED);

            // Check if the column indices are valid
            if (nameIndex != -1 && isRandomizedIndex != -1) {
                do {
                    String categoryName = cursor.getString(nameIndex);
                    boolean isRandomized = cursor.getInt(isRandomizedIndex) == 1;

                    // Add the category and its boolean value to the map
                    selectedCategories.put(categoryName, isRandomized);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();

        // Close the database
        close();

        return selectedCategories;
    }


    // Method to remove a category and associated exercises
    public void removeCategory(String categoryName) {
        open();

        // First, get the category ID
        int categoryId = getCategoryId(categoryName);

        // Delete exercises associated with the category
        database.delete(TABLE_EXERCISES, COLUMN_CATEGORY_ID_FK + " = ?", new String[]{String.valueOf(categoryId)});

        // Delete the category from the SELECTED_CATEGORIES table
        database.delete(TABLE_SELECTED_CATEGORIES, COLUMN_SELECTED_CATEGORY_NAME + " = ?", new String[]{categoryName});

        // Delete the category from the CATEGORIES table
        database.delete(TABLE_CATEGORIES, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)});

        close();
    }

    //method to insert completed exercises into the new table
    public long addCompletedExercise(String exerciseName, String completedDate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETED_EXERCISE_NAME, exerciseName);
        values.put(COLUMN_COMPLETED_DATE, completedDate);
        return database.insert(TABLE_COMPLETED_EXERCISES, null, values);
    }




}
