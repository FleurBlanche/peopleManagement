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
import com.alibaba.fastjson.JSONObject;
import com.hjq.demo.R;
import com.hjq.demo.api.Api;
import com.hjq.demo.api.ApiImpl;
import com.hjq.demo.api.RecordApi;
import com.hjq.demo.api.UserApi;
import com.hjq.demo.common.Constants;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.domain.activity.SocialActivity;
import com.hjq.demo.domain.relation.Record;
import com.hjq.demo.domain.relation.Relation;
import com.hjq.demo.domain.user.Info;
import com.hjq.demo.domain.user.User;
import com.hjq.demo.ui.adapter.FriendListAdapter;
import com.hjq.demo.ui.adapter.RecordListAdapter;

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


public final class RecordActivity extends MyActivity {

    //添加好友，针对username进行搜索

    @BindView(R.id.record_list)
    RecyclerView recyclerView;

    @BindView(R.id.record_button_add)
    Button addButton;

    public static List<Record> recordList;

    public static RecordListAdapter adapter;

    private Long fid;

    public static Relation selectedRelation;

    //todo
    //api
    RecordApi api;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initView() {

        //好友的id
        fid = getIntent().getLongExtra("id",0L);

        api = ApiImpl.getInstance(Api.RECORD, RecordApi.class);

        recordList = new ArrayList<>();

        adapter = new RecordListAdapter(recordList);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void initData() {

        //读取record
        //todo
        //api
        showLoading();
        List<Long> ids = new ArrayList<>();
        for(int i = 0; i < Constants.relations.size(); ++i){
            if(Constants.relations.get(i).getFriend().equals(Long.toString(fid))){
                //读取到对应的关系
                selectedRelation = Constants.relations.get(i);
                String records = Constants.relations.get(i).getRecordlist();
                ids = Arrays.asList(records.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            }
        }
        //根据历史记录id读取全部历史记录
        for(int i = 0; i < ids.size(); ++i){
            Long id = ids.get(i);
            api.getRecord(id).enqueue(new Callback<ResponseBody>() {
                @SneakyThrows
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.body() != null){
                        String body = response.body().string();
                        Log.d("apidebug读取历史记录",body);
                        if(body.length() >= 3){
                            //有数据，而且必定是单条数据，直接读取
                            JSONObject ob = JSON.parseObject(body);
                            Record record = new Record();
                            record.setId(ob.getLong("id"));
                            record.setRecordtime(ob.getString("recordtime"));
                            record.setText(ob.getString("text"));
                            record.setImage(ob.getString("image"));

                            //todo
                            //获取头像、背景文件等,并设置Constants
                            //fileApi
                            record.setImageBMP(BitmapFactory.decodeResource(getResources(), R.drawable.ic_head_placeholder));

                            recordList.add(record);
                            adapter.notifyDataSetChanged();
                            Collections.sort(recordList, new Record.RecordComparetor());
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        }
//        Record record = new Record();
//        record.setImageBMP(BitmapFactory.decodeResource(this.getResources(), R.drawable.bg_launcher));
//        record.setRecordtime("2019 q92 39");
//        record.setText("初次相见");
//        recordList.add(record);
        Collections.sort(recordList, new Record.RecordComparetor());
        adapter.notifyDataSetChanged();
        adapter.setItemClickListener(position -> {
            return;
        });
        showComplete();
    }


    //bind click
    @OnClick({R.id.record_button_add})
    public void onClick(View view) {
        if (view == addButton) {
            //跳转到添加record的界面
            Intent intent = new Intent(this, RecordAddActivity.class);
            startActivity(intent);
        }
    }
}
