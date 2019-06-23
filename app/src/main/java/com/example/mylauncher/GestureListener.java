package com.example.mylauncher;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class GestureListener extends GestureDetector.SimpleOnGestureListener {


    private static final String TAG = "GestureListener";

    private BottomSheetBehavior bottomSheet;

    public GestureListener(BottomSheetBehavior bt) {
        bottomSheet = bt;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        switch (getDirection(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
            case 1:
                bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                return true;
            case 2:
                return true;
            case 3:
                return true;
            case 4:
                return true;
            default:
                return false;
        }
    }

    private int getDirection(float x1, float y1, float x2, float y2) {

        double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
        Log.d("swipe", String.valueOf(angle));
        if (angle > 45 && angle <= 135)
            return 1;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            return 2;
        if (angle < -45 && angle >= -135)
            return 3;
        if (angle > -45 && angle <= 45)
            return 4;

        return 0;     // required by java to avoid error
    }
}
