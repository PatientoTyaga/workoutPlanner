package com.example.workoutplanner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CompletedWorkoutsActivity extends AppCompatActivity {

    private List<CompletedExercise> completedExercisesList;
    private CompletedExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_workouts);

        // Initialize the list
        if(completedExercisesList == null) {
            completedExercisesList = new ArrayList<>();
        }

        // Set up the RecyclerView and its adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCompletedExercises);
        adapter = new CompletedExerciseAdapter(completedExercisesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get data from Intent extras
        String exerciseName = getIntent().getStringExtra("exerciseName");
        String dayCompleted = getIntent().getStringExtra("dayCompleted");

        // Check if both extras are present
        if (exerciseName != null && dayCompleted != null) {
            // Add the completed exercise
            addCompletedExercise(exerciseName, dayCompleted);
        }

    }

    // Method to add a completed exercise to the list
    private void addCompletedExercise(String exerciseName, String dayCompleted) {
        CompletedExercise completedExercise = new CompletedExercise(exerciseName, dayCompleted);
        completedExercisesList.add(completedExercise);

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
    }
}
