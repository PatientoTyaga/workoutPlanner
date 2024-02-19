
package com.example.workoutplanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TodaysWorkoutActivity extends AppCompatActivity implements ExerciseAdapter.ExerciseRemoveListener{

    //declare variables to be used
    private DatabaseManager databaseManager;
    private Map<String, Boolean> selectedCategories; // for categories that were added
    private ViewPager viewPager;
    private TabPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_workout);

        // Initialize your DatabaseManager
        databaseManager = new DatabaseManager(this);

        // Retrieve the selected categories from the database
        selectedCategories = loadSelectedCategoriesFromDatabase();

        // Set up tabs and view pager
        setUpTabsAndViewPager(selectedCategories);

        // Set up the "Remove Workout" button click listener
        Button removeWorkoutButton = findViewById(R.id.removeWorkoutButton);

        // Find the home button view
        Button homeButton = findViewById(R.id.homeButton);

        removeWorkoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //ask user first if they want to proceed with deletion
                showConfirmationDialog();
            }
        });

        // Set OnClickListener on the home button
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to the main activity
                Intent intent = new Intent(TodaysWorkoutActivity.this, MainActivity.class);
                startActivity(intent); // Start the activity
                finish(); // Finish the current activity
            }
        });

    }

    // Method to set up TabLayout and ViewPager
    private void setUpTabsAndViewPager(Map<String, Boolean> selectedCategories) {

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),this, selectedCategories, databaseManager);

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
                // User clicked No, do nothing
            }
        });

        // Show the dialog
        builder.show();
    }

    @Override
    public void onExerciseRemoved(String categoryName) {
        onCategoryEmpty(categoryName);
    }


    public void onCategoryEmpty(String categoryName) {

        // Check if there are more tabs available
        int currentTabIndex = viewPager.getCurrentItem();
        int totalTabs = pagerAdapter.getCount();
        // Clear any references or state related to the fragment

        if (currentTabIndex < totalTabs - 1) {
            //viewPager.setCurrentItem(currentTabIndex + 1);

            pagerAdapter.removeTab(currentTabIndex, false);

            // Switch to the next tab

        } else {

            pagerAdapter.removeTab(currentTabIndex, true);
            // No more tabs, navigate back to the main activity if needed
            if (pagerAdapter.getCount() == 0) {
                Intent intent = new Intent(TodaysWorkoutActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void updateData(Map<String, Boolean> selectedCategories) {
        // Recreate the ViewPager and its adapter with the updated data
        setUpTabsAndViewPager(selectedCategories);
    }




}