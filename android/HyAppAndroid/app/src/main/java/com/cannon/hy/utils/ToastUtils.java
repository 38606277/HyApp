package com.cannon.hy.utils;

import android.widget.Toast;

import androidx.annotation.StringRes;

import com.cannon.hy.App;
import com.cannon.hy.R;


/**
 * 管理toast的类，整个app用该类来显示toast
 */
public final class ToastUtils {




    public static void showToast(@StringRes int strRes){
        Toast.makeText(App.getContext(), App.getContext().getString(strRes), Toast.LENGTH_LONG).show();
    }

    public static void showToast(String message) {
        Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showNoNetworkToast(){
        Toast.makeText(App.getContext(), App.getContext().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();
    }


}
