package com.cannon.hy.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库辅助类
 */
public class DBHelper extends SQLiteOpenHelper {

    // 数据库文件名
    public static final String DB_NAME = "my_database.db";
    // 数据库版本号
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 初始化表
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

       // sqLiteDatabase.execSQL("create table if not exists USER (ID integer primary key autoincrement,ACCOUNT varchar,PASSWORD varchar,NAME varchar)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
