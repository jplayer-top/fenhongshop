package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeMessagesBean;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyfreePersonalMessagesHeaderHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyfreePersonalMessagesItemHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Oblivion on 2017/1/9.
 * 功能描述:
 * Change By Someone (You Should Mark in Here)
 */
public class DutyfreePersonalMessagesAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder, RecyclerView.ViewHolder> {
    private Context mContext;
    private static final int TYPE_HEADER1 = 0;
    private static final int TYPE_HEADER2 = 1;
    private static final int TYPE_ITEM1 = 2;
    private static final int TYPE_ITEM2 = 3;

    public DutyfreePersonalMessagesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 分区数目--拥有几个分局
     *
     * @return
     */
    @Override
    protected int getSectionCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        } else {
            return 0;
        }
    }

    /**
     * 每个分区下所拥有的条目,设定每个分区下都有一个条目
     *
     * @param section
     * @return
     */
    @Override
    protected int getItemCountForSection(int section) {
        return 1;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return DutyfreePersonalMessagesHeaderHolder.getHolder(mContext);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return DutyfreePersonalMessagesItemHolder.getHodler(mContext);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        //每一个分区都一样
        DutyfreePersonalMessagesHeaderHolder headerHolder = (DutyfreePersonalMessagesHeaderHolder) holder;
        headerHolder.bind2Holder(data.get(section));
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {
        DutyfreePersonalMessagesItemHolder itemHolder = (DutyfreePersonalMessagesItemHolder) holder;
        itemHolder.bindData2ItemHolder(data.get(section));
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }


    private List<DutyfreeMessagesBean> data;

    public void setRequData(List<DutyfreeMessagesBean> data) {
        if (data != null) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public void addRequData(List<DutyfreeMessagesBean> data) {
        if (data != null) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }
}
