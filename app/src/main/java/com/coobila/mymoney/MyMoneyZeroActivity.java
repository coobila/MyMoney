package com.coobila.mymoney;

import static android.widget.Toast.*;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class MyMoneyZeroActivity extends AppCompatActivity {
    public static ArrayList<Activity> activityList = new ArrayList<>();
    private double AccountId;
    private String AccountPassword;
    private SQLiteDatabase DataDB;
    private ListView DataList;
    private TextView DayOutMountView;
    private TextView DownMonth;
    private TextView InOutMountView;
    private String InputPassword;
    private TextView MonthOutMountView;
    private TextView NowMonthShow;
    private String SQL;
    private TextView UpMonth;
    private TextView WeekOutMountView;
    private int MountSub = 0;
    private int ExchangeSub = 0;
    private int Loop_I = 0;
    private String MountFormat = "";
    private String ExchangeFormat = "";
    private String AccountName = "";
    private String AccountPasswordMessage = "";
    private String ShowVibrate = "";
    private String Week = "";
    private Boolean mLongPressed = false;
    private String WeekStartDate = "";
    private String WeekEndDate = "";
    double DayOutMount = 0.0d;
    double WeekOutMount = 0.0d;
    double MonthOutMount = 0.0d;
    double MonthInMount = 0.0d;
    double ErrorPassword = 0.0d;
    boolean ShowMount1 = false;
    boolean ShowMount2 = false;
    boolean ShowMount3 = false;
    boolean ShowMount4 = false;
    boolean ShowMount5 = false;
    boolean SumOtherMount = true;
    boolean LockShowAdLocus = false;
    int EndCountStyle = 0;

    private static Context context;

    //建立並顯示 Activity 頂部的選項選單
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (!this.AccountPassword.trim().equals(this.InputPassword.trim()) && !this.AccountPassword.trim().equals("")) {
            ShowInputPassword();
        } else {
            makeText(this, "系統訊息：程式更新調整中，此功能暫時停止使用!", LENGTH_LONG).show();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityList.remove(this);
    }

    //這個方法 onConfigurationChanged 是 Android Activity 生命週期的一部分。當裝置的設定發生變化時（例如螢幕方向、鍵盤可用性、語言等），系統會呼叫這個方法。
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == 2) {
            GridView gridView = (GridView) findViewById(R.id.gridview);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            FrameLayout.LayoutParams gridview_params = new FrameLayout.LayoutParams(-2, -2);
            gridview_params.height = dpToPx(180);
            gridView.setLayoutParams(gridview_params);
            return;
        }
        if (newConfig.orientation == 1) {
            GridView gridView2 = (GridView) findViewById(R.id.gridview);
            DisplayMetrics metrics2 = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics2);
            FrameLayout.LayoutParams gridview_params2 = new FrameLayout.LayoutParams(-2, -2);
            gridview_params2.height = dpToPx(275);
            gridView2.setLayoutParams(gridview_params2);
        }
    }

    public static Context getAppContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) throws SQLException {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();//給全域變數
        setContentView(R.layout.main);

        MaterialToolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        Log.d("kevin -test----------", "onCreate------------");
        activityList.add(this);
        File dir = getFilesDir();

        //判斷有無資料庫
        int OpenDataBase = 0;
        try {
            OpenDataBase = CreateDatabase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        //Export
//        String tSDCardPath = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/MyMoneyZero/Export/";
//        File tDataPath = new File(tSDCardPath);
//        try {
//            tDataPath.mkdirs();
//        } catch (Exception e) {
//        }
//        String DataFileDir = dir.getPath();
//        File fd = new File(String.valueOf(DataFileDir) + "/");
//        try {
//            String[] dirarray = fd.list();
//            Arrays.sort(dirarray);
//            for (int i = 0; i < dirarray.length; i++) {
//                if (!dirarray[i].trim().equals("mymoney.db")) {
//                    try {
//                        File KillFile = new File(String.valueOf(DataFileDir) + "/" + dirarray[i]);
//                        KillFile.delete();
//                    } catch (Exception e2) {
//                    }
//                }
//            }
//        } catch (Exception e3) {
//        }

//        //DataBack
//        String DataFileDir2 = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/MyMoneyZero/DataBack";
//        File fd2 = new File(String.valueOf(DataFileDir2) + "/");
//        try {
//            String[] dirarray2 = fd2.list();
//            Arrays.sort(dirarray2);
//            if (dirarray2.length > 7) {
//                for (int i2 = 0; i2 < dirarray2.length - 7; i2++) {
//                    if (!dirarray2[i2].trim().equals("mymoney.db")) {
//                        try {
//                            File KillFile2 = new File(String.valueOf(DataFileDir2) + "/" + dirarray2[i2]);
//                            KillFile2.delete();
//                        } catch (Exception e4) {
//                        }
//                    }
//                }
//            }
//        } catch (Exception e5) {
//        }

        ErrorPassword = 1.0d;
        GridView gridview = this.findViewById(R.id.gridview);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("ItemImage", Integer.valueOf(R.drawable.addout));
        map1.put("ItemText", "新增支出");
        lstImageItem.add(map1);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("ItemImage", Integer.valueOf(R.drawable.addin));
        map2.put("ItemText", "新增收入");
        lstImageItem.add(map2);
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("ItemImage", Integer.valueOf(R.drawable.refresh));
        map3.put("ItemText", "資產轉帳");
        lstImageItem.add(map3);
        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("ItemImage", Integer.valueOf(R.drawable.edit));
        map4.put("ItemText", "帳務記錄");
        lstImageItem.add(map4);
        HashMap<String, Object> map5 = new HashMap<>();
        map5.put("ItemImage", Integer.valueOf(R.drawable.chart));
        map5.put("ItemText", "統計報表");
        lstImageItem.add(map5);
        HashMap<String, Object> map6 = new HashMap<>();
        map6.put("ItemImage", Integer.valueOf(R.drawable.tools));
        map6.put("ItemText", "系統設定");
        lstImageItem.add(map6);
        HashMap<String, Object> map7 = new HashMap<>();
        map7.put("ItemImage", Integer.valueOf(R.drawable.book));
        map7.put("ItemText", "帳本選擇");
        lstImageItem.add(map7);
        HashMap<String, Object> map8 = new HashMap<>();
        map8.put("ItemImage", Integer.valueOf(R.drawable.itemset));
        map8.put("ItemText", "項目設定");
        lstImageItem.add(map8);
        HashMap<String, Object> map9 = new HashMap<>();
        map9.put("ItemImage", Integer.valueOf(R.drawable.exit));
        map9.put("ItemText", "結束離開");
        lstImageItem.add(map9);
        SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem, R.layout.grid_view_item, new String[]{"ItemImage", "ItemText"}, new int[]{R.id.ItemImage, R.id.ItemText});
        gridview.setAdapter((ListAdapter) saImageItems);
        gridview.setOnItemClickListener(new ItemClickListener());
        //檢查裝置目前的螢幕方向是否為橫向
        //1: 代表 直向 (Portrait);2: 代表 橫向 (Landscape)
        if (getResources().getConfiguration().orientation == 2) {
            GridView gridView = (GridView) findViewById(R.id.gridview);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            FrameLayout.LayoutParams gridview_params = new FrameLayout.LayoutParams(-2, -2);
            gridview_params.height = dpToPx(180);
            gridView.setLayoutParams(gridview_params);
        } else if (getResources().getConfiguration().orientation == 1) {
            GridView gridView2 = (GridView) findViewById(R.id.gridview);
            DisplayMetrics metrics2 = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics2);
            FrameLayout.LayoutParams gridview_params2 = new FrameLayout.LayoutParams(-2, -2);
            gridview_params2.height = dpToPx(275);
            gridView2.setLayoutParams(gridview_params2);
        }

        //資料庫有資料則顯示
        if (OpenDataBase == 0) {
            ShowData();
        }
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    class ItemClickListener implements AdapterView.OnItemClickListener {
        ItemClickListener() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            HashMap<String, Object> item = (HashMap) arg0.getItemAtPosition(arg2);
            if (item.get("ItemText").equals("新增支出")) {
                if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(30L);
                    } catch (Exception e) {
                    }
                }
                if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                    MyMoneyZeroActivity.this.ShowInputPassword();
                } else {
//                    Intent intent1 = new Intent();
//                    intent1.setClass(MyMoneyZeroActivity.this, Addout.class);
//                    MyMoneyZeroActivity.this.startActivity(intent1);
                    MyMoneyZeroActivity.this.finish();
                }
            }
            if (item.get("ItemText").equals("新增收入")) {
                if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator2 = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator2.vibrate(30L);
                    } catch (Exception e2) {
                    }
                }
                if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                    MyMoneyZeroActivity.this.ShowInputPassword();
                } else {
//                    Intent intent12 = new Intent();
//                    intent12.setClass(MyMoneyZeroActivity.this, Addin.class);
//                    MyMoneyZeroActivity.this.startActivity(intent12);
//                    MyMoneyZeroActivity.this.finish();
                }
            }
            if (item.get("ItemText").equals("資產轉帳")) {
                if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator3 = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator3.vibrate(30L);
                    } catch (Exception e3) {
                    }
                }
                if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                    MyMoneyZeroActivity.this.ShowInputPassword();
                } else {
//                    Intent intent13 = new Intent();
//                    intent13.setClass(MyMoneyZeroActivity.this, Addtransfer.class);
//                    MyMoneyZeroActivity.this.startActivity(intent13);
//                    MyMoneyZeroActivity.this.finish();
                }
            }
            //帳務記錄
            if (item.get("ItemText").equals("帳務記錄")) {
                if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator4 = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator4.vibrate(30L);
                    } catch (Exception e4) {
                    }
                }
                if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                    MyMoneyZeroActivity.this.ShowInputPassword();
                } else {
                    Intent intent14 = new Intent();
//                    String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath = String.valueOf(getExternalFilesDir(null));
                    try {
                        MyMoneyZeroActivity.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                    } catch (Exception e5) {
                    }
                    MyMoneyZeroActivity.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(MyMoneyZeroActivity.this.AccountId) + " AND DATA_NOTE = '帳務記錄顯示發票號碼'";
                    Cursor cursor = MyMoneyZeroActivity.this.DataDB.rawQuery(MyMoneyZeroActivity.this.SQL, null);
                    if (cursor.moveToNext() && cursor.getString(0).trim().endsWith("1")) {
                        cursor.close();
                        intent14.setClass(MyMoneyZeroActivity.this, DayInOutShow2.class);
                    } else {
                        cursor.close();
                        intent14.setClass(MyMoneyZeroActivity.this, DayInOutShow.class);
                    }
                    MyMoneyZeroActivity.this.startActivity(intent14);
                    MyMoneyZeroActivity.this.finish();
                }
            }
            //統計報表
            if (item.get("ItemText").equals("統計報表")) {
                if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator5 = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator5.vibrate(30L);
                    } catch (Exception e6) {
                    }
                }
                if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                    MyMoneyZeroActivity.this.ShowInputPassword();
                } else {
//                    Intent intent15 = new Intent();
//                    intent15.setClass(MyMoneyZeroActivity.this, ReportView.class);
//                    MyMoneyZeroActivity.this.startActivity(intent15);
//                    MyMoneyZeroActivity.this.finish();
                }
            }

            if (item.get("ItemText").equals("系統設定")) {
                if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator6 = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator6.vibrate(30L);
                    } catch (Exception e7) {
                    }
                }
//                Intent intent16 = new Intent();
//                intent16.setClass(MyMoneyZeroActivity.this, SystemSet.class);
//                MyMoneyZeroActivity.this.startActivity(intent16);
//                MyMoneyZeroActivity.this.finish();
            }
            if (item.get("ItemText").equals("帳本選擇")) {
                if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator7 = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator7.vibrate(30L);
                    } catch (Exception e8) {
                    }
                }
                if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                    MyMoneyZeroActivity.this.ShowInputPassword();
                } else {
//                    Intent intent17 = new Intent();
//                    intent17.setClass(MyMoneyZeroActivity.this, AccountSelect.class);
//                    MyMoneyZeroActivity.this.startActivity(intent17);
//                    MyMoneyZeroActivity.this.finish();
                }
            }
            if (item.get("ItemText").equals("項目設定")) {
                if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator8 = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator8.vibrate(30L);
                    } catch (Exception e9) {
                    }
                }
                if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                    MyMoneyZeroActivity.this.ShowInputPassword();
                } else {
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("ITEM_CLASS", "資產");
//                    intent.putExtras(bundle);
//                    intent.setClass(MyMoneyZeroActivity.this, ItemSet.class);
//                    MyMoneyZeroActivity.this.startActivity(intent);
//                    MyMoneyZeroActivity.this.finish();
                }
            }
            if (item.get("ItemText").equals("結束離開")) {
                if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator9 = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator9.vibrate(30L);
                    } catch (Exception e10) {
                    }
                }
                AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(MyMoneyZeroActivity.this);
                MyAlertDialog.setTitle("確認");
                MyAlertDialog.setMessage("您是否確定要離開本系統?");
                DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.ItemClickListener.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) throws SQLException {
                        try {
                            ExitProg();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.ItemClickListener.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                    }
                };
                MyAlertDialog.setPositiveButton("確定", OkClick);
                MyAlertDialog.setNegativeButton("取消", CanCelClick);
                MyAlertDialog.show();
            }
            if (item.get("ItemText").equals("Dropbox")) {
                if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                    try {
                        Vibrator vibrator10 = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator10.vibrate(30L);
                    } catch (Exception e11) {
                    }
                }
                if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                    MyMoneyZeroActivity.this.ShowInputPassword();
                    return;
                }
