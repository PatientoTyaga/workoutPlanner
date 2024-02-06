package com.example.workoutplanner;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompletedExerciseAdapter extends RecyclerView.Adapter<CompletedExerciseAdapter.CompletedExerciseViewHolder> {

    private Map<String, List<String>> completedExercisesByDate;

    public CompletedExerciseAdapter(Map<String, List<String>> completedExercisesByDate) {
        this.completedExercisesByDate = completedExercisesByDate;
    }

    @NonNull
    @Override
    public CompletedExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_exercise, parent, false);
        return new CompletedExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedExerciseViewHolder holder, int position) {
        // Get the date for the current position
        String date = new ArrayList<>(completedExercisesByDate.keySet()).get(position);

        // Get the exercises list for the current date
        List<String> exercises = completedExercisesByDate.get(date);

        // Join the exercises list into a single string
        String exercisesText = TextUtils.join(", ", exercises);

        // Set the date and exercises text to the ViewHolder
        holder.textDate.setText(date);
        holder.textExercises.setText(exercisesText);
    }


    @Override
    public int getItemCount() {
        return completedExercisesByDate.size();
    }

    public static class CompletedExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView textDate;
        TextView textExercises;

        public CompletedExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
            textExercises = itemView.findViewById(R.id.textExercises);
        }
    }
}
