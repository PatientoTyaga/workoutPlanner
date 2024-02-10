package com.example.workoutplanner;

public interface ExerciseRemoveListener {
    void onExerciseRemoved(Exercise exercise, int position);
    //void onCategoryEmpty(String categoryName);
    default void onCategoryEmpty(String categoryName) {

    }
}