package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

public class CustomWorkoutActivity extends AppCompatActivity {

    private TextView descriptionTextView;
    private ImageView[] checkMarks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_workout);

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

        TextView tabTextView = findViewById(R.id.tabTextView);
        tabTextView.setOnClickListener(v -> toggleDescriptionVisibility());

    }

    private void setCardClickListener(CardView card, ImageView checkMark) {
        card.setOnClickListener(v -> {
            toggleCheckMark(checkMark);
            updateAddButtonState();
        });
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


}