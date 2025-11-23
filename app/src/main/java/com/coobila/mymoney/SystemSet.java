package com.coobila.mymoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class SystemSet extends AppCompatActivity {
    private String AccountPassword;
    private SQLiteDatabase DataDB;
    private Boolean RegStyle;
    private String SQL;
    private Cursor cursor;
    private double AccountId = 0.0d;
    private String AccountName = "";
    private String ShowVibrate = "";
    private String AccountPasswordMessage = "";
    private String InputPassword = "";
    double ErrorPassword = 0.0d;

    private File appDBPath;

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        Intent intent = new Intent();
        intent.setClass(this, MyMoneyZeroActivity.class);
        startActivity(intent);
        finish();
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.systemset);
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND DATA_NOTE = '使用帳本'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        this.AccountId = 0.0d;
        if (this.cursor.moveToNext()) {
            this.AccountId = this.cursor.getDouble(0);
        }
        this.cursor.close();
        this.SQL = "SELECT ACCOUNT_NOTE FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId);
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.AccountName = this.cursor.getString(0);
        }
        this.cursor.close();
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '強制直式顯示'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext() && this.cursor.getString(0).trim().equals("1")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        this.cursor.close();
        if (this.AccountId == 0.0d) {
            ShowBox("訊息", "您尚未選擇欲作業的帳本");
            this.DataDB.close();
            return;
        }
        setTitle(String.valueOf(this.AccountName) + " - 系統設定");
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '按鈕振動提醒功能'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            if (this.cursor.getString(0).trim().equals("1")) {
                this.ShowVibrate = "1";
            } else {
                this.ShowVibrate = "0";
            }
        }
        this.cursor.close();
        GridView gridview = (GridView) findViewById(R.id.gridview);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("ItemImage", Integer.valueOf(R.drawable.paste));
        map1.put("ItemText", "常用摘要");
        lstImageItem.add(map1);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("ItemImage", Integer.valueOf(R.drawable.contact));
        map2.put("ItemText", "收付人");
        lstImageItem.add(map2);
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("ItemImage", Integer.valueOf(R.drawable.tip));
        map3.put("ItemText", "專案設定");
        lstImageItem.add(map3);
        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("ItemImage", Integer.valueOf(R.drawable.book));
        map4.put("ItemText", "帳本維護");
        lstImageItem.add(map4);
        HashMap<String, Object> map5 = new HashMap<>();
        map5.put("ItemImage", Integer.valueOf(R.drawable.subset));
        map5.put("ItemText", "金額小數點");
        lstImageItem.add(map5);
        HashMap<String, Object> map6 = new HashMap<>();
        map6.put("ItemImage", Integer.valueOf(R.drawable.subsetex));
        map6.put("ItemText", "匯率小數點");
        lstImageItem.add(map6);
        HashMap<String, Object> map7 = new HashMap<>();
        map7.put("ItemImage", Integer.valueOf(R.drawable.replace));
        map7.put("ItemText", "預算設定");
        lstImageItem.add(map7);
        HashMap<String, Object> map11 = new HashMap<>();
        map11.put("ItemImage", Integer.valueOf(R.drawable.android));
        map11.put("ItemText", "其它設定");
        lstImageItem.add(map11);
        HashMap<String, Object> map8 = new HashMap<>();
        map8.put("ItemImage", Integer.valueOf(R.drawable.calendar1));
        map8.put("ItemText", "發票對獎");
        lstImageItem.add(map8);
        HashMap<String, Object> map10 = new HashMap<>();
        map10.put("ItemImage", Integer.valueOf(R.drawable.databack));
        map10.put("ItemText", "資料備份");
        lstImageItem.add(map10);
        HashMap<String, Object> map9 = new HashMap<>();
        map9.put("ItemImage", Integer.valueOf(R.drawable.system_file_manager));
        map9.put("ItemText", "備份回存");
        lstImageItem.add(map9);
        HashMap<String, Object> map12 = new HashMap<>();
        map12.put("ItemImage", Integer.valueOf(R.drawable.edit1));
        map12.put("ItemText", "同步匯出");
        lstImageItem.add(map12);
        HashMap<String, Object> map13 = new HashMap<>();
        map13.put("ItemImage", Integer.valueOf(R.drawable.export));
        map13.put("ItemText", "帳務匯出");
        lstImageItem.add(map13);
        HashMap<String, Object> map14 = new HashMap<>();
        map14.put("ItemImage", Integer.valueOf(R.drawable.key));
        map14.put("ItemText", "密碼設定");
        lstImageItem.add(map14);
        HashMap<String, Object> map15 = new HashMap<>();
        map15.put("ItemImage", Integer.valueOf(R.drawable.look));
        map15.put("ItemText", "註冊升級");
        lstImageItem.add(map15);
        HashMap<String, Object> map16 = new HashMap<>();
        map16.put("ItemImage", Integer.valueOf(R.drawable.getinfo));
        map16.put("ItemText", "關於系統");
        lstImageItem.add(map16);
        HashMap<String, Object> map17 = new HashMap<>();
        map17.put("ItemImage", Integer.valueOf(R.drawable.ies));
        map17.put("ItemText", "逛逛官網");
        lstImageItem.add(map17);
        HashMap<String, Object> map18 = new HashMap<>();
        map18.put("ItemImage", Integer.valueOf(R.drawable.home));
        map18.put("ItemText", "回主畫面");
        lstImageItem.add(map18);
        SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem, R.layout.grid_view_item, new String[]{"ItemImage", "ItemText"}, new int[]{R.id.ItemImage, R.id.ItemText});
        gridview.setAdapter((ListAdapter) saImageItems);
        gridview.setOnItemClickListener(new ItemClickListener());
    }

    class ItemClickListener implements AdapterView.OnItemClickListener {
        ItemClickListener() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            HashMap<String, Object> item = (HashMap) arg0.getItemAtPosition(arg2);
            if (item.get("ItemText").equals("常用摘要")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(30L);
                    } catch (Exception e) {
                    }
                }
                try {
//                    String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e2) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet = SystemSet.this;
                systemSet.InputPassword = new StringBuilder(String.valueOf(systemSet.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e3) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet2 = SystemSet.this;
                systemSet2.AccountPassword = new StringBuilder(String.valueOf(systemSet2.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
//                    Intent intent1 = new Intent();
//                    intent1.setClass(SystemSet.this, OftenNote.class);
//                    SystemSet.this.startActivity(intent1);
//                    SystemSet.this.finish();
                }
            }
            if (item.get("ItemText").equals("收付人")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator2 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator2.vibrate(30L);
                    } catch (Exception e4) {
                    }
                }
                try {
//                    String tSDCardPath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath2 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath2) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e5) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet3 = SystemSet.this;
                systemSet3.InputPassword = new StringBuilder(String.valueOf(systemSet3.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e6) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet4 = SystemSet.this;
                systemSet4.AccountPassword = new StringBuilder(String.valueOf(systemSet4.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
//                    Intent intent12 = new Intent();
//                    intent12.setClass(SystemSet.this, CollPayName.class);
//                    SystemSet.this.startActivity(intent12);
//                    SystemSet.this.finish();
                }
            }
            if (item.get("ItemText").equals("專案設定")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator3 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator3.vibrate(30L);
                    } catch (Exception e7) {
                    }
                }
                try {
//                    String tSDCardPath3 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath3 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath3) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e8) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet5 = SystemSet.this;
                systemSet5.InputPassword = new StringBuilder(String.valueOf(systemSet5.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e9) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet6 = SystemSet.this;
                systemSet6.AccountPassword = new StringBuilder(String.valueOf(systemSet6.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
//                    Intent intent13 = new Intent();
//                    intent13.setClass(SystemSet.this, ProjectSet.class);
//                    SystemSet.this.startActivity(intent13);
//                    SystemSet.this.finish();
                }
            }
            if (item.get("ItemText").equals("帳本維護")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator4 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator4.vibrate(30L);
                    } catch (Exception e10) {
                    }
                }
                try {
//                    String tSDCardPath4 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath4 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath4) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e11) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet7 = SystemSet.this;
                systemSet7.InputPassword = new StringBuilder(String.valueOf(systemSet7.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e12) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet8 = SystemSet.this;
                systemSet8.AccountPassword = new StringBuilder(String.valueOf(systemSet8.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
//                    Intent intent14 = new Intent();
//                    intent14.setClass(SystemSet.this, AccountData.class);
//                    SystemSet.this.startActivity(intent14);
//                    SystemSet.this.finish();
                }
            }
            if (item.get("ItemText").equals("金額小數點")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator5 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator5.vibrate(30L);
                    } catch (Exception e13) {
                    }
                }
                try {
//                    String tSDCardPath5 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath5 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath5) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e14) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet9 = SystemSet.this;
                systemSet9.InputPassword = new StringBuilder(String.valueOf(systemSet9.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e15) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet10 = SystemSet.this;
                systemSet10.AccountPassword = new StringBuilder(String.valueOf(systemSet10.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
//                    Intent intent15 = new Intent();
//                    intent15.setClass(SystemSet.this, MountSubSet.class);
//                    SystemSet.this.startActivity(intent15);
//                    SystemSet.this.finish();
                }
            }
            if (item.get("ItemText").equals("匯率小數點")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator6 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator6.vibrate(30L);
                    } catch (Exception e16) {
                    }
                }
                try {
//                    String tSDCardPath6 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath6 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath6) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e17) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet11 = SystemSet.this;
                systemSet11.InputPassword = new StringBuilder(String.valueOf(systemSet11.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e18) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet12 = SystemSet.this;
                systemSet12.AccountPassword = new StringBuilder(String.valueOf(systemSet12.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
//                    Intent intent16 = new Intent();
//                    intent16.setClass(SystemSet.this, ExchangeSubSet.class);
//                    SystemSet.this.startActivity(intent16);
//                    SystemSet.this.finish();
                }
            }
            if (item.get("ItemText").equals("預算設定")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator7 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator7.vibrate(30L);
                    } catch (Exception e19) {
                    }
                }
                try {
//                    String tSDCardPath7 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath7 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath7) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e20) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet13 = SystemSet.this;
                systemSet13.InputPassword = new StringBuilder(String.valueOf(systemSet13.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e21) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet14 = SystemSet.this;
                systemSet14.AccountPassword = new StringBuilder(String.valueOf(systemSet14.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
//                    Intent intent17 = new Intent();
//                    intent17.setClass(SystemSet.this, YearSpandSet.class);
//                    SystemSet.this.startActivity(intent17);
//                    SystemSet.this.finish();
                }
            }
            if (item.get("ItemText").equals("發票對獎")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator8 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator8.vibrate(30L);
                    } catch (Exception e22) {
                    }
                }
                try {
//                    String tSDCardPath8 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath8 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath8) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e23) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet15 = SystemSet.this;
                systemSet15.InputPassword = new StringBuilder(String.valueOf(systemSet15.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e24) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet16 = SystemSet.this;
                systemSet16.AccountPassword = new StringBuilder(String.valueOf(systemSet16.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
//                    Intent intent18 = new Intent();
//                    intent18.setClass(SystemSet.this, InvoiceCheck.class);
//                    SystemSet.this.startActivity(intent18);
//                    SystemSet.this.finish();
                }
            }
            if (item.get("ItemText").equals("備份回存")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator9 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator9.vibrate(30L);
                    } catch (Exception e25) {
                    }
                }
//                try {
//                    String tSDCardPath9 = Environment.getExternalStorageDirectory().getAbsolutePath();
//                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath9) + "/MyMoneyZero/mymoney.db", null, 0);
//                } catch (Exception e26) {
//                }
//                SystemSet.this.InputPassword = "";
//                SystemSet systemSet17 = SystemSet.this;
//                systemSet17.InputPassword = new StringBuilder(String.valueOf(systemSet17.InputPassword)).toString();
//                try {
//                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
//                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
//                    if (SystemSet.this.cursor.moveToNext()) {
//                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
//                    }
//                    SystemSet.this.cursor.close();
//                } catch (Exception e27) {
//                }
//                SystemSet.this.AccountPassword = "";
//                SystemSet systemSet18 = SystemSet.this;
//                systemSet18.AccountPassword = new StringBuilder(String.valueOf(systemSet18.AccountPassword)).toString();
//                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
//                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
//                if (SystemSet.this.cursor.moveToNext()) {
//                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
//                }
//                SystemSet.this.cursor.close();
//                SystemSet.this.DataDB.close();
//                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
//                    SystemSet.this.ShowInputPassword();
//                } else {
////                    Intent intent19 = new Intent();
////                    intent19.setClass(SystemSet.this, DatabaseRestore.class);
////                    SystemSet.this.startActivity(intent19);
////                    SystemSet.this.finish();
//                }
              chooseFileToImport();
            }
            if (item.get("ItemText").equals("其它設定")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator10 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator10.vibrate(30L);
                    } catch (Exception e28) {
                    }
                }
                try {
                    //String tSDCardPath10 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath10 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath10) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e29) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet19 = SystemSet.this;
                systemSet19.InputPassword = new StringBuilder(String.valueOf(systemSet19.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e30) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet20 = SystemSet.this;
                systemSet20.AccountPassword = new StringBuilder(String.valueOf(systemSet20.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
//                    Intent intent110 = new Intent();
//                    intent110.setClass(SystemSet.this, Sysset.class);
//                    SystemSet.this.startActivity(intent110);
//                    SystemSet.this.finish();
                }
            }
            if (item.get("ItemText").equals("同步匯出")) {
                try {
//                    String tSDCardPath11 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath11 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath11) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e31) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet21 = SystemSet.this;
                systemSet21.InputPassword = new StringBuilder(String.valueOf(systemSet21.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e32) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet22 = SystemSet.this;
                systemSet22.AccountPassword = new StringBuilder(String.valueOf(systemSet22.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
                    SystemSet.this.SentData();
                }
            }
            if (item.get("ItemText").equals("帳務匯出")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator11 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator11.vibrate(30L);
                    } catch (Exception e33) {
                    }
                }
                SystemSet.this.RegStyle = false;
                CheckSerial Serial = new CheckSerial();
                if (Serial.GetInputSerial()) {
                    SystemSet.this.RegStyle = true;
                }
                if (!SystemSet.this.RegStyle.booleanValue()) {
                    SystemSet.this.ShowBox("說明", "此功能於 [專業版] 才可使用!");
                    return;
                }
                try {
//                    String tSDCardPath12 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath12 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath12) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e34) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet23 = SystemSet.this;
                systemSet23.InputPassword = new StringBuilder(String.valueOf(systemSet23.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e35) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet24 = SystemSet.this;
                systemSet24.AccountPassword = new StringBuilder(String.valueOf(systemSet24.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
//                    Intent intent111 = new Intent();
//                    intent111.setClass(SystemSet.this, ExportData.class);
//                    SystemSet.this.startActivity(intent111);
//                    SystemSet.this.finish();
                }
            }
            if (item.get("ItemText").equals("密碼設定")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator12 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator12.vibrate(30L);
                    } catch (Exception e36) {
                    }
                }
                try {
//                    String tSDCardPath13 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath13 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath13) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e37) {
                }
                SystemSet.this.InputPassword = "";
                SystemSet systemSet25 = SystemSet.this;
                systemSet25.InputPassword = new StringBuilder(String.valueOf(systemSet25.InputPassword)).toString();
                try {
                    SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    if (SystemSet.this.cursor.moveToNext()) {
                        SystemSet.this.InputPassword = SystemSet.this.cursor.getString(0);
                    }
                    SystemSet.this.cursor.close();
                } catch (Exception e38) {
                }
                SystemSet.this.AccountPassword = "";
                SystemSet systemSet26 = SystemSet.this;
                systemSet26.AccountPassword = new StringBuilder(String.valueOf(systemSet26.AccountPassword)).toString();
                SystemSet.this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountPassword = SystemSet.this.cursor.getString(0).trim();
                }
                SystemSet.this.cursor.close();
                SystemSet.this.DataDB.close();
                if (!SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim()) && !SystemSet.this.AccountPassword.trim().equals("")) {
                    SystemSet.this.ShowInputPassword();
                } else {
//                    Intent intent112 = new Intent();
//                    intent112.setClass(SystemSet.this, EditAccountPassword.class);
//                    SystemSet.this.startActivity(intent112);
//                    SystemSet.this.finish();
                }
            }
            if (item.get("ItemText").equals("註冊升級")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator13 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator13.vibrate(30L);
                    } catch (Exception e39) {
                    }
                }
//                Intent intent113 = new Intent();
//                intent113.setClass(SystemSet.this, RegisterView.class);
//                SystemSet.this.startActivity(intent113);
//                SystemSet.this.finish();
            }
            if (item.get("ItemText").equals("資料備份")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator14 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator14.vibrate(30L);
                    } catch (Exception e40) {
                    }
                }
//                try {
//                    String tSDCardPath14 = Environment.getExternalStorageDirectory().getAbsolutePath();
//                    Intent DBIntent = new Intent("android.intent.action.SEND");
//                    DBIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(String.valueOf(tSDCardPath14) + "/MyMoneyZero/mymoney.db")));
//                    DBIntent.setType("text/*");
//                    SystemSet.this.startActivity(DBIntent);
//                } catch (Exception e41) {
//                }
                exportDB();
            }
            if (item.get("ItemText").equals("關於系統")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator15 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator15.vibrate(30L);
                    } catch (Exception e42) {
                    }
                }
//                Intent intent114 = new Intent();
//                intent114.setClass(SystemSet.this, AboutSystem.class);
//                SystemSet.this.startActivity(intent114);
//                SystemSet.this.finish();
            }
            if (item.get("ItemText").equals("回主畫面")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator16 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator16.vibrate(30L);
                    } catch (Exception e43) {
                    }
                }
                Intent intent115 = new Intent();
                intent115.setClass(SystemSet.this, MyMoneyZeroActivity.class);
                SystemSet.this.startActivity(intent115);
                SystemSet.this.finish();
            }
            if (item.get("ItemText").equals("逛逛官網")) {
                if (SystemSet.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator17 = (Vibrator) SystemSet.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator17.vibrate(30L);
                    } catch (Exception e44) {
                    }
                }
                Intent ie = new Intent("android.intent.action.VIEW", Uri.parse("http://www.mysoft.idv.tw/"));
                SystemSet.this.startActivity(ie);
            }
        }
    }

    public void ShowCollPay(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, CollPayName.class);
