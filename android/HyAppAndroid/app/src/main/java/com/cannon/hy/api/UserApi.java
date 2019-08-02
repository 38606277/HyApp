package com.cannon.hy.api;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.cannon.hy.model.UserModel;
import com.cannon.hy.utils.JsonUtils;

import wendu.dsbridge.CompletionHandler;

public class UserApi {
    private Context mContext;

    public UserApi(Context mContext) {
        this.mContext = mContext;
    }

    @JavascriptInterface
    public String getName(Object name){
        return "姓名" + String.valueOf(name);
    }

    @JavascriptInterface
    public void callBack(final Object content,final CompletionHandler<String> handler){
        final UserModel userModel = new UserModel();
        userModel.setName(String.valueOf(content));


        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.complete(JsonUtils.toJson(userModel));
            }
        }).start();
    }

}
