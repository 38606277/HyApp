package com.cannon.hy.network;


import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 *
 * 返回数据为Json格式 不进行Json解析  因为data节点中的数据格式不固定
 * 需要提前判断 code == 200
 *
 *  Created by Administrator on 2017/5/5.
 */

public interface JsonApi {

    @GET
    Observable<String> get(@Url String url, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST
    Observable<String> post(@Url String url, @FieldMap Map<String, Object> params);

}
