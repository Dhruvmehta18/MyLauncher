package com.example.mylauncher;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreen extends Fragment implements View.OnClickListener {

    HomeScreenPageAdapter homeScreenPageAdapter;
    BottomAppAdapter bottomAppAdapter;
    ViewPager viewPager;
    GridView gridView;

    public HomeScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
//        ImageView Icon = v.findViewById(R.id.icon);
//        Icon.setImageDrawable(MainActivity.getActivityIcon(this.getContext(), "com.android.chrome", "com.google.android.apps.chrome.Main"));
//        Icon.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<AppInfo> appsList;
        PackageManager pm = this.getContext().getPackageManager();
        appsList = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        int j = 1;
        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
        for (ResolveInfo ri : allApps) {
            AppInfo app = new AppInfo();
            app.label = ri.loadLabel(pm);
            app.packageName = ri.activityInfo.packageName;
            Log.d("applist", String.valueOf(app.label) + app.packageName);
            app.icon = ri.activityInfo.loadIcon(pm);
            appsList.add(app);
        }
        appsList = appsList.subList(0, 5);
        homeScreenPageAdapter = new HomeScreenPageAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.home_screen_pager);
        viewPager.setAdapter(homeScreenPageAdapter);
        bottomAppAdapter = new BottomAppAdapter(this.getContext(), R.layout.fragment_item, appsList);
        gridView = view.findViewById(R.id.grid_home_panel);
        gridView.setNumColumns(5);
        gridView.setAdapter(bottomAppAdapter);
    }

}