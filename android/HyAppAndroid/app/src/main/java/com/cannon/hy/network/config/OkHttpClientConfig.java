package com.cannon.hy.network.config;

/**
 * 连接网络配置
 * Created by admin on 2018/3/27.
 */

public class OkHttpClientConfig {

    public static final int DEFAULT_READ_TIMEOUT_MILLIS = 20 * 1000; // 20s
    public static final int DEFAULT_WRITE_TIMEOUT_MILLIS = 20 * 1000; // 20s
    public static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s
}
