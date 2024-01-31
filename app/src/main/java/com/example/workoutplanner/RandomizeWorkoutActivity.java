package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class RandomizeWorkoutActivity extends AppCompatActivity {

    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomize_workout);

        //get home button by id
        MaterialButton homeButton = findViewById(R.id.homeButton);

        //initialize descriptionTextView
        descriptionTextView = findViewById(R.id.descriptionTextView);

        //set an onclick listener
        homeButton.setOnClickListener(view-> {
            Intent intent = new Intent(RandomizeWorkoutActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        //set click listener for the "Click Here For Instructions" TextView
        TextView tabTextView = findViewById(R.id.tabTextView);
        tabTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDescriptionVisibility();
            }
        });

    }

    private void toggleDescriptionVisibility() {
        if(descriptionTextView.getVisibility() == View.VISIBLE) {
            //if description is visible, hide it
            descriptionTextView.setVisibility(View.GONE);
        } else {
            //it is not visible so show
            descriptionTextView.setVisibility(View.VISIBLE);
        }
    }
}