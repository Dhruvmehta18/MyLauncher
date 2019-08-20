package com.example.mylauncher;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CellLayout gridLayout;
    RelativeLayout.LayoutParams layoutParams;

    public MainPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageFragment newInstance(String param1, String param2) {
        MainPageFragment fragment = new MainPageFragment();
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
        context=this.getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_main_page, container, false);
        gridLayout = view.findViewById(R.id.cell_layout);

        MyDragEventListener dragEventListener = new MyDragEventListener();
        view.setOnDragListener(dragEventListener);
        return view;
    }
    class MyDragEventListener implements View.OnDragListener {

        // This is the method that the system calls when it dispatches a drag event to the
        // listener.
        public boolean onDrag(View v, DragEvent event) {

            // Defines a variable to store the action type for the incoming event
            final int action = event.getAction();

            // Handles each of the expected events
            switch(action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        // As an example of what your application might do,
                        // applies a blue color tint to the View to indicate that it can accept
                        // data.

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        // returns true to indicate that the View can accept the dragged data.
                        return true;

                    }

                    // Returns false. During the current drag and drop operation, this View will
                    // not receive events again until ACTION_DRAG_ENDED is sent.
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:

                    // Applies a green tint to the View. Return true; the return value is ignored.


                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    // Re-sets the color tint to blue. Returns true; the return value is ignored.

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DROP:
                    v.invalidate();
                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    // Gets the text data from the item.
                    final CharSequence dragData = item.getText();
                    // Displays a message containing the dragged data.
                    Log.d(getClass().getSimpleName(),"Dragged data is " + dragData);
                    // Turns off any color tints
                    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                    int density = (int) Math.floor(metrics.density);
                    Log.d("density", String.valueOf(density));
                    LinearLayout linearLayout = new LinearLayout(context);
                    // Invalidates the view to force a redraw
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setPadding(8*density,8*density,8*density,8*density);
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setLines(1);
                    textView.setTextSize(12f);
                    textView.setPadding(0,4,0,0);
                    textView.setText(getAppLabel(context, String.valueOf(dragData)));
                    final ImageView imageView = new ImageView(context);
                    LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(60*density,60*density);
                    layoutParams1.gravity=Gravity.CENTER_HORIZONTAL;
                    imageView.setLayoutParams(layoutParams1);
                    imageView.setContentDescription(getString(R.string.app_image));
                    imageView.setImageDrawable(getAppIcon(context, String.valueOf(dragData)));
                    linearLayout.addView(imageView);
                    linearLayout.addView(textView);
                    imageView.setTag(dragData);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String app_package = (String) dragData;
                            Context context = view.getContext();
                            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(app_package);
                            context.startActivity(launchIntent);
                        }
                    });
                    imageView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            // Create a new ClipData.
                            // This is done in two steps to provide clarity. The convenience method
                            // ClipData.newPlainText() can create a plain text ClipData in one step.

                            // Create a new ClipData.Item from the ImageView object's tag
                            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

                            // Create a new ClipData using the tag as a label, the plain text MIME type, and
                            // the already-created item. This will create a new ClipDescription object within the
                            // ClipData, and set its MIME type entry to "text/plain"
                            ClipData dragData = new ClipData(
                                    (CharSequence) view.getTag(),
                                    new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN },
                                    item);

                            // Instantiates the drag shadow builder.
                            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(imageView);

                            // Starts the drag

                            view.startDrag(dragData,  // the data to be dragged
                                    myShadow,  // the drag shadow builder
                                    null,      // no need to use local data
                                    0          // flags (not currently used, set to 0)
                            );
                            return false;
                        }
                    });
                    imageView.setOnDragListener(this);
                    int[] cell=gridLayout.coordinateToCellXY(event.getX(),event.getY(),0,0);
                    gridLayout.addView(linearLayout,new CellLayout.LayoutParams(cell[0],cell[1],1,1));
                    gridLayout.setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View view, DragEvent dragEvent) {
                            return false;
                        }
                    });
                    // Returns true. DragEvent.getResult() will return true.

                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Turns off any color tinting

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Does a getResult(), and displays what happened.
                    if (event.getResult()) {
                        Log.d(getClass().getSimpleName(), "The drop was handled.");

                    } else {
                        Log.d(getClass().getSimpleName(),"The drop didn't work.");
                    }

                    // returns true; the value is ignored.
                    return true;

                // An unknown action type was received.
                default:
                    Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                    break;
            }

            return false;
        }
    }
    private static Drawable getAppIcon(Context context, String packageName){
        AppsManager appsManager = new AppsManager(context);
        return appsManager.getAppIconByPackageName(packageName);
    }
    private static CharSequence getAppLabel(Context context, String packageName){
        AppsManager appsManager = new AppsManager(context);
        return appsManager.getApplicationLabelByPackageName(packageName);
    }
}
