package com.example.mylauncher;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.RenderScript;


class Wallpaper {
    static Drawable wallpaperDrawable;
    static Bitmap wallpaperBitmap;
    static Drawable blurWallpaper;
    private Context context;

    Wallpaper(Context context) {
        this.context = context;
    }

    Drawable getWallPaperDrawable() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        wallpaperDrawable = wallpaperManager.getDrawable();
        return wallpaperDrawable;
    }

    Bitmap getWallpaperBitmap() {
        wallpaperBitmap = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        return wallpaperBitmap;
    }

    Drawable getDefaultBlurWallpaper() {
        RenderScript renderScript = RenderScript.create(context);
        Utility.RSBlurProcessor blurProcessor = new Utility.RSBlurProcessor(renderScript);
        blurWallpaper = new BitmapDrawable(context.getResources(), blurProcessor.blur(wallpaperBitmap, 15f, 1));
        return blurWallpaper;
    }

    Drawable getCustomBlurWallpaper(float radius, int repeat) {
        RenderScript renderScript = RenderScript.create(context);
        Utility.RSBlurProcessor blurProcessor = new Utility.RSBlurProcessor(renderScript);
        blurWallpaper = new BitmapDrawable(context.getResources(), blurProcessor.blur(wallpaperBitmap, radius, repeat));
        return blurWallpaper;
    }
}
