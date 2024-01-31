package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the CardViews by their IDs
        CardView todaysWorkout = findViewById(R.id.todaysWorkoutCard);
        CardView completedWorkout = findViewById(R.id.completedWorkouts);
        CardView customWorkout = findViewById(R.id.customWorkout);
        CardView randomizeWorkout = findViewById(R.id.randomizeWorkout);

        //initially grey out todaysWorkout and completedWorkout cards
        disableCard(todaysWorkout);
        disableCard(completedWorkout);

        //setting click listeners for the card views
        randomizeWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RandomizeWorkoutActivity.class);
                startActivity(intent);
            }
        });

        customWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomWorkoutActivity.class);
                startActivity(intent);
            }
        });


    }

    private void disableCard(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.greyed_out_color));
    }


}