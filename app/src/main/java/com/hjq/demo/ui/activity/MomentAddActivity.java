package com.hjq.demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjq.demo.R;
import com.hjq.demo.api.Api;
import com.hjq.demo.api.ApiImpl;
import com.hjq.demo.api.MomentApi;
import com.hjq.demo.api.UserApi;
import com.hjq.demo.common.Constants;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.domain.relation.Moment;
import com.hjq.demo.domain.user.Info;
import com.hjq.image.ImageLoader;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.SneakyThrows;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MomentAddActivity extends MyActivity {

    @BindView(R.id.moment_add_img)
    ImageView image;

    @BindView(R.id.moment_add_picBtn)
    Button addPicButton;

    @BindView(R.id.moment_add_sendBtn)
    Button sendButton;

    @BindView(R.id.moment_add_text)
    EditText text;


    //todo
    //api
    MomentApi momentApi;
    UserApi userApi;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_moment_add;
    }

    @Override
    protected void initView() {

        image.setVisibility(View.GONE);

        userApi = ApiImpl.getInstance(Api.USER, UserApi.class);
        momentApi = ApiImpl.getInstance(Api.MOMENT, MomentApi.class);

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.moment_add_picBtn, R.id.moment_add_sendBtn})
    public void onClick(View view) {
        if (view == addPicButton) {
            //add image
            PhotoActivity.start(this, new PhotoActivity.OnPhotoSelectListener() {
                @Override
                public void onSelect(List<String> data) {
                    image.setVisibility(View.VISIBLE);
                    ImageLoader.with(getApplicationContext())
                            .load(data.get(0))
                            .into(image);
                }
                @Override
                public void onCancel() {
                    toast("取消了");
                }
            });
        }
        else if (view == sendButton){
            //send
            //todo
            //api
            Moment moment = new Moment();
            moment.setText(text.getText().toString());
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = format.format(date);
            moment.setMomenttime(dateStr);
            moment.setImageBMP(((BitmapDrawable)image.getDrawable()).getBitmap());
            Constants.user.setPersoninformObj(Constants.info);
            moment.setUser(Constants.user);
            Constants.momentList.add(moment);

            Collections.sort(MomentActivity.momentList, new Moment.MomentComparetor());
            MomentActivity.momentList.add(moment);
            MomentActivity.momentListAdapter.notifyDataSetChanged();

            //POST添加
            JSONObject ob = new JSONObject();
            ob.put("text",moment.getText());
            ob.put("momenttime",moment.getMomenttime());
            ob.put("image",moment.getImage());
            Log.d("apidebugPOST添加json",ob.toJSONString());
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),ob.toJSONString());
            momentApi.AddMoment(requestBody).enqueue(new Callback<ResponseBody>() {
                @SneakyThrows
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("apidebugPOST添加的请求内容",call.request().toString());
                    if(response.body() != null){
                        String body = response.body().string();
                        Log.d("apidebugPOST添加的返回内容",body);
                        if(body.length() >= 3){
                            //有数据，而且必定是单条数据，直接读取
                            JSONObject ob = JSON.parseObject(body);
                            moment.setId(ob.getLong("id"));

                            //修改user信息
                            JSONObject obu = new JSONObject();
                            obu.put("id",Constants.user.getId());
                            obu.put("username",Constants.user.getUsername());
                            obu.put("passward",Constants.user.getPassward());
                            obu.put("active",Constants.user.getActive());
                            obu.put("authority",Constants.user.getAuthority());
                            //modify moment list
                            Constants.user.setMomentlist(Constants.user.getMomentlist() + "," + ob.getString("id"));
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
                    Log.d("apidebugPOST添加失败",call.request().toString());
                }
            });

        }
    }
}
