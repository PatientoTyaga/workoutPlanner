package com.example.workoutplanner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises;
    private boolean isRandomizing;

    public ExerciseAdapter(List<Exercise> exercises, boolean isRandomizing) {
        this.exercises = exercises;
        this.isRandomizing = isRandomizing;

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
}