
package com.example.workoutplanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class TodaysWorkoutActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private List<String> selectedCategories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_workout);

        // Initialize your DatabaseManager
        databaseManager = new DatabaseManager(this);

        // Retrieve the selected categories from the database
        selectedCategories = loadSelectedCategoriesFromDatabase();

        //Display the selected categories in a TextView
        displaySelectedCategories(selectedCategories);

        // Set up the "Remove Workout" button click listener
        MaterialButton removeWorkoutButton = findViewById(R.id.removeWorkoutButton);
        removeWorkoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //ask user first if they want to proceed with deletion
                showConfirmationDialog();
            }
        });

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

    // Method to display selected categories in the UI
    private void displaySelectedCategories(List<String> selectedCategories) {
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
                    //categoriesText.append("  - No exercises found\n");
                }
            }
        } else {
            //categoriesText.append("No categories selected.");
        }

        categoriesTextView.setText(categoriesText.toString());
    }

    // Method to remove all selected categories from the database
    private void removeSelectedCategoriesFromDatabase() {
        databaseManager.open();

        for (String categoryName : selectedCategories) {
            databaseManager.removeCategory(categoryName);
        }

        databaseManager.close();
    }

    //asking user if they are sure they want to proceed with deletion
    //asking user if they are sure they want to proceed with deletion
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete the workout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked Yes, proceed with removing the workout
                removeSelectedCategoriesFromDatabase();

                // Navigate back to the main page
                Intent intent = new Intent(TodaysWorkoutActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  // Finish the current activity to prevent going back to it when pressing back
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked No, do nothing or provide feedback if needed
            }
        });

        // Show the dialog
        builder.show();
    }

}