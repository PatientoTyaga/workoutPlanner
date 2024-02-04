package com.example.workoutplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises;

    public ExerciseAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
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