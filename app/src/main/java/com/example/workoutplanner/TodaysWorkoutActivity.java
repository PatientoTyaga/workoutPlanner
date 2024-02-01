package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TodaysWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_workout);

        // Retrieve the selected categories from the Intent
        ArrayList<String> selectedCategories = getIntent().getStringArrayListExtra("selectedCategories");

        // Example: Display the selected categories in a TextView
        TextView categoriesTextView = findViewById(R.id.categoriesTextView);
        if (selectedCategories != null && !selectedCategories.isEmpty()) {
            StringBuilder categoriesText = new StringBuilder("Selected Categories:\n");
            for (String category : selectedCategories) {
                categoriesText.append("- ").append(category).append("\n");
            }
            categoriesTextView.setText(categoriesText.toString());
        } else {
            categoriesTextView.setText("No categories selected.");
        }
    }

    // Method to retrieve selected categories from SharedPreferences
    private Set<String> getSelectedCategoriesFromPrefs() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return preferences.getStringSet("selectedCategories", new HashSet<>());
    }
}