package com.cannon.hy.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.webkit.JavascriptInterface;

import androidx.core.app.ActivityCompat;

import com.cannon.hy.activity.MainActivity;

import wendu.dsbridge.CompletionHandler;

public class IntentApi {
    private Context mContext;
    public static final int SELECT_CONTACT_REQUEST_CODE = 1234;

    public IntentApi(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    @JavascriptInterface
    public void callPhone(Object phoneNum){

         if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
             ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.CALL_PHONE},2);
             return;
         }

        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        mContext.startActivity(intent);
    }



    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    @JavascriptInterface
    public void openPhoneView(Object phoneNum){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        mContext.startActivity(intent);
    }

    /**
     * 选择联系人
     */
    @JavascriptInterface
    public void selectContact(Object obj){

        int permission = ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_CONTACTS);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_CONTACTS},
                    1);
            return ;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        ((Activity)mContext).startActivityForResult(intent, SELECT_CONTACT_REQUEST_CODE);
    }






}