//                Intent intent2 = new Intent();
//                intent2.setClass(MyMoneyZeroActivity.this, Dropbox.class);
//                MyMoneyZeroActivity.this.startActivity(intent2);
//                MyMoneyZeroActivity.this.finish();
            }
        }
    }

    public void ExitProg() throws IOException, SQLException {
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        Cursor cursor = this.DataDB.rawQuery("SELECT ACCOUNT_ID FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND BOOKED = '1'", null);
        if (cursor.moveToNext()) {
            this.AccountId = cursor.getDouble(0);
            String SQL = "UPDATE SYSTEM_DATA SET DATA_VALUE = " + String.valueOf(this.AccountId) + " WHERE USER_ID = 'admin' AND DATA_NOTE = '使用帳本'";
            this.DataDB.execSQL(SQL);
            cursor.close();
        } else {
            cursor.close();
        }
        try {
            this.DataDB.execSQL("UPDATE SYSTEM_DATA SET DATA_VALUE = '' WHERE USER_ID = 'admin' AND DATA_NOTE = '輸入帳密'");
        } catch (Exception e2) {
        }
        this.DataDB.close();
        BackDatabase();
//        int pid = Process.myPid();
//        Process.killProcess(pid);
        if (activityList.size() > 0) {
            Iterator<Activity> it = activityList.iterator();
            while (it.hasNext()) {
                Activity activity = it.next();
                activity.finish();
            }
//            Process.killProcess(Process.myPid());
        }
        Intent startMain = new Intent("android.intent.action.MAIN");
        startMain.addCategory("android.intent.category.HOME");
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
//        System.exit(0);
//        Process.killProcess(Process.myPid());
        finish();
    }

    public void BackDatabase() throws IOException {
        copyDBtoSDCard();
//        String tSDCardPath = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/MyMoneyZero/DataBack/";
        String tSDCardPath = String.valueOf(getExternalFilesDir(null));
        File tDataPath = new File(tSDCardPath);
        try {
            tDataPath.mkdirs();
        } catch (Exception e) {
        }
        File fd = new File(tSDCardPath);
        try {
            String[] dirarray = fd.list();
            Arrays.sort(dirarray);
            if (dirarray.length > 7) {
                for (int i = 0; i < dirarray.length - 7; i++) {
                    try {
                        File KillFile = new File(String.valueOf(tSDCardPath) + dirarray[i]);
                        KillFile.delete();
                    } catch (Exception e2) {
                    }
                }
            }
        } catch (Exception e3) {
        }
    }
    public int CreateDatabase() throws IOException, SQLException {
        String SQL;
        String SQL2;
        String SQL3;
        String SQL4;
        double NowMount;
        double NowMount2;
        this.MonthInMount += 0.0d;
        double d = 0.0d + 0.0d;
        // 取得外部儲存路徑
//        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String tSDCardPath = String.valueOf(getExternalFilesDir(null));
        try {
            ///storage/emulated/0+MoneyZero
//            File tDataPath = new File(String.valueOf(tSDCardPath) + "/MyMoneyZero/");
            File tDataPath = new File(getExternalFilesDir(null), "MyMoneyZero");
            // 若資料夾不存在則建立
            if (!tDataPath.exists()) {
                tDataPath.mkdirs();
                File dir = getFilesDir();
                ///data/user/0/com.coobila.mymoney/files/mymoney.db
                File ChackDataPath = new File(String.valueOf(dir.getPath()) + "/mymoney.db");
//                檔案或資料夾是否存在的檢查方法
                if (ChackDataPath.exists()) {
                    FileInputStream tISStream = new FileInputStream(String.valueOf(dir.getPath()) + "/mymoney.db");
                    FileOutputStream tOutStream = new FileOutputStream(tDataPath + "/mymoney.db");
                    byte[] tBuffer = new byte[5120];
                    while (true) {
                        int tCount = tISStream.read(tBuffer);
                        if (tCount > 0) {
                            tOutStream.write(tBuffer, 0, tCount);
                        } else {
                            tOutStream.close();
                            tISStream.close();
                            AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
                            MyAlertDialog.setTitle("系統資料庫回復");
                            MyAlertDialog.setMessage("系統偵測到您的資料庫異常消失,目前系統已從自動備份區將資料庫還原,系統會先離開本系統,請您重新進入一次即可正常使用!");
                            ExitProg();
                            return 0;
                        }
                    }
                } else {
                    try {
//                        this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                        this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                    } catch (Exception e) {
                        try {
                            this.DataDB = SQLiteDatabase.openOrCreateDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", (SQLiteDatabase.CursorFactory) null);
                        } catch (Exception e2) {
                            return 0;
                        }
                    }
                    this.SQL = "CREATE TABLE PROJECT_SET(USER_ID char(20) not null,ACCOUNT_ID int,PROJECT_ID char(20),PROJECT_NOTE char(40))";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE UNIQUE INDEX PROJECT ON PROJECT_SET(USER_ID,ACCOUNT_ID,PROJECT_ID)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE TABLE PAY_COLL_NAME(USER_ID char(20) not null,ACCOUNT_ID int,MAKE_NO double,COLL_NAME char(40) not null)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE INDEX PayCollIndex ON PAY_COLL_NAME(USER_ID,ACCOUNT_ID,MAKE_NO)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE TABLE OFTEN_NOTE(USER_ID char(20) not null,ACCOUNT_ID int,MAKE_NO double,ITEM_CLASS char(10) not null,ITEM_NOTE char(40) not null,DATA_NOTE char(60) not null)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE INDEX OftenIndex ON OFTEN_NOTE(USER_ID,ACCOUNT_ID,MAKE_NO)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE TABLE SYSTEM_DATA(USER_ID char(20) not null,ACCOUNT_ID int,DATA_NOTE char(40) not null,DATA_VALUE char(180) not null)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE TABLE NOTEPAD_DATA(USER_ID char(20) not null,ACCOUNT_ID int,ACCOUNT_NOTE char(40) not null,BOOKED char(1) not null,NPASSWORD char(20) not null)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE TABLE ITEM_DATA(USER_ID char(20) not null,ACCOUNT_ID int,MAKE_NO double,ITEM_NOTE char(40) not null,ITEM_CLASS char(10) not null,BEFORE_MOUNT double,ITEM_STYLE char(10) not null,PARENT_NOTE char(40) not null,EXCHANGE double,CHARGE char(1) not null)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE TABLE YEAR_SPEND(USER_ID char(20) not null,ACCOUNT_ID int,YEAR_MONTH char(7) not null,ITEM_NOTE char(40) not null,SPEND_MOUNT double)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE UNIQUE INDEX Index_Make_no ON ITEM_DATA(USER_ID,ACCOUNT_ID,ITEM_CLASS,MAKE_NO)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE TABLE MYMONEY_DATA(USER_ID char(20) not null,ACCOUNT_ID int,MAKE_NO double,ITEM_CLASS char(10) not null,DATA_DATE char(10) not null,ITEM_NOTE char(40) not null,IN_MOUNT double,OUT_MOUNT double,DATA_NOTE char(60) not null,INVOICE_NO char(10) not null,DATA_NO char(1) not null,PAY_COLL_NAME char(50) not null,PROJECT_ID char(20) not null,EXCHANGE double,LINK_PC char(1) not null,PC_MAKE_NO double,DATA_NOTE2 MEMO)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE INDEX Index_System_Data ON SYSTEM_DATA(USER_ID,ACCOUNT_ID,DATA_NOTE)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE UNIQUE INDEX Index_Notepad ON NOTEPAD_DATA(USER_ID,ACCOUNT_ID)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE UNIQUE INDEX Index_Year_Spend ON YEAR_SPEND(USER_ID,ACCOUNT_ID,YEAR_MONTH,ITEM_NOTE)";
                    this.DataDB.execSQL(this.SQL);
                    this.SQL = "CREATE INDEX Index_Make_no ON ITEM_DATA(USER_ID,ACCOUNT_ID,DATA_DATE)";
                    this.DataDB.execSQL(this.SQL);
                    this.DataDB.close();
                }
            } else {
                File tDataPath2 = new File(String.valueOf(tSDCardPath) + "/MyMoneyZero/");
                String tDBFilePath = tDataPath2 + "/mymoney.db";
                File tFile = new File(tDBFilePath);
                if (!tFile.exists()) {
                    File dir2 = getFilesDir();
                    File ChackDataPath2 = new File(String.valueOf(dir2.getPath()) + "/mymoney.db");
                    if (ChackDataPath2.exists()) {
//                        FileInputStream tISStream2 = new FileInputStream(String.valueOf(dir2.getPath()) + "/mymoney.db");
//                        FileOutputStream tOutStream2 = new FileOutputStream(tDataPath2 + "/mymoney.db");
                        FileInputStream tISStream2 = new FileInputStream("storage/emulated/0/Android/data/com.coobila.mymoney/files/mymoney.db");
                        FileOutputStream tOutStream2 = new FileOutputStream("storage/emulated/0/Android/data/com.coobila.mymoney/files/mymoney.db");
                        byte[] tBuffer2 = new byte[5120];
                        while (true) {
                            int tCount2 = tISStream2.read(tBuffer2);
                            if (tCount2 <= 0) {
                                break;
                            }
                            tOutStream2.write(tBuffer2, 0, tCount2);
                        }
                        tOutStream2.close();
                        tISStream2.close();
                    }
                }
            }
        } catch (Exception e3) {
        }
        try {
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
            try {
                this.SQL = "ALTER TABLE MYMONEY_DATA ADD PC_MAKE_NO DOUBLE";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e4) {
            }
            try {
                this.SQL = "ALTER TABLE MYMONEY_DATA ADD MODIFY_DATE CHAR(10)";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e5) {
            }
            try {
                this.SQL = "ALTER TABLE MYMONEY_DATA ADD DATA_NOTE2 MEMO";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e6) {
            }
            try {
                this.SQL = "UPDATE MYMONEY_DATA SET DATA_NOTE2 = '' WHERE DATA_NOTE2 IS NULL";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e7) {
            }
            try {
                this.SQL = "ALTER TABLE MYMONEY_DATA ADD MODIFY_TIME CHAR(10)";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e8) {
            }
            try {
                this.SQL = "ALTER TABLE MYMONEY_DATA ADD DATA_KEY CHAR(20)";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e9) {
            }
            try {
                this.SQL = "ALTER TABLE MYMONEY_DATA ADD EXPORT CHAR(1)";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e10) {
            }
            try {
                this.SQL = "ALTER TABLE ITEM_DATA ADD HIDESTYLE CHAR(1)";
                this.DataDB.execSQL(this.SQL);
                this.SQL = "UPDATE ITEM_DATA SET HIDESTYLE = '' WHERE HIDESTYLE IS NULL";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e11) {
            }
            try {
                this.SQL = "ALTER TABLE ITEM_DATA ADD CASH CHAR(1)";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e12) {
            }
            try {
                this.SQL = "CREATE INDEX DATA_DATE ON MYMONEY_DATA(USER_ID,ACCOUNT_ID,DATA_DATE,ITEM_NOTE)";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e13) {
            }
            try {
                this.SQL = "CREATE TABLE MYMONEY_DATA_KILL (USER_ID CHAR(20) NOT NULL,ACCOUNT_ID INT,DATA_KEY CHAR(20))";
                this.DataDB.execSQL(this.SQL);
            } catch (Exception e14) {
            }
            try {
                this.SQL = "UPDATE ITEM_DATA SET CASH = '1' WHERE ITEM_NOTE = '現金' AND CASH IS NULL";
                this.DataDB.execSQL(this.SQL);
                try {
                    this.DataDB.rawQuery("SELECT BOOKED,ACCOUNT_ID FROM NOTEPAD_DATA WHERE USER_ID = 'admin' ORDER BY BOOKED DESC", null).close();
                    Cursor cursor = this.DataDB.rawQuery("SELECT BOOKED,ACCOUNT_ID FROM NOTEPAD_DATA WHERE USER_ID = 'admin' ORDER BY BOOKED DESC", null);
                    if (cursor.moveToNext()) {
                        cursor.getString(0).equals("1");
                        cursor.close();
                    } else {
                        cursor.close();
                        this.DataDB.execSQL("INSERT INTO NOTEPAD_DATA (USER_ID,ACCOUNT_ID,ACCOUNT_NOTE,BOOKED,NPASSWORD) VALUES ('admin',1,'我的記帳簿','1','')");
                        this.DataDB.execSQL("INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin',0,'使用帳本','1')");
                        this.DataDB.execSQL("INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin',1,'金額小數點','0')");
                        this.DataDB.execSQL("INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin',1,'匯率小數點','3')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'現金','資產',0,'','現金',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,2,'銀行存款','資產',0,'','銀行存款',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,3,'定期存款','資產',0,'','定期存款',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,4,'股票投資','資產',0,'','股票投資',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,5,'基金投資','資產',0,'','基金投資',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,6,'iCash卡','資產',0,'','iCash卡',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,7,'悠遊卡','資產',0,'','悠遊卡',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'信用卡','負債',0,'','信用卡',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,2,'房屋貸款','負債',0,'','房屋貸款',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'薪資收入','收入',0,'','薪資收入',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,2,'兼職收入','收入',0,'','兼職收入',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,3,'獎金收入','收入',0,'','獎金收入',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,4,'三節獎金','收入',0,'','獎金收入',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,5,'年終獎金','收入',0,'','獎金收入',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,6,'業績獎金','收入',0,'','獎金收入',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,7,'利息收入','收入',0,'','利息收入',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,8,'租金收入','收入',0,'','租金收入',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,9,'其它收入','收入',0,'','其它收入',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'生活飲食','支出',0,'','生活飲食',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,2,'早餐','支出',0,'','生活飲食',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,3,'午餐','支出',0,'','生活飲食',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,4,'晚餐','支出',0,'','生活飲食',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,5,'宵夜','支出',0,'','生活飲食',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,6,'飲料','支出',0,'','生活飲食',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,7,'菸酒','支出',0,'','生活飲食',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,8,'蔬菜水果','支出',0,'','生活飲食',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,9,'點心零食','支出',0,'','生活飲食',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,10,'休閒娛樂','支出',0,'','休閒娛樂',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,11,'聯誼聚餐','支出',0,'','休閒娛樂',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,12,'運動健身','支出',0,'','休閒娛樂',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,13,'影片租購','支出',0,'','休閒娛樂',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,14,'電影觀賞','支出',0,'','休閒娛樂',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,15,'戶外郊遊','支出',0,'','休閒娛樂',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,16,'旅遊度假','支出',0,'','休閒娛樂',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,17,'寵物寶貝','支出',0,'','休閒娛樂',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,18,'教育學習','支出',0,'','教育學習',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,19,'教育學費','支出',0,'','教育學習',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,20,'上課進修','支出',0,'','教育學習',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,21,'書籍購買','支出',0,'','教育學習',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,22,'才藝學習','支出',0,'','教育學習',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,23,'安親補習','支出',0,'','教育學習',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,24,'行車交通','支出',0,'','行車交通',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,25,'機車油費','支出',0,'','行車交通',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,26,'汽車油費','支出',0,'','行車交通',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,27,'捷運搭乘','支出',0,'','行車交通',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,28,'公車搭乘','支出',0,'','行車交通',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,29,'停車費','支出',0,'','行車交通',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,30,'計程車','支出',0,'','行車交通',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,31,'過路費','支出',0,'','行車交通',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,32,'居家物業','支出',0,'','居家物業',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,33,'水費','支出',0,'','居家物業',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,34,'電費','支出',0,'','居家物業',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,35,'瓦斯費','支出',0,'','居家物業',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,36,'管理費','支出',0,'','居家物業',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,37,'房屋租金','支出',0,'','居家物業',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,38,'房貸利息','支出',0,'','居家物業',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,39,'住屋裝修','支出',0,'','居家物業',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,40,'住屋傢飾','支出',0,'','居家物業',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,41,'行動通訊','支出',0,'','行動通訊',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,42,'電話費','支出',0,'','行動通訊',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,43,'手機費','支出',0,'','行動通訊',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,44,'網路費','支出',0,'','行動通訊',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,45,'有線電視費','支出',0,'','行動通訊',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,46,'MOD網路電視費','支出',0,'','行動通訊',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,47,'書報雜誌','支出',0,'','書報雜誌',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,48,'報紙','支出',0,'','書報雜誌',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,49,'雜誌','支出',0,'','書報雜誌',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,50,'定期刊物','支出',0,'','書報雜誌',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,51,'人情往來','支出',0,'','人情往來',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,52,'婚喪喜慶','支出',0,'','人情往來',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,53,'送禮請客','支出',0,'','人情往來',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,54,'孝親費用','支出',0,'','人情往來',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,55,'慈善捐款','支出',0,'','人情往來',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,56,'服飾美容','支出',0,'','服飾美容',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,57,'置裝費','支出',0,'','服飾美容',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,58,'化妝品','支出',0,'','服飾美容',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,59,'保養品','支出',0,'','服飾美容',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,60,'醫療保健','支出',0,'','醫療保健',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,61,'醫療掛號','支出',0,'','醫療保健',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,62,'健康檢查','支出',0,'','醫療保健',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,63,'醫療藥物','支出',0,'','醫療保健',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,64,'保健食品','支出',0,'','醫療保健',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,65,'美容養生','支出',0,'','醫療保健',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,66,'金融保險','支出',0,'','金融保險',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,67,'汽機車險','支出',0,'','金融保險',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,68,'人壽險','支出',0,'','金融保險',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,69,'產物險','支出',0,'','金融保險',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,70,'勞保費','支出',0,'','金融保險',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,71,'健保費','支出',0,'','金融保險',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,72,'稅金','支出',0,'','稅金',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,73,'燃料稅','支出',0,'','稅金',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,74,'牌照稅','支出',0,'','稅金',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,75,'綜所稅','支出',0,'','稅金',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,76,'房屋稅','支出',0,'','稅金',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,77,'地價稅','支出',0,'','稅金',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,78,'其它支出','支出',0,'','其它支出',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,79,'提款手續費','支出',0,'','其它支出',1,'1','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,80,'轉帳手續費','支出',0,'','其它支出',1,'1','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'投資收入','業外收入',0,'','投資收入',1,'0','')");
                        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'投資損失','業外支出',0,'','投資損失',1,'0','')");
                        try {
                            this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'Change','其它',0,'','Change',1,'0','')");
                        } catch (Exception e15) {
                        }
                        this.AccountId = 1.0d;
                    }
                    GetNowDate c = new GetNowDate();
                    String NowMonth = c.GetDate().substring(0, 7);
                    String NowDay = c.GetDate();
                    this.DayOutMountView = (TextView) findViewById(R.id.DayOut);
                    this.WeekOutMountView = (TextView) findViewById(R.id.WeekOutMount);
                    this.MonthOutMountView = (TextView) findViewById(R.id.MonthOutMount);
                    this.InOutMountView = (TextView) findViewById(R.id.InOutMount);
                    this.UpMonth = (TextView) findViewById(R.id.UpMonth);
                    this.NowMonthShow = (TextView) findViewById(R.id.NowMonthShow);
                    this.DownMonth = (TextView) findViewById(R.id.DownMonth);
                    GetNowDate GetNowMonth = new GetNowDate();
                    Bundle bundle = getIntent().getExtras();
                    String GetNowMonthShow = "";
                    this.NowMonthShow.setText(String.valueOf(GetNowMonth.GetDate().substring(0, 7)) + "月 項目統計金額");
                    try {
                        GetNowMonthShow = bundle.getString("NowMonthShow");
                    } catch (Exception e16) {
                    }
                    if (GetNowMonthShow != null) {
                        if (GetNowMonthShow.length() >= 7) {
                            this.NowMonthShow.setText(String.valueOf(GetNowMonthShow.substring(0, 7)) + "月 項目統計金額");
                        } else {
                            this.NowMonthShow.setText(String.valueOf(GetNowMonth.GetDate().substring(0, 7)) + "月 項目統計金額");
                        }
                    }
                    this.UpMonth.setOnClickListener(new View.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.4
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) throws NumberFormatException {
                            if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                                try {
                                    Vibrator vibrator = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(30L);
                                } catch (Exception e17) {
                                }
                            }
                            int GetYear = Integer.parseInt(MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(0, 4));
                            int GetMonth = Integer.parseInt(MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(5, 7)) - 1;
                            if (GetMonth < 1) {
                                GetYear--;
                                GetMonth = 12;
                            }
                            if (GetMonth <= 9) {
                                MyMoneyZeroActivity.this.NowMonthShow.setText(String.valueOf(String.valueOf(GetYear)) + "/0" + String.valueOf(GetMonth) + "月 項目統計金額");
                            } else {
                                MyMoneyZeroActivity.this.NowMonthShow.setText(String.valueOf(String.valueOf(GetYear)) + "/" + String.valueOf(GetMonth) + "月 項目統計金額");
                            }
                            MyMoneyZeroActivity.this.ShowData();
                        }
                    });
                    this.DownMonth.setOnClickListener(new View.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.5
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) throws NumberFormatException {
                            if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                                try {
                                    Vibrator vibrator = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(30L);
                                } catch (Exception e17) {
                                }
                            }
                            int GetYear = Integer.parseInt(MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(0, 4));
                            int GetMonth = Integer.parseInt(MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(5, 7)) + 1;
                            if (GetMonth > 12) {
                                GetYear++;
                                GetMonth = 1;
                            }
                            if (GetMonth <= 9) {
                                MyMoneyZeroActivity.this.NowMonthShow.setText(String.valueOf(String.valueOf(GetYear)) + "/0" + String.valueOf(GetMonth) + "月 項目統計金額");
                            } else {
                                MyMoneyZeroActivity.this.NowMonthShow.setText(String.valueOf(String.valueOf(GetYear)) + "/" + String.valueOf(GetMonth) + "月 項目統計金額");
                            }
                            MyMoneyZeroActivity.this.ShowData();
                        }
                    });
                    Cursor cursor2 = this.DataDB.rawQuery("SELECT DATA_VALUE FROM SYSTEM_DATA WHERE DATA_NOTE = 'KillPassWord'", null);
                    if (cursor2.moveToNext()) {
                        cursor2.close();
                    } else {
                        cursor2.close();
                        String SQL5 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'KillPassWord','')";
                        this.DataDB.execSQL(SQL5);
                        this.DataDB.execSQL("UPDATE NOTEPAD_DATA SET NPASSWORD = ''");
                    }
                    Cursor cursor3 = this.DataDB.rawQuery("SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND DATA_NOTE = '使用帳本'", null);
                    this.AccountId = 0.0d;
                    if (cursor3.moveToNext()) {
                        this.AccountId = cursor3.getDouble(0);
                    }
                    cursor3.close();
                    if (this.AccountId == 0.0d) {
                        Cursor cursor4 = this.DataDB.rawQuery("SELECT ACCOUNT_ID,NPASSWORD,INPUT_NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND BOOKED = '1'", null);
                        if (cursor4.moveToNext()) {
                            this.AccountId = cursor4.getDouble(0);
                            String SQL6 = "UPDATE SYSTEM_DATA SET DATA_VALUE = " + String.valueOf(this.AccountId) + " WHERE USER_ID = 'admin' AND DATA_NOTE = '使用帳本'";
                            this.DataDB.execSQL(SQL6);
                            cursor4.close();
                        } else {
                            cursor4.close();
                        }
                    }
                    if (this.AccountId > 0.0d) {
                        String SQL7 = "SELECT ACCOUNT_NOTE FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId);
                        Cursor cursor5 = this.DataDB.rawQuery(SQL7, null);
                        if (cursor5.moveToNext()) {
                            this.AccountName = cursor5.getString(0);
                        }
                        cursor5.close();
                        String SQL8 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '金額小數點'";
                        Cursor cursor6 = this.DataDB.rawQuery(SQL8, null);
                        this.MountSub = 0;
                        if (cursor6.moveToNext()) {
                            this.MountSub = (int) cursor6.getDouble(0);
                        }
                        cursor6.close();
                        String SQL9 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '匯率小數點'";
                        Cursor cursor7 = this.DataDB.rawQuery(SQL9, null);
                        this.ExchangeSub = 3;
                        if (cursor7.moveToNext()) {
                            this.ExchangeSub = (int) cursor7.getDouble(0);
                        }
                        cursor7.close();
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
                        String SQL10 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示本日支出金額'";
                        Cursor cursor8 = this.DataDB.rawQuery(SQL10, null);
                        if (!cursor8.moveToNext()) {
                            cursor8.close();
                            String SQL11 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'顯示本日支出金額','1')";
                            this.DataDB.execSQL(SQL11);
                            this.ShowMount1 = true;
                        } else {
                            if (cursor8.getString(0).trim().endsWith("1")) {
                                this.ShowMount1 = true;
                            } else {
                                this.ShowMount1 = false;
                            }
                            cursor8.close();
                        }
                        String SQL12 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示本週支出金額'";
                        Cursor cursor9 = this.DataDB.rawQuery(SQL12, null);
                        if (!cursor9.moveToNext()) {
                            cursor9.close();
                            String SQL13 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'顯示本週支出金額','1')";
                            this.DataDB.execSQL(SQL13);
                            this.ShowMount2 = true;
                        } else {
                            if (cursor9.getString(0).trim().endsWith("1")) {
                                this.ShowMount2 = true;
                            } else {
                                this.ShowMount2 = false;
                            }
                            cursor9.close();
                        }
                        String SQL14 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示本月支出金額'";
                        Cursor cursor10 = this.DataDB.rawQuery(SQL14, null);
                        if (!cursor10.moveToNext()) {
                            cursor10.close();
                            String SQL15 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'顯示本月支出金額','1')";
                            this.DataDB.execSQL(SQL15);
                            this.ShowMount3 = true;
                        } else {
                            if (cursor10.getString(0).trim().endsWith("1")) {
                                this.ShowMount3 = true;
                            } else {
                                this.ShowMount3 = false;
                            }
                            cursor10.close();
                        }
                        String SQL16 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示本月收入金額'";
                        Cursor cursor11 = this.DataDB.rawQuery(SQL16, null);
                        if (!cursor11.moveToNext()) {
                            cursor11.close();
                            String SQL17 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'顯示本月收入金額','0')";
                            this.DataDB.execSQL(SQL17);
                            this.ShowMount4 = false;
                        } else {
                            if (cursor11.getString(0).trim().endsWith("1")) {
                                this.ShowMount4 = true;
                            } else {
                                this.ShowMount4 = false;
                            }
                            cursor11.close();
                        }
                        String SQL18 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示本月收支餘額'";
                        Cursor cursor12 = this.DataDB.rawQuery(SQL18, null);
                        if (!cursor12.moveToNext()) {
                            cursor12.close();
                            String SQL19 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'顯示本月收支餘額','0')";
                            this.DataDB.execSQL(SQL19);
                            this.ShowMount5 = false;
                        } else {
                            if (cursor12.getString(0).trim().endsWith("1")) {
                                this.ShowMount5 = true;
                            } else {
                                this.ShowMount5 = false;
                            }
                            cursor12.close();
                        }
                        String SQL20 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '主畫面統計包含業外收支'";
                        Cursor cursor13 = this.DataDB.rawQuery(SQL20, null);
                        if (!cursor13.moveToNext()) {
                            cursor13.close();
                            String SQL21 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'主畫面統計包含業外收支','1')";
                            this.DataDB.execSQL(SQL21);
                            this.SumOtherMount = false;
                        } else {
                            if (cursor13.getString(0).trim().endsWith("1")) {
                                this.SumOtherMount = true;
                            } else {
                                this.SumOtherMount = false;
                            }
                            cursor13.close();
                        }
                        String SQL22 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '關閉優惠廣告通知'";
                        Cursor cursor14 = this.DataDB.rawQuery(SQL22, null);
                        if (!cursor14.moveToNext()) {
                            cursor14.close();
                            String SQL23 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'關閉優惠廣告通知','0')";
                            this.DataDB.execSQL(SQL23);
                            this.LockShowAdLocus = false;
                        } else {
                            if (cursor14.getString(0).trim().endsWith("1")) {
                                this.LockShowAdLocus = true;
                            } else {
                                this.LockShowAdLocus = false;
                            }
                            cursor14.close();
                        }
                        String SQL24 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示最後異動'";
                        Cursor cursor15 = this.DataDB.rawQuery(SQL24, null);
                        if (!cursor15.moveToNext()) {
                            cursor15.close();
                            String SQL25 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'顯示最後異動','1')";
                            this.DataDB.execSQL(SQL25);
                        } else {
                            cursor15.close();
                        }
                        String SQL26 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '帳務記錄顯示發票號碼'";
                        Cursor cursor16 = this.DataDB.rawQuery(SQL26, null);
                        if (!cursor16.moveToNext()) {
                            cursor16.close();
                            String SQL27 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'帳務記錄顯示發票號碼','0')";
                            this.DataDB.execSQL(SQL27);
                        } else {
                            cursor16.close();
                        }
                        if (this.SumOtherMount) {
                            SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE = '" + NowDay.toString() + "' AND (ITEM_CLASS = '支出' OR ITEM_CLASS = '業外支出')";
                        } else {
                            SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE = '" + NowDay.toString() + "' AND (ITEM_CLASS = '支出')";
                        }
                        Cursor cursor17 = this.DataDB.rawQuery(SQL, null);
                        if (cursor17.moveToNext() && cursor17.getString(0) != null) {
                            this.DayOutMount = Double.valueOf(cursor17.getString(0)).doubleValue();
                        }
                        cursor17.close();
                        if (this.SumOtherMount) {
                            SQL2 = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '支出' OR ITEM_CLASS = '業外支出')";
                        } else {
                            SQL2 = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '支出')";
                        }
                        Cursor cursor18 = this.DataDB.rawQuery(SQL2, null);
                        if (cursor18.moveToNext() && cursor18.getString(0) != null) {
                            this.MonthOutMount = Double.valueOf(cursor18.getString(0)).doubleValue();
                        }
                        cursor18.close();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        String[] WeekData = {"", "日", "一", "二", "三", "四", "五", "六"};
                        Calendar GetWeek = Calendar.getInstance();
                        this.Week = WeekData[GetWeek.get(7)];
                        if (this.Week.equals("一")) {
                            this.WeekStartDate = sdf.format(GetWeek.getTime());
                            GetWeek.add(7, 6);
                            this.WeekEndDate = sdf.format(GetWeek.getTime());
                        }
                        if (this.Week.equals("二")) {
                            GetWeek.add(7, -1);
                            this.WeekStartDate = sdf.format(GetWeek.getTime());
                            GetWeek.add(7, 6);
                            this.WeekEndDate = sdf.format(GetWeek.getTime());
                        }
                        if (this.Week.equals("三")) {
                            GetWeek.add(7, -2);
                            this.WeekStartDate = sdf.format(GetWeek.getTime());
                            GetWeek.add(7, 6);
                            this.WeekEndDate = sdf.format(GetWeek.getTime());
                        }
                        if (this.Week.equals("四")) {
                            GetWeek.add(7, -3);
                            this.WeekStartDate = sdf.format(GetWeek.getTime());
                            GetWeek.add(7, 6);
                            this.WeekEndDate = sdf.format(GetWeek.getTime());
                        }
                        if (this.Week.equals("五")) {
                            GetWeek.add(7, -4);
                            this.WeekStartDate = sdf.format(GetWeek.getTime());
                            GetWeek.add(7, 6);
                            this.WeekEndDate = sdf.format(GetWeek.getTime());
                        }
                        if (this.Week.equals("六")) {
                            GetWeek.add(7, -5);
                            this.WeekStartDate = sdf.format(GetWeek.getTime());
                            GetWeek.add(7, 6);
                            this.WeekEndDate = sdf.format(GetWeek.getTime());
                        }
                        if (this.Week.equals("日")) {
                            GetWeek.add(7, -6);
                            this.WeekStartDate = sdf.format(GetWeek.getTime());
                            GetWeek.add(7, 6);
                            this.WeekEndDate = sdf.format(GetWeek.getTime());
                        }
                        if (this.SumOtherMount) {
                            SQL3 = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE BETWEEN '" + this.WeekStartDate + "' AND '" + this.WeekEndDate + "' AND (ITEM_CLASS = '支出' OR ITEM_CLASS = '業外支出')";
                        } else {
                            SQL3 = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE BETWEEN '" + this.WeekStartDate + "' AND '" + this.WeekEndDate + "' AND (ITEM_CLASS = '支出')";
                        }
                        Cursor cursor19 = this.DataDB.rawQuery(SQL3, null);
                        if (cursor19.moveToNext()) {
                            if (cursor19.getString(0) != null) {
                                this.WeekOutMount = Double.valueOf(cursor19.getString(0)).doubleValue();
                            } else {
                                this.WeekOutMount = 0.0d;
                            }
                        }
                        cursor19.close();
                        DecimalFormat df = new DecimalFormat("#,##0" + this.MountFormat);
                        this.WeekOutMountView.setText("本週支出金額：" + df.format(this.WeekOutMount));
                        if (this.SumOtherMount) {
                            SQL4 = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '收入' OR ITEM_CLASS = '業外收入')";
                        } else {
                            SQL4 = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '收入')";
                        }
                        Cursor cursor20 = this.DataDB.rawQuery(SQL4, null);
                        if (cursor20.moveToNext() && cursor20.getString(0) != null) {
                            this.MonthInMount = Double.valueOf(cursor20.getString(0)).doubleValue();
                        }
                        cursor20.close();
                        String SQL28 = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '其它' AND ITEM_NOTE = 'Change'";
                        Cursor cursor21 = this.DataDB.rawQuery(SQL28, null);
                        if (cursor21.moveToNext()) {
                            cursor21.close();
                        } else {
                            cursor21.close();
                            try {
                                String SQL29 = "INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",1,'Change','其它',0,'','Change',1,'0','')";
                                this.DataDB.execSQL(SQL29);
                            } catch (Exception e17) {
                            }
                        }
                        GetNowDate GetNow = new GetNowDate();
                        double d2 = 0.0d + 0.0d;
                        double NowMoney = 0.0d;
                        String SQL30 = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '資產' AND CASH = '1' ORDER BY MAKE_NO";
                        Cursor cursor22 = this.DataDB.rawQuery(SQL30, null);
                        while (cursor22.moveToNext()) {
                            double Exchange = cursor22.getDouble(2);
                            double BeforeMount = cursor22.getDouble(1) * Exchange;
                            String SQL31 = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor22.getString(0).toString() + "' AND DATA_DATE <= '" + GetNow.GetDate() + "'";
                            Cursor cursor23 = this.DataDB.rawQuery(SQL31, null);
                            if (cursor23.moveToNext()) {
                                NowMount = cursor23.getDouble(0) * Exchange;
                            } else {
                                NowMount = 0.0d;
                            }
                            cursor23.close();
                            NowMoney += BeforeMount + NowMount;
                            String SQL32 = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '資產' AND PARENT_NOTE <> ITEM_NOTE AND PARENT_NOTE = '" + cursor22.getString(0).toString() + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                            Cursor cursor24 = this.DataDB.rawQuery(SQL32, null);
                            while (cursor24.moveToNext()) {
                                double Exchange2 = cursor24.getDouble(2);
                                double NowMoney2 = NowMoney + (cursor24.getDouble(1) * Exchange2);
                                String SQL33 = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor24.getString(0).toString() + "' AND DATA_DATE <= '" + GetNow.GetDate() + "'";
                                Cursor cursor32 = this.DataDB.rawQuery(SQL33, null);
                                if (cursor32.moveToNext()) {
                                    NowMount2 = cursor32.getDouble(0) * Exchange2;
                                } else {
                                    NowMount2 = 0.0d;
                                }
                                NowMoney = NowMoney2 + NowMount2;
                                cursor32.close();
                            }
                            cursor24.close();
                        }
                        this.DayOutMountView.setText(df.format(this.DayOutMount));
                        this.MonthOutMountView.setText(df.format(this.MonthOutMount));
                        this.InOutMountView.setText(df.format(NowMoney));
                        this.ErrorPassword = 0.0d;
                    } else {
                        this.DayOutMountView.setText("0");
                        this.MonthOutMountView.setText("0");
                        this.MonthOutMountView.setText("0");
                        this.InOutMountView.setText("0");
                        this.ErrorPassword = 1.0d;
                    }
                    try {
                        String SQL34 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '輸入帳密'";
                        Cursor cursor25 = this.DataDB.rawQuery(SQL34, null);
                        if (cursor25.moveToNext()) {
                            cursor25.close();
                        } else {
                            cursor25.close();
                            String SQL35 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'輸入帳密','')";
                            this.DataDB.execSQL(SQL35);
                        }
                    } catch (Exception e18) {
                    }
                    this.InputPassword = "";
                    this.InputPassword = new StringBuilder(String.valueOf(this.InputPassword)).toString();
                    try {
                        String SQL36 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
                        Cursor cursor26 = this.DataDB.rawQuery(SQL36, null);
                        if (cursor26.moveToNext()) {
                            this.InputPassword = cursor26.getString(0);
                        }
                        cursor26.close();
                    } catch (Exception e19) {
                    }
                    this.AccountPassword = "";
                    this.AccountPassword = new StringBuilder(String.valueOf(this.AccountPassword)).toString();
                    String SQL37 = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId);
                    Cursor cursor27 = this.DataDB.rawQuery(SQL37, null);
                    if (cursor27.moveToNext()) {
                        this.AccountPassword = cursor27.getString(0).trim();
                    }
                    if (!this.AccountPassword.trim().equals("") && !this.AccountPassword.trim().equals(this.InputPassword.trim()) && !this.AccountPassword.trim().equals("")) {
                        this.DayOutMountView.setText("0");
                        this.MonthOutMountView.setText("0");
                        this.InOutMountView.setText("0");
                        this.ErrorPassword = 1.0d;
                        ShowInputPassword();
                    } else {
                        this.ErrorPassword = 0.0d;
                    }
                    if (this.AccountId > 0.0d) {
                        String SQL38 = "SELECT * FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示收付人欄位'";
                        Cursor cursor28 = this.DataDB.rawQuery(SQL38, null);
                        if (!cursor28.moveToNext()) {
                            cursor28.close();
                            String SQL39 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'顯示收付人欄位','1')";
                            this.DataDB.execSQL(SQL39);
                        } else {
                            cursor28.close();
                        }
                        String SQL40 = "SELECT * FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示發票號碼欄位'";
                        Cursor cursor29 = this.DataDB.rawQuery(SQL40, null);
                        if (!cursor29.moveToNext()) {
                            cursor29.close();
                            String SQL41 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'顯示發票號碼欄位','1')";
                            this.DataDB.execSQL(SQL41);
                        } else {
                            cursor29.close();
                        }
                        String SQL42 = "SELECT * FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示專案名稱欄位'";
                        Cursor cursor30 = this.DataDB.rawQuery(SQL42, null);
                        if (!cursor30.moveToNext()) {
                            cursor30.close();
                            String SQL43 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'顯示專案名稱欄位','1')";
                            this.DataDB.execSQL(SQL43);
                        } else {
                            cursor30.close();
                        }
                        String SQL44 = "SELECT * FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '顯示資產與負債項目餘額'";
                        Cursor cursor31 = this.DataDB.rawQuery(SQL44, null);
                        if (!cursor31.moveToNext()) {
                            cursor31.close();
                            String SQL45 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'顯示資產與負債項目餘額','1')";
                            this.DataDB.execSQL(SQL45);
                        } else {
                            cursor31.close();
                        }
                        String SQL46 = "SELECT * FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '按鈕振動提醒功能'";
                        Cursor cursor33 = this.DataDB.rawQuery(SQL46, null);
                        if (!cursor33.moveToNext()) {
                            cursor33.close();
                            String SQL47 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'按鈕振動提醒功能','1')";
                            this.DataDB.execSQL(SQL47);
                        } else {
                            cursor33.close();
                        }
                        String SQL48 = "SELECT * FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '強制直式顯示'";
                        Cursor cursor34 = this.DataDB.rawQuery(SQL48, null);
                        if (!cursor34.moveToNext()) {
                            cursor34.close();
                            String SQL49 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'強制直式顯示','0')";
                            this.DataDB.execSQL(SQL49);
                        } else {
                            cursor34.close();
                        }
                        String SQL50 = "SELECT * FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '回到主畫面'";
                        Cursor cursor35 = this.DataDB.rawQuery(SQL50, null);
                        if (!cursor35.moveToNext()) {
                            cursor35.close();
                            String SQL51 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'回到主畫面','0')";
                            this.DataDB.execSQL(SQL51);
                        } else {
                            cursor35.close();
                        }
                        String SQL52 = "SELECT * FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '外幣轉台幣四捨五入'";
                        Cursor cursor36 = this.DataDB.rawQuery(SQL52, null);
                        if (!cursor36.moveToNext()) {
                            cursor36.close();
                            String SQL53 = "INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin'," + String.valueOf(this.AccountId) + ",'外幣轉台幣四捨五入','1')";
                            this.DataDB.execSQL(SQL53);
                        } else {
                            cursor36.close();
                        }
                    }
                    String SQL54 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '按鈕振動提醒功能'";
                    Cursor cursor37 = this.DataDB.rawQuery(SQL54, null);
                    if (cursor37.moveToNext()) {
                        if (cursor37.getString(0).trim().equals("1")) {
                            this.ShowVibrate = "1";
                        } else {
                            this.ShowVibrate = "0";
                        }
                    }
                    cursor37.close();
                    String SQL55 = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '強制直式顯示'";
                    Cursor cursor38 = this.DataDB.rawQuery(SQL55, null);
                    if (cursor38.moveToNext() && cursor38.getString(0).trim().equals("1")) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    cursor38.close();
                    this.DataDB.close();
                    CheckSerial Serial = new CheckSerial();
                    if (Serial.GetInputSerial()) {
                        if (this.AccountId > 0.0d) {
                            this.setTitle("帳務小管家ZERO v4.7(專業版) - " + this.AccountName);
                        } else {
                            this.setTitle("帳務小管家ZERO v4.7(專業版) - 未選擇作業的帳本");
                        }
                    } else if (this.AccountId > 0.0d) {
                        Objects.requireNonNull(getSupportActionBar()).setTitle("帳務小管家ZERO v4.7 - " + this.AccountName);
//                        this.setTitle("帳務小管家ZERO v4.7 - " + this.AccountName);
                    } else {
                        this.setTitle("帳務小管家ZERO v4.7 - 未選擇作業的帳本");
                    }
                    return 0;
                } catch (Exception e20) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("系統錯誤");
                    alert.setMessage("找不到系統資料庫或資料庫已毀\n損，系統將啟動自動還原功能，\n請您按下 [確定] 按鈕結束程式並\n重新啟動應可正常!!");
                    alert.setPositiveButton("確定", new DialogInterface.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent startMain = new Intent("android.intent.action.MAIN");
                            startMain.addCategory("android.intent.category.HOME");
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyMoneyZeroActivity.this.startActivity(startMain);
                            System.exit(0);
                            MyMoneyZeroActivity.this.finish();
                        }
                    });
                    alert.show();
                    return 1;
                }
            } catch (Exception e21) {
                AlertDialog.Builder alert2 = new AlertDialog.Builder(this);
                alert2.setTitle("系統錯誤");
                alert2.setMessage("找不到系統資料庫或資料庫已毀\n損，系統將啟動自動還原功能，\n請您按下 [確定] 按鈕結束程式並\n重新啟動應可正常!!");
                alert2.setPositiveButton("確定", new DialogInterface.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent startMain = new Intent("android.intent.action.MAIN");
                        startMain.addCategory("android.intent.category.HOME");
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyMoneyZeroActivity.this.startActivity(startMain);
                        System.exit(0);
                        MyMoneyZeroActivity.this.finish();
                    }
                });
                alert2.show();
                return 1;
            }
        } catch (Exception e22) {
            CreateDatabase2();
            AlertDialog.Builder alert3 = new AlertDialog.Builder(this);
            alert3.setTitle("系統錯誤");
            alert3.setMessage("找不到系統資料庫或資料庫已毀\n損，系統將啟動自動還原功能，\n請您按下 [確定] 按鈕結束程式並\n重新啟動應可正常!!");
            alert3.setPositiveButton("確定", new DialogInterface.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent startMain = new Intent("android.intent.action.MAIN");
                    startMain.addCategory("android.intent.category.HOME");
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyMoneyZeroActivity.this.startActivity(startMain);
                    System.exit(0);
                    MyMoneyZeroActivity.this.finish();
                }
            });
            alert3.show();
            return 1;
        }
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
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) throws SQLException {
                MyMoneyZeroActivity.this.InputPassword = input.getText().toString();
                if (MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim())) {
                    double DayOutMount = 0.0d;
                    double MonthOutMount = 0.0d;
                    double MonthInMount = 0.0d;
                    MyMoneyZeroActivity.this.InputPassword = MyMoneyZeroActivity.this.AccountPassword;
                    GetNowDate c = new GetNowDate();
                    String NowMonth = c.GetDate().substring(0, 7);
                    String NowDay = c.GetDate();
                    try {
//                        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        String tSDCardPath = String.valueOf(getExternalFilesDir(null));
                        MyMoneyZeroActivity.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                    } catch (Exception e) {
                    }
                    try {
                        MyMoneyZeroActivity.this.SQL = "UPDATE SYSTEM_DATA SET DATA_VALUE = '" + MyMoneyZeroActivity.this.InputPassword.trim() + "' WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(MyMoneyZeroActivity.this.AccountId) + " AND DATA_NOTE = '輸入帳密'";
                        MyMoneyZeroActivity.this.DataDB.execSQL(MyMoneyZeroActivity.this.SQL);
                    } catch (Exception e2) {
                    }
                    if (MyMoneyZeroActivity.this.SumOtherMount) {
                        MyMoneyZeroActivity.this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(MyMoneyZeroActivity.this.AccountId) + " AND DATA_DATE = '" + NowDay.toString() + "' AND (ITEM_CLASS = '支出' OR ITEM_CLASS = '業外支出')";
                    } else {
                        MyMoneyZeroActivity.this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(MyMoneyZeroActivity.this.AccountId) + " AND DATA_DATE = '" + NowDay.toString() + "' AND (ITEM_CLASS = '支出')";
                    }
                    Cursor cursor = MyMoneyZeroActivity.this.DataDB.rawQuery(MyMoneyZeroActivity.this.SQL, null);
                    if (cursor.moveToNext() && cursor.getString(0) != null) {
                        DayOutMount = Double.valueOf(cursor.getString(0)).doubleValue();
                    }
                    cursor.close();
                    if (MyMoneyZeroActivity.this.SumOtherMount) {
                        MyMoneyZeroActivity.this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(MyMoneyZeroActivity.this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '支出' OR ITEM_CLASS = '業外支出')";
                    } else {
                        MyMoneyZeroActivity.this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(MyMoneyZeroActivity.this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '支出')";
                    }
                    Cursor cursor2 = MyMoneyZeroActivity.this.DataDB.rawQuery(MyMoneyZeroActivity.this.SQL, null);
                    if (cursor2.moveToNext() && cursor2.getString(0) != null) {
                        MonthOutMount = Double.valueOf(cursor2.getString(0)).doubleValue();
                    }
                    cursor2.close();
                    if (MyMoneyZeroActivity.this.SumOtherMount) {
                        MyMoneyZeroActivity.this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(MyMoneyZeroActivity.this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '收入' OR ITEM_CLASS = '業外收入')";
                        MyMoneyZeroActivity.this.setTitle("含業外");
                    } else {
                        MyMoneyZeroActivity.this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(MyMoneyZeroActivity.this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '收入')";
                        MyMoneyZeroActivity.this.setTitle("不含業外");
                    }
                    Cursor cursor3 = MyMoneyZeroActivity.this.DataDB.rawQuery(MyMoneyZeroActivity.this.SQL, null);
                    if (cursor3.moveToNext() && cursor3.getString(0) != null) {
                        MonthInMount = Double.valueOf(cursor3.getString(0)).doubleValue();
                    }
                    cursor3.close();
                    DecimalFormat df = new DecimalFormat("#,##0" + MyMoneyZeroActivity.this.MountFormat);
                    MyMoneyZeroActivity.this.DayOutMountView.setText(df.format(DayOutMount));
                    MyMoneyZeroActivity.this.MonthOutMountView.setText(df.format(MonthOutMount));
                    MyMoneyZeroActivity.this.InOutMountView.setText(df.format(MonthInMount - MonthOutMount));
                    MyMoneyZeroActivity.this.ErrorPassword = 0.0d;
                    MyMoneyZeroActivity.this.ShowData();
                    MyMoneyZeroActivity.this.DataDB.close();
                    return;
                }
                try {
//                    String tSDCardPath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath2 = String.valueOf(getExternalFilesDir(null));
                    MyMoneyZeroActivity.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath2) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e3) {
                }
                MyMoneyZeroActivity.this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(MyMoneyZeroActivity.this.AccountId) + " AND DATA_NOTE = '密碼提示'";
                Cursor cursor4 = MyMoneyZeroActivity.this.DataDB.rawQuery(MyMoneyZeroActivity.this.SQL, null);
                if (!cursor4.moveToNext()) {
                    MyMoneyZeroActivity.this.AccountPasswordMessage = "無設定密碼提示";
                } else {
                    MyMoneyZeroActivity.this.AccountPasswordMessage = cursor4.getString(0).trim();
                }
                cursor4.close();
                MyMoneyZeroActivity.this.DataDB.close();
                if (MyMoneyZeroActivity.this.AccountPasswordMessage.trim().equals("")) {
                    MyMoneyZeroActivity.this.AccountPasswordMessage = "無設定密碼提示";
                }
                MyMoneyZeroActivity.this.ShowBox("錯誤!", "帳本密碼輸入錯誤!\n\n密碼提示：" + MyMoneyZeroActivity.this.AccountPasswordMessage.trim());
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        alert.setPositiveButton("確定", OkClick);
        alert.setNegativeButton("取消", CanCelClick);
        alert.show();
    }

    public void ShowDayInOutData(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (!this.AccountPassword.trim().equals(this.InputPassword.trim()) && !this.AccountPassword.trim().equals("")) {
            ShowInputPassword();
            return;
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, DayInOutShow2.class);
//        startActivity(intent1);
//        finish();
    }

    public void ShowAddOut(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (!this.AccountPassword.trim().equals(this.InputPassword.trim()) && !this.AccountPassword.trim().equals("")) {
            ShowInputPassword();
            return;
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, Addout.class);
//        startActivity(intent1);
//        finish();
    }

    public void ShowAddIn(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (!this.AccountPassword.trim().equals(this.InputPassword.trim()) && !this.AccountPassword.trim().equals("")) {
            ShowInputPassword();
            return;
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, Addin.class);
//        startActivity(intent1);
//        finish();
    }

    public void SystemSet(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (!this.AccountPassword.trim().equals(this.InputPassword.trim()) && !this.AccountPassword.trim().equals("")) {
            ShowInputPassword();
            return;
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, SystemSet.class);
//        startActivity(intent1);
//        finish();
    }

    public void SelectAccount(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, AccountSelect.class);
