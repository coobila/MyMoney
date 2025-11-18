package com.coobila.mymoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/* loaded from: classes.dex */
public class AssetDetailShow extends Activity {
    private double AccountId;
    private SQLiteDatabase DataDB;
    private ListView DataList;
    private TextView EndDate;
    private String GetItemNote;
    private String ItemClass;
    private TextView ItemNoteShow;
    private TextView MakeNoRec;
    private Spinner MakeNoSpinner;
    private double SHOW_MAKE_NO;
    private String SQL;
    private TextView StartDate;
    private Cursor cursor;
    private String AccountName = "";
    private String MountFormat = "";
    private int MountSub = 0;
    private int Loop_I = 0;
    private double MAKE_NO = 0.0d;
    private DatePickerDialog.OnDateSetListener myOnDateSetListener = new DatePickerDialog.OnDateSetListener() { // from class: mymoney.zero.AssetDetailShow.1
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
            AssetDetailShow.this.StartDate.setText(String.valueOf(NY) + "/" + NM + "/" + ND);
            AssetDetailShow.this.ShowData();
        }
    };

    @Override // android.app.Activity, android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("功能");
        menu.add(0, 0, 0, "修改此筆帳務資料");
        menu.add(0, 1, 0, "刪除此筆帳錄資料");
        menu.add(0, 2, 0, "顯示詳細備註資料");
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = new Intent();
        intent.setClass(this, MyMoneyZeroActivity.class);
        startActivity(intent);
        finish();
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assetdetailshow);
        this.StartDate = (TextView) findViewById(R.id.StartDate);
        this.EndDate = (TextView) findViewById(R.id.EndDate);
        this.ItemNoteShow = (TextView) findViewById(R.id.ItemNoteShow);
        Bundle bundle = getIntent().getExtras();
        this.GetItemNote = bundle.getString("ItemNote");
        String GetEndDate = bundle.getString("EndDate");
        String GetStartDate = bundle.getString("StartDate");
        this.ItemNoteShow.setText(" 項目名稱：" + this.GetItemNote);
        setTitle(String.valueOf(this.GetItemNote) + " 項目異動明細");
        this.StartDate.setText(GetStartDate);
        this.EndDate.setText(GetEndDate);
        this.AccountName = new StringBuilder(String.valueOf(this.AccountName)).toString();
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
            return;
        }
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '金額小數點'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        this.MountSub = 0;
        if (this.cursor.moveToNext()) {
            this.MountSub = (int) this.cursor.getDouble(0);
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
        this.DataDB.close();
        this.SHOW_MAKE_NO = 0.0d;
        this.MAKE_NO = 0.0d;
        try {
            this.MAKE_NO = bundle.getDouble("MAKE_NO");
        } catch (Exception e2) {
        }
        ShowData();
        if (this.MAKE_NO != 0.0d) {
            this.Loop_I = 0;
            while (this.Loop_I <= this.DataList.getCount() - 1) {
                this.MakeNoSpinner.setSelection(this.Loop_I);
                if (this.MakeNoSpinner.getSelectedItem().toString().trim().compareTo(String.valueOf(this.MAKE_NO).trim()) != 0) {
                    this.Loop_I++;
                } else {
                    try {
                        this.DataList.setSelection(this.Loop_I);
                        return;
                    } catch (Exception e3) {
                        return;
                    }
                }
            }
        }
    }

    public void ShowData() {
        double NowMount;
        String SourceItem = "";
        String ObjectItem = "";
        double InMount = 0.0d;
        double OutMount = 0.0d;
        String ShowClass = "";
        String InOutTemp = "";
        double InOutMount = 0.0d;
        double Exchange1 = 1.0d;
        double Exchange2 = 1.0d;
        this.MakeNoSpinner = (Spinner) findViewById(R.id.MakeNoSpinner);
        this.MakeNoRec = (TextView) findViewById(R.id.MakeNoRec);
        ArrayList<String> MakeNoList = new ArrayList<>();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MakeNoList);
        this.MakeNoSpinner.setAdapter((SpinnerAdapter) adapter2);
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.DataList = (ListView) findViewById(R.id.DataList);
        MakeNoList.add("0");
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        DecimalFormat MountDf = new DecimalFormat("#,##0" + this.MountFormat);
        MountDf.format(0.0d);
        this.SQL = "SELECT ITEM_CLASS,EXCHANGE,BEFORE_MOUNT FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + this.GetItemNote + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        this.ItemClass = "";
        double Exchange = 1.0d;
        double BeforeMount = 0.0d;
        if (this.cursor.moveToNext()) {
            this.ItemClass = this.cursor.getString(0);
            Exchange = this.cursor.getDouble(1);
            BeforeMount = this.cursor.getDouble(2) * Exchange;
        }
        this.cursor.close();
        if (this.ItemClass.equals("資產")) {
            this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + this.GetItemNote + "' AND DATA_DATE < '" + ((Object) this.StartDate.getText()) + "'";
        }
        if (this.ItemClass.equals("負債")) {
            this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + this.GetItemNote + "' AND DATA_DATE < '" + ((Object) this.StartDate.getText()) + "'";
        }
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            NowMount = this.cursor.getDouble(0) * Exchange;
        } else {
            NowMount = 0.0d;
        }
        this.cursor.close();
        double NowMount2 = NowMount + BeforeMount;
        HashMap<String, Object> map2 = new HashMap<>();
        String ShowMount = MountDf.format(NowMount2);
        map2.put("DataDate", "");
        map2.put("SourceItem", "至 " + this.StartDate.getText().toString() + " 前");
        map2.put("ShowClass", String.valueOf(this.GetItemNote) + " 項目期初餘額");
        map2.put("ObjectItem", "");
        map2.put("PMount", "$" + ShowMount + " ");
        map2.put("DataNote", "");
        map2.put("AssSum", "點選此處可設定期初餘額開始日期");
        if (this.ItemClass.equals("資產")) {
            map2.put("ItemImage", Integer.valueOf(R.drawable.aa));
        }
        if (this.ItemClass.equals("負債")) {
            map2.put("ItemImage", Integer.valueOf(R.drawable.al));
        }
        listItem.add(map2);
        this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE,EXCHANGE FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE BETWEEN '" + ((Object) this.StartDate.getText()) + "' AND '" + ((Object) this.EndDate.getText()) + "' ORDER BY DATA_DATE,MAKE_NO,DATA_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        String DataClass = "";
        while (this.cursor.moveToNext()) {
            if (this.cursor.getString(0).equals("1")) {
                SourceItem = this.cursor.getString(2).trim();
                Exchange1 = this.cursor.getDouble(8);
            }
            if (this.cursor.getString(0).equals("2")) {
                ObjectItem = this.cursor.getString(2).trim();
                Exchange2 = this.cursor.getDouble(8);
            }
            DataClass = String.valueOf(DataClass) + this.cursor.getString(3).trim();
            double MakeNO = this.cursor.getDouble(1);
            if (this.cursor.getString(3).trim().equals("收入") || this.cursor.getString(3).trim().equals("支出") || this.cursor.getString(3).trim().equals("業外收入") || this.cursor.getString(3).trim().equals("業外支出")) {
                ShowClass = String.valueOf(this.cursor.getString(3)) + "至";
            }
            if (DataClass.indexOf("其它") > 0) {
                ShowClass = "到帳目";
            }
            if (ShowClass.trim().equals("業外收入至")) {
                InOutTemp = "1";
                ShowClass = "收入至";
            }
            if (ShowClass.trim().equals("業外支出至")) {
                InOutTemp = "2";
                ShowClass = "支出至";
            }
            if (this.cursor.getDouble(4) != 0.0d) {
                InOutMount = this.cursor.getDouble(4);
                InMount = this.cursor.getDouble(4);
            }
            if (this.cursor.getDouble(5) != 0.0d) {
                InOutMount = this.cursor.getDouble(5);
                OutMount = this.cursor.getDouble(5);
            }
            if (InMount != 0.0d && OutMount != 0.0d) {
                if (SourceItem.trim().equals(this.GetItemNote.trim())) {
                    InOutMount = OutMount;
                }
                if (ObjectItem.trim().equals(this.GetItemNote.trim())) {
                    InOutMount = InMount;
                }
            }
            if (this.cursor.getString(0).equals("2")) {
                if (DataClass.trim().equals("資產資產")) {
                    ShowClass = "轉帳至";
                }
                if (DataClass.trim().equals("負債負債")) {
                    ShowClass = "轉帳至";
                }
                if (DataClass.trim().equals("資產負債")) {
                    ShowClass = "支付至";
                }
                if (DataClass.trim().equals("負債資產")) {
                    ShowClass = "轉帳至";
                }
                if (DataClass.trim().equals("資產其它")) {
                    ShowClass = "支出至";
                }
                if (DataClass.trim().equals("負債其它")) {
                    ShowClass = "支出至";
                }
                if (DataClass.trim().equals("其它資產")) {
                    ShowClass = "收入至";
                }
                if (DataClass.trim().equals("其它負債")) {
                    ShowClass = "收入至";
                }
                String[] WeekData = {"", "(日)", "(一)", "(二)", "(三)", "(四)", "(五)", "(六)"};
                double y = Double.valueOf(this.cursor.getString(6).toString().substring(0, 4)).doubleValue();
                double m = Double.valueOf(this.cursor.getString(6).toString().substring(5, 7)).doubleValue() - 1.0d;
                double d = Double.valueOf(this.cursor.getString(6).toString().substring(8, 10)).doubleValue();
                Calendar c = Calendar.getInstance();
                c.set((int) y, (int) m, (int) d);
                String Week = WeekData[c.get(7)];
                ObjectItem = this.cursor.getString(2).trim();
                if (SourceItem.trim().toString().equals(this.GetItemNote.trim()) || ObjectItem.trim().toString().equals(this.GetItemNote.trim())) {
                    HashMap<String, Object> map = new HashMap<>();
                    if (this.ItemClass.equals("資產") && SourceItem.trim().equals(this.GetItemNote.trim())) {
                        if (Exchange == 1.0d) {
                            NowMount2 -= InOutMount;
                        } else if (Exchange1 == Exchange2) {
                            NowMount2 -= InOutMount * Exchange1;
                        } else {
                            NowMount2 -= (InOutMount * Exchange1) * Exchange2;
                        }
                    }
                    if (this.ItemClass.equals("資產") && ObjectItem.trim().equals(this.GetItemNote.trim())) {
                        if (Exchange == 1.0d) {
                            NowMount2 += InOutMount;
                        } else if (Exchange1 == Exchange2) {
                            NowMount2 += InOutMount * Exchange1;
                        } else {
                            NowMount2 += InOutMount * Exchange1 * Exchange2;
                        }
                    }
                    if (this.ItemClass.equals("負債") && SourceItem.trim().equals(this.GetItemNote.trim())) {
                        if (Exchange == 1.0d) {
                            NowMount2 += InOutMount;
                        } else {
                            NowMount2 += InOutMount * Exchange1 * Exchange2;
                        }
                    }
                    if (this.ItemClass.equals("負債") && ObjectItem.trim().equals(this.GetItemNote.trim())) {
                        if (Exchange == 1.0d) {
                            NowMount2 -= InOutMount;
                        } else {
                            NowMount2 -= (InOutMount * Exchange1) * Exchange2;
                        }
                    }
                    MountDf.format(InOutMount);
                    map.put("DataYear", this.cursor.getString(6).substring(0, 4));
                    map.put("DataDate", this.cursor.getString(6).substring(5, 10));
                    map.put("Week", Week);
                    map.put("SourceItem", SourceItem.trim().toString());
                    map.put("ShowClass", ShowClass);
                    map.put("ObjectItem", ObjectItem.trim().toString());
                    String InOutShowMount = MountDf.format(InOutMount);
                    String ShowMount2 = MountDf.format(NowMount2);
                    map.put("DataNote", this.cursor.getString(7));
                    map.put("AssSum", "結餘金額：" + ShowMount2);
                    if (ShowClass.equals("收入至")) {
                        map.put("InMount", "$" + InOutShowMount + " ");
                        map.put("ItemImage", Integer.valueOf(R.drawable.ai));
                    }
                    if (ShowClass.equals("支出至")) {
                        map.put("OutMount", "$" + InOutShowMount + " ");
                        map.put("ItemImage", Integer.valueOf(R.drawable.ae));
                    }
                    if (ShowClass.equals("到帳目")) {
                        map.put("ItemImage", Integer.valueOf(R.drawable.ae));
                        map.put("OutMount", "$" + ShowMount2 + " ");
                    }
                    if (ShowClass.equals("支付至")) {
                        map.put("PMount", "$" + InOutShowMount + " ");
                        map.put("ItemImage", Integer.valueOf(R.drawable.aa));
                    }
                    if (InOutTemp.trim().equals("1")) {
                        map.put("ItemImage", Integer.valueOf(R.drawable.ai1));
                    }
                    if (InOutTemp.trim().equals("2")) {
                        map.put("ItemImage", Integer.valueOf(R.drawable.ae1));
                    }
                    if (ShowClass.equals("轉帳至")) {
                        if (this.ItemClass.equals("資產")) {
                            map.put("TrnMount", "$" + InOutShowMount + " ");
                            map.put("ItemImage", Integer.valueOf(R.drawable.aa));
                        }
                        if (this.ItemClass.equals("負債")) {
                            map.put("TrnMount", "$" + InOutShowMount + " ");
                            map.put("ItemImage", Integer.valueOf(R.drawable.al));
                        }
                    }
                    listItem.add(map);
                    MakeNoList.add(String.valueOf(MakeNO));
                }
                InOutMount = 0.0d;
                InMount = 0.0d;
                OutMount = 0.0d;
                InOutTemp = "";
                DataClass = "";
            }
        }
        this.cursor.close();
        this.DataList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() { // from class: mymoney.zero.AssetDetailShow.2
            @Override // android.view.View.OnCreateContextMenuListener
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("功能");
                menu.add(0, 0, 0, "修改此筆帳務資料");
                menu.add(0, 1, 0, "刪除此筆帳務資料");
                menu.add(0, 2, 0, "顯示詳細備註資料");
            }
        });
        this.DataDB.close();
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.assetdetailshowlistitem, new String[]{"DataDate", "Week", "SourceItem", "DataYear", "ShowClass", "ObjectItem", "OutMount", "InMount", "TrnMount", "PMount", "DataNote", "AssSum", "ItemImage"}, new int[]{R.id.DataDate, R.id.Week, R.id.SourceItem, R.id.DataYear, R.id.ShowClass, R.id.ObjectItem, R.id.OutMount, R.id.InMount, R.id.TrnMount, R.id.PMount, R.id.DataNote, R.id.AssSum, R.id.ItemImage});
        this.DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: mymoney.zero.AssetDetailShow.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AssetDetailShow.this.MakeNoSpinner.setSelection(position);
                AssetDetailShow.this.MakeNoRec.setText(AssetDetailShow.this.MakeNoSpinner.getSelectedItem().toString().trim());
                if (position == 0) {
                    AssetDetailShow.this.SelectDate();
                } else {
                    view.showContextMenu();
                }
            }
        });
        this.DataList.setAdapter((ListAdapter) listItemAdapter);
    }

    @Override // android.app.Activity
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        this.MakeNoSpinner.setSelection(menuInfo.position);
        this.MakeNoRec.setText(this.MakeNoSpinner.getSelectedItem().toString().trim());
        if (item.getItemId() == 0) {
            CEditData();
        }
        if (item.getItemId() == 1) {
            CDeleteData();
        }
        if (item.getItemId() == 2) {
            CShowDetail();
        }
        return super.onContextItemSelected(item);
    }

    public void CShowDetail() {
        this.MAKE_NO = Double.valueOf(this.MakeNoRec.getText().toString()).doubleValue();
        if (this.MAKE_NO == 0.0d) {
            ShowBox("訊息提示", "您尚未選取帳務記錄資料!");
            return;
        }
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "SELECT DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(this.MAKE_NO) + " ORDER BY DATA_DATE,MAKE_NO,DATA_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        String ShowDetail = "";
        while (this.cursor.moveToNext()) {
            if (this.cursor.getString(0) != null) {
                ShowDetail = this.cursor.getString(0).trim();
            }
        }
        this.cursor.close();
        this.DataDB.close();
        if (!ShowDetail.equals("")) {
            ShowBox("詳細備註明細", ShowDetail);
        } else {
            ShowBox("訊息", "此筆帳務資料無記錄詳細備註!");
        }
    }

    public void CEditData() {
        String ShowClass = "";
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        this.MAKE_NO = Double.valueOf(this.MakeNoRec.getText().toString()).doubleValue();
        if (this.MAKE_NO == 0.0d) {
            ShowBox("訊息提示", "您尚未選取帳務記錄資料!");
            return;
        }
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(this.MAKE_NO) + " ORDER BY DATA_DATE,MAKE_NO,DATA_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        String DataClass = "";
        while (this.cursor.moveToNext()) {
            DataClass = String.valueOf(DataClass) + this.cursor.getString(3).trim();
            if (this.cursor.getString(3).trim().equals("收入") || this.cursor.getString(3).trim().equals("支出") || this.cursor.getString(3).trim().equals("業外收入") || this.cursor.getString(3).trim().equals("業外支出")) {
                ShowClass = this.cursor.getString(3);
            }
            if (ShowClass.trim().equals("業外收入至")) {
                ShowClass = "收入至";
            }
            if (ShowClass.trim().equals("業外支出至")) {
                ShowClass = "支出至";
            }
        }
        this.cursor.close();
        this.DataDB.close();
        bundle.putDouble("MAKE_NO", this.MAKE_NO);
        bundle.putString("REPORT_MODE", "REPORT04");
        bundle.putString("ItemNote", this.GetItemNote);
        bundle.putString("StartDate", this.StartDate.getText().toString());
        bundle.putString("EndDate", this.EndDate.getText().toString());
        intent.putExtras(bundle);
        if (DataClass.trim().equals("資產資產")) {
            ShowClass = "轉帳";
        }
        if (DataClass.trim().equals("負債負債")) {
            ShowClass = "轉帳";
        }
        if (DataClass.trim().equals("資產負債")) {
            ShowClass = "轉帳";
        }
        if (DataClass.trim().equals("負債資產")) {
            ShowClass = "轉帳";
        }
        if (DataClass.trim().equals("資產其它")) {
            ShowClass = "支出";
        }
        if (DataClass.trim().equals("負債其它")) {
            ShowClass = "支出";
        }
        if (DataClass.trim().equals("其它資產")) {
            ShowClass = "收入";
        }
        if (DataClass.trim().equals("其它負債")) {
            ShowClass = "收入";
        }
//        if (ShowClass.trim().equals("支出") || ShowClass.trim().equals("業外支出")) {
//            intent.setClass(this, Editout.class);
//            startActivity(intent);
//            finish();
//        }
//        if (ShowClass.trim().equals("收入") || ShowClass.trim().equals("業外收入")) {
//            intent.setClass(this, Editin.class);
//            startActivity(intent);
//            finish();
//        }
//        if (ShowClass.trim().equals("轉帳")) {
//            intent.setClass(this, Edittransfer.class);
//            startActivity(intent);
//            finish();
//        }
        finish();
    }

    public void CDeleteData() {
        String SourceItem = "";
        String ObjectItem = "";
        double InOutMount = 0.0d;
        String ShowClass = new StringBuilder(String.valueOf("")).toString();
        this.MAKE_NO = Double.valueOf(this.MakeNoRec.getText().toString()).doubleValue();
        if (this.MAKE_NO == 0.0d) {
            ShowBox("訊息提示", "您尚未選取帳務記錄資料!");
            return;
        }
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(this.MAKE_NO) + " ORDER BY DATA_DATE,MAKE_NO,DATA_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        String DataClass = "";
        while (this.cursor.moveToNext()) {
            DataClass = String.valueOf(DataClass) + this.cursor.getString(3).trim();
            if (this.cursor.getString(0).equals("1")) {
                SourceItem = this.cursor.getString(2).trim();
            }
            if (this.cursor.getString(0).equals("2")) {
                ObjectItem = this.cursor.getString(2).trim();
            }
            if (this.cursor.getString(3).trim().equals("收入") || this.cursor.getString(3).trim().equals("支出") || this.cursor.getString(3).trim().equals("業外收入") || this.cursor.getString(3).trim().equals("業外支出")) {
                ShowClass = String.valueOf(this.cursor.getString(3)) + "至";
            }
            if (DataClass.indexOf("其它") > 0) {
                ShowClass = "到帳目";
            }
            if (ShowClass.trim().equals("業外收入至")) {
                ShowClass = "收入至";
            }
            if (ShowClass.trim().equals("業外支出至")) {
                ShowClass = "支出至";
            }
            if (DataClass.trim().equals("資產其它")) {
                ShowClass = "支出";
            }
            if (DataClass.trim().equals("負債其它")) {
                ShowClass = "支出";
            }
            if (DataClass.trim().equals("其它資產")) {
                ShowClass = "收入";
            }
            if (DataClass.trim().equals("其它負債")) {
                ShowClass = "收入";
            }
            if (this.cursor.getDouble(4) != 0.0d) {
                InOutMount = this.cursor.getDouble(4);
            }
            if (this.cursor.getDouble(5) != 0.0d) {
                InOutMount = this.cursor.getDouble(5);
            }
        }
        this.cursor.close();
        this.DataDB.close();
        if (DataClass.trim().equals("資產資產")) {
            ShowClass = "轉帳至";
        }
        if (DataClass.trim().equals("負債負債")) {
            ShowClass = "轉帳至";
        }
        if (DataClass.trim().equals("資產負債")) {
            ShowClass = "支付至";
        }
        if (DataClass.trim().equals("負債資產")) {
            ShowClass = "轉帳至";
        }
        if (ShowClass.equals("")) {
            ShowBox("訊息提示", "已無帳務記錄資料可供刪除!");
            return;
        }
        DecimalFormat MountDf = new DecimalFormat("#,##0" + this.MountFormat);
        String ShowMount = MountDf.format(InOutMount);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("帳務記錄刪除");
        alert.setMessage("請問您是否要將\n從帳目【" + SourceItem.trim() + "】\n" + ShowClass.toString() + "【" + ObjectItem + "】\n金額：" + ShowMount.toString() + "\n此筆帳務記錄資料刪除嗎?");
        alert.setPositiveButton("確定刪除", new DialogInterface.OnClickListener() { // from class: mymoney.zero.AssetDetailShow.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) throws SQLException {
                AssetDetailShow.this.KillData();
            }
        });
        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: mymoney.zero.AssetDetailShow.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void KillData() throws SQLException {
        this.MAKE_NO = Double.valueOf(this.MakeNoRec.getText().toString()).doubleValue();
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "DELETE FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(this.MAKE_NO);
        this.DataDB.execSQL(this.SQL);
        this.DataDB.close();
        Toast.makeText(this, "刪除帳務記錄資料完成!", Toast.LENGTH_SHORT).show();
//        try {
//            Intent i = new Intent(getBaseContext(), (Class<?>) MyWidget.class);
//            i.setAction("com.android.mymoneyzero");
//            sendBroadcast(i);
//        } catch (Exception e2) {
//        }
        ShowData();
    }

    public void SelectDate() {
        double y = Double.valueOf(this.StartDate.getText().toString().substring(0, 4)).doubleValue();
        double m = Double.valueOf(this.StartDate.getText().toString().substring(5, 7)).doubleValue() - 1.0d;
        double d = Double.valueOf(this.StartDate.getText().toString().substring(8, 10)).doubleValue();
        DatePickerDialog dpd = new DatePickerDialog(this, this.myOnDateSetListener, (int) y, (int) m, (int) d);
        dpd.show();
    }

    public void ShowBox(String TitleString, String BodyString) {
        if (TitleString == "") {
            TitleString = "訊息";
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle(TitleString);
        MyAlertDialog.setMessage(BodyString);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.AssetDetailShow.6
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
                Bundle bundle = new Bundle();
                bundle.putString("StartDate", this.StartDate.getText().toString());
                bundle.putString("EndDate", this.EndDate.getText().toString());
                intent.putExtras(bundle);
                if (this.ItemClass.trim().equals("資產")) {
                    intent.setClass(this, Report04.class);
                    startActivity(intent);
                    finish();
                }
//                if (this.ItemClass.trim().equals("負債")) {
//                    intent.setClass(this, Report05.class);
//                    startActivity(intent);
//                    finish();
//                    break;
//                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}