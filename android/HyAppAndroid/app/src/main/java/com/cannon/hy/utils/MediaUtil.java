package com.cannon.hy.utils;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;


import com.cannon.hy.App;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 媒体播放工具
 */
public class MediaUtil {

    private static final String TAG = "MediaUtil";

    private MediaPlayer player;
    private EventListener eventListener;

    private MediaUtil(){
        player = new MediaPlayer();
    }

    private static MediaUtil instance = new MediaUtil();

    public static MediaUtil getInstance(){
        return instance;
    }

    public MediaPlayer getPlayer() {
        return player;
    }


    public void setEventListener(final EventListener eventListener) {
        if (player != null){
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    eventListener.onStop(false);
                }
            });
        }
        this.eventListener = eventListener;
    }

    public void play(FileInputStream inputStream){
        try{
            if (eventListener != null){
                eventListener.onStop(true);
            }
            player.reset();
            player.setDataSource(inputStream.getFD());
            player.prepare();
            player.start();
        }catch (IOException e){
            Log.e(TAG, "play error:" + e);
        }
    }

    public void stop(){
        if (player != null && player.isPlaying()){
            player.stop();
        }
    }

    public long getDuration(String path){
        player = MediaPlayer.create(App.getContext(), Uri.parse(path));
        return player.getDuration();
    }


    /**
     * 播放器事件监听
     */
    public interface EventListener{
        /**
         * 是否是再次播放
         * @param play
         */
        void onStop(boolean play);
    }

}
