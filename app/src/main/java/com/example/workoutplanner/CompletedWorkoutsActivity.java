package com.example.workoutplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class CompletedWorkoutsActivity extends AppCompatActivity {

    private CompletedExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_workouts);

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCompletedExercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get completed exercises grouped by date from the database
        DatabaseManager databaseManager = new DatabaseManager(this);
        Map<String, List<String>> completedExercisesByDate = databaseManager.getCompletedExercisesGroupedByDate();

        // Set up the adapter
        adapter = new CompletedExerciseAdapter(completedExercisesByDate);
        recyclerView.setAdapter(adapter);

        // Button click listeners
        Button homeButton = findViewById(R.id.buttonHome);
        Button clearAllButton = findViewById(R.id.buttonClearAll);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to main activity
                Intent intent = new Intent(CompletedWorkoutsActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // finish this activity to prevent going back to it when pressing back from the main activity
            }
        });

        clearAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CompletedWorkoutsActivity.this);
                builder.setMessage("Are you sure you want to clear all completed workouts?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Clear all exercises
                                databaseManager.clearAllCompletedExercises();

                                // Navigate to main activity
                                Intent intent = new Intent(CompletedWorkoutsActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // finish this activity to prevent going back to it when pressing back from the main activity
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }


}
