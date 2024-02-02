
package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TodaysWorkoutActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_workout);

        // Initialize your DatabaseManager
        databaseManager = new DatabaseManager(this);

        // Retrieve the selected categories from the database
        List<String> selectedCategories = loadSelectedCategoriesFromDatabase();

        // Example: Display the selected categories in a TextView
        TextView categoriesTextView = findViewById(R.id.categoriesTextView);
        StringBuilder categoriesText = new StringBuilder("Selected Categories:\n");

        if (selectedCategories != null && !selectedCategories.isEmpty()) {
            for (String categoryName : selectedCategories) {
                // Query your database to get exercises based on categoryName
                List<String> exercises = getExercisesForCategory(categoryName);

                categoriesText.append("- ").append(categoryName).append("\n");

                if (!exercises.isEmpty()) {
                    for (String exercise : exercises) {
                        categoriesText.append("  - ").append(exercise).append("\n");
                    }
                } else {
                    categoriesText.append("  - No exercises found\n");
                }
            }
        } else {
            categoriesText.append("No categories selected.");
        }

        categoriesTextView.setText(categoriesText.toString());

    }

    // Method to retrieve selected categories from the database
    private List<String> loadSelectedCategoriesFromDatabase() {
        databaseManager.open();
        List<String> selectedCategories = databaseManager.loadSelectedCategories();
        databaseManager.close();
        return selectedCategories;
    }

    // Method to get exercises for a specific category
    private List<String> getExercisesForCategory(String categoryName) {
        List<String> exercisesList = new ArrayList<>();

        // Use the DatabaseManager to retrieve exercises for the given category
        databaseManager.open();

        // Add logging to check if the correct category is being passed
        Log.d("TodaysWorkoutActivity", "Getting exercises for category: " + categoryName);

        exercisesList = databaseManager.getExercisesForCategory(categoryName);

        // Add logging to check the result
        Log.d("TodaysWorkoutActivity", "Exercises for category " + categoryName + ": " + exercisesList);

        databaseManager.close();

        return exercisesList;
    }
}




/*
package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TodaysWorkoutActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_workout);

        // Initialize your DatabaseManager
        databaseManager = new DatabaseManager(this);

        // Retrieve the selected categories from the Intent
        ArrayList<String> selectedCategories = getIntent().getStringArrayListExtra("selectedCategories");

        // Example: Display the selected categories in a TextView
        TextView categoriesTextView = findViewById(R.id.categoriesTextView);
        StringBuilder categoriesText = new StringBuilder("Selected Categories:\n");

        if (selectedCategories != null && !selectedCategories.isEmpty()) {
            for (String categoryName : selectedCategories) {
                // Query your database to get exercises based on categoryName
                List<String> exercises = getExercisesForCategory(categoryName);

                categoriesText.append("- ").append(categoryName).append("\n");

                if (!exercises.isEmpty()) {
                    for (String exercise : exercises) {
                        categoriesText.append("  - ").append(exercise).append("\n");
                    }
                } else {
                    categoriesText.append("  - No exercises found\n");
                }
            }
        } else {
            categoriesText.append("No categories selected.");
        }

        categoriesTextView.setText(categoriesText.toString());

    }
    // Method to get exercises for a specific category
    private List<String> getExercisesForCategory(String categoryName) {
        List<String> exercisesList = new ArrayList<>();

        // Use the DatabaseManager to retrieve exercises for the given category
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.open();

        // Add logging to check if the correct category is being passed
        Log.d("TodaysWorkoutActivity", "Getting exercises for category: " + categoryName);

        exercisesList = databaseManager.getExercisesForCategory(categoryName);

        // Add logging to check the result
        Log.d("TodaysWorkoutActivity", "Exercises for category " + categoryName + ": " + exercisesList);

        databaseManager.close();

        return exercisesList;
    }

    // Method to retrieve selected categories from SharedPreferences
    private Set<String> getSelectedCategoriesFromPrefs() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return preferences.getStringSet("selectedCategories", new HashSet<>());
    }
}


 */

