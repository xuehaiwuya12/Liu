/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package wuxian.com.liu;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ProgressBar;
import android.os.Handler;
import android.widget.ImageView;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.view.inputmethod.InputMethodManager;
import android.os.IBinder;
import android.text.Editable;
import android.content.Context;
import android.view.MenuItem;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ContextMenu;
import android.util.Log;
import android.content.ActivityNotFoundException;
import android.net.VpnService;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.app.PendingIntent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.KeyEvent;
import android.net.Uri;
import android.view.MotionEvent;

public class OpenVPNClient extends OpenVPNClientBase implements View.OnClickListener, View.OnTouchListener, AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener {
    private static final int REQUEST_IMPORT_PKCS12 = 0x3;
    private static final int REQUEST_IMPORT_PROFILE = 0x2;
    private static final int REQUEST_VPN_ACTOR_RIGHTS = 0x1;
    private static final boolean RETAIN_AUTH = false;
    private static final int S_BIND_CALLED = 0x1;
    private static final int S_ONSTART_CALLED = 0x2;
    private static final String TAG = "OpenVPNClient";
    private static final int UIF_PROFILE_SETTING_FROM_SPINNER = 0x40000;
    private static final int UIF_REFLECTED = 0x20000;
    private static final int UIF_RESET = 0x10000;
    private static final boolean UI_OVERLOADED;
    private String autostart_profile_name;
    private View button_group;
    private TextView bytes_in_view;
    private TextView bytes_out_view;
    private TextView challenge_view;
    private View conn_details_group;
    private Button connect_button;
    private View cr_group;
    private TextView details_more_less;
    private Button disconnect_button;
    private TextView duration_view;
    private OpenVPNClient.FinishOnConnect finish_on_connect;
    private View info_group;
    private boolean last_active;
    private TextView last_pkt_recv_view;
    private ScrollView main_scroll_view;
    private EditText password_edit;
    private View password_group;
    private CheckBox password_save_checkbox;
    private EditText pk_password_edit;
    private View pk_password_group;
    private CheckBox pk_password_save_checkbox;
    private View post_import_help_blurb;
    private PrefUtil prefs;
    private ImageButton profile_edit;
    private View profile_group;
    private Spinner profile_spin;
    private ProgressBar progress_bar;
    private ImageButton proxy_edit;
    private View proxy_group;
    private Spinner proxy_spin;
    private PasswordUtil pwds;
    private EditText response_edit;
    private View server_group;
    private Spinner server_spin;
    private int startup_state;
    private View stats_expansion_group;
    private View stats_group;
    private Handler stats_timer_handler;
    private Runnable stats_timer_task;
    private ImageView status_icon_view;
    private TextView status_view;
    private boolean stop_service_on_client_exit;
    private View[] textgroups;
    private TextView[] textviews;
    private Handler ui_reset_timer_handler;
    private Runnable ui_reset_timer_task;
    private EditText username_edit;
    private View username_group;
    
    public OpenVPNClient() {
        stop_service_on_client_exit = false;
        startup_state = 0x0;
        finish_on_connect = OpenVPNClient.FinishOnConnect.DISABLED;
        last_active = false;
        stats_timer_handler = new Handler();
        stats_timer_task = new Runnable(this) {
            
            public void run() {
            }
        };
        ui_reset_timer_handler = new Handler();
        ui_reset_timer_task = new Runnable(this) {
            
            public void run() {
                if(!is_active()) {
                }
            }
        };
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Log.d("OpenVPNClient", String.format("CLI: onCreate intent=%s", intent.toString()));
        prefs = new PrefUtil(PreferenceManager.getDefaultSharedPreferences(this));
        pwds = new PasswordUtil(PreferenceManager.getDefaultSharedPreferences(this));
        init_default_preferences(prefs);
        setContentView(0x7f030009);
        load_ui_elements();
        doBindService();
        warn_app_expiration(prefs);
        new AppRate(this).setMinDaysUntilPrompt(0xe).setMinLaunchesUntilPrompt(0xa).init();
    }
    
