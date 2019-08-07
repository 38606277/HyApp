package com.cannon.hy.manager;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.cannon.hy.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationMgr {


   public static void notify(Context context,String title,String content){

        //创建通知栏管理工具
       NotificationManager notificationManager = (NotificationManager) context.getSystemService
               (NOTIFICATION_SERVICE);
        //实例化通知栏构造器
       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"channelId");

       //设置Builder
       //设置标题
       mBuilder.setContentTitle(title)
               //设置内容
               .setContentText(content)
               //设置大图标
               .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon))
               //设置小图标
               .setSmallIcon(R.mipmap.app_icon)
               //设置通知时间
               .setWhen(System.currentTimeMillis())
               //首次进入时显示效果
               .setTicker(content)
               //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
               .setDefaults(Notification.DEFAULT_SOUND);
       //发送通知请求
       notificationManager.notify(10, mBuilder.build());

   }



}
