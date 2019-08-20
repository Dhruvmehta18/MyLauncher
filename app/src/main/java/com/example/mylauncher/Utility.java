package com.example.mylauncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.Type;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Utility {
    private static int noOfRows;
    private static int noOfColumns;
    static float displayDensity;
    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        displayDensity = displayMetrics.density;
        float screenWidthDp = displayMetrics.widthPixels / displayDensity;
        noOfColumns=(int) (screenWidthDp / columnWidthDp + 0.5);
        return noOfColumns;
    }

    public static int cslculateNoOfRows(Context context,float rowHeight){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenHeightDp = displayMetrics.heightPixels / displayDensity;
        noOfRows=(int) (screenHeightDp / rowHeight + 0.5);
        return noOfRows;
    }


    public static int getNoOfColumns() {
        return noOfColumns;
    }

    public static int getNoOfRows() {
        return noOfRows;
    }

    public static Bitmap getBitmapFromView(View view, int width, int height) {
        Log.d("View parameters", "width:" + width + ", Height:" + height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }

    public static class RSBlurProcessor {

        private static final boolean IS_BLUR_SUPPORTED = true;
        private static final int MAX_RADIUS = 25;
        private RenderScript rs;

        public RSBlurProcessor(RenderScript rs) {
            this.rs = rs;
        }

        @Nullable
        public Bitmap blur(@NonNull Bitmap bitmap, float radius, int repeat) {

            if (!IS_BLUR_SUPPORTED) {
                return null;
            }

            if (radius > MAX_RADIUS) {
                radius = MAX_RADIUS;
            }

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            // Create allocation type
            Type bitmapType = new Type.Builder(rs, Element.RGBA_8888(rs))
                    .setX(width)
                    .setY(height)
                    .setMipmaps(false) // We are using MipmapControl.MIPMAP_NONE
                    .create();

            // Create allocation
            Allocation allocation = Allocation.createTyped(rs, bitmapType);

            // Create blur script
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            blurScript.setRadius(radius);

            // Copy data to allocation
            allocation.copyFrom(bitmap);

            // set blur script input
            blurScript.setInput(allocation);

            // invoke the script to blur
            blurScript.forEach(allocation);

            // Repeat the blur for extra effect
            for (int i = 0; i < repeat; i++) {
                blurScript.forEach(allocation);
            }

            // copy data back to the bitmap
            allocation.copyTo(bitmap);

            // release memory
            allocation.destroy();
            blurScript.destroy();
            allocation = null;
            blurScript = null;

            return bitmap;
        }
    }
}
