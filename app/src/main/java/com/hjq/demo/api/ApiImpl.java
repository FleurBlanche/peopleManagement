package com.hjq.demo.api;

import com.hjq.demo.common.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiImpl {

    public static <T> T getInstance(Api api, Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                //设置网络请求的Url地址
                .baseUrl(Constants.serverUrl + api.getUri())
                .build();
        // 创建网络请求接口的实例
        return retrofit.create(clazz);
    }
}
