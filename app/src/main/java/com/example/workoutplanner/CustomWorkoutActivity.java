package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

public class CustomWorkoutActivity extends AppCompatActivity {

    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_workout);

        //get home button by id
        MaterialButton homeButton = findViewById(R.id.homeButton);

        //get id for description text
        descriptionTextView = findViewById(R.id.descriptionTextView);

        //set on click listener
        homeButton.setOnClickListener(view-> {
            Intent intent = new Intent(CustomWorkoutActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        TextView tabTextView = findViewById(R.id.tabTextView);
        tabTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDescriptionVisibility();
            }
        });

    }

    private void toggleDescriptionVisibility() {
        if(descriptionTextView.getVisibility() == descriptionTextView.VISIBLE) {
            //description text is visible so we hide it
            descriptionTextView.setVisibility(View.GONE);
        }else {
            //description text not visible so show it
            descriptionTextView.setVisibility(View.VISIBLE);
        }
    }
}