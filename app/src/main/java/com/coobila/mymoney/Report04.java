package com.coobila.mymoney;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableRow;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
//import org.achartengine.ChartFactory;
//import org.achartengine.model.CategorySeries;
//import org.achartengine.renderer.DefaultRenderer;
//import org.achartengine.renderer.SimpleSeriesRenderer;

/* loaded from: classes.dex */
public class Report04 extends TabActivity {
    private double AccountId;
    private SQLiteDatabase DataDB;
    private ListView DataList;
    private TextView EndDate;
    private TextView EndMountShow;
    private Spinner ItemNoteRec;
    private String SQL;
    private CheckBox ShowZeroItem;
    private CheckBox SumSubMount;
    private Cursor cursor;
    private Cursor cursor2;
    private Cursor cursor3;
    private TabHost tab_TabHost;
    private String AccountName = "";
    private String MountFormat = "";
    private int MountSub = 0;
    private int Loop_I = 0;
    private String ShowVibrate = "";
    private int ItemCount = 0;
    private DatePickerDialog.OnDateSetListener EndOnDateSetListener = new DatePickerDialog.OnDateSetListener() { // from class: mymoney.zero.Report04.1
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
            Report04.this.EndDate.setText(String.valueOf(NY) + "/" + NM + "/" + ND);
            if (!Report04.this.SumSubMount.isChecked()) {
                Report04.this.ShowData();
            } else {
                Report04.this.ShowData2();
            }
        }
    };

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

    @Override // android.app.ActivityGroup, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tab_TabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.report04, (ViewGroup) this.tab_TabHost.getTabContentView(), true);
        this.tab_TabHost.addTab(this.tab_TabHost.newTabSpec("tab1").setIndicator("文字顯示").setContent(R.id.showchart));
        this.tab_TabHost.addTab(this.tab_TabHost.newTabSpec("tab2").setIndicator("圖表顯示").setContent(R.id.showchart));
        this.tab_TabHost.setBackgroundColor(Color.parseColor("#F98421"));
        TabWidget tabWidget = this.tab_TabHost.getTabWidget();
        View child1 = tabWidget.getChildAt(0);
        View child2 = tabWidget.getChildAt(1);
        child1.setBackgroundResource(R.drawable.widgetbg1);
        child2.setBackgroundResource(R.drawable.widgetbg2);
        TextView tv1 = (TextView) child1.findViewById(android.R.id.title);
        TextView tv2 = (TextView) child2.findViewById(android.R.id.title);
        tv1.setTextColor(Color.parseColor("#FFFFFF"));
        tv2.setTextColor(Color.parseColor("#555555"));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int i = dm.heightPixels;
