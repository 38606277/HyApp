package com.cannon.hy.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.cannon.hy.activity.CameraActivity;
import com.cannon.hy.activity.JsCallNativeActivity;
import com.cannon.hy.activity.ScanActivity;
import com.cannon.hy.utils.JsonUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.devio.takephoto.app.TakePhotoActivity;

import java.io.File;
import java.util.List;

import wendu.dsbridge.CompletionHandler;

/**
 * 相机Api
 */
public class CameraApi {
    
    private Context mContext;
    private CompletionHandler<String> completionHandler;
    private CameraActionListener mCameraActionListener;

    public CameraApi(Context mContext,CameraActionListener mCameraActionListener) {
        this.mContext = mContext;
        this.mCameraActionListener = mCameraActionListener;

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
    public void takePhoto(Object obj,CompletionHandler<String> completionHandler){
        this.completionHandler  = completionHandler;
        mCameraActionListener.takePhoto();
    }

    /**
     * 选择图片
     * @param obj
     */
    @JavascriptInterface
    public void selectPhoto(Object obj,CompletionHandler<String> completionHandler){
        this.completionHandler  = completionHandler;
        mCameraActionListener.selectPhoto();
    }

    @JavascriptInterface
    public void selectMultiplePhoto(Object obj,CompletionHandler<String> completionHandler){
        this.completionHandler  = completionHandler;
        mCameraActionListener.selectMultiplePhoto(Integer.parseInt(String.valueOf(obj)));
    }


    public void callBackPath(List<String> path){
        if(completionHandler!=null){
            completionHandler.complete(JsonUtils.toJson(path));
        }

    }

    public interface CameraActionListener{
        void takePhoto();
        void selectPhoto();
        void selectMultiplePhoto(int limit);
    }


}
