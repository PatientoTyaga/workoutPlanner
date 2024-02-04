package com.example.workoutplanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryFragmentActivity extends Fragment {

    private String categoryName;
    private DatabaseManager databaseManager;

    public CategoryFragmentActivity(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_category_fragment, container, false);

        // Initialize DatabaseManager
        databaseManager = new DatabaseManager(getActivity());
        databaseManager.open();

        // Get the list of exercises for the current category
        List<String> exercises = databaseManager.getExercisesForCategory(categoryName);

        if(exercises.isEmpty()) {
            Log.d("DatabaseManager", "testing in fragment" + exercises);
        }

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewExercises);
        ExerciseAdapter adapter = new ExerciseAdapter(exercises);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Close the database
        databaseManager.close();

        return view;
    }
}