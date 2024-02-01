package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CustomWorkoutActivity extends AppCompatActivity {

    private TextView descriptionTextView;
    private ImageView[] checkMarks;
    private List<String> selectedCategories = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_workout);

        DatabaseManager dataBaseManager = new DatabaseManager(this);
        dataBaseManager.open();

        //get home button by id
        MaterialButton homeButton = findViewById(R.id.homeButton);

        //get id for description text
        descriptionTextView = findViewById(R.id.descriptionTextView);

        //Initialize card views and checkmark imageview
        CardView[] cards = new CardView[9];
        checkMarks = new ImageView[9];

        for(int i=0; i<9; i++) {
            int cardId = getResources().getIdentifier("card" + (i+1), "id", getPackageName());
            int checkMarkId = getResources().getIdentifier("checkMarkImageView" + (i+1), "id", getPackageName());

            cards[i] = findViewById(cardId);
            checkMarks[i] = findViewById(checkMarkId);

            //set click listener for cards 1-9
            setCardClickListener(cards[i], checkMarks[i]);
        }

        //set on click listener for the home button
        homeButton.setOnClickListener(view-> {
            Intent intent = new Intent(CustomWorkoutActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Set click listener for the "Add" button
        MaterialButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this::onAddButtonClick);

        TextView tabTextView = findViewById(R.id.tabTextView);
        tabTextView.setOnClickListener(v -> toggleDescriptionVisibility());

    }

    private void setCardClickListener(CardView card, ImageView checkMark) {
        card.setOnClickListener(v -> {
            toggleCheckMark(checkMark);
            updateAddButtonState();

            String categoryName = getCategoryNameFromCard(card); // Implement this method to get the category name based on the card

            // Update the list of selected categories only if the categoryName is not null or empty
            if (categoryName != null && !categoryName.isEmpty()) {
                if (checkMark.getVisibility() == View.VISIBLE) {
                    selectedCategories.add(categoryName);
                } else {
                    selectedCategories.remove(categoryName);
                }
            }
        });
    }

    private String getCategoryNameFromCard(CardView card) {
        // Find the TextView inside the CardView based on the tag
        String cardTag = (String) card.getTag();
        int textViewId = getResources().getIdentifier("cardNameTextView" + cardTag, "id", getPackageName());

        // Use card.findViewById instead of just findViewById
        TextView categoryNameTextView = card.findViewById(textViewId);

        // Check if categoryNameTextView is not null before getting text
        if (categoryNameTextView != null) {
            return categoryNameTextView.getText().toString();
        } else {
            // Handle the case where categoryNameTextView is null
            return "Unknown Category";
        }
    }

    private void toggleCheckMark(ImageView checkMark) {
        checkMark.setVisibility(checkMark.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    private void updateAddButtonState() {

        boolean atLeastOneVisible = checkAtLeastOneCheckMarkVisible();
        findViewById(R.id.addButton).setEnabled(atLeastOneVisible);
    }

    private boolean checkAtLeastOneCheckMarkVisible() {
        for(ImageView checkMark : checkMarks) {
            if(checkMark.getVisibility() == View.VISIBLE) {
                return true;
            }
        }

        return false;
    }


    private void toggleDescriptionVisibility() {
        descriptionTextView.setVisibility(descriptionTextView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    // Method to save selected categories in SharedPreferences
    private void saveSelectedCategories(List<String> selectedCategories) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("selectedCategories", new HashSet<>(selectedCategories));
        editor.apply();
    }


    // Add button click listener
    public void onAddButtonClick(View view) {
        // Create an Intent to pass the selected categories to TodaysWorkoutActivity
        Intent intent = new Intent(CustomWorkoutActivity.this, TodaysWorkoutActivity.class);
        intent.putStringArrayListExtra("selectedCategories", (ArrayList<String>) selectedCategories);
        startActivity(intent);

        // Save selected categories in SharedPreferences for persistence
        saveSelectedCategories(selectedCategories);
    }


}