package com.hjq.demo.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.demo.R;
import com.hjq.demo.api.ActivityApi;
import com.hjq.demo.api.Api;
import com.hjq.demo.api.ApiImpl;
import com.hjq.demo.api.FileApi;
import com.hjq.demo.api.UserApi;
import com.hjq.demo.common.Constants;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.domain.activity.SocialActivity;
import com.hjq.demo.ui.activity.ActivityShowActivity;
import com.hjq.demo.ui.activity.HomeActivity;
import com.hjq.demo.ui.adapter.ActivityListAdapter;
import com.hjq.demo.widget.XCollapsingToolbarLayout;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
 *    desc   : 项目炫酷效果示例
 */
public final class TestFragmentA extends MyLazyFragment<HomeActivity>
        implements XCollapsingToolbarLayout.OnScrimsListener {

    //活动列表

    @BindView(R.id.abl_test_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.ctl_test_bar)
    XCollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.t_test_title)
    Toolbar mToolbar;

    @BindView(R.id.tv_test_search)
    TextView mSearchView;


    //全部活动的列表
    @BindView(R.id.activity_list)
    RecyclerView recyclerView;

    ActivityListAdapter activityListAdapter;

    List<SocialActivity>socialActivityList;

    ActivityApi api;

    FileApi fileapi;



    public static TestFragmentA newInstance() {
        return new TestFragmentA();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_a;
    }

    @Override
    protected void initView() {
        // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐
        ImmersionBar.setTitleBar(getAttachActivity(), mToolbar);

        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener(this);

        socialActivityList = new ArrayList<>();
        activityListAdapter = new ActivityListAdapter(socialActivityList);
        recyclerView.setAdapter(activityListAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        api = ApiImpl.getInstance(Api.ACTIVITY, ActivityApi.class);
        fileapi = ApiImpl.getFileInstance(Api.FILE, FileApi.class);
    }

    @Override
    protected void initData() {
        showLoading();

        Constants.socialActivityList = new ArrayList<>();

        //从服务器获取全部的活动信息，然后展示
        //点击每个活动信息后，会跳转到活动信息的show界面
        //获取tag为空""的活动
        api.getTagActivity("").enqueue(new Callback<ResponseBody>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = response.body().string();
                Log.d("apidebug",body);
                if(body.length() >= 3){
                    //存在数据
                    //读取并写入activityList中
                    JSONObject object = JSON.parseObject(body);
                    JSONArray ja = object.getJSONArray("Activity");
                    for(int i = 0; i < ja.size(); ++i){
                        JSONObject ob = ja.getJSONObject(i);
                        SocialActivity sc = new SocialActivity();
                        sc.setId(ob.getLong("id"));
                        sc.setStarttime(ob.getString("starttime"));
                        sc.setEndtime(ob.getString("endtime"));
                        sc.setPlace(ob.getString("place"));
                        sc.setName(ob.getString("name"));
                        sc.setDescription(ob.getString("description"));
                        sc.setTag(ob.getString("tag"));
                        sc.setImage(ob.getString("image"));
                        sc.setSponsorid(ob.getString("sponsorid"));

                        //add to list
                        socialActivityList.add(sc);
                        Constants.socialActivityList.add(sc);
                    }

                    //获取全部互动的图片
                    //todo
                    for(int i = 0; i < socialActivityList.size(); ++i ){
                        //
                        SocialActivity sc = socialActivityList.get(i);
                        String imageUrl = sc.getImage();

                        //api get file
//                      Bitmap bmp;
//                      sc.setImageBMP(bmp);
                    }
                    Collections.sort(socialActivityList, new SocialActivity.socialActivityComparetor());
                    activityListAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showComplete();
            }
        });

        //显示在界面上
        activityListAdapter.notifyDataSetChanged();
        activityListAdapter.setItemClickListener(position -> {
            SocialActivity socialActivity = socialActivityList.get(position);
            Logger.i("Chosen Id: %s", socialActivity.getId());
            Intent intent = new Intent(this.getContext().getApplicationContext(), ActivityShowActivity.class);
            intent.putExtra("id", position);
            //position in the two list is the same
            startActivity(intent);
        });
        showComplete();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public boolean statusBarDarkFont() {
        return mCollapsingToolbarLayout.isScrimsShown();
    }

    /**
     * CollapsingToolbarLayout 渐变回调
     *
     * {@link XCollapsingToolbarLayout.OnScrimsListener}
     */
    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        if (shown) {
            mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_gray);
            getStatusBarConfig().statusBarDarkFont(true).init();
        } else {
            mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_transparent);
            getStatusBarConfig().statusBarDarkFont(false).init();
        }
    }
}