package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private boolean todaysWorkoutHasCategories = false;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseManager
        databaseManager = new DatabaseManager(this);
        databaseManager.initializeDatabase();

        // Find the CardViews by their IDs
        CardView todaysWorkout = findViewById(R.id.todaysWorkoutCard);
        CardView completedWorkout = findViewById(R.id.completedWorkouts);
        CardView customWorkout = findViewById(R.id.customWorkout);
        CardView randomizeWorkout = findViewById(R.id.randomizeWorkout);

        // Retrieve the selected categories
        List<String> selectedCategories = getSelectedCategories();

        // Set todaysWorkoutHasCategories based on whether there are selected categories
        todaysWorkoutHasCategories = !selectedCategories.isEmpty();

        //initially grey out todaysWorkout and completedWorkout cards
        disableCard(todaysWorkout);
        disableCard(completedWorkout);

        //setting click listeners for the card views
        randomizeWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RandomizeWorkoutActivity.class);
                startActivity(intent);
            }
        });

        customWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomWorkoutActivity.class);
                startActivity(intent);
            }
        });

        // Check if TodaysWorkout has categories and enable the card if true
        if (todaysWorkoutHasCategories) {
            enableCard(todaysWorkout);
        }

    }


    // Assume you have a method to get the selected categories
    private List<String> getSelectedCategories() {
        // Retrieve selected categories from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Set<String> selectedCategoriesSet = preferences.getStringSet("selectedCategories", null);

        // Convert Set to List
        if (selectedCategoriesSet != null) {
            return new ArrayList<>(selectedCategoriesSet);
        } else {
            return new ArrayList<>();
        }
    }

    private void enableCard(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TodaysWorkoutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void disableCard(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.greyed_out_color));
        cardView.setOnClickListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if TodaysWorkout has categories and enable or disable the card accordingly
        updateTodaysWorkoutCardStatus();
    }

    private void updateTodaysWorkoutCardStatus() {
        // Retrieve the selected categories from the database
        Map<String, Boolean> selectedCategories = databaseManager.loadSelectedCategories();

        // Set todaysWorkoutHasCategories based on whether there are selected categories
        todaysWorkoutHasCategories = !selectedCategories.isEmpty();

        // Find the CardView by its ID
        CardView todaysWorkout = findViewById(R.id.todaysWorkoutCard);

        // Check if TodaysWorkout has categories and enable or disable the card accordingly
        if (todaysWorkoutHasCategories) {
            enableCard(todaysWorkout);
        } else {
            disableCard(todaysWorkout);
        }
    }



}