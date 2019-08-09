package com.cannon.hy.network;


import com.cannon.hy.utils.ToastUtils;

/**
 * String格式统一响应回调接口
 * Created by admin on 2018/3/27.
 */

public abstract class OnStringResponseListener implements OnResponseListener<String>{

    @Override
    public void onError(String msg) {
        ToastUtils.showToast(msg);
    }

    public void onNoNetwork(){
        ToastUtils.showNoNetworkToast();
    }

}