//        for (int i2 = 0; i2 < tabWidget.getChildCount(); i2++) {
//            View child = tabWidget.getChildAt(i2);
//            TextView tv = (TextView) child.findViewById(android.R.id.title);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
//            params.addRule(12, 0);
//            params.addRule(13, -1);
//            tabWidget.setStripEnabled(false);
//            tv.setTextSize(16.0f);
//            child.getLayoutParams().height = 45;
//            child.getLayoutParams().height = dpToPx(40);
//        }
        this.tab_TabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() { // from class: mymoney.zero.Report04.2
            @Override // android.widget.TabHost.OnTabChangeListener
            public void onTabChanged(String tabId) {
                LinearLayout layout = (LinearLayout) Report04.this.findViewById(R.id.showchart);
                LinearLayout top2 = (LinearLayout) Report04.this.findViewById(R.id.top2);
                TableRow top1 = (TableRow) Report04.this.findViewById(R.id.top1);
                TabWidget tabWidget2 = Report04.this.tab_TabHost.getTabWidget();
                if (tabId.equals("tab1")) {
                    layout.setVisibility(View.GONE);
                    top1.setVisibility(View.VISIBLE);
                    top2.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.VISIBLE);
                    top1.setVisibility(View.GONE);
                    top2.setVisibility(View.GONE);
                }
                for (int i3 = 0; i3 < Report04.this.tab_TabHost.getChildCount(); i3++) {
                    View child12 = tabWidget2.getChildAt(0);
                    View child22 = tabWidget2.getChildAt(1);
                    TextView tv12 = (TextView) child12.findViewById(android.R.id.title);
                    TextView tv22 = (TextView) child22.findViewById(android.R.id.title);
                    if (tabId.equals("tab1")) {
                        child12.setBackgroundDrawable(Report04.this.getResources().getDrawable(R.drawable.widgetbg1));
                        child22.setBackgroundDrawable(Report04.this.getResources().getDrawable(R.drawable.widgetbg2));
                        tv12.setTextColor(Color.parseColor("#FFFFFF"));
                        tv22.setTextColor(Color.parseColor("#555555"));
                    } else {
                        child12.setBackgroundDrawable(Report04.this.getResources().getDrawable(R.drawable.widgetbg2));
                        child22.setBackgroundDrawable(Report04.this.getResources().getDrawable(R.drawable.widgetbg1));
                        tv12.setTextColor(Color.parseColor("#555555"));
                        tv22.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
            }
        });
        this.EndDate = (TextView) findViewById(R.id.EndDate);
        this.AccountName = "";
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.DataList = (ListView) findViewById(R.id.DataList);
        this.EndMountShow = (TextView) findViewById(R.id.EndMountShow);
        this.ItemNoteRec = (Spinner) findViewById(R.id.ItemNoteRec);
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
        setTitle(String.valueOf(this.AccountName) + " - 資產項目餘額表");
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
        GetNowDate GetNow = new GetNowDate();
        this.EndDate.setText(GetNow.GetDate().toString());
        String GetEndDate = "";
        Bundle bundle = getIntent().getExtras();
        try {
            GetEndDate = bundle.getString("EndDate");
        } catch (Exception e2) {
        }
        if (GetEndDate == null) {
            GetEndDate = "";
        }
        if (!GetEndDate.equals("")) {
            this.EndDate.setText(GetEndDate);
        }
        this.DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: mymoney.zero.Report04.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Report04.this.ItemNoteRec.setSelection(position);
                Intent intent = new Intent();
                Bundle bundle2 = new Bundle();
                bundle2.putString("StartDate", String.valueOf(Report04.this.EndDate.getText().toString().substring(0, 8)) + "01");
                bundle2.putString("EndDate", Report04.this.EndDate.getText().toString());
                bundle2.putString("ItemNote", Report04.this.ItemNoteRec.getSelectedItem().toString());
                intent.putExtras(bundle2);
                intent.setClass(Report04.this, AssetDetailShow.class);
                Report04.this.startActivity(intent);
                Report04.this.finish();
            }
        });
        ListView gridView = (ListView) findViewById(R.id.DataList);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        FrameLayout.LayoutParams gridview_params = new FrameLayout.LayoutParams(-2, -2);
        gridview_params.height = metrics.heightPixels - dpToPx(261);
        gridView.setLayoutParams(gridview_params);
        this.SumSubMount = (CheckBox) findViewById(R.id.SumSubMount);
        this.ShowZeroItem = (CheckBox) findViewById(R.id.ShowZeroItem);
        this.SumSubMount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: mymoney.zero.Report04.4
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!Report04.this.SumSubMount.isChecked()) {
                    Report04.this.ShowData();
                } else {
                    Report04.this.ShowData2();
                }
            }
        });
        this.ShowZeroItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: mymoney.zero.Report04.5
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!Report04.this.SumSubMount.isChecked()) {
                    Report04.this.ShowData();
                } else {
                    Report04.this.ShowData2();
                }
            }
        });
        if (!this.SumSubMount.isChecked()) {
            ShowData();
        } else {
            ShowData2();
        }
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public int pxTodp(int pxValue) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) ((pxValue / density) + 0.5f);
    }

    public void ShowData() {
        double NowMount;
        double NowMount2;
        double NowMount3;
        double EndMount = 0.0d;
        DecimalFormat AllMountDf = new DecimalFormat("#,##0");
        this.ItemCount = 0;
        String[] ShowItem = new String[150];
        double[] ShowData = new double[150];
        int[] colors = {Color.parseColor("#FF19FC"), Color.parseColor("#FF1C19"), Color.parseColor("#FF8F19"), Color.parseColor("#FCFF19"), Color.parseColor("#89FF19"), Color.parseColor("#19FF8F"), Color.parseColor("#19FCFF"), Color.parseColor("#1989FF"), Color.parseColor("#1C19FF"), Color.parseColor("#8F19FF"), Color.parseColor("#FA00F7"), Color.parseColor("#FA0300"), Color.parseColor("#FA8000"), Color.parseColor("#F7FA00"), Color.parseColor("#7AFA00"), Color.parseColor("#00FA80"), Color.parseColor("#00F7FA"), Color.parseColor("#007AFA"), Color.parseColor("#0300FA"), Color.parseColor("#8000FA"), Color.parseColor("#DC00D9"), Color.parseColor("#DC0300"), Color.parseColor("#DC7100"), Color.parseColor("#D9DC00"), Color.parseColor("#6BDC00"), Color.parseColor("#00DC71"), Color.parseColor("#00D9DC"), Color.parseColor("#006BDC"), Color.parseColor("#0300DC"), Color.parseColor("#7100DC"), Color.parseColor("#BE00BC"), Color.parseColor("#BE0200"), Color.parseColor("#BE6100"), Color.parseColor("#BCBE00"), Color.parseColor("#5DBE00"), Color.parseColor("#00BE61"), Color.parseColor("#00BCBE"), Color.parseColor("#005DBE"), Color.parseColor("#0200BE"), Color.parseColor("#6100BE"), Color.parseColor("#A0009E"), Color.parseColor("#A00200"), Color.parseColor("#A05200"), Color.parseColor("#9EA000"), Color.parseColor("#4EA000"), Color.parseColor("#00A052"), Color.parseColor("#009EA0"), Color.parseColor("#004EA0"), Color.parseColor("#0200A0"), Color.parseColor("#5200A0"), Color.parseColor("#A0004E"), Color.parseColor("#00A002"), Color.parseColor("#BE005D"), Color.parseColor("#00BE02"), Color.parseColor("#DC006B"), Color.parseColor("#00DC03"), Color.parseColor("#FA007A"), Color.parseColor("#00FA03"), Color.parseColor("#FF1989"), Color.parseColor("#19FF1C"), Color.parseColor("#FF37FD"), Color.parseColor("#FF3937"), Color.parseColor("#FF9D37"), Color.parseColor("#FDFF37"), Color.parseColor("#99FF37"), Color.parseColor("#37FF9D"), Color.parseColor("#37FDFF"), Color.parseColor("#3799FF"), Color.parseColor("#3937FF"), Color.parseColor("#9D37FF"), Color.parseColor("#FF3799"), Color.parseColor("#37FF39"), Color.parseColor("#FF55FD"), Color.parseColor("#FF5755"), Color.parseColor("#FFAC55"), Color.parseColor("#FDFF55"), Color.parseColor("#A8FF55"), Color.parseColor("#55FFAC"), Color.parseColor("#55FDFF"), Color.parseColor("#55A8FF"), Color.parseColor("#5755FF"), Color.parseColor("#AC55FF"), Color.parseColor("#FF55A8"), Color.parseColor("#55FF57"), Color.parseColor("#FF73FD"), Color.parseColor("#FF7573"), Color.parseColor("#FFBB73"), Color.parseColor("#FDFF73"), Color.parseColor("#B7FF73"), Color.parseColor("#73FFBB"), Color.parseColor("#73FDFF"), Color.parseColor("#73B7FF"), Color.parseColor("#7573FF"), Color.parseColor("#BB73FF"), Color.parseColor("#FF73B7"), Color.parseColor("#73FF75"), Color.parseColor("#FF91FE"), Color.parseColor("#FF9291"), Color.parseColor("#FFC991"), Color.parseColor("#FEFF91"), Color.parseColor("#C7FF91"), Color.parseColor("#91FFC9"), Color.parseColor("#91FEFF"), Color.parseColor("#91C7FF"), Color.parseColor("#9291FF"), Color.parseColor("#C991FF"), Color.parseColor("#FF91C7"), Color.parseColor("#91FF92"), Color.parseColor("#FFAFFE"), Color.parseColor("#FFB0AF"), Color.parseColor("#FFD8AF"), Color.parseColor("#FEFFAF"), Color.parseColor("#D6FFAF"), Color.parseColor("#AFFFD8"), Color.parseColor("#AFFEFF"), Color.parseColor("#AFD6FF"), Color.parseColor("#B0AFFF"), Color.parseColor("#D8AFFF"), Color.parseColor("#FFAFD6"), Color.parseColor("#AFFFB0"), Color.parseColor("#FFCDFE"), Color.parseColor("#FFCECD"), Color.parseColor("#FFE7CD"), Color.parseColor("#FEFFCD"), Color.parseColor("#E5FFCD"), Color.parseColor("#CDFFE7"), Color.parseColor("#CDFEFF"), Color.parseColor("#CDE5FF"), Color.parseColor("#CECDFF"), Color.parseColor("#E7CDFF"), Color.parseColor("#FFCDE5"), Color.parseColor("#CDFFCE"), Color.parseColor("#FFEBFF"), Color.parseColor("#FFEBEB"), Color.parseColor("#FFF5EB"), Color.parseColor("#FFFFEB"), Color.parseColor("#F5FFEB"), Color.parseColor("#EBFFF5"), Color.parseColor("#EBFFFF"), Color.parseColor("#EBF5FF"), Color.parseColor("#EBEBFF"), Color.parseColor("#F5EBFF"), Color.parseColor("#FFEBF5"), Color.parseColor("#EBFFEB"), 0, 0, 0, 0, 0, 0};
//        CategorySeries categorySeries = new CategorySeries("Vehicles Chart");
        DecimalFormat MountDf = new DecimalFormat("#,##0" + this.MountFormat);
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        ArrayList<String> ItemListData = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ItemListData);
        this.ItemNoteRec.setAdapter((SpinnerAdapter) adapter);
        this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '資產' AND ITEM_NOTE = PARENT_NOTE AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        while (this.cursor.moveToNext()) {
            double Exchange = this.cursor.getDouble(2);
            double BeforeMount = this.cursor.getDouble(1) * Exchange;
            this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + this.cursor.getString(0).toString() + "' AND DATA_DATE <= '" + ((Object) this.EndDate.getText()) + "'";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            if (this.cursor2.moveToNext()) {
                NowMount = this.cursor2.getDouble(0) * Exchange;
            } else {
                NowMount = 0.0d;
            }
            this.cursor2.close();
            double ItemMount = BeforeMount + NowMount;
            this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '資產' AND PARENT_NOTE <> ITEM_NOTE AND PARENT_NOTE = '" + this.cursor.getString(0).toString() + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            int SubItem = 0;
            while (this.cursor2.moveToNext()) {
                SubItem = 1;
                double Exchange2 = this.cursor2.getDouble(2);
                double ItemMount2 = ItemMount + (this.cursor2.getDouble(1) * Exchange2);
                this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + this.cursor2.getString(0).toString() + "' AND DATA_DATE <= '" + ((Object) this.EndDate.getText()) + "'";
                this.cursor3 = this.DataDB.rawQuery(this.SQL, null);
                if (this.cursor3.moveToNext()) {
                    NowMount3 = this.cursor3.getDouble(0) * Exchange2;
                } else {
                    NowMount3 = 0.0d;
                }
                ItemMount = ItemMount2 + NowMount3;
                this.cursor3.close();
            }
            this.cursor2.close();
            if (ItemMount != 0.0d || this.ShowZeroItem.isChecked()) {
                String ShowMount = MountDf.format(ItemMount);
                HashMap<String, Object> map = new HashMap<>();
                map.put("ItemImage", Integer.valueOf(R.drawable.aa));
                map.put("ItemNote", this.cursor.getString(0).toString());
                map.put("Mount", "$" + ShowMount);
                ItemListData.add(this.cursor.getString(0).toString());
                listItem.add(map);
            }
            if (SubItem == 0 && ItemMount != 0.0d) {
                ShowItem[this.ItemCount] = this.cursor.getString(0);
                ShowData[this.ItemCount] = ItemMount;
                this.ItemCount++;
            }
            EndMount += ItemMount;
            this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '資產' AND PARENT_NOTE <> ITEM_NOTE AND PARENT_NOTE = '" + this.cursor.getString(0).toString() + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            while (this.cursor2.moveToNext()) {
                double Exchange3 = this.cursor2.getDouble(2);
                double BeforeMount2 = this.cursor2.getDouble(1) * Exchange3;
                this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + this.cursor2.getString(0).toString() + "' AND DATA_DATE <= '" + ((Object) this.EndDate.getText()) + "'";
                this.cursor3 = this.DataDB.rawQuery(this.SQL, null);
                if (this.cursor3.moveToNext()) {
                    NowMount2 = this.cursor3.getDouble(0) * Exchange3;
                } else {
                    NowMount2 = 0.0d;
                }
                this.cursor3.close();
                double ItemMount3 = BeforeMount2 + NowMount2;
                if (ItemMount3 != 0.0d || this.ShowZeroItem.isChecked()) {
                    String ShowMount2 = MountDf.format(ItemMount3);
                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("ItemImage2", Integer.valueOf(R.drawable.aa));
                    map1.put("ItemNote", "    " + this.cursor2.getString(0).toString());
                    map1.put("Mount2", "$" + ShowMount2);
                    ItemListData.add(this.cursor2.getString(0).toString());
                    listItem.add(map1);
                }
                if (ItemMount3 != 0.0d) {
                    ShowItem[this.ItemCount] = this.cursor2.getString(0);
                    ShowData[this.ItemCount] = ItemMount3;
                    this.ItemCount++;
                }
            }
            this.cursor2.close();
        }
        this.cursor.close();
        this.DataDB.close();
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.report04listitem, new String[]{"ItemImage", "ItemImage2", "ItemNote", "Mount", "Mount2"}, new int[]{R.id.ItemImage, R.id.ItemImage2, R.id.ItemNote, R.id.Mount, R.id.Mount2});
        this.DataList.setAdapter((ListAdapter) listItemAdapter);
        String ShowMount3 = MountDf.format(EndMount);
        this.EndMountShow.setText("$" + ShowMount3);
