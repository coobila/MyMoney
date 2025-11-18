package com.coobila.mymoney;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class FileUtil {
    public static File getAppExternalFolder(String folderName) {
        Context context = MyMoneyZeroActivity.getAppContext();
        File baseDir = context.getExternalFilesDir(null);
        if (baseDir != null) {
            File folder = new File(baseDir, folderName);
            if (!folder.exists()) folder.mkdirs();
            return folder;
        }
        return null;
    }
}