    protected void onNewIntent(Intent intent) {
        Log.d("OpenVPNClient", String.format("CLI: onNewIntent intent=%s", intent.toString()));
        setIntent(intent);
    }
    
    protected void post_bind() {
        Log.d("OpenVPNClient", "CLI: post bind");
        startup_state = startup_state;
        process_autostart_intent(is_active());
        render_last_event();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(0x7f090000, menu);
        return true;
    }
    
    public void event(OpenVPNService.EventMsg ev) {
        boolean active = is_active();
        render_event(ev, false, active, false);
    }
    
    private void render_last_event() {
        boolean active = is_active();
        OpenVPNService.EventMsg ev = get_last_event();
        if(ev != null) {
            render_event(ev, true, active, true);
        } else if(n_profiles_loaded() > 0) {
            ev = OpenVPNService.EventMsg.disconnected();
            render_event(ev, true, active, true);
        } else {
            hide_status();
            ui_setup(active, 0x10000, 0x0);
            show_progress(0x0, active);
        }
        OpenVPNService.EventMsg pev = get_last_event_prof_manage();
        if(pev != null) {
            render_event(pev, true, active, true);
        }
    }
    
    private boolean show_conn_info_field(String text, int field_id, int row_id) {
        TextView tv = (TextView)findViewById(field_id);
        View row = findViewById(row_id);
        tv.setText(text);
        row.setVisibility(vis ? 0x0 : 0x8);
        return vis;
    }
    
    private void reset_conn_info() {
        show_conn_info(new ClientAPI_ConnectionInfo());
    }
    
    private void show_conn_info(ClientAPI_ConnectionInfo ci) {
        boolean onevis = 0x0;
        onevis |= show_conn_info_field(ci.getVpnIp4(), 0x7f0a006b, 0x7f0a006a);
        onevis |= show_conn_info_field(ci.getVpnIp6(), 0x7f0a006d, 0x7f0a006c);
        onevis |= show_conn_info_field(ci.getUser(), 0x7f0a006f, 0x7f0a006e);
        onevis |= show_conn_info_field(ci.getClientIp(), 0x7f0a0071, 0x7f0a0070);
        onevis |= show_conn_info_field(ci.getServerHost(), 0x7f0a0073, 0x7f0a0072);
        onevis |= show_conn_info_field(ci.getServerIp(), 0x7f0a0075, 0x7f0a0074);
        onevis |= show_conn_info_field(ci.getServerPort(), 0x7f0a0077, 0x7f0a0076);
        onevis |= show_conn_info_field(ci.getServerProto(), 0x7f0a0079, 0x7f0a0078);
        info_group.setVisibility(onevis ? 0x0 : 0x8);
        set_visibility_stats_expansion_group();
    }
    
    private void set_visibility_stats_expansion_group() {
        boolean expand_stats = prefs.get_boolean("expand_stats", false);
        stats_expansion_group.setVisibility(expand_stats ? 0x0 : 0x8);
        details_more_less.setText(expand_stats ? 0x7f0600a8 : 0x7f0600a7);
    }
    
    private void render_event(OpenVPNService.EventMsg ev, boolean reset, boolean active, boolean cached) {
        // :( Parsing error. Please contact me.
    }
    
    private void stop_service() {
        submitDisconnectIntent(true);
    }
    
    private void stop() {
        cancel_stats();
        doUnbindService();
        if(stop_service_on_client_exit) {
            Log.d("OpenVPNClient", "CLI: stopping service");
            stop_service();
        }
    }
    
    protected void onStop() {
        Log.d("OpenVPNClient", "CLI: onStop");
        cancel_stats();
        super.onStop();
    }
    
