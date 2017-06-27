package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.viewholder.FullScreenGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/8.
 * Modify by lizhixin on 2015/12/18.
 */
public class GoodsListAdapter extends RecyclerView.Adapter<FullScreenGoodsViewHolder> {

    private Context mContext;
    private List<GoodsList> list = new ArrayList<>();

    public GoodsListAdapter(Context c) {
        mContext = c;
    }

    public void setList(List<GoodsList> lst) {
        list = lst;
        notifyDataSetChanged();
    }

    public void Clear() {
        this.list.clear();
    }

    public void addList(List<GoodsList> lst) {
        if (lst != null && lst.size() > 0) {
            if (list != null) {
                list.addAll(lst);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public void onBindViewHolder(FullScreenGoodsViewHolder holder, final int position) {
        final GoodsList goodsList = list.get(position);
        holder.setModelData(mContext, position, goodsList, listener);
    }

    @Override
    public FullScreenGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_fullscreen_goods, null);
        return new FullScreenGoodsViewHolder(view);
    }

    private GoodsListCallBack listener;

    public void setListener(GoodsListCallBack l) {
        this.listener = l;
    }

    public interface GoodsListCallBack {
        void onItemClick(GoodsList c1, int position);
    }
}
