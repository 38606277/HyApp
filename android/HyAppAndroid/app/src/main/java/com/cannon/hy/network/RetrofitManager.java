package com.cannon.hy.network;

import androidx.annotation.NonNull;

import com.cannon.hy.App;
import com.cannon.hy.BuildConfig;
import com.cannon.hy.R;
import com.cannon.hy.network.config.ApiConfig;
import com.cannon.hy.network.config.OkHttpClientConfig;
import com.cannon.hy.utils.JsonUtils;
import com.cannon.hy.utils.LogUtils;
import com.cannon.hy.utils.NetWorkUtils;
import com.google.gson.JsonSyntaxException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 网络请求
 * Created by admin on 2018/3/27.
 */

public class RetrofitManager {

    private Retrofit mRetrofit;
    private static RetrofitManager sInstance;

    private LinkedHashMap<String, Disposable> subscriberMap = null;

    /**
     * 得到网络请求类
     * @return RetrofitManager
     */
    public static RetrofitManager getInstance() {

        if (sInstance == null) {
            synchronized (RetrofitManager.class) {
                sInstance = new RetrofitManager();
            }
        }
        return sInstance;
    }


    private RetrofitManager() {
        // 打印每次请求的网址
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String s) {
                // LogUtils.d(s);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        // OkHttpClient参数设置
        final OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(logging);
        }
        builder.connectTimeout(OkHttpClientConfig.DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS); // 几秒之后超时
        builder.readTimeout(OkHttpClientConfig.DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(OkHttpClientConfig.DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.API_HOST_URL)
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 添加Rx适配器
                .build();

        subscriberMap = new LinkedHashMap<>();
    }


    /**
     * get 请求
     * @param url url路径
     * @param params 请求参数
     * @param listener 回调接口
     * @param <T> 回调类型
     */
    public <T> void getRequest(@NonNull String url, @NonNull Map<String,Object> params, @NonNull final OnResponseListener<T> listener){
        LogUtils.d(url + "   ====>>>>   "+  params.toString());
        JsonApi jsonApi = mRetrofit.create(JsonApi.class);
        this.request(jsonApi.get(url,params), url,listener);
    }


    /**
     * post请求
     * @param url url路径
     * @param params 请求参数
     * @param listener 回调接口
     * @param <T> 回调类型
     */
    public <T> void postRequest(@NonNull String url, @NonNull Map<String,Object> params, @NonNull final OnResponseListener<T> listener){
        LogUtils.d(url + "   ====>>>>   "+  params.toString());
        JsonApi jsonApi = mRetrofit.create(JsonApi.class);
        this.request(jsonApi.post(url,params),url,listener);
    }


    /**
     * 返回结果  Json 对象和 String
     * o 订阅对象
     * @param listener 回调接口
     */
    private <T> void request(Observable<String> o, final String tag, @NonNull final OnResponseListener<T> listener){
        if (NetWorkUtils.isNetworkConnected(App.getContext())) {
            final Observer<String> subscriber = new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {
                    subscriberMap.put(tag,d);
                    //LogUtils.d(tag + "  onSubscribe  ====>>>>   " + "绑定订阅");
                }

                @Override
                public void onComplete() {
                    subscriberMap.remove(tag);
                    //LogUtils.d(tag + "  onComplete  ====>>>>   " + "订阅完成");
                }

                @Override
                public void onError(Throwable throwable) {
                    subscriberMap.remove(tag);
                    listener.onError(throwable.getMessage());
                }

                @Override
                public void onNext(String result) {
                    LogUtils.d(tag + "   ====>>>>   "+  result);
                    //转换为对象
                    if(listener instanceof OnJsonResponseListener){
                        try {
                            T t = JsonUtils.fromJson(result,(OnJsonResponseListener<T>)listener);
                            listener.onResult(t);
                        }catch (JsonSyntaxException e){
                            listener.onError(App.getContext().getString(R.string.error_analysis_json_message));
                        }
                    }else if(listener instanceof OnStringResponseListener){
                        ((OnStringResponseListener)listener).onResult(result);
                    }
                }
            };
            o.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        } else {
            listener.onNoNetwork();
        }
    }

    public void cancel(String tag){
        if(!subscriberMap.containsKey(tag)){//是否有这个请求
            return;
        }
        Disposable disposable = subscriberMap.get(tag);
        if(disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
            subscriberMap.remove(tag);
            //LogUtils.d(tag + "  cancel    ====>>>>   "+ "取消订阅");
        }
    }


}
