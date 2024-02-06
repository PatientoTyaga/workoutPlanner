package com.example.workoutplanner;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises;
    private boolean isRandomizing;

    // Add a variable to store the background color


    public ExerciseAdapter(List<Exercise> exercises, boolean isRandomizing) {
        this.isRandomizing = isRandomizing;

        // Create a copy of the exercises list to avoid ConcurrentModificationException
        this.exercises = new ArrayList<>(exercises);

        if (isRandomizing) {
            // Shuffle the list of exercises only if randomizing
            Collections.shuffle(this.exercises);
            // Take only the first two exercises if randomizing
            this.exercises = this.exercises.subList(0, Math.min(this.exercises.size(), 2));
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
        // Use the size of the copied list
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

                // If confirmed, get the exercise details
                Exercise removedExercise = exercises.get(position);
                String exerciseName = removedExercise.getName();

                // Remove the exercise from the list before showing the dialog
                exercises.remove(position);
                notifyItemRemoved(position);

                // Show a confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Mark it as completed?");

                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // Get the current day
                    Calendar calendar = Calendar.getInstance();
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                    // Convert the day to a string (you can customize this based on your needs)
                    String dayCompleted = getDayOfWeekString(dayOfWeek);

                    // Move the completed exercise to CompletedWorkoutActivity
                    moveCompletedExercise(context, exerciseName, dayCompleted);

                    // If confirmed, call removeExercise method
                    if (exerciseRemoveListener != null) {
                        exerciseRemoveListener.onExerciseRemoved(removedExercise, position);
                    }
                });

                builder.setNegativeButton("No", (dialog, which) -> {
                    // If canceled, add the exercise back to the list
                    exercises.add(position, removedExercise);
                    notifyItemInserted(position);
                });

                builder.setOnCancelListener(dialog -> {
                    // If canceled by tapping outside the dialog, do nothing (don't add exercise back)
                    // The exercise has already been removed from the list, and the UI is updated accordingly
                });

                builder.show();
            }


            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                if (viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    // Check if the position is valid
                    int position = viewHolder.getAdapterPosition();
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

    private void moveCompletedExercise(Context context, String exerciseName, String dayCompleted) {
        Intent intent = new Intent(context, CompletedWorkoutsActivity.class);
        intent.putExtra("exerciseName", exerciseName);
        intent.putExtra("dayCompleted", dayCompleted);
        context.startActivity(intent);
    }

    public interface ExerciseRemoveListener {
        void onExerciseRemoved(Exercise exercise, int position);
    }

    private ExerciseRemoveListener exerciseRemoveListener;

    public void setExerciseRemoveListener(ExerciseRemoveListener exerciseRemoveListener) {
        this.exerciseRemoveListener = exerciseRemoveListener;
    }

    private String getDayOfWeekString(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "Unknown Day";
        }
    }

}