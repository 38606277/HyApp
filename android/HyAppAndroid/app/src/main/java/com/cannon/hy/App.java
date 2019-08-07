package com.cannon.hy;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by admin on 2018/3/27.
 */

public class App extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
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
