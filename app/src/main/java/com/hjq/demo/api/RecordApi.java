package com.hjq.demo.api;

import com.hjq.demo.common.response.Result;
import com.hjq.demo.domain.relation.Record;
import com.hjq.demo.domain.user.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RecordApi {

    //添加一条record信息
    @POST
    Call<Result<String>> addRecord(@Body Record record);

    //获取某一个Record信息
    @GET("{uid}")
    Call<ResponseBody> getRecord(@Path("uid") Long uid);

//    //获取某一个用户的User信息
//    //user lixuhui:1577075819057
//    @GET("{uid}")
//    Call<ResponseBody> getUser(@Path("uid") Long uid);
}