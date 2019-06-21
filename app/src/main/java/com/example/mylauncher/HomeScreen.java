package com.example.mylauncher;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreen extends Fragment implements View.OnClickListener {
    AppDrawerFragment drawerFragment;
    HomeScreenPageAdapter homeScreenPageAdapter;
    BottomAppAdapter bottomAppAdapter;
    ViewPager viewPager;
    GridView gridView;
    View view1;
    public HomeScreen() {
        // Required empty public constructor
    }

    public boolean isOnBackPressedAvailable() {
        return view1.getVisibility() == View.GONE;
    }

    public void onBackPressed() {
        if (isOnBackPressedAvailable()) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .remove(drawerFragment)
                    .commit();
            view1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
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
        AppInfo info = new AppInfo("App Drawer", "app_drawer", getResources().getDrawable(R.drawable.ic_apps_black_24dp));
        appsList.remove(2);
        appsList.add(2, info);
        homeScreenPageAdapter = new HomeScreenPageAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.home_screen_pager);
        viewPager.setAdapter(homeScreenPageAdapter);
        bottomAppAdapter = new BottomAppAdapter(this.getContext(), R.layout.fragment_item, appsList);
        gridView = view.findViewById(R.id.grid_home_panel);
        gridView.setNumColumns(5);
        gridView.setAdapter(bottomAppAdapter);
        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view1 = view.findViewById(R.id.homescreen_active);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getAdapter().getItem(position);
                if (object instanceof AppInfo) {
                    AppInfo appInfo = (AppInfo) object;
                    String app_package = (String) appInfo.packageName;
                    Context context = view.getContext();
                    if (app_package.equals("app_drawer")) {
                        FragmentManager fragmentManager = getFragmentManager();
                        drawerFragment = new AppDrawerFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.home_screen_layout, drawerFragment)
                                .commit();
                        view1.setVisibility(View.GONE);

                    } else {
                        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appInfo.packageName.toString());
                        context.startActivity(launchIntent);
                    }
                }
            }
        });
    }

}