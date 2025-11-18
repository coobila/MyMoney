package com.coobila.mymoney;

import android.app.Activity;
import android.app.AlertDialog;
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
public class InOutDetailShow extends Activity {
    private double AccountId;
    private SQLiteDatabase DataDB;
    private ListView DataList;
    private TextView DataMonth;
    private String GetDataMonth;
    private String GetItemNote;
    private String ItemClass;
    private TextView ItemNoteShow;
    private TextView MakeNoRec;
    private Spinner MakeNoSpinner;
    private double SHOW_MAKE_NO;
    private String SQL;
    private Cursor cursor;
    private String AccountName = "";
    private String MountFormat = "";
    private int MountSub = 0;
    private int Loop_I = 0;
    private double MAKE_NO = 0.0d;
    private String ShowClass = "";

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
        setContentView(R.layout.inoutdetailshow);
        this.DataMonth = (TextView) findViewById(R.id.DataMonth);
        this.ItemNoteShow = (TextView) findViewById(R.id.ItemNoteShow);
        Bundle bundle = getIntent().getExtras();
        this.GetItemNote = bundle.getString("ItemNote");
        this.GetDataMonth = bundle.getString("DataMonth");
        this.ItemNoteShow.setText(" 項目名稱：" + this.GetItemNote);
        this.DataMonth.setText(this.GetDataMonth);
        setTitle(String.valueOf(this.GetItemNote) + " 項目異動明細");
        this.AccountName = new StringBuilder(String.valueOf(this.AccountName)).toString();
        try {
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
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
        String SourceItem = "";
        double InOutMount = 0.0d;
        double InAMount = 0.0d;
        double OutAMount = 0.0d;
        String InOutTemp = "";
        this.MakeNoSpinner = (Spinner) findViewById(R.id.MakeNoSpinner);
        this.MakeNoRec = (TextView) findViewById(R.id.MakeNoRec);
        ArrayList<String> MakeNoList = new ArrayList<>();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MakeNoList);
        this.MakeNoSpinner.setAdapter((SpinnerAdapter) adapter2);
        try {
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        String FindData = "";
        this.SQL = "SELECT ITEM_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND (ITEM_NOTE = '" + this.GetItemNote + "' OR PARENT_NOTE = '" + this.GetItemNote + "')";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        while (this.cursor.moveToNext()) {
            this.ItemClass = this.cursor.getString(1);
            FindData = String.valueOf(FindData) + "," + this.cursor.getString(0) + " ";
        }
        this.cursor.close();
        this.DataList = (ListView) findViewById(R.id.DataList);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE LIKE '" + ((Object) this.DataMonth.getText()) + "%' ORDER BY DATA_DATE,MAKE_NO DESC,DATA_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        String DataClass = "";
        while (this.cursor.moveToNext()) {
            if (this.cursor.getString(0).equals("1")) {
                SourceItem = this.cursor.getString(2).trim();
            }
            if (this.cursor.getString(0).equals("2")) {
                this.cursor.getString(2).trim();
            }
            DataClass = String.valueOf(DataClass) + this.cursor.getString(3).trim();
            double MakeNO = this.cursor.getDouble(1);
            if (this.cursor.getString(3).trim().equals("收入") || this.cursor.getString(3).trim().equals("支出") || this.cursor.getString(3).trim().equals("業外收入") || this.cursor.getString(3).trim().equals("業外支出")) {
                this.ShowClass = String.valueOf(this.cursor.getString(3)) + "至";
            }
            if (DataClass.indexOf("其它") > 0) {
                this.ShowClass = "到帳目";
            }
            if (this.ShowClass.trim().equals("業外收入至")) {
                InOutTemp = "1";
                this.ShowClass = "收入至";
            }
            if (this.ShowClass.trim().equals("業外支出至")) {
                InOutTemp = "2";
                this.ShowClass = "支出至";
            }
            if (this.cursor.getDouble(4) != 0.0d) {
                InOutMount = this.cursor.getDouble(4);
            }
            if (this.cursor.getDouble(5) != 0.0d) {
                InOutMount = this.cursor.getDouble(5);
            }
            if (this.cursor.getString(0).equals("2")) {
                if (DataClass.trim().equals("資產資產")) {
                    this.ShowClass = "轉帳至";
                }
                if (DataClass.trim().equals("負債負債")) {
                    this.ShowClass = "轉帳至";
                }
                if (DataClass.trim().equals("資產負債")) {
                    this.ShowClass = "支付至";
                }
                if (DataClass.trim().equals("負債資產")) {
                    this.ShowClass = "轉帳至";
                }
                if (DataClass.trim().equals("資產其它")) {
                    this.ShowClass = "支出至";
                }
                if (DataClass.trim().equals("負債其它")) {
                    this.ShowClass = "支出至";
                }
                if (DataClass.trim().equals("其它資產")) {
                    this.ShowClass = "收入至";
                }
                if (DataClass.trim().equals("其它負債")) {
                    this.ShowClass = "收入至";
                }
                String[] WeekData = {"", "(日)", "(一)", "(二)", "(三)", "(四)", "(五)", "(六)"};
                double y = Double.valueOf(this.cursor.getString(6).toString().substring(0, 4)).doubleValue();
                double m = Double.valueOf(this.cursor.getString(6).toString().substring(5, 7)).doubleValue() - 1.0d;
                double d = Double.valueOf(this.cursor.getString(6).toString().substring(8, 10)).doubleValue();
                Calendar c = Calendar.getInstance();
                c.set((int) y, (int) m, (int) d);
                String Week = WeekData[c.get(7)];
                DecimalFormat MountDf = new DecimalFormat("#,##0" + this.MountFormat);
                String ShowMount = MountDf.format(InOutMount);
                String ObjectItem = this.cursor.getString(2).trim();
                if (this.ShowClass.equals("收入至") || this.ShowClass.equals("支出至")) {
                    if (this.ShowClass.equals("收入至")) {
                        InAMount += InOutMount;
                    }
                    if (this.ShowClass.equals("支出至")) {
                        OutAMount += InOutMount;
                    }
                    String[] DataTemp = FindData.split(",");
                    for (String FindTemp : DataTemp) {
                        System.out.println(FindTemp);
                        if (SourceItem.trim().toString().equals(FindTemp.trim().toString()) || ObjectItem.trim().toString().equals(FindTemp.trim().toString())) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("DataYear", this.cursor.getString(6).substring(0, 4));
                            map.put("DataDate", this.cursor.getString(6).substring(5, 10));
                            map.put("Week", Week);
                            map.put("SourceItem", SourceItem.trim().toString());
                            map.put("ShowClass", this.ShowClass);
                            map.put("ObjectItem", ObjectItem.trim().toString());
                            map.put("DataNote", this.cursor.getString(7).toString());
                            if (this.ShowClass.equals("收入至")) {
                                map.put("InMount", "$" + ShowMount + " ");
                                map.put("OutMount", "");
                                map.put("TrnMount", "");
                                map.put("PMount", "");
                                map.put("ItemImage", Integer.valueOf(R.drawable.ai));
                            }
                            if (this.ShowClass.equals("支出至")) {
                                map.put("OutMount", "$" + ShowMount + " ");
                                map.put("InMount", "");
                                map.put("TrnMount", "");
                                map.put("PMount", "");
                                map.put("ItemImage", Integer.valueOf(R.drawable.ae));
                            }
                            if (this.ShowClass.equals("到帳目")) {
                                map.put("OutMount", "$" + ShowMount + " ");
                                map.put("InMount", "");
                                map.put("TrnMount", "");
                                map.put("PMount", "");
                                map.put("ItemImage", Integer.valueOf(R.drawable.ae));
                            }
                            if (InOutTemp.trim().equals("1")) {
                                map.put("ItemImage", Integer.valueOf(R.drawable.ai1));
                                map.put("InMount", "$" + ShowMount + " ");
                                map.put("OutMount", "");
                                map.put("TrnMount", "");
                                map.put("PMount", "");
                            }
                            if (InOutTemp.trim().equals("2")) {
                                map.put("ItemImage", Integer.valueOf(R.drawable.ae1));
                                map.put("OutMount", "$" + ShowMount + " ");
                                map.put("InMount", "");
                                map.put("TrnMount", "");
                                map.put("PMount", "");
                            }
                            listItem.add(map);
                            MakeNoList.add(String.valueOf(MakeNO));
                        }
                    }
                }
                InOutTemp = "";
                InOutMount = 0.0d;
                DataClass = "";
            }
        }
        this.cursor.close();
        this.DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: mymoney.zero.InOutDetailShow.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InOutDetailShow.this.MakeNoSpinner.setSelection(position);
                InOutDetailShow.this.MakeNoRec.setText(InOutDetailShow.this.MakeNoSpinner.getSelectedItem().toString().trim());
                view.showContextMenu();
            }
        });
        this.DataList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() { // from class: mymoney.zero.InOutDetailShow.2
            @Override // android.view.View.OnCreateContextMenuListener
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("功能");
                menu.add(0, 0, 0, "修改此筆帳務資料");
                menu.add(0, 1, 0, "刪除此筆帳務資料");
                menu.add(0, 2, 0, "顯示詳細備註資料");
            }
        });
        this.DataDB.close();
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.inoutdetailshowlistitem, new String[]{"DataDate", "Week", "SourceItem", "DataYear", "ShowClass", "ObjectItem", "OutMount", "InMount", "DataNote", "ItemImage"}, new int[]{R.id.DataDate, R.id.Week, R.id.SourceItem, R.id.DataYear, R.id.ShowClass, R.id.ObjectItem, R.id.OutMount, R.id.InMount, R.id.DataNote, R.id.ItemImage});
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
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
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
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
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
        if (ShowClass.trim().equals("支出至") || ShowClass.trim().equals("支出")) {
            bundle.putString("REPORT_MODE", "REPORT02");
            bundle.putString("ItemNote", this.GetItemNote);
            bundle.putString("DataMonth", (String) this.DataMonth.getText());
        } else {
            bundle.putString("REPORT_MODE", "REPORT03");
            bundle.putString("ItemNote", this.GetItemNote);
            bundle.putString("DataMonth", (String) this.DataMonth.getText());
        }
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
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
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
        alert.setPositiveButton("確定刪除", new DialogInterface.OnClickListener() { // from class: mymoney.zero.InOutDetailShow.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) throws SQLException {
                InOutDetailShow.this.KillData();
            }
        });
        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: mymoney.zero.InOutDetailShow.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void KillData() throws SQLException {
        this.MAKE_NO = Double.valueOf(this.MakeNoRec.getText().toString()).doubleValue();
        try {
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
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

    public void ShowBox(String TitleString, String BodyString) {
        if (TitleString == "") {
            TitleString = "訊息";
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle(TitleString);
        MyAlertDialog.setMessage(BodyString);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.InOutDetailShow.5
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
                bundle.putString("DataMonth", this.GetDataMonth);
                bundle.putString("ItemNote", this.GetItemNote);
                intent.putExtras(bundle);
//                if (this.ItemClass.trim().equals("支出") || this.ItemClass.trim().equals("業外支出")) {
//                    intent.setClass(this, Report02.class);
//                    startActivity(intent);
//                    finish();
//                }
//                if (this.ItemClass.trim().equals("收入") || this.ItemClass.trim().equals("業外收入")) {
//                    intent.setClass(this, Report03.class);
//                    startActivity(intent);
//                    finish();
//                    break;
//                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}