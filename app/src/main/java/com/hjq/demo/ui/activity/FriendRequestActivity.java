package com.hjq.demo.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjq.demo.R;
import com.hjq.demo.api.Api;
import com.hjq.demo.api.ApiImpl;
import com.hjq.demo.api.InformationApi;
import com.hjq.demo.api.RequestApi;
import com.hjq.demo.api.UserApi;
import com.hjq.demo.common.Constants;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.domain.relation.Moment;
import com.hjq.demo.domain.user.Info;
import com.hjq.demo.domain.user.Request;
import com.hjq.demo.domain.user.User;
import com.hjq.demo.ui.adapter.MomentListAdapter;
import com.hjq.demo.ui.adapter.RequestListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
public final class FriendRequestActivity extends MyActivity {

    //全部动态列表
    @BindView(R.id.request_list)
    RecyclerView recyclerView;

    RequestListAdapter requestListAdapter;

    public static List<Request> requestList;

    //todo
    //api
    UserApi userApi;
    InformationApi informationApi;
    RequestApi requestApi;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friend_request;
    }

    @Override
    protected void initView() {

        userApi = ApiImpl.getInstance(Api.USER, UserApi.class);
        informationApi = ApiImpl.getInstance(Api.INFORMATION, InformationApi.class);
        requestApi = ApiImpl.getInstance(Api.REQUEST, RequestApi.class);

        requestList = new ArrayList<>();
        requestListAdapter = new RequestListAdapter(requestList);
        recyclerView.setAdapter(requestListAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void initData() {

        showLoading();

        Constants.requestList = new ArrayList<>();
        String rqs = Constants.user.getRequestgetlist();
        List<Long> ids = Arrays.asList(rqs.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        for(int i = 0; i < ids.size(); ++i){
            Long id = ids.get(i);
            requestApi.getRequest(id).enqueue(new Callback<ResponseBody>() {
                @SneakyThrows
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.body() != null){
                        String body = response.body().string();
                        Log.d("apidebug读取一个请求",body);
                        if(body.length() >= 3){
                            //有数据，而且必定是单条数据，直接读取
                            JSONObject ob = JSON.parseObject(body);
                            Request request = new Request();
                            request.setId(ob.getLong("id"));
                            request.setSendtime(ob.getString("sendtime"));
                            request.setContent(ob.getString("content"));
                            request.setSender(ob.getString("senderid"));
                            request.setGetter(ob.getString("getterid"));
                            //todo
                            //获取头像、背景文件等,并设置Constants
                            //fileApi
                            //获取id
                            userApi.getUser(ob.getLong("senderid")).enqueue(new Callback<ResponseBody>() {
                                @SneakyThrows
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.body() != null){
                                        String body = response.body().string();
                                        Log.d("apidebug 读取信息表",body);
                                        if(body.length() >= 3){
                                            //有数据，而且必定是单条数据，直接读取
                                            JSONObject ob = JSON.parseObject(body);
                                            request.setName(ob.getString("username"));
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    String body = call.request().toString();
                                    Log.d("apidebug 读取信息表",body);
                                }
                            });
                            requestList.add(request);
                            Constants.requestList.add(request);
                            requestListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        }

        //todo
        //从服务器获取全部的动态信息，然后展示

//        //test
//        Request request = new Request();
//        request.setContent("请求添加好友");
//        request.setName("jianglikang");
//        request.setPortraitBMP(BitmapFactory.decodeResource(this.getResources(), R.drawable.bg_launcher));
//        request.setSendtime("2019 12 20 11 23");
//        requestList.add(request);
//        Constants.requestList.add(request);

        requestListAdapter.notifyDataSetChanged();
        requestListAdapter.setItemClickListener(position -> {
            Intent intent = new Intent(this,FriendDataActivity.class);
            intent.putExtra("button","request");
            intent.putExtra("id",position);
            startActivity(intent);
        });

        showComplete();

    }
}
