package com.example.mylauncher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylauncher.utils.RecyclerViewEmptySupport;

/**
 * A fragment representing a list of Apps.
 * <p/>
 */
public class AppDrawerFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    static Bitmap bitmap;
    AppAdapter appAdapter;
    RecyclerViewEmptySupport recyclerView;
    private int mColumnCount;
    private String queryText = "";
    private String search = "";
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AppDrawerFragment() {
    }

    public static AppDrawerFragment newInstance(Bitmap bitmap1) {
        AppDrawerFragment fragment = new AppDrawerFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
        bitmap = bitmap1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (!queryText.isEmpty()) {
            View view1 = view.findViewById(R.id.search_play_store);
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search = "http://play.google.com/store/search?q=" + queryText + "&c=apps";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(search));
                    intent.setPackage("com.android.vending");
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_drawer, container, false);

        // Set the adapter
        view.setBackground(MainActivity.getBlurWallpaper());
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
        recyclerView.setEmptyView(view.findViewById(R.id.list_empty));
        searchView.setOnQueryTextListener(this);
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

    private void runLayoutAnimation(RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        appAdapter.getFilter().filter(newText);
        runLayoutAnimation(recyclerView);
        queryText = newText;
        return true;
    }

}
