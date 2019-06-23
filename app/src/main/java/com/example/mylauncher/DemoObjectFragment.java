package com.example.mylauncher;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class DemoObjectFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    public DemoObjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_demo_object, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        assert args != null;
        Log.e("Errorffff", Integer.toString(args.getInt(ARG_OBJECT)));
        TextView textView = view.findViewById(android.R.id.text1);
//        textView.setText(String.format(Locale.US,"Hello %d",args.getInt(ARG_OBJECT)));
    }
}