//        for (int i = 0; i <= this.ItemCount - 1; i++) {
//            categorySeries.add(String.valueOf(ShowItem[i]) + "(" + AllMountDf.format((ShowData[i] / EndMount) * 100.0d) + "%)", ShowData[i]);
//        }
//        LinearLayout layout = (LinearLayout) findViewById(R.id.showchart);
//        layout.removeAllViews();
//        DefaultRenderer renderer = buildCategoryRenderer(colors);
//        layout.addView(ChartFactory.getPieChartView(getBaseContext(), categorySeries, renderer));
    }

    public void ShowData2() {
        double NowMount;
        double NowMount2;
        double NowMount3;
        double EndMount = 0.0d;
        DecimalFormat AllMountDf = new DecimalFormat("#,##0");
        this.ItemCount = 0;
        String[] ShowItem = new String[150];
        double[] ShowData = new double[150];
        int[] colors = {Color.parseColor("#FF19FC"), Color.parseColor("#FF1C19"), Color.parseColor("#FF8F19"), Color.parseColor("#FCFF19"), Color.parseColor("#89FF19"), Color.parseColor("#19FF8F"), Color.parseColor("#19FCFF"), Color.parseColor("#1989FF"), Color.parseColor("#1C19FF"), Color.parseColor("#8F19FF"), Color.parseColor("#FA00F7"), Color.parseColor("#FA0300"), Color.parseColor("#FA8000"), Color.parseColor("#F7FA00"), Color.parseColor("#7AFA00"), Color.parseColor("#00FA80"), Color.parseColor("#00F7FA"), Color.parseColor("#007AFA"), Color.parseColor("#0300FA"), Color.parseColor("#8000FA"), Color.parseColor("#DC00D9"), Color.parseColor("#DC0300"), Color.parseColor("#DC7100"), Color.parseColor("#D9DC00"), Color.parseColor("#6BDC00"), Color.parseColor("#00DC71"), Color.parseColor("#00D9DC"), Color.parseColor("#006BDC"), Color.parseColor("#0300DC"), Color.parseColor("#7100DC"), Color.parseColor("#BE00BC"), Color.parseColor("#BE0200"), Color.parseColor("#BE6100"), Color.parseColor("#BCBE00"), Color.parseColor("#5DBE00"), Color.parseColor("#00BE61"), Color.parseColor("#00BCBE"), Color.parseColor("#005DBE"), Color.parseColor("#0200BE"), Color.parseColor("#6100BE"), Color.parseColor("#A0009E"), Color.parseColor("#A00200"), Color.parseColor("#A05200"), Color.parseColor("#9EA000"), Color.parseColor("#4EA000"), Color.parseColor("#00A052"), Color.parseColor("#009EA0"), Color.parseColor("#004EA0"), Color.parseColor("#0200A0"), Color.parseColor("#5200A0"), Color.parseColor("#A0004E"), Color.parseColor("#00A002"), Color.parseColor("#BE005D"), Color.parseColor("#00BE02"), Color.parseColor("#DC006B"), Color.parseColor("#00DC03"), Color.parseColor("#FA007A"), Color.parseColor("#00FA03"), Color.parseColor("#FF1989"), Color.parseColor("#19FF1C"), Color.parseColor("#FF37FD"), Color.parseColor("#FF3937"), Color.parseColor("#FF9D37"), Color.parseColor("#FDFF37"), Color.parseColor("#99FF37"), Color.parseColor("#37FF9D"), Color.parseColor("#37FDFF"), Color.parseColor("#3799FF"), Color.parseColor("#3937FF"), Color.parseColor("#9D37FF"), Color.parseColor("#FF3799"), Color.parseColor("#37FF39"), Color.parseColor("#FF55FD"), Color.parseColor("#FF5755"), Color.parseColor("#FFAC55"), Color.parseColor("#FDFF55"), Color.parseColor("#A8FF55"), Color.parseColor("#55FFAC"), Color.parseColor("#55FDFF"), Color.parseColor("#55A8FF"), Color.parseColor("#5755FF"), Color.parseColor("#AC55FF"), Color.parseColor("#FF55A8"), Color.parseColor("#55FF57"), Color.parseColor("#FF73FD"), Color.parseColor("#FF7573"), Color.parseColor("#FFBB73"), Color.parseColor("#FDFF73"), Color.parseColor("#B7FF73"), Color.parseColor("#73FFBB"), Color.parseColor("#73FDFF"), Color.parseColor("#73B7FF"), Color.parseColor("#7573FF"), Color.parseColor("#BB73FF"), Color.parseColor("#FF73B7"), Color.parseColor("#73FF75"), Color.parseColor("#FF91FE"), Color.parseColor("#FF9291"), Color.parseColor("#FFC991"), Color.parseColor("#FEFF91"), Color.parseColor("#C7FF91"), Color.parseColor("#91FFC9"), Color.parseColor("#91FEFF"), Color.parseColor("#91C7FF"), Color.parseColor("#9291FF"), Color.parseColor("#C991FF"), Color.parseColor("#FF91C7"), Color.parseColor("#91FF92"), Color.parseColor("#FFAFFE"), Color.parseColor("#FFB0AF"), Color.parseColor("#FFD8AF"), Color.parseColor("#FEFFAF"), Color.parseColor("#D6FFAF"), Color.parseColor("#AFFFD8"), Color.parseColor("#AFFEFF"), Color.parseColor("#AFD6FF"), Color.parseColor("#B0AFFF"), Color.parseColor("#D8AFFF"), Color.parseColor("#FFAFD6"), Color.parseColor("#AFFFB0"), Color.parseColor("#FFCDFE"), Color.parseColor("#FFCECD"), Color.parseColor("#FFE7CD"), Color.parseColor("#FEFFCD"), Color.parseColor("#E5FFCD"), Color.parseColor("#CDFFE7"), Color.parseColor("#CDFEFF"), Color.parseColor("#CDE5FF"), Color.parseColor("#CECDFF"), Color.parseColor("#E7CDFF"), Color.parseColor("#FFCDE5"), Color.parseColor("#CDFFCE"), Color.parseColor("#FFEBFF"), Color.parseColor("#FFEBEB"), Color.parseColor("#FFF5EB"), Color.parseColor("#FFFFEB"), Color.parseColor("#F5FFEB"), Color.parseColor("#EBFFF5"), Color.parseColor("#EBFFFF"), Color.parseColor("#EBF5FF"), Color.parseColor("#EBEBFF"), Color.parseColor("#F5EBFF"), Color.parseColor("#FFEBF5"), Color.parseColor("#EBFFEB"), 0, 0, 0, 0, 0, 0};
//        CategorySeries categorySeries = new CategorySeries("Vehicles Chart");
        DecimalFormat MountDf = new DecimalFormat("#,##0" + this.MountFormat);
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        ArrayList<String> ItemListData = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ItemListData);
        this.ItemNoteRec.setAdapter((SpinnerAdapter) adapter);
        this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '資產' AND ITEM_NOTE = PARENT_NOTE AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        while (this.cursor.moveToNext()) {
            double Exchange = this.cursor.getDouble(2);
            double BeforeMount = this.cursor.getDouble(1) * Exchange;
            this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + this.cursor.getString(0).toString() + "' AND DATA_DATE <= '" + ((Object) this.EndDate.getText()) + "'";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            if (this.cursor2.moveToNext()) {
                NowMount = this.cursor2.getDouble(0) * Exchange;
            } else {
                NowMount = 0.0d;
            }
            this.cursor2.close();
            double ItemMount = BeforeMount + NowMount;
            this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '資產' AND PARENT_NOTE <> ITEM_NOTE AND PARENT_NOTE = '" + this.cursor.getString(0).toString() + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            while (this.cursor2.moveToNext()) {
                double Exchange2 = this.cursor2.getDouble(2);
                double ItemMount2 = ItemMount + (this.cursor2.getDouble(1) * Exchange2);
                this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + this.cursor2.getString(0).toString() + "' AND DATA_DATE <= '" + ((Object) this.EndDate.getText()) + "'";
                this.cursor3 = this.DataDB.rawQuery(this.SQL, null);
                if (this.cursor3.moveToNext()) {
                    NowMount3 = this.cursor3.getDouble(0) * Exchange2;
                } else {
                    NowMount3 = 0.0d;
                }
                ItemMount = ItemMount2 + NowMount3;
                this.cursor3.close();
            }
            this.cursor2.close();
            if (ItemMount != 0.0d || this.ShowZeroItem.isChecked()) {
                String ShowMount = MountDf.format(ItemMount);
                HashMap<String, Object> map = new HashMap<>();
                map.put("ItemImage", Integer.valueOf(R.drawable.aa));
                map.put("ItemNote", this.cursor.getString(0).toString());
                map.put("Mount", "$" + ShowMount);
                ItemListData.add(this.cursor.getString(0).toString());
                listItem.add(map);
            }
            if (ItemMount != 0.0d) {
                ShowItem[this.ItemCount] = this.cursor.getString(0);
                ShowData[this.ItemCount] = ItemMount;
                this.ItemCount++;
            }
            EndMount += ItemMount;
            this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '資產' AND PARENT_NOTE <> ITEM_NOTE AND PARENT_NOTE = '" + this.cursor.getString(0).toString() + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
            this.cursor2 = this.DataDB.rawQuery(this.SQL, null);
            while (this.cursor2.moveToNext()) {
                double Exchange3 = this.cursor2.getDouble(2);
                double BeforeMount2 = this.cursor2.getDouble(1) * Exchange3;
                this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + this.cursor2.getString(0).toString() + "' AND DATA_DATE <= '" + ((Object) this.EndDate.getText()) + "'";
                this.cursor3 = this.DataDB.rawQuery(this.SQL, null);
                if (this.cursor3.moveToNext()) {
                    NowMount2 = this.cursor3.getDouble(0) * Exchange3;
                } else {
                    NowMount2 = 0.0d;
                }
                this.cursor3.close();
                double ItemMount3 = BeforeMount2 + NowMount2;
                String ShowMount2 = MountDf.format(ItemMount3);
                HashMap<String, Object> map1 = new HashMap<>();
                if (ItemMount3 != 0.0d) {
                    map1.put("ItemImage2", Integer.valueOf(R.drawable.aa));
                    map1.put("ItemNote", "    " + this.cursor2.getString(0).toString());
                    map1.put("Mount2", "$" + ShowMount2);
                }
            }
            this.cursor2.close();
        }
        this.cursor.close();
        this.DataDB.close();
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.report04listitem, new String[]{"ItemImage", "ItemImage2", "ItemNote", "Mount", "Mount2"}, new int[]{R.id.ItemImage, R.id.ItemImage2, R.id.ItemNote, R.id.Mount, R.id.Mount2});
        this.DataList.setAdapter((ListAdapter) listItemAdapter);
        String ShowMount3 = MountDf.format(EndMount);
        this.EndMountShow.setText("$" + ShowMount3);
