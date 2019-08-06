package com.cannon.hy;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 * Created by admin on 2018/3/27.
 */

public class App extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }


    /**
     * 获取上下文
     * @return 返回App
     */
    public static Context getContext() {
        return context;
    }


    /**
     * 分包初始化
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

}
