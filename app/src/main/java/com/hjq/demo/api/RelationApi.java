package com.hjq.demo.api;

import com.hjq.demo.common.response.Result;
import com.hjq.demo.domain.relation.Record;
import com.hjq.demo.domain.relation.Relation;
import com.hjq.demo.domain.user.User;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RelationApi {

    //添加一条relation信息
    @POST(".")
    Call<ResponseBody> addRelation(@Body RequestBody body);

    @GET("{uid}")
    Call<ResponseBody> getRelation(@Path("uid") Long uid);
}
