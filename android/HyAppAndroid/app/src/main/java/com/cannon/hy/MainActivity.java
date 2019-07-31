package com.cannon.hy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        startActivity(new Intent(this,JsCallNativeActivity.class));
    }
}
