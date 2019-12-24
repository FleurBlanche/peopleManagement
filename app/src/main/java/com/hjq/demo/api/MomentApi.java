package com.hjq.demo.api;

import com.hjq.demo.common.response.Result;
import com.hjq.demo.domain.activity.SocialActivity;
import com.hjq.demo.domain.relation.Moment;
import com.hjq.demo.domain.user.User;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MomentApi {

    //增加个人信息,返回修改的值
    @POST(".")
    Call<ResponseBody> AddMoment(@Body RequestBody body);

    //获取一个uid的详细个人信息
    @GET("{uid}")
    Call<ResponseBody> getMoment(@Path("uid") Long uid);

}
