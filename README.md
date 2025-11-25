String tSDCardPath = String.valueOf(getExternalFilesDir(null)); this.DataDB = SQLiteDatabase.openDatabase(String.valueOf(tSDCardPath) + "/Myapp/myapp.db", null, 0); 如何避免 Activity 記憶體洩漏又可以公用
