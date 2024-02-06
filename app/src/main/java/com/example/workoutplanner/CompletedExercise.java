package com.example.workoutplanner;

public class CompletedExercise {

    private String exerciseName;
    private String dayCompleted;

    public CompletedExercise(String exerciseName, String dayCompleted) {
        this.exerciseName = exerciseName;
        this.dayCompleted = dayCompleted;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getDayCompleted() {
        return dayCompleted;
    }

    public void setDayCompleted(String dayCompleted) {
        this.dayCompleted = dayCompleted;
    }
}
