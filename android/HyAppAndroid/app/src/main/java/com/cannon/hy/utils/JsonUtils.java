package com.cannon.hy.utils;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    public static Map<String,Object> toMapSO(String result){
        return gson.fromJson(result,new TypeToken<Map<String,Object>>(){}.getType());
    }

    public static Map<String,String> toMapSS(String result){
        return gson.fromJson(result,new TypeToken<Map<String,Object>>(){}.getType());
    }


}

