package com.example.workoutplanner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompletedWorkoutsActivity extends AppCompatActivity {

    private List<CompletedExercise> completedExercisesList;
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
    }


}