    protected void onStart() {
        super.onStart();
        Log.d("OpenVPNClient", "CLI: onStart");
        startup_state = startup_state;
        if(finish_on_connect == OpenVPNClient.FinishOnConnect.ENABLED) {
            finish_on_connect = OpenVPNClient.FinishOnConnect.ENABLED_ACROSS_ONSTART;
        }
        boolean active = is_active();
        if(active) {
            schedule_stats();
        }
        if(process_autostart_intent(active)) {
            ui_setup(active, 0x10000, 0x0);
        }
    }
    
    protected void onDestroy() {
        stop();
        Log.d("OpenVPNClient", "CLI: onDestroy called");
        super.onDestroy();
    }
    
    private boolean process_autostart_intent(boolean active) {
        if((startup_state & 0x3) == 0x3) {
            Intent intent = getIntent();
            String apn = intent.getStringExtra(apn_key);
            if(apn != null) {
                autostart_profile_name = 0x0;
                Log.d("OpenVPNClient", String.format("CLI: autostart: %s", apn));
                intent.removeExtra(apn_key);
                if(active) {
                    OpenVPNService.Profile prof = current_profile();
                    if(!prof.get_name().equals(apn)) {
                        autostart_profile_name = apn;
                        submitDisconnectIntent(false);
                    }
                    return false;
                }
                OpenVPNService.ProfileList proflist = profile_list();
                if((proflist != null) && (proflist.get_profile_by_name(apn) != null)) {
                    autostart_profile_name = apn;
                    return true;
                }
                ok_dialog(resString(0x7f06002f), apn);
                return false;
            }
            // Parsing error may occure here :(
        }
        // Parsing error may occure here :(
    }
    
    private void cancel_ui_reset() {
        ui_reset_timer_handler.removeCallbacks(ui_reset_timer_task);
    }
    
    private void schedule_ui_reset(long delay) {
        cancel_ui_reset();
        ui_reset_timer_handler.postDelayed(ui_reset_timer_task, delay);
    }
    
    private void hide_status() {
        status_view.setVisibility(0x8);
    }
    
    private void show_status(String text) {
        status_view.setVisibility(0x0);
        status_view.setText(text);
    }
    
    private void show_status(int res_id) {
        status_view.setVisibility(0x0);
        status_view.setText(res_id);
    }
    
    private void show_status_icon(int res_id) {
        status_icon_view.setImageResource(res_id);
    }
    
    private void show_progress(int progress, boolean active) {
        if((progress > 0) && (progress < 0x63)) {
            progress_bar.setVisibility(0x0);
            progress_bar.setProgress(progress);
            return;
        }
        progress_bar.setVisibility(0x8);
    }
    
    private void cancel_stats() {
        stats_timer_handler.removeCallbacks(stats_timer_task);
    }
    
    private void schedule_stats() {
        cancel_stats();
        stats_timer_handler.postDelayed(stats_timer_task, 0x3e8);
    }
    
    private static String render_bandwidth(long bw) {
        float bwf = (float)bw;
        if(bwf >= 0x5368d4a5) {
            String postfix = "TB";
            float div = 1099511627776.0f;
        } else if(bwf >= 0x4e6e6b28) {
            String postfix = "GB";
            div = 1073741824.0f;
        } else if(bwf >= 0x49742400) {
            String postfix = "MB";
            div = 1048576.0f;
        } else if(bwf >= 1000.0f) {
            String postfix = "KB";
            div = 1024.0f;
        } else {
            return String.format("%.0f", Float.valueOf(bwf));
        }
        return String.format("%.2f %s", Float.valueOf(bwf), Float.valueOf((bwf / div)), postfix);
    }
    