//        for (int i = 0; i <= this.ItemCount - 1; i++) {
//            categorySeries.add(String.valueOf(ShowItem[i]) + "(" + AllMountDf.format((ShowData[i] / EndMount) * 100.0d) + "%)", ShowData[i]);
//        }
//        LinearLayout layout = (LinearLayout) findViewById(R.id.showchart);
//        layout.removeAllViews();
//        DefaultRenderer renderer = buildCategoryRenderer(colors);
//        layout.addView(ChartFactory.getPieChartView(getBaseContext(), categorySeries, renderer));
    }

//    protected DefaultRenderer buildCategoryRenderer(int[] colors) {
//        DefaultRenderer renderer = new DefaultRenderer();
//        for (int color = 0; color <= this.ItemCount - 1; color++) {
//            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
//            r.setColor(colors[color]);
//            renderer.setLabelsTextSize(12.0f);
//            renderer.setLegendTextSize(12.0f);
//            DisplayMetrics dm = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
//            int vHeight = dm.heightPixels;
//            if (vHeight > 600) {
//                renderer.setLabelsTextSize(18.0f);
//                renderer.setLegendTextSize(18.0f);
//            }
//            if (vHeight > 900) {
//                renderer.setLabelsTextSize(24.0f);
//                renderer.setLegendTextSize(24.0f);
//            }
//            renderer.setLabelsColor(Color.parseColor("#000000"));
//            renderer.setShowLegend(false);
//            renderer.setZoomButtonsVisible(true);
//            renderer.setZoomEnabled(true);
//            renderer.setPanEnabled(true);
//            renderer.setFitLegend(true);
//            renderer.addSeriesRenderer(r);
//        }
//        return renderer;
//    }

    public void ShowBox(String TitleString, String BodyString) {
        if (TitleString == "") {
            TitleString = "訊息";
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle(TitleString);
        MyAlertDialog.setMessage(BodyString);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.Report04.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyAlertDialog.setPositiveButton("確定", OkClick);
        MyAlertDialog.show();
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
        intent.setClass(this, ReportView.class);
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
                intent.setClass(this, ReportView.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("ProgNo", 3.0d);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void EndDateSelect(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        String TempDate = this.EndDate.getText().toString();
        double y = Double.valueOf(TempDate.substring(0, 4)).doubleValue();
        double m = Double.valueOf(TempDate.substring(5, 7)).doubleValue() - 1.0d;
        double d = Double.valueOf(TempDate.substring(8, 10)).doubleValue();
        DatePickerDialog dpd = new DatePickerDialog(this, this.EndOnDateSetListener, (int) y, (int) m, (int) d);
        dpd.show();
    }
}