package com.cannon.hy.utils;

import android.util.Log;

import com.cannon.hy.BuildConfig;


/**
 * 功能: 日志输出
 */
public final class LogUtils {

    private static final String TAG = "LogUtils";

    public static void d(String msg) {

        if (BuildConfig.DEBUG) {
            Log.d(TAG,  "Debug信息：" + msg);
           //showLongLog(msg,200);
        }
    }

    /**
     * 日志太长分行
     * @param logContent 日志内容
     * @param logLineLength 一行日志长度
     */
    private static void showLongLog(String logContent,int logLineLength){
        StringBuilder content = new StringBuilder(logContent);
        int contentLength = logContent.length();
        int line = contentLength/logLineLength;//多少行
        int endContentIndex = contentLength%logLineLength;//最后一次剩余多少  例如不剩 则endIndex刚好 如果剩余 则endIndex=starIndex+剩余长度
        if(line < 1){
            Log.d(TAG,  "Debug信息：" + content.toString());
        }else{
            for(int i = 0 ; i < line ; i++){
                int startIndex = i * logLineLength;
                int endIndex = startIndex + logLineLength;
                Log.d(TAG,i == 0 ? "Debug信息："  + content.substring(startIndex,endIndex): content.substring(startIndex,endIndex));
            }
            if(0 < endContentIndex)Log.d(TAG,content.substring(contentLength - endContentIndex ,contentLength));
        }
    }


}
