package com.hjq.demo.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.api.ActivityApi;
import com.hjq.demo.api.Api;
import com.hjq.demo.api.ApiImpl;
import com.hjq.demo.api.FileApi;
import com.hjq.demo.api.InformationApi;
import com.hjq.demo.api.UserApi;
import com.hjq.demo.common.Constants;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.domain.user.Info;
import com.hjq.demo.ui.dialog.AddressDialog;
import com.hjq.demo.ui.dialog.InputDialog;
import com.hjq.image.ImageLoader;
import com.hjq.widget.layout.SettingBar;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.SneakyThrows;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/04/20
 *    desc   : 个人资料
 */
public final class PersonalDataActivity extends MyLazyFragment<HomeActivity> {

    @BindView(R.id.iv_person_data_header)
    ImageView mAvatarView;
    @BindView(R.id.sb_person_data_uid)
    SettingBar mIDView;
    @BindView(R.id.sb_person_data_name)
    SettingBar mNameView;
    @BindView(R.id.sb_person_data_address)
    SettingBar mAddressView;
    @BindView(R.id.sb_person_data_phone)
    SettingBar mPhoneView;
    @BindView(R.id.sb_person_data_age)
    SettingBar mAgeView;
    @BindView(R.id.sb_person_data_company)
    SettingBar mCompanyView;
    @BindView(R.id.sb_person_data_email)
    SettingBar mEmailView;
    @BindView(R.id.sb_person_data_motto)
    SettingBar mMottoView;
    @BindView(R.id.sb_person_data_position)
    SettingBar mPositionView;
    @BindView(R.id.sb_person_data_sex)
    SettingBar mSexView;
    @BindView(R.id.sb_person_data_commitButton)
    Button mCommitButton;
    @BindView(R.id.sb_person_data_settingButton)
    Button mSettingButton;

    InformationApi api;
    FileApi fileApi;
    UserApi userApi;

    private String mAvatarUrl;

