package com.coobila.mymoney;

import android.content.Context;

import java.io.File;

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
