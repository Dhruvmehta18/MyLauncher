package com.example.mylauncher;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    ScreenFragment screenFragment;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    static Drawable wallpaperDrawable;
    static Bitmap wallpaperBitmap;
    static Drawable blurWallpaper;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private View view;
    private View mainView;


    static Drawable getActivityIcon(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
        return resolveInfo.loadIcon(pm);
    }

    static CharSequence getAppLabel(Context context,String packageName,String activityName){
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityName));
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
        return resolveInfo.loadLabel(pm);
    }
    static Drawable getBlurWallpaper() {
        return blurWallpaper;
    }

    static Bitmap getWallpaperBitmap() {
        return wallpaperBitmap;
    }

    @Override
    public void onBackPressed() {
        Log.d("onback", String.valueOf(screenFragment.isOnBackPressedAvailable()));
        if (screenFragment.isOnBackPressedAvailable()) {
            screenFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    static Drawable getWallpaperDrawable() {
        return wallpaperDrawable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        view = findViewById(R.id.no_permission_layout);
        mainView = findViewById(R.id.main);
        if (getPermissionGrantStatus()) {
            proceedAfterPermission();
        } else {
            checkPermissions();
        }
    }

    private boolean getPermissionGrantStatus() {
        return ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void initialization() {
        screenFragment = new ScreenFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main, screenFragment)
                .commit();
        Wallpaper wallpaper = new Wallpaper(this);
        wallpaperDrawable = wallpaper.getWallPaperDrawable();
        wallpaperBitmap = wallpaper.getWallpaperBitmap();
        blurWallpaper = wallpaper.getDefaultBlurWallpaper();
    }

    private void checkPermissions() {
        if (!getPermissionGrantStatus()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                builder.setTitle("Need Storage Permission")
                        .setMessage("This app needs storage permission.")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                proceedAfterDeniedPermission();
                            }
                        })
                        .show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                builder.setTitle("Need Storage Permission")
                        .setMessage("This app needs storage permission.")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                sentToSettings = true;
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                proceedAfterDeniedPermission();
                            }
                        })
                        .show();

            } else {
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
        SharedPreferences.Editor editor = permissionStatus.edit();
        editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
        editor.apply();
    }

    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
        initialization();
    }

    private void proceedAfterDeniedPermission() {
        if (view.getVisibility() == View.GONE)
            view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                    builder.setTitle("Need Storage Permission")
                            .setMessage("This app needs storage permission.")
                            .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    proceedAfterDeniedPermission();
                                }
                            })
                            .show();
                } else {
                    proceedAfterDeniedPermission();
                    Snackbar.make(mainView, "Permission Denied", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (getPermissionGrantStatus()) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (getPermissionGrantStatus()) {
                //Got Permission
                proceedAfterPermission();
            } else {
                proceedAfterDeniedPermission();
            }
        }
    }

    public void grandPermissionButtonClick(View view) {
        checkPermissions();
    }
}