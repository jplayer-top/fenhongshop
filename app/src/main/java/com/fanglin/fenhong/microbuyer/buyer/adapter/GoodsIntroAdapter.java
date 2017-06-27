package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.GoodsIntro;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/29-下午3:34.
 * 功能描述: 商品说明适配器
 */
public class GoodsIntroAdapter extends BaseAdapter {
    private List<GoodsIntro> list;
    private Context mContext;

    public GoodsIntroAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<GoodsIntro> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public GoodsIntro getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_goods_intro, null);
            new ItemViewHolder(convertView);
        }

        ItemViewHolder holder = (ItemViewHolder) convertView.getTag();
        final GoodsIntro intro = getItem(position);
        holder.tvTitle.setText(intro.getTitle());
        holder.tvDesc.setText(intro.getDescription());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(intro.getUrl())) {
                    BaseFunc.urlClick(mContext, intro.getUrl());
                }
            }
        });

        return convertView;
    }


    class ItemViewHolder {
        TextView tvTitle;
        TextView tvDesc;

        public ItemViewHolder(View itemView) {
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            itemView.setTag(this);
        }
    }
}
