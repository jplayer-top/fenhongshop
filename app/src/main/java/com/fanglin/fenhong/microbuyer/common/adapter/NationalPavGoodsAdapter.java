package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.viewholder.HalfScreenGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.base.model.NationalPavGoodsEntity;

import java.util.ArrayList;

/**
 * 国家馆 —— 商品列表 —— 数据适配器
 * Created by lizhixin on 2015/11/27.
 */
public class NationalPavGoodsAdapter extends RecyclerView.Adapter<HalfScreenGoodsViewHolder> {

    private Context context;
    private ArrayList<NationalPavGoodsEntity> list;
    private String resource_tags;//统计用

    public NationalPavGoodsAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<NationalPavGoodsEntity> list, String resource_tags) {
        this.resource_tags = resource_tags;
        this.list = list;
    }

    public void addList(ArrayList<NationalPavGoodsEntity> list, String resource_tags) {
        this.resource_tags = resource_tags;
        if (list != null && list.size() > 0) {
            if (this.list != null) {
                this.list.addAll(list);
            }
        }
    }

    @Override
    public HalfScreenGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_halfscreen_goods, null);

        return new HalfScreenGoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HalfScreenGoodsViewHolder holder, int position) {
        final NationalPavGoodsEntity goodsEntity = list.get(position);
        holder.setModelData(context, goodsEntity);
        holder.setResource_tags(resource_tags);
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public int[] getOffsets(int pos) {
        //left top right bottom
        int[] res = new int[]{0, 0, 0, 0};
        int span = 2;
        int count = getItemCount();
        // left：第一列 6px
        if (pos % span == 0) {
            res[0] = 6;
        }
        // top：第一行 6px
        if (pos < span) {
            res[1] = 6;
        }
        // right: 最后一列 6px
        if (pos % span == span - 1) {
            res[2] = 6;
        }
        // bottom: 最后一行 6px
        int line = (count % span == 0) ? count / span : count / span + 1;
        int lastIndex = (line - 1) * span - 1;
        if (pos > lastIndex) {
            res[3] = 6;
        }

        return res;
    }
}
