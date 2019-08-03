package com.cannon.hy.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.cannon.hy.activity.CameraActivity;
import com.cannon.hy.activity.JsCallNativeActivity;
import com.cannon.hy.activity.ScanActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import wendu.dsbridge.CompletionHandler;

/**
 * 相机Api
 */
public class CameraApi {
    
    private Context mContext;
    private CompletionHandler<String> completionHandler;

    public CameraApi(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 扫描二维码
     * @param obj
     * @param completionHandler
     */
    @JavascriptInterface
    public void scanQrCode(Object obj,CompletionHandler<String> completionHandler){
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //权限发生了改变 true  //  false 小米
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,Manifest.permission.CAMERA)){
                new AlertDialog.Builder(mContext).setTitle("标题")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请求授权
                                ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.CAMERA},1);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }else {
                ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.CAMERA},1);
            }
        }else{

            this.completionHandler = completionHandler;

            startScan();

        }
    }

    private void startScan(){
        IntentIntegrator integrator = new IntentIntegrator((Activity) mContext);
        integrator.setPrompt("");
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.initiateScan();
    }


    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String resultContent;
        if(result != null && (resultContent = result.getContents()) != null) {
            //此处调用js提供的回调方法
            if(completionHandler!=null){
                completionHandler.complete(resultContent);
            }
            return true;
        }
        return false;
    }

    /**
     * 拍照
     * @param obj
     */
    @JavascriptInterface
    public void takePhoto(Object obj){
        mContext.startActivity(new Intent(mContext, CameraActivity.class));

    }



}
