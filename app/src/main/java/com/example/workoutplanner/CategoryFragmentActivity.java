package com.example.workoutplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class CategoryFragmentActivity extends Fragment {

    private static final String ARG_CATEGORY_NAME = "category_name";

    private String categoryName;
    private DatabaseManager databaseManager;
    private boolean isRandomizing;

    private ExerciseAdapter adapter; // Hold an instance of ExerciseAdapter


    // Default constructor
    public CategoryFragmentActivity() {
        // Required empty public constructor
    }

    // Factory method to create a new instance of the fragment with arguments
    public static CategoryFragmentActivity newInstance(String categoryName, boolean isRandomizing) {
        CategoryFragmentActivity fragment = new CategoryFragmentActivity();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        args.putBoolean("is_randomizing", isRandomizing);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString(ARG_CATEGORY_NAME);
            isRandomizing = getArguments().getBoolean("is_randomizing");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_category_fragment, container, false);

        // Update UI with the correct category name
        if (getArguments() != null) {
            categoryName = getArguments().getString(ARG_CATEGORY_NAME);
        }

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

            adapter = new ExerciseAdapter(exercises, categoryName, isRandomizing, databaseManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


            adapter.setExerciseRemoveListener(new ExerciseAdapter.ExerciseRemoveListener() {
                @Override
                public void onExerciseRemoved(String categoryName) {
                    // Call the onExerciseRemoved method in TodaysWorkoutActivity
                    TodaysWorkoutActivity activity = (TodaysWorkoutActivity) getActivity();
                    if (activity != null) {
                        activity.onExerciseRemoved(categoryName);
                    }
                }
            });

            // Enable swipe-to-remove
            adapter.enableSwipeToDelete(recyclerView);


        } finally {
            // Close the database in a finally block to ensure it's always closed
            databaseManager.close();
        }

        return view;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args != null) {
            categoryName = args.getString(ARG_CATEGORY_NAME);
            isRandomizing = args.getBoolean("is_randomizing");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getArguments() != null) {
            categoryName = getArguments().getString(ARG_CATEGORY_NAME);
            // Update UI with the correct category name
        }
    }


}
