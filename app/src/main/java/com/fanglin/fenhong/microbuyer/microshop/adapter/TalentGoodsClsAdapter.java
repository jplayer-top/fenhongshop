package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.GoodsClass;

import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/22-上午9:37.
 * 功能描述: 达人推荐商品分类
 */
public class TalentGoodsClsAdapter extends RecyclerView.Adapter<TalentGoodsClsAdapter.ItemViewHolder> {

    private Context mContext;
    private List<GoodsClass> list;
    private int curPos = 0;

    private GoodsClass goodsClassDef;

    public TalentGoodsClsAdapter(Context mContext) {
        goodsClassDef = new GoodsClass();
        goodsClassDef.gc_id = null;
        goodsClassDef.gc_name = "全部商品";

        this.mContext = mContext;
    }

    public void setList(List<GoodsClass> lst) {
        this.list = new ArrayList<>();
        this.list.add(goodsClassDef);
        if (lst != null && lst.size() > 0) {
            this.list.addAll(lst);
        }
        notifyDataSetChanged();
    }

    public GoodsClass getItem(int position) {
        return list.get(position);
    }

    public void setCurPos(int curPos) {
        this.curPos = curPos;
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_talentgoods_cls, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        GoodsClass goodsClass = getItem(position);
        holder.tvName.setText(goodsClass.gc_name);
        holder.tvName.setSelected(curPos == position);
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }
    }

    private TalentGoodsClsAdapterListener listener;

    public void setListener(TalentGoodsClsAdapterListener listener) {
        this.listener = listener;
    }

    public interface TalentGoodsClsAdapterListener {
        void onItemClick(int position);
    }
}
