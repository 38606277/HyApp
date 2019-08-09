package com.cannon.hy.activity;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cannon.hy.R;
import com.cannon.hy.api.AudioApi;
import com.cannon.hy.api.CameraApi;
import com.cannon.hy.api.DBApi;
import com.cannon.hy.api.IntentApi;
import com.cannon.hy.api.LocationApi;
import com.cannon.hy.api.NetworkRequestApi;
import com.cannon.hy.helper.DBHelper;
import com.cannon.hy.manager.NotificationMgr;
import com.cannon.hy.manager.RecorderManager;
import com.cannon.hy.utils.FileUtils;
import com.cannon.hy.utils.ToastUtils;


import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.model.TakePhotoOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wendu.dsbridge.DWebView;

public class JsCallNativeActivity extends TakePhotoActivity implements CameraApi.CameraActionListener {
    private DWebView mDWebView;
    private CameraApi cameraApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_guide_page,null);
        final View jsCallNativeLayout = LayoutInflater.from(this).inflate(R.layout.activity_js_call_native,null);

        mDWebView = (DWebView) jsCallNativeLayout.findViewById(R.id.webview);
        // set debug mode
        DWebView.setWebContentsDebuggingEnabled(true);

        mDWebView.addJavascriptObject(new DBApi(this), "dbApi");
        mDWebView.addJavascriptObject(cameraApi = new CameraApi(this,this), "cameraApi");
        mDWebView.addJavascriptObject(new LocationApi(this), "locationApi");
        mDWebView.addJavascriptObject(new NetworkRequestApi(), "requestApi");
        mDWebView.addJavascriptObject(new IntentApi(this), "intentApi");
        mDWebView.addJavascriptObject(new AudioApi(this), "audioApi");

        mDWebView.loadUrl("file:///android_asset/dist/index.html");//js-call-native.html");
        //mDWebView.loadUrl("file:///android_asset/js-call-native.html");

        //NotificationMgr.notify(this,"资产APP","这里是通知栏内容");
        registerReceiver(broadcastReceiver,new IntentFilter("action.setContent"));

        //透明度动画
        final AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2000);
        rootView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setContentView(jsCallNativeLayout);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        setContentView(rootView);

    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setContent(intent.getStringExtra("content"));
        }
    };

    //设置消息内容给webView
    public void setContent(String content){
        if(mDWebView!=null){
            //mDWebView.callHandler("setContent", new Object[]{content});
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //回调摄像头的扫描结果
        if(cameraApi!=null&&cameraApi.onActivityResult(requestCode,resultCode,data)){

        }else if (resultCode == RESULT_OK &&IntentApi.SELECT_CONTACT_REQUEST_CODE==requestCode) {
            Uri contactData = data.getData();
            Cursor cursor = managedQuery(contactData, null, null, null,
                    null);
            cursor.moveToFirst();
            String num = this.getContactPhone(cursor);
            ToastUtils.showToast(num);
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }


    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){//监听返回键，如果可以后退就后退
            if(mDWebView.canGoBack()){
                mDWebView.goBack();
                return true;
            }
        }
        //继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }

        /***************** 相机实现  *********/

    /**
     * 拍照
     */
    public void takePhoto(){
        File file = new File(FileUtils.getFilePathForType(FileUtils.CACHE_IMAGE_FILE_DIR_PATH,System.currentTimeMillis() + ".jpg"));
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





    //获取联系人电话
    private String getContactPhone(Cursor cursor)
    {

        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String phoneResult="";
        //System.out.print(phoneNum);
        if (phoneNum > 0)
        {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " + contactId,
                    null, null);
            //int phoneCount = phones.getCount();
            //allPhoneNum = new ArrayList<String>(phoneCount);
            if (phones.moveToFirst())
            {
                // 遍历所有的电话号码
                for (;!phones.isAfterLast();phones.moveToNext())
                {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phones.getInt(typeindex);
                    String phoneNumber = phones.getString(index);
                    switch(phone_type)
                    {
                        case 2:
                            phoneResult=phoneNumber;
                            break;
                    }
                    //allPhoneNum.add(phoneNumber);
                }
                if (!phones.isClosed())
                {
                    phones.close();
                }
            }
        }
        return phoneResult;
    }
}