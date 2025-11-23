package com.coobila.mymoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.text.method.NumberKeyListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class Addtransfer extends Activity {
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private Spinner CHARGE_ITEM;
    private TextView CHARGE_MOUNT;
    private TextView DATA_DATE;
    private TextView DATA_NOTE;
    private SQLiteDatabase DataDB;
    private Button EXCHANGE_VIEW;
    private TextView INOUT_MOUNT;
    private Spinner OBJECT_ITEM;
    private String OBJECT_PARENT_NOTE;
    private boolean OBJECT_SUB_STYLE;
    private TextView PAY_COLL_NAME;
    private Spinner SOURCE_ITEM;
    private String SOURCE_PARENT_NOTE;
    private boolean SOURCE_SUB_STYLE;
    private Button SaveExit;
    private TableRow ShowPayCollFrame;
    private TextView VCHARGE_MOUNT;
    private Spinner VOBJECT_ITEM;
    private Button VOICE_INPUT;
    private Spinner VSOURCE_ITEM;
    private TextView WeekView;
    private Cursor cursor;
    private Cursor cursor2;
    private Spinner mySpinner;
    private double AccountId = 0.0d;
    private int MountSub = 0;
    private int ExchangeSub = 0;
    private int Loop_I = 0;
    private double EexchangeValue = 1.0d;
    private String MountFormat = "";
    private String ExchangeFormat = "";
    private String AccountName = "";
    private String SQL;
    private String MountFocus = "";
    private String Item_Note = "";
    private String ShowMount = "";
    private String ShowPayColl = "0";
    private String ShowAssMount = "0";
    private String ShowVibrate = "0";
    private String InputDetail = "";
    private String ReturnHome = "0";
    private String Rounding = "0";
    View.OnFocusChangeListener datefocusListener = new View.OnFocusChangeListener() { // from class: mymoney.zero.Addtransfer.1
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Addtransfer.this.ShowDateSelect();
            }
        }
    };
    View.OnTouchListener dateTouchListener = new View.OnTouchListener() { // from class: mymoney.zero.Addtransfer.2
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 0) {
                Addtransfer.this.ShowDateSelect();
                return false;
            }
            return false;
        }
    };
    View.OnFocusChangeListener DataNotefocusListener = new View.OnFocusChangeListener() { // from class: mymoney.zero.Addtransfer.3
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            Addtransfer.this.VOICE_INPUT.setVisibility(View.INVISIBLE);
            if (hasFocus) {
                Addtransfer.this.VOICE_INPUT.setVisibility(View.VISIBLE);
            }
        }
    };
    View.OnFocusChangeListener MountfocusListener = new View.OnFocusChangeListener() { // from class: mymoney.zero.Addtransfer.4
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            String INOUT_MOUNT_TEMP = Addtransfer.this.INOUT_MOUNT.getText().toString().replace(",", "");
            Addtransfer.this.INOUT_MOUNT.setText(INOUT_MOUNT_TEMP);
            if (Addtransfer.this.INOUT_MOUNT.getText().toString().trim().length() == 0) {
                Addtransfer.this.INOUT_MOUNT.setText("0");
            }
            try {
                DecimalFormat df2 = new DecimalFormat("#,##0" + Addtransfer.this.MountFormat);
                Addtransfer.this.INOUT_MOUNT.setText(df2.format(Double.valueOf(Addtransfer.this.INOUT_MOUNT.getText().toString())));
            } catch (Exception e) {
            }
            if (hasFocus) {
                Addtransfer.this.MountFocus = "MOUNT";
                Intent intent = new Intent();
                intent.setClass(Addtransfer.this, Calc.class);
                Bundle bundle = new Bundle();
                bundle.putString("InMount", INOUT_MOUNT_TEMP);
                intent.putExtras(bundle);
                Addtransfer.this.startActivityForResult(intent, 0);
            }
        }
    };
    View.OnFocusChangeListener ChargefocusListener = new View.OnFocusChangeListener() { // from class: mymoney.zero.Addtransfer.5
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            String CHARGE_MOUNT_TEMP = Addtransfer.this.CHARGE_MOUNT.getText().toString().replace(",", "");
            Addtransfer.this.CHARGE_MOUNT.setText(CHARGE_MOUNT_TEMP);
            if (Addtransfer.this.CHARGE_MOUNT.getText().toString().trim().length() == 0) {
                Addtransfer.this.CHARGE_MOUNT.setText("0");
            }
            try {
                DecimalFormat df2 = new DecimalFormat("#,##0" + Addtransfer.this.MountFormat);
                Addtransfer.this.CHARGE_MOUNT.setText(df2.format(Double.valueOf(Addtransfer.this.CHARGE_MOUNT.getText().toString())));
            } catch (Exception e) {
            }
            if (hasFocus) {
                Addtransfer.this.MountFocus = "CHARGE";
                Intent intent = new Intent();
                intent.setClass(Addtransfer.this, Calc.class);
                Bundle bundle = new Bundle();
                bundle.putString("InMount", CHARGE_MOUNT_TEMP);
                intent.putExtras(bundle);
                Addtransfer.this.startActivityForResult(intent, 0);
            }
        }
    };
    private DatePickerDialog.OnDateSetListener myOnDateSetListener = new DatePickerDialog.OnDateSetListener() { // from class: mymoney.zero.Addtransfer.6
        @Override // android.app.DatePickerDialog.OnDateSetListener
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String NY = String.valueOf(year);
            String NM = String.valueOf(monthOfYear + 1);
            String ND = String.valueOf(dayOfMonth);
            if (NM.length() == 1) {
                NM = "0" + NM;
            }
            if (ND.length() == 1) {
                ND = "0" + ND;
            }
            Addtransfer.this.DATA_DATE.setText(String.valueOf(NY) + " 年 " + NM + " 月 " + ND + " 日");
            Addtransfer.this.WeekView = (TextView) Addtransfer.this.findViewById(R.id.WeekView);
            GetNowDate c = new GetNowDate();
            String SaveDate = c.CDateToEDate(Addtransfer.this.DATA_DATE.getText().toString());
            String[] WeekData = {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
            double y = Double.valueOf(SaveDate.substring(0, 4)).doubleValue();
            double m = Double.valueOf(SaveDate.substring(5, 7)).doubleValue() - 1.0d;
            double d = Double.valueOf(SaveDate.substring(8, 10)).doubleValue();
            Calendar cc = Calendar.getInstance();
            cc.set((int) y, (int) m, (int) d);
            String Week = WeekData[cc.get(7)];
            Addtransfer.this.WeekView.setText(Week);
            Addtransfer.this.DATA_NOTE.setFocusable(true);
            Addtransfer.this.DATA_NOTE.requestFocus();
            Addtransfer.this.DATA_NOTE.setFocusableInTouchMode(true);
        }
    };

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String Mount = null;
        if (this.MountFocus.equals("MOUNT")) {
            try {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Mount = bundle.getString("Mount");
                }
                if (!Mount.equals("")) {
                    this.INOUT_MOUNT.setText(Mount);
                }
            } catch (Exception e) {
            }
            try {
                this.INOUT_MOUNT.setText(new DecimalFormat("#,##0" + this.MountFormat).format(Double.valueOf(this.INOUT_MOUNT.getText().toString())));
            } catch (Exception e2) {
            }
        }
        if (this.MountFocus.equals("CHARGE")) {
            this.CHARGE_MOUNT.setText(Mount);
            try {
                Bundle bundle2 = data.getExtras();
                if (bundle2 != null) {
                    Mount = bundle2.getString("Mount");
                }
                if (!Mount.equals("")) {
                    this.CHARGE_MOUNT.setText(Mount);
                }
            } catch (Exception e3) {
            }
            try {
                this.CHARGE_MOUNT.setText(new DecimalFormat("#,##0" + this.MountFormat).format(Double.valueOf(this.CHARGE_MOUNT.getText().toString())));
            } catch (Exception e4) {
            }
        }
        if (this.MountFocus.equals("EXCHANGE")) {
            this.EXCHANGE_VIEW.setText(Mount);
            try {
                Bundle bundle3 = data.getExtras();
                if (bundle3 != null) {
                    Mount = bundle3.getString("Mount");
                }
                if (!Mount.equals("")) {
                    this.EXCHANGE_VIEW.setText(Mount);
                }
            } catch (Exception e5) {
            }
            try {
                DecimalFormat df2 = new DecimalFormat("#,##0" + this.ExchangeFormat);
                this.EXCHANGE_VIEW.setText("匯：" + df2.format(Double.valueOf(Mount)));
                this.EexchangeValue = Double.valueOf(df2.format(Double.valueOf(Mount))).doubleValue();
            } catch (Exception e6) {
            }
        }
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == -1) {
            ArrayList<String> matches = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            if (matches.size() > 0) {
                AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
                MyAlertDialog.setTitle("摘要選擇");
                final String[] OftenList = new String[matches.size()];
                for (int i = 0; i <= matches.size() - 1; i++) {
                    OftenList[i] = matches.get(i);
                }
                DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.7
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        Addtransfer.this.DATA_NOTE.setText(String.valueOf(Addtransfer.this.DATA_NOTE.getText().toString().trim()) + OftenList[which]);
                    }
                };
                DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.8
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                    }
                };
                MyAlertDialog.setItems(OftenList, OkClick);
                MyAlertDialog.setNegativeButton("取消", CanCelClick);
                MyAlertDialog.show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        this.DATA_NOTE.setFocusable(true);
        this.DATA_NOTE.requestFocus();
        this.DATA_NOTE.setFocusableInTouchMode(true);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtransfer);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        double ItemMount = 0.0d;
