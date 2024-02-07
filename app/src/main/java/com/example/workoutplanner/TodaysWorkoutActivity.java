
package com.example.workoutplanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TodaysWorkoutActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private Map<String, Boolean> selectedCategories;
    private Map<String, List<Exercise>> randomizedCategories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_workout);

        // Initialize your DatabaseManager
        databaseManager = new DatabaseManager(this);

        // Retrieve the selected categories from the database
        selectedCategories = loadSelectedCategoriesFromDatabase();

        //Retrieve the randomizedCategories
        randomizedCategories = loadRandomizedCategoriesFromDatabase();

        // Retrieve the boolean indicating randomization
        //boolean isRandomizing = getIntent().getBooleanExtra("isRandomizing", false);

        // Set up tabs and view pager
        setUpTabsAndViewPager(selectedCategories, randomizedCategories);

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

    // Method to set up TabLayout and ViewPager
    private void setUpTabsAndViewPager(Map<String, Boolean> selectedCategories, Map<String, List<Exercise>> randomizedCategories) {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        // Create an adapter for the ViewPager
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), selectedCategories, randomizedCategories);

        // Set up the ViewPager with the adapter
        viewPager.setAdapter(pagerAdapter);

        // Connect the TabLayout to the ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    // Method to retrieve selected categories from the database
    private Map<String, Boolean> loadSelectedCategoriesFromDatabase() {
        databaseManager.open();
        Map<String, Boolean> selectedCategories = databaseManager.loadSelectedCategories();
        databaseManager.close();
        return selectedCategories;
    }

    // Method to retrieve randomizedCategories from the database
    private Map<String, List<Exercise>> loadRandomizedCategoriesFromDatabase() {
        databaseManager.open();
        Map<String, List<Exercise>> randomizedCategories = databaseManager.loadRandomizedExercises();
        databaseManager.close();
        return randomizedCategories;
    }

    // Method to remove all selected categories from the database
    private void removeSelectedCategoriesFromDatabase() {
        databaseManager.open();

        for (Map.Entry<String, Boolean> entry : selectedCategories.entrySet()) {
            String categoryName = entry.getKey();
            databaseManager.removeCategory(categoryName);
        }

        databaseManager.close();
    }

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