/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package wuxian.com.liu;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;

public class AppRate implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    public static final String PREF_DATE_FIRST_LAUNCH = "date_firstlaunch";
    public static final String PREF_DONT_SHOW_AGAIN = "dont_show_again";
    public static final String PREF_LAUNCH_COUNT = "launch_count";
    public static final String SHARED_PREFS_NAME = "apprate_prefs";
    private static final String TAG = "AppRate";
    private Activity hostActivity;
    private SharedPreferences preferences;
    private AlertDialog.Builder dialogBuilder = null;
    private long minLaunchesUntilPrompt = 10;
    private long minDaysUntilPrompt = 7;
    
    public AppRate(Activity hostActivity) {
        hostActivity = hostActivity;
        preferences = hostActivity.getSharedPreferences("apprate_prefs", 0x0);
    }
    
    public AppRate setMinLaunchesUntilPrompt(long minLaunchesUntilPrompt) {
        minLaunchesUntilPrompt = minLaunchesUntilPrompt;
        return this;
    }
    
    public AppRate setMinDaysUntilPrompt(long minDaysUntilPrompt) {
        minDaysUntilPrompt = minDaysUntilPrompt;
        return this;
    }
    
    public AppRate setCustomDialog(AlertDialog.Builder customBuilder) {
        dialogBuilder = customBuilder;
        return this;
    }
    
    public static void reset(Context context) {
        getSharedPreferences("apprate_prefs", 0x0).edit().clear().commit();
        Log.d("AppRate", "reset");
    }
    
    public void init() {
        Log.d("AppRate", "init");
        if(preferences.getBoolean("dont_show_again", false)) {
            return;
        }
        SharedPreferences.Editor editor = preferences.edit();
        long launch_count = preferences.getLong("launch_count", 0x0) + 0x1;
        editor.putLong("launch_count", launch_count);
        Long date_firstLaunch = Long.valueOf(preferences.getLong("date_firstlaunch", 0x0));
        if(date_firstLaunch.longValue() == 0x0) {
            date_firstLaunch = Long.valueOf(System.currentTimeMillis());
            editor.putLong("date_firstlaunch", date_firstLaunch.longValue());
        }
        if(launch_count >= minLaunchesUntilPrompt) {
            if(System.currentTimeMillis() >= (date_firstLaunch.longValue() + (minDaysUntilPrompt * 0x5265c00))) {
                if(dialogBuilder != null) {
                    showDialog(dialogBuilder);
                } else {
                    showDefaultDialog();
                }
            }
        }
        editor.commit();
    }
    
    private void showDefaultDialog() {
        Log.d("AppRate", "create default dialog");
        String appName = getApplicationName(hostActivity.getApplicationContext());
        String title = String.format(resString(0x7f060102), appName);
        String message = String.format(resString(0x7f060103), appName, appName);
        String rate = resString(0x7f060104);
        String remindLater = resString(0x7f060105);
        String dismiss = resString(0x7f060106);
        new AlertDialog.Builder(hostActivity).setTitle(title).setMessage(message).setPositiveButton(rate, this).setNegativeButton(dismiss, this).setNeutralButton(remindLater, this).setOnCancelListener(this).create().show();
    }
    
    private void showDialog(AlertDialog.Builder builder) {
        Log.d("AppRate", "create custom dialog");
        AlertDialog dialog = builder.create();
        dialog.show();
        String rate = (String)dialog.getButton(-0x1).getText();
        String remindLater = (String)dialog.getButton(-0x3).getText();
        String dismiss = (String)dialog.getButton(-0x2).getText();
        dialog.setButton(-0x1, rate, this);
        dialog.setButton(-0x3, remindLater, this);
        dialog.setButton(-0x2, dismiss, this);
        dialog.setOnCancelListener(this);
    }
    
    public void onCancel(DialogInterface dialog) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("date_firstlaunch", System.currentTimeMillis());
        editor.putLong("launch_count", 0x0);
        editor.commit();
    }
    
    public void onClick(DialogInterface dialog, int which) {
        SharedPreferences.Editor editor = preferences.edit();
        switch(which) {
            case -1:
            {
                new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + hostActivity.getPackageName()));
                hostActivity.startActivity(localIntent1);
                editor.putBoolean("dont_show_again", true);
                break;
            }
            case -2:
            {
                editor.putBoolean("dont_show_again", true);
                break;
            }
            case -3:
            {
                editor.putLong("date_firstlaunch", System.currentTimeMillis());
                editor.putLong("launch_count", 0x0);
                break;
            }
        }
        editor.commit();
        dialog.dismiss();
    }
    
    private static final String getApplicationName(Context context) {
        PackageManager packageManager = getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0x0);
        } catch(PackageManager.NameNotFoundException e) {
            int applicationInfo = 0x0;
        }
        applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : ;
        return "(unknown)";
    }
    
    private String resString(int res_id) {
        return hostActivity.getResources().getString(res_id);
    }
}
