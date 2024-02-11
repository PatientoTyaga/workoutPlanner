package com.example.workoutplanner;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class TabPagerAdapter extends FragmentPagerAdapter {

    // declare variables to be used
    private List<String> categoryNames;
    private Map<String, Boolean> categories;

    private List<Fragment> fragments;

    private DatabaseManager databaseManager;

    private TodaysWorkoutActivity activity;


    public TabPagerAdapter(FragmentManager fm, TodaysWorkoutActivity activity, Map<String, Boolean> categories, DatabaseManager databaseManager) {
        super(fm);
        this.categories = categories;
        this.categoryNames = new ArrayList<>(categories.keySet()); // Convert keys to a list for ordering
        this.fragments = new ArrayList<>();
        this.databaseManager = databaseManager;
        this.activity = activity;
        initializeFragments();

    }

    private void initializeFragments() {
        for (String categoryName : categoryNames) {
            boolean categoryValue = categories.get(categoryName);
            fragments.add(new CategoryFragmentActivity(categoryName, categoryValue));
        }
    }

    // calls CategoryFragmentActivity class in order to arrange for display of exercises for category
    @NonNull
    @Override
    public Fragment getItem(int position) {
        /*
        String categoryName = categoryNames.get(position);
        boolean categoryValue = categories.get(categoryName);
        return new CategoryFragmentActivity(categoryName, categoryValue);

         */

        return fragments.get(position);

    }

    //get count of number of selected categories
    @Override
    public int getCount() {
        return categoryNames.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryNames.get(position);
    }

    // Method to remove a tab at a given position
    public void removeTab(int position, boolean lastCategory) {

        if (position >= 0 && position < fragments.size()) {
            fragments.remove(position);

            String categoryName = categoryNames.remove(position);
            //categoryNames.remove(position);

            categories.remove(categoryName);

            if(!databaseManager.isOpen()) {
                databaseManager.open();
            }

            Map<String, Boolean> selectedCategories = databaseManager.loadSelectedCategories();
            Map<String, List<Exercise>> randomizedCategories = databaseManager.loadRandomizedExercises();

            //remove key in selectedCategories
            if(selectedCategories.containsKey(categoryName)) {
                selectedCategories.remove(categoryName);
                databaseManager.saveSelectedCategories(selectedCategories);
            }

            //remove key in randomizedCategories
            if(randomizedCategories.containsKey(categoryName)) {
                randomizedCategories.remove(categoryName);
                databaseManager.saveRandomizedExercises(randomizedCategories);
            }

            // Update the data in TodaysWorkoutActivity
            if(!lastCategory) {
                activity.updateData(selectedCategories);
            }

            databaseManager.deleteCategory(categoryName);


            //left off here. now issue is when you go to home page and return it brings it back
            //look into the other lists to fix this

            notifyDataSetChanged(); //might put this back or not. to determine later

        }

    }


}
