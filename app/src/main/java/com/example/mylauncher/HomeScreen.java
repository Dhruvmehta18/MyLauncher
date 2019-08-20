package com.example.mylauncher;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreen extends Fragment {
    private HomeScreenPageAdapter homeScreenPageAdapter;
    private BottomAppAdapter bottomAppAdapter;
    private ViewPager viewPager;
    private GridView gridView;
    private View view1;
    private BottomSheetBehavior bottomSheetBehavior;
    private View contentView;
    private View loadingView;
    private int shortAnimationDuration;
    public HomeScreen() {
        // Required empty public constructor
    }

    public boolean isOnBackPressedAvailable() {
        return bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    public void onBackPressed() {
        if (isOnBackPressedAvailable()) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void crossfade() {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        contentView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        loadingView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingView.setVisibility(View.GONE);
                    }
                });
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
//        AppInfo info = new AppInfo("App Drawer", "app_drawer", getResources().getDrawable(R.drawable.ic_apps_black_24dp));
//        appsList.remove(2);
//        appsList.add(2, info);
        homeScreenPageAdapter = new HomeScreenPageAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager = view.findViewById(R.id.home_screen_pager);
        viewPager.setAdapter(homeScreenPageAdapter);
        bottomAppAdapter = new BottomAppAdapter(this.getContext(), R.layout.fragment_item, appsList);
        gridView = view.findViewById(R.id.grid_home_panel);
        gridView.setNumColumns(5);
        gridView.setAdapter(bottomAppAdapter);
        contentView = view.findViewById(R.id.bottom_home_panel);
        loadingView = view.findViewById(R.id.app_drawer_fragment);

        // Initially hide the content view.
        loadingView.setVisibility(View.GONE);
        view1 = view.findViewById(R.id.homescreen_active);
        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        contentView.setVisibility(View.VISIBLE);
                        loadingView.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        contentView.setVisibility(View.VISIBLE);
                        loadingView.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        contentView.setVisibility(View.GONE);
                        loadingView.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        contentView.setVisibility(View.VISIBLE);
                        loadingView.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                contentView.setAlpha(1 - slideOffset);
                loadingView.setAlpha(slideOffset);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view1.setClickable(true);
        view1.setFocusable(true);
        // BEGIN_INCLUDE(init_detector)


        // First create the GestureListener that will include all our callbacks.

        // Then create the GestureDetector, which takes that listener as an argument.



        /* For the view where gestures will occur, create an onTouchListener that sends

         * all motion events to the gesture detector.  When the gesture detector

         * actually detects an event, it will use the callbacks you created in the

         * SimpleOnGestureListener to alert your application.

         */
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureListener(bottomSheetBehavior);

        final GestureDetector gd = new GestureDetector(getActivity(), gestureListener);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gd.onTouchEvent(motionEvent);
                return false;
            }
        });

// Sets the drag event listener for the View
        // END_INCLUDE(init_detector)
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getAdapter().getItem(position);
                if (object instanceof AppInfo) {
                    AppInfo appInfo = (AppInfo) object;
                    String app_package = (String) appInfo.packageName;
                    Context context = view.getContext();
                        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(app_package);
                        context.startActivity(launchIntent);
                }
            }
        });
    }


}