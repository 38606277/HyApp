package com.cannon.hy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.cannon.hy.manager.NotificationMgr;

/**
 * 消息服务 轮询查询消息
 */
public class MessageService extends Service {


    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //开启线程
        MessageThread  messageThread = new MessageThread();
        messageThread.isRunning = true;
        messageThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 从服务器端获取消息
     *
     */
    class MessageThread extends Thread{
        //运行状态，下一步骤有大用
        public boolean isRunning = true;
        public void run() {
            while(isRunning){
                try {
                    //休息10分钟
                    Thread.sleep(600000);
                    //获取服务器消息
                    String serverMessage = getServerMessage();
                    if(serverMessage!=null&&!"".equals(serverMessage)){

                        NotificationMgr.notify(MessageService.this,"新消息","这里是消息内容");

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 这里以此方法为服务器Demo，仅作示例
     * @return 返回服务器要推送的消息，否则如果为空的话，不推送
     */
    public String getServerMessage(){
        return "YES!";
    }

}
