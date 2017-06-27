package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.viewholder.FullScreenGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsListAdapter;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/14-下午3:36.
 * 功能描述: 店铺商品列表适配器
 */
public class StoreGoodsListAdapter extends RecyclerView.Adapter<FullScreenGoodsViewHolder> {

    private Context mContext;
    private List<GoodsList> list;

    public StoreGoodsListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<GoodsList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<GoodsList> alist) {
        if (alist != null && alist.size() > 0) {
            if (list != null) {
                list.addAll(alist);
                notifyDataSetChanged();
            }
        }
    }

    public GoodsList getItem(int position) {
        return list.get(position);
    }

    @Override
    public FullScreenGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_fullscreen_goods, null);
        return new FullScreenGoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FullScreenGoodsViewHolder holder, int position) {
        final GoodsList goodsList = list.get(position);
        holder.setModelData(mContext, position, goodsList, listener);
    }

    private GoodsListAdapter.GoodsListCallBack listener;

    public void setListener(GoodsListAdapter.GoodsListCallBack l) {
        this.listener = l;
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }
}
