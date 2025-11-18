package com.coobila.mymoney;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;

public class utility {
    public static File createMyMoneyFolder(Context context) {
        // App 專屬的外部儲存路徑
        File baseDir = context.getExternalFilesDir(null);

        // 自行加上子資料夾名稱
        File myDir = new File(baseDir, "MyMoneyZero");

        if (!myDir.exists()) {
            boolean ok = myDir.mkdirs();
            if (!ok) {
                Log.e("MyMoney", "資料夾建立失敗: " + myDir.getAbsolutePath());
            }
        }

        Log.i("MyMoney", "資料夾路徑：" + myDir.getAbsolutePath());
        return myDir;
    }
}
