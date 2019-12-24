package com.hjq.demo.ui.activity;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.domain.user.Info;
import com.hjq.demo.domain.user.User;
import com.hjq.demo.ui.adapter.FriendListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public final class RecommendActivity extends MyActivity {

    @BindView(R.id.recommend_list)
    RecyclerView recyclerView;

    public static List<User> friendList;

    FriendListAdapter adapter;

    //todo
    //api

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recommend;
    }

    @Override
    protected void initView() {

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

        //todo
        //get recommendList

        User user = new User();
        Info info = new Info();
        info.setPortraitBMP(BitmapFactory.decodeResource(this.getResources(), R.drawable.bg_launcher));
        info.setBackgroundBMP(BitmapFactory.decodeResource(this.getResources(), R.drawable.bg_launcher));
        info.setName("jianglikang");
        user.setPersoninformObj(info);

        friendList.add(user);
        adapter.notifyDataSetChanged();
        adapter.setItemClickListener(position -> {
//            Intent intent = new Intent(this,FriendDataActivity.class);
//            intent.putExtra("button","add");
//            startActivity(intent);
        });

        showComplete();

    }
}
