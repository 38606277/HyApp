package com.cannon.hy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cannon.hy.api.UserApi;

import wendu.dsbridge.DWebView;

public class JsCallNativeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_native);
        final DWebView dwebView= (DWebView) findViewById(R.id.webview);
        // set debug mode
        DWebView.setWebContentsDebuggingEnabled(true);
        dwebView.addJavascriptObject(new UserApi(this), "user");
        dwebView.loadUrl("file:///android_asset/js-call-native.html");
    }
}
