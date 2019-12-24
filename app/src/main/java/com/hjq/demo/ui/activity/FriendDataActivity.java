package com.hjq.demo.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjq.base.BaseDialog;
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
import com.hjq.demo.domain.relation.Moment;
import com.hjq.demo.domain.relation.Relation;
import com.hjq.demo.domain.user.Info;
import com.hjq.demo.domain.user.Request;
import com.hjq.demo.domain.user.User;
import com.hjq.demo.ui.dialog.InputDialog;
import com.hjq.demo.ui.fragment.FriendFragment;
import com.hjq.widget.layout.SettingBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.SneakyThrows;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class FriendDataActivity extends MyActivity {

    @BindView(R.id.iv_friend_data_header)
    ImageView mAvatarView;
    @BindView(R.id.sb_friend_data_uid)
    SettingBar mIDView;
    @BindView(R.id.sb_friend_data_name)
    SettingBar mNameView;
    @BindView(R.id.sb_friend_data_address)
    SettingBar mAddressView;
    @BindView(R.id.sb_friend_data_phone)
    SettingBar mPhoneView;
    @BindView(R.id.sb_friend_data_age)
    SettingBar mAgeView;
    @BindView(R.id.sb_friend_data_company)
    SettingBar mCompanyView;
    @BindView(R.id.sb_friend_data_email)
    SettingBar mEmailView;
    @BindView(R.id.sb_friend_data_motto)
    SettingBar mMottoView;
    @BindView(R.id.sb_friend_data_position)
    SettingBar mPositionView;
    @BindView(R.id.sb_friend_data_sex)
    SettingBar mSexView;
    @BindView(R.id.sb_friend_data_button)
    Button mButton;
    @BindView(R.id.sb_friend_data_record)
    Button mRecordButton;

    //todo
    //api
    UserApi userApi;
    MomentApi momentApi;
    FileApi fileApi;
    RelationApi relationApi;
    InformationApi informationApi;
    RequestApi requestApi;

    private String mAvatarUrl;

    private int pageClass = 0;

    private Integer fid = 0;

    private User requestUser = new User();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friend_data;
    }

    @Override
    protected void initView() {

        fileApi = ApiImpl.getFileInstance(Api.FILE, FileApi.class);
        userApi = ApiImpl.getInstance(Api.USER, UserApi.class);
        momentApi = ApiImpl.getInstance(Api.MOMENT, MomentApi.class);
        relationApi = ApiImpl.getInstance(Api.RELATION, RelationApi.class);
        informationApi = ApiImpl.getInstance(Api.INFORMATION, InformationApi.class);
        requestApi = ApiImpl.getInstance(Api.REQUEST, RequestApi.class);

        mRecordButton.setVisibility(View.GONE);

        fid = getIntent().getIntExtra("id",0);

        //根据intent选择button的样式和文字（添加或者删除）
        String button_flag = getIntent().getStringExtra("button");
        if(button_flag.equals("add")){
            //添加
            mButton.setBackgroundResource(R.drawable.selector_button);
            mButton.setText("添加好友");
            pageClass = 0;
        }
        else if(button_flag.equals("delete")){
            //删除
            mButton.setBackgroundResource(R.drawable.selector_update_button);
            mButton.setText("删除好友");
            mRecordButton.setVisibility(View.VISIBLE);
            pageClass = 1;
        }
        else if(button_flag.equals("recommend")){
            //推荐
            mButton.setBackgroundResource(R.drawable.selector_button);
            mButton.setText("添加好友");
            pageClass = 2;
        }
        else{
            //同意请求
            mButton.setBackgroundResource(R.drawable.selector_button);
            mButton.setText("同意请求");
            pageClass = 3;
        }
    }

    @Override
    protected void initData() {

        //根据传入的用户id，写入该用户的全部信息
        //todo
        //api
        User user;

        if(pageClass == 0){
            //add
            user = FriendAddActivity.friendList.get(fid);
            Info info = user.getPersoninformObj();
            mIDView.setRightText(Long.toString(info.getId()));
            mAddressView.setRightText(info.getAddress());
            mNameView.setRightText(info.getName());
            mMottoView.setRightText(info.getMotto());
            mCompanyView.setRightText(info.getCompany());
            mPositionView.setRightText(info.getPosition());
            mAgeView.setRightText(Integer.toString(info.getAge()));
            mSexView.setRightText(info.getSex());
            mEmailView.setRightText(info.getEmail());
            mPhoneView.setRightText(info.getMobile());
        }
        else if(pageClass == 1){
            //delete
            Integer list = getIntent().getIntExtra("list",1);
            if(list == 1){
                user = FriendFragment.friendList1.get(fid);
            }
            else if(list == 2){
                user = FriendFragment.friendList2.get(fid);
            }
            else {
                user = FriendFragment.friendList3.get(fid);
            }
            Info info = user.getPersoninformObj();
            mIDView.setRightText(Long.toString(info.getId()));
            mAddressView.setRightText(info.getAddress());
            mNameView.setRightText(info.getName());
            mMottoView.setRightText(info.getMotto());
            mCompanyView.setRightText(info.getCompany());
            mPositionView.setRightText(info.getPosition());
            mAgeView.setRightText(Integer.toString(info.getAge()));
            mSexView.setRightText(info.getSex());
            mEmailView.setRightText(info.getEmail());
            mPhoneView.setRightText(info.getMobile());
        }
        else if(pageClass == 2){
            //recommend
            user = RecommendActivity.friendList.get(fid);
            Info info = user.getPersoninformObj();
            mIDView.setRightText(Long.toString(info.getId()));
            mAddressView.setRightText(info.getAddress());
            mNameView.setRightText(info.getName());
            mMottoView.setRightText(info.getMotto());
            mCompanyView.setRightText(info.getCompany());
            mPositionView.setRightText(info.getPosition());
            mAgeView.setRightText(Integer.toString(info.getAge()));
            mSexView.setRightText(info.getSex());
            mEmailView.setRightText(info.getEmail());
            mPhoneView.setRightText(info.getMobile());
        }
        else{
            //agree
            user = new User();
            Info info = new Info();
            Request request = FriendRequestActivity.requestList.get(fid);
            //获取user和info
            Long uid = Long.getLong(request.getSender());
            userApi.getUser(uid).enqueue(new Callback<ResponseBody>() {
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
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                }
                            });

                            //set
                            mIDView.setRightText(Long.toString(info.getId()));
                            mAddressView.setRightText(info.getAddress());
                            mNameView.setRightText(info.getName());
                            mMottoView.setRightText(info.getMotto());
                            mCompanyView.setRightText(info.getCompany());
                            mPositionView.setRightText(info.getPosition());
                            mAgeView.setRightText(Integer.toString(info.getAge()));
                            mSexView.setRightText(info.getSex());
                            mEmailView.setRightText(info.getEmail());
                            mPhoneView.setRightText(info.getMobile());

                            requestUser = user;
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        }
    }

    //bind click
    @OnClick({R.id.sb_friend_data_record,R.id.sb_friend_data_button})
    public void onClick(View view) {
        if (view == mButton) {
            //添加或者删除button
            //todo
            //api
            showLoading();

            if(pageClass == 0 || pageClass == 2){
                //add
                new InputDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle("请输入验证信息")
                        .setContent("")
                        //.setHint(getString(R.string.personal_data_name_hint))
                        //.setConfirm("确定")
                        // 设置 null 表示不显示取消按钮
                        //.setCancel("取消")
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                //content为输入的验证信息
                                //todo
                                //api添加一个request
                                //更新到两个用户的对应requestlist
                                JSONObject ob = new JSONObject();
                                ob.put("content",content);
                                ob.put("senderid",Constants.user.getId());
                                ob.put("getterid",FriendAddActivity.friendList.get(fid).getId());
                                Date date = new Date();
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String dateStr = format.format(date);
                                ob.put("sendtime",dateStr);
                                RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),ob.toJSONString());
                                requestApi.addRequest(requestBody).enqueue(new Callback<ResponseBody>() {
                                    @SneakyThrows
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Log.d("apidebugPOET request",call.request().toString());
                                        if(response.body() != null){
                                            String body = response.body().string();
                                            Log.d("apidebugPOET request",body);
                                            if(body.length() >= 3){
                                                //有数据，而且必定是单条数据，直接读取
                                                JSONObject ob = JSON.parseObject(body);
                                                Request request = new Request();
                                                request.setId(ob.getLong("id"));
                                                request.setSender(ob.getString("senderid"));
                                                request.setSendtime(ob.getString("sendtime"));
                                                request.setGetter(ob.getString("getterid"));
                                                request.setContent(ob.getString("content"));

                                                //修改两个用户的requestlist
                                                Constants.user.setRequestsendlist(Constants.user.getRequestsendlist() + "," + ob.getString("id"));
                                                //修改user信息
                                                JSONObject obu = new JSONObject();
                                                obu.put("id",Constants.user.getId());
                                                obu.put("username",Constants.user.getUsername());
                                                obu.put("passward",Constants.user.getPassward());
                                                obu.put("active",Constants.user.getActive());
                                                obu.put("authority",Constants.user.getAuthority());
                                                obu.put("momentlist",Constants.user.getMomentlist());
                                                obu.put("relationlist",Constants.user.getRelationlist());
                                                obu.put("value",Constants.user.getValue());
                                                obu.put("requestsendlist",Constants.user.getRequestsendlist());
                                                obu.put("requestgetlist",Constants.user.getRequestgetlist());
                                                obu.put("personinform",Constants.user.getPersoninform());
                                                Log.d("apidebugPUT修改json",obu.toJSONString());
                                                RequestBody requestBodyu = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),obu.toJSONString());
                                                userApi.modifyInfo(Constants.user.getId(),requestBodyu).enqueue(new Callback<ResponseBody>() {
                                                    @SneakyThrows
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        Log.d("apidebugPUT修改用户信息的请求uri",call.request().toString());
                                                        if(response.body() != null){
                                                            String body = response.body().string();
                                                            Log.d("apidebugPUT修改用户信息返回内容",body);
                                                            if(body.length() >= 3){
                                                                //有数据，而且必定是单条数据，直接读取
                                                                //本地已经更新，不同读取
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        Log.d("apidebugPUT修改失败uri",call.request().toString());
                                                    }
                                                });

                                                //修改两个用户的requestlist
                                                FriendAddActivity.friendList.get(fid).setRequestgetlist(FriendAddActivity.friendList.get(fid).getRequestgetlist() + "," + ob.getString("id"));
                                                //修改user信息
                                                JSONObject obf = new JSONObject();
                                                obf.put("id",FriendAddActivity.friendList.get(fid).getId());
                                                obf.put("username",FriendAddActivity.friendList.get(fid).getUsername());
                                                obf.put("passward",FriendAddActivity.friendList.get(fid).getPassward());
                                                obf.put("active",FriendAddActivity.friendList.get(fid).getActive());
                                                obf.put("authority",FriendAddActivity.friendList.get(fid).getAuthority());
                                                obf.put("momentlist",FriendAddActivity.friendList.get(fid).getMomentlist());
                                                obf.put("relationlist",FriendAddActivity.friendList.get(fid).getRelationlist());
                                                obf.put("value",FriendAddActivity.friendList.get(fid).getValue());
                                                obf.put("requestsendlist",FriendAddActivity.friendList.get(fid).getRequestsendlist());
                                                obf.put("requestgetlist",FriendAddActivity.friendList.get(fid).getRequestgetlist());
                                                obf.put("personinform",FriendAddActivity.friendList.get(fid).getPersoninform());
                                                Log.d("apidebugPUT修改json",obu.toJSONString());
                                                RequestBody requestBodyf = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),obf.toJSONString());
                                                userApi.modifyInfo(FriendAddActivity.friendList.get(fid).getId(),requestBodyf).enqueue(new Callback<ResponseBody>() {
                                                    @SneakyThrows
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        Log.d("apidebugPUT修改用户信息的请求uri",call.request().toString());
                                                        if(response.body() != null){
                                                            String body = response.body().string();
                                                            Log.d("apidebugPUT修改用户信息返回内容",body);
                                                            if(body.length() >= 3){
                                                                //有数据，而且必定是单条数据，直接读取
                                                                //本地已经更新，不同读取
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        Log.d("apidebugPUT修改失败uri",call.request().toString());
                                                    }
                                                });
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.d("apidebug个人信息修改失败uri",call.request().toString());
                                    }
                                });
                            }
                            @Override
                            public void onCancel(BaseDialog dialog) {}
                        })
                        .show();
            }
            else if(pageClass == 1){
                //delete
                //todo
                //api删除两个relation（双方的）(不做操作)
                //更新两个用户的relationlist
            }
            else if(pageClass == 3){
                //同意请求
                //todo
                //api删除一个request（不做操作）
                //api添加两个relation（双方的）
                //更新两个用户的relationlist和对应的requestlist
                Relation relation1 = new Relation();
                relation1.setRecordlist("");
                relation1.setMemo("");
                relation1.setGroup(10);
                relation1.setFriend(mIDView.getRightText().toString());
                JSONObject ob1 = new JSONObject();
                ob1.put("friend",relation1.getFriend());
                ob1.put("group",relation1.getGroup());
                ob1.put("memo",relation1.getMemo());
                ob1.put("recordlist",relation1.getRecordlist());
                RequestBody requestBody1 = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),ob1.toJSONString());
                relationApi.addRelation(requestBody1).enqueue(new Callback<ResponseBody>() {
                    @SneakyThrows
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("apidebugPOET Relation",call.request().toString());
                        if(response.body() != null){
                            String body = response.body().string();
                            Log.d("apidebugPOET Relation",body);
                            if(body.length() >= 3){
                                //有数据，而且必定是单条数据，直接读取
                                JSONObject ob = JSON.parseObject(body);
                                relation1.setId(ob.getLong("id"));
                                //修改用户的relationlist和requestlist
                                Constants.user.setRelationlist(Constants.user.getRelationlist() + "," + ob.getString("id"));
                                //
                                String lists1 = Constants.user.getRequestgetlist();
                                List<Long> listl1 = Arrays.asList(lists1.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                                String out = "";
                                for(int i = 0; i < listl1.size(); ++i){
                                    if(!listl1.get(i).equals(relation1.getId())){
                                        if(i != 0){
                                            out += ",";
                                        }
                                        out += listl1.get(i).toString();
                                    }
                                }
                                Constants.user.setRequestgetlist(out);
                                //
                                //
                                //修改user信息
                                JSONObject obu = new JSONObject();
                                obu.put("id",Constants.user.getId());
                                obu.put("username",Constants.user.getUsername());
                                obu.put("passward",Constants.user.getPassward());
                                obu.put("active",Constants.user.getActive());
                                obu.put("authority",Constants.user.getAuthority());
                                obu.put("momentlist",Constants.user.getMomentlist());
                                obu.put("relationlist",Constants.user.getRelationlist());
                                obu.put("value",Constants.user.getValue());
                                obu.put("requestsendlist",Constants.user.getRequestsendlist());
                                obu.put("requestgetlist",Constants.user.getRequestgetlist());
                                obu.put("personinform",Constants.user.getPersoninform());
                                Log.d("apidebugPUT修改json",obu.toJSONString());
                                RequestBody requestBodyu = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),obu.toJSONString());
                                userApi.modifyInfo(Constants.user.getId(),requestBodyu).enqueue(new Callback<ResponseBody>() {
                                    @SneakyThrows
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Log.d("apidebugPUT修改用户信息的请求uri",call.request().toString());
                                        if(response.body() != null){
                                            String body = response.body().string();
                                            Log.d("apidebugPUT修改用户信息返回内容",body);
                                            if(body.length() >= 3){
                                                //有数据，而且必定是单条数据，直接读取
                                                //本地已经更新，不同读取
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.d("apidebugPUT修改失败uri",call.request().toString());
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("apidebug个人信息修改失败uri",call.request().toString());
                    }
                });

                Relation relation2 = new Relation();
                relation2.setRecordlist("");
                relation2.setMemo("");
                relation2.setGroup(10);
                relation2.setFriend(Constants.user.getId().toString());
                JSONObject ob2 = new JSONObject();
                ob2.put("friend",relation2.getFriend());
                ob2.put("group",relation2.getGroup());
                ob2.put("memo",relation2.getMemo());
                ob2.put("recordlist",relation2.getRecordlist());
                RequestBody requestBody2 = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),ob2.toJSONString());
                relationApi.addRelation(requestBody2).enqueue(new Callback<ResponseBody>() {
                    @SneakyThrows
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("apidebugPOET Relation",call.request().toString());
                        if(response.body() != null){
                            String body = response.body().string();
                            Log.d("apidebugPOET Relation",body);
                            if(body.length() >= 3){
                                //有数据，而且必定是单条数据，直接读取
                                JSONObject ob = JSON.parseObject(body);
                                relation2.setId(ob.getLong("id"));
                                Integer list = getIntent().getIntExtra("list",1);
                                //修改用户的relationlist和requestlist
                                requestUser.setRelationlist(requestUser.getRelationlist() + "," + ob.getString("id"));
                                //
                                String lists1 = requestUser.getRequestgetlist();
                                List<Long> listl1 = Arrays.asList(lists1.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                                String out = "";
                                for(int i = 0; i < listl1.size(); ++i){
                                    if(!listl1.get(i).equals(relation1.getId())){
                                        if(i != 0){
                                            out += ",";
                                        }
                                        out += listl1.get(i).toString();
                                    }
                                }
                                requestUser.setRequestgetlist(out);
                                //
                                //
                                //修改user信息
                                JSONObject obu = new JSONObject();
                                obu.put("id",requestUser.getId());
                                obu.put("username",requestUser.getUsername());
                                obu.put("passward",requestUser.getPassward());
                                obu.put("active",requestUser.getActive());
                                obu.put("authority",requestUser.getAuthority());
                                obu.put("momentlist",requestUser.getMomentlist());
                                obu.put("relationlist",requestUser.getRelationlist());
                                obu.put("value",requestUser.getValue());
                                obu.put("requestsendlist",requestUser.getRequestsendlist());
                                obu.put("requestgetlist",requestUser.getRequestgetlist());
                                obu.put("personinform",requestUser.getPersoninform());
                                Log.d("apidebugPUT修改json",obu.toJSONString());
                                RequestBody requestBodyu = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),obu.toJSONString());
                                userApi.modifyInfo(requestUser.getId(),requestBodyu).enqueue(new Callback<ResponseBody>() {
                                    @SneakyThrows
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Log.d("apidebugPUT修改用户信息的请求uri",call.request().toString());
                                        if(response.body() != null){
                                            String body = response.body().string();
                                            Log.d("apidebugPUT修改用户信息返回内容",body);
                                            if(body.length() >= 3){
                                                //有数据，而且必定是单条数据，直接读取
                                                //本地已经更新，不同读取
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.d("apidebugPUT修改失败uri",call.request().toString());
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("apidebug个人信息修改失败uri",call.request().toString());
                    }
                });
            }

            showComplete();

        }
        else {
            //查看历史记录，传入查看人的uid,在relation列表中，查看历史记录列表id，然后获取
            Intent intent = new Intent(this,RecordActivity.class);
            intent.putExtra("id",fid);
            startActivity(intent);
        }
    }
}
