package com.hjq.demo.api;

import com.hjq.demo.common.response.Result;
import com.hjq.demo.domain.relation.Record;
import com.hjq.demo.domain.relation.Relation;
import com.hjq.demo.domain.user.Request;
import com.hjq.demo.domain.user.User;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestApi {

    //添加一条Request信息
    @POST(".")
    Call<ResponseBody> addRequest(@Body RequestBody body);

    //获取某一个Request信息
    @GET("{uid}")
    Call<ResponseBody> getRequest(@Path("uid") Long uid);
}
