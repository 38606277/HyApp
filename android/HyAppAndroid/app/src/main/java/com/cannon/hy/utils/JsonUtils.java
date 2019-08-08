package com.cannon.hy.utils;
import android.text.TextUtils;

import com.cannon.hy.network.OnJsonResponseListener;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 *
 * Json 工具类
 *
 * Created by Administrator on 2017/7/3.
 */

public class JsonUtils {

    private static Gson gson = new Gson();

    private JsonUtils() {
    }

    public static boolean isGoodJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    /**
     * 转化为Json字符串
     * @param src 数据对象
     * @return 转化后的json串
     */
    public static String toJson(Object src) {
        if (src == null) {
            return gson.toJson(JsonNull.INSTANCE);
        }
        return gson.toJson(src);
    }


    public static <T>  T toModel(Reader reader , Class<T> model){
        return gson.fromJson(reader,model);
    }
    public static <T>  T toModel(String json , Class<T> model){
        return gson.fromJson(json,model);
    }

    public static Map<String,Object> toMapSO(String json){
        return gson.fromJson(json,new TypeToken<Map<String,Object>>(){}.getType());
    }

    public static Map<String,String> toMapSS(String json){
        return gson.fromJson(json,new TypeToken<Map<String,Object>>(){}.getType());
    }

    public static String[] toStringArray(String json){
         return gson.fromJson(json,String[].class);
    }


    /**
     * @MethodName : fromJson
     * @Description : 用来将JSON串转为对象，此方法可用来转带泛型的集合
     * @param json
     * @return
     */
    public static <T> T fromJson(String json, OnJsonResponseListener<T> listener) {
        return gson.fromJson(json , getResponseListenerTypeToAbs(listener));
    }

    /**
     * 获取接口中的泛型类型
     * @param listener
     * @return
     */
    private static Type getResponseListenerTypeToImp(OnJsonResponseListener listener){
        Type mInterFace = listener.getClass().getGenericInterfaces()[0];
        ParameterizedType parameter = (ParameterizedType) mInterFace;
        return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }


    /**
     * 获取抽象类中的泛型
     * @param listener
     * @return
     */
    private static Type getResponseListenerTypeToAbs(OnJsonResponseListener listener){
        Type mInterFace = listener.getClass().getGenericSuperclass();
        ParameterizedType parameter = (ParameterizedType) mInterFace;
        return  $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }
}

