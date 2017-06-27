package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeConsume;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyConsumeListViewHolder;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/8-上午10:24.
 * 功能描述: 极速免税 消费明细列表
 */
public class DutyConsumeListAdapter extends RecyclerView.Adapter<DutyConsumeListViewHolder> {

    private Context mContext;
    private List<DutyfreeConsume> list;

    public DutyConsumeListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<DutyfreeConsume> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<DutyfreeConsume> list) {
        if (list != null && list.size() > 0) {
            if (this.list != null) {
                this.list.addAll(list);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public DutyfreeConsume getItem(int position) {
        return list.get(position);
    }

    @Override
    public DutyConsumeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return DutyConsumeListViewHolder.getHolder(mContext);
    }

    @Override
    public void onBindViewHolder(DutyConsumeListViewHolder holder, int position) {
        DutyfreeConsume consume = getItem(position);
        holder.refreshView(consume);
    }
}
