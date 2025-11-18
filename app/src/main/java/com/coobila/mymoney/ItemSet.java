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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ItemSet extends Activity {
    private double AccountId;
    private String BeforeMount;
    private SQLiteDatabase DataDB;
    private String Exchange;
    private String ItemClass;
    private String ItemNote;
    private Spinner ItemNoteRec;
    private String SQL;
    private TextView SelectItemNote;
    private Cursor cursor;
    private Cursor cursor2;
    private int MountSub = 0;
    private int ExchangeSub = 0;
    private int Loop_I = 0;
    private String MountFormat = "";
    private String ExchangeFormat = "";
    private String ItemNoteRecPtr = "";
    private String AccountName = "";
    private String ShowVibrate = "";

    @Override // android.app.Activity, android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("功能");
        menu.add(0, 0, 0, "修改此筆項目資料");
        menu.add(0, 1, 0, "刪除此筆項目資料");
        menu.add(0, 2, 0, "上移此筆項目資料");
        menu.add(0, 3, 0, "下移此筆項目資料");
        menu.add(0, 4, 0, "設定/取消顯示在桌面上");
    }

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
        setContentView(R.layout.itemset);
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
        this.DataDB.close();
        setTitle(String.valueOf(this.AccountName) + " - 項目設定作業");
        this.SelectItemNote = (TextView) findViewById(R.id.SelectItemNote);
        Bundle bundle = getIntent().getExtras();
        this.ItemClass = bundle.getString("ITEM_CLASS");
        this.ItemNoteRecPtr = "";
        try {
            this.ItemNoteRecPtr = bundle.getString("ItemNoteRecPtr");
        } catch (Exception e2) {
        }
        if (this.ItemClass == null) {
            this.ItemClass = "資產";
        }
        ListView gridView = (ListView) findViewById(R.id.ListView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        FrameLayout.LayoutParams gridview_params = new FrameLayout.LayoutParams(-2, -2);
        gridview_params.height = metrics.heightPixels - dpToPx(225);
        gridView.setLayoutParams(gridview_params);
        if (this.ItemNoteRecPtr == null) {
            ShowItemData(this.ItemClass, "");
        } else {
            ShowItemData(this.ItemClass, this.ItemNoteRecPtr.toString().trim());
        }
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void ShowA(View CvClick) {
        this.SelectItemNote.setText("");
        this.ItemClass = "資產";
        ShowItemData("資產", "");
    }

    public void ShowL(View CvClick) {
        this.SelectItemNote.setText("");
        this.ItemClass = "負債";
        ShowItemData("負債", "");
    }

    public void ShowI(View CvClick) {
        this.SelectItemNote.setText("");
        this.ItemClass = "收入";
        ShowItemData("收入", "");
    }

    public void ShowIE(View CvClick) {
        this.SelectItemNote.setText("");
        this.ItemClass = "業外收入";
        ShowItemData("業外收入", "");
    }

    public void ShowE(View CvClick) {
        this.SelectItemNote.setText("");
        this.ItemClass = "支出";
        ShowItemData("支出", "");
    }

    public void ShowEE(View CvClick) {
        this.SelectItemNote.setText("");
        this.ItemClass = "業外支出";
        ShowItemData("業外支出", "");
    }

    public void ShowItemData(String ItemClass, String ItemNotePtr) {
        int ItemCount = 0;
        int ItemPtr = 0;
        DecimalFormat MountDf = new DecimalFormat("#,##0" + this.MountFormat);
        DecimalFormat ExchangeDF = new DecimalFormat("#,##0" + this.ExchangeFormat);
        this.ItemNoteRec = (Spinner) findViewById(R.id.ItemNoteRec);
        ListView list = (ListView) findViewById(R.id.ListView);
        ArrayList<String> ItemListData = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ItemListData);
        this.ItemNoteRec.setAdapter((SpinnerAdapter) adapter);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE,HIDESTYLE,CASH FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = PARENT_NOTE AND ITEM_CLASS = '" + ItemClass.replace("'", "") + "' ORDER BY MAKE_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        while (this.cursor.moveToNext()) {
            this.ItemNote = this.cursor.getString(0);
            this.BeforeMount = String.valueOf(this.cursor.getDouble(1));
            this.Exchange = String.valueOf(this.cursor.getDouble(2));
            this.BeforeMount = MountDf.format(Double.valueOf(this.BeforeMount));
            this.Exchange = ExchangeDF.format(Double.valueOf(this.Exchange));
            HashMap<String, Object> map = new HashMap<>();
            if (ItemClass.equals("資產")) {
                map.put("ItemImage", Integer.valueOf(R.drawable.aa));
                map.put("ItemNote", this.ItemNote);
            }
            if (ItemClass.equals("負債")) {
                map.put("ItemImage", Integer.valueOf(R.drawable.al));
                map.put("ItemNote", this.ItemNote);
            }
            if (ItemClass.equals("收入")) {
                this.Exchange = ExchangeDF.format(1L);
                map.put("ItemImage", Integer.valueOf(R.drawable.ai));
                map.put("ItemNote2", this.ItemNote);
            }
            if (ItemClass.equals("業外收入")) {
                this.Exchange = ExchangeDF.format(1L);
                map.put("ItemImage", Integer.valueOf(R.drawable.ai1));
                map.put("ItemNote2", this.ItemNote);
            }
            if (ItemClass.equals("支出")) {
                this.Exchange = ExchangeDF.format(1L);
                map.put("ItemImage", Integer.valueOf(R.drawable.ae));
                map.put("ItemNote2", this.ItemNote);
            }
            if (ItemClass.equals("業外支出")) {
                this.Exchange = ExchangeDF.format(1L);
                map.put("ItemImage", Integer.valueOf(R.drawable.ae1));
                map.put("ItemNote2", this.ItemNote);
            }
            if (ItemClass.equals("資產") || ItemClass.equals("負債")) {
                map.put("Exchange", String.valueOf(this.Exchange) + "  ");
                map.put("BeforeMount", "期初餘額：$" + this.BeforeMount);
            } else {
                map.put("Exchange", String.valueOf(this.Exchange) + "  ");
                map.put("BeforeMount", "");
            }
            if (this.cursor.getString(3).trim().equals("1")) {
                map.put("HideImage", Integer.valueOf(R.drawable.close));
            }
            if (this.cursor.getString(4) != null && this.cursor.getString(4).trim().equals("1")) {
                map.put("Cash", Integer.valueOf(R.drawable.computer));
            }
            listItem.add(map);
            ItemCount++;
            if (!ItemNotePtr.equals("") && ItemNotePtr.trim().equals(this.ItemNote.toString().trim())) {
                ItemPtr = ItemCount;
            }
            ItemListData.add(this.ItemNote);
            this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE,HIDESTYLE,CASH FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE <> PARENT_NOTE AND ITEM_CLASS = '" + ItemClass.replace("'", "").trim() + "' AND PARENT_NOTE = '" + this.ItemNote.replace("'", "").trim() + "' ORDER BY MAKE_NO";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            while (this.cursor2.moveToNext()) {
                this.ItemNote = this.cursor2.getString(0);
                this.BeforeMount = String.valueOf(this.cursor2.getDouble(1));
                this.Exchange = String.valueOf(this.cursor2.getDouble(2));
                this.BeforeMount = MountDf.format(Double.valueOf(this.BeforeMount));
                this.Exchange = ExchangeDF.format(Double.valueOf(this.Exchange));
                HashMap<String, Object> map2 = new HashMap<>();
                if (ItemClass.equals("資產")) {
                    map2.put("ItemImage2", Integer.valueOf(R.drawable.aa));
                    map2.put("ItemNote", "    " + this.ItemNote);
                }
                if (ItemClass.equals("負債")) {
                    map2.put("ItemImage2", Integer.valueOf(R.drawable.al));
                    map2.put("ItemNote", "    " + this.ItemNote);
                }
                if (ItemClass.equals("收入") || ItemClass.equals("業外收入")) {
                    this.Exchange = ExchangeDF.format(1L);
                    map2.put("ItemImage2", Integer.valueOf(R.drawable.ai));
                    map2.put("ItemNote2", "    " + this.ItemNote);
                }
                if (ItemClass.equals("支出") || ItemClass.equals("業外支出")) {
                    this.Exchange = ExchangeDF.format(1L);
                    map2.put("ItemImage2", Integer.valueOf(R.drawable.ae));
                    map2.put("ItemNote2", "    " + this.ItemNote);
                }
                if (ItemClass.equals("資產") || ItemClass.equals("負債")) {
                    map2.put("Exchange", String.valueOf(this.Exchange) + "  ");
                    map2.put("BeforeMount", "     期初餘額：$" + this.BeforeMount);
                } else {
                    map2.put("Exchange", String.valueOf(this.Exchange) + "  ");
                    map2.put("BeforeMount", "");
                }
                if (this.cursor2.getString(3).trim().equals("1")) {
                    map2.put("HideImage", Integer.valueOf(R.drawable.close));
                }
                if (this.cursor2.getString(4) != null && this.cursor2.getString(4).trim().equals("1")) {
                    map2.put("Cash", Integer.valueOf(R.drawable.computer));
                }
                listItem.add(map2);
                ItemCount++;
                if (!ItemNotePtr.equals("") && ItemNotePtr.trim().equals(this.ItemNote.toString().trim())) {
                    ItemPtr = ItemCount;
                }
                ItemListData.add(this.ItemNote);
            }
            this.cursor2.close();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: mymoney.zero.ItemSet.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemSet.this.ItemNoteRec.setSelection(position);
                ItemSet.this.SelectItemNote.setText(ItemSet.this.ItemNoteRec.getSelectedItem().toString());
                TextView Item_Note = (TextView) view.findViewById(R.id.ItemNote);
                ItemSet.this.ItemNoteRecPtr = Item_Note.getText().toString().trim();
                if (ItemSet.this.ItemNoteRecPtr.equals("")) {
                    TextView Item_Note2 = (TextView) view.findViewById(R.id.ItemNote2);
                    ItemSet.this.ItemNoteRecPtr = Item_Note2.getText().toString().trim();
                }
                view.showContextMenu();
            }
        });
        list.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() { // from class: mymoney.zero.ItemSet.2
            @Override // android.view.View.OnCreateContextMenuListener
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("功能");
                menu.add(0, 0, 0, "修改此筆項目資料");
                menu.add(0, 1, 0, "刪除此筆項目資料");
                menu.add(0, 2, 0, "上移此筆項目資料");
                menu.add(0, 3, 0, "下移此筆項目資料");
                menu.add(0, 4, 0, "設定/取消顯示在桌面上");
            }
        });
        this.cursor.close();
        this.DataDB.close();
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.itemsetlist, new String[]{"ItemImage", "ItemImage2", "ItemNote", "ItemNote2", "BeforeMount", "Exchange", "HideImage", "Cash"}, new int[]{R.id.ItemImage, R.id.ItemImage2, R.id.ItemNote, R.id.ItemNote2, R.id.BeforeMount, R.id.Exchange, R.id.HideImage, R.id.Cash});
        list.setAdapter((ListAdapter) listItemAdapter);
        if (ItemPtr > 0) {
            try {
                list.setSelection(ItemPtr - 1);
            } catch (Exception e2) {
            }
        }
    }

    @Override // android.app.Activity
    public boolean onContextItemSelected(MenuItem item) throws SQLException {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        this.ItemNoteRec.setSelection(menuInfo.position);
        this.SelectItemNote.setText(this.ItemNoteRec.getSelectedItem().toString());
        if (item.getItemId() == 0) {
            CEditData();
        }
        if (item.getItemId() == 1) {
            CDeleteData();
        }
        if (item.getItemId() == 2) {
            UpItem();
        }
        if (item.getItemId() == 3) {
            DownItem();
        }
        if (item.getItemId() == 4) {
            SetCash();
        }
        return super.onContextItemSelected(item);
    }

    public void Up_Item(View CvClick) throws SQLException {
        UpItem();
    }

    public void Down_Item(View CvClick) throws SQLException {
        DownItem();
    }

    public void UpItem() throws SQLException {
        String ITEM_NOTE = this.SelectItemNote.getText().toString().trim();
        if (ITEM_NOTE.equals("")) {
            ShowBox("訊息提示", "您尚未選取項目資料!");
            return;
        }
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "SELECT MAKE_NO,PARENT_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            String PARENT_NOTE = this.cursor.getString(1);
            double MAKE_NO1 = this.cursor.getDouble(0);
            String NOW_ITEM_CLASS = this.cursor.getString(2);
            this.cursor.close();
            if (ITEM_NOTE.trim().equals(PARENT_NOTE.trim())) {
                this.SQL = "SELECT MAKE_NO FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = PARENT_NOTE AND ITEM_NOTE <> '" + ITEM_NOTE.trim() + "' AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO < " + String.valueOf(MAKE_NO1) + " ORDER BY MAKE_NO DESC";
                this.cursor = this.DataDB.rawQuery(this.SQL, null);
                if (this.cursor.moveToNext()) {
                    double MAKE_NO2 = this.cursor.getDouble(0);
                    this.cursor.close();
                    this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = -1 WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS + "' AND MAKE_NO = " + String.valueOf(MAKE_NO1);
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = " + String.valueOf(MAKE_NO1) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO = " + String.valueOf(MAKE_NO2);
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = " + String.valueOf(MAKE_NO2) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO = -1";
                    this.DataDB.execSQL(this.SQL);
                    this.DataDB.close();
                    ShowItemData(NOW_ITEM_CLASS, this.ItemNoteRecPtr.trim());
                    return;
                }
                this.cursor.close();
                this.DataDB.close();
                return;
            }
            this.SQL = "SELECT MAKE_NO FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + PARENT_NOTE.trim() + "' AND ITEM_NOTE <> '" + ITEM_NOTE.trim() + "' AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO < " + String.valueOf(MAKE_NO1) + " ORDER BY MAKE_NO DESC";
            this.cursor = this.DataDB.rawQuery(this.SQL, null);
            if (this.cursor.moveToNext()) {
                double MAKE_NO22 = this.cursor.getDouble(0);
                this.cursor.close();
                this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = -1 WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS + "' AND MAKE_NO = " + String.valueOf(MAKE_NO1);
                this.DataDB.execSQL(this.SQL);
                this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = " + String.valueOf(MAKE_NO1) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO = " + String.valueOf(MAKE_NO22);
                this.DataDB.execSQL(this.SQL);
                this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = " + String.valueOf(MAKE_NO22) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO = -1";
                this.DataDB.execSQL(this.SQL);
                this.DataDB.close();
                ShowItemData(NOW_ITEM_CLASS, this.ItemNoteRecPtr.trim());
                return;
            }
            this.cursor.close();
            this.DataDB.close();
            return;
        }
        this.cursor.close();
        this.DataDB.close();
    }

    public void DownItem() throws SQLException {
        String ITEM_NOTE = this.SelectItemNote.getText().toString().trim();
        if (ITEM_NOTE.equals("")) {
            ShowBox("訊息提示", "您尚未選取項目資料!");
            return;
        }
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "SELECT MAKE_NO,PARENT_NOTE,ITEM_CLASS FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            String PARENT_NOTE = this.cursor.getString(1);
            double MAKE_NO1 = this.cursor.getDouble(0);
            String NOW_ITEM_CLASS = this.cursor.getString(2);
            this.cursor.close();
            if (ITEM_NOTE.trim().equals(PARENT_NOTE.trim())) {
                this.SQL = "SELECT MAKE_NO FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = PARENT_NOTE AND ITEM_NOTE <> '" + ITEM_NOTE.trim() + "' AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO > " + String.valueOf(MAKE_NO1) + " ORDER BY MAKE_NO";
                this.cursor = this.DataDB.rawQuery(this.SQL, null);
                if (this.cursor.moveToNext()) {
                    double MAKE_NO2 = this.cursor.getDouble(0);
                    this.cursor.close();
                    this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = -1 WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS + "' AND MAKE_NO = " + String.valueOf(MAKE_NO1);
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = " + String.valueOf(MAKE_NO1) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO = " + String.valueOf(MAKE_NO2);
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = " + String.valueOf(MAKE_NO2) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO = -1";
                    this.DataDB.execSQL(this.SQL);
                    this.DataDB.close();
                    ShowItemData(NOW_ITEM_CLASS, this.ItemNoteRecPtr.trim());
                    return;
                }
                this.cursor.close();
                this.DataDB.close();
                return;
            }
            this.SQL = "SELECT MAKE_NO FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE <> PARENT_NOTE AND PARENT_NOTE = '" + PARENT_NOTE.trim() + "' AND ITEM_NOTE <> '" + ITEM_NOTE.trim() + "' AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO > " + String.valueOf(MAKE_NO1) + " ORDER BY MAKE_NO";
            this.cursor = this.DataDB.rawQuery(this.SQL, null);
            if (this.cursor.moveToNext()) {
                double MAKE_NO22 = this.cursor.getDouble(0);
                this.cursor.close();
                this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = -1 WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS + "' AND MAKE_NO = " + String.valueOf(MAKE_NO1);
                this.DataDB.execSQL(this.SQL);
                this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = " + String.valueOf(MAKE_NO1) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO = " + String.valueOf(MAKE_NO22);
                this.DataDB.execSQL(this.SQL);
                this.SQL = "UPDATE ITEM_DATA SET MAKE_NO = " + String.valueOf(MAKE_NO22) + " WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + NOW_ITEM_CLASS.trim() + "' AND MAKE_NO = -1";
                this.DataDB.execSQL(this.SQL);
                this.DataDB.close();
                ShowItemData(NOW_ITEM_CLASS, this.ItemNoteRecPtr.trim());
                return;
            }
            this.cursor.close();
            this.DataDB.close();
            return;
        }
        this.cursor.close();
        this.DataDB.close();
    }

    public void ShowBox(String TitleString, String BodyString) {
        if (TitleString == "") {
            TitleString = "訊息";
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle(TitleString);
        MyAlertDialog.setMessage(BodyString);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.ItemSet.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyAlertDialog.setPositiveButton("確定", OkClick);
        MyAlertDialog.show();
    }

    public void AddData(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("ITEM_CLASS", this.ItemClass.trim());
        intent.putExtras(bundle);
        intent.setClass(this, ItemSetAdd.class);
        startActivity(intent);
        finish();
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
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        String ITEM_NOTE = this.SelectItemNote.getText().toString().trim();
        if (ITEM_NOTE.equals("")) {
            ShowBox("訊息提示", "您尚未選取項目資料!");
            return;
        }
        bundle.putString("ITEM_NOTE", this.SelectItemNote.getText().toString().trim());
        bundle.putString("ITEM_CLASS", this.ItemClass.trim());
        intent.putExtras(bundle);
        intent.setClass(this, ItemSetEdit.class);
        startActivity(intent);
        finish();
    }

    public void SetCash() throws SQLException {
        double CashCount = 0.0d;
        String ITEM_NOTE = this.SelectItemNote.getText().toString().trim();
        if (ITEM_NOTE.equals("")) {
            ShowBox("訊息提示", "您尚未選取項目資料!");
            return;
        }
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        String CASH = "";
        this.SQL = "SELECT CASH FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            if (this.cursor.getString(0) == null) {
                CASH = "";
            } else {
                CASH = this.cursor.getString(0);
            }
        }
        this.cursor.close();
        if (CASH.equals("")) {
            Boolean RegStyle = false;
            CheckSerial Serial = new CheckSerial();
            if (Serial.GetInputSerial()) {
                RegStyle = true;
            }
            if (!RegStyle.booleanValue()) {
                this.SQL = "SELECT COUNT(*) AS CASH_COUNT FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND CASH = '1'";
                this.cursor = this.DataDB.rawQuery(this.SQL, null);
                if (this.cursor.moveToNext()) {
                    CashCount = this.cursor.getDouble(0);
                }
                this.cursor.close();
                if (CashCount >= 2.0d) {
                    ShowBox("訊息", "[非專業版] 版本最多只能設定二個項目顯示於桌面上!");
                    return;
                }
            }
            this.SQL = "UPDATE ITEM_DATA SET CASH = '1' WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "'";
            this.DataDB.execSQL(this.SQL);
            Toast.makeText(this, "已成功的將 " + ITEM_NOTE.trim() + " 項目設定為顯示在桌面上!", Toast.LENGTH_SHORT).show();
        } else {
            this.SQL = "UPDATE ITEM_DATA SET CASH = '' WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "'";
            this.DataDB.execSQL(this.SQL);
            Toast.makeText(this, "已成功的將 " + ITEM_NOTE.trim() + " 項目取消為顯示在桌面上!", Toast.LENGTH_SHORT).show();
        }
        if (this.ItemNoteRecPtr == null || this.ItemNoteRecPtr.equals("")) {
            ShowItemData(this.ItemClass, "");
        } else {
            setTitle(this.ItemNoteRecPtr.trim());
            ShowItemData(this.ItemClass, this.ItemNoteRecPtr.trim());
        }
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
        boolean SubItem;
        String ITEM_NOTE = this.SelectItemNote.getText().toString().trim();
        if (ITEM_NOTE.equals("")) {
            ShowBox("訊息提示", "您尚未選取項目資料!");
            return;
        }
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "SELECT * FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND PARENT_NOTE = '" + ITEM_NOTE.trim() + "' AND PARENT_NOTE <> ITEM_NOTE";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.cursor.close();
            SubItem = true;
        } else {
            this.cursor.close();
            SubItem = false;
        }
        if (SubItem) {
            this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND PARENT_NOTE = '" + ITEM_NOTE.trim() + "' AND PARENT_NOTE <> ITEM_NOTE";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            while (this.cursor2.moveToNext()) {
                this.SQL = "SELECT MAKE_NO FROM MYMONEY_DATA WHERE USER_ID = 'admin' And ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + this.cursor2.getString(0) + "'";
                this.cursor = this.DataDB.rawQuery(this.SQL, null);
                if (this.cursor.moveToNext()) {
                    this.cursor.close();
                    if (1 != 0) {
                        ShowBox("訊息提示", "【" + ITEM_NOTE.trim() + "】項目中的子項目【" + this.cursor2.getString(0) + "】已有輸入帳務記錄，您無法刪除此母項目資料!");
                        this.DataDB.close();
                        return;
                    }
                } else {
                    this.cursor.close();
                }
            }
            this.cursor2.close();
        }
        this.SQL = "SELECT * FROM MYMONEY_DATA WHERE USER_ID = 'admin' And ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext()) {
            this.cursor.close();
            this.DataDB.close();
            ShowBox("訊息提示", "【" + ITEM_NOTE.trim() + "】此項目已有記錄帳務資料，無法刪除");
            return;
        }
        this.cursor.close();
        this.DataDB.close();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("項目資料刪除");
        alert.setMessage("請問您是否要將 " + ITEM_NOTE.trim() + " 此筆項目資料刪除嗎?");
        alert.setPositiveButton("確定刪除", new DialogInterface.OnClickListener() { // from class: mymoney.zero.ItemSet.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) throws SQLException {
                ItemSet.this.KillData();
            }
        });
        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: mymoney.zero.ItemSet.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void KillData() throws SQLException {
        String ITEM_NOTE = this.SelectItemNote.getText().toString().trim();
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND PARENT_NOTE = '" + ITEM_NOTE.trim() + "'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        while (this.cursor.moveToNext()) {
            try {
                this.SQL = "DELETE FROM OFTEN_NOTE WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + this.ItemClass.trim() + "' AND ITEM_NOTE = '" + this.cursor.getString(0).trim() + "'";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e2) {
            }
            try {
                this.SQL = "DELETE FROM YEAR_SPEND WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + this.cursor.getString(0).trim() + "'";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e3) {
            }
            try {
                this.SQL = "DELETE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + this.ItemClass.trim() + "' AND ITEM_NOTE = '" + this.cursor.getString(0).trim() + "'";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e4) {
            }
        }
        this.cursor.close();
        try {
            this.SQL = "DELETE FROM OFTEN_NOTE WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + this.ItemClass.trim() + "' AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "'";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e5) {
        }
        try {
            this.SQL = "DELETE FROM YEAR_SPEND WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "'";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e6) {
        }
        try {
            this.SQL = "DELETE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '" + this.ItemClass.trim() + "' AND ITEM_NOTE = '" + ITEM_NOTE.trim() + "'";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e7) {
        }
        ShowItemData(this.ItemClass, "");
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
}