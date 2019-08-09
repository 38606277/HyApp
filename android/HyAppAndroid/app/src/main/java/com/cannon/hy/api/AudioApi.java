package com.cannon.hy.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.util.Log;
import android.webkit.JavascriptInterface;

import androidx.core.app.ActivityCompat;

import com.cannon.hy.utils.FileUtils;
import com.cannon.hy.utils.MediaFileUtils;
import com.cannon.hy.utils.MediaUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import wendu.dsbridge.CompletionHandler;

public class AudioApi {

    private static final String TAG = "AudioApi";

    private Context mContext;

    private String mFileName = null;
    private MediaRecorder mRecorder = null;
    private long startTime;
    private long timeInterval;
    private boolean isRecording;
    private CompletionHandler<String> onStopListener;

    public AudioApi(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 开始录音
     */
    @JavascriptInterface
    public void startRecording(Object object) {
        int permission = ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECORD_AUDIO},
                    1);
            return ;
        }

        if (isRecording){
            mRecorder.release();
            mRecorder = null;
        }
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile( mFileName =  FileUtils.getCacheAudioFilePath(System.currentTimeMillis() + ".mp3"));
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        startTime = System.currentTimeMillis();
        try {
            mRecorder.prepare();
            mRecorder.start();
            isRecording = true;
        } catch (Exception e){
            Log.e(TAG, "prepare() failed");
        }
    }
    /**
     * 停止录音
     */
    @JavascriptInterface
    public void stopRecording(Object object) {
        if (mFileName == null) return;
        timeInterval = System.currentTimeMillis() - startTime;
        try{
            if (timeInterval>1000){
                mRecorder.stop();
            }
            mRecorder.release();
            mRecorder = null;
            isRecording =false;
            if(onStopListener!=null){
                onStopListener.complete(mFileName);
            }
        }catch (Exception e){
            Log.e(TAG, "release() failed");
        }



    }
    /**
     * 取消语音
     */
    @JavascriptInterface
    public synchronized void cancelRecording(Object object) {
        if (mRecorder != null) {
            try {
                mRecorder.release();
                mRecorder = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = new File(mFileName);
            file.deleteOnExit();
        }
        isRecording =false;
    }
    /**
     * 获取录音文件
     */
    public byte[] getDate() {
        if (mFileName == null) return null;
        try{
            return readFile(new File(mFileName));
        }catch (IOException e){
            Log.e(TAG, "read file error" + e);
            return null;
        }
    }
    /**
     * 获取录音文件地址
     */
    public String getFilePath(){
        return mFileName;
    }
    /**
     * 获取录音时长,单位秒
     */
    public long getTimeInterval() {
        return timeInterval/1000;
    }
    /**
     * 将文件转化为byte[]
     *
     * @param file 输入文件
     */
    private static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

    /**
     * 设置停止录音回调
     * @param obj
     * @param onStopListener
     */
    @JavascriptInterface
    public void setOnStopListener(Object obj,CompletionHandler<String> onStopListener){
        this.onStopListener = onStopListener;

    }


    /**
     * 播放录音文件
     * @param filePath
     */
    @JavascriptInterface
    public void play(Object filePath){
        //盘点是否为录音文件
        if(MediaFileUtils.isAudioFileType(String.valueOf(filePath))){
            try {
                MediaUtil.getInstance().play(new FileInputStream(String.valueOf(filePath)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 停止播放
     */
    @JavascriptInterface
    public void stop(Object obj){
        //停止播放
        MediaUtil.getInstance().stop();
    }

    /**
     * 设置播放结束的回调
     * @param obj
     * @param onStopListener
     */
    @JavascriptInterface
    public void setPlayStopListener(Object obj,final CompletionHandler<String> onStopListener){
        MediaUtil.getInstance().setEventListener(new MediaUtil.EventListener() {
            @Override
            public void onStop(boolean play) {
                if(!play){
                    onStopListener.complete("播放结束");
                }

            }
        });
    }

}
