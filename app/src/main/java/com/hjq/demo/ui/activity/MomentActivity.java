package com.hjq.demo.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjq.demo.R;
import com.hjq.demo.api.Api;
import com.hjq.demo.api.ApiImpl;
import com.hjq.demo.api.FileApi;
import com.hjq.demo.api.InformationApi;
import com.hjq.demo.api.MomentApi;
import com.hjq.demo.api.RelationApi;
import com.hjq.demo.api.UserApi;
import com.hjq.demo.common.Constants;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.domain.activity.SocialActivity;
import com.hjq.demo.domain.relation.Moment;
import com.hjq.demo.domain.relation.Relation;
import com.hjq.demo.domain.user.Info;
import com.hjq.demo.domain.user.User;
import com.hjq.demo.ui.adapter.ActivityListAdapter;
import com.hjq.demo.ui.adapter.MomentListAdapter;
import com.hjq.demo.widget.XCollapsingToolbarLayout;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public final class MomentActivity extends MyLazyFragment<HomeActivity>
        implements XCollapsingToolbarLayout.OnScrimsListener {

    @BindView(R.id.moment_button_send)
    Button sendButton;

    @BindView(R.id.moment_image_bg)
    ImageView bgImage;

    //全部动态列表
    @BindView(R.id.moment_list)
    RecyclerView recyclerView;

    public static MomentListAdapter momentListAdapter;
    public static List<Moment> momentList;

    //todo
    //api
    //根据User获取到自己的好友User列表，获取到全部的好友，然后读取全部好友的朋友圈列表，包括自己的朋友圈列表，根据列表，读取全部的朋友圈信息,然后写入列表中
    //另一个界面中也存在好友的问题，那么就全部在这个页面中获取数据，然后写入到全局的变量中
    UserApi userApi;
    MomentApi momentApi;
    FileApi fileApi;
    RelationApi relationApi;
    InformationApi informationApi;


    public static MomentActivity newInstance() {
        return new MomentActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_moment;
    }

    @Override
    protected void initView() {

        fileApi = ApiImpl.getFileInstance(Api.FILE, FileApi.class);
        userApi = ApiImpl.getInstance(Api.USER, UserApi.class);
        momentApi = ApiImpl.getInstance(Api.MOMENT, MomentApi.class);
        relationApi = ApiImpl.getInstance(Api.RELATION, RelationApi.class);
        informationApi = ApiImpl.getInstance(Api.INFORMATION, InformationApi.class);

        momentList = new ArrayList<>();
        momentListAdapter = new MomentListAdapter(momentList);
        recyclerView.setAdapter(momentListAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initData() {

        showLoading();

        Constants.momentList = new ArrayList<>();
        Constants.friends = new ArrayList<>();


        //todo
        //从服务器获取全部的动态信息，然后展示
        //读取完全部好友后，获取动态信息
        String relations = Constants.user.getRelationlist();
        List<Long> ids = Arrays.asList(relations.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        //ids列表中为用户的全部relation
        //根据uid获取relation信息
        for(int i = 0; i < ids.size(); ++i){
            Long id = ids.get(i);
            relationApi.getRelation(id).enqueue(new Callback<ResponseBody>() {
                @SneakyThrows
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.body() != null){
                        String body = response.body().string();
                        Log.d("apidebug 读取关系",body);
                        if(body.length() >= 3){
                            //有数据，而且必定是单条数据，直接读取到一个关系中，保存到Constants
                            JSONObject ob = JSON.parseObject(body);
                            Relation relation = new Relation();
                            relation.setId(ob.getLong("id"));
                            relation.setGroup(ob.getInteger("group"));
                            relation.setFriend(ob.getString("friend"));
                            relation.setMemo(ob.getString("memo"));
                            relation.setRecordlist(ob.getString("recordlist"));
                            Constants.relations.add(relation);

                            User user = new User();
                            Info info = new Info();

                            //根据friendId获取user以及他的info
                            Long fid = ob.getLong("friend");
                            userApi.getUser(fid).enqueue(new Callback<ResponseBody>() {
                                @SneakyThrows
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.body() != null){
                                        String body = response.body().string();
                                        Log.d("apidebug 读取用户信息",body);
                                        if(body.length() >= 3){
                                            //有数据，而且必定是单条数据，直接读取到一个User中，保存到Constants.friends
                                            JSONObject ject = JSON.parseObject(body);
                                            user.setId(ject.getLong("id"));
                                            user.setUsername(ject.getString("username"));
                                            user.setPassward(ject.getString("passward"));
                                            user.setActive(ject.getInteger("active"));
                                            user.setAuthority(ject.getInteger("authority"));
                                            user.setMomentlist(ject.getString("momentlist"));
                                            user.setRelationlist(ject.getString("relationlist"));
                                            user.setValue(ject.getString("value"));
                                            user.setRequestgetlist(ject.getString("requestgetlist"));
                                            user.setRequestsendlist(ject.getString("requestsendlist"));
                                            user.setPersoninform(ject.getString("personinform"));

                                            //获取每个用户的详细信息
                                            //get self information
                                            informationApi.getUser(user.getPersoninform()).enqueue(new Callback<ResponseBody>() {
                                                @SneakyThrows
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if(response.body() != null){
                                                        String body = response.body().string();
                                                        Log.d("apidebug 界面初始化读取好友个人信息表",body);
                                                        if(body.length() >= 3){
                                                            //有数据，而且必定是单条数据，直接读取
                                                            JSONObject ob = JSON.parseObject(body);
                                                            info.setId(ob.getLong("id"));
                                                            info.setName(ob.getString("name"));
                                                            info.setSex(ob.getString("sex"));
                                                            info.setAge(ob.getInteger("age"));
                                                            info.setTag1(ob.getString("tag1"));
                                                            info.setTag2(ob.getString("tag2"));
                                                            info.setTag3(ob.getString("tag3"));
                                                            info.setCompany(ob.getString("company"));
                                                            info.setPosition(ob.getString("position"));
                                                            info.setMobile(ob.getString("mobile"));
                                                            info.setEmail(ob.getString("email"));
                                                            info.setAddress(ob.getString("address"));
                                                            info.setMotto(ob.getString("motto"));
                                                            info.setPortrait(ob.getString("portrait"));
                                                            info.setBackground(ob.getString("background"));

                                                            user.setPersoninformObj(info);

                                                            //todo
                                                            //获取头像、背景文件等,并设置Constants
                                                            //fileApi

                                                        }
                                                    }
                                                    Constants.friends.add(user);

                                                    //对应每一个user
                                                    String idliststring = user.getMomentlist();
                                                    List<Long> mids = Arrays.asList(idliststring.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                                                    for(int i = 0; i < ids.size(); ++i){
                                                        Long id = mids.get(i);
                                                        momentApi.getMoment(id).enqueue(new Callback<ResponseBody>() {
                                                            @SneakyThrows
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                if(response.body() != null){
                                                                    String body = response.body().string();
                                                                    Log.d("apidebug读取一个好友动态",body);
                                                                    if(body.length() >= 3){
                                                                        //有数据，而且必定是单条数据，直接读取
                                                                        JSONObject ob = JSON.parseObject(body);
                                                                        Moment moment = new Moment();
                                                                        moment.setId(ob.getLong("id"));
                                                                        moment.setText(ob.getString("text"));
                                                                        moment.setImage(ob.getString("image"));

                                                                        //todo
                                                                        //获取头像、背景文件等,并设置Constants
                                                                        //fileApi
                                                                        moment.setUser(user);
                                                                        momentList.add(moment);
                                                                        Constants.momentList.add(moment);
                                                                        Collections.sort(momentList, new Moment.MomentComparetor());
                                                                        momentListAdapter.notifyDataSetChanged();
                                                                    }
                                                                }
                                                            }
                                                            @Override
                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                            }
                                                        });
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                }
                                            });
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                }
                            });
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        }

        //添加自己的moment
        //get self information
        informationApi.getUser(Constants.user.getPersoninform()).enqueue(new Callback<ResponseBody>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null){
                    String body = response.body().string();
                    Log.d("apidebug 界面初始化读取个人信息表",body);
                    if(body.length() >= 3){
                        //有数据，而且必定是单条数据，直接读取
                        JSONObject ob = JSON.parseObject(body);
                        Info info = new Info();
                        info.setId(ob.getLong("id"));
                        info.setName(ob.getString("name"));
                        info.setSex(ob.getString("sex"));
                        info.setAge(ob.getInteger("age"));
                        info.setTag1(ob.getString("tag1"));
                        info.setTag2(ob.getString("tag2"));
                        info.setTag3(ob.getString("tag3"));
                        info.setCompany(ob.getString("company"));
                        info.setPosition(ob.getString("position"));
                        info.setMobile(ob.getString("mobile"));
                        info.setEmail(ob.getString("email"));
                        info.setAddress(ob.getString("address"));
                        info.setMotto(ob.getString("motto"));
                        info.setPortrait(ob.getString("portrait"));
                        info.setBackground(ob.getString("background"));

                        Constants.info = info;
                        Constants.user.setPersoninformObj(info);
                        //todo
                        //获取头像、背景文件等,并设置Constants
                        //fileApi
                        //读取自己
                        String msidliststring = Constants.user.getMomentlist();
                        List<Long> msids = Arrays.asList(msidliststring.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                        for(int i = 0; i < msids.size(); ++i){
                            Long id = msids.get(i);
                            momentApi.getMoment(id).enqueue(new Callback<ResponseBody>() {
                                @SneakyThrows
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.body() != null){
                                        String body = response.body().string();
                                        Log.d("apidebug读取自己的动态",body);
                                        if(body.length() >= 3){
                                            //有数据，而且必定是单条数据，直接读取
                                            JSONObject ob = JSON.parseObject(body);
                                            Moment moment = new Moment();
                                            moment.setId(ob.getLong("id"));
                                            moment.setText(ob.getString("text"));
                                            moment.setImage(ob.getString("image"));

                                            //todo
                                            //获取头像、背景文件等,并设置Constants
                                            //fileApi
                                            Constants.user.setPersoninformObj(Constants.info);
                                            moment.setUser(Constants.user);
                                            momentList.add(moment);
                                            Constants.momentList.add(moment);
                                            Collections.sort(momentList, new Moment.MomentComparetor());
                                            momentListAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });


        Collections.sort(momentList, new Moment.MomentComparetor());
        momentListAdapter.notifyDataSetChanged();
        momentListAdapter.setItemClickListener(position -> {
            return;
        });

        showComplete();
    }

    //bind click
    @OnClick({R.id.moment_button_send})
    public void onClick(View view) {
        if (view == sendButton) {
            Intent intent = new Intent(this.getContext().getApplicationContext(), MomentAddActivity.class);
            startActivity(intent);
        }
    }


    /**
     * CollapsingToolbarLayout 渐变回调
     *
     * {@link XCollapsingToolbarLayout.OnScrimsListener}
     */
    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        if (shown) {
            bgImage.setBackgroundResource(R.drawable.bg_home_search_bar_gray);
            getStatusBarConfig().statusBarDarkFont(true).init();
        } else {
            bgImage.setBackgroundResource(R.drawable.bg_home_search_bar_transparent);
            getStatusBarConfig().statusBarDarkFont(false).init();
        }
    }
}
