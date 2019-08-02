package com.cannon.hy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.cannon.hy.R;

import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.LubanOptions;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;

public class CameraActivity extends TakePhotoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        Uri imageUri = Uri.fromFile(file);

        getTakePhoto().onPickFromCapture(imageUri);

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
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("images", images);
        startActivity(intent);
        finish();
    }
}
