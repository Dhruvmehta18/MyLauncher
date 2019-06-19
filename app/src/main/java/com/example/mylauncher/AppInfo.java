package com.example.mylauncher;

import android.graphics.drawable.Drawable;

class AppInfo {
    CharSequence label;
    CharSequence packageName;
    Drawable icon;

    AppInfo() {

    }

    AppInfo(String label, String packageName, Drawable icon) {
        this.label = label;
        this.icon = icon;
        this.packageName = packageName;
    }
}

