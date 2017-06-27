package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import java.text.DecimalFormat;

/**
 * 作者： Created by Plucky on 15-10-5.
 * modify by lizhiixn on 15-12-21
 */
public class GoodsRecommendAdapter extends GoodsSchemeAdapter {

    public GoodsRecommendAdapter (Context c) {
        super (c);
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_goods_recommend, null);
            new ViewHolder (convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag ();
        String goods_img = getItem (position).goods_image;

        new FHImageViewUtil (holder.fv).setImageURI(goods_img, FHImageViewUtil.SHOWTYPE.DEFAULT);

        //国旗及描述行 添加标签后缀
        holder.tv_desc.setText(getItem(position).goods_promise);
        new FHImageViewUtil(holder.sdv).setImageURI(getItem(position).nation_flag, FHImageViewUtil.SHOWTYPE.DEFAULT);

        DecimalFormat df = new DecimalFormat ("¥#0.00");
        String fmt = mContext.getString(R.string.yuan_);

        //商品描述
        holder.tv_title.setText (getItem (position).getGoodsName(mContext));
        BaseFunc.setFont(holder.tv_title);

        //全球购标志
        if (getItem(position).goods_source > 0) {
            holder.iv_flag_global.setVisibility(View.VISIBLE);
        } else {
            holder.iv_flag_global.setVisibility(View.GONE);
        }

        //已售罄、已下架等透明图片
        holder.iv_top.setImageResource(getItem(position).getGoodsSaleState());

        holder.tv_price.setText (df.format (getItem (position).goods_price));
        holder.tv_memo_1.setText (String.format(fmt, getItem (position).goods_marketprice));
        holder.tv_memo_1.getPaint ().setFlags (Paint.STRIKE_THRU_TEXT_FLAG);

        //不再显示折扣
        /*double discount = (getItem (position).goods_price / getItem (position).goods_marketprice) * 10;
        if (discount < 10.0) {
            holder.tv_memo.setText (discountfmt.format (discount));
        } else {
            holder.tv_memo.setText ("");
        }*/


        if (position == getCount () - 1) {
            holder.vline.setVisibility (View.GONE);
        } else {
            holder.vline.setVisibility (View.VISIBLE);
        }

        convertView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                BaseFunc.gotoGoodsDetail(mContext,getItem (position).goods_id,null,null);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public ImageView fv;
        public LinearLayout LGlobal;
        public ImageView sdv, iv_flag_global, iv_top;
        public TextView tv_desc;
        public TextView tv_title;
        public TextView tv_price;
        public TextView tv_memo;//不再显示折扣
        public TextView tv_memo_1;
        public View vline;

        public ViewHolder (View v) {
            fv = (ImageView) v.findViewById (R.id.fv);
            LGlobal = (LinearLayout) v.findViewById (R.id.LGlobal);
            sdv = (ImageView) v.findViewById (R.id.sdv);
            tv_desc = (TextView) v.findViewById (R.id.tv_desc);
            tv_title = (TextView) v.findViewById (R.id.tv_title);
            tv_price = (TextView) v.findViewById (R.id.tv_price);
            //tv_memo = (TextView) v.findViewById (R.id.tv_memo);
            tv_memo_1 = (TextView) v.findViewById (R.id.tv_memo_1);
            vline = v.findViewById (R.id.vline);
            iv_flag_global = (ImageView) v.findViewById(R.id.iv_flag_global);
            iv_top = (ImageView) v.findViewById(R.id.iv_top);
            v.setTag (this);
        }
    }
}
