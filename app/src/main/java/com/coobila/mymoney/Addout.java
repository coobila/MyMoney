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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
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
public class Addout extends Activity {
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private TextView DATA_DATE;
    private TextView DATA_NOTE;
    private SQLiteDatabase DataDB;
    private String DataNoteFocus;
    private TextView INOUT_MOUNT;
    private TextView INVOICE_NO;
    private Spinner OBJECT_ITEM;
    private TextView PAY_COLL_NAME;
    private Spinner PROJECT_ID;
    private Spinner PROJECT_NAME;
    private Spinner SOURCE_ITEM;
    private Spinner SUB_OBJECT_ITEM;
    private Button SaveExit;
    private TableRow ShowInvoiceFrame;
    private LinearLayout ShowPayCollFrame;
    private TableRow ShowProjectFrame;
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
    private String MountFormat = "";
    private String ExchangeFormat = "";
    private String AccountName = "";
    private String SQL = "";
    private String Item_Note = "";
    private String Project = "";
    private String FocusFeild = "";
    private String InputDetail = "";
    private String ShowMount = "";
    private String ShowPayColl = "0";
    private String ShowInvoice = "0";
    private String ShowProject = "0";
    private String ShowAssMount = "0";
    private String ShowVibrate = "0";
    private String ReturnHome = "0";
    View.OnFocusChangeListener datefocusListener = new View.OnFocusChangeListener() { // from class: mymoney.zero.Addout.1
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Addout.this.ShowDateSelect();
            }
        }
    };
    View.OnTouchListener dateTouchListener = new View.OnTouchListener() { // from class: mymoney.zero.Addout.2
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 0) {
                Addout.this.ShowDateSelect();
                return false;
            }
            return false;
        }
    };
    View.OnFocusChangeListener InvoicefocusListener = new View.OnFocusChangeListener() { // from class: mymoney.zero.Addout.3
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Addout.this.DATA_NOTE.setInputType(1);
                Addout.this.FocusFeild = "INVOICE";
                if (hasFocus) {
                    Intent intent = new Intent();
                    intent.setClass(Addout.this, Calc2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("InMount", Addout.this.INVOICE_NO.getText().toString());
                    intent.putExtras(bundle);
                    Addout.this.startActivityForResult(intent, 0);
                }
            }
        }
    };
    View.OnFocusChangeListener CollNamefocusListener = new View.OnFocusChangeListener() { // from class: mymoney.zero.Addout.4
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Addout.this.FocusFeild = "PAYCOLLNAME";
                Addout.this.PAY_COLL_NAME.setInputType(1);
                Addout.this.DATA_NOTE.setInputType(1);
            }
        }
    };
    View.OnFocusChangeListener DataNotefocusListener = new View.OnFocusChangeListener() { // from class: mymoney.zero.Addout.5
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            Addout.this.VOICE_INPUT.setVisibility(View.INVISIBLE);
            if (hasFocus) {
                Addout.this.FocusFeild = "DATANOTE";
                Addout.this.VOICE_INPUT.setVisibility(View.VISIBLE);
            }
        }
    };
    View.OnFocusChangeListener MountfocusListener = new View.OnFocusChangeListener() { // from class: mymoney.zero.Addout.6
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            String INOUT_MOUNT_TEMP = Addout.this.INOUT_MOUNT.getText().toString().replace(",", "");
            Addout.this.INOUT_MOUNT.setText(INOUT_MOUNT_TEMP);
            Addout.this.DATA_NOTE.setInputType(1);
            if (Addout.this.INOUT_MOUNT.getText().toString().trim().isEmpty()) {
                Addout.this.INOUT_MOUNT.setText("0");
            }
            try {
                DecimalFormat df2 = new DecimalFormat("#,##0" + Addout.this.MountFormat);
                Addout.this.INOUT_MOUNT.setText(df2.format(Double.valueOf(Addout.this.INOUT_MOUNT.getText().toString())));
            } catch (Exception e) {
            }
            if (hasFocus) {
                Addout.this.FocusFeild = "MOUNT";
                Intent intent = new Intent();
                intent.setClass(Addout.this, Calc.class);
                Bundle bundle = new Bundle();
                bundle.putString("InMount", INOUT_MOUNT_TEMP);
                intent.putExtras(bundle);
                Addout.this.startActivityForResult(intent, 0);
            }
        }
    };
    private DatePickerDialog.OnDateSetListener myOnDateSetListener = new DatePickerDialog.OnDateSetListener() { // from class: mymoney.zero.Addout.7
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
            Addout.this.DATA_DATE.setText(String.valueOf(NY) + " 年 " + NM + " 月 " + ND + " 日");
            Addout.this.WeekView = (TextView) Addout.this.findViewById(R.id.WeekView);
            GetNowDate c = new GetNowDate();
            String SaveDate = c.CDateToEDate(Addout.this.DATA_DATE.getText().toString());
            String[] WeekData = {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
            double y = Double.valueOf(SaveDate.substring(0, 4)).doubleValue();
            double m = Double.valueOf(SaveDate.substring(5, 7)).doubleValue() - 1.0d;
            double d = Double.valueOf(SaveDate.substring(8, 10)).doubleValue();
            Calendar cc = Calendar.getInstance();
            cc.set((int) y, (int) m, (int) d);
            String Week = WeekData[cc.get(7)];
            Addout.this.WeekView.setText(Week);
            Addout.this.DATA_NOTE.setFocusable(true);
            Addout.this.DATA_NOTE.requestFocus();
            Addout.this.DATA_NOTE.setFocusableInTouchMode(true);
        }
    };

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setTitle(String.valueOf(this.AccountName.trim()) + " - 新增支出記錄");
        if (this.FocusFeild.equals("MOUNT")) {
            String Mount = null;
            try {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Mount = bundle.getString("Mount");
                }
                this.INOUT_MOUNT.setText(Mount);
            } catch (Exception e) {
            }
            try {
                if (Mount.length() == 0) {
                    this.INOUT_MOUNT.setText("0");
                }
            } catch (Exception e2) {
            }
            try {
                DecimalFormat df2 = new DecimalFormat("#,##0" + this.MountFormat);
                this.INOUT_MOUNT.setText(df2.format(Double.valueOf(this.INOUT_MOUNT.getText().toString())));
            } catch (Exception e3) {
            }
        }
        if (this.FocusFeild.equals("INVOICE")) {
            String Mount2 = null;
            try {
                Bundle bundle2 = data.getExtras();
                if (bundle2 != null) {
                    Mount2 = bundle2.getString("Mount");
                }
                this.INVOICE_NO.setText(Mount2);
            } catch (Exception e4) {
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
                DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.8
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        Addout.this.DATA_NOTE.setText(String.valueOf(Addout.this.DATA_NOTE.getText().toString().trim()) + OftenList[which]);
                    }
                };
                DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.9
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
        setContentView(R.layout.addout);
        double ItemMount = 0.0d;
        this.DataNoteFocus = new StringBuilder(String.valueOf(this.DataNoteFocus)).toString();
        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
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
        if (this.AccountId == 0.0d) {
            ShowBox("訊息", "您尚未選擇欲作業的帳本");
            return;
        }
        setTitle(String.valueOf(this.AccountName.trim()) + " - 新增支出記錄");
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
        this.ShowPayCollFrame = (LinearLayout) findViewById(R.id.ShowPayCollFrame);
        this.ShowInvoiceFrame = (TableRow) findViewById(R.id.ShowInvoiceFrame);
        this.ShowProjectFrame = (TableRow) findViewById(R.id.ShowProjectFrame);
        this.ShowPayColl = new StringBuilder(String.valueOf(this.ShowPayColl)).toString();
        this.ShowInvoice = new StringBuilder(String.valueOf(this.ShowInvoice)).toString();
        this.ShowProject = new StringBuilder(String.valueOf(this.ShowProject)).toString();
        this.ShowAssMount = new StringBuilder(String.valueOf(this.ShowAssMount)).toString();
        this.ShowVibrate = new StringBuilder(String.valueOf(this.ShowVibrate)).toString();
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示收付人欄位'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            if (this.cursor.getString(0).trim().equals("1")) {
                this.ShowPayColl = "1";
                this.ShowPayCollFrame.setVisibility(0);
            } else {
                this.ShowPayColl = "0";
                this.ShowPayCollFrame.setVisibility(8);
            }
        }
        this.cursor.close();
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示發票號碼欄位'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            if (this.cursor.getString(0).trim().equals("1")) {
                this.ShowInvoice = "1";
                this.ShowInvoiceFrame.setVisibility(0);
            } else {
                this.ShowInvoice = "0";
                this.ShowInvoiceFrame.setVisibility(8);
            }
        }
        this.cursor.close();
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示專案名稱欄位'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            if (this.cursor.getString(0).trim().equals("1")) {
                this.ShowProject = "1";
                this.ShowProjectFrame.setVisibility(0);
            } else {
                this.ShowProject = "0";
                this.ShowProjectFrame.setVisibility(8);
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
        this.INVOICE_NO = (TextView) findViewById(R.id.INVOICE_NO);
        this.PAY_COLL_NAME = (TextView) findViewById(R.id.PAY_COLL_NAME);
        this.PROJECT_NAME = (Spinner) findViewById(R.id.PROJECT_NAME);
        this.DATA_NOTE = (TextView) findViewById(R.id.DATA_NOTE);
        this.OBJECT_ITEM = (Spinner) findViewById(R.id.OBJECT_ITEM);
        this.SUB_OBJECT_ITEM = (Spinner) findViewById(R.id.SUB_OBJECT_ITEM);
        this.VOICE_INPUT = (Button) findViewById(R.id.VOICE_INPUT);
        this.DATA_DATE.setInputType(0);
        this.INOUT_MOUNT.setInputType(0);
        this.INVOICE_NO.setInputType(0);
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
            this.cursor2 = this.DataDB.rawQuery("SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + this.cursor.getString(0) + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO", null);
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
        this.VSOURCE_ITEM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: mymoney.zero.Addout.10
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Addout.this.SOURCE_ITEM.setSelection(arg2);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        ArrayList<String> allCountries2 = new ArrayList<>();
        this.cursor = this.DataDB.rawQuery("SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '支出' OR ITEM_CLASS = '業外支出') AND ITEM_NOTE = PARENT_NOTE AND HIDESTYLE <> '1' ORDER BY ITEM_CLASS,MAKE_NO", null);
        while (this.cursor.moveToNext()) {
            this.Item_Note = this.cursor.getString(0);
            allCountries2.add(this.Item_Note);
        }
        this.cursor.close();
        this.cursor = this.DataDB.rawQuery("SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '其它') AND ITEM_NOTE = PARENT_NOTE ORDER BY ITEM_CLASS,MAKE_NO", null);
        while (this.cursor.moveToNext()) {
            this.Item_Note = this.cursor.getString(0);
            allCountries2.add(this.Item_Note);
        }
        this.cursor.close();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCountries2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mySpinner = (Spinner) findViewById(R.id.OBJECT_ITEM);
        this.mySpinner.setAdapter((SpinnerAdapter) adapter2);
        ArrayList<String> Project_Id_List = new ArrayList<>();
        ArrayList<String> Project_Name_List = new ArrayList<>();
        Project_Id_List.add("");
        Project_Name_List.add("無指定的專案");
        this.cursor = this.DataDB.rawQuery("SELECT PROJECT_ID,PROJECT_NOTE FROM PROJECT_SET WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " ORDER BY PROJECT_ID", null);
        while (this.cursor.moveToNext()) {
            this.Project = this.cursor.getString(0);
            Project_Id_List.add(this.Project);
            this.Project = this.cursor.getString(1);
            Project_Name_List.add(this.Project);
        }
        this.cursor.close();
        ArrayAdapter<String> projectadapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Project_Id_List);
        projectadapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mySpinner = (Spinner) findViewById(R.id.PROJECT_ID);
        this.mySpinner.setAdapter((SpinnerAdapter) projectadapter1);
        ArrayAdapter<String> projectadapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Project_Name_List);
        projectadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mySpinner = (Spinner) findViewById(R.id.PROJECT_NAME);
        this.mySpinner.setAdapter((SpinnerAdapter) projectadapter2);
        this.DataDB.close();
        this.DATA_DATE.setOnTouchListener(this.dateTouchListener);
        this.INOUT_MOUNT.setOnFocusChangeListener(this.MountfocusListener);
        this.INVOICE_NO.setOnFocusChangeListener(this.InvoicefocusListener);
        this.PAY_COLL_NAME.setOnFocusChangeListener(this.CollNamefocusListener);
        this.DATA_NOTE.setOnFocusChangeListener(this.DataNotefocusListener);
        this.OBJECT_ITEM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: mymoney.zero.Addout.11
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Addout.this.ReLoadSubItem();
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        EditText InputDate = (EditText) findViewById(R.id.DATA_DATE);
        InputDate.setKeyListener(new NumberKeyListener() { // from class: mymoney.zero.Addout.12
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

    public void ReLoadSubItem() {
        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        double ItemCount = 0.0d + 0.0d;
        this.cursor = this.DataDB.rawQuery("SELECT COUNT(*) AS ACOUNT FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '支出' OR ITEM_CLASS = '業外支出') AND PARENT_NOTE = '" + this.OBJECT_ITEM.getSelectedItem().toString() + "' AND ITEM_NOTE <> PARENT_NOTE AND HIDESTYLE <> '1' ORDER BY MAKE_NO", null);
        if (this.cursor.moveToNext()) {
            ItemCount = this.cursor.getDouble(0);
        }
        ArrayList<String> allCountries = new ArrayList<>();
        this.cursor = this.DataDB.rawQuery("SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '支出' OR ITEM_CLASS = '業外支出') AND PARENT_NOTE = '" + this.OBJECT_ITEM.getSelectedItem().toString() + "' AND ITEM_NOTE <> PARENT_NOTE AND HIDESTYLE <> '1' ORDER BY ITEM_CLASS,MAKE_NO", null);
        while (this.cursor.moveToNext()) {
            this.Item_Note = this.cursor.getString(0);
            allCountries.add(this.Item_Note);
        }
        this.cursor.close();
        if (ItemCount == 0.0d) {
            allCountries.add("無");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCountries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.SUB_OBJECT_ITEM.setAdapter((SpinnerAdapter) adapter);
        this.DataDB.close();
    }

    public void SaveDataNext(View CvClick) throws IOException, SQLException {
        boolean SubItem;
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        String SOURCE_ITEM_NOTE = this.SOURCE_ITEM.getSelectedItem().toString();
        String OBJECT_ITEM_NOTE = this.OBJECT_ITEM.getSelectedItem().toString();
        if (SOURCE_ITEM_NOTE.trim().equals(OBJECT_ITEM_NOTE.trim())) {
            Toast.makeText(this, "提示：來源 與 目的 帳戶不可相同喔!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e2) {
        }
        this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + SOURCE_ITEM_NOTE.trim() + "'";
        this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor2.moveToNext()) {
            SubItem = true;
        } else {
            SubItem = false;
        }
        this.cursor2.close();
        if (SubItem) {
            this.DataDB.close();
            Toast.makeText(this, "提示：[" + SOURCE_ITEM_NOTE.trim() + "] 底下尚有設定子項目，您不可使用母項目作為來源項目!!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.DataDB.close();
        SaveData();
        this.INOUT_MOUNT.setText("0");
        this.DATA_NOTE.setText("");
        this.INVOICE_NO.setText("");
        this.PAY_COLL_NAME.setText("");
        this.PROJECT_NAME.setSelection(0);
        this.DATA_NOTE.setFocusable(true);
        this.DATA_NOTE.requestFocus();
        this.DATA_NOTE.setFocusableInTouchMode(true);
        this.InputDetail = "";
        if (this.ShowAssMount.equals("1")) {
            String SOURCE_ITEM_NOTE2 = this.SOURCE_ITEM.getSelectedItem().toString();
            RefAssMount();
            for (int i = 0; i <= this.SOURCE_ITEM.getCount(); i++) {
                this.SOURCE_ITEM.setSelection(i, true);
                this.VSOURCE_ITEM.setSelection(i, true);
                if (this.SOURCE_ITEM.getSelectedItem().toString().trim().equals(SOURCE_ITEM_NOTE2)) {
                    return;
                }
            }
        }
    }

    public void SaveDataExit(View CvClick) throws IOException, SQLException {
        boolean SubItem;
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        String SOURCE_ITEM_NOTE = this.SOURCE_ITEM.getSelectedItem().toString();
        String OBJECT_ITEM_NOTE = this.OBJECT_ITEM.getSelectedItem().toString();
        if (SOURCE_ITEM_NOTE.trim().equals(OBJECT_ITEM_NOTE.trim())) {
            Toast.makeText(this, "提示：來源 與 目的 帳戶不可相同喔!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e2) {
        }
        this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + SOURCE_ITEM_NOTE.trim() + "'";
        this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor2.moveToNext()) {
            SubItem = true;
        } else {
            SubItem = false;
        }
        this.cursor2.close();
        if (SubItem) {
            this.DataDB.close();
            Toast.makeText(this, "提示：[" + SOURCE_ITEM_NOTE.trim() + "] 底下尚有設定子項目，您不可使用母項目作為來源項目!!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.DataDB.close();
        SaveData();
        Intent intent = new Intent();
        if (this.ReturnHome.equals("1")) {
            intent.setClass(this, MyMoneyZeroActivity.class);
        } else {
            String tSDCardPath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
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

    public void SaveData() throws IOException, SQLException {
        String SAVE_OBJECT_ITEM_NOTE;
        double Make_No;
        double SOURCE_ITEM_EXCHANGE = 1.0d;
        double OBJECT_ITEM_EXCHANGE = 1.0d;
        try {
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
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
        this.SUB_OBJECT_ITEM = (Spinner) findViewById(R.id.SUB_OBJECT_ITEM);
        this.INOUT_MOUNT = (TextView) findViewById(R.id.INOUT_MOUNT);
        this.DATA_NOTE = (TextView) findViewById(R.id.DATA_NOTE);
        this.INVOICE_NO = (TextView) findViewById(R.id.INVOICE_NO);
        this.PAY_COLL_NAME = (TextView) findViewById(R.id.PAY_COLL_NAME);
        this.PROJECT_ID = (Spinner) findViewById(R.id.PROJECT_ID);
        this.PROJECT_NAME = (Spinner) findViewById(R.id.PROJECT_NAME);
        if (this.INOUT_MOUNT.getText().toString().trim().length() == 0) {
            this.INOUT_MOUNT.setText("0");
        }
        GetNowDate c = new GetNowDate();
        String SaveDate = c.CDateToEDate(this.DATA_DATE.getText().toString());
        if (this.DATA_DATE.length() != 16) {
            ShowBox("日期錯誤", "您所輸入的日期 " + this.DATA_DATE.getText().toString() + "\n不是正確的期格式");
            return;
        }
        if (!IsDate(SaveDate)) {
            ShowBox("日期錯誤", "您所輸入的日期\n" + this.DATA_DATE.getText().toString() + "\n不是正確的期格式");
            return;
        }
        this.PROJECT_NAME = (Spinner) findViewById(R.id.PROJECT_NAME);
        int SelectedItemPosition = this.PROJECT_NAME.getSelectedItemPosition();
        this.PROJECT_ID = (Spinner) findViewById(R.id.PROJECT_ID);
        this.PROJECT_ID.setSelection(SelectedItemPosition);
        String SOURCE_ITEM_NOTE = this.SOURCE_ITEM.getSelectedItem().toString();
        String OBJECT_ITEM_NOTE = this.OBJECT_ITEM.getSelectedItem().toString();
        String SUB_OBJECT_ITEM_NOTE = this.SUB_OBJECT_ITEM.getSelectedItem().toString();
        if (SUB_OBJECT_ITEM_NOTE.equals("無")) {
            SAVE_OBJECT_ITEM_NOTE = OBJECT_ITEM_NOTE.trim().toString();
        } else {
            SAVE_OBJECT_ITEM_NOTE = SUB_OBJECT_ITEM_NOTE.trim().toString();
        }
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
        this.SQL = "SELECT ITEM_CLASS,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + SAVE_OBJECT_ITEM_NOTE + "'";
        Cursor cursor3 = this.DataDB.rawQuery(this.SQL, null);
        if (cursor3.moveToNext()) {
            OBJECT_ITEM_CLASS = cursor3.getString(0);
            OBJECT_ITEM_EXCHANGE = cursor3.getDouble(1);
        }
        cursor3.close();
        double OBJECT_SaveMount = SaveMount / (OBJECT_ITEM_EXCHANGE / SOURCE_ITEM_EXCHANGE);
        GetNowDate GetNow = new GetNowDate();
        String DATA_KEY = GetNow.GetDataKey();
        this.SQL = "INSERT INTO MYMONEY_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_CLASS,DATA_DATE,ITEM_NOTE,IN_MOUNT,OUT_MOUNT,DATA_NOTE,INVOICE_NO,DATA_NO,PAY_COLL_NAME,PROJECT_ID,EXCHANGE,LINK_PC,EXPORT,DATA_NOTE2,DATA_KEY) VALUES ('admin'," + String.valueOf(this.AccountId) + "," + String.valueOf(Make_No) + ",'" + SOURCE_ITEM_CLASS + "','" + SaveDate.trim() + "','" + SOURCE_ITEM_NOTE + "',0,0" + String.valueOf(SaveMount) + ",'" + this.DATA_NOTE.getText().toString().trim().replaceAll("'", "''") + "','" + this.INVOICE_NO.getText().toString().trim() + "','1','" + this.PAY_COLL_NAME.getText().toString().trim().replaceAll("'", "''") + "','" + this.PROJECT_ID.getSelectedItem().toString().trim() + "'," + String.valueOf(SOURCE_ITEM_EXCHANGE) + ",'','0','" + this.InputDetail.replaceAll("'", "''") + "','" + DATA_KEY + "')";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "INSERT INTO MYMONEY_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_CLASS,DATA_DATE,ITEM_NOTE,IN_MOUNT,OUT_MOUNT,DATA_NOTE,INVOICE_NO,DATA_NO,PAY_COLL_NAME,PROJECT_ID,EXCHANGE,LINK_PC,EXPORT,DATA_NOTE2,DATA_KEY) VALUES ('admin'," + String.valueOf(this.AccountId) + "," + String.valueOf(Make_No) + ",'" + OBJECT_ITEM_CLASS + "','" + SaveDate.trim() + "','" + SAVE_OBJECT_ITEM_NOTE + "',0" + String.valueOf(OBJECT_SaveMount) + ",0,'" + this.DATA_NOTE.getText().toString().trim().replaceAll("'", "''") + "','" + this.INVOICE_NO.getText().toString().trim() + "','2','" + this.PAY_COLL_NAME.getText().toString().trim().replaceAll("'", "''") + "','" + this.PROJECT_ID.getSelectedItem().toString().trim() + "'," + String.valueOf(OBJECT_ITEM_EXCHANGE) + ",'','0','" + this.InputDetail.replaceAll("'", "''") + "','" + DATA_KEY + "')";
        this.DataDB.execSQL(this.SQL);
        this.DataDB.close();
        Toast.makeText(this, "新增帳務記錄資料完成!", Toast.LENGTH_SHORT).show();
        BackDataToStorage();
        try {
            Intent i = new Intent(getBaseContext(), (Class<?>) MyWidget.class);
            i.setAction("com.android.mymoneyzero");
            sendBroadcast(i);
        } catch (Exception e3) {
        }
    }

    private void BackDataToStorage() throws IOException {
        new GetNowDate();
        File dir = getFilesDir();
        String DataFileDir = dir.getPath();
        try {
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
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

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void ShowBox(String TitleString, String BodyString) {
        if (TitleString == "") {
            TitleString = "訊息";
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle(TitleString);
        MyAlertDialog.setMessage(BodyString);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.13
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
        if (!this.INOUT_MOUNT.getText().toString().trim().equals("0")) {
            AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
            MyAlertDialog.setTitle("確認");
            MyAlertDialog.setMessage("您目前的資料尚未存入,請問您是否要離開此畫面呢?");
            DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.14
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(Addout.this, MyMoneyZeroActivity.class);
                    Addout.this.startActivity(intent);
                    Addout.this.finish();
                }
            };
            DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.15
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
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("詳細備註輸入");
        alert.setMessage("請輸入詳細備註");
        final EditText input = new EditText(this);
        alert.setView(input);
        input.setText(this.InputDetail);
        input.setHeight(DropboxServerException._200_OK);
        input.setGravity(48);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.16
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Addout.this.InputDetail = input.getText().toString();
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.17
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        alert.setPositiveButton("確定", OkClick);
        alert.setNegativeButton("取消", CanCelClick);
        alert.show();
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
        String OBJECT_ITEM_NOTE;
        try {
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.OBJECT_ITEM = (Spinner) findViewById(R.id.OBJECT_ITEM);
        if (this.SUB_OBJECT_ITEM.getSelectedItem().equals("無")) {
            OBJECT_ITEM_NOTE = this.OBJECT_ITEM.getSelectedItem().toString();
        } else {
            OBJECT_ITEM_NOTE = this.SUB_OBJECT_ITEM.getSelectedItem().toString();
        }
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
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.18
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (OftenList[which].toString().trim() != "新增摘要") {
                    Addout.this.DATA_NOTE.setText(OftenList[which]);
                } else {
                    Addout.this.ShowAddOfftenNote();
                }
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.19
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
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.20
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) throws SQLException {
                String SAVE_OBJECT_ITEM_NOTE;
                double MAKE_NO;
                String InputNote = input.getText().toString();
                String OBJECT_ITEM_CLASS = "";
                if (InputNote.toString().trim() != "") {
                    try {
                        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        Addout.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                    } catch (Exception e) {
                    }
                    String OBJECT_ITEM_NOTE = Addout.this.OBJECT_ITEM.getSelectedItem().toString();
                    String SUB_OBJECT_ITEM_NOTE = Addout.this.SUB_OBJECT_ITEM.getSelectedItem().toString();
                    if (SUB_OBJECT_ITEM_NOTE.equals("無")) {
                        SAVE_OBJECT_ITEM_NOTE = OBJECT_ITEM_NOTE.trim().toString();
                    } else {
                        SAVE_OBJECT_ITEM_NOTE = SUB_OBJECT_ITEM_NOTE.trim().toString();
                    }
                    Addout.this.SQL = "SELECT ITEM_CLASS,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(Addout.this.AccountId) + " AND ITEM_NOTE = '" + SAVE_OBJECT_ITEM_NOTE + "'";
                    Addout.this.cursor = Addout.this.DataDB.rawQuery(Addout.this.SQL, null);
                    if (Addout.this.cursor.moveToNext()) {
                        OBJECT_ITEM_CLASS = Addout.this.cursor.getString(0);
                    }
                    Addout.this.cursor.close();
                    Addout.this.SQL = "SELECT MAKE_NO FROM OFTEN_NOTE WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(Addout.this.AccountId) + " AND ITEM_CLASS = '" + OBJECT_ITEM_CLASS + "' ORDER BY MAKE_NO DESC";
                    Addout.this.cursor = Addout.this.DataDB.rawQuery(Addout.this.SQL, null);
                    if (Addout.this.cursor.moveToNext()) {
                        MAKE_NO = Addout.this.cursor.getDouble(0) + 1.0d;
                    } else {
                        MAKE_NO = 1.0d;
                    }
                    Addout.this.cursor.close();
                    Addout.this.SQL = "INSERT INTO OFTEN_NOTE (USER_ID,ACCOUNT_ID,ITEM_CLASS,ITEM_NOTE,MAKE_NO,DATA_NOTE) VALUES ('admin'," + String.valueOf(Addout.this.AccountId) + ",'" + OBJECT_ITEM_CLASS + "','" + SAVE_OBJECT_ITEM_NOTE.trim() + "'," + String.valueOf(MAKE_NO) + ",'" + InputNote.replace("'", "") + "')";
                    Addout.this.DataDB.execSQL(Addout.this.SQL);
                    Addout.this.DataDB.close();
                    Addout.this.DATA_NOTE.setText(InputNote.toString().trim());
                }
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.21
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
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
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
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.22
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (OftenList[which].toString().trim() != "新增收付人") {
                    Addout.this.PAY_COLL_NAME.setText(OftenList[which]);
                } else {
                    Addout.this.ShowAddPayCollName();
                }
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.23
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
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.24
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) throws SQLException {
                double MAKE_NO;
                String InputNote = input.getText().toString();
                if (InputNote.toString().trim() != "") {
                    try {
                        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        Addout.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                    } catch (Exception e) {
                    }
                    Addout.this.SQL = "SELECT MAKE_NO FROM PAY_COLL_NAME WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(Addout.this.AccountId) + " ORDER BY MAKE_NO DESC";
                    Addout.this.cursor = Addout.this.DataDB.rawQuery(Addout.this.SQL, null);
                    if (Addout.this.cursor.moveToNext()) {
                        MAKE_NO = Addout.this.cursor.getDouble(0) + 1.0d;
                    } else {
                        MAKE_NO = 1.0d;
                    }
                    Addout.this.cursor.close();
                    Addout.this.SQL = "INSERT INTO PAY_COLL_NAME (USER_ID,ACCOUNT_ID,MAKE_NO,COLL_NAME) VALUES ('admin'," + String.valueOf(Addout.this.AccountId) + "," + String.valueOf(MAKE_NO) + ",'" + InputNote.replace("'", "") + "')";
                    Addout.this.DataDB.execSQL(Addout.this.SQL);
                    Addout.this.DataDB.close();
                    Addout.this.PAY_COLL_NAME.setText(InputNote.toString().trim());
                }
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.25
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        alert.setPositiveButton("確定", OkClick);
        alert.setNegativeButton("取消", CanCelClick);
        alert.show();
    }

    public void SelectDate() {
        GetNowDate c = new GetNowDate();
        double y = c.GetCDateYear(this.DATA_DATE.getText().toString()).doubleValue();
        double m = c.GetCDateMonth(this.DATA_DATE.getText().toString()).doubleValue() - 1.0d;
        double d = c.GetCDateDay(this.DATA_DATE.getText().toString()).doubleValue();
        DatePickerDialog dpd = new DatePickerDialog(this, 1, this.myOnDateSetListener, (int) y, (int) m, (int) d);
        dpd.show();
    }

    public void RefAssMount() {
        double ItemMount = 0.0d;
        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        ArrayList<String> allCountries = new ArrayList<>();
        ArrayList<String> allCountriesv = new ArrayList<>();
        this.cursor = this.DataDB.rawQuery("SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE = PARENT_NOTE AND HIDESTYLE <> '1' ORDER BY ITEM_CLASS DESC,MAKE_NO", null);
        while (this.cursor.moveToNext()) {
            this.Item_Note = this.cursor.getString(0);
            if (this.ShowAssMount.equals("1")) {
                ItemMount = GetItemMount(this.cursor.getString(1), this.Item_Note);
            }
            allCountries.add(this.Item_Note);
            DecimalFormat df2 = new DecimalFormat("#,##0" + this.MountFormat);
            this.ShowMount = df2.format(ItemMount);
            if (this.ShowAssMount.equals("1")) {
                allCountriesv.add(String.valueOf(this.Item_Note) + "  $" + this.ShowMount);
            } else {
                allCountriesv.add(this.Item_Note);
            }
            this.cursor2 = this.DataDB.rawQuery("SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_CLASS = '資產' OR ITEM_CLASS = '負債') AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + this.cursor.getString(0) + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO", null);
            while (this.cursor2.moveToNext()) {
                this.Item_Note = this.cursor2.getString(0);
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
        this.VSOURCE_ITEM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: mymoney.zero.Addout.26
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Addout.this.SOURCE_ITEM.setSelection(arg2);
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
        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
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

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 3:
                return true;
            case 4:
                this.INOUT_MOUNT = (TextView) findViewById(R.id.INOUT_MOUNT);
                if (!this.INOUT_MOUNT.getText().toString().trim().equals("0")) {
                    AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
                    MyAlertDialog.setTitle("確認");
                    MyAlertDialog.setMessage("您目前的資料尚未存入,請問您是否要離開此畫面呢?");
                    DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.27
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(Addout.this, MyMoneyZeroActivity.class);
                            Addout.this.startActivity(intent);
                            Addout.this.finish();
                        }
                    };
                    DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Addout.28
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