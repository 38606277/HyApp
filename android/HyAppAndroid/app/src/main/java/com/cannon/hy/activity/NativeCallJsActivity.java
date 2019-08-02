package com.cannon.hy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cannon.hy.R;

import org.json.JSONObject;

import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

public class NativeCallJsActivity extends AppCompatActivity implements View.OnClickListener {

    DWebView dWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_call_js);
        findViewById(R.id.addValue).setOnClickListener(this);
        findViewById(R.id.append).setOnClickListener(this);
        findViewById(R.id.startTimer).setOnClickListener(this);
        findViewById(R.id.synAddValue).setOnClickListener(this);
        findViewById(R.id.synGetInfo).setOnClickListener(this);
        findViewById(R.id.asynAddValue).setOnClickListener(this);
        findViewById(R.id.asynGetInfo).setOnClickListener(this);
        findViewById(R.id.hasMethodAddValue).setOnClickListener(this);
        findViewById(R.id.hasMethodXX).setOnClickListener(this);
        findViewById(R.id.hasMethodAsynAddValue).setOnClickListener(this);
        findViewById(R.id.hasMethodAsynXX).setOnClickListener(this);
        DWebView.setWebContentsDebuggingEnabled(true);
        dWebView= findViewById(R.id.webview);
        dWebView.loadUrl("file:///android_asset/native-call-js.html");

    }


    void showToast(Object o) {
        Toast.makeText(this, o.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addValue:
                dWebView.callHandler("addValue", new Object[]{3, 4}, new OnReturnValue<Integer>() {
                    @Override
                    public void onValue(Integer retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.append:
                dWebView.callHandler("append", new Object[]{"I", "love", "you"}, new OnReturnValue<String>() {
                    @Override
                    public void onValue(String retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.startTimer:
                dWebView.callHandler("startTimer", new OnReturnValue<Integer>() {
                    @Override
                    public void onValue(Integer retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.synAddValue:
                dWebView.callHandler("syn.addValue", new Object[]{5, 6}, new OnReturnValue<Integer>() {
                    @Override
                    public void onValue(Integer retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.synGetInfo:
                dWebView.callHandler("syn.getInfo", new OnReturnValue<JSONObject>() {
                    @Override
                    public void onValue(JSONObject retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.asynAddValue:
                dWebView.callHandler("asyn.addValue", new Object[]{5, 6}, new OnReturnValue<Integer>() {
                    @Override
                    public void onValue(Integer retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.asynGetInfo:
                dWebView.callHandler("asyn.getInfo", new OnReturnValue<JSONObject>() {
                    @Override
                    public void onValue(JSONObject retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.hasMethodAddValue:
                dWebView.hasJavascriptMethod("addValue", new OnReturnValue<Boolean>() {
                    @Override
                    public void onValue(Boolean retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.hasMethodXX:
                dWebView.hasJavascriptMethod("XX", new OnReturnValue<Boolean>() {
                    @Override
                    public void onValue(Boolean retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.hasMethodAsynAddValue:
                dWebView.hasJavascriptMethod("asyn.addValue", new OnReturnValue<Boolean>() {
                    @Override
                    public void onValue(Boolean retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.hasMethodAsynXX:
                dWebView.hasJavascriptMethod("asyn.XX", new OnReturnValue<Boolean>() {
                    @Override
                    public void onValue(Boolean retValue) {
                        showToast(retValue);
                    }
                });
                break;
        }

    }
}