//        startActivity(intent1);
//        finish();
    }

    public void ReportView(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (!this.AccountPassword.trim().equals(this.InputPassword.trim()) && !this.AccountPassword.trim().equals("")) {
            ShowInputPassword();
            return;
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, ReportView.class);
//        startActivity(intent1);
//        finish();
    }

    public void ShowItemSet(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (!this.AccountPassword.trim().equals(this.InputPassword.trim()) && !this.AccountPassword.trim().equals("")) {
            ShowInputPassword();
            return;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("ITEM_CLASS", "資產");
        intent.putExtras(bundle);
//        intent.setClass(this, ItemSet.class);
//        startActivity(intent);
//        finish();
    }

    public void ShowAddtransfer(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (!this.AccountPassword.trim().equals(this.InputPassword.trim()) && !this.AccountPassword.trim().equals("")) {
            ShowInputPassword();
            return;
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, Addtransfer.class);
//        startActivity(intent1);
//        finish();
    }

    public void bbb(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
//        Intent intent1 = new Intent();
//        intent1.setClass(this, Calc.class);
//        startActivity(intent1);
    }

    public void Exit(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("訊息");
        MyAlertDialog.setMessage("您確定是否要離開本系統?");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) throws SQLException {
                try {
//                    String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath = String.valueOf(getExternalFilesDir(null));
                    MyMoneyZeroActivity.this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e2) {
                }
                Cursor cursor = MyMoneyZeroActivity.this.DataDB.rawQuery("SELECT ACCOUNT_ID FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND BOOKED = '1'", null);
                if (cursor.moveToNext()) {
                    MyMoneyZeroActivity.this.AccountId = cursor.getDouble(0);
                    String SQL = "UPDATE SYSTEM_DATA SET DATA_VALUE = " + String.valueOf(MyMoneyZeroActivity.this.AccountId) + " WHERE USER_ID = 'admin' AND DATA_NOTE = '使用帳本'";
                    MyMoneyZeroActivity.this.DataDB.execSQL(SQL);
                    cursor.close();
                } else {
                    cursor.close();
                }
                try {
                    MyMoneyZeroActivity.this.DataDB.execSQL("UPDATE SYSTEM_DATA SET DATA_VALUE = '' WHERE USER_ID = 'admin' AND DATA_NOTE = '輸入帳密'");
                } catch (Exception e3) {
                }
                try {
                    MyMoneyZeroActivity.this.DataDB.execSQL("DELETE FROM SYSTEM_DATA WHERE DATA_NOTE = '輸入帳密' AND DATA_VALUE = ''");
                } catch (Exception e4) {
                }
                MyMoneyZeroActivity.this.DataDB.close();
                try {
                    MyMoneyZeroActivity.this.BackDatabase();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                int pid = Process.myPid();
//                Process.killProcess(pid);
                MyMoneyZeroActivity.this.finish();
            }
        };
        DialogInterface.OnClickListener CanCelClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyAlertDialog.setPositiveButton("確定", OkClick);
        MyAlertDialog.setNegativeButton("取消", CanCelClick);
        MyAlertDialog.show();
    }

    public void ShowBox(String TitleString, String BodyString) {
        if (TitleString == "") {
            TitleString = "訊息";
        }
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle(TitleString);
        MyAlertDialog.setMessage(BodyString);
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyAlertDialog.setPositiveButton("確定", OkClick);
        MyAlertDialog.show();
    }

    private void copyDBtoSDCard() throws IOException {
        GetNowDate c = new GetNowDate();
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
                FileOutputStream tOutStream = new FileOutputStream(String.valueOf(tSDCardPath) + "/MyMoneyZero/DataBack/" + c.GetYYYYMMDD() + ".db");
                byte[] tBuffer = new byte[5120];
                while (true) {
                    int tCount = tISStream.read(tBuffer);
                    if (tCount <= 0) {
                        break;
                    } else {
                        tOutStream.write(tBuffer, 0, tCount);
                    }
                }
                tOutStream.close();
                tISStream.close();
            }
            if (tFile.exists()) {
                FileInputStream tISStream2 = new FileInputStream(tDBFilePath);
                FileOutputStream tOutStream2 = new FileOutputStream(String.valueOf(DataFileDir) + "/mymoney.db");
                byte[] tBuffer2 = new byte[5120];
                while (true) {
                    int tCount2 = tISStream2.read(tBuffer2);
                    if (tCount2 > 0) {
                        tOutStream2.write(tBuffer2, 0, tCount2);
                    } else {
                        tOutStream2.close();
                        tISStream2.close();
                        return;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) throws SQLException {
        switch (keyCode) {
            case 3:
            case 5:
            case 17:
            case 24:
            case 25:
            case 63:
                return true;
            case 4:
                if (event.getDownTime() == event.getEventTime()) {
                    this.mLongPressed = false;
                    if (keyCode == 4) {
                        makeText(this, "提示：再按一次後退鍵將退出本應用程式!", Toast.LENGTH_SHORT).show();
                        this.EndCountStyle++;
                        if (this.EndCountStyle >= 2) {
                            try {
                                ExitProg();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return super.onKeyUp(keyCode, event);
                    }
                    this.mLongPressed = true;
                    return super.onKeyUp(keyCode, event);
                }
                try {
//                    String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String tSDCardPath = String.valueOf(getExternalFilesDir(null));
                    this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
                } catch (Exception e) {
                }
                Cursor cursor = this.DataDB.rawQuery("SELECT ACCOUNT_ID FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND BOOKED = '1'", null);
                if (cursor.moveToNext()) {
                    this.AccountId = cursor.getDouble(0);
                    String SQL = "UPDATE SYSTEM_DATA SET DATA_VALUE = " + String.valueOf(this.AccountId) + " WHERE USER_ID = 'admin' AND DATA_NOTE = '使用帳本'";
                    this.DataDB.execSQL(SQL);
                    cursor.close();
                } else {
                    cursor.close();
                }
                try {
                    this.DataDB.execSQL("UPDATE SYSTEM_DATA SET DATA_VALUE = '' WHERE USER_ID = 'admin' AND DATA_NOTE = '輸入帳密'");
                } catch (Exception e2) {
                }
                this.DataDB.close();
                try {
                    BackDatabase();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                int pid = Process.myPid();
//                Process.killProcess(pid);
                finish();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public void ShowData() {
        double NowMount;
        double NowMount2;
        double NowMount3;
        double NowMount4;
        double NowMount5;
        double NowMount6;
        double NowMount7;
        double NowMount8;
        double NowMount9;
        double NowMount10;
        double NowMount11;
        double NowMount12;
        double EndMount = 0.0d;
        GetNowDate c = new GetNowDate();
        c.GetDate();
        String NowMonth = this.NowMonthShow.getText().toString().substring(0, 7);
        this.DataList = (ListView) findViewById(R.id.DataList);
        DecimalFormat MountDf = new DecimalFormat("#,##0" + this.MountFormat);
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tSDCardPath = String.valueOf(getExternalFilesDir(null));
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        try {
            this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '輸入帳密' AND DATA_VALUE <> ''";
            Cursor cursor = this.DataDB.rawQuery(this.SQL, null);
            if (cursor.moveToNext()) {
                this.InputPassword = cursor.getString(0);
            }
            cursor.close();
        } catch (Exception e2) {
        }
        try {
            this.SQL = "SELECT NPASSWORD FROM NOTEPAD_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId);
            Cursor cursor2 = this.DataDB.rawQuery(this.SQL, null);
            if (cursor2.moveToNext()) {
                this.AccountPassword = cursor2.getString(0).trim();
            }
            cursor2.close();
            if (!this.AccountPassword.trim().isEmpty()) {
                if (!this.AccountPassword.trim().equals(this.InputPassword.trim()) && !this.AccountPassword.trim().isEmpty()) {
                    this.DayOutMountView.setText("0");
                    this.MonthOutMountView.setText("0");
                    this.InOutMountView.setText("0");
                    this.ErrorPassword = 1.0d;
                } else {
                    this.ErrorPassword = 0.0d;
                }
            } else {
                this.ErrorPassword = 0.0d;
            }
            if (this.SumOtherMount) {
                this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '支出' OR ITEM_CLASS = '業外支出')";
            } else {
                this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '支出')";
            }
            Cursor cursor3 = this.DataDB.rawQuery(this.SQL, null);
            if (cursor3.moveToNext()) {
                if (cursor3.getString(0) != null) {
                    this.MonthOutMount = Double.valueOf(cursor3.getString(0)).doubleValue();
                } else {
                    this.MonthOutMount = 0.0d;
                }
            }
            cursor3.close();
            if (this.SumOtherMount) {
                this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '收入' OR ITEM_CLASS = '業外收入')";
            } else {
                this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_DATE LIKE '" + NowMonth.toString() + "%' AND (ITEM_CLASS = '收入')";
            }
            Cursor cursor4 = this.DataDB.rawQuery(this.SQL, null);
            if (cursor4.moveToNext()) {
                if (cursor4.getString(0) != null) {
                    this.MonthInMount = Double.valueOf(cursor4.getString(0)).doubleValue();
                } else {
                    this.MonthInMount = 0.0d;
                }
            }
            cursor4.close();
            ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
            ArrayList<String> ItemListData = new ArrayList<>();
            if (this.ErrorPassword == 0.0d) {
                if (this.ShowMount1) {
                    MountDf.format(0.0d);
                    HashMap<String, Object> mmap1 = new HashMap<>();
                    mmap1.put("ItemImage", Integer.valueOf(R.drawable.ae));
                    mmap1.put("ItemNote", "本日支出金額");
                    mmap1.put("ItemNoteX", "本日支出金額");
                    mmap1.put("Mount", "$" + MountDf.format(this.DayOutMount));
                    listItem.add(mmap1);
                }
                if (this.ShowMount2) {
                    HashMap<String, Object> mmap2 = new HashMap<>();
                    mmap2.put("ItemImage", Integer.valueOf(R.drawable.ae));
                    mmap2.put("ItemNote2", "本週支出金額");
                    mmap2.put("ItemNoteX", "本週支出金額");
                    mmap2.put("Mount2", "$" + MountDf.format(this.WeekOutMount));
                    listItem.add(mmap2);
                }
                if (this.ShowMount3) {
                    HashMap<String, Object> mmap3 = new HashMap<>();
                    mmap3.put("ItemImage", Integer.valueOf(R.drawable.ae));
                    mmap3.put("ItemNote3", "本月支出金額");
                    mmap3.put("ItemNoteX", "本月支出金額");
                    mmap3.put("Mount3", "$" + MountDf.format(this.MonthOutMount));
                    listItem.add(mmap3);
                }
                if (this.ShowMount4) {
                    HashMap<String, Object> mmap4 = new HashMap<>();
                    mmap4.put("ItemImage", Integer.valueOf(R.drawable.ai));
                    mmap4.put("ItemNote1", "本月收入金額");
                    mmap4.put("ItemNoteX", "本月收入金額");
                    mmap4.put("Mount1", "$" + MountDf.format(this.MonthInMount));
                    listItem.add(mmap4);
                }
                if (this.ShowMount5) {
                    HashMap<String, Object> mmap5 = new HashMap<>();
                    mmap5.put("ItemImage", Integer.valueOf(R.drawable.dollar2));
                    mmap5.put("ItemNote4", "本月收支餘額");
                    mmap5.put("ItemNoteX", "本月收支餘額");
                    mmap5.put("Mount4", "$" + MountDf.format(this.MonthInMount - this.MonthOutMount));
                    listItem.add(mmap5);
                }
            } else {
                if (this.ShowMount1) {
                    MountDf.format(0.0d);
                    HashMap<String, Object> mmap12 = new HashMap<>();
                    mmap12.put("ItemImage", Integer.valueOf(R.drawable.ae));
                    mmap12.put("ItemNote", "本日支出金額");
                    mmap12.put("ItemNoteX", "本日支出金額");
                    mmap12.put("Mount", "$" + MountDf.format(0L));
                    listItem.add(mmap12);
                }
                if (this.ShowMount2) {
                    HashMap<String, Object> mmap22 = new HashMap<>();
                    mmap22.put("ItemImage", Integer.valueOf(R.drawable.ae));
                    mmap22.put("ItemNote2", "本週支出金額");
                    mmap22.put("ItemNoteX", "本週支出金額");
                    mmap22.put("Mount2", "$" + MountDf.format(0L));
                    listItem.add(mmap22);
                }
                if (this.ShowMount3) {
                    HashMap<String, Object> mmap32 = new HashMap<>();
                    mmap32.put("ItemImage", Integer.valueOf(R.drawable.ae));
                    mmap32.put("ItemNote3", "本月支出金額");
                    mmap32.put("ItemNoteX", "本月支出金額");
                    mmap32.put("Mount3", "$" + MountDf.format(0L));
                    listItem.add(mmap32);
                }
                if (this.ShowMount4) {
                    HashMap<String, Object> mmap42 = new HashMap<>();
                    mmap42.put("ItemImage", Integer.valueOf(R.drawable.ai));
                    mmap42.put("ItemNote1", "本月收入金額");
                    mmap42.put("ItemNoteX", "本月收入金額");
                    mmap42.put("Mount1", "$" + MountDf.format(0L));
                    listItem.add(mmap42);
                }
                if (this.ShowMount5) {
                    HashMap<String, Object> mmap52 = new HashMap<>();
                    mmap52.put("ItemImage", Integer.valueOf(R.drawable.dollar2));
                    mmap52.put("ItemNote4", "本月收支餘額");
                    mmap52.put("ItemNoteX", "本月收支餘額");
                    mmap52.put("Mount4", "$" + MountDf.format(0L));
                    listItem.add(mmap52);
                }
            }
            if (this.ErrorPassword == 0.0d) {
                this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND CASH = '1' AND ITEM_CLASS = '資產' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                Cursor cursor5 = this.DataDB.rawQuery(this.SQL, null);
                while (cursor5.moveToNext()) {
                    double Exchange = cursor5.getDouble(2);
                    double BeforeMount = cursor5.getDouble(1) * Exchange;
                    if (c.GetDate().substring(0, 7).equals(this.NowMonthShow.getText().toString().substring(0, 7))) {
                        this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor5.getString(0).toString() + "' AND DATA_DATE <= '" + NowMonth + "/" + c.GetDate().substring(8, 10) + "'";
                    } else {
                        this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor5.getString(0).toString() + "' AND DATA_DATE <= '" + NowMonth + "/31'";
                    }
                    Cursor cursor22 = this.DataDB.rawQuery(this.SQL, null);
                    if (cursor22.moveToNext()) {
                        NowMount11 = cursor22.getDouble(0) * Exchange;
                    } else {
                        NowMount11 = 0.0d;
                    }
                    cursor22.close();
                    double ItemMount = BeforeMount + NowMount11;
                    this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '資產' AND PARENT_NOTE <> ITEM_NOTE AND PARENT_NOTE = '" + cursor5.getString(0).toString() + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                    Cursor cursor23 = this.DataDB.rawQuery(this.SQL, null);
                    while (cursor23.moveToNext()) {
                        double Exchange2 = cursor23.getDouble(2);
                        double ItemMount2 = ItemMount + (cursor23.getDouble(1) * Exchange2);
                        if (c.GetDate().substring(0, 7).equals(this.NowMonthShow.getText().toString().substring(0, 7))) {
                            this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor23.getString(0).toString() + "' AND DATA_DATE <= '" + NowMonth + "/" + c.GetDate().substring(8, 10) + "'";
                        } else {
                            this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor23.getString(0).toString() + "' AND DATA_DATE <= '" + NowMonth + "/31'";
                        }
                        Cursor cursor32 = this.DataDB.rawQuery(this.SQL, null);
                        if (cursor32.moveToNext()) {
                            NowMount12 = cursor32.getDouble(0) * Exchange2;
                        } else {
                            NowMount12 = 0.0d;
                        }
                        ItemMount = ItemMount2 + NowMount12;
                        cursor32.close();
                    }
                    cursor23.close();
                    String ShowMount = MountDf.format(ItemMount);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("ItemImage", Integer.valueOf(R.drawable.aa));
                    map.put("ItemNote5", String.valueOf(cursor5.getString(0).toString()) + "餘額");
                    map.put("ItemNoteX", cursor5.getString(0).toString());
                    map.put("ItemClass", "資產");
                    map.put("Mount5", "$" + ShowMount);
                    ItemListData.add(cursor5.getString(0).toString());
                    listItem.add(map);
                    EndMount += ItemMount;
                }
                cursor5.close();
                this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND CASH = '1' AND ITEM_CLASS = '負債' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                Cursor cursor6 = this.DataDB.rawQuery(this.SQL, null);
                while (cursor6.moveToNext()) {
                    double Exchange3 = cursor6.getDouble(2);
                    double BeforeMount2 = cursor6.getDouble(1) * Exchange3;
                    if (c.GetDate().substring(0, 7).equals(this.NowMonthShow.getText().toString().substring(0, 7))) {
                        this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor6.getString(0).toString() + "' AND DATA_DATE <= '" + NowMonth + "/" + c.GetDate().substring(8, 10) + "'";
                    } else {
                        this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor6.getString(0).toString() + "' AND DATA_DATE <= '" + NowMonth + "/31'";
                    }
                    Cursor cursor24 = this.DataDB.rawQuery(this.SQL, null);
                    if (cursor24.moveToNext()) {
                        NowMount9 = cursor24.getDouble(0) * Exchange3;
                    } else {
                        NowMount9 = 0.0d;
                    }
                    cursor24.close();
                    double ItemMount3 = BeforeMount2 + NowMount9;
                    this.SQL = "SELECT ITEM_NOTE,BEFORE_MOUNT,EXCHANGE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '負債' AND PARENT_NOTE <> ITEM_NOTE AND PARENT_NOTE = '" + cursor6.getString(0).toString() + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                    Cursor cursor25 = this.DataDB.rawQuery(this.SQL, null);
                    while (cursor25.moveToNext()) {
                        double Exchange4 = cursor25.getDouble(2);
                        double ItemMount4 = ItemMount3 + (cursor25.getDouble(1) * Exchange4);
                        if (c.GetDate().substring(0, 7).equals(this.NowMonthShow.getText().toString().substring(0, 7))) {
                            this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor25.getString(0).toString() + "' AND DATA_DATE <= '" + NowMonth + "/" + c.GetDate().substring(8, 10) + "'";
                        } else {
                            this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor25.getString(0).toString() + "' AND DATA_DATE <= '" + NowMonth + "/31'";
                        }
                        Cursor cursor33 = this.DataDB.rawQuery(this.SQL, null);
                        if (cursor33.moveToNext()) {
                            NowMount10 = cursor33.getDouble(0) * Exchange4;
                        } else {
                            NowMount10 = 0.0d;
                        }
                        ItemMount3 = ItemMount4 + NowMount10;
                        cursor33.close();
                    }
                    cursor25.close();
                    String ShowMount2 = MountDf.format(ItemMount3);
                    HashMap<String, Object> map2 = new HashMap<>();
                    map2.put("ItemImage", Integer.valueOf(R.drawable.al));
                    map2.put("ItemNote5", String.valueOf(cursor6.getString(0).toString()) + "餘額");
                    map2.put("ItemNoteX", cursor6.getString(0).toString());
                    map2.put("ItemClass", "負債");
                    map2.put("Mount5", "$" + ShowMount2);
                    ItemListData.add(cursor6.getString(0).toString());
                    listItem.add(map2);
                    EndMount += ItemMount3;
                }
                cursor6.close();
                this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND CASH = '1' AND ITEM_CLASS = '收入' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                Cursor cursor7 = this.DataDB.rawQuery(this.SQL, null);
                while (cursor7.moveToNext()) {
                    this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor7.getString(0).toString() + "' AND DATA_DATE BETWEEN '" + NowMonth + "/01' AND '" + NowMonth + "/31'";
                    Cursor cursor26 = this.DataDB.rawQuery(this.SQL, null);
                    if (cursor26.moveToNext()) {
                        NowMount7 = cursor26.getDouble(0);
                    } else {
                        NowMount7 = 0.0d;
                    }
                    cursor26.close();
                    double ItemMount5 = NowMount7;
                    this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '收入' AND PARENT_NOTE <> ITEM_NOTE AND PARENT_NOTE = '" + cursor7.getString(0).toString() + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                    Cursor cursor27 = this.DataDB.rawQuery(this.SQL, null);
                    while (cursor27.moveToNext()) {
                        this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor27.getString(0).toString() + "' AND DATA_DATE BETWEEN '" + NowMonth + "/01' AND '" + NowMonth + "/31'";
                        Cursor cursor34 = this.DataDB.rawQuery(this.SQL, null);
                        if (cursor34.moveToNext()) {
                            NowMount8 = cursor34.getDouble(0);
                        } else {
                            NowMount8 = 0.0d;
                        }
                        ItemMount5 += NowMount8;
                        cursor34.close();
                    }
                    cursor27.close();
                    String ShowMount3 = MountDf.format(ItemMount5);
                    HashMap<String, Object> map3 = new HashMap<>();
                    map3.put("ItemImage", Integer.valueOf(R.drawable.ai));
                    map3.put("ItemNote1", String.valueOf(cursor7.getString(0).toString()) + "總計");
                    map3.put("ItemNoteX", cursor7.getString(0).toString());
                    map3.put("ItemClass", "收入");
                    map3.put("Mount1", "$" + ShowMount3);
                    ItemListData.add(cursor7.getString(0).toString());
                    listItem.add(map3);
                    EndMount += ItemMount5;
                }
                cursor7.close();
                this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND CASH = '1' AND ITEM_CLASS = '支出' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                Cursor cursor8 = this.DataDB.rawQuery(this.SQL, null);
                while (cursor8.moveToNext()) {
                    this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor8.getString(0).toString() + "' AND DATA_DATE BETWEEN '" + NowMonth + "/01' AND '" + NowMonth + "/31'";
                    Cursor cursor28 = this.DataDB.rawQuery(this.SQL, null);
                    if (cursor28.moveToNext()) {
                        NowMount5 = cursor28.getDouble(0);
                    } else {
                        NowMount5 = 0.0d;
                    }
                    cursor28.close();
                    double ItemMount6 = NowMount5;
                    this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '支出' AND PARENT_NOTE <> ITEM_NOTE AND PARENT_NOTE = '" + cursor8.getString(0).toString() + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                    Cursor cursor29 = this.DataDB.rawQuery(this.SQL, null);
                    while (cursor29.moveToNext()) {
                        this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor29.getString(0).toString() + "' AND DATA_DATE BETWEEN '" + NowMonth + "/01' AND '" + NowMonth + "/31'";
                        Cursor cursor35 = this.DataDB.rawQuery(this.SQL, null);
                        if (cursor35.moveToNext()) {
                            NowMount6 = cursor35.getDouble(0);
                        } else {
                            NowMount6 = 0.0d;
                        }
                        ItemMount6 += NowMount6;
                        cursor35.close();
                    }
                    cursor29.close();
                    String ShowMount4 = MountDf.format(ItemMount6);
                    HashMap<String, Object> map4 = new HashMap<>();
                    map4.put("ItemImage", Integer.valueOf(R.drawable.ae));
                    map4.put("ItemNote3", String.valueOf(cursor8.getString(0).toString()) + "總計");
                    map4.put("ItemNoteX", cursor8.getString(0).toString());
                    map4.put("ItemClass", "支出");
                    map4.put("Mount3", "$" + ShowMount4);
                    ItemListData.add(cursor8.getString(0).toString());
                    listItem.add(map4);
                    EndMount += ItemMount6;
                }
                cursor8.close();
                this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND CASH = '1' AND ITEM_CLASS = '業外收入' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                Cursor cursor9 = this.DataDB.rawQuery(this.SQL, null);
                while (cursor9.moveToNext()) {
                    this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor9.getString(0).toString() + "' AND DATA_DATE BETWEEN '" + NowMonth + "/01' AND '" + NowMonth + "/31'";
                    Cursor cursor210 = this.DataDB.rawQuery(this.SQL, null);
                    if (cursor210.moveToNext()) {
                        NowMount3 = cursor210.getDouble(0);
                    } else {
                        NowMount3 = 0.0d;
                    }
                    cursor210.close();
                    double ItemMount7 = NowMount3;
                    this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '業外收入' AND PARENT_NOTE <> ITEM_NOTE AND PARENT_NOTE = '" + cursor9.getString(0).toString() + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                    Cursor cursor211 = this.DataDB.rawQuery(this.SQL, null);
                    while (cursor211.moveToNext()) {
                        this.SQL = "SELECT SUM(OUT_MOUNT - IN_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor211.getString(0).toString() + "' AND DATA_DATE BETWEEN '" + NowMonth + "/01' AND '" + NowMonth + "/31'";
                        Cursor cursor36 = this.DataDB.rawQuery(this.SQL, null);
                        if (cursor36.moveToNext()) {
                            NowMount4 = cursor36.getDouble(0);
                        } else {
                            NowMount4 = 0.0d;
                        }
                        ItemMount7 += NowMount4;
                        cursor36.close();
                    }
                    cursor211.close();
                    String ShowMount5 = MountDf.format(ItemMount7);
                    HashMap<String, Object> map5 = new HashMap<>();
                    map5.put("ItemImage", Integer.valueOf(R.drawable.ai));
                    map5.put("ItemNote1", String.valueOf(cursor9.getString(0).toString()) + "總計");
                    map5.put("ItemNoteX", cursor9.getString(0).toString());
                    map5.put("ItemClass", "業外收入");
                    map5.put("Mount1", "$" + ShowMount5);
                    ItemListData.add(cursor9.getString(0).toString());
                    listItem.add(map5);
                    EndMount += ItemMount7;
                }
                cursor9.close();
                this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND CASH = '1' AND ITEM_CLASS = '業外支出' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                Cursor cursor10 = this.DataDB.rawQuery(this.SQL, null);
                while (cursor10.moveToNext()) {
                    this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor10.getString(0).toString() + "' AND DATA_DATE BETWEEN '" + NowMonth + "/01' AND '" + NowMonth + "/31'";
                    Cursor cursor212 = this.DataDB.rawQuery(this.SQL, null);
                    if (cursor212.moveToNext()) {
                        NowMount = cursor212.getDouble(0);
                    } else {
                        NowMount = 0.0d;
                    }
                    cursor212.close();
                    double ItemMount8 = NowMount;
                    this.SQL = "SELECT ITEM_NOTE FROM ITEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_CLASS = '業外支出' AND PARENT_NOTE <> ITEM_NOTE AND PARENT_NOTE = '" + cursor10.getString(0).toString() + "' AND HIDESTYLE <> '1' ORDER BY MAKE_NO";
                    Cursor cursor213 = this.DataDB.rawQuery(this.SQL, null);
                    while (cursor213.moveToNext()) {
                        this.SQL = "SELECT SUM(IN_MOUNT - OUT_MOUNT) AS AMOUNT FROM MYMONEY_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND ITEM_NOTE = '" + cursor213.getString(0).toString() + "' AND DATA_DATE BETWEEN '" + NowMonth + "/01' AND '" + NowMonth + "/31'";
                        Cursor cursor37 = this.DataDB.rawQuery(this.SQL, null);
                        if (cursor37.moveToNext()) {
                            NowMount2 = cursor37.getDouble(0);
                        } else {
                            NowMount2 = 0.0d;
                        }
                        ItemMount8 += NowMount2;
                        cursor37.close();
                    }
                    cursor213.close();
                    String ShowMount6 = MountDf.format(ItemMount8);
                    HashMap<String, Object> map6 = new HashMap<>();
                    map6.put("ItemImage", Integer.valueOf(R.drawable.ae));
                    map6.put("ItemNote3", String.valueOf(cursor10.getString(0).toString()) + "總計");
                    map6.put("ItemNoteX", cursor10.getString(0).toString());
                    map6.put("ItemClass", "業外支出");
                    map6.put("Mount3", "$" + ShowMount6);
                    ItemListData.add(cursor10.getString(0).toString());
                    listItem.add(map6);
                    EndMount += ItemMount8;
                }
                cursor10.close();
            }
            //可以點選統計收支查看明細
            SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.maindetailshow, new String[]{"ItemImage", "ItemNote", "ItemNote1", "ItemNote2", "ItemNote3", "ItemNote4", "ItemNote5", "ItemNoteX", "ItemClass", "Mount", "Mount1", "Mount2", "Mount3", "Mount4", "Mount5"}, new int[]{R.id.ItemImage, R.id.ItemNote, R.id.ItemNote1, R.id.ItemNote2, R.id.ItemNote3, R.id.ItemNote4, R.id.ItemNote5, R.id.ItemNoteX, R.id.ItemClass, R.id.Mount, R.id.Mount1, R.id.Mount2, R.id.Mount3, R.id.Mount4, R.id.Mount5});
            this.DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: mymoney.zero.MyMoneyZeroActivity.11
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (MyMoneyZeroActivity.this.ShowVibrate.equals("1")) {
                        try {
                            Vibrator vibrator = (Vibrator) MyMoneyZeroActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(30L);
                        } catch (Exception e3) {
                        }
                    }
                    TextView ItemNote = (TextView) view.findViewById(R.id.ItemNoteX);
                    TextView ItemClass = (TextView) view.findViewById(R.id.ItemClass);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    if (ItemNote.getText().toString().trim().equals("本日支出金額")) {
                        if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().isEmpty()) {
                            MyMoneyZeroActivity.this.ShowInputPassword();
                        } else {
                            bundle.putString("DataDay", new GetNowDate().GetDate());
//                            intent.putExtras(bundle);
//                            intent.setClass(MyMoneyZeroActivity.this, maindetailshow1.class);
//                            MyMoneyZeroActivity.this.startActivity(intent);
                            MyMoneyZeroActivity.this.finish();
                            return;
                        }
                    }
                    if (ItemNote.getText().toString().trim().equals("本週支出金額")) {
                        if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                            MyMoneyZeroActivity.this.ShowInputPassword();
                        } else {
                            new GetNowDate();
                            bundle.putString("StartDate", MyMoneyZeroActivity.this.WeekStartDate);
                            bundle.putString("EndDate", MyMoneyZeroActivity.this.WeekEndDate);
//                            intent.putExtras(bundle);
//                            intent.setClass(MyMoneyZeroActivity.this, maindetailshow2.class);
//                            MyMoneyZeroActivity.this.startActivity(intent);
                            MyMoneyZeroActivity.this.finish();
                            return;
                        }
                    }
                    if (ItemNote.getText().toString().trim().equals("本月支出金額")) {
                        if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                            MyMoneyZeroActivity.this.ShowInputPassword();
                        } else {
                            new GetNowDate();
                            bundle.putString("DataMonth", MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(0, 7));
//                            intent.putExtras(bundle);
//                            intent.setClass(MyMoneyZeroActivity.this, maindetailshow3.class);
//                            MyMoneyZeroActivity.this.startActivity(intent);
                            MyMoneyZeroActivity.this.finish();
                            return;
                        }
                    }
                    if (ItemNote.getText().toString().trim().equals("本月收入金額")) {
                        if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                            MyMoneyZeroActivity.this.ShowInputPassword();
                        } else {
                            new GetNowDate();
                            bundle.putString("DataMonth", MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(0, 7));
//                            intent.putExtras(bundle);
//                            intent.setClass(MyMoneyZeroActivity.this, maindetailshow4.class);
//                            MyMoneyZeroActivity.this.startActivity(intent);
                            MyMoneyZeroActivity.this.finish();
                            return;
                        }
                    }
                    if (ItemClass.getText().toString().trim().equals("資產") || ItemClass.getText().toString().trim().equals("負債")) {
                        if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                            MyMoneyZeroActivity.this.ShowInputPassword();
                        } else {
                            GetNowDate c2 = new GetNowDate();
                            int GetYear = Integer.valueOf(MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(0, 4)).intValue();
                            int GetMonth = Integer.valueOf(MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(5, 7)).intValue();
                            int GetMonthEndDay = c2.GetSetMonthDay(GetYear, GetMonth);
                            if (c2.GetDate().substring(0, 7).equals(MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(0, 7))) {
                                bundle.putString("StartDate", String.valueOf(c2.GetDate().substring(0, 7)) + "/01");
                                bundle.putString("EndDate", c2.GetDate());
                            } else {
                                bundle.putString("StartDate", String.valueOf(MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(0, 7)) + "/01");
                                bundle.putString("EndDate", String.valueOf(MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(0, 7)) + "/" + String.valueOf(GetMonthEndDay));
                            }
                            bundle.putString("ItemNote", ItemNote.getText().toString().trim());
//                            intent.putExtras(bundle);
//                            intent.setClass(MyMoneyZeroActivity.this, maindetailshow5.class);
//                            MyMoneyZeroActivity.this.startActivity(intent);
                            MyMoneyZeroActivity.this.finish();
                            return;
                        }
                    }
                    if (ItemClass.getText().toString().trim().equals("收入") || ItemClass.getText().toString().trim().equals("業外收入") || ItemClass.getText().toString().trim().equals("支出") || ItemClass.getText().toString().trim().equals("業外支出")) {
                        if (!MyMoneyZeroActivity.this.AccountPassword.trim().equals(MyMoneyZeroActivity.this.InputPassword.trim()) && !MyMoneyZeroActivity.this.AccountPassword.trim().equals("")) {
                            MyMoneyZeroActivity.this.ShowInputPassword();
                            return;
                        }
                        new GetNowDate();
                        bundle.putString("DataMonth", MyMoneyZeroActivity.this.NowMonthShow.getText().toString().substring(0, 7));
                        bundle.putString("ItemNote", ItemNote.getText().toString().trim());
//                        intent.putExtras(bundle);
//                        intent.setClass(MyMoneyZeroActivity.this, maindetailshow6.class);
//                        MyMoneyZeroActivity.this.startActivity(intent);
                        MyMoneyZeroActivity.this.finish();
                    }
                }
            });
            this.DataList.setDividerHeight(0);
            this.DataList.setAdapter((ListAdapter) listItemAdapter);
        } catch (Exception e3) {
        }
        this.DataDB.close();
    }

    public void CreateDatabase2() throws SQLException {
        String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            ///storage/emulated/0/Android/data/com.coobila.mymoney/files/mymoney.db
//            this.DataDB = SQLiteDatabase.openOrCreateDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", (SQLiteDatabase.CursorFactory) null);
            this.DataDB = SQLiteDatabase.openOrCreateDatabase("storage/emulated/0/Android/data/com.coobila.mymoney/files/mymoney.db", (SQLiteDatabase.CursorFactory) null);
        } catch (Exception e) {
        }
        this.SQL = "CREATE TABLE PROJECT_SET(USER_ID char(20) not null,ACCOUNT_ID int,PROJECT_ID char(20),PROJECT_NOTE char(40))";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE UNIQUE INDEX PROJECT ON PROJECT_SET(USER_ID,ACCOUNT_ID,PROJECT_ID)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE TABLE PAY_COLL_NAME(USER_ID char(20) not null,ACCOUNT_ID int,MAKE_NO double,COLL_NAME char(40) not null)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE INDEX PayCollIndex ON PAY_COLL_NAME(USER_ID,ACCOUNT_ID,MAKE_NO)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE TABLE OFTEN_NOTE(USER_ID char(20) not null,ACCOUNT_ID int,MAKE_NO double,ITEM_CLASS char(10) not null,ITEM_NOTE char(40) not null,DATA_NOTE char(60) not null)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE INDEX OftenIndex ON OFTEN_NOTE(USER_ID,ACCOUNT_ID,MAKE_NO)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE TABLE SYSTEM_DATA(USER_ID char(20) not null,ACCOUNT_ID int,DATA_NOTE char(40) not null,DATA_VALUE char(180) not null)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE TABLE NOTEPAD_DATA(USER_ID char(20) not null,ACCOUNT_ID int,ACCOUNT_NOTE char(40) not null,BOOKED char(1) not null,NPASSWORD char(20) not null)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE TABLE ITEM_DATA(USER_ID char(20) not null,ACCOUNT_ID int,MAKE_NO double,ITEM_NOTE char(40) not null,ITEM_CLASS char(10) not null,BEFORE_MOUNT double,ITEM_STYLE char(10) not null,PARENT_NOTE char(40) not null,EXCHANGE double,CHARGE char(1) not null)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE TABLE YEAR_SPEND(USER_ID char(20) not null,ACCOUNT_ID int,YEAR_MONTH char(7) not null,ITEM_NOTE char(40) not null,SPEND_MOUNT double)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE UNIQUE INDEX Index_Make_no ON ITEM_DATA(USER_ID,ACCOUNT_ID,ITEM_CLASS,MAKE_NO)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE TABLE MYMONEY_DATA(USER_ID char(20) not null,ACCOUNT_ID int,MAKE_NO double,ITEM_CLASS char(10) not null,DATA_DATE char(10) not null,ITEM_NOTE char(40) not null,IN_MOUNT double,OUT_MOUNT double,DATA_NOTE char(60) not null,INVOICE_NO char(10) not null,DATA_NO char(1) not null,PAY_COLL_NAME char(50) not null,PROJECT_ID char(20) not null,EXCHANGE double,LINK_PC char(1) not null,PC_MAKE_NO double,DATA_NOTE2 MEMO)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE INDEX Index_System_Data ON SYSTEM_DATA(USER_ID,ACCOUNT_ID,DATA_NOTE)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE UNIQUE INDEX Index_Notepad ON NOTEPAD_DATA(USER_ID,ACCOUNT_ID)";
        this.DataDB.execSQL(this.SQL);
        this.SQL = "CREATE UNIQUE INDEX Index_Year_Spend ON YEAR_SPEND(USER_ID,ACCOUNT_ID,YEAR_MONTH,ITEM_NOTE)";
        this.DataDB.execSQL(this.SQL);
        try {
            this.SQL = "CREATE INDEX Index_Make_no2 ON ITEM_DATA(USER_ID,ACCOUNT_ID,DATA_DATE)";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e2) {
        }
        try {
            this.SQL = "ALTER TABLE MYMONEY_DATA ADD PC_MAKE_NO DOUBLE";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e3) {
        }
        try {
            this.SQL = "ALTER TABLE MYMONEY_DATA ADD MODIFY_DATE CHAR(10)";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e4) {
        }
        try {
            this.SQL = "ALTER TABLE MYMONEY_DATA ADD DATA_NOTE2 MEMO";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e5) {
        }
        try {
            this.SQL = "UPDATE MYMONEY_DATA SET DATA_NOTE2 = '' WHERE DATA_NOTE2 IS NULL";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e6) {
        }
        try {
            this.SQL = "ALTER TABLE MYMONEY_DATA ADD MODIFY_TIME CHAR(10)";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e7) {
        }
        try {
            this.SQL = "ALTER TABLE MYMONEY_DATA ADD DATA_KEY CHAR(20)";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e8) {
        }
        try {
            this.SQL = "ALTER TABLE MYMONEY_DATA ADD EXPORT CHAR(1)";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e9) {
        }
        try {
            this.SQL = "ALTER TABLE ITEM_DATA ADD HIDESTYLE CHAR(1)";
            this.DataDB.execSQL(this.SQL);
            this.SQL = "UPDATE ITEM_DATA SET HIDESTYLE = '' WHERE HIDESTYLE IS NULL";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e10) {
        }
        try {
            this.SQL = "ALTER TABLE ITEM_DATA ADD CASH CHAR(1)";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e11) {
        }
        try {
            this.SQL = "CREATE INDEX DATA_DATE ON MYMONEY_DATA(USER_ID,ACCOUNT_ID,DATA_DATE,ITEM_NOTE)";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e12) {
        }
        try {
            this.SQL = "CREATE TABLE MYMONEY_DATA_KILL (USER_ID CHAR(20) NOT NULL,ACCOUNT_ID INT,DATA_KEY CHAR(20))";
            this.DataDB.execSQL(this.SQL);
        } catch (Exception e13) {
            this.SQL = "UPDATE ITEM_DATA SET CASH = '1' WHERE ITEM_NOTE = '現金' AND CASH IS NULL";
            this.DataDB.execSQL(this.SQL);
        }
        Cursor cursor = this.DataDB.rawQuery("SELECT BOOKED,ACCOUNT_ID FROM NOTEPAD_DATA WHERE USER_ID = 'admin' ORDER BY BOOKED DESC", null);
        if (cursor.moveToNext()) {
            cursor.getString(0).equals("1");
            cursor.close();
            return;
        }
        cursor.close();
        this.DataDB.execSQL("INSERT INTO NOTEPAD_DATA (USER_ID,ACCOUNT_ID,ACCOUNT_NOTE,BOOKED,NPASSWORD) VALUES ('admin',1,'我的記帳簿','1','')");
        this.DataDB.execSQL("INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin',0,'使用帳本','1')");
        this.DataDB.execSQL("INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin',1,'金額小數點','0')");
        this.DataDB.execSQL("INSERT INTO SYSTEM_DATA (USER_ID,ACCOUNT_ID,DATA_NOTE,DATA_VALUE) VALUES ('admin',1,'匯率小數點','3')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'現金','資產',0,'','現金',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,2,'銀行存款','資產',0,'','銀行存款',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,3,'定期存款','資產',0,'','定期存款',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,4,'股票投資','資產',0,'','股票投資',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,5,'基金投資','資產',0,'','基金投資',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,6,'iCash卡','資產',0,'','iCash卡',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,7,'悠遊卡','資產',0,'','悠遊卡',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'信用卡','負債',0,'','信用卡',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,2,'房屋貸款','負債',0,'','房屋貸款',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'薪資收入','收入',0,'','薪資收入',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,2,'兼職收入','收入',0,'','兼職收入',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,3,'獎金收入','收入',0,'','獎金收入',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,4,'三節獎金','收入',0,'','獎金收入',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,5,'年終獎金','收入',0,'','獎金收入',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,6,'業績獎金','收入',0,'','獎金收入',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,7,'利息收入','收入',0,'','利息收入',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,8,'租金收入','收入',0,'','租金收入',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,9,'其它收入','收入',0,'','其它收入',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'生活飲食','支出',0,'','生活飲食',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,2,'早餐','支出',0,'','生活飲食',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,3,'午餐','支出',0,'','生活飲食',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,4,'晚餐','支出',0,'','生活飲食',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,5,'宵夜','支出',0,'','生活飲食',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,6,'飲料','支出',0,'','生活飲食',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,7,'菸酒','支出',0,'','生活飲食',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,8,'蔬菜水果','支出',0,'','生活飲食',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,9,'點心零食','支出',0,'','生活飲食',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,10,'休閒娛樂','支出',0,'','休閒娛樂',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,11,'聯誼聚餐','支出',0,'','休閒娛樂',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,12,'運動健身','支出',0,'','休閒娛樂',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,13,'影片租購','支出',0,'','休閒娛樂',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,14,'電影觀賞','支出',0,'','休閒娛樂',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,15,'戶外郊遊','支出',0,'','休閒娛樂',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,16,'旅遊度假','支出',0,'','休閒娛樂',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,17,'寵物寶貝','支出',0,'','休閒娛樂',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,18,'教育學習','支出',0,'','教育學習',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,19,'教育學費','支出',0,'','教育學習',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,20,'上課進修','支出',0,'','教育學習',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,21,'書籍購買','支出',0,'','教育學習',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,22,'才藝學習','支出',0,'','教育學習',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,23,'安親補習','支出',0,'','教育學習',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,24,'行車交通','支出',0,'','行車交通',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,25,'機車油費','支出',0,'','行車交通',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,26,'汽車油費','支出',0,'','行車交通',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,27,'捷運搭乘','支出',0,'','行車交通',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,28,'公車搭乘','支出',0,'','行車交通',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,29,'停車費','支出',0,'','行車交通',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,30,'計程車','支出',0,'','行車交通',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,31,'過路費','支出',0,'','行車交通',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,32,'居家物業','支出',0,'','居家物業',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,33,'水費','支出',0,'','居家物業',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,34,'電費','支出',0,'','居家物業',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,35,'瓦斯費','支出',0,'','居家物業',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,36,'管理費','支出',0,'','居家物業',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,37,'房屋租金','支出',0,'','居家物業',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,38,'房貸利息','支出',0,'','居家物業',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,39,'住屋裝修','支出',0,'','居家物業',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,40,'住屋傢飾','支出',0,'','居家物業',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,41,'行動通訊','支出',0,'','行動通訊',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,42,'電話費','支出',0,'','行動通訊',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,43,'手機費','支出',0,'','行動通訊',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,44,'網路費','支出',0,'','行動通訊',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,45,'有線電視費','支出',0,'','行動通訊',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,46,'MOD網路電視費','支出',0,'','行動通訊',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,47,'書報雜誌','支出',0,'','書報雜誌',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,48,'報紙','支出',0,'','書報雜誌',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,49,'雜誌','支出',0,'','書報雜誌',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,50,'定期刊物','支出',0,'','書報雜誌',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,51,'人情往來','支出',0,'','人情往來',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,52,'婚喪喜慶','支出',0,'','人情往來',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,53,'送禮請客','支出',0,'','人情往來',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,54,'孝親費用','支出',0,'','人情往來',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,55,'慈善捐款','支出',0,'','人情往來',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,56,'服飾美容','支出',0,'','服飾美容',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,57,'置裝費','支出',0,'','服飾美容',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,58,'化妝品','支出',0,'','服飾美容',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,59,'保養品','支出',0,'','服飾美容',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,60,'醫療保健','支出',0,'','醫療保健',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,61,'醫療掛號','支出',0,'','醫療保健',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,62,'健康檢查','支出',0,'','醫療保健',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,63,'醫療藥物','支出',0,'','醫療保健',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,64,'保健食品','支出',0,'','醫療保健',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,65,'美容養生','支出',0,'','醫療保健',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,66,'金融保險','支出',0,'','金融保險',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,67,'汽機車險','支出',0,'','金融保險',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,68,'人壽險','支出',0,'','金融保險',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,69,'產物險','支出',0,'','金融保險',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,70,'勞保費','支出',0,'','金融保險',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,71,'健保費','支出',0,'','金融保險',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,72,'稅金','支出',0,'','稅金',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,73,'燃料稅','支出',0,'','稅金',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,74,'牌照稅','支出',0,'','稅金',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,75,'綜所稅','支出',0,'','稅金',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,76,'房屋稅','支出',0,'','稅金',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,77,'地價稅','支出',0,'','稅金',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,78,'其它支出','支出',0,'','其它支出',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,79,'提款手續費','支出',0,'','其它支出',1,'1','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,80,'轉帳手續費','支出',0,'','其它支出',1,'1','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'投資收入','業外收入',0,'','投資收入',1,'0','')");
        this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'投資損失','業外支出',0,'','投資損失',1,'0','')");
        try {
            this.DataDB.execSQL("INSERT INTO ITEM_DATA (USER_ID,ACCOUNT_ID,MAKE_NO,ITEM_NOTE,ITEM_CLASS,BEFORE_MOUNT,ITEM_STYLE,PARENT_NOTE,EXCHANGE,CHARGE,HIDESTYLE) VALUES ('admin',1,1,'Change','其它',0,'','Change',1,'0','')");
        } catch (Exception e14) {
        }
        this.AccountId = 1.0d;
    }

}