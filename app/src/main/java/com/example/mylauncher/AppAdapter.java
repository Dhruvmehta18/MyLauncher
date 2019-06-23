package com.example.mylauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> implements Filterable {
    private SortedList<AppInfo> appsList;
    private List<AppInfo> mappList;

    AppAdapter(Context c) {

        //This is where we build our list of app details, using the app
        //object we created to store the label, package name and icon
        if (mappList == null || mappList.isEmpty()) {
            Log.d("COnstruct", "Init");
            PackageManager pm = c.getPackageManager();
            appsList = new SortedList<>(AppInfo.class, new SortedList.Callback<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    return String.valueOf(o1.getLabel()).compareTo(String.valueOf(o2.getLabel()));
                }

                @Override
                public void onChanged(int position, int count) {
                    notifyItemRangeChanged(position, count);
                }

                @Override
                public boolean areContentsTheSame(AppInfo oldItem, AppInfo newItem) {
                    return oldItem.getLabel().equals(newItem.getLabel());
                }

                @Override
                public boolean areItemsTheSame(AppInfo item1, AppInfo item2) {
                    return item1.getLabel().equals(item2.getLabel());
                }

                @Override
                public void onInserted(int position, int count) {
                    notifyItemRangeInserted(position, count);
                }

                @Override
                public void onRemoved(int position, int count) {
                    notifyItemRangeRemoved(position, count);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    notifyItemMoved(fromPosition, toPosition);
                }
            });

            mappList = new ArrayList<>();
            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
            for (ResolveInfo ri : allApps) {
                AppInfo app = new AppInfo();
                app.label = ri.loadLabel(pm);
                app.packageName = ri.activityInfo.packageName;
                app.icon = ri.activityInfo.loadIcon(pm);
                appsList.add(app);
            }
            addAll(appsList);
        } else
            this.mappList = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //Here we use the information in the list we created to define the views

        Log.d("mappAdapter", String.valueOf(mappList.size()));

        String appLabel = mappList.get(i).label.toString();
        String appPackage = mappList.get(i).packageName.toString();
        Drawable appIcon = mappList.get(i).icon;

        TextView textView = viewHolder.textView;
        textView.setVisibility(View.VISIBLE);
        textView.setText(appLabel);
        ImageView imageView = viewHolder.img;
        imageView.setImageDrawable(appIcon);
        Log.d("mapplist size", String.valueOf(mappList.size()));

    }

    @Override
    public int getItemCount() {
        return mappList.size();
    }

    @NonNull
    @Override
    public AppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //This is what adds the code we've written in here to our target view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.fragment_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public Filter getFilter() {
        mappList.clear();
        addAll(appsList);
        return new MyFilter(this, mappList);
    }

    private void addAll(SortedList sortedList) {
        for (int i = 0; i < sortedList.size(); i++) {
            mappList.add((AppInfo) sortedList.get(i));
        }
    }


    private static class MyFilter extends Filter {

        private final AppAdapter myAdapter;
        private final List<AppInfo> originalList;
        private final List<AppInfo> filteredList;

        private MyFilter(AppAdapter myAdapter, List<AppInfo> originalList) {
            this.myAdapter = myAdapter;
            this.originalList = originalList;
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (charSequence.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for (AppInfo app : originalList) {
                    if (String.valueOf(app.label).toLowerCase().contains(filterPattern)) {
                        filteredList.add(app);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            Object list = filterResults.values;
            myAdapter.mappList.clear();
            if (list instanceof List) {
                ArrayList<AppInfo> appInfoList = (ArrayList<AppInfo>) list;
                myAdapter.mappList.addAll(appInfoList);
            }
            myAdapter.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView img;

        //This is the subclass ViewHolder which simply
        //'holds the views' for us to show on each row
        ViewHolder(View itemView) {
            super(itemView);

            //Finds the views from our row.xml
            textView = itemView.findViewById(R.id.app_title);
            img = itemView.findViewById(R.id.app_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Context context = v.getContext();
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(mappList.get(pos).packageName.toString());
            context.startActivity(launchIntent);
        }
    }
}