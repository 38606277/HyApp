package com.cannon.hy.network;

/**
 * 统一响应回调接口
 * Created by admin on 2018/3/27.
 */

public interface OnResponseListener<T> {

    void onError(String msg);

    void onNoNetwork();

    void onResult(T result);

}