    public static PersonalDataActivity newInstance() {
        return new PersonalDataActivity();
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected void initView() {

        api = ApiImpl.getInstance(Api.INFORMATION, InformationApi.class);
        fileApi = ApiImpl.getFileInstance(Api.FILE, FileApi.class);
        userApi = ApiImpl.getInstance(Api.USER, UserApi.class);
    }

    @Override
    protected void initData() {
        //根据user设置数据

        //get self information
        api.getUser(Constants.user.getPersoninform()).enqueue(new Callback<ResponseBody>() {
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

                        //todo
                        //获取头像、背景文件等,并设置Constants
                        //fileApi

                        //设置页面文字内容
                        mIDView.setRightText(Long.toString(Constants.info.getId()));
                        mAddressView.setRightText(Constants.info.getAddress());
                        mNameView.setRightText(Constants.info.getName());
                        mMottoView.setRightText(Constants.info.getMotto());
                        mCompanyView.setRightText(Constants.info.getCompany());
                        mPositionView.setRightText(Constants.info.getPosition());
                        mAgeView.setRightText(Integer.toString(Constants.info.getAge()));
                        mSexView.setRightText(Constants.info.getSex());
                        mEmailView.setRightText(Constants.info.getEmail());
                        mPhoneView.setRightText(Constants.info.getMobile());
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    @OnClick({R.id.sb_person_data_settingButton,R.id.sb_person_data_commitButton,R.id.sb_person_data_sex,R.id.sb_person_data_position,R.id.sb_person_data_motto,R.id.sb_person_data_email,R.id.sb_person_data_company,R.id.sb_person_data_age,R.id.iv_person_data_header, R.id.fl_person_data_head, R.id.sb_person_data_name, R.id.sb_person_data_address, R.id.sb_person_data_phone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_person_data_header:
                if (mAvatarUrl != null && !"".equals(mAvatarUrl)) {
                    // 查看头像
                    ImageActivity.start(getActivity(), mAvatarUrl);
                } else {
                    // 选择头像
                    onClick(findViewById(R.id.fl_person_data_head));
                }
                break;
            case R.id.fl_person_data_head:
                PhotoActivity.start(getAttachActivity(), new PhotoActivity.OnPhotoSelectListener() {

                    @Override
                    public void onSelect(List<String> data) {
                        mAvatarUrl = data.get(0);
                        ImageLoader.with(getActivity())
                                .load(mAvatarUrl)
                                .into(mAvatarView);
                    }

                    @Override
                    public void onCancel() {}
                });
                break;
            case R.id.sb_person_data_name:
                new InputDialog.Builder(getAttachActivity())
                        // 标题可以不用填写
                        .setTitle(getString(R.string.personal_data_name_hint))
                        .setContent(mNameView.getRightText())
                        //.setHint(getString(R.string.personal_data_name_hint))
                        //.setConfirm("确定")
                        // 设置 null 表示不显示取消按钮
                        //.setCancel("取消")
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!mNameView.getRightText().equals(content)) {
                                    mNameView.setRightText(content);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {}
                        })
                        .show();
                break;
            case R.id.sb_person_data_address:
                new AddressDialog.Builder(getAttachActivity())
                        //.setTitle("选择地区")
                        // 设置默认省份
                        .setProvince("上海市")
                        // 设置默认城市（必须要先设置默认省份）
                        .setCity("闵行区")
                        // 不选择县级区域
                        //.setIgnoreArea()
                        .setListener(new AddressDialog.OnListener() {

                            @Override
                            public void onSelected(BaseDialog dialog, String province, String city, String area) {
                                String address = province + city + area;
                                if (!mAddressView.getRightText().equals(address)) {
                                    mAddressView.setRightText(address);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {}
                        })
                        .show();
                break;
            case R.id.sb_person_data_phone:
                // 先判断有没有设置过手机号
                new InputDialog.Builder(getAttachActivity())
                        .setTitle("手机号")
                        .setContent(mPhoneView.getRightText())
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!mPhoneView.getRightText().equals(content)) {
                                    mPhoneView.setRightText(content);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {}
                        })
                        .show();
                break;
            case R.id.sb_person_data_age:
                //年龄
                new InputDialog.Builder(getAttachActivity())
                        .setTitle("年龄")
                        .setContent(mAgeView.getRightText())
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!mAgeView.getRightText().equals(content) && isInteger(content)) {
                                    mAgeView.setRightText(content);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {}
                        })
                        .show();
                break;
            case R.id.sb_person_data_company:
                //公司
                new InputDialog.Builder(getAttachActivity())
                        .setTitle("公司")
                        .setContent(mCompanyView.getRightText())
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!mCompanyView.getRightText().equals(content)) {
                                    mCompanyView.setRightText(content);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {}
                        })
                        .show();
                break;
            case R.id.sb_person_data_email:
                //邮箱
                new InputDialog.Builder(getAttachActivity())
                        .setTitle("邮箱")
                        .setContent(mEmailView.getRightText())
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!mEmailView.getRightText().equals(content)) {
                                    mEmailView.setRightText(content);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {}
                        })
                        .show();
                break;
            case R.id.sb_person_data_motto:
                //座右铭
                new InputDialog.Builder(getAttachActivity())
                        .setTitle("座右铭")
                        .setContent(mMottoView.getRightText())
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!mMottoView.getRightText().equals(content)) {
                                    mMottoView.setRightText(content);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {}
                        })
                        .show();
                break;
            case R.id.sb_person_data_position:
                //职位
                new InputDialog.Builder(getAttachActivity())
                        .setTitle("职位")
                        .setContent(mPositionView.getRightText())
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!mPositionView.getRightText().equals(content)) {
                                    mPositionView.setRightText(content);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {}
                        })
                        .show();
                break;
            case R.id.sb_person_data_sex:
                //性别
                new InputDialog.Builder(getAttachActivity())
                        .setTitle("性别")
                        .setContent(mSexView.getRightText())
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!mSexView.getRightText().equals(content)) {
                                    mSexView.setRightText(content);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {}
                        })
                        .show();
                break;
            case R.id.sb_person_data_commitButton:
                Log.d("apidebug", "提交本地修改按钮被点击");
                Log.d("apidebug", Constants.user.getPersoninform());
                //提交本地修改按钮
                //todo
                //api文本内容和图片内容
                //提交文件内容


                //判断添加还是修改
                if(Constants.user.getPersoninform().length() < 10){
                    //原来没有，需要POST添加
                    //构造json字符串
                    JSONObject ob = new JSONObject();
                    ob.put("name",mNameView.getRightText());
                    ob.put("sex",mSexView.getRightText());
                    ob.put("age",Integer.valueOf(mAgeView.getRightText().toString()));
                    ob.put("tag1","上海");
                    ob.put("tag2","IT");
                    ob.put("tag3","CEO");
                    ob.put("company",mCompanyView.getRightText());
                    ob.put("position",mPositionView.getRightText());
                    ob.put("mobile",mPhoneView.getRightText());
                    ob.put("email",mEmailView.getRightText());
                    ob.put("address",mAddressView.getRightText());
                    ob.put("motto",mMottoView.getRightText());
                    ob.put("portrait","PMaS");
                    ob.put("background","PMaS");
                    Log.d("apidebugPOST添加json",ob.toJSONString());
                    RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),ob.toJSONString());
                    api.AddInfo(requestBody).enqueue(new Callback<ResponseBody>() {
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

                                    info.setPortraitBMP(((BitmapDrawable)mAvatarView.getDrawable()).getBitmap());
                                    Constants.info = info;
                                    Constants.user.setPersoninform(Long.toString(info.getId()));

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
                            Log.d("apidebugPOST添加失败",call.request().toString());
                        }
                    });
                }
                else{
                    //本来就有，只需要修改
                    //构造json字符串
                    JSONObject ob = new JSONObject();
                    ob.put("id",Long.valueOf(mIDView.getRightText().toString()));
                    ob.put("name",mNameView.getRightText());
                    ob.put("sex",mSexView.getRightText());
                    ob.put("age",Integer.valueOf(mAgeView.getRightText().toString()));
                    ob.put("tag1","上海");
                    ob.put("tag2","IT");
                    ob.put("tag3","CEO");
                    ob.put("company",mCompanyView.getRightText());
                    ob.put("position",mPositionView.getRightText());
                    ob.put("mobile",mPhoneView.getRightText());
                    ob.put("email",mEmailView.getRightText());
                    ob.put("address",mAddressView.getRightText());
                    ob.put("motto",mMottoView.getRightText());
                    ob.put("portrait","PMaS");
                    ob.put("background","PMaS");
                    Log.d("apidebug",ob.toJSONString());
                    RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),ob.toJSONString());
                    api.modifyInfo(Long.valueOf(mIDView.getRightText().toString()),requestBody).enqueue(new Callback<ResponseBody>() {
                        @SneakyThrows
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("apidebugPUT个人信息修改uri",call.request().toString());
                            if(response.body() != null){
                                String body = response.body().string();
                                Log.d("apidebugPUT个人信息修改返回内容",body);
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

                                    info.setPortraitBMP(((BitmapDrawable)mAvatarView.getDrawable()).getBitmap());
                                    Constants.info = info;
                                    Constants.user.setPersoninform(Long.toString(info.getId()));
                                    Constants.user.setPersoninformObj(info);
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("apidebug个人信息修改失败uri",call.request().toString());
                        }
                    });
                }
                break;
            case R.id.sb_person_data_settingButton:
                //Log.d("debug", "设置");
                startActivity(SettingActivity.class);
                break;
            default:
                break;
        }
    }
}