//        startActivity(intent1);
//        finish();
    }

    public void ShowOften(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, OftenNote.class);
//        startActivity(intent1);
//        finish();
    }

    public void ShowProjectSet(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, ProjectSet.class);
//        startActivity(intent1);
//        finish();
    }

    public void MountSubSet(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, MountSubSet.class);
//        startActivity(intent1);
//        finish();
    }

    public void ExchangeSubSet(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, ExchangeSubSet.class);
//        startActivity(intent1);
//        finish();
    }

    public void ShowAccountData(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, AccountData.class);
//        startActivity(intent1);
//        finish();
    }

    public void YearSpandSet(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, YearSpandSet.class);
//        startActivity(intent1);
//        finish();
    }

    public void DatabaseRestore(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, DatabaseRestore.class);
//        startActivity(intent1);
//        finish();
    }

    public void Sysset(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, Sysset.class);
//        startActivity(intent1);
//        finish();
    }

    public void InvoiceCheck(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, InvoiceCheck.class);
//        startActivity(intent1);
//        finish();
    }

    public void InputSerial(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, RegisterView.class);
//        startActivity(intent1);
//        finish();
    }

    public void AboutSystem(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, AboutSystem.class);
//        startActivity(intent1);
//        finish();
    }

    public void ShowBox(String TitleString, String BodyString) {
        if (TitleString == "") {
            TitleString = "訊息";
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle(TitleString);
        MyAlertDialog.setMessage(BodyString);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.SystemSet.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyAlertDialog.setPositiveButton("確定", OkClick);
        MyAlertDialog.show();
    }

    public void SentData() {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.RegStyle = false;
        CheckSerial Serial = new CheckSerial();
        if (Serial.GetInputSerial()) {
            this.RegStyle = true;
        }
        if (!this.RegStyle.booleanValue()) {
            ShowBox("說明", "此功能於 [專業版] 才可使用!");
            return;
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("同步匯出功能說明");
        MyAlertDialog.setMessage("同步匯出功能係指將手機端所記錄的帳務資料匯出至網路上(如Dropbox)後,再透過 PC 版 帳務小管家 中的 [從手機版匯出檔匯入] 功能來將手機端的帳務資料透過網路方式將手機版的帳務記錄資料匯入於 PC 版中，透過此方式您可不必透過 USB 連接線就可達到雲端與 PC 同步功能\n\n註：所有的記錄只會匯出一次不會重覆匯出");
        MyAlertDialog.setPositiveButton("資料匯出", new DialogInterface.OnClickListener() { // from class: mymoney.zero.SystemSet.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) throws SQLException {
                try {
//                    String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e2) {
                }
                SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND DATA_NOTE = '使用帳本'";
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                SystemSet.this.AccountId = 0.0d;
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountId = SystemSet.this.cursor.getDouble(0);
                }
                SystemSet.this.cursor.close();
                SystemSet.this.SQL = "SELECT ACCOUNT_NOTE FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId);
                SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (SystemSet.this.cursor.moveToNext()) {
                    SystemSet.this.AccountName = SystemSet.this.cursor.getString(0);
                }
                SystemSet.this.cursor.close();
                if (SystemSet.this.AccountId == 0.0d) {
                    SystemSet.this.ShowBox("訊息", "您尚未選擇欲作業的帳本");
                    return;
                }
                GetNowDate GetNow = new GetNowDate();
                String FileName = (String.valueOf(GetNow.GetDate().replaceAll("/", "")) + GetNow.GetTime()).replaceAll(":", "");
                File sdCardDir = Environment.getExternalStorageDirectory();
                File saveFile = new File(sdCardDir, "/MyMoneyZero/Export/" + FileName.trim() + ".xdb");
                try {
                    FileOutputStream outStream = new FileOutputStream(saveFile);
                    SystemSet.this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,OUT_MOUNT,IN_MOUNT,DATA_DATE,EXCHANGE,DATA_NOTE,PAY_COLL_NAME,INVOICE_NO,PROJECT_ID,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND EXPORT = '0' ORDER BY DATA_DATE,MAKE_NO,DATA_NO";
                    SystemSet.this.cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                    new StringBuilder(String.valueOf("")).toString();
                    String SourceItem = new StringBuilder(String.valueOf("")).toString();
                    String ObjectItem = new StringBuilder(String.valueOf("")).toString();
                    new StringBuilder(String.valueOf("")).toString();
                    new StringBuilder(String.valueOf("")).toString();
                    new StringBuilder(String.valueOf("")).toString();
                    new StringBuilder(String.valueOf("")).toString();
                    new StringBuilder(String.valueOf("")).toString();
                    new StringBuilder(String.valueOf("")).toString();
                    double OutMount = 0.0d + 0.0d;
                    double InMount = 0.0d + 0.0d;
                    double SourceExchange = 0.0d + 0.0d;
                    double ObjectExchange = 0.0d + 0.0d;
                    double d = 0.0d + 0.0d;
                    while (SystemSet.this.cursor.moveToNext()) {
                        double MakeNo = SystemSet.this.cursor.getDouble(1);
                        String DataDate = SystemSet.this.cursor.getString(6).trim();
                        String InvoiceNo = SystemSet.this.cursor.getString(10).trim();
                        String PayCollName = SystemSet.this.cursor.getString(9).trim();
                        String ProjectId = SystemSet.this.cursor.getString(11).trim();
                        String DataNote = SystemSet.this.cursor.getString(8).trim();
                        String DataNote2 = SystemSet.this.cursor.getString(12).trim();
                        if (SystemSet.this.cursor.getString(0).equals("1")) {
                            SourceItem = SystemSet.this.cursor.getString(2).trim();
                            OutMount = SystemSet.this.cursor.getDouble(4);
                            SourceExchange = SystemSet.this.cursor.getDouble(7);
                        }
                        if (SystemSet.this.cursor.getString(0).equals("2")) {
                            ObjectItem = SystemSet.this.cursor.getString(2).trim();
                            InMount = SystemSet.this.cursor.getDouble(5);
                            ObjectExchange = SystemSet.this.cursor.getDouble(7);
                        }
                        if (SystemSet.this.cursor.getString(0).equals("2")) {
                            String SaveData = String.valueOf(String.valueOf('\"')) + DataDate.trim() + String.valueOf('\"') + "," + String.valueOf('\"') + SourceItem.trim() + String.valueOf('\"') + "," + String.valueOf('\"') + String.valueOf(SourceExchange) + String.valueOf('\"') + "," + String.valueOf('\"') + String.valueOf(OutMount) + String.valueOf('\"') + "," + String.valueOf('\"') + ObjectItem.trim() + String.valueOf('\"') + "," + String.valueOf('\"') + String.valueOf(ObjectExchange) + String.valueOf('\"') + "," + String.valueOf('\"') + String.valueOf(InMount) + String.valueOf('\"') + "," + String.valueOf('\"') + InvoiceNo.trim().replace(",", "，") + String.valueOf('\"') + "," + String.valueOf('\"') + PayCollName.trim().replace(",", "，") + String.valueOf('\"') + "," + String.valueOf('\"') + ProjectId.trim().replace(",", "，") + String.valueOf('\"') + "," + String.valueOf('\"') + DataNote.trim().replace(",", "，") + String.valueOf('\"') + "," + String.valueOf('\"') + String.valueOf('\"') + "," + String.valueOf('\"') + String.valueOf(MakeNo) + String.valueOf('\"') + "," + String.valueOf('\"') + DataNote2.trim().replace(",", "，").trim().replace("\n", "%n") + String.valueOf('\"');
                            outStream.write((String.valueOf(SaveData) + String.valueOf('\r')).getBytes());
                            SystemSet.this.SQL = "UPDATE MYMONEY_DATA SET EXPORT = '1' WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND MAKE_NO = " + String.valueOf(MakeNo);
                            SystemSet.this.DataDB.execSQL(SystemSet.this.SQL);
                        }
                    }
                    SystemSet.this.cursor.close();
                    outStream.close();
//                    String tSDCardPath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath2 = String.valueOf(getExternalFilesDir(null));
                    Intent DBIntent = new Intent("android.intent.action.SEND");
                    DBIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(tSDCardPath2, "/MyMoneyZero/Export/" + FileName.trim() + ".xdb")));
                    DBIntent.setType("text/*");
                    SystemSet.this.startActivity(DBIntent);
                } catch (FileNotFoundException e3) {
                } catch (IOException e4) {
                }
            }
        });
        MyAlertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: mymoney.zero.SystemSet.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        MyAlertDialog.show();
    }

    public void ShowInputPassword() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("帳本密碼輸入");
        alert.setMessage("請輸入帳本密碼：");
        final EditText input = new EditText(this);
        input.setTransformationMethod(new PasswordTransformationMethod());
        alert.setView(input);
        input.setText("");
        input.setInputType(3);
        input.setHint("請輸入帳本密碼");
        input.setTransformationMethod(new PasswordTransformationMethod());
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.SystemSet.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) throws SQLException {
                SystemSet.this.InputPassword = input.getText().toString();
                if (SystemSet.this.AccountPassword.trim().equals(SystemSet.this.InputPassword.trim())) {
                    SystemSet.this.InputPassword = SystemSet.this.AccountPassword;
                    try {
//                        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        String tSDCardPath = String.valueOf(getExternalFilesDir(null));
                        SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                    } catch (Exception e) {
                    }
                    try {
                        SystemSet.this.SQL = "UPDATE SYSTEM_DATA SET DATA_VALUE = '" + SystemSet.this.InputPassword.trim() + "' WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '輸入帳密'";
                        SystemSet.this.DataDB.execSQL(SystemSet.this.SQL);
                    } catch (Exception e2) {
                    }
                    SystemSet.this.ErrorPassword = 0.0d;
                    SystemSet.this.DataDB.close();
                    return;
                }
                try {
//                    String tSDCardPath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath2 = String.valueOf(getExternalFilesDir(null));
                    SystemSet.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath2) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e3) {
                }
                SystemSet.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(SystemSet.this.AccountId) + " AND DATA_NOTE = '密碼提示'";
                Cursor cursor = SystemSet.this.DataDB.rawQuery(SystemSet.this.SQL, null);
                if (!cursor.moveToNext()) {
                    SystemSet.this.AccountPasswordMessage = "無設定密碼提示";
                } else {
                    SystemSet.this.AccountPasswordMessage = cursor.getString(0).trim();
                }
                cursor.close();
                SystemSet.this.DataDB.close();
                if (SystemSet.this.AccountPasswordMessage.trim().equals("")) {
                    SystemSet.this.AccountPasswordMessage = "無設定密碼提示";
                }
                SystemSet.this.ShowBox("錯誤!", "帳本密碼輸入錯誤!\n\n密碼提示：" + SystemSet.this.AccountPasswordMessage.trim());
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.SystemSet.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        alert.setPositiveButton("確定", OkClick);
        alert.setNegativeButton("取消", CanCelClick);
        alert.show();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 3:
                return true;
            case 4:
                Intent intent = new Intent();
                intent.setClass(this, MyMoneyZeroActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    // ----------------------------- 匯出 DB -----------------------------
    public void exportDB() {
        Context c = MyMoneyZeroActivity.getAppContext();
        try {
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File data = c.getExternalFilesDir(null);

            File src = new File(data + "/MyMoneyZero/mymoney.db");
            File dst = new File(sd, "mymoney.db");

            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();
            outChannel.transferFrom(inChannel, 0, inChannel.size());
            inChannel.close();
            outChannel.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ----------------------------- 匯入 DB -----------------------------
    private void importDB(Uri uri) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            OutputStream out = new FileOutputStream(appDBPath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();

            Toast.makeText(this, "匯入成功！請重新啟動 App", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "匯入失敗：" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // 使用 SAF 選擇要匯入的 DB
    public void chooseFileToImport() {
        appDBPath = new File(getExternalFilesDir(null) + "/MyMoneyZero/mymoney.db");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        filePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Uri uri = result.getData().getData();
                            importDB(uri);
                        }
                    });

}