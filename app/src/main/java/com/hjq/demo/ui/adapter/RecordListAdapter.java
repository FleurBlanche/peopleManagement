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
import com.hjq.demo.domain.relation.Record;
import com.hjq.demo.domain.user.User;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.recordVH>{

    List<Record> recordList;

    ItemClickListener itemClickListener;

    public RecordListAdapter(List<Record> recordList) {
        this.recordList = recordList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public recordVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new recordVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull recordVH holder, int position) {

        Record record = recordList.get(position);

        //todo
        //set data
        holder.pic.setImageBitmap(record.getImageBMP());
        holder.content.setText(record.getText());
        holder.time.setText(record.getRecordtime());

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public static class recordVH extends RecyclerView.ViewHolder{

        @BindView(R.id.record_list_pic)
        ImageView pic;

        @BindView(R.id.record_list_content)
        TextView content;

        @BindView(R.id.record_list_time)
        TextView time;

        public recordVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
