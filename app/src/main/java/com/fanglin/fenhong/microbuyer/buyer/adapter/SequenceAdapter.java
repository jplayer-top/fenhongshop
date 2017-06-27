package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.SequenceModel;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/7-下午2:23.
 * 功能描述:
 */
public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.ViewHolder> {

    List<SequenceModel> list;
    private Context mContext;
    private int curindex = -1;

    public SequenceAdapter(Context c) {
        this.mContext = c;
    }

    public void setList(List<SequenceModel> list) {
        this.list = list;
        curindex = getOnSalePos();
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public SequenceModel getItem(int position) {
        return list.get(position);
    }

    public void setSelected(int position) {
        curindex = position;
        notifyDataSetChanged();
    }

    public int getOnSalePos() {
        if (getItemCount() == 0) return 0;
        for (int i = 0; i < list.size(); i++) {
            if (getItem(i).if_current == 1) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.LSeq.setSelected(curindex == position);
        holder.tv_status.setText(getItem(position).getSeqStatus());
        if (TextUtils.isEmpty(getItem(position).sequence_title)) {
            holder.tv_title.setText(getItem(position).sequence_time);
        } else {
            holder.tv_title.setText(getItem(position).sequence_title);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcb != null) mcb.onItemClick(position);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_sequence, null);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout LSeq;
        TextView tv_title, tv_status;

        public ViewHolder(View itemView) {
            super(itemView);
            LSeq = (LinearLayout) itemView.findViewById(R.id.LSeq);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
        }
    }

    public interface onItemClickInterface {
        void onItemClick(int position);
    }

    onItemClickInterface mcb;

    public void setCallBack(onItemClickInterface callBack) {
        this.mcb = callBack;
    }

}
