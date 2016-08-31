/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package net.openvpn.openvpn;

import android.app.ListActivity;
import android.view.inputmethod.InputMethodManager;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.widget.EditText;
import java.util.ArrayList;
import android.widget.TextView;
import java.util.List;
import android.widget.Button;
import java.io.File;
import android.widget.ListView;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Collection;
import android.widget.SimpleAdapter;
import android.view.View;
import android.os.IBinder;
import android.os.Bundle;
import android.content.Intent;
import android.view.KeyEvent;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class FileDialog extends ListActivity {
    public static final String CAN_SELECT_DIR = "CAN_SELECT_DIR";
    public static final String FORMAT_FILTER = "FORMAT_FILTER";
    private static final String ITEM_IMAGE = "image";
    private static final String ITEM_KEY = "key";
    public static final int MODE_CREATE = 0x0;
    public static final int MODE_OPEN = 0x1;
    public static final String OPTION_CURRENT_PATH_IN_TITLEBAR = "OPTION_CURRENT_PATH_IN_TITLEBAR";
    public static final String OPTION_ONE_CLICK_SELECT = "OPTION_ONE_CLICK_SELECT";
    public static final String OPTION_PROMPT = "OPTION_PROMPT";
    public static final String RESULT_PATH = "RESULT_PATH";
    private static final String ROOT = "/";
    public static final String SELECTION_MODE = "SELECTION_MODE";
    public static final String START_PATH = "START_PATH";
    private boolean canSelectDir;
    private String currentPath;
    private String[] formatFilter;
    private InputMethodManager inputManager;
    private HashMap<String, Integer> lastPositions;
    private LinearLayout layoutCreate;
    private LinearLayout layoutSelect;
    private EditText mFileName;
    private ArrayList<HashMap<String, Object>> mList;
    private boolean m_bOneClickSelect;
    private boolean m_bTitlebarFolder;
    private TextView myPath;
    private TextView myPrompt;
    private String parentPath;
    private List<String> path;
    private Button selectButton;
    private File selectedFile;
    private int selectionMode;
    
    public FileDialog() {
        path = 0x0;
        currentPath = "/";
        selectionMode = 0x0;
        m_bTitlebarFolder = false;
        m_bOneClickSelect = false;
        formatFilter = 0x0;
        canSelectDir = false;
        lastPositions = new HashMap();
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(0x0, getIntent());
        setContentView(0x7f030007);
        myPath = (TextView)findViewById(0x7f0a0043);
        myPrompt = (TextView)findViewById(0x7f0a0042);
        mFileName = (EditText)findViewById(0x7f0a0040);
        m_bOneClickSelect = getIntent().getBooleanExtra("OPTION_ONE_CLICK_SELECT", m_bOneClickSelect);
        m_bTitlebarFolder = getIntent().getBooleanExtra("OPTION_CURRENT_PATH_IN_TITLEBAR", m_bTitlebarFolder);
        if(m_bTitlebarFolder) {
            myPath.setVisibility(0x8);
        }
        String prompt = getIntent().getStringExtra("OPTION_PROMPT");
        if(prompt != null) {
            myPrompt.setText(prompt);
            myPrompt.setVisibility(0x0);
        } else {
            myPrompt.setVisibility(0x8);
        }
        inputManager = (InputMethodManager)getSystemService("input_method");
        selectButton = (Button)findViewById(0x7f0a003c);
        selectButton.setEnabled(false);
        selectButton.setOnClickListener(new View.OnClickListener(this) {
            
            public void onClick(View v) {
                if(selectedFile != null) {
                    getIntent().putExtra("RESULT_PATH", selectedFile.getPath());
                    setResult(-0x1, getIntent());
                    finish();
                }
            }
        });
        Button newButton = (Button)findViewById(0x7f0a003b);
        newButton.setOnClickListener(new View.OnClickListener(this) {
            
            public void onClick(View v) {
                mFileName.setText("");
                mFileName.requestFocus();
            }
        });
        selectionMode = getIntent().getIntExtra("SELECTION_MODE", 0x0);
        formatFilter = getIntent().getStringArrayExtra("FORMAT_FILTER");
        canSelectDir = getIntent().getBooleanExtra("CAN_SELECT_DIR", false);
        if(selectionMode == 0x1) {
            newButton.setVisibility(0x8);
        }
        layoutSelect = (LinearLayout)findViewById(0x7f0a003a);
        layoutCreate = (LinearLayout)findViewById(0x7f0a003e);
        layoutCreate.setVisibility(0x8);
        if((selectionMode == 0x1) && (m_bOneClickSelect)) {
            layoutSelect.setVisibility(0x8);
        }
        Button cancelButton = (Button)findViewById(0x7f0a003d);
        cancelButton.setOnClickListener(new View.OnClickListener(this) {
            
            public void onClick(View v) {
                setResult(0x0, getIntent());
                finish();
            }
        });
        Button createButton = (Button)findViewById(0x7f0a0041);
        createButton.setOnClickListener(new View.OnClickListener(this) {
            
            public void onClick(View v) {
                if(mFileName.getText().length() > 0) {
                    getIntent().putExtra("RESULT_PATH", currentPath + "/" + mFileName.getText());
                    setResult(-0x1, getIntent());
                    finish();
                }
            }
        });
        String startPath = getIntent().getStringExtra("START_PATH");
        startPath != null ? startPath : ;
        if(canSelectDir) {
            File file = new File(startPath);
            selectedFile = file;
            selectButton.setEnabled(true);
        }
        getDir(startPath);
    }
    
    private void getDir(String dirPath) {
        boolean useAutoSelection = dirPath.length() < currentPath.length();
        Integer position = (Integer)lastPositions.get(parentPath);
        getDirImpl(dirPath);
        if((position != null) && (useAutoSelection)) {
            getListView().setSelection(position.intValue());
        }
    }
    
    private void showLocation(int res_id_prefix, String currentPath) {
        if(m_bTitlebarFolder) {
            setTitle(currentPath);
            return;
        }
        myPath.setText(getText(res_id_prefix) + ": " + currentPath);
    }
    
    private void getDirImpl(String dirPath) {
        // :( Parsing error. Please contact me.
    }
    
    private void addItem(String fileName, int imageId) {
        HashMap<String, Object> item = new HashMap<String, Object>();
        item.put("key", fileName);
        item.put("image", Integer.valueOf(imageId));
        mList.add(item);
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File((String)path.get(position));
        setSelectVisible(v);
        if(file.isDirectory()) {
            selectButton.setEnabled(false);
            if(file.canRead()) {
                lastPositions.put(currentPath, Integer.valueOf(position));
                getDir((String)path.get(position));
                if(canSelectDir) {
                    selectedFile = file;
                    v.setSelected(true);
                    selectButton.setEnabled(true);
                }
                return;
            }
            new AlertDialog.Builder(this).setIcon(0x7f020005).setTitle("[" + file.getName() + "] " + getText(0x7f0600e4)).setPositiveButton("OK", new FileDialog.5(this)).show();
            return;
        }
        selectedFile = file;
        v.setSelected(true);
        selectButton.setEnabled(true);
        showLocation(0x7f0600e6, file.getPath());
        if(m_bOneClickSelect) {
            selectButton.performClick();
        }
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 0x4) {
            selectButton.setEnabled(false);
            if(layoutCreate.getVisibility() == 0) {
                layoutCreate.setVisibility(0x8);
                layoutSelect.setVisibility(0x0);
            } else if(!currentPath.equals("/")) {
                getDir(parentPath);
            } else {
                return super.onKeyDown(keyCode, event);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private void setCreateVisible(View v) {
        layoutCreate.setVisibility(0x0);
        layoutSelect.setVisibility(0x8);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0x0);
        selectButton.setEnabled(false);
    }
    
    private void setSelectVisible(View v) {
        if(m_bOneClickSelect) {
            return;
        }
        layoutCreate.setVisibility(0x8);
        layoutSelect.setVisibility(0x0);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0x0);
        selectButton.setEnabled(false);
    }
}