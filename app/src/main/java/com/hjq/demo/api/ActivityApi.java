package com.hjq.demo.api;

import com.hjq.demo.common.response.Result;
import com.hjq.demo.domain.activity.SocialActivity;
import com.hjq.demo.domain.user.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ActivityApi {

    //获取某一tag的全部活动信息
    @GET(".")
    Call<ResponseBody> getTagActivity(@Query("Activity.tag") String tag);

}

