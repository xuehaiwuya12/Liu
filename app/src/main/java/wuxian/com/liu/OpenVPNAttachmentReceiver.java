/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package net.openvpn.openvpn;

import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.widget.TextView;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import java.io.IOException;

public class OpenVPNAttachmentReceiver extends OpenVPNClientBase implements View.OnClickListener {
    private static final String TAG = "OpenVPNAttachmentReceiver";
    private Button accept_button;
    private Button cancel_button;
    private Uri filePathUri;
    private OpenVPNService.MergedProfile profile;
    private String profileContent;
    private TextView profile_description;
    private TextView profile_name;
    private TextView profile_note;
    private OpenVPNAttachmentReceiver.ReadError readError;
    
    public OpenVPNAttachmentReceiver() {
        readError = OpenVPNAttachmentReceiver.ReadError.NONE;
    }
    
    private void setReadError(Exception e) {
        // :( Parsing error. Please contact me.
    }
    
    private String basename() {
        String bn = FileUtil.uriBasename(filePathUri);
        if((bn == null) || (!bn.endsWith(".ovpn"))) {
        }
        return bn;
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Log.d("OpenVPNAttachmentReceiver", String.format("onCreate intent=%s", intent.toString()));
        profileContent = 0x0;
        readError = OpenVPNAttachmentReceiver.ReadError.NONE;
        filePathUri = intent.getData();
        if(filePathUri != null) {
            Log.d("OpenVPNAttachmentReceiver", String.format("import path=%s", intent.toString(), filePathUri.toString()));
            if(filePathUri != null) {
                try {
                    String path = filePathUri.getPath();
                    if(path != null) {
                        profileContent = FileUtil.readFile(path, max_profile_size());
                    }
                } catch(IOException e) {
                    Log.e("OpenVPNAttachmentReceiver", "profile read error via file URI", e);
                    setReadError(e);
                }
                try {
                    if((profileContent == null) && (readError != OpenVPNAttachmentReceiver.ReadError.PROFILE_TOO_LARGE)) {
                        profileContent = FileUtil.readUri(this, filePathUri, max_profile_size());
                    }
                } catch(IOException e) {
                    Log.e("OpenVPNAttachmentReceiver", "profile read error via content URI", e);
                    setReadError(e);
                }
                
            }
        }
        if((filePathUri == null) || (profileContent == null)) {
            Log.i("OpenVPNAttachmentReceiver", filePathUri != null ? filePathUri.toString() : String.format("error reading profile from %s", intent.toString(), filePathUri.toString(), "NULL"));
            if((readError == OpenVPNAttachmentReceiver.ReadError.OTHER) || (readError == OpenVPNAttachmentReceiver.ReadError.NONE)) {
                do_finish();
            }
            return;
        }
        setContentView(0x7f030003);
        profile_name = (TextView)findViewById(0x7f0a0015);
        profile_description = (TextView)findViewById(0x7f0a0016);
        profile_note = (TextView)findViewById(0x7f0a0017);
        profile_note.setVisibility(0x8);
        accept_button = (Button)findViewById(0x7f0a0018);
        accept_button.setOnClickListener(this);
        accept_button.setEnabled(false);
        cancel_button = (Button)findViewById(0x7f0a0019);
        cancel_button.setOnClickListener(this);
        doBindService();
    }
    
    private void launch_main_page() {
        Intent i = new Intent(this, OpenVPNClient.class);
        i.addFlags(0x4000000);
        try {
            startActivity(i);
            return;
        } catch(ActivityNotFoundException e) {
            Log.e("OpenVPNAttachmentReceiver", "launch_main_page", e);
        }
    }
    
    private void do_finish() {
        launch_main_page();
        finish();
    }
    
    public void onClick(View v) {
        Log.d("OpenVPNAttachmentReceiver", "onClick");
        int viewid = v.getId();
        if(viewid == 0x7f0a0018) {
            Log.d("OpenVPNAttachmentReceiver", "Accept button");
            if((filePathUri != null) && (profile != null) && (profile.profile_content != null)) {
                submitImportProfileIntent(profile.profile_content, basename(), false);
            }
            do_finish();
            return;
        }
        if(viewid == 0x7f0a0019) {
            Log.d("OpenVPNAttachmentReceiver", "Cancel button");
            do_finish();
        }
    }
    
    protected void onDestroy() {
        Log.d("OpenVPNAttachmentReceiver", "onDestroy");
        doUnbindService();
        super.onDestroy();
    }
    
    protected void post_bind() {
        String error = 0x0;
        Log.d("OpenVPNAttachmentReceiver", "post_bind");
        if((filePathUri != null) && (profileContent != null)) {
            profile = merge_parse_profile(basename(), profileContent);
            if(profile != null) {
                error = profile.get_error();
                if(error == null) {
                    String name = profile.get_name();
                    String description = profile.get_type_string();
                    profile_name.setText(name);
                    profile_description.setText(description);
                    accept_button.setEnabled(true);
                    OpenVPNService.ProfileList proflist = profile_list();
                    if(exists) {
                        profile_note.setText(resString(0x7f0600c1));
                        profile_note.setVisibility(0x0);
                    }
                }
            } else {
            }
            if(error != null) {
                setError(error);
            }
            return;
        }
        if(readError == OpenVPNAttachmentReceiver.ReadError.PROFILE_TOO_LARGE) {
            setError(String.format(resString(0x7f0600c2), max_profile_size());
            return;
        }
        Log.i("OpenVPNAttachmentReceiver", "internal error in post_bind");
        do_finish();
    }
    
    private void setError(String text) {
        if(filePathUri != null) {
            profile_name.setText(basename());
        }
        profile_description.setText(text);
        profile_description.setTextColor(Color.parseColor("#ff8080"));
        profile_description.setTypeface(0x0, 0x1);
    }
}