    private String render_last_pkt_recv(int sec) {
        if(sec >= 0xe10) {
            return resString(0x7f0600a9);
        }
        if(sec >= 0x78) {
            return String.format(resString(0x7f0600aa), (sec / 0x3c);
        }
        if(sec >= 0x2) {
            return String.format(resString(0x7f0600ab), (sec / 0x3c, sec);
        }
        if(sec == 0x1) {
            return resString(0x7f0600ac);
        }
        if(sec == 0) {
            return resString(0x7f0600ad);
        }
        return "";
    }
    
    private void show_stats() {
        if(is_active()) {
            OpenVPNService.ConnectionStats stats = get_connection_stats();
            last_pkt_recv_view.setText(render_last_pkt_recv(stats.last_packet_received));
            duration_view.setText(render_duration(stats.duration));
            bytes_in_view.setText(render_bandwidth(stats.bytes_in));
            bytes_out_view.setText(render_bandwidth(stats.bytes_out));
        }
    }
    
    private void clear_stats() {
        last_pkt_recv_view.setText("");
        duration_view.setText("");
        bytes_in_view.setText("");
        bytes_out_view.setText("");
        reset_conn_info();
    }
    
    private int n_profiles_loaded() {
        OpenVPNService.ProfileList proflist = profile_list();
        if(proflist != null) {
            return proflist.size();
        }
        return 0x0;
    }
    
    private String selected_profile_name() {
        String ret = 0x0;
        OpenVPNService.ProfileList proflist = profile_list();
        if((proflist != null) && (proflist.size() > 0)) {
            if(proflist.size() == 0x1) {
                ret = (OpenVPNService.Profile)proflist.get(0x0).get_name();
            } else {
                ret = SpinUtil.get_spinner_selected_item(profile_spin);
            }
        }
        if(ret == null) {
            String ret = "UNDEFINED_PROFILE";
        }
        return ret;
    }
    
    private OpenVPNService.Profile selected_profile() {
        OpenVPNService.ProfileList proflist = profile_list();
        if(proflist != null) {
            return proflist.get_profile_by_name(selected_profile_name());
        }
        return null;
    }
    
    private void clear_auth() {
        username_edit.setText("");
        pk_password_edit.setText("");
        password_edit.setText("");
        response_edit.setText("");
    }
    
    private void ui_setup(boolean active, int flags, String profile_override) {
        // :( Parsing error. Please contact me.
    }
    
    private void set_enabled(EditText editText, boolean state) {
        editText.setEnabled(state);
        editText.setFocusable(state);
        editText.setFocusableInTouchMode(state);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        // :( Parsing error. Please contact me.
    }
    
    public void onClick(View v) {
        cancel_ui_reset();
        autostart_profile_name = 0x0;
        finish_on_connect = OpenVPNClient.FinishOnConnect.DISABLED;
        int viewid = v.getId();
        if(viewid == 0x7f0a005f) {
            start_connect();
            return;
        }
        if(viewid == 0x7f0a0060) {
            submitDisconnectIntent(false);
            return;
        }
        if((viewid == 0x7f0a004a) || (viewid == 0x7f0a004d)) {
            openContextMenu(v);
        }
    }
    
    private void start_connect() {
        cancel_ui_reset();
        Intent intent = VpnService.prepare(this);
        if(intent != null) {
            try {
                Log.d("OpenVPNClient", "CLI: requesting VPN actor rights");
                startActivityForResult(intent, 0x1);
                return;
            } catch(ActivityNotFoundException e) {
                Log.e("OpenVPNClient", "CLI: requesting VPN actor rights failed", e);
                ok_dialog(resString(0x7f0600fc), resString(0x7f0600fd));
                return;
            }
            Log.d("OpenVPNClient", "CLI: app is already authorized as VPN actor");
            resolve_epki_alias_then_connect();
        }
    }
    
    public boolean onTouch(View v, MotionEvent event) {
        if((v.getId() == 0x7f0a0062) && (event.getAction() == 0)) {
            boolean new_expand_stats = !prefs.get_boolean("expand_stats", false);
            prefs.set_boolean("expand_stats", new_expand_stats);
            set_visibility_stats_expansion_group();
            return true;
        }
        return false;
    }
    
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        cancel_ui_reset();
        int viewid = parent.getId();
        if(viewid == 0x7f0a0011) {
            ui_setup(is_active(), 0x50000, 0x0);
            return;
        }
        if(viewid == 0x7f0a004c) {
            ProxyList proxy_list = get_proxy_list();
            if(proxy_list != null) {
                String proxy_name = SpinUtil.get_spinner_list_item(proxy_spin, position);
                proxy_list.set_enabled(proxy_name);
                proxy_list.save();
                gen_ui_reset_event(true);
            }
            return;
        }
        if(viewid == 0x7f0a004f) {
            String server = SpinUtil.get_spinner_list_item(server_spin, position);
            String profile_name = SpinUtil.get_spinner_selected_item(profile_spin);
            prefs.set_string_by_profile(profile_name, "server", server);
            gen_ui_reset_event(true);
        }
    }
    
    public void onNothingSelected(AdapterView<?> parent) {
    }
    
    private void menu_add(ContextMenu menu, int id, boolean enabled, String menu_key) {
        MenuItem item = menu.add(0x0, id, 0x0, id).setEnabled(enabled);
        if(menu_key != null) {
            item.setIntent(new Intent().putExtra("net.openvpn.openvpn.MENU_KEY", menu_key));
        }
    }
    
    private String get_menu_key(MenuItem item) {
        if(item != null) {
            Intent intent = item.getIntent();
            if(intent != null) {
                return intent.getStringExtra("net.openvpn.openvpn.MENU_KEY");
            }
        }
        return null;
    }
    
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d("OpenVPNClient", "CLI: onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);
        int viewid = v.getId();
        if((!is_active()) && (viewid == 0x7f0a0011) || (viewid == 0x7f0a004a)) {
            OpenVPNService.Profile prof = selected_profile();
            if(prof != null) {
                String profile_name = prof.get_name();
                menu.setHeaderTitle(profile_name);
                menu_add(menu, 0x7f06007b, SpinUtil.get_spinner_count(profile_spin) > 0x1 ? 0x1 : SpinUtil.get_spinner_count(profile_spin) > 0x1 ? 0x1, 0x0);
                menu_add(menu, 0x7f06007c, true, profile_name);
                menu_add(menu, 0x7f06007d, prof.is_deleteable(), profile_name);
                menu_add(menu, 0x7f06007e, prof.is_renameable(), profile_name);
                menu_add(menu, 0x7f06007f, true, profile_name);
            } else {
                menu.setHeaderTitle(0x7f06007a);
            }
            menu_add(menu, 0x7f060080, true, 0x0);
            return;
        }
        if((!is_active()) && (viewid == 0x7f0a004c) || (viewid == 0x7f0a004d)) {
            ProxyList proxy_list = get_proxy_list();
            if(proxy_list != null) {
                String proxy_name = proxy_list.get_enabled(true);
                boolean is_none = proxy_list.is_none(proxy_name);
                menu.setHeaderTitle(proxy_name);
                menu_add(menu, 0x7f0600d2, (SpinUtil.get_spinner_count(proxy_spin) > 0x1), 0x0);
                menu_add(menu, 0x7f0600d3, (!is_none), proxy_name);
                menu_add(menu, 0x7f0600d4, !is_none ? 0x1 : !is_none ? 0x1, proxy_name);
                menu_add(menu, 0x7f0600d5, proxy_list.has_saved_creds(proxy_name), proxy_name);
            } else {
                menu.setHeaderTitle(0x7f0600d7);
            }
            menu_add(menu, 0x7f0600d6, true, 0x0);
        }
    }
    
    public boolean onContextItemSelected(MenuItem item) {
        // :( Parsing error. Please contact me.
    }
    
    private void launch_create_profile_shortcut_dialog(String prof_name) {
        View view = getLayoutInflater().inflate(0x7f030006, 0x0);
        EditText name_field = (EditText)view.findViewById(0x7f0a0012);
        name_field.setText(prof_name);
        name_field.selectAll();
        OpenVPNClient.4 dialogClickListener = new DialogInterface.OnClickListener(this, name_field, prof_name) {
            
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case -1:
                    {
                        String shortcut_name = name_field.getText().toString();
                        createConnectShortcut(prof_name, shortcut_name);
                        break;
                    }
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(0x7f060090).setView(view).setPositiveButton(0x7f060092, dialogClickListener).setNegativeButton(0x7f060093, dialogClickListener).show();
    }
    
    private void launch_rename_profile_dialog(String orig_prof_name) {
        View view = getLayoutInflater().inflate(0x7f03000e, 0x0);
        EditText name_field = (EditText)view.findViewById(0x7f0a008e);
        name_field.setText(orig_prof_name);
        name_field.selectAll();
        OpenVPNClient.5 dialogClickListener = new DialogInterface.OnClickListener(this, name_field, orig_prof_name) {
            
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case -1:
                    {
                        String new_prof_name = name_field.getText().toString();
                        submitRenameProfileIntent(orig_prof_name, new_prof_name);
                        break;
                    }
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(0x7f060086).setView(view).setPositiveButton(0x7f060087, dialogClickListener).setNegativeButton(0x7f060088, dialogClickListener).show();
    }
    
    private void delete_proxy_with_confirm(String proxy_name) {
        ProxyList proxy_list = get_proxy_list();
        OpenVPNClient.6 dialogClickListener = new DialogInterface.OnClickListener(this, proxy_list, proxy_name) {
            
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case -1:
                    {
                        if(proxy_list != null) {
                            proxy_list.remove(proxy_name);
                            proxy_list.save();
                            gen_ui_reset_event(false);
                            break;
                        }
                    }
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(0x7f0600d8).setMessage(proxy_name).setPositiveButton(0x7f0600d9, dialogClickListener).setNegativeButton(0x7f0600da, dialogClickListener).show();
    }
    
    private void forget_creds_with_confirm() {
        OpenVPNClient context = this;
        OpenVPNClient.7 dialogClickListener = new DialogInterface.OnClickListener(this, context) {
            
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case -1:
                    {
                        pwds.regenerate(true);
                        OpenVPNService.ProfileList proflist = profile_list();
                        if(proflist != null) {
                            proflist.forget_certs();
                        }
                        TrustMan.forget_certs(context);
                        OpenVPNImportProfile.forget_server_history(prefs);
                        ProxyList proxy_list = get_proxy_list();
                        if(proxy_list != null) {
                            proxy_list.forget_creds();
                            proxy_list.save();
                        }
                        break;
                    }
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(0x7f0600db).setMessage(0x7f0600dc).setPositiveButton(0x7f0600dd, dialogClickListener).setNegativeButton(0x7f0600de, dialogClickListener).show();
    }
    
    public PendingIntent get_configure_intent(int requestCode) {
        PendingIntent configure_intent = PendingIntent.getActivity(this, requestCode, getIntent(), 0x10000000);
        return configure_intent;
    }
    
    private void resolve_epki_alias_then_connect() {
        resolveExternalPkiAlias(selected_profile(), new OpenVPNClientBase.EpkiPost(this) {
            
            public void post_dispatch(String alias) {
            }
        });
    }
    
    private void do_connect(String epki_alias) {
        String app_name = "net.openvpn.connect.android";
        String proxy_name = 0x0;
        String server = 0x0;
        String username = 0x0;
        String password = 0x0;
        String pk_password = 0x0;
        String response = 0x0;
        boolean is_auth_pwd_save = 0x0;
        String profile_name = selected_profile_name();
        if(proxy_group.getVisibility() == 0) {
            ProxyList proxy_list = get_proxy_list();
            if(proxy_list != null) {
                proxy_name = proxy_list.get_enabled(false);
            }
        }
        if(server_group.getVisibility() == 0) {
            server = SpinUtil.get_spinner_selected_item(server_spin);
        }
        if(username_group.getVisibility() == 0) {
            username = username_edit.getText().toString();
            if(username.length() > 0) {
                prefs.set_string_by_profile(profile_name, "username", username);
            }
        }
        if(pk_password_group.getVisibility() == 0) {
            pk_password = pk_password_edit.getText().toString();
            boolean is_pk_pwd_save = pk_password_save_checkbox.isChecked();
            prefs.set_boolean_by_profile(profile_name, "pk_password_save", is_pk_pwd_save);
            if(is_pk_pwd_save) {
                pwds.set("pk", profile_name, pk_password);
            } else {
                pwds.remove("pk", profile_name);
            }
        }
        if(password_group.getVisibility() == 0) {
            password = password_edit.getText().toString();
            is_auth_pwd_save = password_save_checkbox.isChecked();
            prefs.set_boolean_by_profile(profile_name, "auth_password_save", is_auth_pwd_save);
            if(is_auth_pwd_save) {
                pwds.set("auth", profile_name, password);
            } else {
                pwds.remove("auth", profile_name);
            }
        }
        if(cr_group.getVisibility() == 0) {
            response = response_edit.getText().toString();
        }
        clear_auth();
        String vpn_proto = prefs.get_string("vpn_proto");
        String conn_timeout = prefs.get_string("conn_timeout");
        String compression_mode = prefs.get_string("compression_mode");
        clear_stats();
        submitConnectIntent(this, server, vpn_proto, conn_timeout, username, this, is_auth_pwd_save, this, response, epki_alias, compression_mode, this, 0x0, 0x0, true, get_gui_version(app_name));
    }
    
    private void import_profile(String path) {
        submitImportProfileViaPathIntent(path);
    }
    
    protected void onActivityResult(int request, int result, Intent data) {
        Log.d("OpenVPNClient", String.format("CLI: onActivityResult request=%d result=%d", request, result));
        switch(request) {
            case 1:
            {
                if(result == -0x1) {
                    resolve_epki_alias_then_connect();
                    return;
                }
                if(result == 0) {
                    if(finish_on_connect == OpenVPNClient.FinishOnConnect.ENABLED) {
                        finish();
                        return;
                    }
                    if(finish_on_connect == OpenVPNClient.FinishOnConnect.ENABLED_ACROSS_ONSTART) {
                        finish_on_connect = OpenVPNClient.FinishOnConnect.ENABLED;
                        start_connect();
                    }
                }
                return;
            }
            case 2:
            {
                if(result == -0x1) {
                    String path = data.getStringExtra("RESULT_PATH");
                    Log.d("OpenVPNClient", String.format("CLI: IMPORT_PROFILE: %s", request, result, path));
                    import_profile(path);
                }
                return;
            }
            case 3:
            {
                if(result == -0x1) {
                    path = data.getStringExtra("RESULT_PATH");
                    Log.d("OpenVPNClient", String.format("CLI: IMPORT_PKCS12: %s", request, result, path, path));
                    import_pkcs12(path);
                }
                return;
            }
        }
        super.onActivityResult(request, result, data);
    }
    
    private TextView last_visible_edittext() {
        for(int i = 0x0; i < textgroups.length; i = i + 0x1) {
            if(textgroups[i].getVisibility() == 0) {
                return textviews[i];
            }
        }
        return null;
    }
    
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(v == last_visible_edittext()) {
            if((action_enter(actionId, event)) && (connect_button.getVisibility() == 0)) {
                onClick(connect_button);
            }
            return true;
        }
        return false;
    }
    
    private void req_focus(EditText editText) {
        boolean auto_keyboard = prefs.get_boolean("auto_keyboard", false);
        if(editText != null) {
            editText.requestFocus();
            if(auto_keyboard) {
                raise_keyboard(editText);
            }
            return;
        }
        main_scroll_view.requestFocus();
        if(auto_keyboard) {
            dismiss_keyboard();
        }
    }
    
    private void raise_keyboard(EditText editText) {
        InputMethodManager mgr = (InputMethodManager)getSystemService("input_method");
        if(mgr != null) {
            mgr.showSoftInput(editText, 0x1);
        }
    }
    
    private void dismiss_keyboard() {
        InputMethodManager mgr = (InputMethodManager)getSystemService("input_method");
        if(mgr != null) {
            for(TextView tv : textviews) {
                mgr.hideSoftInputFromWindow(tv.getWindowToken(), 0x0);
            }
        }
    }
    
    private void load_ui_elements() {
        main_scroll_view = (ScrollView)findViewById(0x7f0a0046);
        post_import_help_blurb = findViewById(0x7f0a0047);
        profile_group = findViewById(0x7f0a0049);
        proxy_group = findViewById(0x7f0a004b);
        server_group = findViewById(0x7f0a004e);
        username_group = findViewById(0x7f0a0050);
        password_group = findViewById(0x7f0a0056);
        pk_password_group = findViewById(0x7f0a0053);
        cr_group = findViewById(0x7f0a0059);
        conn_details_group = findViewById(0x7f0a0061);
        stats_group = findViewById(0x7f0a0063);
        stats_expansion_group = findViewById(0x7f0a0068);
        info_group = findViewById(0x7f0a0069);
        button_group = findViewById(0x7f0a005e);
        profile_spin = (Spinner)findViewById(0x7f0a0011);
        profile_edit = (ImageButton)findViewById(0x7f0a004a);
        proxy_spin = (Spinner)findViewById(0x7f0a004c);
        proxy_edit = (ImageButton)findViewById(0x7f0a004d);
        server_spin = (Spinner)findViewById(0x7f0a004f);
        challenge_view = (TextView)findViewById(0x7f0a005a);
        username_edit = (EditText)findViewById(0x7f0a0051);
        password_edit = (EditText)findViewById(0x7f0a0055);
        pk_password_edit = (EditText)findViewById(0x7f0a0052);
        response_edit = (EditText)findViewById(0x7f0a0058);
        password_save_checkbox = (CheckBox)findViewById(0x7f0a0057);
        pk_password_save_checkbox = (CheckBox)findViewById(0x7f0a0054);
        status_view = (TextView)findViewById(0x7f0a005d);
        status_icon_view = (ImageView)findViewById(0x7f0a005c);
        progress_bar = (ProgressBar)findViewById(0x7f0a005b);
        connect_button = (Button)findViewById(0x7f0a005f);
        disconnect_button = (Button)findViewById(0x7f0a0060);
        details_more_less = (TextView)findViewById(0x7f0a007a);
        last_pkt_recv_view = (TextView)findViewById(0x7f0a0065);
        duration_view = (TextView)findViewById(0x7f0a0064);
        bytes_in_view = (TextView)findViewById(0x7f0a0066);
        bytes_out_view = (TextView)findViewById(0x7f0a0067);
        connect_button.setOnClickListener(this);
        disconnect_button.setOnClickListener(this);
        profile_spin.setOnItemSelectedListener(this);
        proxy_spin.setOnItemSelectedListener(this);
        server_spin.setOnItemSelectedListener(this);
        registerForContextMenu(profile_spin);
        registerForContextMenu(proxy_spin);
        findViewById(0x7f0a0062).setOnTouchListener(this);
        profile_edit.setOnClickListener(this);
        registerForContextMenu(profile_edit);
        proxy_edit.setOnClickListener(this);
        registerForContextMenu(proxy_edit);
        username_edit.setOnEditorActionListener(this);
        password_edit.setOnEditorActionListener(this);
        pk_password_edit.setOnEditorActionListener(this);
        response_edit.setOnEditorActionListener(this);
        textgroups = new View[] {cr_group, password_group, pk_password_group, username_group}
        textviews = new EditText[] {response_edit, password_edit, pk_password_edit, username_edit}
    }
}
