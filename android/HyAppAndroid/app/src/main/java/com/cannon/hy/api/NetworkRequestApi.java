package com.cannon.hy.api;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.cannon.hy.model.RequestModel;
import com.cannon.hy.network.OnResponseListener;
import com.cannon.hy.network.OnStringResponseListener;
import com.cannon.hy.network.RetrofitManager;
import com.cannon.hy.utils.JsonUtils;
import com.cannon.hy.utils.ToastUtils;

import wendu.dsbridge.CompletionHandler;

public class NetworkRequestApi {

    public static final String TAG = "NetworkRequestApi";

    @JavascriptInterface
    public void request(Object requestData,final CompletionHandler<String> completionHandler){
        RequestModel requestModel = JsonUtils.toModel(String.valueOf(requestData),RequestModel.class);
        if("post".equals(requestModel.getMethod())){
            requestPost(requestModel,completionHandler);
        }else if("get".equals(requestModel.getMethod())){
            requestGet(requestModel,completionHandler);
        }else{
            Log.d(TAG,"请求类型错误");
        }



    }

    /**
     * POST 请求
     * @param requestModel
     * @param completionHandler
     */
    private void requestPost(RequestModel requestModel,final CompletionHandler<String> completionHandler){
        RetrofitManager.getInstance().postRequest(requestModel.getUrl(), requestModel.getDataMap(), new OnStringResponseListener() {

            @Override
            public void onError(String msg) {
                ToastUtils.showToast(msg);
            }

            @Override
            public void onNoNetwork() {
                ToastUtils.showNoNetworkToast();
            }

            @Override
            public void onResult(String result) {
                completionHandler.complete(result);
            }
        });
    }

    /**
     * GET请求
     * @param requestModel
     * @param completionHandler
     */
    private void requestGet(RequestModel requestModel,final CompletionHandler<String> completionHandler) {
        RetrofitManager.getInstance().getRequest(requestModel.getUrl(), requestModel.getDataMap(), new OnStringResponseListener() {

            @Override
            public void onError(String msg) {
                ToastUtils.showToast(msg);
            }

            @Override
            public void onNoNetwork() {
                ToastUtils.showNoNetworkToast();
            }

            @Override
            public void onResult(String result) {
                completionHandler.complete(result);
            }
        });
    }
}
