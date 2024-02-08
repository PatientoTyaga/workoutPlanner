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

    // declare variables to be used
    private boolean todaysWorkoutHasCategories = false;
    private boolean completedWorkoutHasEntries = false;
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

        //set click listener for customWorkout card. when clicked, go to CustomWorkoutActivity class
        customWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomWorkoutActivity.class);
                startActivity(intent);
            }
        });

        // Check if TodaysWorkout has categories and enable the card if true
        if (todaysWorkoutHasCategories) {
            enableCard(todaysWorkout, TodaysWorkoutActivity.class);
        }

        // Check if completedWorkout page has data in it. if so, make card clickable
        if(completedWorkoutHasEntries) {
            enableCard(completedWorkout, CompletedWorkoutsActivity.class);
        }

    }


    // takes in a class and enables the card associated with that class and starts that class' activity
    private void enableCard(CardView cardView, final Class<?> destinationActivity) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, destinationActivity);
                startActivity(intent);
            }
        });
    }

    // used to grey out card
    private void disableCard(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.greyed_out_color));
        cardView.setOnClickListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if TodaysWorkout has categories and enable or disable the card accordingly
        updateTodaysWorkoutCardStatus();
        updateCompletedWorkoutCardStatus();
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
            enableCard(todaysWorkout, TodaysWorkoutActivity.class);
        } else {
            disableCard(todaysWorkout);
        }
    }

    private void updateCompletedWorkoutCardStatus() {
        // Retrieve the selected categories from the database
        Map<String, List<String>> completedWorkouts = databaseManager.getCompletedExercisesGroupedByDate();

        // Set completedWorkoutHasEntries based on whether there are entries in completed workout
        completedWorkoutHasEntries = !completedWorkouts.isEmpty();

        // Find the CardView by its ID
        CardView completedWorkout = findViewById(R.id.completedWorkouts);

        // Check if CompletedWorkout has categories and enable or disable the card accordingly
        if (completedWorkoutHasEntries) {
            enableCard(completedWorkout, CompletedWorkoutsActivity.class);
        } else {
            disableCard(completedWorkout);
        }
    }



}