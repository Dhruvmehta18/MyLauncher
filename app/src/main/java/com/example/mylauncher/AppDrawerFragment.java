package com.example.mylauncher;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

/**
 * A fragment representing a list of Apps.
 * <p/>
 */
public class AppDrawerFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 5;
    AppAdapter appAdapter;
    RecyclerView recyclerView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AppDrawerFragment() {
    }

    public static AppDrawerFragment newInstance(int columnCount) {
        AppDrawerFragment fragment = new AppDrawerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_drawer, container, false);

        // Set the adapter
        SearchView searchView = view.findViewById(R.id.search);
        recyclerView = view.findViewById(R.id.recycler_app_list);
            mColumnCount = Utility.calculateNoOfColumns(getContext(), 80);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
            }
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        appAdapter = new AppAdapter(this.getContext());
        recyclerView.setAdapter(appAdapter);
        searchView.setOnQueryTextListener(this);
        appAdapter.notifyDataSetChanged();
        return view;
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
    public boolean onQueryTextSubmit(String query) {

        appAdapter.getFilter().filter(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("newText", newText);
        appAdapter.getFilter().filter(newText);
        return true;
    }
}
