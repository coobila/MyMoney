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
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ItemSetAdd extends Activity {
    private double AccountId;
    private TextView BEFORE_MOUNT;
    private CheckBox CHARGE;
    private SQLiteDatabase DataDB;
    private TextView EXCHANGE;
    private String FocusFeild;
    private CheckBox HideStyle;
    private String ITEM_CLASS;
    private String ITEM_DATA_TEMP;
    private TextView ITEM_NOTE;
    private Double MAKE_NO;
    private Spinner PARENT_NOTE;
    private String SQL;
    private TextView VBEFORE_MOUNT;
    private TextView VEXCHANGE;
    private Cursor cursor;
    private int MountSub = 0;
    private int ExchangeSub = 0;
    private int Loop_I = 0;
    private String MountFormat = "";
    private String ExchangeFormat = "";
    private String AccountName = "";
    private String ShowVibrate = "";
    View.OnFocusChangeListener MountfocusListener = new View.OnFocusChangeListener() { // from class: mymoney.zero.ItemSetAdd.1
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            String MOUNT_TEMP = ItemSetAdd.this.BEFORE_MOUNT.getText().toString();
            ItemSetAdd.this.BEFORE_MOUNT.setText(MOUNT_TEMP.replace(",", ""));
            ItemSetAdd.this.BEFORE_MOUNT.setInputType(0);
            if (ItemSetAdd.this.BEFORE_MOUNT.getText().toString().trim().length() == 0) {
                ItemSetAdd.this.BEFORE_MOUNT.setText("0");
            }
            if (hasFocus) {
                ItemSetAdd.this.FocusFeild = "MOUNT";
                Intent intent = new Intent();
                intent.setClass(ItemSetAdd.this, Calc.class);
                Bundle bundle = new Bundle();
                bundle.putString("InMount", ItemSetAdd.this.BEFORE_MOUNT.getText().toString());
                intent.putExtras(bundle);
                ItemSetAdd.this.startActivityForResult(intent, 0);
            }
            if (hasFocus) {
                try {
                    if (Double.valueOf(ItemSetAdd.this.BEFORE_MOUNT.getText().toString()).doubleValue() == 0.0d) {
                        ItemSetAdd.this.BEFORE_MOUNT.setText("");
                    }
                } catch (Exception e) {
                }
            }
            try {
                DecimalFormat df2 = new DecimalFormat("#,##0" + ItemSetAdd.this.MountFormat);
                ItemSetAdd.this.BEFORE_MOUNT.setText(df2.format(Double.valueOf(ItemSetAdd.this.BEFORE_MOUNT.getText().toString())));
            } catch (Exception e2) {
            }
        }
    };
    View.OnFocusChangeListener ExchangefocusListener = new View.OnFocusChangeListener() { // from class: mymoney.zero.ItemSetAdd.2
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            String MOUNT_TEMP = ItemSetAdd.this.EXCHANGE.getText().toString();
            ItemSetAdd.this.EXCHANGE.setText(MOUNT_TEMP.replace(",", ""));
            ItemSetAdd.this.EXCHANGE.setInputType(0);
            if (ItemSetAdd.this.EXCHANGE.getText().toString().trim().length() == 0) {
                ItemSetAdd.this.EXCHANGE.setText("1" + ItemSetAdd.this.ExchangeFormat);
            }
            if (hasFocus) {
                ItemSetAdd.this.FocusFeild = "EXCHANGE";
                Intent intent = new Intent();
                intent.setClass(ItemSetAdd.this, Calc.class);
                Bundle bundle = new Bundle();
                bundle.putString("InMount", ItemSetAdd.this.EXCHANGE.getText().toString());
                intent.putExtras(bundle);
                ItemSetAdd.this.startActivityForResult(intent, 0);
            }
            if (hasFocus) {
                try {
                    if (Double.valueOf(ItemSetAdd.this.EXCHANGE.getText().toString()).doubleValue() == 0.0d) {
                        ItemSetAdd.this.EXCHANGE.setText("");
                    }
                } catch (Exception e) {
                }
            }
            try {
                DecimalFormat df2 = new DecimalFormat("#,##0.000");
                ItemSetAdd.this.EXCHANGE.setText(df2.format(Double.valueOf(ItemSetAdd.this.EXCHANGE.getText().toString())));
            } catch (Exception e2) {
            }
        }
    };

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.FocusFeild.equals("MOUNT")) {
            String Mount = null;
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Mount = bundle.getString("Mount");
            }
            this.BEFORE_MOUNT.setText(Mount);
            if (Mount.length() == 0) {
                this.BEFORE_MOUNT.setText("0");
            }
            try {
                DecimalFormat df2 = new DecimalFormat("#,##0" + this.MountFormat);
                this.BEFORE_MOUNT.setText(df2.format(Double.valueOf(this.BEFORE_MOUNT.getText().toString())));
            } catch (Exception e) {
            }
        }
        if (this.FocusFeild.equals("EXCHANGE")) {
            String Mount2 = null;
            Bundle bundle2 = data.getExtras();
            if (bundle2 != null) {
                Mount2 = bundle2.getString("Mount");
            }
            this.EXCHANGE.setText(Mount2);
            if (this.EXCHANGE.length() == 0) {
                this.EXCHANGE.setText("0");
            }
            try {
                DecimalFormat df22 = new DecimalFormat("#,##0" + this.ExchangeFormat);
                this.EXCHANGE.setText(df22.format(Double.valueOf(this.EXCHANGE.getText().toString())));
            } catch (Exception e2) {
            }
        }
        this.ITEM_NOTE.setFocusable(true);
        this.ITEM_NOTE.requestFocus();
        this.ITEM_NOTE.setFocusableInTouchMode(true);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemsetadd);
        InitData();
    }

    public void InitData() {
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
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '金額小數點'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        this.MountSub = 0;
        if (this.cursor.moveToNext()) {
            this.MountSub = (int) this.cursor.getDouble(0);
        }
        this.cursor.close();
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '匯率小數點'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        this.ExchangeSub = 3;
        if (this.cursor.moveToNext()) {
            this.ExchangeSub = (int) this.cursor.getDouble(0);
        }
        this.cursor.close();
        if (this.MountSub > 0) {
            this.MountFormat = ".";
            this.Loop_I = 1;
            while (this.Loop_I <= this.MountSub) {
                this.MountFormat = String.valueOf(this.MountFormat) + "0";
                this.Loop_I++;
            }
        } else {
            this.MountFormat = "";
        }
        if (this.ExchangeSub > 0) {
            this.ExchangeFormat = ".";
            this.Loop_I = 1;
            while (this.Loop_I <= this.ExchangeSub) {
                this.ExchangeFormat = String.valueOf(this.ExchangeFormat) + "0";
                this.Loop_I++;
            }
        } else {
            this.ExchangeFormat = "";
        }
        setTitle(String.valueOf(this.AccountName) + " - 項目資料新增");
        Bundle bundle = getIntent().getExtras();
        this.ITEM_CLASS = bundle.getString("ITEM_CLASS");
        this.PARENT_NOTE = (Spinner) findViewById(R.id.PARENT_NOTE);
        this.ITEM_NOTE = (TextView) findViewById(R.id.ITEM_NOTE);
        this.BEFORE_MOUNT = (TextView) findViewById(R.id.BEFORE_MOUNT);
        this.EXCHANGE = (TextView) findViewById(R.id.EXCHANGE);
        this.VBEFORE_MOUNT = (TextView) findViewById(R.id.VBEFORE_MOUNT);
        this.VEXCHANGE = (TextView) findViewById(R.id.VEXCHANGE);
        this.CHARGE = (CheckBox) findViewById(R.id.CHARGE);
        this.HideStyle = (CheckBox) findViewById(R.id.HideStyle);
        this.ITEM_NOTE.setFocusable(true);
        this.ITEM_NOTE.requestFocus();
        this.ITEM_NOTE.setFocusableInTouchMode(true);
        this.BEFORE_MOUNT.setInputType(0);
        this.EXCHANGE.setInputType(0);
        this.BEFORE_MOUNT.setInputType(2);
        this.EXCHANGE.setInputType(2);
        ArrayList<String> ParentList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ParentList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.PARENT_NOTE.setAdapter((SpinnerAdapter) adapter);
        ParentList.add("無");
        String SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = PARENT_NOTE AND ITEM_CLASS = '" + this.ITEM_CLASS.replace("'", "").trim() + "' ORDER BY MAKE_NO";
        this.cursor = this.DataDB.rawQuery(SQL, null);
        while (this.cursor.moveToNext()) {
            this.ITEM_DATA_TEMP = this.cursor.getString(0);
            ParentList.add(this.ITEM_DATA_TEMP);
        }
        this.cursor.close();
        this.DataDB.close();
        this.PARENT_NOTE.setAdapter((SpinnerAdapter) adapter);
        if (this.ITEM_CLASS.equals("資產") || this.ITEM_CLASS.equals("負債")) {
            this.BEFORE_MOUNT.setVisibility(View.VISIBLE);
            this.VBEFORE_MOUNT.setVisibility(View.VISIBLE);
            this.EXCHANGE.setVisibility(View.VISIBLE);
            this.VEXCHANGE.setVisibility(View.VISIBLE);
            this.CHARGE.setVisibility(View.GONE);
        } else {
            this.BEFORE_MOUNT.setVisibility(View.INVISIBLE);
            this.VBEFORE_MOUNT.setVisibility(View.INVISIBLE);
            this.EXCHANGE.setVisibility(View.GONE);
            this.VEXCHANGE.setVisibility(View.INVISIBLE);
            this.CHARGE.setVisibility(View.VISIBLE);
        }
        if (this.ITEM_CLASS.equals("收入")) {
            this.CHARGE.setVisibility(View.GONE);
        }
        if (this.ITEM_CLASS.equals("支出")) {
            this.CHARGE.setVisibility(View.VISIBLE);
        }
        if (this.ITEM_CLASS.equals("業外收入") || this.ITEM_CLASS.equals("業外支出")) {
            this.CHARGE.setVisibility(View.GONE);
        }
        this.BEFORE_MOUNT.setOnFocusChangeListener(this.MountfocusListener);
        this.EXCHANGE.setOnFocusChangeListener(this.ExchangefocusListener);
    }

    public void SaveNext(View CvClick) throws SQLException {
        String INPUT_HIDESTYLE;
        String INPUT_CHARGE;
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        String INPUT_PARENT_NOTE = this.PARENT_NOTE.getSelectedItem().toString().trim();
        String INPUT_ITEM_NOTE = this.ITEM_NOTE.getText().toString().trim();
        String INPUT_BEFORE_MOUNT = this.BEFORE_MOUNT.getText().toString().trim();
        String INPUT_EXCHANGE = this.EXCHANGE.getText().toString().trim();
        new StringBuilder(String.valueOf("")).toString();
        if (this.HideStyle.isChecked()) {
            INPUT_HIDESTYLE = "1";
        } else {
            INPUT_HIDESTYLE = "";
        }
        if (this.CHARGE.isChecked()) {
            INPUT_CHARGE = "1";
        } else {
            INPUT_CHARGE = "0";
        }
        if (INPUT_BEFORE_MOUNT.equals("")) {
            INPUT_BEFORE_MOUNT = "0";
        }
        if (INPUT_PARENT_NOTE.equals("無")) {
            INPUT_PARENT_NOTE = INPUT_ITEM_NOTE;
        }
        if (INPUT_ITEM_NOTE.trim().equals("")) {
            ShowBox("訊息", "您尚未輸入項目名稱!");
            return;
        }
        if (Double.valueOf(INPUT_EXCHANGE).doubleValue() == 0.0d) {
            this.EXCHANGE.setText("1.000");
            INPUT_EXCHANGE = "1" + this.ExchangeFormat;
        }
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e2) {
        }
        this.SQL = "SELECT * FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + INPUT_ITEM_NOTE.replace("'", "") + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.cursor.close();
            ShowBox("提示", "項目名稱 " + INPUT_ITEM_NOTE.trim() + " 已有使用記錄,您不可以建立相同的項目名稱");
            return;
        }
        this.cursor.close();
        this.SQL = "SELECT MAKE_NO FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + this.ITEM_CLASS.trim() + "' ORDER BY MAKE_NO DESC";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.MAKE_NO = Double.valueOf(this.cursor.getDouble(0) + 1.0d);
        } else {
            this.MAKE_NO = Double.valueOf(1.0d);
        }
        this.cursor.close();
        this.SQL = "INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin'," + String.valueOf(this.AccountId) + "," + String.valueOf(this.MAKE_NO) + ",'" + INPUT_ITEM_NOTE.replace("'", "") + "','" + this.ITEM_CLASS + "'," + INPUT_BEFORE_MOUNT.replace(",", "") + ",'','" + INPUT_PARENT_NOTE.trim() + "'," + INPUT_EXCHANGE.replace(",", "") + ",'" + INPUT_CHARGE + "','" + INPUT_HIDESTYLE + "')";
        this.DataDB.execSQL(this.SQL);
        this.PARENT_NOTE.setSelection(0);
        this.ITEM_NOTE.setText("");
        this.BEFORE_MOUNT.setText("0");
        this.EXCHANGE.setText("1" + this.ExchangeFormat);
        this.CHARGE.setChecked(false);
        this.DataDB.close();
        InitData();
        this.ITEM_NOTE.setFocusable(true);
        this.ITEM_NOTE.requestFocus();
        this.ITEM_NOTE.setFocusableInTouchMode(true);
    }

    public void SaveExit(View CvClick) throws SQLException {
        String INPUT_HIDESTYLE;
        String INPUT_CHARGE;
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        Intent intent = new Intent();
        String INPUT_PARENT_NOTE = this.PARENT_NOTE.getSelectedItem().toString().trim();
        String INPUT_ITEM_NOTE = this.ITEM_NOTE.getText().toString().trim();
        String INPUT_BEFORE_MOUNT = this.BEFORE_MOUNT.getText().toString().trim();
        String INPUT_EXCHANGE = this.EXCHANGE.getText().toString().trim();
        new StringBuilder(String.valueOf("")).toString();
        if (this.HideStyle.isChecked()) {
            INPUT_HIDESTYLE = "1";
        } else {
            INPUT_HIDESTYLE = "";
        }
        if (this.CHARGE.isChecked()) {
            INPUT_CHARGE = "1";
        } else {
            INPUT_CHARGE = "0";
        }
        if (INPUT_PARENT_NOTE.equals("無")) {
            INPUT_PARENT_NOTE = INPUT_ITEM_NOTE;
        }
        if (INPUT_ITEM_NOTE.trim().equals("")) {
            ShowBox("訊息", "您尚未輸入項目名稱!");
            return;
        }
        if (Double.valueOf(INPUT_EXCHANGE).doubleValue() == 0.0d) {
            this.EXCHANGE.setText("1.000");
            INPUT_EXCHANGE = "1" + this.ExchangeFormat;
        }
        if (INPUT_BEFORE_MOUNT.equals("")) {
            INPUT_BEFORE_MOUNT = "0";
        }
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e2) {
        }
        this.SQL = "SELECT * FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + INPUT_ITEM_NOTE.replace("'", "") + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.cursor.close();
            ShowBox("提示", "項目名稱 " + INPUT_ITEM_NOTE.trim() + " 已有使用記錄,您不可以建立相同的項目名稱");
            return;
        }
        this.cursor.close();
        this.SQL = "SELECT MAKE_NO FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + this.ITEM_CLASS.trim() + "' ORDER BY MAKE_NO DESC";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.MAKE_NO = Double.valueOf(this.cursor.getDouble(0) + 1.0d);
        } else {
            this.MAKE_NO = Double.valueOf(1.0d);
        }
        this.cursor.close();
        String INPUT_BEFORE_MOUNT2 = INPUT_BEFORE_MOUNT.replace(",", "");
        String INPUT_EXCHANGE2 = INPUT_EXCHANGE.replace(",", "");
        String INPUT_ITEM_NOTE2 = INPUT_ITEM_NOTE.replace("'", "");
        this.SQL = "INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin'," + String.valueOf(this.AccountId) + "," + String.valueOf(this.MAKE_NO) + ",'" + INPUT_ITEM_NOTE2 + "','" + this.ITEM_CLASS + "'," + INPUT_BEFORE_MOUNT2 + ",'','" + INPUT_PARENT_NOTE.trim() + "'," + INPUT_EXCHANGE2 + ",'" + INPUT_CHARGE + "','" + INPUT_HIDESTYLE + "')";
        this.DataDB.execSQL(this.SQL);
        this.DataDB.close();
        Bundle bundle = getIntent().getExtras();
        bundle.putString("ITEM_CLASS", this.ITEM_CLASS);
        bundle.putString("ItemNoteRecPtr", INPUT_ITEM_NOTE2.replace(",", ""));
        intent.putExtras(bundle);
        intent.setClass(this, ItemSet.class);
        startActivity(intent);
        finish();
    }

    public void CloseExit(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        Intent intent = new Intent();
        Bundle bundle = getIntent().getExtras();
        bundle.putString("ITEM_CLASS", this.ITEM_CLASS);
        intent.putExtras(bundle);
        intent.setClass(this, ItemSet.class);
        startActivity(intent);
        finish();
    }

    public void ShowBox(String TitleString, String BodyString) {
        if (TitleString == "") {
            TitleString = "訊息";
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle(TitleString);
        MyAlertDialog.setMessage(BodyString);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.ItemSetAdd.3
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
                Bundle bundle = getIntent().getExtras();
                bundle.putString("ITEM_CLASS", this.ITEM_CLASS);
                intent.putExtras(bundle);
                intent.setClass(this, ItemSet.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}