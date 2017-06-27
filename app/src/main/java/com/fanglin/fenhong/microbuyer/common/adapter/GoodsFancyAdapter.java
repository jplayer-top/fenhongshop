package com.fanglin.fenhong.microbuyer.common.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.UpdateAction;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import java.text.DecimalFormat;

/**
 * 作者： Created by Plucky on 2015/10/12.
 * modify by lizhixin on 2015/12/21
 */
public class GoodsFancyAdapter extends GoodsSchemeAdapter {

    public GoodsFancyAdapter (Context c) {
        super (c);
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_fancy, null);
            new ViewHolder (convertView);
        }

        int w = BaseFunc.getDisplayMetrics (mContext).widthPixels;
        int h = w * 540 / 1080;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams (w, h);

        ViewHolder holder = (ViewHolder) convertView.getTag ();

        //已售罄、已下架等透明图片
        holder.fiv_pic.setLayoutParams(params);
        new FHImageViewUtil (holder.fiv_pic).setImageURI(getItem(position).goods_pic, FHImageViewUtil.SHOWTYPE.REC);
        holder.iv_top.setImageResource(getItem(position).getGoodsSaleState());

        //国旗及描述行 添加标签后缀
        holder.tv_global.setText(getItem(position).goods_promise);
        new FHImageViewUtil(holder.iv_flag).setImageURI(getItem(position).nation_flag, FHImageViewUtil.SHOWTYPE.DEFAULT);

        //商品描述
        holder.tv_title.setText (getItem (position).getGoodsName(mContext));
        BaseFunc.setFont(holder.tv_title);

        //全球购标志
        if (getItem(position).goods_source > 0) {
            holder.iv_flag_global.setVisibility(View.VISIBLE);
        } else {
            holder.iv_flag_global.setVisibility(View.GONE);
        }

        DecimalFormat df = new DecimalFormat ("¥#0.00");
        holder.tv_price.setText (df.format (getItem (position).goods_price));
        BaseFunc.setFont (holder.LIcon);

        convertView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                BaseFunc.gotoGoodsDetail(mContext,getItem (position).goods_id,null,null);
            }
        });

        holder.tv_addcart.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                UpdateAction.add2Cart ((Activity) mContext, getItem (position).goods_id, 1, getItem (position).goods_source);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        public ImageView fiv_pic;
        public LinearLayout LGlobal;
        public ImageView iv_flag, iv_top, iv_flag_global;
        public TextView tv_global;
        public TextView tv_title;
        public TextView tv_price;
        public TextView tv_memo;
        public TextView tv_addcart;
        public LinearLayout LIcon;

        public ViewHolder (View v) {
            fiv_pic = (ImageView) v.findViewById (R.id.fiv_pic);
            LGlobal = (LinearLayout) v.findViewById (R.id.LGlobal);
            iv_flag = (ImageView) v.findViewById (R.id.iv_flag);
            iv_top = (ImageView) v.findViewById (R.id.iv_top);
            iv_flag_global = (ImageView) v.findViewById (R.id.iv_flag_global);
            tv_global = (TextView) v.findViewById (R.id.tv_global);
            tv_title = (TextView) v.findViewById (R.id.tv_title);
            tv_price = (TextView) v.findViewById (R.id.tv_price);
            tv_memo = (TextView) v.findViewById (R.id.tv_memo);
            tv_addcart = (TextView) v.findViewById (R.id.tv_addcart);
            LIcon = (LinearLayout) v.findViewById (R.id.LIcon);
            v.setTag (this);
        }
    }
}
