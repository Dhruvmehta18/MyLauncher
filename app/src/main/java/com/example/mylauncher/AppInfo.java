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

    CharSequence getLabel() {
        return label;
    }

    void setLabel(CharSequence label) {
        this.label = label;
    }

    CharSequence getPackageName() {
        return packageName;
    }

    void setPackageName(CharSequence packageName) {
        this.packageName = packageName;
    }

    Drawable getIcon() {
        return icon;
    }

    void setIcon(Drawable icon) {
        this.icon = icon;
    }
}

