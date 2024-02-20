package com.example.workoutplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

public class RandomizeWorkoutActivity extends AppCompatActivity {

    //declare variables to be used
    private DatabaseManager databaseManager; //set up database
    private TextView descriptionTextView;
    private ImageView[] checkMarks; //used to control green checkmark for when category is selected
    private Map<String, Boolean> selectedCategories = new HashMap<>(); //set up map to store selected categories
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomize_workout);

        //initialize connection to database
        databaseManager = new DatabaseManager(this);
        databaseManager.open();

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
            setCardClickListener(cards[i], checkMarks[i], i);
        }

        //set on click listener for the home button
        homeButton.setOnClickListener(view-> {
            Intent intent = new Intent(RandomizeWorkoutActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Set click listener for the "Add" button
        MaterialButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this::onAddButtonClick);

        TextView tabTextView = findViewById(R.id.tabTextView);
        tabTextView.setOnClickListener(v -> toggleDescriptionVisibility());

    }

    //show green check mark when a card is selected to indicate card is selected
    private void setCardClickListener(CardView card, ImageView checkMark, int cardIndex) {
        card.setOnClickListener(v -> {
            toggleCheckMark(checkMark);
            updateAddButtonState();

            String categoryName = getCategoryNameFromCard(card, cardIndex);

            // Update the list of selected categories only if the categoryName is not null or empty
            if (categoryName != null && !categoryName.isEmpty()) {
                if (checkMark.getVisibility() == View.VISIBLE) {
                    selectedCategories.put(categoryName, true);
                } else {
                    selectedCategories.remove(categoryName);
                }
            }
        });
    }

    private String getCategoryNameFromCard(CardView card, int cardIndex) {
        // Array to hold resource IDs for cardNameTextViews
        int[] cardNameTextViewIds = {
                R.id.cardNameTextView1, R.id.cardNameTextView2, R.id.cardNameTextView3,
                R.id.cardNameTextView4, R.id.cardNameTextView5, R.id.cardNameTextView6,
                R.id.cardNameTextView7, R.id.cardNameTextView8, R.id.cardNameTextView9
        };

        // Find the TextView inside the CardView based on its actual ID using the cardIndex
        TextView categoryNameTextView = card.findViewById(cardNameTextViewIds[cardIndex]);

        // Check if categoryNameTextView is not null before getting text
        if (categoryNameTextView != null) {
            return categoryNameTextView.getText().toString();
        } else {
            // Handle the case where categoryNameTextView is null
            return "Unknown Category";
        }
    }

    //method to toggle green check mark on and off
    private void toggleCheckMark(ImageView checkMark) {
        checkMark.setVisibility(checkMark.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    //method to make add button visible when atleast one card is selected
    private void updateAddButtonState() {

        boolean atLeastOneVisible = checkAtLeastOneCheckMarkVisible();
        findViewById(R.id.addButton).setEnabled(atLeastOneVisible);
    }

    //check if atleast one checkmark is visisble
    private boolean checkAtLeastOneCheckMarkVisible() {
        for(ImageView checkMark : checkMarks) {
            if(checkMark.getVisibility() == View.VISIBLE) {
                return true;
            }
        }

        return false;
    }


    //expand description at the top to show and minimize once clicked again
    private void toggleDescriptionVisibility() {
        descriptionTextView.setVisibility(descriptionTextView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public void onAddButtonClick(View view) {
        // Check if there are selected categories
        if (selectedCategories.isEmpty()) {
            // if no categories selected, simply return
            // can add more functionality here in future if need more to be done
            return;
        }

        // Retrieve existing categories and their randomized status from the database
        Map<String, Boolean> existingCategories = databaseManager.loadSelectedCategories();

        // Check for duplicates and update the existing categories map
        for (String selectedCategory : selectedCategories.keySet()) {
            if (!existingCategories.containsKey(selectedCategory)) {
                // Set the boolean value to true for new categories
                existingCategories.put(selectedCategory, true);
            }
        }

        // Create a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please note that adding a randomized workout will shuffle any workout added earlier from randomized workout. Do you want to proceed?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Proceed with adding the workout

                // Save the updated categories map back to the database
                databaseManager.saveSelectedCategories(existingCategories);

                // Create an Intent to pass the updated categories to TodaysWorkoutActivity
                Intent intent = new Intent(RandomizeWorkoutActivity.this, TodaysWorkoutActivity.class);
                intent.putExtra("selectedCategories", (HashMap<String, Boolean>) existingCategories);

                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User chose not to proceed, do nothing
            }
        });

        // Show the confirmation dialog
        builder.create().show();
    }


}