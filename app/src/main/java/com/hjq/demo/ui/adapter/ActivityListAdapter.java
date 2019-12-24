package com.hjq.demo.ui.adapter;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseRecyclerViewAdapter;
import com.hjq.demo.R;
import com.hjq.demo.domain.activity.SocialActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.activityVH>{

    List<SocialActivity> socialActivityList;

    ItemClickListener itemClickListener;

    public ActivityListAdapter(List<SocialActivity> socialActivityList) {
        this.socialActivityList = socialActivityList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public activityVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new activityVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull activityVH holder, int position) {
        SocialActivity socialActivity = socialActivityList.get(position);

        //设置default pic
        if(socialActivity.getImageBMP() == null){
            //default,不设置
        }
        else{
            holder.pic.setImageBitmap(socialActivity.getImageBMP());
        }
        holder.name.setText(socialActivity.getName());
        holder.place.setText(socialActivity.getPlace());
        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(position));
    }


    @Override
    public int getItemCount() {
        return socialActivityList.size();
    }

    public static class activityVH extends RecyclerView.ViewHolder{

        @BindView(R.id.activity_list_pic)
        ImageView pic;

        @BindView(R.id.activity_list_name)
        TextView name;

        @BindView(R.id.activity_list_place)
        TextView place;

        public activityVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