//        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String tSDCardPath = String.valueOf(getExternalFilesDir(null));
        try {
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
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '回到主畫面'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            if (this.cursor.getString(0).trim().equals("1")) {
                this.ReturnHome = "1";
            } else {
                this.ReturnHome = "0";
            }
        }
        this.cursor.close();
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '外幣轉台幣四捨五入'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            if (this.cursor.getString(0).trim().equals("1")) {
                this.Rounding = "1";
            } else {
                this.Rounding = "0";
            }
        }
        this.cursor.close();
        if (this.AccountId == 0.0d) {
            ShowBox("訊息", "您尚未選擇欲作業的帳本");
            return;
        }
        setTitle(String.valueOf(this.AccountName.trim()) + " - 新增轉帳記錄");
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
        this.ShowPayCollFrame = (TableRow) findViewById(R.id.ShowPayCollFrame);
        this.ShowPayColl = new StringBuilder(String.valueOf(this.ShowPayColl)).toString();
        this.ShowAssMount = new StringBuilder(String.valueOf(this.ShowAssMount)).toString();
        this.ShowVibrate = new StringBuilder(String.valueOf(this.ShowVibrate)).toString();
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示收付人欄位'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            if (this.cursor.getString(0).trim().equals("1")) {
                this.ShowPayColl = "1";
                this.ShowPayCollFrame.setVisibility(View.VISIBLE);
            } else {
                this.ShowPayColl = "0";
                this.ShowPayCollFrame.setVisibility(android.view.View.GONE);
            }
        }
        this.cursor.close();
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示資產與負債項目餘額'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            if (this.cursor.getString(0).trim().equals("1")) {
                this.ShowAssMount = "1";
            } else {
                this.ShowAssMount = "0";
            }
        }
        this.cursor.close();
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
        DecimalFormat df2 = new DecimalFormat("#,##0" + this.MountFormat);
        this.DATA_DATE = (TextView) findViewById(R.id.DATA_DATE);
        this.INOUT_MOUNT = (TextView) findViewById(R.id.INOUT_MOUNT);
        this.CHARGE_MOUNT = (TextView) findViewById(R.id.CHARGE_MOUNT);
        this.VCHARGE_MOUNT = (TextView) findViewById(R.id.VCHARGE_MOUNT);
        this.PAY_COLL_NAME = (TextView) findViewById(R.id.PAY_COLL_NAME);
        this.DATA_NOTE = (TextView) findViewById(R.id.DATA_NOTE);
        this.CHARGE_ITEM = (Spinner) findViewById(R.id.CHARGE_ITEM);
        this.VOICE_INPUT = (Button) findViewById(R.id.VOICE_INPUT);
        this.EXCHANGE_VIEW = (Button) findViewById(R.id.DetailInput);
        this.SOURCE_ITEM = (Spinner) findViewById(R.id.SOURCE_ITEM);
        this.OBJECT_ITEM = (Spinner) findViewById(R.id.OBJECT_ITEM);
        this.VSOURCE_ITEM = (Spinner) findViewById(R.id.VSOURCE_ITEM);
        this.VOBJECT_ITEM = (Spinner) findViewById(R.id.VOBJECT_ITEM);
        DecimalFormat df3 = new DecimalFormat("#,##0" + this.ExchangeFormat);
        this.EXCHANGE_VIEW.setText("匯：" + df3.format(Double.valueOf(1.0d)));
        this.DATA_DATE.setInputType(0);
        this.INOUT_MOUNT.setInputType(0);
        this.CHARGE_MOUNT.setInputType(0);
        GetNowDate GetNow = new GetNowDate();
        this.DATA_DATE.setText(GetNow.GetCDate());
        ArrayList<String> allCountries = new ArrayList<>();
        ArrayList<String> allCountriesv = new ArrayList<>();
        this.SQL = "SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE = PARENT_NOTE AND HIDESTYLE <> '1' ORDER BY ITEM_CLASS DESC,MAKE_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        while (this.cursor.moveToNext()) {
            this.Item_Note = this.cursor.getString(0);
            this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + this.Item_Note.trim() + "'";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            if (this.cursor2.moveToNext()) {
            }
            this.cursor2.close();
            if (this.ShowAssMount.equals("1")) {
                ItemMount = GetItemMount(this.cursor.getString(1), this.Item_Note);
            }
            allCountries.add(this.Item_Note);
            this.ShowMount = df2.format(ItemMount);
            if (this.ShowAssMount.equals("1")) {
                allCountriesv.add(String.valueOf(this.Item_Note) + "  $" + this.ShowMount);
            } else {
                allCountriesv.add(this.Item_Note);
            }
            this.SQL = "SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + this.cursor.getString(0) + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            while (this.cursor2.moveToNext()) {
                this.Item_Note = this.cursor2.getString(0);
                if (this.ShowAssMount.equals("1")) {
                    ItemMount = GetItemMount(this.cursor.getString(1), this.Item_Note);
                }
                allCountries.add(this.Item_Note);
                this.ShowMount = df2.format(ItemMount);
                if (this.ShowAssMount.equals("1")) {
                    allCountriesv.add("→" + this.Item_Note + "  $" + this.ShowMount);
                } else {
                    allCountriesv.add("→" + this.Item_Note);
                }
            }
            this.cursor2.close();
        }
        this.cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCountries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.SOURCE_ITEM = (Spinner) findViewById(R.id.SOURCE_ITEM);
        this.SOURCE_ITEM.setAdapter((SpinnerAdapter) adapter);
        ArrayAdapter<String> adapterv = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCountriesv);
        adapterv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.VSOURCE_ITEM = (Spinner) findViewById(R.id.VSOURCE_ITEM);
        this.VSOURCE_ITEM.setAdapter((SpinnerAdapter) adapterv);
        this.VSOURCE_ITEM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: mymoney.zero.Addtransfer.9
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Addtransfer.this.SOURCE_ITEM.setSelection(arg2);
                double SOURCE_ITEM_EXCHANGE = 1.0d;
                try {
//                    String tSDCardPath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath2 = String.valueOf(getExternalFilesDir(null));
                    Addtransfer.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath2) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e2) {
                }
                Addtransfer.this.SOURCE_ITEM = (Spinner) Addtransfer.this.findViewById(R.id.SOURCE_ITEM);
                String SOURCE_ITEM_NOTE = Addtransfer.this.SOURCE_ITEM.getSelectedItem().toString();
                Addtransfer.this.SQL = "SELECT ITEM_CLASS,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(Addtransfer.this.AccountId) + " AND ITEM_NOTE = '" + SOURCE_ITEM_NOTE + "'";
                Addtransfer.this.cursor = Addtransfer.this.DataDB.rawQuery(Addtransfer.this.SQL, null);
                if (Addtransfer.this.cursor.moveToNext()) {
                    SOURCE_ITEM_EXCHANGE = Addtransfer.this.cursor.getDouble(1);
                }
                Addtransfer.this.cursor.close();
                if (SOURCE_ITEM_EXCHANGE != 1.0d) {
                    Addtransfer.this.EexchangeValue = SOURCE_ITEM_EXCHANGE;
                    DecimalFormat df32 = new DecimalFormat("#,##0" + Addtransfer.this.ExchangeFormat);
                    Addtransfer.this.EXCHANGE_VIEW.setText("匯：" + df32.format(Addtransfer.this.EexchangeValue));
                }
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        ArrayList<String> allCountries2 = new ArrayList<>();
        ArrayList<String> allCountries2v = new ArrayList<>();
        this.SQL = "SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE = PARENT_NOTE AND HIDESTYLE <> '1' ORDER BY ITEM_CLASS DESC,MAKE_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        while (this.cursor.moveToNext()) {
            this.Item_Note = this.cursor.getString(0);
            this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + this.Item_Note.trim() + "'";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            if (this.cursor2.moveToNext()) {
            }
            this.cursor2.close();
            allCountries2.add(this.Item_Note);
            if (this.ShowAssMount.equals("1")) {
                ItemMount = GetItemMount(this.cursor.getString(1), this.Item_Note);
            }
            this.ShowMount = df2.format(ItemMount);
            if (this.ShowAssMount.equals("1")) {
                allCountries2v.add(String.valueOf(this.Item_Note) + "  $" + this.ShowMount);
            } else {
                allCountries2v.add(this.Item_Note);
            }
            this.SQL = "SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + this.cursor.getString(0) + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            while (this.cursor2.moveToNext()) {
                this.Item_Note = this.cursor2.getString(0);
                allCountries2.add(this.Item_Note);
                if (this.ShowAssMount.equals("1")) {
                    ItemMount = GetItemMount(this.cursor2.getString(1), this.Item_Note);
                }
                this.ShowMount = df2.format(ItemMount);
                if (this.ShowAssMount.equals("1")) {
                    allCountries2v.add("→" + this.Item_Note + "  $" + this.ShowMount);
                } else {
                    allCountries2v.add("→" + this.Item_Note);
                }
            }
            this.cursor2.close();
        }
        this.cursor.close();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCountries2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.OBJECT_ITEM = (Spinner) findViewById(R.id.OBJECT_ITEM);
        this.OBJECT_ITEM.setAdapter((SpinnerAdapter) adapter2);
        ArrayAdapter<String> adapter2v = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCountries2v);
        adapter2v.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.VOBJECT_ITEM = (Spinner) findViewById(R.id.VOBJECT_ITEM);
        this.VOBJECT_ITEM.setAdapter((SpinnerAdapter) adapter2v);
        this.VOBJECT_ITEM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: mymoney.zero.Addtransfer.10
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Addtransfer.this.OBJECT_ITEM.setSelection(arg2);
                double OBJECT_ITEM_EXCHANGE = 1.0d;
                try {
//                    String tSDCardPath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath2 = String.valueOf(getExternalFilesDir(null));
                    Addtransfer.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath2) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e2) {
                }
                Addtransfer.this.OBJECT_ITEM = (Spinner) Addtransfer.this.findViewById(R.id.OBJECT_ITEM);
                String OBJECT_ITEM_NOTE = Addtransfer.this.OBJECT_ITEM.getSelectedItem().toString();
                Addtransfer.this.SQL = "SELECT ITEM_CLASS,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(Addtransfer.this.AccountId) + " AND ITEM_NOTE = '" + OBJECT_ITEM_NOTE + "'";
                Addtransfer.this.cursor = Addtransfer.this.DataDB.rawQuery(Addtransfer.this.SQL, null);
                if (Addtransfer.this.cursor.moveToNext()) {
                    OBJECT_ITEM_EXCHANGE = Addtransfer.this.cursor.getDouble(1);
                }
                Addtransfer.this.cursor.close();
                if (OBJECT_ITEM_EXCHANGE != 1.0d) {
                    Addtransfer.this.EexchangeValue = OBJECT_ITEM_EXCHANGE;
                    DecimalFormat df32 = new DecimalFormat("#,##0" + Addtransfer.this.ExchangeFormat);
                    Addtransfer.this.EXCHANGE_VIEW.setText("匯：" + df32.format(Addtransfer.this.EexchangeValue));
                }
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        ArrayList<String> allCountries3 = new ArrayList<>();
        this.cursor = this.DataDB.rawQuery("SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '支出' AND CHARGE = '1' AND HIDESTYLE <> '1' ORDER BY MAKE_NO", null);
        allCountries3.add("無");
        while (this.cursor.moveToNext()) {
            this.Item_Note = this.cursor.getString(0);
            allCountries3.add(this.Item_Note);
        }
        this.cursor.close();
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCountries3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mySpinner = (Spinner) findViewById(R.id.CHARGE_ITEM);
        this.mySpinner.setAdapter((SpinnerAdapter) adapter3);
        this.DataDB.close();
        this.CHARGE_ITEM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: mymoney.zero.Addtransfer.11
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (Addtransfer.this.CHARGE_ITEM.getSelectedItem().toString().equals("無")) {
                    Addtransfer.this.VCHARGE_MOUNT.setVisibility(View.INVISIBLE);
                    Addtransfer.this.CHARGE_MOUNT.setVisibility(View.INVISIBLE);
                } else {
                    Addtransfer.this.VCHARGE_MOUNT.setVisibility(View.VISIBLE);
                    Addtransfer.this.CHARGE_MOUNT.setVisibility(View.VISIBLE);
                }
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        this.DATA_DATE.setOnTouchListener(this.dateTouchListener);
        this.INOUT_MOUNT.setOnFocusChangeListener(this.MountfocusListener);
        this.CHARGE_MOUNT.setOnFocusChangeListener(this.ChargefocusListener);
        this.DATA_NOTE.setOnFocusChangeListener(this.DataNotefocusListener);
        EditText InputDate = (EditText) findViewById(R.id.DATA_DATE);
        InputDate.setKeyListener(new NumberKeyListener() { // from class: mymoney.zero.Addtransfer.12
            @Override // android.text.method.KeyListener
            public int getInputType() {
                return 0;
            }

            @Override // android.text.method.NumberKeyListener
            protected char[] getAcceptedChars() {
                return new char[0];
            }
        });
        this.DATA_NOTE.setFocusable(true);
        this.DATA_NOTE.requestFocus();
        this.DATA_NOTE.setFocusableInTouchMode(true);
        this.WeekView = (TextView) findViewById(R.id.WeekView);
        GetNowDate c = new GetNowDate();
        String SaveDate = c.CDateToEDate(this.DATA_DATE.getText().toString());
        String[] WeekData = {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        double y = Double.valueOf(SaveDate.substring(0, 4)).doubleValue();
        double m = Double.valueOf(SaveDate.substring(5, 7)).doubleValue() - 1.0d;
        double d = Double.valueOf(SaveDate.substring(8, 10)).doubleValue();
        Calendar cc = Calendar.getInstance();
        cc.set((int) y, (int) m, (int) d);
        String Week = WeekData[cc.get(7)];
        this.WeekView.setText(Week);
    }

    public void VoiceInput(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        startVoiceRecognitionActivity();
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.PROMPT", "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    public void SaveDataNext(View CvClick) throws IOException {
        boolean SubItem1;
        boolean SubItem2;
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.SOURCE_ITEM = (Spinner) findViewById(R.id.SOURCE_ITEM);
        this.OBJECT_ITEM = (Spinner) findViewById(R.id.OBJECT_ITEM);
        String SOURCE_ITEM_NOTE = this.SOURCE_ITEM.getSelectedItem().toString();
        String OBJECT_ITEM_NOTE = this.OBJECT_ITEM.getSelectedItem().toString();
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e2) {
        }
        this.SQL = "SELECT PARENT_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + SOURCE_ITEM_NOTE.toString().replace("'", "''") + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.SOURCE_PARENT_NOTE = this.cursor.getString(0);
        } else {
            this.SOURCE_PARENT_NOTE = "";
        }
        this.cursor.close();
        if (this.SOURCE_PARENT_NOTE.trim().equals(SOURCE_ITEM_NOTE.trim())) {
            this.SOURCE_SUB_STYLE = false;
        } else {
            this.SOURCE_SUB_STYLE = true;
        }
        this.SQL = "SELECT PARENT_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + OBJECT_ITEM_NOTE.toString().replace("'", "''") + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.OBJECT_PARENT_NOTE = this.cursor.getString(0);
        } else {
            this.OBJECT_PARENT_NOTE = "";
        }
        this.cursor.close();
        if (this.OBJECT_PARENT_NOTE.trim().equals(OBJECT_ITEM_NOTE.trim())) {
            this.OBJECT_SUB_STYLE = false;
        } else {
            this.OBJECT_SUB_STYLE = true;
        }
        this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + SOURCE_ITEM_NOTE.trim() + "'";
        this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor2.moveToNext()) {
            SubItem1 = true;
        } else {
            SubItem1 = false;
        }
        this.cursor2.close();
        if (SubItem1) {
            this.DataDB.close();
            Toast.makeText(this, "訊息：[" + SOURCE_ITEM_NOTE.trim() + "] 底下尚有設定子項目，您不可使用母項目作為目的項目!!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + OBJECT_ITEM_NOTE.trim() + "'";
        this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor2.moveToNext()) {
            SubItem2 = true;
        } else {
            SubItem2 = false;
        }
        this.cursor2.close();
        if (SubItem2) {
            this.DataDB.close();
            Toast.makeText(this, "訊息：[" + OBJECT_ITEM_NOTE.trim() + "] 底下尚有設定子項目，您不可使用母項目作為目的項目!!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.DataDB.close();
        if (this.SOURCE_PARENT_NOTE.trim().equals(this.OBJECT_PARENT_NOTE.trim()) && !this.SOURCE_SUB_STYLE && this.OBJECT_SUB_STYLE) {
            Toast.makeText(this, "提示：您不可將同一 [母項目] 下的 [母項目] 轉帳到同一 [母項目] 的 [子項目] 下喔!", Toast.LENGTH_LONG).show();
            return;
        }
        if (this.SOURCE_PARENT_NOTE.trim().equals(this.OBJECT_PARENT_NOTE.trim()) && this.SOURCE_SUB_STYLE && !this.OBJECT_SUB_STYLE) {
            Toast.makeText(this, "提示：您不可將同一 [母項目] 下的 [子項目] 轉帳到同一 [母項目] 的 [母項目] 下喔!", Toast.LENGTH_LONG).show();
            return;
        }
        if (SOURCE_ITEM_NOTE.trim().equals(OBJECT_ITEM_NOTE.trim())) {
            Toast.makeText(this, "提示：來源 與 目的 帳戶不可相同喔!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (SaveData() == 1) {
            this.INOUT_MOUNT.setText("0");
            this.DATA_NOTE.setText("");
            this.PAY_COLL_NAME.setText("");
            this.DATA_NOTE.setFocusable(true);
            this.DATA_NOTE.requestFocus();
            this.DATA_NOTE.setFocusableInTouchMode(true);
            if (this.ShowAssMount.equals("1")) {
                RefAssMount();
            }
            for (int i = 0; i <= this.SOURCE_ITEM.getCount(); i++) {
                this.SOURCE_ITEM.setSelection(i, true);
                this.VSOURCE_ITEM.setSelection(i, true);
                if (this.SOURCE_ITEM.getSelectedItem().toString().trim().equals(SOURCE_ITEM_NOTE)) {
                    break;
                }
            }
            for (int i2 = 0; i2 <= this.OBJECT_ITEM.getCount(); i2++) {
                this.OBJECT_ITEM.setSelection(i2, true);
                this.VOBJECT_ITEM.setSelection(i2, true);
                if (this.OBJECT_ITEM.getSelectedItem().toString().trim().equals(OBJECT_ITEM_NOTE)) {
                    break;
                }
            }
            this.InputDetail = "";
        }
    }

    public void SaveDataExit(View CvClick) throws IOException {
        boolean SubItem1;
        boolean SubItem2;
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.SOURCE_ITEM = (Spinner) findViewById(R.id.SOURCE_ITEM);
        this.OBJECT_ITEM = (Spinner) findViewById(R.id.OBJECT_ITEM);
        String SOURCE_ITEM_NOTE = this.SOURCE_ITEM.getSelectedItem().toString();
        String OBJECT_ITEM_NOTE = this.OBJECT_ITEM.getSelectedItem().toString();
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e2) {
        }
        this.SQL = "SELECT PARENT_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + SOURCE_ITEM_NOTE.toString().replace("'", "''") + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.SOURCE_PARENT_NOTE = this.cursor.getString(0);
        } else {
            this.SOURCE_PARENT_NOTE = "";
        }
        this.cursor.close();
        if (this.SOURCE_PARENT_NOTE.trim().equals(SOURCE_ITEM_NOTE.trim())) {
            this.SOURCE_SUB_STYLE = false;
        } else {
            this.SOURCE_SUB_STYLE = true;
        }
        this.SQL = "SELECT PARENT_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + OBJECT_ITEM_NOTE.toString().replace("'", "''") + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.OBJECT_PARENT_NOTE = this.cursor.getString(0);
        } else {
            this.OBJECT_PARENT_NOTE = "";
        }
        this.cursor.close();
        if (this.OBJECT_PARENT_NOTE.trim().equals(OBJECT_ITEM_NOTE.trim())) {
            this.OBJECT_SUB_STYLE = false;
        } else {
            this.OBJECT_SUB_STYLE = true;
        }
        this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + SOURCE_ITEM_NOTE.trim() + "'";
        this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor2.moveToNext()) {
            SubItem1 = true;
        } else {
            SubItem1 = false;
        }
        this.cursor2.close();
        if (SubItem1) {
            this.DataDB.close();
            Toast.makeText(this, "訊息：[" + SOURCE_ITEM_NOTE.trim() + "] 底下尚有設定子項目，您不可使用母項目作為目的項目!!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + OBJECT_ITEM_NOTE.trim() + "'";
        this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor2.moveToNext()) {
            SubItem2 = true;
        } else {
            SubItem2 = false;
        }
        this.cursor2.close();
        if (SubItem2) {
            this.DataDB.close();
            Toast.makeText(this, "訊息：[" + OBJECT_ITEM_NOTE.trim() + "] 底下尚有設定子項目，您不可使用母項目作為目的項目!!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.DataDB.close();
        if (this.SOURCE_PARENT_NOTE.trim().equals(this.OBJECT_PARENT_NOTE.trim()) && !this.SOURCE_SUB_STYLE && this.OBJECT_SUB_STYLE) {
            Toast.makeText(this, "提示：您不可將同一 [母項目] 下的 [母項目] 轉帳到同一 [母項目] 的 [子項目] 下喔!", Toast.LENGTH_LONG).show();
            return;
        }
        if (this.SOURCE_PARENT_NOTE.trim().equals(this.OBJECT_PARENT_NOTE.trim()) && this.SOURCE_SUB_STYLE && !this.OBJECT_SUB_STYLE) {
            Toast.makeText(this, "提示：您不可將同一 [母項目] 下的 [子項目] 轉帳到同一 [母項目] 的 [母項目] 下喔!", Toast.LENGTH_LONG).show();
            return;
        }
        if (SOURCE_ITEM_NOTE.trim().equals(OBJECT_ITEM_NOTE.trim())) {
            Toast.makeText(this, "提示：來源 與 目的 帳戶不可相同喔!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (SaveData() == 1) {
            Intent intent = new Intent();
            if (this.ReturnHome.equals("1")) {
                intent.setClass(this, MyMoneyZeroActivity.class);
            } else {
//                String tSDCardPath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
                String tSDCardPath2 = String.valueOf(getExternalFilesDir(null));
                try {
                    this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath2) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e3) {
                }
                this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '帳務記錄顯示發票號碼'";
                this.cursor = this.DataDB.rawQuery(this.SQL, null);
                if (this.cursor.moveToNext() && this.cursor.getString(0).trim().endsWith("1")) {
                    this.cursor.close();
                    intent.setClass(this, DayInOutShow2.class);
                } else {
                    this.cursor.close();
                    intent.setClass(this, DayInOutShow.class);
                }
            }
            startActivity(intent);
            finish();
        }
    }

    public int SaveData() throws IOException, SQLException {
        double Make_No;
        double SOURCE_ITEM_EXCHANGE = 1.0d;
        double OBJECT_ITEM_EXCHANGE = 1.0d;
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        try {
            DecimalFormat df2 = new DecimalFormat("#,##0" + this.MountFormat);
            this.INOUT_MOUNT.setText(df2.format(Double.valueOf(this.INOUT_MOUNT.getText().toString())));
        } catch (Exception e2) {
        }
        this.DATA_DATE = (TextView) findViewById(R.id.DATA_DATE);
        this.SOURCE_ITEM = (Spinner) findViewById(R.id.SOURCE_ITEM);
        this.OBJECT_ITEM = (Spinner) findViewById(R.id.OBJECT_ITEM);
        this.INOUT_MOUNT = (TextView) findViewById(R.id.INOUT_MOUNT);
        this.CHARGE_MOUNT = (TextView) findViewById(R.id.CHARGE_MOUNT);
        this.DATA_NOTE = (TextView) findViewById(R.id.DATA_NOTE);
        this.PAY_COLL_NAME = (TextView) findViewById(R.id.PAY_COLL_NAME);
        if (this.INOUT_MOUNT.getText().toString().trim().length() == 0) {
            this.INOUT_MOUNT.setText("0");
        }
        if (this.CHARGE_MOUNT.getText().toString().trim().length() == 0) {
            this.CHARGE_MOUNT.setText("0");
        }
        GetNowDate c = new GetNowDate();
        String SaveDate = c.CDateToEDate(this.DATA_DATE.getText().toString());
        if (this.DATA_DATE.length() != 16) {
            ShowBox("日期錯誤", "您所輸入的日期 " + this.DATA_DATE.getText().toString() + "\n不是正確的期格式");
            return 0;
        }
        if (!IsDate(SaveDate)) {
            ShowBox("日期錯誤", "您所輸入的日期\n" + this.DATA_DATE.getText().toString() + "\n不是正確的期格式");
            return 0;
        }
        String SOURCE_ITEM_NOTE = this.SOURCE_ITEM.getSelectedItem().toString();
        String OBJECT_ITEM_NOTE = this.OBJECT_ITEM.getSelectedItem().toString();
        double SaveMount = Double.valueOf(this.INOUT_MOUNT.getText().toString().replace(",", "")).doubleValue();
        Cursor cursor = this.DataDB.rawQuery("SELECT MAKE_NO FROM MYMONEY_DATA ORDER BY MAKE_NO DESC", null);
        if (cursor.moveToNext()) {
            Make_No = cursor.getDouble(0) + 1.0d;
        } else {
            Make_No = 1.0d;
        }
        cursor.close();
        String SOURCE_ITEM_CLASS = "";
        String OBJECT_ITEM_CLASS = "";
        this.SQL = "SELECT ITEM_CLASS,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + SOURCE_ITEM_NOTE + "'";
        Cursor cursor2 = this.DataDB.rawQuery(this.SQL, null);
        if (cursor2.moveToNext()) {
            SOURCE_ITEM_CLASS = cursor2.getString(0);
            SOURCE_ITEM_EXCHANGE = cursor2.getDouble(1);
        }
        cursor2.close();
        this.SQL = "SELECT ITEM_CLASS,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + OBJECT_ITEM_NOTE + "'";
        Cursor cursor3 = this.DataDB.rawQuery(this.SQL, null);
        if (cursor3.moveToNext()) {
            OBJECT_ITEM_CLASS = cursor3.getString(0);
            OBJECT_ITEM_EXCHANGE = cursor3.getDouble(1);
        }
        cursor3.close();
        if (SOURCE_ITEM_EXCHANGE != 1.0d && OBJECT_ITEM_EXCHANGE != 1.0d && SOURCE_ITEM_EXCHANGE != OBJECT_ITEM_EXCHANGE) {
            ShowBox("訊息!", "您不可將二種記錄有匯率 1 以外的不同幣別互相轉換!");
            return 0;
        }
        if (SOURCE_ITEM_EXCHANGE != 1.0d) {
            SOURCE_ITEM_EXCHANGE = this.EexchangeValue;
        }
        if (OBJECT_ITEM_EXCHANGE != 1.0d) {
            OBJECT_ITEM_EXCHANGE = this.EexchangeValue;
        }
        double OBJECT_SaveMount = SaveMount / (OBJECT_ITEM_EXCHANGE / SOURCE_ITEM_EXCHANGE);
        double CHANGE_SaveMount = Double.valueOf(this.CHARGE_MOUNT.getText().toString().replaceAll(",", "")).doubleValue() / (1.0d / SOURCE_ITEM_EXCHANGE);
        GetNowDate GetNow = new GetNowDate();
        String DATA_KEY = null;
        try {
            DATA_KEY = GetNow.GetDataKey();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (SOURCE_ITEM_EXCHANGE != 1.0d && OBJECT_ITEM_EXCHANGE == 1.0d && this.Rounding == "1") {
            OBJECT_SaveMount = Math.round(OBJECT_SaveMount);
        }
        this.SQL = "INSERT INTO MYMONEY_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_CLASS,DATA_DATE,ITEM_NOTE,IN_MOUNT,OUT_MOUNT,DATA_NOTE,INVOICE_NO,DATA_NO,PAY_COLL_NAME,PROJECT_ID,EXCHANGE,LINK_PC,EXPORT,DATA_NOTE2,DATA_KEY) VALUES ('admin'," + String.valueOf(this.AccountId) + "," + String.valueOf(Make_No) + ",'" + SOURCE_ITEM_CLASS + "','" + SaveDate.trim() + "','" + SOURCE_ITEM_NOTE + "',0,0" + String.valueOf(SaveMount) + ",'" + this.DATA_NOTE.getText().toString().trim().replaceAll("'", "''") + "','','1','" + this.PAY_COLL_NAME.getText().toString().trim().replaceAll("'", "''") + "',''," + String.valueOf(SOURCE_ITEM_EXCHANGE) + ",'','0','" + this.InputDetail.replace("'", "''") + "','" + DATA_KEY + "')";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "INSERT INTO MYMONEY_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_CLASS,DATA_DATE,ITEM_NOTE,IN_MOUNT,OUT_MOUNT,DATA_NOTE,INVOICE_NO,DATA_NO,PAY_COLL_NAME,PROJECT_ID,EXCHANGE,LINK_PC,EXPORT,DATA_NOTE2,DATA_KEY) VALUES ('admin'," + String.valueOf(this.AccountId) + "," + String.valueOf(Make_No) + ",'" + OBJECT_ITEM_CLASS + "','" + SaveDate.trim() + "','" + OBJECT_ITEM_NOTE + "',0" + String.valueOf(OBJECT_SaveMount) + ",0,'" + this.DATA_NOTE.getText().toString().trim().replaceAll("'", "''") + "','','2','" + this.PAY_COLL_NAME.getText().toString().trim().replaceAll("'", "''") + "',''," + String.valueOf(OBJECT_ITEM_EXCHANGE) + ",'','0','" + this.InputDetail.replace("'", "''") + "','" + DATA_KEY + "')";
        this.DataDB.execSQL(this.SQL);
        String SaveChargeItem = this.CHARGE_ITEM.getSelectedItem().toString().trim();
        if (!SaveChargeItem.equals("無")) {
            GetNowDate GetNow2 = new GetNowDate();
            String DATA_KEY2 = null;
            try {
                DATA_KEY2 = GetNow2.GetDataKey();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (DATA_KEY2 == DATA_KEY) {
                for (int i = 0; i <= 1000; i++) {
                }
                try {
                    DATA_KEY2 = GetNow2.GetDataKey();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            this.SQL = "INSERT INTO MYMONEY_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_CLASS,DATA_DATE,ITEM_NOTE,IN_MOUNT,OUT_MOUNT,DATA_NOTE,INVOICE_NO,DATA_NO,PAY_COLL_NAME,PROJECT_ID,EXCHANGE,LINK_PC,EXPORT,DATA_NOTE2,DATA_KEY) VALUES ('admin'," + String.valueOf(this.AccountId) + "," + String.valueOf(1.0d + Make_No) + ",'" + SOURCE_ITEM_CLASS + "','" + SaveDate.trim() + "','" + SOURCE_ITEM_NOTE + "',0,0" + String.valueOf(CHANGE_SaveMount) + ",'" + SaveChargeItem + "','','1','',''," + String.valueOf(SOURCE_ITEM_EXCHANGE) + ",'','0','','" + DATA_KEY2 + "')";
            this.DataDB.execSQL(this.SQL);
            this.SQL = "INSERT INTO MYMONEY_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_CLASS,DATA_DATE,ITEM_NOTE,IN_MOUNT,OUT_MOUNT,DATA_NOTE,INVOICE_NO,DATA_NO,PAY_COLL_NAME,PROJECT_ID,EXCHANGE,LINK_PC,EXPORT,DATA_NOTE2,DATA_KEY) VALUES ('admin'," + String.valueOf(this.AccountId) + "," + String.valueOf(1.0d + Make_No) + ",'支出','" + SaveDate.trim() + "','" + SaveChargeItem + "',0" + String.valueOf(CHANGE_SaveMount) + ",0,'" + SaveChargeItem + "','','2','',''," + String.valueOf(OBJECT_ITEM_EXCHANGE) + ",'','0','','" + DATA_KEY2 + "')";
            this.DataDB.execSQL(this.SQL);
        }
        this.DataDB.close();
        Toast.makeText(this, "新增帳務記錄資料完成!", Toast.LENGTH_SHORT).show();
        BackDataToStorage();
//        try {
//            Intent i2 = new Intent(getBaseContext(), (Class<?>) MyWidget.class);
//            i2.setAction("com.android.mymoneyzero");
//            sendBroadcast(i2);
//        } catch (Exception e3) {
//        }
        return 1;
    }

    private void BackDataToStorage() throws IOException {
        new GetNowDate();
        File dir = getFilesDir();
        String DataFileDir = dir.getPath();
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            File tDataPath = new File(String.valueOf(tSDCardPath) + "/MyMoneyZero/");
            String tDBFilePath = tDataPath + "/mymoney.db";
            File tFile = new File(tDBFilePath);
            if (tFile.exists()) {
                FileInputStream tISStream = new FileInputStream(tDBFilePath);
                FileOutputStream tOutStream = new FileOutputStream(String.valueOf(DataFileDir) + "/mymoney.db");
                byte[] tBuffer = new byte[5120];
                while (true) {
                    int tCount = tISStream.read(tBuffer);
                    if (tCount > 0) {
                        tOutStream.write(tBuffer, 0, tCount);
                    } else {
                        tOutStream.close();
                        tISStream.close();
                        return;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void ShowBox(String TitleString, String BodyString) {
        if (TitleString == "") {
            TitleString = "訊息";
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle(TitleString);
        MyAlertDialog.setMessage(BodyString);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.13
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyAlertDialog.setPositiveButton("確定", OkClick);
        MyAlertDialog.show();
    }

    public boolean IsDate(String date) {
        Pattern p = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1][0-9])|([2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = p.matcher(date);
        boolean b = m.matches();
        return b;
    }

    public void CloseReturn(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.INOUT_MOUNT = (TextView) findViewById(R.id.INOUT_MOUNT);
        if (!this.INOUT_MOUNT.getText().toString().trim().equals("0") && !this.INOUT_MOUNT.getText().toString().trim().equals("")) {
            AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
            MyAlertDialog.setTitle("確認");
            MyAlertDialog.setMessage("您目前的資料尚未存入,請問您是否要離開此畫面呢?");
            DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.14
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(Addtransfer.this, MyMoneyZeroActivity.class);
                    Addtransfer.this.startActivity(intent);
                    Addtransfer.this.finish();
                }
            };
            DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.15
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                }
            };
            MyAlertDialog.setPositiveButton("確定", OkClick);
            MyAlertDialog.setNegativeButton("取消", CanCelClick);
            MyAlertDialog.show();
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, MyMoneyZeroActivity.class);
        startActivity(intent);
        finish();
    }

    public void ShowDateSelect() {
        GetNowDate c = new GetNowDate();
        String TempDate = this.DATA_DATE.getText().toString();
        if (!IsDate(c.CDateToEDate(TempDate))) {
            this.DATA_DATE.setText(c.GetCDate());
        }
        if (this.DATA_DATE.length() != 16) {
            this.DATA_DATE.setText(c.GetCDate());
        }
        SelectDate();
    }

    public void ShowOftenSelect(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        ShwoOftenWindow();
    }

    public void ShowDetailInput(View CvClick) {
        double SOURCE_ITEM_EXCHANGE = 1.0d;
        double OBJECT_ITEM_EXCHANGE = 1.0d;
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e2) {
        }
        this.SOURCE_ITEM = (Spinner) findViewById(R.id.SOURCE_ITEM);
        this.OBJECT_ITEM = (Spinner) findViewById(R.id.OBJECT_ITEM);
        String SOURCE_ITEM_NOTE = this.SOURCE_ITEM.getSelectedItem().toString();
        String OBJECT_ITEM_NOTE = this.OBJECT_ITEM.getSelectedItem().toString();
        this.SQL = "SELECT ITEM_CLASS,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + SOURCE_ITEM_NOTE + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            SOURCE_ITEM_EXCHANGE = this.cursor.getDouble(1);
        }
        this.cursor.close();
        this.SQL = "SELECT ITEM_CLASS,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + OBJECT_ITEM_NOTE + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            OBJECT_ITEM_EXCHANGE = this.cursor.getDouble(1);
        }
        this.cursor.close();
        if (SOURCE_ITEM_EXCHANGE == 1.0d && OBJECT_ITEM_EXCHANGE == 1.0d) {
            ShowBox("訊息!", "您這次的轉帳記錄不是外幣轉帳,您不可變更匯率值!");
            return;
        }
        this.MountFocus = "EXCHANGE";
        Intent intent = new Intent();
        intent.setClass(this, Calc.class);
        Bundle bundle = new Bundle();
        bundle.putString("InMount", String.valueOf(this.EexchangeValue));
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    public void ShowCollPalSelect(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        ShwoCollNameWindow();
    }

    public void ShwoOftenWindow() {
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.OBJECT_ITEM = (Spinner) findViewById(R.id.OBJECT_ITEM);
        String OBJECT_ITEM_NOTE = this.OBJECT_ITEM.getSelectedItem().toString();
        this.cursor = this.DataDB.rawQuery("SELECT DATA_NOTE FROM OFTEN_NOTE WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + OBJECT_ITEM_NOTE + "'", null);
        int NoteCount = this.cursor.getCount();
        this.cursor.close();
        final String[] OftenList = new String[NoteCount + 1];
        int OftenPtr = 0;
        this.cursor = this.DataDB.rawQuery("SELECT DATA_NOTE FROM OFTEN_NOTE WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + OBJECT_ITEM_NOTE + "' ORDER BY DATA_NOTE", null);
        while (this.cursor.moveToNext()) {
            OftenList[OftenPtr] = this.cursor.getString(0);
            OftenPtr++;
        }
        this.cursor.close();
        this.DataDB.close();
        OftenList[OftenPtr] = "新增摘要";
        int i = OftenPtr + 1;
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("常用摘要選擇");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.16
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (OftenList[which].toString().trim() != "新增摘要") {
                    Addtransfer.this.DATA_NOTE.setText(OftenList[which]);
                } else {
                    Addtransfer.this.ShowAddOfftenNote();
                }
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.17
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyAlertDialog.setItems(OftenList, OkClick);
        MyAlertDialog.setNegativeButton("取消", CanCelClick);
        MyAlertDialog.show();
    }

    public void ShowAddOfftenNote() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("新增常用摘要");
        alert.setMessage("請輸入常用摘要");
        final EditText input = new EditText(this);
        alert.setView(input);
        input.setText("");
        input.setHint("請輸入常用摘要");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.18
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) throws SQLException {
                double MAKE_NO;
                String InputNote = input.getText().toString();
                String OBJECT_ITEM_CLASS = "";
                if (InputNote.toString().trim() != "") {
                    try {
//                        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        String tSDCardPath = String.valueOf(getExternalFilesDir(null));
                        Addtransfer.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                    } catch (Exception e) {
                    }
                    String OBJECT_ITEM_NOTE = Addtransfer.this.OBJECT_ITEM.getSelectedItem().toString();
                    Addtransfer.this.SQL = "SELECT ITEM_CLASS,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(Addtransfer.this.AccountId) + " AND ITEM_NOTE = '" + OBJECT_ITEM_NOTE + "'";
                    Addtransfer.this.cursor = Addtransfer.this.DataDB.rawQuery(Addtransfer.this.SQL, null);
                    if (Addtransfer.this.cursor.moveToNext()) {
                        OBJECT_ITEM_CLASS = Addtransfer.this.cursor.getString(0);
                    }
                    Addtransfer.this.cursor.close();
                    Addtransfer.this.SQL = "SELECT MAKE_NO FROM OFTEN_NOTE WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(Addtransfer.this.AccountId) + " AND ITEM_CLASS = '" + OBJECT_ITEM_CLASS + "' ORDER BY MAKE_NO DESC";
                    Addtransfer.this.cursor = Addtransfer.this.DataDB.rawQuery(Addtransfer.this.SQL, null);
                    if (Addtransfer.this.cursor.moveToNext()) {
                        MAKE_NO = Addtransfer.this.cursor.getDouble(0) + 1.0d;
                    } else {
                        MAKE_NO = 1.0d;
                    }
                    Addtransfer.this.cursor.close();
                    Addtransfer.this.SQL = "INSERT INTO OFTEN_NOTE (USER_ID,ACCOUNT_ID,ITEM_CLASS,ITEM_NOTE,MAKE_NO,DATA_NOTE) VALUES ('admin'," + String.valueOf(Addtransfer.this.AccountId) + ",'" + OBJECT_ITEM_CLASS + "','" + OBJECT_ITEM_NOTE.trim() + "'," + String.valueOf(MAKE_NO) + ",'" + InputNote.replace("'", "") + "')";
                    Addtransfer.this.DataDB.execSQL(Addtransfer.this.SQL);
                    Addtransfer.this.DataDB.close();
                    Addtransfer.this.DATA_NOTE.setText(InputNote.toString().trim());
                }
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.19
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        alert.setPositiveButton("確定", OkClick);
        alert.setNegativeButton("取消", CanCelClick);
        alert.show();
    }

    public void ShwoCollNameWindow() {
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.cursor = this.DataDB.rawQuery("SELECT COLL_NAME FROM PAY_COLL_NAME WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId), null);
        int NoteCount = this.cursor.getCount();
        this.cursor.close();
        final String[] OftenList = new String[NoteCount + 1];
        int OftenPtr = 0;
        this.cursor = this.DataDB.rawQuery("SELECT COLL_NAME FROM PAY_COLL_NAME WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " ORDER BY MAKE_NO", null);
        while (this.cursor.moveToNext()) {
            OftenList[OftenPtr] = this.cursor.getString(0);
            OftenPtr++;
        }
        this.cursor.close();
        this.DataDB.close();
        OftenList[OftenPtr] = "新增收付人";
        int i = OftenPtr + 1;
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("收付人選擇");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.20
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (OftenList[which].toString().trim() != "新增收付人") {
                    Addtransfer.this.PAY_COLL_NAME.setText(OftenList[which]);
                } else {
                    Addtransfer.this.ShowAddPayCollName();
                }
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.21
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyAlertDialog.setItems(OftenList, OkClick);
        MyAlertDialog.setNegativeButton("取消", CanCelClick);
        MyAlertDialog.show();
    }

    public void ShowAddPayCollName() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("新增收付人");
        alert.setMessage("請輸入收付人");
        final EditText input = new EditText(this);
        alert.setView(input);
        input.setText("");
        input.setHint("請輸入收付人");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.22
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) throws SQLException {
                double MAKE_NO;
                String InputNote = input.getText().toString();
                if (InputNote.toString().trim() != "") {
                    try {
//                        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        String tSDCardPath = String.valueOf(getExternalFilesDir(null));
                        Addtransfer.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                    } catch (Exception e) {
                    }
                    Addtransfer.this.SQL = "SELECT MAKE_NO FROM PAY_COLL_NAME WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(Addtransfer.this.AccountId) + " ORDER BY MAKE_NO DESC";
                    Addtransfer.this.cursor = Addtransfer.this.DataDB.rawQuery(Addtransfer.this.SQL, null);
                    if (Addtransfer.this.cursor.moveToNext()) {
                        MAKE_NO = Addtransfer.this.cursor.getDouble(0) + 1.0d;
                    } else {
                        MAKE_NO = 1.0d;
                    }
                    Addtransfer.this.cursor.close();
                    Addtransfer.this.SQL = "INSERT INTO PAY_COLL_NAME (USER_ID,ACCOUNT_ID,MAKE_NO,COLL_NAME) VALUES ('admin'," + String.valueOf(Addtransfer.this.AccountId) + "," + String.valueOf(MAKE_NO) + ",'" + InputNote.replace("'", "") + "')";
                    Addtransfer.this.DataDB.execSQL(Addtransfer.this.SQL);
                    Addtransfer.this.DataDB.close();
                    Addtransfer.this.PAY_COLL_NAME.setText(InputNote.toString().trim());
                }
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.23
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        alert.setPositiveButton("確定", OkClick);
        alert.setNegativeButton("取消", CanCelClick);
        alert.show();
    }

    public void RefAssMount() {
        double ItemMount = 0.0d;
//        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String tSDCardPath = String.valueOf(getExternalFilesDir(null));
        try {
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        ArrayList<String> allCountries = new ArrayList<>();
        ArrayList<String> allCountriesv = new ArrayList<>();
        this.SQL = "SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE = PARENT_NOTE AND HIDESTYLE <> '1' ORDER BY ITEM_CLASS DESC,MAKE_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        while (this.cursor.moveToNext()) {
            this.Item_Note = this.cursor.getString(0);
            this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + this.Item_Note.trim() + "'";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            if (this.cursor2.moveToNext()) {
            }
            this.cursor2.close();
            DecimalFormat df2 = new DecimalFormat("#,##0" + this.MountFormat);
            if (this.ShowAssMount.equals("1")) {
                ItemMount = GetItemMount(this.cursor.getString(1), this.Item_Note);
            }
            allCountries.add(this.Item_Note);
            this.ShowMount = df2.format(ItemMount);
            if (this.ShowAssMount.equals("1")) {
                allCountriesv.add(String.valueOf(this.Item_Note) + "  $" + this.ShowMount);
            } else {
                allCountriesv.add(this.Item_Note);
            }
            this.SQL = "SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + this.cursor.getString(0) + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            while (this.cursor2.moveToNext()) {
                this.Item_Note = this.cursor2.getString(0);
                if (this.ShowAssMount.equals("1")) {
                    ItemMount = GetItemMount(this.cursor.getString(1), this.Item_Note);
                }
                allCountries.add(this.Item_Note);
                this.ShowMount = df2.format(ItemMount);
                if (this.ShowAssMount.equals("1")) {
                    allCountriesv.add("→" + this.Item_Note + "  $" + this.ShowMount);
                } else {
                    allCountriesv.add("→" + this.Item_Note);
                }
            }
            this.cursor2.close();
        }
        this.cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCountries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.SOURCE_ITEM = (Spinner) findViewById(R.id.SOURCE_ITEM);
        this.SOURCE_ITEM.setAdapter((SpinnerAdapter) adapter);
        ArrayAdapter<String> adapterv = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCountriesv);
        adapterv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.VSOURCE_ITEM = (Spinner) findViewById(R.id.VSOURCE_ITEM);
        this.VSOURCE_ITEM.setAdapter((SpinnerAdapter) adapterv);
        this.VSOURCE_ITEM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: mymoney.zero.Addtransfer.24
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Addtransfer.this.SOURCE_ITEM.setSelection(arg2);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        ArrayList<String> allCountries2 = new ArrayList<>();
        ArrayList<String> allCountries2v = new ArrayList<>();
        this.SQL = "SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE = PARENT_NOTE AND HIDESTYLE <> '1' ORDER BY ITEM_CLASS DESC,MAKE_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        while (this.cursor.moveToNext()) {
            this.Item_Note = this.cursor.getString(0);
            this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + this.Item_Note.trim() + "'";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            if (this.cursor2.moveToNext()) {
            }
            this.cursor2.close();
            DecimalFormat df22 = new DecimalFormat("#,##0" + this.MountFormat);
            allCountries2.add(this.Item_Note);
            if (this.ShowAssMount.equals("1")) {
                ItemMount = GetItemMount(this.cursor.getString(1), this.Item_Note);
            }
            this.ShowMount = df22.format(ItemMount);
            if (this.ShowAssMount.equals("1")) {
                allCountries2v.add(String.valueOf(this.Item_Note) + "  $" + this.ShowMount);
            } else {
                allCountries2v.add(this.Item_Note);
            }
            this.SQL = "SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + this.cursor.getString(0) + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            while (this.cursor2.moveToNext()) {
                this.Item_Note = this.cursor2.getString(0);
                allCountries2.add(this.Item_Note);
                if (this.ShowAssMount.equals("1")) {
                    ItemMount = GetItemMount(this.cursor2.getString(1), this.Item_Note);
                }
                this.ShowMount = df22.format(ItemMount);
                if (this.ShowAssMount.equals("1")) {
                    allCountries2v.add("→" + this.Item_Note + "  $" + this.ShowMount);
                } else {
                    allCountries2v.add("→" + this.Item_Note);
                }
            }
            this.cursor2.close();
        }
        this.cursor.close();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCountries2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.OBJECT_ITEM = (Spinner) findViewById(R.id.OBJECT_ITEM);
        this.OBJECT_ITEM.setAdapter((SpinnerAdapter) adapter2);
        ArrayAdapter<String> adapter2v = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCountries2v);
        adapter2v.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.VOBJECT_ITEM = (Spinner) findViewById(R.id.VOBJECT_ITEM);
        this.VOBJECT_ITEM.setAdapter((SpinnerAdapter) adapter2v);
        this.VOBJECT_ITEM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: mymoney.zero.Addtransfer.25
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Addtransfer.this.OBJECT_ITEM.setSelection(arg2);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public double GetItemMount(String ITEM_CLASS, String ITEM_NOTE) {
        double NowMount;
        double ItemMount = 0.0d;
        GetNowDate c = new GetNowDate();
        String LoadDate = c.CDateToEDate(this.DATA_DATE.getText().toString());
//        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String tSDCardPath = String.valueOf(getExternalFilesDir(null));
        try {
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + ITEM_CLASS.trim() + "' AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "' ORDER BY MAKE_NO";
        Cursor vcursor = this.DataDB.rawQuery(this.SQL, null);
        while (vcursor.moveToNext()) {
            double BeforeMount = vcursor.getDouble(1);
            if (ITEM_CLASS.trim().equals("資產")) {
                this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "' AND DATA_DATE <= '" + LoadDate + "'";
            } else {
                this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "' AND DATA_DATE <= '" + LoadDate + "'";
            }
            Cursor vcursor2 = this.DataDB.rawQuery(this.SQL, null);
            if (vcursor2.moveToNext()) {
                NowMount = vcursor2.getDouble(0);
            } else {
                NowMount = 0.0d;
            }
            vcursor2.close();
            ItemMount = BeforeMount + NowMount;
        }
        vcursor.close();
        return ItemMount;
    }

    public void SelectDate() {
        GetNowDate c = new GetNowDate();
        double y = c.GetCDateYear(this.DATA_DATE.getText().toString()).doubleValue();
        double m = c.GetCDateMonth(this.DATA_DATE.getText().toString()).doubleValue() - 1.0d;
        double d = c.GetCDateDay(this.DATA_DATE.getText().toString()).doubleValue();
        DatePickerDialog dpd = new DatePickerDialog(this, this.myOnDateSetListener, (int) y, (int) m, (int) d);
        dpd.show();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 3:
                return true;
            case 4:
                this.INOUT_MOUNT = (TextView) findViewById(R.id.INOUT_MOUNT);
                if (!this.INOUT_MOUNT.getText().toString().trim().equals("0") && !this.INOUT_MOUNT.getText().toString().trim().equals("")) {
                    AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
                    MyAlertDialog.setTitle("確認");
                    MyAlertDialog.setMessage("您目前的資料尚未存入,請問您是否要離開此畫面呢?");
                    DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.26
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(Addtransfer.this, MyMoneyZeroActivity.class);
                            Addtransfer.this.startActivity(intent);
                            Addtransfer.this.finish();
                        }
                    };
                    DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addtransfer.27
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    };
                    MyAlertDialog.setPositiveButton("確定", OkClick);
                    MyAlertDialog.setNegativeButton("取消", CanCelClick);
                    MyAlertDialog.show();
                    break;
                } else {
                    Intent intent = new Intent();
                    intent.setClass(this, MyMoneyZeroActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
        }
        return super.onKeyDown(keyCode, event);
    }
}