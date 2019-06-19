package com.example.mylauncher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BottomAppAdapter extends ArrayAdapter {
    private List<AppInfo> appsList;

    BottomAppAdapter(@NonNull Context context, int resource, @NonNull List<AppInfo> objects) {
        super(context, resource, objects);
        appsList = objects;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.fragment_item, null);
        TextView textView = v.findViewById(R.id.app_title);
        ImageView imageView = v.findViewById(R.id.app_icon);
        String appLabel = appsList.get(position).label.toString();
        String appPackage = appsList.get(position).packageName.toString();
        Drawable appIcon = appsList.get(position).icon;
        textView.setText(appLabel);
        textView.setVisibility(View.GONE);
        imageView.setImageDrawable(appIcon);
        return v;
    }
}
