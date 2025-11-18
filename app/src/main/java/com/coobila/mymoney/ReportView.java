package com.coobila.mymoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ReportView extends Activity {
    private SQLiteDatabase DataDB;
    private ListView DataList;
    private Boolean RegStyle;
    private String SQL;
    private Cursor cursor;
    private double AccountId = 0.0d;
    private String AccountName = "";
    private String ShowVibrate = "";
    private double REPORT_NO = 0.0d;

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
        setContentView(R.layout.reportview);
        this.RegStyle = false;
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        CheckSerial Serial = new CheckSerial();
        if (Serial.GetInputSerial()) {
            this.RegStyle = true;
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
        setTitle(String.valueOf(this.AccountName) + " - 統計報表");
        this.DataList = (ListView) findViewById(R.id.DataList);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map1.put("ReportName", "收支日報表");
        listItem.add(map1);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map2.put("ReportName", "支出項目統計表");
        listItem.add(map2);
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map3.put("ReportName", "收入項目統計表");
        listItem.add(map3);
        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map4.put("ReportName", "資產項目餘額表");
        listItem.add(map4);
        HashMap<String, Object> map41 = new HashMap<>();
        map41.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map41.put("ReportName", "負債項目餘額表");
        listItem.add(map41);
        HashMap<String, Object> map6 = new HashMap<>();
        map6.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map6.put("ReportName", "支出預算表");
        listItem.add(map6);
        HashMap<String, Object> map14 = new HashMap<>();
        map14.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map14.put("ReportName", "專案收支統計表");
        listItem.add(map14);
        HashMap<String, Object> map141 = new HashMap<>();
        map141.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map141.put("ReportName", "每日收支統計表");
        listItem.add(map141);
        HashMap<String, Object> map142 = new HashMap<>();
        map142.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map142.put("ReportName", "年度專案收支統計表");
        listItem.add(map142);
        HashMap<String, Object> map7 = new HashMap<>();
        map7.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map7.put("ReportName", "年度支出項目統計表");
        listItem.add(map7);
        HashMap<String, Object> map8 = new HashMap<>();
        map8.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map8.put("ReportName", "年度收入項目統計表");
        listItem.add(map8);
        HashMap<String, Object> map9 = new HashMap<>();
        map9.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map9.put("ReportName", "年度收支統計表");
        listItem.add(map9);
        HashMap<String, Object> map10 = new HashMap<>();
        map10.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map10.put("ReportName", "損益表");
        listItem.add(map10);
        HashMap<String, Object> map11 = new HashMap<>();
        map11.put("ItemImage", Integer.valueOf(R.drawable.chart4));
        map11.put("ReportName", "資產負債表");
        listItem.add(map11);
        this.DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: mymoney.zero.ReportView.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ReportView.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator = (Vibrator) ReportView.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(30L);
                    } catch (Exception e2) {
                    }
                }
                if (position == 0) {
//                    Intent intent = new Intent();
//                    intent.setClass(ReportView.this, Report01.class);
//                    ReportView.this.startActivity(intent);
//                    ReportView.this.finish();
                }
                if (position == 1) {
//                    Intent intent2 = new Intent();
//                    intent2.setClass(ReportView.this, Report02.class);
//                    ReportView.this.startActivity(intent2);
//                    ReportView.this.finish();
                }
                if (position == 2) {
                    Intent intent3 = new Intent();
                    intent3.setClass(ReportView.this, Report03.class);
                    ReportView.this.startActivity(intent3);
                    ReportView.this.finish();
                }
                if (position == 3) {
                    Intent intent4 = new Intent();
                    intent4.setClass(ReportView.this, Report04.class);
                    ReportView.this.startActivity(intent4);
                    ReportView.this.finish();
                }
