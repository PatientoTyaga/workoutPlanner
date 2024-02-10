package com.example.workoutplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> implements ExerciseRemoveListener{

    // declare variables
    private List<Exercise> exercises;

    private Map<String, List<Exercise>> randomizedCategories;
    private boolean isRandomizing;

    private DatabaseManager databaseManager;

    private String categoryName;

    private ExerciseRemoveListener exerciseRemoveListener;

    // Add a variable to store the background color


    public ExerciseAdapter(List<Exercise> exercises, String categoryName, boolean isRandomizing, DatabaseManager databaseManager) {
        this.isRandomizing = isRandomizing;
        this.databaseManager = databaseManager;
        this.categoryName = categoryName;

        // Make a copy of the original list
        this.exercises = new ArrayList<>(exercises);

        if (isRandomizing) {
            Collections.shuffle(this.exercises);
            // Take only the first two exercises if randomizing
            this.exercises = this.exercises.subList(0, Math.min(this.exercises.size(), 2));

        }

        // Always open the database before performing any operations
        if (!databaseManager.isOpen()) {
            databaseManager.open();
        }

        try {
            randomizedCategories = databaseManager.loadRandomizedExercises();

            if (!randomizedCategories.containsKey(categoryName)) {
                randomizedCategories.put(categoryName, this.exercises);
                databaseManager.saveRandomizedExercises(randomizedCategories);
            }
        } finally {
            // Always close the database connection after performing operations
            if (databaseManager.isOpen()) {
                databaseManager.close();
            }
        }
    }



    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.textExerciseName.setText(exercise.getName());
        holder.textNumberOfSets.setText("Sets: " + exercise.getNumberOfSets());
        holder.textNumberOfReps.setText("Reps: " + exercise.getNumberOfReps());


        // Set click listener to toggle the expanded state
        holder.itemView.setOnClickListener(view -> {
            exercise.setExpanded(!exercise.isExpanded());
            notifyItemChanged(position);
        });

        // Show/hide additional details based on the expanded state
        holder.textNumberOfSets.setVisibility(exercise.isExpanded() ? View.VISIBLE : View.GONE);
        holder.textNumberOfReps.setVisibility(exercise.isExpanded() ? View.VISIBLE : View.GONE);

        // Toggle visibility of the arrow based on the expanded state
        holder.arrowImageView.setVisibility(View.VISIBLE);
        holder.arrowImageView.setRotation(exercise.isExpanded() ? 180f : 0f);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView textExerciseName;
        TextView textNumberOfSets;
        TextView textNumberOfReps;
        ImageView arrowImageView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            textExerciseName = itemView.findViewById(R.id.textExerciseName);
            textNumberOfSets = itemView.findViewById(R.id.textNumberOfSets);
            textNumberOfReps = itemView.findViewById(R.id.textNumberOfReps);
            arrowImageView = itemView.findViewById(R.id.arrowImageView);

        }
    }

    public void enableSwipeToDelete(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                // Store the context from the itemView
                Context context = viewHolder.itemView.getContext();

                // Get the exercise to be removed
                Exercise removedExercise = exercises.get(position);

                // Remove the exercise from the copied list
                exercises.remove(position);
                notifyItemRemoved(position);

                // Show a confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Mark it as completed?");

                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // Check if the database connection is open
                    checkDatabaseState();

                    //remove the exercise from the list of exercises under the category
                    Map<String, List<Exercise>> randomizedCategories = databaseManager.loadRandomizedExercises();
                    List<Exercise> categoryToEdit = randomizedCategories.get(categoryName);

                    // Remove the exercise from the list if it is contained
                    categoryToEdit.removeIf(exercise -> exercise.getName().equals(removedExercise.getName()));
                    randomizedCategories.put(categoryName, categoryToEdit);

                    checkDatabaseState();
                    //update database
                    databaseManager.saveRandomizedExercises(randomizedCategories);

                    // Get the current date and time
                    Date currentDate = new Date();

                    // Format the date
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = formatter.format(currentDate);

                    checkDatabaseState();

                    // Move the completed exercise to database
                    databaseManager.addCompletedExercise(removedExercise.getName(), formattedDate);

                    // Close the database connection if it was opened in this method
                    if (databaseManager.isOpen()) {
                        databaseManager.close();
                    }

                    // Check if all exercises under the current category have been removed
                    if (exercises.isEmpty()) {
                        // Notify the listener to switch to the next category or main activity
                        if (exerciseRemoveListener != null) {
                            exerciseRemoveListener.onCategoryEmpty(categoryName);
                        }
                    }
                });

                builder.setNegativeButton("No", (dialog, which) -> {
                    // If canceled, restore the exercise to the copied list
                    exercises.add(position, removedExercise);
                    notifyItemInserted(position);
                });

                builder.setOnCancelListener(dialog -> {
                    // If canceled by tapping outside the dialog, restore the exercise to the copied list
                    exercises.add(position, removedExercise);
                    notifyItemInserted(position);
                });

                builder.show();
            }

            private void checkDatabaseState() {
                if(!databaseManager.isOpen()) {
                    databaseManager.open();
                }
            }


            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                if (viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    // Get the adapter position
                    int position = viewHolder.getAdapterPosition();

                    // Set the background color based on the swipe progress
                    if (position >= 0 && position < exercises.size()) {
                        Exercise exercise = exercises.get(position);
                        exercise.setBackgroundColor(getBackgroundColor(dX));
                        viewHolder.itemView.setBackgroundColor(exercise.getBackgroundColor());
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


            private int getBackgroundColor(float dX) {
                // Customize the color based on the swipe progress
                // For simplicity, I'm using a solid green color here
                int backgroundColor = Color.argb(255, 0, 255, 0);

                // You can adjust the green color based on the swipe progress for a gradient effect
                // For example, you can decrease the green component as the user swipes
                float swipeProgress = Math.min(1f, Math.abs(dX) / recyclerView.getWidth());
                int greenComponent = (int) (255 * (1 - swipeProgress));
                backgroundColor = Color.argb(255, 0, greenComponent, 0);

                return backgroundColor;
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /*
    private void moveCompletedExercise(Context context, String exerciseName, String dayCompleted) {
        Intent intent = new Intent(context, CompletedWorkoutsActivity.class);
        intent.putExtra("exerciseName", exerciseName);
        intent.putExtra("dayCompleted", dayCompleted);
        context.startActivity(intent);
    }

     */



     /*
    public interface ExerciseRemoveListener {
        void onExerciseRemoved(Exercise exercise, int position);
    }

      */


    @Override
    public void onExerciseRemoved(Exercise exercise, int position) {
        //to modify
    }

    public void setExerciseRemoveListener(ExerciseRemoveListener exerciseRemoveListener) {
        this.exerciseRemoveListener = exerciseRemoveListener;
    }



}