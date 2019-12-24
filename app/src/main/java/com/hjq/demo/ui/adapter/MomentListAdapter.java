package com.hjq.demo.ui.adapter;

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
import com.hjq.demo.domain.relation.Moment;
import com.hjq.demo.domain.user.User;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MomentListAdapter extends RecyclerView.Adapter<MomentListAdapter.momentVH>{

    List<Moment> momentList;

    ItemClickListener itemClickListener;

    public MomentListAdapter(List<Moment> momentList) {
        this.momentList = momentList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public momentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moment, parent, false);
        return new momentVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull momentVH holder, int position) {
        Moment moment = momentList.get(position);

        //todo
        //set data
        if(moment.getImageBMP() == null){
            //default,不设置
        }
        else{
            holder.pic.setImageBitmap(moment.getImageBMP());
        }
        holder.content.setText(moment.getText());
        holder.time.setText(moment.getMomenttime());

        //get friend's header and name
        holder.name.setText(moment.getUser().getPersoninformObj().getName());

        if(moment.getUser().getPersoninformObj().getPortraitBMP() == null){
            //default,不设置
        }
        else{
            holder.header.setImageBitmap(moment.getUser().getPersoninformObj().getPortraitBMP());
        }
        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return momentList.size();
    }

    public static class momentVH extends RecyclerView.ViewHolder{

        @BindView(R.id.moment_list_header)
        ImageView header;

        @BindView(R.id.moment_list_name)
        TextView name;

        @BindView(R.id.moment_list_pic)
        ImageView pic;

        @BindView(R.id.moment_list_content)
        TextView content;

        @BindView(R.id.moment_list_time)
        TextView time;

        public momentVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
