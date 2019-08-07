package com.cannon.hy.activity;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cannon.hy.R;
import com.cannon.hy.api.CameraApi;
import com.cannon.hy.api.DBApi;
import com.cannon.hy.api.IntentApi;
import com.cannon.hy.api.LocationApi;
import com.cannon.hy.api.NetworkRequestApi;
import com.cannon.hy.helper.DBHelper;
import com.cannon.hy.manager.NotificationMgr;


import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.model.TakePhotoOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

public class JsCallNativeActivity extends TakePhotoActivity implements CameraApi.CameraActionListener {
    private DWebView mDWebView;
    private CameraApi cameraApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_native);
        mDWebView = (DWebView) findViewById(R.id.webview);
        // set debug mode
        DWebView.setWebContentsDebuggingEnabled(true);

        mDWebView.addJavascriptObject(new DBApi(this), "dbApi");
        mDWebView.addJavascriptObject(cameraApi = new CameraApi(this,this), "cameraApi");
        mDWebView.addJavascriptObject(new LocationApi(this), "locationApi");
        mDWebView.addJavascriptObject(new NetworkRequestApi(), "requestApi");
        mDWebView.addJavascriptObject(new IntentApi(this), "intentApi");

        mDWebView.loadUrl("file:///android_asset/dist/index.html");//js-call-native.html");
//       mDWebView.loadUrl("file:///android_asset/js-call-native.html");

        getSupportActionBar().hide();

        //NotificationMgr.notify(this,"资产APP","这里是通知栏内容");
        registerReceiver(broadcastReceiver,new IntentFilter("action.setContent"));
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //setContent(intent.getStringExtra("content"));
        }
    };

    public void setContent(String content){
        if(mDWebView!=null){
            mDWebView.callHandler("setContent", new Object[]{content}, new OnReturnValue<Integer>() {
                @Override
                public void onValue(Integer retValue) {

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //回调摄像头的扫描结果
        if(!(cameraApi!=null&&cameraApi.onActivityResult(requestCode,resultCode,data))){
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            //mDWebView.callHandler("");
            return true;
        }
        //继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }

        /***************** 相机实现  *********/


    /**
     * 拍照
     */
    public void takePhoto(){
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        Uri imageUri = Uri.fromFile(file);
        getTakePhoto().onPickFromCapture(imageUri);
    }

    /**
     * 选择图片
     */
    public void selectPhoto(){
        getTakePhoto().onPickFromGallery();
    }

    /**
     * 选择多张
     * @param limit
     */
    public void selectMultiplePhoto(int limit){
        TakePhoto takePhoto  = getTakePhoto();
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        takePhoto.setTakePhotoOptions(builder.create());
        takePhoto.onPickMultiple(limit);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        List<String> pathList = new ArrayList<>();
        for(TImage image: result.getImages()){
            pathList.add(image.getOriginalPath());
        }
        cameraApi.callBackPath(pathList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}