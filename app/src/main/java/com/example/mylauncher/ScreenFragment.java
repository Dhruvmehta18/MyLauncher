package com.example.mylauncher;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //ScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScreenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ScreenCollectionAdapter screenCollectionAdpater;
    HomeScreen homeScreen = null;
    ViewPager viewPager;
    private String mParam1;
    private String mParam2;
//    private OnFragmentInteractionListener mListener;

    public ScreenFragment() {
        // Required empty public constructor
    }

    public static ScreenFragment newInstance(String param1, String param2) {
        ScreenFragment fragment = new ScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen, container, false);
    }

    public boolean isOnBackPressedAvailable() {
        if (homeScreen != null) {
            return homeScreen.isOnBackPressedAvailable();
        }
        return false;
    }

    public void onBackPressed() {
        if (isOnBackPressedAvailable()) {
            homeScreen.onBackPressed();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        screenCollectionAdpater = new ScreenCollectionAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.screen_pager);
        viewPager.setAdapter(screenCollectionAdpater);

    }


    public class ScreenCollectionAdapter extends FragmentStatePagerAdapter {
        int maxscreens = 2;

        ScreenCollectionAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                homeScreen = new HomeScreen();
                return homeScreen;
            } else {
                Fragment fragment = new DemoObjectFragment();
                Bundle args = new Bundle();
                // Our object is just an integer :-P
                args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
                fragment.setArguments(args);
                return fragment;
            }
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
}
