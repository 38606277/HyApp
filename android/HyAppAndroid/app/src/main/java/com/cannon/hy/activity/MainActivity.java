package com.cannon.hy.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.cannon.hy.R;
import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void nativeCallJs(View view) {
        startActivity(new Intent(this,NativeCallJsActivity.class));
    }

    public void jsCallNative(View view) {
        startActivity(new Intent(this, JsCallNativeActivity.class));
    }




}
