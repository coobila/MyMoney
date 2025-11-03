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
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/* loaded from: classes.dex */
public class DayInOutShow2 extends Activity {
    private double AccountId;
    private String DATA_MONTH;
    private String DATA_MONTH_VIEW;
    private SQLiteDatabase DataDB;
    private TextView DataDate;
    private ListView DataList;
    private Button Dm;
    private double MAKE_NO;
    private TextView MakeNoRec;
    private Spinner MakeNoSpinner;
    private TextView MenuClick;
    private Button Pm;
    private double SHOW_MAKE_NO;
    private String SQL;
    private CheckBox ShowTop30;
    private Cursor cursor;
    private String AccountName = "";
    private String MountFormat = "";
    private int MountSub = 0;
    private int Loop_I = 0;
    private double GetYear = 0.0d;
    private double GetMonth = 0.0d;
    private String ShowVibrate = "";
    private String ShowDataMonth = "";
    private double SortType = 0.0d;
    private boolean LinkPC = false;

    @Override // android.app.Activity, android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("功能選單");
        menu.add(0, 0, 0, "修改此筆記錄資料");
        menu.add(0, 1, 0, "刪除此筆記錄資料");
        menu.add(0, 2, 0, "複製此筆記錄資料");
        menu.add(0, 3, 0, "上移此筆記錄資料");
        menu.add(0, 4, 0, "下移此筆記錄資料");
        menu.add(0, 5, 0, "顯示詳細備註資料");
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dayinoutshow2);
        this.AccountName = "";
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
        ListView gridView = (ListView) findViewById(R.id.DataList);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        FrameLayout.LayoutParams gridview_params = new FrameLayout.LayoutParams(-2, -2);
        gridview_params.height = metrics.heightPixels - dpToPx(180);
        gridView.setLayoutParams(gridview_params);
        if (this.AccountId == 0.0d) {
            ShowBox("訊息", "您尚未選擇欲作業的帳本");
            return;
        }
        setTitle(String.valueOf(this.AccountName) + " - 帳務記錄明細");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.ShowDataMonth = bundle.getString("ShowDataMonth");
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
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND DATA_NOTE = '排序方式'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.SortType = Double.valueOf(this.cursor.getString(0)).doubleValue();
            this.cursor.close();
        } else {
            this.cursor.close();
        }
        this.DataDB.close();
        this.DataDate = (TextView) findViewById(R.id.DataDate);
        this.MenuClick = (TextView) findViewById(R.id.MenuClick);
        this.ShowTop30 = (CheckBox) findViewById(R.id.ShowTop30);
        this.Pm = (Button) findViewById(R.id.Pm);
        this.Dm = (Button) findViewById(R.id.Dm);
        this.ShowTop30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: mymoney.zero.DayInOutShow2.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DayInOutShow2.this.ShowData("", "", "");
            }
        });
        this.MenuClick.setOnClickListener(new View.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DayInOutShow2.this.ShowMenu();
            }
        });
        GetNowDate GetNow = new GetNowDate();
        if (this.ShowDataMonth.equals("")) {
            this.DATA_MONTH = GetNow.GetDate().substring(0, 7);
            this.DATA_MONTH_VIEW = this.DATA_MONTH;
            this.DATA_MONTH_VIEW = this.DATA_MONTH_VIEW.replace("/", "年");
            this.DATA_MONTH_VIEW = this.DATA_MONTH_VIEW.replace("/", "月");
            this.DataDate.setText(String.valueOf(this.DATA_MONTH_VIEW) + "月份帳務明細");
            this.GetYear = GetNow.GetDateYear();
            this.GetMonth = GetNow.GetDateMonth();
        } else {
            this.DATA_MONTH_VIEW = this.ShowDataMonth;
            this.DATA_MONTH_VIEW = this.DATA_MONTH_VIEW.replace("/", "年");
            this.DATA_MONTH_VIEW = this.DATA_MONTH_VIEW.replace("/", "月");
            this.DataDate.setText(String.valueOf(this.DATA_MONTH_VIEW) + "月份帳務明細");
            this.DATA_MONTH = this.ShowDataMonth;
            this.GetYear = Double.valueOf(this.ShowDataMonth.substring(0, 4)).doubleValue();
            this.GetMonth = Double.valueOf(this.ShowDataMonth.substring(5, 7)).doubleValue() - 1.0d;
            if (this.GetMonth < 1.0d) {
                this.GetYear -= 1.0d;
                this.GetMonth = 12.0d;
            }
            if (this.GetMonth > 12.0d) {
                this.GetYear += 1.0d;
                this.GetMonth = 1.0d;
            }
        }
        this.GetMonth += 1.0d;
        if (this.GetMonth > 12.0d) {
            this.GetYear += 1.0d;
            this.GetMonth = 1.0d;
        }
        Calendar cal = Calendar.getInstance();
        cal.set((int) this.GetYear, (int) this.GetMonth, 1);
        SimpleDateFormat theDate = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = theDate.format(cal.getTime());
        this.DATA_MONTH = dateString.substring(0, 7);
        this.DATA_MONTH_VIEW = this.DATA_MONTH_VIEW.replace("/", "年");
        this.DATA_MONTH_VIEW = this.DATA_MONTH_VIEW.replace("/", "月");
        this.GetMonth -= 1.0d;
        if (this.GetMonth < 1.0d) {
            this.GetYear -= 1.0d;
            this.GetMonth = 12.0d;
        }
        cal.set((int) this.GetYear, (int) this.GetMonth, 1);
        String dateString2 = theDate.format(cal.getTime());
        this.DATA_MONTH = dateString2.substring(0, 7);
        this.SHOW_MAKE_NO = 0.0d;
        this.MAKE_NO = 0.0d;
        try {
            this.MAKE_NO = bundle.getDouble("MAKE_NO");
        } catch (Exception e2) {
        }
        ShowData("", "", "");
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

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void ShowData(String InputNote, String InputNote1, String InputNote2) {
        String SourceItem = "";
        double InMount = 0.0d;
        double OutMount = 0.0d;
        String ShowClass = "";
        double InOutMount = 0.0d;
        String InOutTemp = "";
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.DataList = (ListView) findViewById(R.id.DataList);
        this.MakeNoSpinner = (Spinner) findViewById(R.id.MakeNoSpinner);
        this.MakeNoRec = (TextView) findViewById(R.id.MakeNoRec);
        ArrayList<String> MakeNoList = new ArrayList<>();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MakeNoList);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        this.MakeNoSpinner.setAdapter((SpinnerAdapter) adapter2);
        GetNowDate GetNow = new GetNowDate();
        String ShowDate = this.DataDate.getText().toString().trim();
        GetNow.CDateToEDate(ShowDate);
        if (InputNote.trim().equals("")) {
            if (this.SortType == 0.0d) {
                this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE,PAY_COLL_NAME,INVOICE_NO,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE LIKE '" + this.DATA_MONTH + "%' ORDER BY DATA_DATE DESC,MAKE_NO DESC,DATA_NO";
            } else {
                this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE,PAY_COLL_NAME,INVOICE_NO,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE LIKE '" + this.DATA_MONTH + "%' ORDER BY DATA_DATE,MAKE_NO,DATA_NO";
            }
        } else if (this.SortType == 0.0d) {
            this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE,PAY_COLL_NAME,INVOICE_NO,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE LIKE '%" + InputNote.replace("'", "") + "%' ORDER BY DATA_DATE DESC,MAKE_NO DESC,DATA_NO";
        } else {
            this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE,PAY_COLL_NAME,INVOICE_NO,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE LIKE '%" + InputNote.replace("'", "") + "%' ORDER BY DATA_DATE,MAKE_NO,DATA_NO";
        }
        if (!InputNote1.trim().equals("")) {
            if (this.SortType == 0.0d) {
                this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE,PAY_COLL_NAME,INVOICE_NO,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND PAY_COLL_NAME LIKE '%" + InputNote1.replace("'", "") + "%' ORDER BY DATA_DATE DESC,MAKE_NO DESC,DATA_NO";
            } else {
                this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE,PAY_COLL_NAME,INVOICE_NO,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND PAY_COLL_NAME LIKE '%" + InputNote1.replace("'", "") + "%' ORDER BY DATA_DATE,MAKE_NO,DATA_NO";
            }
        }
        if (!InputNote2.trim().equals("")) {
            if (this.SortType == 0.0d) {
                this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE,PAY_COLL_NAME,INVOICE_NO,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE2 <> '' ORDER BY DATA_DATE DESC,MAKE_NO DESC,DATA_NO";
            } else {
                this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE,PAY_COLL_NAME,INVOICE_NO,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE2 <> '' ORDER BY DATA_DATE,MAKE_NO,DATA_NO";
            }
        }
        if (this.ShowTop30.isChecked()) {
            this.Pm.setEnabled(false);
            this.Dm.setEnabled(false);
            if (this.SortType == 0.0d) {
                this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE,PAY_COLL_NAME,INVOICE_NO,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " ORDER BY MAKE_NO DESC,DATA_NO LIMIT 60";
            } else {
                this.SQL = "SELECT MAKE_NO FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " ORDER BY MAKE_NO DESC,DATA_NO LIMIT 60";
                this.cursor = this.DataDB.rawQuery(this.SQL, null);
                double LimitMakeNO = 0.0d;
                while (this.cursor.moveToNext()) {
                    LimitMakeNO = this.cursor.getDouble(0);
                }
                this.cursor.close();
                this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,DATA_DATE,DATA_NOTE,PAY_COLL_NAME,INVOICE_NO,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO >= " + String.valueOf(LimitMakeNO) + " ORDER BY MAKE_NO,DATA_NO LIMIT 60";
            }
            this.DataDate.setText("最近輸入的 30 筆記錄");
        } else {
            this.Pm.setEnabled(true);
            this.Dm.setEnabled(true);
            this.DataDate.setText(String.valueOf(this.DATA_MONTH_VIEW) + "月份帳務明細");
        }
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        String DataClass = "";
        while (this.cursor.moveToNext()) {
            if (this.cursor.getString(0).equals("1")) {
                SourceItem = this.cursor.getString(2).trim();
            }
            if (this.cursor.getString(0).equals("2")) {
                this.cursor.getString(2).trim();
            }
            double MakeNO = this.cursor.getDouble(1);
            DataClass = String.valueOf(DataClass) + this.cursor.getString(3).trim();
            String DATA_NOTE2 = this.cursor.getString(10).trim();
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
                InOutMount = OutMount;
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
                DecimalFormat MountDf = new DecimalFormat("#,##0" + this.MountFormat);
                String ShowMount = MountDf.format(InOutMount);
                String ObjectItem = this.cursor.getString(2).trim();
                HashMap<String, Object> map = new HashMap<>();
                map.put("DataDate", this.cursor.getString(6).substring(5, 10));
                map.put("Week", Week);
                map.put("DataYear", this.cursor.getString(6).substring(0, 4));
                map.put("SourceItem", SourceItem.trim().toString());
                map.put("ShowClass", ShowClass);
                map.put("ObjectItem", ObjectItem.trim().toString());
                map.put("DataNote", this.cursor.getString(7).toString());
                if (ShowClass.equals("收入至")) {
                    map.put("ItemImage", Integer.valueOf(R.drawable.ai));
                    map.put("InMount", "$" + ShowMount + " ");
                }
                if (ShowClass.equals("支出至")) {
                    map.put("ItemImage", Integer.valueOf(R.drawable.ae));
                    map.put("OutMount", "$" + ShowMount + " ");
                }
                if (ShowClass.equals("到帳目")) {
                    map.put("ItemImage", Integer.valueOf(R.drawable.ae));
                    map.put("OutMount", "$" + ShowMount + " ");
                }
                if (ShowClass.equals("轉帳至")) {
                    map.put("ItemImage", Integer.valueOf(R.drawable.aa));
                    map.put("TrnMount", "$" + ShowMount + " ");
                }
                if (ShowClass.equals("支付至")) {
                    map.put("ItemImage", Integer.valueOf(R.drawable.al));
                    map.put("PMount", "$" + ShowMount + " ");
                }
                if (InOutTemp.trim().equals("1")) {
                    map.put("ItemImage", Integer.valueOf(R.drawable.ai1));
                    map.put("InMount", "$" + ShowMount + " ");
                }
                if (InOutTemp.trim().equals("2")) {
                    map.put("ItemImage", Integer.valueOf(R.drawable.ae1));
                    map.put("OutMount", "$" + ShowMount + " ");
                }
                if (!this.cursor.getString(8).trim().equals("")) {
                    map.put("CollNameImage", Integer.valueOf(R.drawable.user));
                    map.put("CollName", this.cursor.getString(8).trim());
                }
                if (!this.cursor.getString(9).trim().equals("")) {
                    map.put("InvoiceImage", Integer.valueOf(R.drawable.write));
                    map.put("InvoiceNo", this.cursor.getString(9).trim());
                }
                if (this.cursor.getString(10) != null && !this.cursor.getString(10).trim().equals("")) {
                    map.put("DetailImage", Integer.valueOf(R.drawable.edit2));
                }
                InOutTemp = "";
                InOutMount = 0.0d;
                InMount = 0.0d;
                OutMount = 0.0d;
                if (InputNote2.trim().equals("")) {
                    listItem.add(map);
                    MakeNoList.add(String.valueOf(MakeNO));
                } else if (DATA_NOTE2 != null && DATA_NOTE2.indexOf(InputNote2.trim()) > -1) {
                    listItem.add(map);
                    MakeNoList.add(String.valueOf(MakeNO));
                }
                DataClass = "";
            }
        }
        this.cursor.close();
        this.DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: mymoney.zero.DayInOutShow2.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DayInOutShow2.this.MakeNoSpinner.setSelection(position);
                DayInOutShow2.this.MakeNoRec.setText(DayInOutShow2.this.MakeNoSpinner.getSelectedItem().toString().trim());
                view.showContextMenu();
            }
        });
        this.DataList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() { // from class: mymoney.zero.DayInOutShow2.4
            @Override // android.view.View.OnCreateContextMenuListener
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("功能");
                menu.add(0, 0, 0, "修改此筆記錄資料");
                menu.add(0, 1, 0, "刪除此筆記錄資料");
                menu.add(0, 2, 0, "複製此筆記錄資料");
                menu.add(0, 3, 0, "上移此筆記錄資料");
                menu.add(0, 4, 0, "下移此筆記錄資料");
                menu.add(0, 5, 0, "顯示詳細備註資料");
            }
        });
        this.DataDB.close();
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.dayinoutlistitem2, new String[]{"DataDate", "Week", "SourceItem", "DataYear", "ShowClass", "ObjectItem", "OutMount", "InMount", "TrnMount", "PMount", "DataNote", "ItemImage", "CollNameImage", "InvoiceImage", "DetailImage", "CollName", "InvoiceNo"}, new int[]{R.id.DataDate, R.id.Week, R.id.SourceItem, R.id.DataYear, R.id.ShowClass, R.id.ObjectItem, R.id.OutMount, R.id.InMount, R.id.TrnMount, R.id.PMount, R.id.DataNote, R.id.ItemImage, R.id.CollNameImage, R.id.InvoiceImage, R.id.DetailImage, R.id.CollName, R.id.InvoiceNo});
        this.DataList.setAdapter((ListAdapter) listItemAdapter);
    }

    @Override // android.app.Activity
    public boolean onContextItemSelected(MenuItem item) throws SQLException {
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
            try {
                CCopyData();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (item.getItemId() == 3) {
            CMoveUpData();
        }
        if (item.getItemId() == 4) {
            CMoveDownData();
        }
        if (item.getItemId() == 5) {
            CShowDetail();
        }
        return super.onContextItemSelected(item);
    }

    public void ShowBox(String TitleString, String BodyString) {
        if (TitleString == "") {
            TitleString = "訊息";
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle(TitleString);
        MyAlertDialog.setMessage(BodyString);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyAlertDialog.setPositiveButton("確定", OkClick);
        MyAlertDialog.show();
    }

    public void PmClick(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.GetMonth -= 1.0d;
        if (this.GetMonth < 1.0d) {
            this.GetYear -= 1.0d;
            this.GetMonth = 12.0d;
        }
        Calendar cal = Calendar.getInstance();
        cal.set((int) this.GetYear, (int) this.GetMonth, 1);
        SimpleDateFormat theDate = new SimpleDateFormat("yyyy/MM/dd ");
        String dateString = theDate.format(cal.getTime());
        this.DATA_MONTH = dateString.substring(0, 7);
        this.DATA_MONTH_VIEW = this.DATA_MONTH;
        this.DATA_MONTH_VIEW = this.DATA_MONTH_VIEW.replace("/", "年");
        this.DATA_MONTH_VIEW = this.DATA_MONTH_VIEW.replace("/", "月");
        this.DataDate.setText(String.valueOf(this.DATA_MONTH_VIEW) + "月份帳務明細");
        ShowData("", "", "");
    }

    public void DmClick(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.GetMonth += 1.0d;
        if (this.GetMonth > 12.0d) {
            this.GetYear += 1.0d;
            this.GetMonth = 1.0d;
        }
        Calendar cal = Calendar.getInstance();
        cal.set((int) this.GetYear, (int) this.GetMonth, 1);
        SimpleDateFormat theDate = new SimpleDateFormat("yyyy/MM/dd ");
        String dateString = theDate.format(cal.getTime());
        this.DATA_MONTH = dateString.substring(0, 7);
        this.DATA_MONTH_VIEW = this.DATA_MONTH;
        this.DATA_MONTH_VIEW = this.DATA_MONTH_VIEW.replace("/", "年");
        this.DATA_MONTH_VIEW = this.DATA_MONTH_VIEW.replace("/", "月");
        this.DataDate.setText(String.valueOf(this.DATA_MONTH_VIEW) + "月份帳務明細");
        ShowData("", "", "");
    }

    public void EditData(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        CEditData();
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
        if (ShowClass.trim().equals("支出") || ShowClass.trim().equals("業外支出")) {
//            intent.setClass(this, Editout.class);
//            startActivity(intent);
            finish();
        }
        if (ShowClass.trim().equals("收入") || ShowClass.trim().equals("業外收入")) {
//            intent.setClass(this, Editin.class);
//            startActivity(intent);
            finish();
        }
        if (ShowClass.trim().equals("轉帳")) {
//            intent.setClass(this, Edittransfer.class);
//            startActivity(intent);
            finish();
        }
    }

    public void NoteFind() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("摘要搜尋");
        alert.setMessage("請輸入要搜尋摘要的文字");
        final EditText input = new EditText(this);
        alert.setView(input);
        input.setText("");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                String InputNote = input.getText().toString();
                DayInOutShow2.this.ShowData(InputNote, "", "");
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        alert.setPositiveButton("確定", OkClick);
        alert.setNegativeButton("取消", CanCelClick);
        alert.show();
    }

    public void Note1Find() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("摘要搜尋");
        alert.setMessage("請輸入要搜尋收付人的文字");
        final EditText input = new EditText(this);
        alert.setView(input);
        input.setText("");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                String InputNote = input.getText().toString();
                DayInOutShow2.this.ShowData("", InputNote, "");
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        alert.setPositiveButton("確定", OkClick);
        alert.setNegativeButton("取消", CanCelClick);
        alert.show();
    }

    public void Note2Find() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("詳細備註搜尋");
        alert.setMessage("請輸入要搜尋詳細備註的文字");
        final EditText input = new EditText(this);
        alert.setView(input);
        input.setText("");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                String InputNote = input.getText().toString();
                DayInOutShow2.this.ShowData("", "", InputNote);
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        alert.setPositiveButton("確定", OkClick);
        alert.setNegativeButton("取消", CanCelClick);
        alert.show();
    }

    public void SetSort(int SetSortType) throws SQLException {
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND DATA_NOTE = '排序方式'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.cursor.close();
        } else {
            this.cursor.close();
            this.SQL = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + this.AccountId + ",'排序方式','0')";
            this.DataDB.execSQL(this.SQL);
        }
        this.SortType = SetSortType;
        this.SQL = "UPDATE SYSTEM_DATA SET DATA_VALUE = " + String.valueOf(SetSortType) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '排序方式'";
        this.DataDB.execSQL(this.SQL);
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

    public void CEditCollNameData() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
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
            if (this.cursor.getString(3).trim().equals("收入") || this.cursor.getString(3).trim().equals("支出") || this.cursor.getString(3).trim().equals("業外收入") || this.cursor.getString(3).trim().equals("業外支出")) {
                ShowClass = this.cursor.getString(3);
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
        }
        this.cursor.close();
        this.DataDB.close();
        bundle.putDouble("MAKE_NO", this.MAKE_NO);
