package com.example.workoutplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.textNumberOfSets.setText(":\tSets: " + exercise.getNumberOfSets());
        holder.textNumberOfReps.setText("\tReps: " + exercise.getNumberOfReps());
        // You can set other exercise details if needed
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView textExerciseName;
        TextView textNumberOfSets;
        TextView textNumberOfReps;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            textExerciseName = itemView.findViewById(R.id.textExerciseName);
            textNumberOfSets = itemView.findViewById(R.id.textNumberOfSets);
            textNumberOfReps = itemView.findViewById(R.id.textNumberOfReps);
        }
    }
}