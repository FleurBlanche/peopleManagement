package com.hjq.demo.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.JsonUtils;
import com.hjq.demo.R;
import com.hjq.demo.api.Api;
import com.hjq.demo.api.ApiImpl;
import com.hjq.demo.api.InformationApi;
import com.hjq.demo.api.UserApi;
import com.hjq.demo.common.Constants;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.common.response.CustomCallback;
import com.hjq.demo.common.response.Result;
import com.hjq.demo.common.response.ResultCode;
import com.hjq.demo.domain.user.Info;
import com.hjq.demo.domain.user.User;
import com.hjq.demo.helper.InputTextHelper;
import com.hjq.demo.other.IntentKey;
import com.hjq.demo.other.KeyboardWatcher;
import com.hjq.demo.wxapi.WXEntryActivity;
import com.hjq.image.ImageLoader;
import com.hjq.umeng.Platform;
import com.hjq.umeng.UmengClient;
import com.hjq.umeng.UmengLogin;

import java.util.Map;

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
 *    desc   : 登录界面
 */
public final class LoginActivity extends MyActivity
        implements UmengLogin.OnLoginListener,
        KeyboardWatcher.SoftKeyboardStateListener {

    @BindView(R.id.iv_login_logo)
    ImageView mLogoView;

    @BindView(R.id.ll_login_body)
    LinearLayout mBodyLayout;
    @BindView(R.id.et_login_phone)
    EditText mPhoneView;
    @BindView(R.id.et_login_password)
    EditText mPasswordView;

    @BindView(R.id.btn_login_commit)
    Button mCommitView;

    @BindView(R.id.v_login_blank)
    View mBlankView;

    @BindView(R.id.ll_login_other)
    View mOtherView;
    @BindView(R.id.iv_login_qq)
    View mQQView;
    @BindView(R.id.iv_login_wx)
    View mWeChatView;

    UserApi api;
    InformationApi infoApi;

    /** logo 缩放比例 */
    private final float mLogoScale = 0.8f;
    /** 动画时间 */
    private final int mAnimTime = 300;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        InputTextHelper.with(this)
                .addView(mPhoneView)
                .addView(mPasswordView)
                .setMain(mCommitView)
                .setListener(new InputTextHelper.OnInputTextListener() {

                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return mPhoneView.getText().toString().length() >= 1 &&
                                mPasswordView.getText().toString().length() >= 1;
                    }
                })
                .build();

        post(new Runnable() {

            @Override
            public void run() {
                // 因为在小屏幕手机上面，因为计算规则的因素会导致动画效果特别夸张，所以不在小屏幕手机上面展示这个动画效果
                if (mBlankView.getHeight() > mBodyLayout.getHeight()) {
                    // 只有空白区域的高度大于登录框区域的高度才展示动画
                    KeyboardWatcher.with(LoginActivity.this)
                            .setListener(LoginActivity.this);
                }
            }
        });
    }

    @Override
    protected void initData() {
        // 判断用户当前有没有安装 QQ
        if (!UmengClient.isAppInstalled(this, Platform.QQ)) {
            mQQView.setVisibility(View.GONE);
        }

        // 判断用户当前有没有安装微信
        if (!UmengClient.isAppInstalled(this, Platform.WECHAT)) {
            mWeChatView.setVisibility(View.GONE);
        }

        //hide the two views (do not apply these two method to login)
        mQQView.setVisibility(View.GONE);
        mWeChatView.setVisibility(View.GONE);

        // 如果这两个都没有安装就隐藏提示
        if (mQQView.getVisibility() == View.GONE && mWeChatView.getVisibility() == View.GONE) {
            mOtherView.setVisibility(View.GONE);
        }

        api = ApiImpl.getInstance(Api.USER,UserApi.class);
        infoApi = ApiImpl.getInstance(Api.INFORMATION, InformationApi.class);
    }

    @Override
    public void onRightClick(View v) {
        // 跳转到注册界面
        startActivityForResult(RegisterActivity.class, new ActivityCallback() {

            @Override
            public void onActivityResult(int resultCode, @Nullable Intent data) {
                // 如果已经注册成功，就执行登录操作
                if (resultCode == RESULT_OK && data != null) {
                    mPhoneView.setText(data.getStringExtra(IntentKey.PHONE));
                    mPasswordView.setText(data.getStringExtra(IntentKey.PASSWORD));
                    onClick(mCommitView);
                }
            }
        });
    }

    @OnClick({R.id.tv_login_forget, R.id.btn_login_commit, R.id.iv_login_qq, R.id.iv_login_wx})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_forget:
                startActivity(PasswordForgetActivity.class);
                break;
            case R.id.btn_login_commit:
                showLoading();
                api.login(mPhoneView.getText().toString(), mPasswordView.getText().toString())
                        .enqueue(new Callback<ResponseBody>() {
                            @SneakyThrows
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                String jsonStr = response.body().string();
                                Log.d("apidebug",jsonStr);
                                Log.d("apidebug",Integer.toString(jsonStr.length()));
                                if(jsonStr.length() <= 3){
                                    //失败
                                    showComplete();
                                    toast("登录失败，请重新登录");
                                }
                                else{
                                    JSONObject object = JSON.parseObject(jsonStr);
                                    JSONArray ja = object.getJSONArray("User");
                                    JSONObject ject = ja.getJSONObject(0);
                                    Log.d("apidebug",ject.toJSONString());
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

                                    Constants.user = user;
                                    showComplete();
                                    toast("登录成功");
                                    //get self information
                                    infoApi.getUser(Constants.user.getPersoninform()).enqueue(new Callback<ResponseBody>() {
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
                                                }
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        }
                                    });


                                    startActivity(HomeActivity.class);
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                showComplete();
                                toast("登录失败，请重新登录");
                            }
                        });
