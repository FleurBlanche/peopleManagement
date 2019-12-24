package com.hjq.demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.Constants;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.domain.activity.SocialActivity;

import butterknife.BindView;


public final class ActivityShowActivity extends MyActivity {

    @BindView(R.id.activity_show_imageView)
    ImageView activityImage;

    @BindView(R.id.activity_show_text_description)
    TextView activityDescription;

    @BindView(R.id.activity_show_text_startTime)
    EditText activityStartTime;

    @BindView(R.id.activity_show_text_endTime)
    EditText activityEndTime;

    @BindView(R.id.activity_show_text_name)
    EditText activityName;

    @BindView(R.id.activity_show_text_place)
    EditText activityPlace;

    @BindView(R.id.activity_show_text_sponsor)
    EditText activitySponsor;

    SocialActivity shownActivity;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show;
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initData() {
        //todo
        //read data from intent and get data from website
        int id = this.getIntent().getIntExtra("id",0);
        shownActivity = Constants.socialActivityList.get(id);

        //show
        if(shownActivity.getImageBMP() != null){
            activityImage.setImageBitmap(shownActivity.getImageBMP());
        }
        activityEndTime.setText(shownActivity.getEndtime());
        activityName.setText(shownActivity.getName());
        activityStartTime.setText(shownActivity.getStarttime());
        activityPlace.setText(shownActivity.getPlace());
        activitySponsor.setText(shownActivity.getSponsorid());
        activityDescription.setText(shownActivity.getDescription());
    }

}
