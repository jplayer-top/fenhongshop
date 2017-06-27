package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.BaseGoods;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/8/15-下午5:58.
 * 功能描述: 不支持配送商品
 */
public class NotSupportGoodsAdapter extends BaseAdapter {

    private Context mContext;
    private List<BaseGoods> list;

    public NotSupportGoodsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    public void setList(List<BaseGoods> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public BaseGoods getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_notsupport_goods, null);
            new ItemViewHolder(convertView);
        }

        ItemViewHolder holder = (ItemViewHolder) convertView.getTag();
        BaseGoods goods = getItem(position);
        holder.tvName.setText(goods.goods_name);
        new FHImageViewUtil(holder.imageView).setImageURI(goods.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
        holder.tvDesc.setText(goods.getGoodsNumDesc());
        return convertView;
    }

    class ItemViewHolder {
        ImageView imageView;
        TextView tvName, tvDesc;

        public ItemViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.imageView);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            view.setTag(this);
        }
    }
}
