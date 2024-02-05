package com.example.workoutplanner;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TabPagerAdapter extends FragmentPagerAdapter {


    private List<String> categoryNames;
    private Map<String, Boolean> categories;

    public TabPagerAdapter(FragmentManager fm, Map<String, Boolean> categories) {
        super(fm);
        this.categories = categories;
        this.categoryNames = new ArrayList<>(categories.keySet()); // Convert keys to a list for ordering
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        String categoryName = categoryNames.get(position);
        boolean categoryValue = categories.get(categoryName);
        return new CategoryFragmentActivity(categoryName, categoryValue);
    }

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
