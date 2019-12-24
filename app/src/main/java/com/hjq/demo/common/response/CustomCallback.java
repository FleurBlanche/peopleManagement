package com.hjq.demo.common.response;


import android.util.Log;

import com.hjq.toast.ToastUtils;

import com.hjq.demo.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CustomCallback<T> implements Callback<Result<T>> {
    public abstract void uiBeforeStart();

    public abstract void processResult(T res);

    @Override
    public void onResponse(Call<Result<T>> call, Response<Result<T>> response) {
        uiBeforeStart();
        if (response.code() == 200) {
            Result<T> res = response.body();
            Log.d("apidebug",call.request().toString());
            Log.d("apidebug",res.getData().toString());
            if (res.getCode() == ResultCode.SUCCESS.getCode()) {
                processResult(res.getData());
            }
            else {
                ToastUtils.show(res.getMessage());
            }
        }
        else {
            ToastUtils.show(response.message());
        }
    }

    @Override
    public void onFailure(Call<Result<T>> call, Throwable t) {
        uiBeforeStart();
        ToastUtils.show(R.string.hint_layout_error_network);
    }
}