//                if (position == 4) {
//                    Intent intent5 = new Intent();
//                    intent5.setClass(ReportView.this, Report05.class);
//                    ReportView.this.startActivity(intent5);
//                    ReportView.this.finish();
//                }
//                if (position == 5) {
//                    Intent intent6 = new Intent();
//                    intent6.setClass(ReportView.this, Report06.class);
//                    ReportView.this.startActivity(intent6);
//                    ReportView.this.finish();
//                }
//                if (position == 6) {
//                    Intent intent7 = new Intent();
//                    intent7.setClass(ReportView.this, Report09.class);
//                    ReportView.this.startActivity(intent7);
//                    ReportView.this.finish();
//                }
//                if (position == 7) {
//                    if (ReportView.this.RegStyle.booleanValue()) {
//                        Intent intent8 = new Intent();
//                        intent8.setClass(ReportView.this, Report10.class);
//                        ReportView.this.startActivity(intent8);
//                        ReportView.this.finish();
//                    } else {
//                        ReportView.this.ShowBox("說明", "此報表於 [專業版] 才可使用!");
//                    }
//                }
//                if (position == 8) {
//                    if (ReportView.this.RegStyle.booleanValue()) {
//                        Intent intent9 = new Intent();
//                        intent9.setClass(ReportView.this, Report07.class);
//                        ReportView.this.startActivity(intent9);
//                        ReportView.this.finish();
//                    } else {
//                        ReportView.this.ShowBox("說明", "此報表於 [專業版] 才可使用!");
//                    }
//                }
//                if (position == 9) {
//                    if (ReportView.this.RegStyle.booleanValue()) {
//                        Intent intent10 = new Intent();
//                        intent10.setClass(ReportView.this, Report11.class);
//                        ReportView.this.startActivity(intent10);
//                        ReportView.this.finish();
//                    } else {
//                        ReportView.this.ShowBox("說明", "此報表於 [專業版] 才可使用!");
//                    }
//                }
//                if (position == 10) {
//                    if (ReportView.this.RegStyle.booleanValue()) {
//                        Intent intent11 = new Intent();
//                        intent11.setClass(ReportView.this, Report12.class);
//                        ReportView.this.startActivity(intent11);
//                        ReportView.this.finish();
//                    } else {
//                        ReportView.this.ShowBox("說明", "此報表於 [專業版] 才可使用!");
//                    }
//                }
//                if (position == 11) {
//                    if (ReportView.this.RegStyle.booleanValue()) {
//                        Intent intent12 = new Intent();
//                        intent12.setClass(ReportView.this, Report13.class);
//                        ReportView.this.startActivity(intent12);
//                        ReportView.this.finish();
//                    } else {
//                        ReportView.this.ShowBox("說明", "此報表於 [專業版] 才可使用!");
//                    }
//                }
//                if (position == 12) {
//                    if (ReportView.this.RegStyle.booleanValue()) {
//                        Intent intent13 = new Intent();
//                        intent13.setClass(ReportView.this, Report14.class);
//                        ReportView.this.startActivity(intent13);
//                        ReportView.this.finish();
//                    } else {
//                        ReportView.this.ShowBox("說明", "此報表於 [專業版] 才可使用!");
//                    }
//                }
//                if (position == 13) {
//                    if (ReportView.this.RegStyle.booleanValue()) {
//                        Intent intent14 = new Intent();
//                        intent14.setClass(ReportView.this, Report15.class);
//                        ReportView.this.startActivity(intent14);
//                        ReportView.this.finish();
//                        return;
//                    }
//                    ReportView.this.ShowBox("說明", "此報表於 [專業版] 才可使用!");
//                }
            }
        });
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.reportlistitem, new String[]{"ReportName", "ItemImage"}, new int[]{R.id.ReportName, R.id.ItemImage});
        this.DataList.setAdapter((ListAdapter) listItemAdapter);
        Bundle bundle = getIntent().getExtras();
        double ProgNo = 0.0d;
        try {
            ProgNo = bundle.getDouble("ProgNo");
        } catch (Exception e2) {
        }
        this.DataList.setSelection((int) ProgNo);
    }

    public void ShowBox(String TitleString, String BodyString) {
        if (TitleString == "") {
            TitleString = "訊息";
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle(TitleString);
        MyAlertDialog.setMessage(BodyString);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.ReportView.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyAlertDialog.setPositiveButton("確定", OkClick);
        MyAlertDialog.show();
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
}