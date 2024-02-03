package com.example.workoutplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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

        // Display the list of exercises in your UI (e.g., using a RecyclerView)

        // Close the database
        databaseManager.close();

        return view;
    }
}
