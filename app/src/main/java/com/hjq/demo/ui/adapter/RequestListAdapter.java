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
import com.hjq.demo.domain.user.Request;
import com.hjq.demo.domain.user.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.requestVH>{

    List<Request> requestList;

    ItemClickListener itemClickListener;

    public RequestListAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public requestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new requestVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull requestVH holder, int position) {
        Request request = requestList.get(position);

        //todo
        //
        if(request.getPortraitBMP() == null){
            //default
        }
        else{
            holder.pic.setImageBitmap(request.getPortraitBMP());
        }
        holder.name.setText(request.getName());
        holder.content.setText(request.getContent());
        holder.time.setText(request.getSendtime());

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(position));
    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class requestVH extends RecyclerView.ViewHolder{

        @BindView(R.id.request_list_header)
        ImageView pic;

        @BindView(R.id.request_list_name)
        EditText name;

        @BindView(R.id.request_list_content)
        EditText content;

        @BindView(R.id.request_list_time)
        EditText time;

        public requestVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
