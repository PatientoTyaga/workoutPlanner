package com.example.workoutplanner;

public class Exercise {
    private int id;
    private String name;
    private int categoryId;
    private int sets;
    private int reps;

    private boolean expanded;

    public Exercise(String name, int categoryId, int sets, int reps) {
        this.name = name;
        this.categoryId = categoryId;
        this.sets = sets;
        this.reps = reps;
    }

    public int getNumberOfSets() {
        return sets;
    }

    public int getNumberOfReps() {
        return reps;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
