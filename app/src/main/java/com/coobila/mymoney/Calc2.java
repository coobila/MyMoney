package com.coobila.mymoney;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/* loaded from: classes.dex */
public class Calc2 extends Activity {
    private double AccountId;
    private SQLiteDatabase DataDB;
    private String InMount;
    private String InMountRec;
    private EditText Mount;
    private String SQL;
    private String ShowVibrate = "";
    private Cursor cursor;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc2);
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
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        this.cursor.close();
        this.Mount = (EditText) findViewById(R.id.Mount);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
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
        this.Mount.setText(((Object) this.Mount.getText()) + "0");
        this.Mount.setSelection(this.Mount.length());
    }

    @SuppressLint("SetTextI18n")
    public void b1(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(((Object) this.Mount.getText()) + "1");
        this.Mount.setSelection(this.Mount.length());
    }

    @SuppressLint("SetTextI18n")
    public void b2(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(((Object) this.Mount.getText()) + "2");
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
        this.Mount.setText(((Object) this.Mount.getText()) + "3");
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
        this.Mount.setText(((Object) this.Mount.getText()) + "4");
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
        this.Mount.setText(((Object) this.Mount.getText()) + "5");
        this.Mount.setSelection(this.Mount.length());
    }

    @SuppressLint("SetTextI18n")
    public void b6(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(((Object) this.Mount.getText()) + "6");
        this.Mount.setSelection(this.Mount.length());
    }

    @SuppressLint("SetTextI18n")
    public void b7(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(((Object) this.Mount.getText()) + "7");
        this.Mount.setSelection(this.Mount.length());
    }

    @SuppressLint("SetTextI18n")
    public void b8(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(((Object) this.Mount.getText()) + "8");
        this.Mount.setSelection(this.Mount.length());
    }

    @SuppressLint("SetTextI18n")
    public void b9(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText(((Object) this.Mount.getText()) + "9");
        this.Mount.setSelection(this.Mount.length());
    }

    public void bc(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        this.Mount.setText("");
    }

    public void bb(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
        String TempMount = this.Mount.getText().toString();
        if (!TempMount.isEmpty()) {
            this.Mount.setText(TempMount.substring(0, TempMount.length() - 1));
            this.Mount.setSelection(this.Mount.length());
        }
    }

    public void SaveExit(View CvClick) {
        if (this.ShowVibrate.equals("1")) {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30L);
            } catch (Exception e) {
            }
        }
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
                vibrator.vibrate(30L);
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