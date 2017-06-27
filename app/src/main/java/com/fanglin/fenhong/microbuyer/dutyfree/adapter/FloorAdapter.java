package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeCategory;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.FloorViewHolderListener;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.FloorItemViewHolder;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-下午5:49.
 * 功能描述: 楼层适配器
 */
public class FloorAdapter extends RecyclerView.Adapter<FloorItemViewHolder> {

    private Context context;
    private int index;
    private List<DutyfreeCategory> categories;

    public FloorAdapter(Context context) {
        this.context = context;
    }

    public void setCategories(List<DutyfreeCategory> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (categories == null)
            return 0;
        return categories.size();
    }

    @Override
    public FloorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return FloorItemViewHolder.getHolder(context);
    }

    @Override
    public void onBindViewHolder(FloorItemViewHolder holder, final int position) {
        DutyfreeCategory category = categories.get(position);
        holder.tvName.setText(category.getTypeName());
        holder.tvName.setSelected(position == index);
        holder.vLine.setSelected(position == index);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onFloorClick(position);
                }
            }
        });
    }

    private FloorViewHolderListener listener;

    public void setListener(FloorViewHolderListener listener) {
        this.listener = listener;
    }
}
