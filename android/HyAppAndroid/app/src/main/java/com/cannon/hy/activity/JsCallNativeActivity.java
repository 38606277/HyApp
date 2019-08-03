package com.cannon.hy.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cannon.hy.R;
import com.cannon.hy.api.CameraApi;
import com.cannon.hy.api.UserApi;


import wendu.dsbridge.DWebView;

public class JsCallNativeActivity extends AppCompatActivity {
    private DWebView mDWebView;
    private CameraApi cameraApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_native);
        mDWebView = (DWebView) findViewById(R.id.webview);
        // set debug mode
        DWebView.setWebContentsDebuggingEnabled(true);
        //mDWebView.addJavascriptObject(new UserApi(this), "user");
        mDWebView.addJavascriptObject(cameraApi = new CameraApi(this), "");
        mDWebView.loadUrl("file:///android_asset/dist/index.html");//js-call-native.html");

        getSupportActionBar().hide();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //回调摄像头的扫描结果
        if(!(cameraApi!=null&&cameraApi.onActivityResult(requestCode,resultCode,data))){
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}