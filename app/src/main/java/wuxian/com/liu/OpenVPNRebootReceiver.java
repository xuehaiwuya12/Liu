/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package wuxian.com.liu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OpenVPNRebootReceiver extends BroadcastReceiver {
    private static final String TAG = "OpenVPNRebootReceiver";
    
    public void onReceive(Context context, Intent intent) {
        OpenVPNClientBase.autostart(context);
    }
}
