package com.hjq.demo.api;

import com.hjq.demo.common.response.Result;
import com.hjq.demo.domain.user.Info;
import com.hjq.demo.domain.user.User;

import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface InformationApi {

    //修改个人信息,返回修改的值
    @PUT(("{uid}"))
    @Headers({"Content-Type:application/json;charset=UTF-8"})
    Call<ResponseBody> modifyInfo(@Path("uid") Long id, @Body RequestBody info);

    //增加个人信息,返回修改的值
    @POST(".")
    Call<ResponseBody> AddInfo(@Body RequestBody info);

    //获取一个uid的详细个人信息
    @GET("{uid}")
    Call<ResponseBody> getUser(@Path("uid") String uid);
}
