package com.hjq.demo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseRecyclerViewAdapter;
import com.hjq.demo.R;
import com.hjq.demo.domain.activity.SocialActivity;
import com.hjq.demo.domain.user.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.friendVH>{

    List<User> friendList;

    ItemClickListener itemClickListener;

    public FriendListAdapter(List<User> friendList) {
        this.friendList = friendList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public friendVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new friendVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull friendVH holder, int position) {
        User friend = friendList.get(position);

        if(friend.getPersoninformObj().getPortraitBMP() == null){
            //default,不设置
        }
        else{
            holder.pic.setImageBitmap(friend.getPersoninformObj().getPortraitBMP());
        }
        holder.name.setText(friend.getPersoninformObj().getName());

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(position));
    }


    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class friendVH extends RecyclerView.ViewHolder{

        @BindView(R.id.friend_list_header)
        ImageView pic;

        @BindView(R.id.friend_list_name)
        EditText name;


        public friendVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
