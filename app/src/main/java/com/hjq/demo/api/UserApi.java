package com.hjq.demo.api;

import com.hjq.demo.common.response.Result;
import com.hjq.demo.domain.user.User;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    //数据库建表错误password =》 passward
    //根据用户名和密码获取User
//    @GET("?User.username={username}&User.passward={passward}")
//    Call<Result<User>> login(@Path("username") String username, @Path("passward") String passward);
    //query string must not have replace block
    //
    @GET(".")
    Call<ResponseBody> login(@Query("User.username") String username, @Query("User.passward") String passward);

    //注册
    @POST
    Call<Result<User>> register(@Body RequestBody user);

    //修改信息
    @PUT("{uid}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseBody> modifyInfo(@Path("uid") Long id, @Body RequestBody user);

    //获取某一个用户的User信息
    //user lixuhui:1577075819057
    @GET("{uid}")
    Call<ResponseBody> getUser(@Path("uid") Long uid);

    //test code
//        .enqueue(new Callback<ResponseBody>() {
//        @SneakyThrows
//        @Override
//        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//            String body = response.body().string();
//            Log.d("apidebug",body);
//        }
//        @Override
//        public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//        }
//    });

    //模糊搜索
    //某一用户名的全部用户
    @GET(".")
    Call<ResponseBody> searchUser(@Query("User.username") String name);
}
