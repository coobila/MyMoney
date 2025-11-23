package com.coobila.mymoney;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class utility extends AppCompatActivity {
    private File appDBPath;

    // ----------------------------- 匯出 DB -----------------------------
    public void exportDB() {
        Context c = MyMoneyZeroActivity.getAppContext();
        try {
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File data = c.getExternalFilesDir(null);

            File src = new File(data + "/MyMoneyZero/mymoney.db");
            File dst = new File(sd, "mymoney.db");

            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();
            outChannel.transferFrom(inChannel, 0, inChannel.size());
            inChannel.close();
            outChannel.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ----------------------------- 匯入 DB -----------------------------
    private void importDB(Uri uri) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            OutputStream out = new FileOutputStream(appDBPath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();

            Toast.makeText(this, "匯入成功！請重新啟動 App", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "匯入失敗：" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // 使用 SAF 選擇要匯入的 DB
    public void chooseFileToImport() {
        appDBPath = new File(getExternalFilesDir(null) + "/MyMoneyZero/mymoney.db");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        filePickerLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Uri uri = result.getData().getData();
                            importDB(uri);
                        }
                    });

}
