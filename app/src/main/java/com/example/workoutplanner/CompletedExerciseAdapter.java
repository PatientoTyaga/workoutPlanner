package com.example.workoutplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompletedExerciseAdapter extends RecyclerView.Adapter<CompletedExerciseAdapter.CompletedExerciseViewHolder> {

    private List<CompletedExercise> completedExercisesList;

    public CompletedExerciseAdapter(List<CompletedExercise> completedExercisesList) {
        this.completedExercisesList = completedExercisesList;
    }

    @NonNull
    @Override
    public CompletedExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_exercise, parent, false);
        return new CompletedExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedExerciseViewHolder holder, int position) {
        CompletedExercise completedExercise = completedExercisesList.get(position);
        holder.textExerciseName.setText(completedExercise.getExerciseName());
        holder.textDayCompleted.setText(completedExercise.getDayCompleted());
    }

    @Override
    public int getItemCount() {
        return completedExercisesList.size();
    }

    public static class CompletedExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView textExerciseName;
        TextView textDayCompleted;

        public CompletedExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            textExerciseName = itemView.findViewById(R.id.textExerciseName);
            textDayCompleted = itemView.findViewById(R.id.textDayCompleted);
        }
    }
}
