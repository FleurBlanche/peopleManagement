package com.hjq.demo.ui.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import com.hjq.demo.api.RelationApi;
import com.hjq.demo.api.UserApi;
import com.hjq.demo.common.Constants;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.domain.relation.Relation;
import com.hjq.demo.domain.user.Info;
import com.hjq.demo.domain.user.User;
import com.hjq.demo.ui.activity.CopyActivity;
import com.hjq.demo.ui.activity.FriendAddActivity;
import com.hjq.demo.ui.activity.FriendDataActivity;
import com.hjq.demo.ui.activity.FriendRequestActivity;
import com.hjq.demo.ui.activity.HomeActivity;
import com.hjq.demo.ui.activity.MomentAddActivity;
import com.hjq.demo.ui.activity.RecommendActivity;
import com.hjq.demo.ui.adapter.FriendListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.OnClick;
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
public final class FriendFragment extends MyLazyFragment<HomeActivity> {

    @BindView(R.id.friend_group_tag1)
    EditText tag1;

    @BindView(R.id.friend_group_tag2)
    EditText tag2;

    @BindView(R.id.friend_group_tag3)
    EditText tag3;

    @BindView(R.id.friend_button_add)
    LinearLayout add_friend;

    @BindView(R.id.friend_button_recommend)
    LinearLayout recommend_friend;

    @BindView(R.id.friend_button_request)
    LinearLayout request_friend;

    //三个分组的列表
    @BindView(R.id.friend_list_tag1)
    RecyclerView recyclerView1;

    @BindView(R.id.friend_list_tag2)
    RecyclerView recyclerView2;

    @BindView(R.id.friend_list_tag3)
    RecyclerView recyclerView3;

    //三个队列和三个适配器
    public static List<User> friendList1;
    public static List<User> friendList2;
    public static List<User> friendList3;

    public static FriendListAdapter adapter1;
    public static FriendListAdapter adapter2;
    public static FriendListAdapter adapter3;

    UserApi userApi;
    FileApi fileApi;
    RelationApi relationApi;
    InformationApi informationApi;


    public static FriendFragment newInstance() {
        return new FriendFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend;
    }

    @Override
    protected void initView() {

        //group: 10 同事 20 生活 30 家人

        userApi = ApiImpl.getInstance(Api.USER, UserApi.class);
        fileApi = ApiImpl.getFileInstance(Api.FILE, FileApi.class);
        relationApi = ApiImpl.getInstance(Api.RELATION, RelationApi.class);
        informationApi = ApiImpl.getInstance(Api.INFORMATION, InformationApi.class);

        friendList1 = new ArrayList<>();
        friendList2 = new ArrayList<>();
        friendList3 = new ArrayList<>();

        adapter1 = new FriendListAdapter(friendList1);
        adapter2 = new FriendListAdapter(friendList2);
        adapter3 = new FriendListAdapter(friendList3);

        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);
        recyclerView3.setAdapter(adapter3);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this.getContext());
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this.getContext());
        layoutManager2.setOrientation(RecyclerView.VERTICAL);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this.getContext());
        layoutManager3.setOrientation(RecyclerView.VERTICAL);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initData() {

        showLoading();

        Constants.momentList = new ArrayList<>();
        Constants.friends = new ArrayList<>();
        Constants.relations = new ArrayList<>();

        //todo
        //read friends from server
        //从自己User的关系列表Relationlist中，获取全部的关系信息Relation，
        //然后根据关系信息，获取全部朋友的信息User和详细信息Info
        //根据历史记录列表，获取全部的历史记录
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


                                                    //添加到相应的列表中
                                                    if(relation.getGroup() == 10){
                                                        friendList1.add(user);
                                                        adapter1.notifyDataSetChanged();
                                                    }
                                                    else if(relation.getGroup() == 20){
                                                        friendList2.add(user);
                                                        adapter2.notifyDataSetChanged();
                                                    }
                                                    else{
                                                        friendList3.add(user);
                                                        adapter3.notifyDataSetChanged();
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

        //test user and info
        User tuser = new User();
        Info info = new Info();
        info.setPortraitBMP(BitmapFactory.decodeResource(this.getResources(), R.drawable.bg_launcher));
        info.setBackgroundBMP(BitmapFactory.decodeResource(this.getResources(), R.drawable.bg_launcher));
        info.setName("test");
        tuser.setPersoninformObj(info);
        friendList1.add(tuser);
        Constants.friends.add(tuser);

        //点击后查看每个好友的具体信息
        adapter1.notifyDataSetChanged();
        adapter1.setItemClickListener(position -> {
            Intent intent = new Intent(this.getContext().getApplicationContext(), FriendDataActivity.class);
            User user = friendList1.get(position);
            intent.putExtra("id",user.getId());
            intent.putExtra("button","delete");
            intent.putExtra("list",1);
            startActivity(intent);
        });

        adapter2.notifyDataSetChanged();
        adapter2.setItemClickListener(position -> {
            Intent intent = new Intent(this.getContext().getApplicationContext(), FriendDataActivity.class);
            User user = friendList2.get(position);
            intent.putExtra("id",user.getId());
            intent.putExtra("button","delete");
            intent.putExtra("list",2);
            startActivity(intent);
        });

        adapter3.notifyDataSetChanged();
        adapter3.setItemClickListener(position -> {
            Intent intent = new Intent(this.getContext().getApplicationContext(), FriendDataActivity.class);
            User user = friendList3.get(position);
            intent.putExtra("id",user.getId());
            intent.putExtra("button","delete");
            intent.putExtra("list",3);
            startActivity(intent);
        });


        showComplete();

    }

    //bind click
    @OnClick({R.id.friend_button_request,R.id.friend_button_add, R.id.friend_button_recommend, R.id.friend_group_tag1,R.id.friend_group_tag2,R.id.friend_group_tag3})
    public void onClick(View view) {
        if (view == add_friend) {
            //添加好友
            Intent intent = new Intent(this.getContext().getApplicationContext(), FriendAddActivity.class);
            startActivity(intent);
        }
        else if(view == recommend_friend){
            //推荐算法
            Intent intent = new Intent(this.getContext().getApplicationContext(), RecommendActivity.class);
            startActivity(intent);
        }
        else if(view == request_friend){
            //好友请求
            //FriendRequestActivity
            Intent intent = new Intent(this.getContext().getApplicationContext(), FriendRequestActivity.class);
            startActivity(intent);
        }
        else if(view == tag1) {
            Log.d("debug","clicked tag1 ");
            if(recyclerView1.getVisibility() == View.VISIBLE){
                recyclerView1.setVisibility(View.GONE);
            }
            else{
                recyclerView1.setVisibility(View.VISIBLE);
            }
        }
        else if(view == tag2) {
            Log.d("debug","clicked tag2 ");
            if(recyclerView2.getVisibility() == View.VISIBLE){
                recyclerView2.setVisibility(View.GONE);
            }
            else{
                recyclerView2.setVisibility(View.VISIBLE);
            }
        }
        else if(view == tag3) {
            Log.d("debug","clicked tag3 ");
            if(recyclerView3.getVisibility() == View.VISIBLE){
                recyclerView3.setVisibility(View.GONE);
            }
            else{
                recyclerView3.setVisibility(View.VISIBLE);
            }
        }
    }
}