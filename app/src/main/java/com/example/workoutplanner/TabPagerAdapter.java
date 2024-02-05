package com.example.workoutplanner;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private List<String> categories;
    private boolean isRandomizing;

    public TabPagerAdapter(FragmentManager fm, List<String> categories, boolean isRandomizing) {
        super(fm);
        this.categories = categories;
        this.isRandomizing = isRandomizing;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new CategoryFragmentActivity(categories.get(position), isRandomizing);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position);
    }
}
