package com.coobila.mymoney;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/* loaded from: classes.dex */
public class Calc extends Activity {
    private double AccountId;
    private SQLiteDatabase DataDB;
    private String InMount;
    private String InMountRec;
    private EditText Mount;
    private String SQL;
    private String ShowVibrate = "";
    private Cursor cursor;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) throws SQLException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc);
        setTitle("帳務小管家ZERO");
        this.AccountId += 0.0d;
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
        this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '強制直式顯示'";
        this.cursor = this.DataDB.rawQuery(this.SQL, null);
        if (this.cursor.moveToNext() && this.cursor.getString(0).trim().equals("1")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        this.cursor.close();
        this.SQL = "DELETE FROM ITEM_DATA WHERE USER_ID = 'system'";
        this.DataDB.execSQL(this.SQL);
        this.Mount = (EditText) findViewById(R.id.Mount);
        Bundle bundle = getIntent().getExtras();
        this.InMount = bundle.getString("InMount");
        this.InMountRec = this.InMount;
        this.Mount.setText(this.InMount);
        this.Mount.setInputType(0);
    }

    @SuppressLint("SetTextI18n")
    public void b0(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (this.Mount.getText().toString().trim().equals(this.InMountRec)) {
            this.InMountRec = "";
            this.Mount.setText("");
        }
        if (this.Mount.getText().toString().trim().equals("0")) {
            this.Mount.setText("0");
        } else {
            this.Mount.setText(((Object) this.Mount.getText()) + "0");
        }
        this.Mount.setSelection(this.Mount.length());
    }

    public void b1(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (this.Mount.getText().toString().trim().equals(this.InMountRec)) {
            this.InMountRec = "";
            this.Mount.setText("");
        }
        if (this.Mount.getText().toString().trim().equals("0")) {
            this.Mount.setText("1");
        } else {
            this.Mount.setText(((Object) this.Mount.getText()) + "1");
        }
        this.Mount.setSelection(this.Mount.length());
    }

    public void b2(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (this.Mount.getText().toString().trim().equals(this.InMountRec)) {
            this.InMountRec = "";
            this.Mount.setText("");
        }
        if (this.Mount.getText().toString().trim().equals("0")) {
            this.Mount.setText("2");
        } else {
            this.Mount.setText(((Object) this.Mount.getText()) + "2");
        }
        this.Mount.setSelection(this.Mount.length());
    }

    @SuppressLint("SetTextI18n")
    public void b3(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (this.Mount.getText().toString().trim().equals(this.InMountRec)) {
            this.InMountRec = "";
            this.Mount.setText("");
        }
        if (this.Mount.getText().toString().trim().equals("0")) {
            this.Mount.setText("3");
        } else {
            this.Mount.setText(((Object) this.Mount.getText()) + "3");
        }
        this.Mount.setSelection(this.Mount.length());
    }

    @SuppressLint("SetTextI18n")
    public void b4(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (this.Mount.getText().toString().trim().equals(this.InMountRec)) {
            this.InMountRec = "";
            this.Mount.setText("");
        }
        if (this.Mount.getText().toString().trim().equals("0")) {
            this.Mount.setText("4");
        } else {
            this.Mount.setText(((Object) this.Mount.getText()) + "4");
        }
        this.Mount.setSelection(this.Mount.length());
    }

    @SuppressLint("SetTextI18n")
    public void b5(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (this.Mount.getText().toString().trim().equals(this.InMountRec)) {
            this.InMountRec = "";
            this.Mount.setText("");
        }
        if (this.Mount.getText().toString().trim().equals("0")) {
            this.Mount.setText("5");
        } else {
            this.Mount.setText(((Object) this.Mount.getText()) + "5");
        }
        this.Mount.setSelection(this.Mount.length());
    }

    public void b6(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (this.Mount.getText().toString().trim().equals(this.InMountRec)) {
            this.InMountRec = "";
            this.Mount.setText("");
        }
        if (this.Mount.getText().toString().trim().equals("0")) {
            this.Mount.setText("6");
        } else {
            this.Mount.setText(((Object) this.Mount.getText()) + "6");
        }
        this.Mount.setSelection(this.Mount.length());
    }

    public void b7(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (this.Mount.getText().toString().trim().equals(this.InMountRec)) {
            this.InMountRec = "";
            this.Mount.setText("");
        }
        if (this.Mount.getText().toString().trim().equals("0")) {
            this.Mount.setText("7");
        } else {
            this.Mount.setText(((Object) this.Mount.getText()) + "7");
        }
        this.Mount.setSelection(this.Mount.length());
    }

    public void b8(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (this.Mount.getText().toString().trim().equals(this.InMountRec)) {
            this.InMountRec = "";
            this.Mount.setText("");
        }
        if (this.Mount.getText().toString().trim().equals("0")) {
            this.Mount.setText("8");
        } else {
            this.Mount.setText(((Object) this.Mount.getText()) + "8");
        }
        this.Mount.setSelection(this.Mount.length());
    }

    public void b9(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (this.Mount.getText().toString().trim().equals(this.InMountRec)) {
            this.InMountRec = "";
            this.Mount.setText("");
        }
        if (this.Mount.getText().toString().trim().equals("0")) {
            this.Mount.setText("9");
        } else {
            this.Mount.setText(((Object) this.Mount.getText()) + "9");
        }
        this.Mount.setSelection(this.Mount.length());
    }

    public void b00(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        if (this.Mount.getText().toString().trim().equals("0")) {
            this.Mount.setText("00");
        } else {
            this.Mount.setText(((Object) this.Mount.getText()) + "00");
        }
        this.Mount.setSelection(this.Mount.length());
    }

    public void bdot(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(((Object) this.Mount.getText()) + ".");
        this.Mount.setSelection(this.Mount.length());
    }

    public void badd(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(((Object) this.Mount.getText()) + "+");
        this.Mount.setSelection(this.Mount.length());
    }

    public void bsub(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // 檢查裝置是否支援震動
                if (vibrator != null && vibrator.hasVibrator()) {
                    // 判斷目前的 Android 版本
                    // 若為 API 26（Android 8.0 Oreo）或更高版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // 使用新的 API 方法
                        vibrator.vibrate(VibrationEffect.createOneShot(30L, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        // 舊版本（API 26 以下）使用原本的寫法
                        // 也就是你的舊程式碼，它在 Android 16 上是完全正常的
                        vibrator.vibrate(30L);
                    }
                }
//                Vibrator vibrator = (Vibrator) getSystemService("vibrator");
//                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(((Object) this.Mount.getText()) + "-");
        this.Mount.setSelection(this.Mount.length());
    }

    public void bm(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // 檢查裝置是否支援震動
                if (vibrator != null && vibrator.hasVibrator()) {
                    // 判斷目前的 Android 版本
                    // 若為 API 26（Android 8.0 Oreo）或更高版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // 使用新的 API 方法
                        vibrator.vibrate(VibrationEffect.createOneShot(30L, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        // 舊版本（API 26 以下）使用原本的寫法
                        // 也就是你的舊程式碼，它在 Android 16 上是完全正常的
                        vibrator.vibrate(30L);
                    }
                }
//                Vibrator vibrator = (Vibrator) getSystemService("vibrator");
//                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(((Object) this.Mount.getText()) + "*");
        this.Mount.setSelection(this.Mount.length());
    }

    public void br(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // 檢查裝置是否支援震動
                if (vibrator != null && vibrator.hasVibrator()) {
                    // 判斷目前的 Android 版本
                    // 若為 API 26（Android 8.0 Oreo）或更高版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // 使用新的 API 方法
                        vibrator.vibrate(VibrationEffect.createOneShot(30L, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        // 舊版本（API 26 以下）使用原本的寫法
                        // 也就是你的舊程式碼，它在 Android 16 上是完全正常的
                        vibrator.vibrate(30L);
                    }
                }
//                Vibrator vibrator = (Vibrator) getSystemService("vibrator");
//                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(((Object) this.Mount.getText()) + "/");
        this.Mount.setSelection(this.Mount.length());
    }

    public void bc(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // 檢查裝置是否支援震動
                if (vibrator != null && vibrator.hasVibrator()) {
                    // 判斷目前的 Android 版本
                    // 若為 API 26（Android 8.0 Oreo）或更高版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // 使用新的 API 方法
                        vibrator.vibrate(VibrationEffect.createOneShot(30L, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        // 舊版本（API 26 以下）使用原本的寫法
                        // 也就是你的舊程式碼，它在 Android 16 上是完全正常的
                        vibrator.vibrate(30L);
                    }
                }
//                Vibrator vibrator = (Vibrator) getSystemService("vibrator");
//                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText("0");
    }

    public void bb(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // 檢查裝置是否支援震動
                if (vibrator != null && vibrator.hasVibrator()) {
                    // 判斷目前的 Android 版本
                    // 若為 API 26（Android 8.0 Oreo）或更高版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // 使用新的 API 方法
                        vibrator.vibrate(VibrationEffect.createOneShot(30L, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        // 舊版本（API 26 以下）使用原本的寫法
                        // 也就是你的舊程式碼，它在 Android 16 上是完全正常的
                        vibrator.vibrate(30L);
                    }
                }
//                Vibrator vibrator = (Vibrator) getSystemService("vibrator");
//                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        String TempMount = this.Mount.getText().toString();
        if (TempMount.length() > 0) {
            this.Mount.setText(TempMount.substring(0, TempMount.length() - 1));
            this.Mount.setSelection(this.Mount.length());
        }
    }

    public void bq(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // 檢查裝置是否支援震動
                if (vibrator != null && vibrator.hasVibrator()) {
                    // 判斷目前的 Android 版本
                    // 若為 API 26（Android 8.0 Oreo）或更高版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // 使用新的 API 方法
                        vibrator.vibrate(VibrationEffect.createOneShot(30L, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        // 舊版本（API 26 以下）使用原本的寫法
                        // 也就是你的舊程式碼，它在 Android 16 上是完全正常的
                        vibrator.vibrate(30L);
                    }
                }
//                Vibrator vibrator = (Vibrator) getSystemService("vibrator");
//                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        MathMount();
    }

    public void MathMount() {
        String TempMount = this.Mount.getText().toString();
        String TempMount2 = ("0" + TempMount).replace(",", "");
        while (TempMount2.indexOf("+-") > 0) {
            try {
                TempMount2 = TempMount2.replace("+-", "-");
            } catch (Exception e) {
            }
        }
        while (TempMount2.indexOf("-+") > 0) {
            TempMount2 = TempMount2.replace("-+", "+");
        }
        while (TempMount2.indexOf("++") > 0) {
            TempMount2 = TempMount2.replace("++", "+");
        }
        while (TempMount2.indexOf("--") > 0) {
            TempMount2 = TempMount2.replace("--", "-");
        }
        while (TempMount2.indexOf("*/") > 0) {
            TempMount2 = TempMount2.replace("*/", "/");
        }
        while (TempMount2.indexOf("/*") > 0) {
            TempMount2 = TempMount2.replace("/*", "*");
        }
        while (TempMount2.indexOf("**") > 0) {
            TempMount2 = TempMount2.replace("**", "*");
        }
        while (TempMount2.indexOf("//") > 0) {
            TempMount2 = TempMount2.replace("//", "/");
        }
        TempMount2 = TempMount2.replaceAll("/", "*1.0/");
        try {
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
            this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND DATA_NOTE = '使用帳本'";
            this.cursor = this.DataDB.rawQuery(this.SQL, null);
            this.AccountId = 0.0d;
            if (this.cursor.moveToNext()) {
                this.AccountId = this.cursor.getDouble(0);
            }
            this.cursor.close();
            this.SQL = "SELECT DATA_VALUE FROM SYSTEM_DATA WHERE USER_ID = 'admin' AND ACCOUNT_ID = " + String.valueOf(this.AccountId) + " AND DATA_NOTE = '金額小數點'";
            this.cursor = this.DataDB.rawQuery(this.SQL, null);
            int MountSub = 0;
            if (this.cursor.moveToNext()) {
                MountSub = (int) this.cursor.getDouble(0);
            }
            this.cursor.close();
            if (MountSub > 0) {
                String MountFormat = ".";
                for (int Loop_I = 1; Loop_I <= MountSub; Loop_I++) {
                    MountFormat = String.valueOf(MountFormat) + "0";
                }
            }
        } catch (Exception e2) {
        }
        try {
            this.SQL = "SELECT ROUND(" + TempMount2 + "+0,5) from NOTEPAD_DATA";
            this.cursor = this.DataDB.rawQuery(this.SQL, null);
        } catch (Exception e3) {
        }
        if (this.cursor.moveToNext()) {
            this.Mount.setText(String.valueOf(this.cursor.getDouble(0)));
        } else {
            this.Mount.setText("");
        }
        this.cursor.close();
    }

    public void bref(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // 檢查裝置是否支援震動
                if (vibrator != null && vibrator.hasVibrator()) {
                    // 判斷目前的 Android 版本
                    // 若為 API 26（Android 8.0 Oreo）或更高版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // 使用新的 API 方法
                        vibrator.vibrate(VibrationEffect.createOneShot(30L, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        // 舊版本（API 26 以下）使用原本的寫法
                        // 也就是你的舊程式碼，它在 Android 16 上是完全正常的
                        vibrator.vibrate(30L);
                    }
                }
//                Vibrator vibrator = (Vibrator) getSystemService("vibrator");
//                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(this.InMountRec);
    }

    public void SaveExit(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // 檢查裝置是否支援震動
                if (vibrator != null && vibrator.hasVibrator()) {
                    // 判斷目前的 Android 版本
                    // 若為 API 26（Android 8.0 Oreo）或更高版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // 使用新的 API 方法
                        vibrator.vibrate(VibrationEffect.createOneShot(30L, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        // 舊版本（API 26 以下）使用原本的寫法
                        // 也就是你的舊程式碼，它在 Android 16 上是完全正常的
                        vibrator.vibrate(30L);
                    }
                }
//                Vibrator vibrator = (Vibrator) getSystemService("vibrator");
//                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        MathMount();
        Intent intent = getIntent();
        Bundle bundle2 = new Bundle();
        bundle2.putString("Mount", this.Mount.getText().toString());
        intent.putExtras(bundle2);
        setResult(-1, intent);
        finish();
    }

    public void Exit(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // 檢查裝置是否支援震動
                if (vibrator != null && vibrator.hasVibrator()) {
                    // 判斷目前的 Android 版本
                    // 若為 API 26（Android 8.0 Oreo）或更高版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // 使用新的 API 方法
                        vibrator.vibrate(VibrationEffect.createOneShot(30L, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        // 舊版本（API 26 以下）使用原本的寫法
                        // 也就是你的舊程式碼，它在 Android 16 上是完全正常的
                        vibrator.vibrate(30L);
                    }
                }
//                Vibrator vibrator = (Vibrator) getSystemService("vibrator");
//                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        Intent intent = getIntent();
        Bundle bundle2 = new Bundle();
        bundle2.putString("Mount", this.InMountRec);
        intent.putExtras(bundle2);
        setResult(-1, intent);
        finish();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 3:
                return true;
            case 4:
                Intent intent = getIntent();
                Bundle bundle2 = new Bundle();
                bundle2.putString("Mount", this.InMountRec);
                intent.putExtras(bundle2);
                setResult(-1, intent);
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}