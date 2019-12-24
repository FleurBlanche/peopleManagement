package com.hjq.demo.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjq.demo.R;
import com.hjq.demo.api.Api;
import com.hjq.demo.api.ApiImpl;
import com.hjq.demo.api.FileApi;
import com.hjq.demo.api.InformationApi;
import com.hjq.demo.api.MomentApi;
import com.hjq.demo.api.RelationApi;
import com.hjq.demo.api.RequestApi;
import com.hjq.demo.api.UserApi;
import com.hjq.demo.common.Constants;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.domain.user.Info;
import com.hjq.demo.domain.user.User;
import com.hjq.demo.ui.adapter.FriendListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public final class FriendAddActivity extends MyActivity {

    //添加好友，针对username进行搜索

    @BindView(R.id.friend_add_list)
    RecyclerView recyclerView;

    @BindView(R.id.friend_add_nameText)
    EditText name;

    @BindView(R.id.friend_add_searchButton)
    Button searchButton;

    public static List<User> friendList;

    FriendListAdapter adapter;

    //todo
    //api
    UserApi userApi;
    MomentApi momentApi;
    FileApi fileApi;
    RelationApi relationApi;
    InformationApi informationApi;
    RequestApi requestApi;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friend_add;
    }

    @Override
    protected void initView() {

        fileApi = ApiImpl.getFileInstance(Api.FILE, FileApi.class);
        userApi = ApiImpl.getInstance(Api.USER, UserApi.class);
        momentApi = ApiImpl.getInstance(Api.MOMENT, MomentApi.class);
        relationApi = ApiImpl.getInstance(Api.RELATION, RelationApi.class);
        informationApi = ApiImpl.getInstance(Api.INFORMATION, InformationApi.class);
        requestApi = ApiImpl.getInstance(Api.REQUEST, RequestApi.class);

        friendList = new ArrayList<>();

        adapter = new FriendListAdapter(friendList);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void initData() {

        showLoading();

        showComplete();

    }


    //bind click
    @OnClick({R.id.friend_add_searchButton})
    public void onClick(View view) {
        showLoading();

        //清除
        friendList.clear();

        if (view == searchButton) {
            //搜索
            //todo
            //api
            //String sname = "(like)";
            String sname;
            sname = name.getText().toString();
            userApi.searchUser(sname)
                    .enqueue(new Callback<ResponseBody>() {
                        @SneakyThrows
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String jsonStr = response.body().string();
                            Log.d("apidebug模糊搜索",jsonStr);
                            Log.d("apidebug模糊搜索",call.request().toString());
                            if(jsonStr.length() <= 3){
                            }
                            else{
                                JSONObject object = JSON.parseObject(jsonStr);
                                JSONArray ja = object.getJSONArray("User");

                                for(int index = 0; index < ja.size(); ++index){
                                    JSONObject ject = ja.getJSONObject(index);
                                    Log.d("apidebug模糊搜索结果",ject.toJSONString());
                                    User user = new User();
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

                                    //get self information
                                    informationApi.getUser(user.getPersoninform()).enqueue(new Callback<ResponseBody>() {
                                        @SneakyThrows
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if(response.body() != null){
                                                String body = response.body().string();
                                                Log.d("apidebug 读取用户信息表",body);
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

                                                    user.setPersoninformObj(info);
                                                    friendList.add(user);
                                                    adapter.notifyDataSetChanged();
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

            adapter.notifyDataSetChanged();
            adapter.setItemClickListener(position -> {
                Intent intent = new Intent(this,FriendDataActivity.class);
                intent.putExtra("button","add");
                intent.putExtra("search",true);
                intent.putExtra("id",position);
                startActivity(intent);
            });

            showComplete();

        }
    }
}
