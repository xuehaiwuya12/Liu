/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package wuxian.com.liu;

import android.view.View;
import android.widget.TextView;
import java.util.Set;
import android.widget.EditText;
import java.lang.String;
import java.util.Arrays;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.util.Log;
import android.widget.CheckBox;
import android.text.Editable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import java.util.HashSet;
import android.view.KeyEvent;

public class OpenVPNImportProfile extends OpenVPNClientBase implements View.OnClickListener, HttpsClient.CancelDetect.I, TextView.OnEditorActionListener {
    private static final String TAG = "OpenVPNImportProfile";
    private int generation;
    private PrefUtil prefs;
    private Set<String> server_history;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generation = 0x1;
        setContentView(0x7f03000a);
        prefs = new PrefUtil(PreferenceManager.getDefaultSharedPreferences(this));
        server_history = prefs.get_string_set("import_server_history");
        if(server_history == null) {
            server_history = new HashSet();
        }
        Button import_button = (Button)findViewById(0x7f0a0081);
        Button cancel_button = (Button)findViewById(0x7f0a0082);
        import_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
        TextView password_edit = (TextView)findViewById(0x7f0a0055);
        password_edit.setOnEditorActionListener(this);
        set_ui_state(false);
        set_server_history_autocomplete();
        doBindService();
    }
    
    public int cancel_generation() {
        return generation;
    }
    
    public static void forget_server_history(PrefUtil prefs) {
        delete_key("import_server_history");
    }
    
    private void set_ui_state(boolean pending) {
        Button import_button = (Button)findViewById(0x7f0a0081);
        ProgressBar progress = (ProgressBar)findViewById(0x7f0a0083);
        if(pending) {
            import_button.setEnabled(false);
            progress.setVisibility(0x0);
            return;
        }
        import_button.setEnabled(true);
        progress.setVisibility(0x8);
    }
    
    private void set_server_history_autocomplete() {
        String[] sh = (String[])Arrays.copyOf(server_history.toArray(), server_history.size(), String.class);
        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(0x7f0a007c);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 0x7f03000b, sh);
        textView.setAdapter(adapter);
    }
    
    private void add_to_server_history(String server) {
        server_history.add(server);
        prefs.set_string_set("import_server_history", server_history);
    }
    
    public void onClick(View v) {
        Log.d("OpenVPNImportProfile", "onClick");
        int viewid = v.getId();
        if(viewid == 0x7f0a0081) {
            generation = (generation + 0x1);
            EditText server_edit = (EditText)findViewById(0x7f0a007c);
            EditText username_edit = (EditText)findViewById(0x7f0a0051);
            EditText password_edit = (EditText)findViewById(0x7f0a0055);
            CheckBox autologin_checkbox = (CheckBox)findViewById(0x7f0a007f);
            String server = server_edit.getText().toString();
            String username = username_edit.getText().toString();
            String password = password_edit.getText().toString();
            password = OpenVPNDebug.pw_repl(username, password);
            HttpsClient.AuthContext ac = new HttpsClient.AuthContext(server, username, password);
            if((server.length() > 0) && (username.length() > 0)) {
                importProfileRemote(ac, autologin_checkbox.isChecked(), this, new Runnable(this, server) {
                    
                    public void run() {
                        finish();
                    }
                }, new OpenVPNImportProfile.2(this), 0x0, true, true);
                set_ui_state(true);
            }
            return;
        }
        if(viewid == 0x7f0a0082) {
            generation = (generation + 0x1);
            finish();
        }
    }
    
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        TextView password_edit = (TextView)findViewById(0x7f0a0055);
        if((action_enter(actionId, event)) && (v == password_edit)) {
            Button import_button = (Button)findViewById(0x7f0a0081);
            onClick(import_button);
            return true;
        }
        return false;
    }
    
    private void clear_auth() {
        EditText password_edit = (EditText)findViewById(0x7f0a0055);
        if(password_edit != null) {
            password_edit.setText("");
        }
    }
    
    protected void onDestroy() {
        Log.d("OpenVPNImportProfile", "onDestroy");
        stop();
        super.onDestroy();
    }
    
    private void stop() {
        generation = (generation + 0x1);
        clear_auth();
        doUnbindService();
    }
    
    protected void post_bind() {
        Log.d("OpenVPNImportProfile", "post_bind");
    }
}
