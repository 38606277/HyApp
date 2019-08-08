package com.cannon.hy.network;


import com.cannon.hy.utils.ToastUtils;

/**
 * Json格式统一响应回调接口
 * Created by admin on 2018/3/27.
 */

public abstract class OnJsonResponseListener<T> implements OnResponseListener<T> {

    @Override
    public void onError(String msg) {
        ToastUtils.showToast(msg);
    }

    public void onNoNetwork(){
        ToastUtils.showNoNetworkToast();
    }

}