//        intent.putExtras(bundle);
//        intent.setClass(this, EditCollName.class);
//        startActivity(intent);
        finish();
    }

    public void DeleteData(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        CDeleteData();
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
        this.SQL = "SELECT DATA_NO,MAKE_NO,ITEM_NOTE,ITEM_CLASS,IN_MOUNT,OUT_MOUNT,PC_MAKE_NO FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(this.MAKE_NO) + " ORDER BY DATA_DATE,MAKE_NO,DATA_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        String DataClass = "";
        while (this.cursor.moveToNext()) {
            if (this.cursor.getDouble(6) != 0.0d) {
                this.LinkPC = true;
            } else {
                this.LinkPC = false;
            }
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
        alert.setPositiveButton("確定刪除", new DialogInterface.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) throws SQLException {
                DayInOutShow2.this.KillData();
            }
        });
        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.13
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void CCopyData() throws SQLException, InterruptedException {
        double NEW_MAKE_NO;
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.MAKE_NO = Double.valueOf(this.MakeNoRec.getText().toString()).doubleValue();
        this.cursor = this.DataDB.rawQuery("SELECT MAKE_NO FROM MYMONEY_DATA ORDER BY MAKE_NO DESC", null);
        if (this.cursor.moveToNext()) {
            NEW_MAKE_NO = this.cursor.getDouble(0) + 1.0d;
        } else {
            NEW_MAKE_NO = 1.0d;
        }
        this.cursor.close();
        GetNowDate GetNow = new GetNowDate();
        String DATA_KEY = GetNow.GetDataKey();
        this.SQL = "SELECT ITEM_CLASS,DATA_DATE,ITEM_NOTE,IN_MOUNT,OUT_MOUNT,DATA_NOTE,INVOICE_NO,DATA_NO,PAY_COLL_NAME,PROJECT_ID,EXCHANGE,DATA_NOTE2 FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(this.MAKE_NO) + " ORDER BY DATA_DATE,MAKE_NO,DATA_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        while (this.cursor.moveToNext()) {
            this.SQL = "INSERT INTO MYMONEY_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_CLASS,DATA_DATE,ITEM_NOTE,IN_MOUNT,OUT_MOUNT,DATA_NOTE,INVOICE_NO,DATA_NO,PAY_COLL_NAME,PROJECT_ID,EXCHANGE,LINK_PC,EXPORT,DATA_NOTE2,DATA_KEY) VALUES ('admin'," + String.valueOf(this.AccountId) + "," + String.valueOf(NEW_MAKE_NO) + ",'" + this.cursor.getString(0).replaceAll("'", "''") + "','" + GetNow.GetDate() + "','" + this.cursor.getString(2).replaceAll("'", "''") + "'," + String.valueOf(this.cursor.getDouble(3)) + "," + String.valueOf(this.cursor.getDouble(4)) + ",'" + this.cursor.getString(5).replaceAll("'", "''") + "','" + this.cursor.getString(6).replaceAll("'", "''") + "','" + this.cursor.getString(7).replaceAll("'", "''") + "','" + this.cursor.getString(8).replaceAll("'", "''") + "','" + this.cursor.getString(9).replaceAll("'", "''") + "'," + String.valueOf(this.cursor.getDouble(10)) + ",'','0','" + this.cursor.getString(11).replaceAll("'", "''") + "','" + DATA_KEY + "')";
            this.DataDB.execSQL(this.SQL);
        }
        this.cursor.close();
        this.DataDB.close();
        Toast.makeText(this, "帳務記錄資料複製完成!", Toast.LENGTH_SHORT).show();
        try {
//            Intent i = new Intent(getBaseContext(), (Class<?>) MyWidget.class);
//            i.setAction("com.android.mymoneyzero");
//            sendBroadcast(i);
        } catch (Exception e2) {
        }
        ShowData("", "", "");
        Toast.makeText(this, "資料已複製完成!", Toast.LENGTH_SHORT).show();
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

    public void KillData() throws SQLException {
        this.MAKE_NO = Double.valueOf(this.MakeNoRec.getText().toString()).doubleValue();
        double MAKE_NO2 = 0.0d;
        if (this.MakeNoSpinner.getAdapter().getCount() > 1) {
            if (this.MakeNoSpinner.getSelectedItemPosition() + 1 < this.MakeNoSpinner.getAdapter().getCount()) {
                MAKE_NO2 = Double.valueOf(this.MakeNoSpinner.getItemAtPosition(this.MakeNoSpinner.getSelectedItemPosition() + 1).toString()).doubleValue();
            } else if (this.MakeNoSpinner.getAdapter().getCount() > 0) {
                MAKE_NO2 = Double.valueOf(this.MakeNoSpinner.getItemAtPosition(this.MakeNoSpinner.getSelectedItemPosition() - 1).toString()).doubleValue();
            }
        }
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        String DATA_KEY = "";
        if (this.LinkPC) {
            this.SQL = "SELECT DATA_KEY FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(this.MAKE_NO);
            this.cursor = this.DataDB.rawQuery(this.SQL, null);
            if (this.cursor.moveToNext()) {
                DATA_KEY = this.cursor.getString(0);
            }
            this.cursor.close();
            if (!DATA_KEY.equals("")) {
                this.SQL = "INSERT INTO MYMONEY_DATA_KILL (USER_ID,ACCOUNT_ID,DATA_KEY) VALUES ('admin'," + this.AccountId + ",'" + DATA_KEY.trim() + "')";
                this.DataDB.execSQL(this.SQL);
            }
        }
        this.SQL = "DELETE FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(this.MAKE_NO);
        this.DataDB.execSQL(this.SQL);
        this.DataDB.close();
        Toast.makeText(this, "刪除帳務記錄資料完成!", Toast.LENGTH_SHORT).show();
        try {
//            Intent i = new Intent(getBaseContext(), (Class<?>) MyWidget.class);
//            i.setAction("com.android.mymoneyzero");
//            sendBroadcast(i);
        } catch (Exception e2) {
        }
        ShowData("", "", "");
        if (MAKE_NO2 != 0.0d) {
            this.Loop_I = 0;
            while (this.Loop_I <= this.DataList.getCount() - 1) {
                this.MakeNoSpinner.setSelection(this.Loop_I);
                if (this.MakeNoSpinner.getSelectedItem().toString().trim().compareTo(String.valueOf(MAKE_NO2).trim()) != 0) {
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

    public void CMoveUpData() throws SQLException {
        String DataDate;
        double NEW_MAKE_NO = 0.0d;
        double PC_MAKE_NO = 0.0d;
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        double OLD_MAKE_NO = Double.valueOf(this.MakeNoRec.getText().toString()).doubleValue();
        this.SQL = "SELECT DATA_DATE,PC_MAKE_NO FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + OLD_MAKE_NO;
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            DataDate = this.cursor.getString(0);
            PC_MAKE_NO = this.cursor.getDouble(1);
        } else {
            DataDate = "";
        }
        this.cursor.close();
        if (PC_MAKE_NO != 0.0d) {
            Toast.makeText(this, "此筆記錄已同步到電腦版上,您無法再上下移動此筆記錄!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.SQL = "SELECT MAKE_NO FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE = '" + DataDate + "' AND MAKE_NO > " + String.valueOf(OLD_MAKE_NO) + " ORDER BY MAKE_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            NEW_MAKE_NO = this.cursor.getDouble(0);
        }
        if (NEW_MAKE_NO != 0.0d) {
            this.SQL = "UPDATE MYMONEY_DATA SET MAKE_NO = -9999 WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(OLD_MAKE_NO);
            this.DataDB.execSQL(this.SQL);
            this.SQL = "UPDATE MYMONEY_DATA SET MAKE_NO = " + String.valueOf(OLD_MAKE_NO) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(NEW_MAKE_NO);
            this.DataDB.execSQL(this.SQL);
            this.SQL = "UPDATE MYMONEY_DATA SET MAKE_NO = " + String.valueOf(NEW_MAKE_NO) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = -9999";
            this.DataDB.execSQL(this.SQL);
        }
        this.DataDB.close();
        Toast.makeText(this, "帳務記錄資料向上移動完成!", Toast.LENGTH_LONG).show();
        ShowData("", "", "");
    }

    public void CMoveDownData() throws SQLException {
        String DataDate;
        double NEW_MAKE_NO = 0.0d;
        double PC_MAKE_NO = 0.0d;
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        double OLD_MAKE_NO = Double.valueOf(this.MakeNoRec.getText().toString()).doubleValue();
        this.SQL = "SELECT DATA_DATE,PC_MAKE_NO FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + OLD_MAKE_NO;
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            DataDate = this.cursor.getString(0);
            PC_MAKE_NO = this.cursor.getDouble(1);
        } else {
            DataDate = "";
        }
        this.cursor.close();
        if (PC_MAKE_NO != 0.0d) {
            Toast.makeText(this, "此筆記錄已同步到電腦版上,您無法再上下移動此筆記錄!", Toast.LENGTH_LONG).show();
            return;
        }
        this.SQL = "SELECT MAKE_NO FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE = '" + DataDate + "' AND MAKE_NO < " + String.valueOf(OLD_MAKE_NO) + " ORDER BY MAKE_NO DESC";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            NEW_MAKE_NO = this.cursor.getDouble(0);
        }
        if (NEW_MAKE_NO != 0.0d) {
            this.SQL = "UPDATE MYMONEY_DATA SET MAKE_NO = -9999 WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(OLD_MAKE_NO);
            this.DataDB.execSQL(this.SQL);
            this.SQL = "UPDATE MYMONEY_DATA SET MAKE_NO = " + String.valueOf(OLD_MAKE_NO) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = " + String.valueOf(NEW_MAKE_NO);
            this.DataDB.execSQL(this.SQL);
            this.SQL = "UPDATE MYMONEY_DATA SET MAKE_NO = " + String.valueOf(NEW_MAKE_NO) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND MAKE_NO = -9999";
            this.DataDB.execSQL(this.SQL);
        }
        this.DataDB.close();
        Toast.makeText(this, "帳務記錄資料向下移動完成!", Toast.LENGTH_SHORT).show();
        ShowData("", "", "");
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
        intent.setClass(this, MyMoneyZeroActivity.class);
        startActivity(intent);
        finish();
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

    public void ShowMenu() {
        final String[] ListStr = {"摘要搜尋記錄", "收付人搜尋記錄", "詳細備註搜尋記錄", "日期由近到遠排序顯示", "日期由遠到近排序顯示", "顯示/取消發票號碼與收付人欄"};
        AlertDialog.Builder MyListAlertDialog = new AlertDialog.Builder(this);
        MyListAlertDialog.setTitle("功能選單");
        DialogInterface.OnClickListener ListClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.14
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) throws SQLException {
                if (ListStr[which].toString().trim().equals("摘要搜尋記錄")) {
                    DayInOutShow2.this.NoteFind();
                }
                if (ListStr[which].toString().trim().equals("收付人搜尋記錄")) {
                    DayInOutShow2.this.Note1Find();
                }
                if (ListStr[which].toString().trim().equals("詳細備註搜尋記錄")) {
                    DayInOutShow2.this.Note2Find();
                }
                if (ListStr[which].toString().trim().equals("日期由近到遠排序顯示")) {
                    DayInOutShow2.this.SetSort(1);
                    DayInOutShow2.this.ShowData("", "", "");
                }
                if (ListStr[which].toString().trim().equals("日期由遠到近排序顯示")) {
                    DayInOutShow2.this.SetSort(0);
                    DayInOutShow2.this.ShowData("", "", "");
                }
                if (ListStr[which].toString().trim().equals("顯示/取消發票號碼與收付人欄")) {
//                    String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath = String.valueOf(getExternalFilesDir(null));
                    try {
                        DayInOutShow2.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                    } catch (Exception e) {
                    }
                    DayInOutShow2.this.SQL = "UPDATE SYSTEM_DATA SET DATA_VALUE = '0' WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(DayInOutShow2.this.AccountId) + " AND DATA_NOTE = '帳務記錄顯示發票號碼'";
                    DayInOutShow2.this.DataDB.execSQL(DayInOutShow2.this.SQL);
                    Intent intent1 = new Intent();
                    intent1.setClass(DayInOutShow2.this, DayInOutShow.class);
                    DayInOutShow2.this.startActivity(intent1);
                    DayInOutShow2.this.finish();
                }
            }
        };
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.DayInOutShow2.15
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyListAlertDialog.setItems(ListStr, ListClick);
        MyListAlertDialog.setNeutralButton("取消", OkClick);
        MyListAlertDialog.show();
    }
}