//                break;
                //blocked third party
                break;
            case R.id.iv_login_qq:
            case R.id.iv_login_wx:
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 友盟登录回调
        UmengClient.onActivityResult(this, requestCode, resultCode, data);
    }

    /**
     * 友盟第三方登录授权回调接口
     * {@link UmengLogin.OnLoginListener}
     */

    /**
     * 授权成功的回调
     *
     * @param platform      平台名称
     * @param data          用户资料返回
     */
    @Override
    public void onSucceed(Platform platform, UmengLogin.LoginData data) {
        // 判断第三方登录的平台
        switch (platform) {
            case QQ:
                break;
            case WECHAT:
                break;
            default:
                break;
        }

        ImageLoader.with(this)
                .load(data.getIcon())
                .circle()
                .into(mLogoView);

        toast("昵称：" + data.getName() + "\n" + "性别：" + data.getSex());
        toast("id：" + data.getId());
        toast("token：" + data.getToken());
    }

    /**
     * 授权失败的回调
     *
     * @param platform      平台名称
     * @param t             错误原因
     */
    @Override
    public void onError(Platform platform, Throwable t) {
        toast("第三方登录出错：" + t.getMessage());
    }

    /**
     * 授权取消的回调
     *
     * @param platform      平台名称
     */
    @Override
    public void onCancel(Platform platform) {
        toast("取消第三方登录");
    }

    /**
     * {@link KeyboardWatcher.SoftKeyboardStateListener}
     */

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int[] location = new int[2];
        // 获取这个 View 在屏幕中的坐标（左上角）
        mBodyLayout.getLocationOnScreen(location);
        //int x = location[0];
        int y = location[1];
        int bottom = screenHeight - (y + mBodyLayout.getHeight());
        if (keyboardHeight > bottom){
            // 执行位移动画
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBodyLayout, "translationY", 0, -(keyboardHeight - bottom));
            objectAnimator.setDuration(mAnimTime);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.start();

            // 执行缩小动画
            mLogoView.setPivotX(mLogoView.getWidth() / 2f);
            mLogoView.setPivotY(mLogoView.getHeight());
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mLogoView, "scaleX", 1.0f, mLogoScale);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mLogoView, "scaleY", 1.0f, mLogoScale);
            ObjectAnimator translationY = ObjectAnimator.ofFloat(mLogoView, "translationY", 0.0f, -(keyboardHeight - bottom));
            animatorSet.play(translationY).with(scaleX).with(scaleY);
            animatorSet.setDuration(mAnimTime);
            animatorSet.start();
        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        // 执行位移动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBodyLayout, "translationY", mBodyLayout.getTranslationY(), 0);
        objectAnimator.setDuration(mAnimTime);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        if (mLogoView.getTranslationY() == 0){
            return;
        }
        // 执行放大动画
        mLogoView.setPivotX(mLogoView.getWidth() / 2f);
        mLogoView.setPivotY(mLogoView.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mLogoView, "scaleX", mLogoScale, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mLogoView, "scaleY", mLogoScale, 1.0f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mLogoView, "translationY", mLogoView.getTranslationY(), 0);
        animatorSet.play(translationY).with(scaleX).with(scaleY);
        animatorSet.setDuration(mAnimTime);
        animatorSet.start();
    }
}