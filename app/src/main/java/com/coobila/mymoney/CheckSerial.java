package com.coobila.mymoney;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/* loaded from: classes.dex */
public class CheckSerial {
    private SQLiteDatabase DataDB;
    private Cursor cursor;

    public boolean GetInputSerial() {
        boolean CheckStyle = false;
        String RegMail = new StringBuilder(String.valueOf("")).toString();
        String RegKey = "";
        try {
//            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            String tSDCardPath =  String.valueOf(FileUtil.getAppExternalFolder("MyMoneyZero").getAbsolutePath());
//            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.cursor = this.DataDB.rawQuery("SELECT DATA_VALUE FROM SYSTEM_DATA WHERE DATA_NOTE = '註冊Mail'", null);
        if (this.cursor.moveToNext()) {
            RegMail = this.cursor.getString(0);
        }
        this.cursor.close();
        this.cursor = this.DataDB.rawQuery("SELECT DATA_VALUE FROM SYSTEM_DATA WHERE DATA_NOTE = '註冊序號'", null);
        if (this.cursor.moveToNext()) {
            RegKey = this.cursor.getString(0);
        }
        Thread thread = new Thread() { // from class: mymoney.zero.CheckSerial.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    CheckSerial.this.CheckSerialError();
                } catch (Exception e2) {
                }
            }
        };
        thread.start();
        try {
            this.cursor.close();
            this.DataDB.close();
        } catch (Exception e2) {
        }
        String RegMail2 = RegMail.trim();
        String RegKey2 = RegKey.trim();
        if (RegKey2.length() > 5 && RegKey2.length() == 20 && CheckKey(RegKey2, RegMail2.substring(0, 5))) {
            CheckStyle = true;
        }
        if (0 != 0) {
            return false;
        }
        return CheckStyle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void CheckSerialError() throws ParseException, InterruptedException, IOException, SQLException {
        SQLiteDatabase DataDB2 = null;
        String RegKey = "";
        try {
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            DataDB2 = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        try {
            Cursor cursor2 = DataDB2.rawQuery("SELECT DATA_VALUE FROM SYSTEM_DATA WHERE DATA_NOTE = '註冊Mail'", null);
            if (cursor2.moveToNext()) {
                cursor2.getString(0);
            }
            cursor2.close();
            Cursor cursor22 = DataDB2.rawQuery("SELECT DATA_VALUE FROM SYSTEM_DATA WHERE DATA_NOTE = '註冊序號'", null);
            if (cursor22.moveToNext()) {
                RegKey = cursor22.getString(0);
            }
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://www.mysoft.idv.tw/mymoneyandroidserial.txt");
            HttpResponse response = client.execute(get);
            HttpEntity resEntity = response.getEntity();
            String result = EntityUtils.toString(resEntity);
            double GetMail = result.indexOf(RegKey.trim());
            if (GetMail > 0.0d) {
                DataDB2.execSQL("DELETE FROM SYSTEM_DATA WHERE DATA_NOTE = '註冊Mail'");
                DataDB2.execSQL("DELETE FROM SYSTEM_DATA WHERE DATA_NOTE = '註冊序號'");
            }
            DataDB2.close();
        } catch (Exception e2) {
        }
    }

    private void setTitle(String regKey) {
    }

    public String GetRegMail() {
        String RegMail = new StringBuilder(String.valueOf("")).toString();
        try {
            String tSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/MyMoneyZero/mymoney.db", null, 0);
        } catch (Exception e) {
        }
        this.cursor = this.DataDB.rawQuery("SELECT DATA_VALUE FROM SYSTEM_DATA WHERE DATA_NOTE = '註冊Mail'", null);
        if (this.cursor.moveToNext()) {
            RegMail = this.cursor.getString(0);
        }
        this.cursor.close();
        this.DataDB.close();
        return RegMail.trim();
    }

    public boolean CheckKey(String InputString, String CheckString) {
        String c1;
        String c2;
        String c3;
        String c4;
        String c5;
        new StringBuilder(String.valueOf("")).toString();
        new StringBuilder(String.valueOf("")).toString();
        new StringBuilder(String.valueOf("")).toString();
        new StringBuilder(String.valueOf("")).toString();
        new StringBuilder(String.valueOf("")).toString();
        if (InputString.trim().length() != 20) {
            return false;
        }
        String c12 = InputString.substring(0, 4);
        String c22 = InputString.substring(4, 8);
        String c32 = InputString.substring(8, 12);
        String c42 = InputString.substring(12, 16);
        String c52 = InputString.substring(16, 20);
        if (Double.valueOf(c12).doubleValue() > 0.0d) {
            c1 = RefKey(c12);
        } else {
            c1 = "";
        }
        if (Double.valueOf(c22).doubleValue() > 0.0d) {
            c2 = RefKey(c22);
        } else {
            c2 = "";
        }
        if (Double.valueOf(c32).doubleValue() > 0.0d) {
            c3 = RefKey(c32);
        } else {
            c3 = "";
        }
        if (Double.valueOf(c42).doubleValue() > 0.0d) {
            c4 = RefKey(c42);
        } else {
            c4 = "";
        }
        if (Double.valueOf(c52).doubleValue() > 0.0d) {
            c5 = RefKey(c52);
        } else {
            c5 = "";
        }
        String KeyCode = String.valueOf(c1) + c2 + c3 + c4 + c5;
        if (KeyCode.trim().length() != 5 || !KeyCode.equals(CheckString.trim())) {
            return false;
        }
        return true;
    }

    public String RefKey(String InputString) {
        if (InputString.length() != 4) {
            return "";
        }
        double k1 = Double.valueOf(InputString.substring(0, 1).toString()).doubleValue();
        double k2 = Double.valueOf(InputString.substring(1, 2).toString()).doubleValue();
        double k3 = Double.valueOf(InputString.substring(2, 3).toString()).doubleValue();
        double k4 = Double.valueOf(InputString.substring(3, 4).toString()).doubleValue();
        double TempValue1 = Double.valueOf(k1).doubleValue();
        double TempValue2 = Double.valueOf(k4).doubleValue();
        double TempValue3 = ((int) TempValue1) ^ ((int) TempValue2);
        if (TempValue3 >= 10.0d) {
            TempValue3 -= 10.0d;
        }
        double k22 = k2 - TempValue3;
        if (k22 < 0.0d) {
            k22 += 10.0d;
        }
        double k32 = k3 - TempValue3;
        if (k32 < 0.0d) {
            k32 += 10.0d;
        }
        double Ascii = (10.0d * k22) + k32;
        String RetCode = String.valueOf((char) (Ascii + 35.0d));
        return RetCode;
    }
}