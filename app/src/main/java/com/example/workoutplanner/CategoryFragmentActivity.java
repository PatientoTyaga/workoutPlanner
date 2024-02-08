package com.example.workoutplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class CategoryFragmentActivity extends Fragment {

    private String categoryName;
    private DatabaseManager databaseManager;
    private boolean isRandomizing;


    public CategoryFragmentActivity(String categoryName, boolean isRandomizing) {
        this.categoryName = categoryName;
        this.isRandomizing = isRandomizing;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_category_fragment, container, false);

        // Initialize DatabaseManager
        databaseManager = new DatabaseManager(getActivity());

        try {

            databaseManager.open(); // set up connection to database

            List<Exercise> exercises;

            Map<String, List<Exercise>> randomizedCategories = databaseManager.loadRandomizedExercises(); //get categories who had their exercises randomized

            if (randomizedCategories.containsKey(categoryName)) {
                exercises = randomizedCategories.get(categoryName);
            } else {

                // reopen database if closed
                if (!databaseManager.isOpen()) {
                    databaseManager.open();
                }

                exercises = databaseManager.getExercisesForCategory(categoryName);
            }

            // Set up RecyclerView
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewExercises);

            ExerciseAdapter adapter = new ExerciseAdapter(exercises, categoryName, isRandomizing, databaseManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            // Enable swipe-to-remove
            adapter.enableSwipeToDelete(recyclerView);

            // Set the exercise removal listener
            adapter.setExerciseRemoveListener((exercise, position) -> {
                // Show a confirmation dialog or directly remove the exercise
                // If confirmed, you can remove the exercise and proceed with other actions
                // could also start completedWorkout activity here if needed in future

                // Remove the exercise from the list and update the adapter
                exercises.remove(position);
                adapter.notifyItemRemoved(position);

                // Start the CompletedWorkouts activity if needed
                startActivity(new Intent(getActivity(), CompletedWorkoutsActivity.class));
            });
        } finally {
            // Close the database in a finally block to ensure it's always closed
            databaseManager.close();
        }

        return view;
    }


}
