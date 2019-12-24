package com.hjq.demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.domain.relation.Record;
import com.hjq.demo.ui.adapter.RecordListAdapter;
import com.hjq.image.ImageLoader;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RecordAddActivity extends MyActivity {

    @BindView(R.id.record_add_img)
    ImageView image;

    @BindView(R.id.record_add_picBtn)
    Button addPicButton;

    @BindView(R.id.record_add_sendBtn)
    Button sendButton;

    @BindView(R.id.record_add_text)
    EditText text;


    //todo
    //api

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_add;
    }

    @Override
    protected void initView() {

        image.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.record_add_picBtn, R.id.record_add_sendBtn})
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
            Record record = new Record();
            record.setId(1L);
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = format.format(date);
            record.setRecordtime(dateStr);
            record.setText(text.getText().toString());
            record.setImage("");
            record.setImageBMP(((BitmapDrawable)image.getDrawable()).getBitmap());

            RecordActivity.recordList.add(record);
            RecordActivity.adapter.notifyDataSetChanged();
        }
    }
}
