package com.example.mylauncher;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class HomeScreenPageAdapter extends FragmentStatePagerAdapter {
    private int maxscreens = 2;

    HomeScreenPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int i) {
        return new MainPageFragment();
    }

    @Override
    public int getCount() {
        return maxscreens;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
