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

    private Map<String, List<Exercise>> randomizedCategories;

    public TabPagerAdapter(FragmentManager fm, Map<String, Boolean> categories, Map<String, List<Exercise>> randomizedCategories) {
        super(fm);
        this.categories = categories;
        this.categoryNames = new ArrayList<>(categories.keySet()); // Convert keys to a list for ordering
        this.randomizedCategories = randomizedCategories;
    }

    // calls CategoryFragmentActivity class in order to arrange for display of exercises for category
    @NonNull
    @Override
    public Fragment getItem(int position) {
        String categoryName = categoryNames.get(position);
        boolean categoryValue = categories.get(categoryName);

        return new CategoryFragmentActivity(categoryName, categoryValue);
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
}
