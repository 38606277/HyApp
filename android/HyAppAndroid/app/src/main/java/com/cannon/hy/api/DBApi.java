package com.cannon.hy.api;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.webkit.JavascriptInterface;

import com.cannon.hy.helper.DBHelper;
import com.cannon.hy.utils.JsonUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wendu.dsbridge.CompletionHandler;

/**
 * 数据库API
 */
public class DBApi {

    private Context mContext;
    private DBHelper dbHelper;


    public DBApi(Context mContext) {
        this.mContext = mContext;
        dbHelper = new DBHelper(mContext);
    }

    /**
     * 执行sql
     * @param sql
     * @param completionHandler
     */
    @JavascriptInterface
    public void execSQL(Object sql, CompletionHandler<String> completionHandler){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(String.valueOf(sql));
        sqLiteDatabase.close();
        completionHandler.complete("执行成功");//执行完成
    }

    /**
     *
     * @param sqlObj 语句
     * @param completionHandler
     */
    @JavascriptInterface
    public void querySQL(Object sqlObj,CompletionHandler<String> completionHandler){
        Map<String,String> sqlMap = JsonUtils.toMapSS(String.valueOf(sqlObj));

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        List<Map<String,String>> dataList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery(sqlMap.get("sql"), JsonUtils.toStringArray(sqlMap.get("selectionArgs")));

        if(cursor!=null){

            while (cursor.moveToNext()){// //遍历所有数据
                Map<String,String> dataMap = new HashMap<>();
                //遍历所有字段
               for(int i = 0 ; i <  cursor.getColumnCount() ;i++){
                   dataMap.put(cursor.getColumnName(i),cursor.getString(i));
               }
                dataList.add(dataMap);

            }
            cursor.close();
        }
        sqLiteDatabase.close();
        completionHandler.complete(JsonUtils.toJson(dataList));
    }

}
